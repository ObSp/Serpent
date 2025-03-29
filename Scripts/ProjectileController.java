package Scripts;

import java.util.ArrayList;

import Classes.Signals;
import Classes.Util;
import Classes.Values;
import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.JGame.Classes.UI.UIImage;
import JGamePackage.JGame.Classes.World.Box2D;
import JGamePackage.JGame.Classes.World.Image2D;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class ProjectileController extends WritableScript {

    private ArrayList<Projectile> activeProjectiles = new ArrayList<>();

    private Image2D player;
    private Box2D projectileNode;

    private Vector2 calculateDirection(Vector2 mouse, Vector2 origin) {
        return mouse.subtract(origin).normalized();
    }
    
    @Override
    public void Start() {
        player = world.GetChild("Ship");
        projectileNode = world.GetChild("ProjectileNode");
        Signals.FireBullet.Connect(this::fireProjectile);
    }

    @Override
    public void Tick(double dt) {
        synchronized (activeProjectiles) {
            for (Projectile bullet : activeProjectiles) {
                bullet.model.Position = bullet.model.Position.add(bullet.direction);
            }
        }
    }

    private void fireProjectile() {
        Vector2 mousePos = game.UINode.<UIImage>GetChild("Cursor").Position.ToVector2FromAbsolute();

        Projectile bullet = new Projectile(calculateDirection(mousePos, player.GetRenderPosition().add(player.Size.divide(2))).multiply(Values.BULLET_VELOCITY), 100);
        bullet.buildModel(player.Position);
        bullet.model.Rotation = Util.lookAt(bullet.model.GetRenderPosition(), mousePos);
        bullet.model.SetParent(projectileNode);
        activeProjectiles.add(bullet);
    }

    public class Projectile {
        public Vector2 direction;
        public double damage;
        public Box2D model;

        public Projectile(Vector2 direction, double damage) {
            this.direction = direction;
            this.damage = damage;
        }

        public void buildModel(Vector2 origin) {
            model = new Box2D();
            model.Size = Values.BULLET_SIZE;
            model.FillColor = Values.BULLET_COLOR;
            model.Position = origin;
        }
    }
}

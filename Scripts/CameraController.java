package Scripts;

import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.JGame.Classes.World.Image2D;
import JGamePackage.JGame.Types.PointObjects.Vector2;


public class CameraController extends WritableScript {

    Image2D ship;
    
    @Override
    public void Start() {
        ship = game.WorldNode.<Image2D>GetChild("Ship");

        game.Services.InputService.OnMouseScroll.Connect(num ->{
            game.Camera.DepthFactor += ((double) num)*.1;
        });
    }

    @Override
    public void Tick(double dt) {
        if (game.TimeService.GetElapsedTicks() < 50) return;
        game.Camera.Position = game.Camera.Position.lerp(ship.Position.add(ship.<Vector2>GetCProp("Velocity").multiply(80)), .015 );
    }
}

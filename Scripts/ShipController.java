package Scripts;

import java.awt.event.KeyEvent;

import Classes.Util;
import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.JGame.Classes.World.Image2D;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class ShipController extends WritableScript {

    Image2D ship;
    Vector2 shipLookDirection;

    Vector2 shipVelocity = Vector2.zero;

    @Override
    public void Start() {
        ship = game.WorldNode.GetChild("Ship");
        ship.Pivot = Vector2.half;
    }

    @Override
    public void Tick(double dt) {
        double dtFactor = dt/game.SecondsPerTick;

        Vector2 mousePos = game.InputService.GetMouseWorldPosition();

        double rot = Util.lookAt(ship.Position, mousePos);
        if (mousePos.X > ship.Position.X) {
            rot = rot + Math.PI;
        }
        ship.Rotation = rot;
        shipLookDirection = mousePos.subtract(ship.Position).normalized();
        
        if (game.InputService.IsKeyDown(KeyEvent.VK_SPACE)) {
            shipVelocity = shipVelocity.lerp(shipLookDirection.multiply(20), .005);
        } else {
            shipVelocity = shipVelocity.lerp(Vector2.zero, .005);
        }

        ship.Position = ship.Position.lerp(ship.Position.add(shipVelocity.multiply(dtFactor)), .9);
        ship.SetCProp("Velocity", shipVelocity);
    }
}

package Scripts;

import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.JGame.Classes.World.Image2D;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class ShipController extends WritableScript {

    Image2D ship;
    Vector2 shipLookDirection;

    Vector2 shipVelocity = Vector2.zero;

    private double lookAt(Vector2 origin, Vector2 target) {
        double xDiff = target.X-origin.X;
        if (xDiff == 0){ //not sure if this can cause any potential errors, but avoids the arithmetic "tried to divide by zere/NaN" error
            return 0.0;
        }
        double yDiff = target.Y-origin.Y;
        return Math.atan(yDiff/xDiff);
    }

    @Override
    public void Start() {
        ship = game.WorldNode.GetChild("Ship");
        ship.Pivot = Vector2.half;
    }

    @Override
    public void Tick(double dt) {
        Vector2 mousePos = game.InputService.GetMouseWorldPosition();
        double rot = lookAt(ship.Position, mousePos);
        if (mousePos.X > ship.Position.X) {
            rot = rot + Math.PI;
        }
        ship.Rotation = rot;
        shipLookDirection = mousePos.subtract(ship.Position).normalized();
        shipVelocity = shipVelocity.lerp(shipLookDirection.multiply(10), .01);
        ship.Position = ship.Position.add(shipVelocity);
    }
}

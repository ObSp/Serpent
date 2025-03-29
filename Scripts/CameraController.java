package Scripts;

import Classes.Values;
import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.JGame.Classes.World.Image2D;
import JGamePackage.JGame.Types.PointObjects.Vector2;


public class CameraController extends WritableScript {

    Image2D ship;

    private Vector2 cameraShakeOffset = Vector2.zero;

    private void updateCameraShake() {
        if (game.Camera.GetCProp("Shake") == null) {
            cameraShakeOffset = Vector2.zero;
        } else {
            cameraShakeOffset = new Vector2(Math.random() * Values.CAMERA_SHAKE_FACTOR, Math.random() * Values.CAMERA_SHAKE_FACTOR);
        }
    }
    
    @Override
    public void Start() {
        ship = game.WorldNode.<Image2D>GetChild("Ship");

        game.Services.InputService.OnMouseScroll.Connect(num ->{
            game.Camera.DepthFactor += ((double) num)*.1;
        });
    }

    @Override
    public void Tick(double dt) {
        Vector2 lastCameraShake = cameraShakeOffset;

        updateCameraShake();

        if (game.TimeService.GetElapsedTicks() < 50) return;
        game.Camera.Position = game.Camera.Position.subtract(lastCameraShake).lerp(ship.Position.add(ship.<Vector2>GetCProp("Velocity").multiply(80)), .015).add(cameraShakeOffset);
    }
}

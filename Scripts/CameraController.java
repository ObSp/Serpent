package Scripts;

import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.JGame.Classes.World.Image2D;


public class CameraController extends WritableScript {

    Image2D ship;
    
    @Override
    public void Start() {
        ship = game.WorldNode.<Image2D>GetChild("Ship");
    }

    @Override
    public void Tick(double dt) {
        //game.Camera.Position = game.Camera.Position.lerp(ship.Position, .01);
    }
}

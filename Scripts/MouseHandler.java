package Scripts;

import Classes.Signals;
import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.JGame.Classes.UI.UIImage;
import JGamePackage.JGame.Types.PointObjects.UDim2;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class MouseHandler extends WritableScript {

    UIImage cursor;
    
    @Override
    public void Start() {
        cursor = new UIImage();
        cursor.Size = UDim2.fromAbsolute(40, 40);
        cursor.SetImage("Assets\\Cursor.png");
        cursor.BackgroundTransparency = 1;
        cursor.PixelPerfect = true;
        cursor.AnchorPoint = Vector2.half;
        cursor.ZIndex = 1000;
        cursor.Name = "Cursor";
        cursor.SetParent(game.UINode);

        game.InputService.OnMouse1Click.Connect(Signals.FireBullet::Fire);
    }

    @Override
    public void Tick(double dt) {
        Vector2 mousePos = game.InputService.GetMousePosition();
        cursor.Position = UDim2.fromAbsolute(mousePos.X, mousePos.Y);
    }
}

package JGamePackage.JGame.Classes.UI.Internal;

import java.awt.Cursor;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Classes.UI.UIBase;
import JGamePackage.JGame.Classes.UI.UIButton;
import JGamePackage.JGame.Classes.UI.UIButtonBase;
import JGamePackage.JGame.Classes.UI.UIImageButton;
import JGamePackage.JGame.Classes.UI.UITextButton;

public class UIButtonInternal {

    public static void updateCursorAfterLeave(JGame game) {
        UIBase target = game.InputService.GetMouseUITarget();

        if (target instanceof UIButtonBase) {
            if (target instanceof UIButton) {
                if (!((UIButton) target).Disabled) return;
            } else if (target instanceof UIImageButton) {
                if (!((UIImageButton) target).Disabled) return;
            } else if (target instanceof UITextButton) {
                if (!((UITextButton) target).Disabled) return;
            }
        }
        
        game.Services.WindowService.SetMouseCursor(Cursor.DEFAULT_CURSOR);
    }

}

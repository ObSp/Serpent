package JGamePackage.JGame.Classes.UI;

import java.awt.Color;
import java.awt.Cursor;

import JGamePackage.JGame.Classes.UI.Internal.UIButtonInternal;

public class UIImageButton extends UIImage implements UIButtonBase {
    public Color HoverColor = Color.gray;
    public boolean HoverEffectsEnabled = true;

    public boolean Disabled = false;

    private boolean isHovering = false;

    public UIImageButton() {
        super();
        
        this.MouseEnter.Connect(()-> {
            if (Disabled) return;
            this.isHovering = true;
            game.Services.WindowService.SetMouseCursor(Cursor.HAND_CURSOR);
        });

        this.MouseLeave.Connect(()-> {
            this.isHovering = false;
            UIButtonInternal.updateCursorAfterLeave(game);
        });

    }

    @Override
    public Color GetBackgroundRenderColor() {
        Color curColor = (isHovering && HoverEffectsEnabled) ? HoverColor : BackgroundColor;
        return new Color(curColor.getRed(), curColor.getGreen(), curColor.getBlue(), (int) (255*(1-BackgroundTransparency)));
    }

    @Override
    public UIImageButton Clone() {
        UIImageButton clone = cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected UIImageButton cloneWithoutChildren() {
        UIImageButton frame = new UIImageButton();
        frame.SetImage(this.imagePath);

        if (this.scale != null)
            frame.SetImageScale(scale);

        frame.HoverColor = this.HoverColor;
        frame.HoverEffectsEnabled = this.HoverEffectsEnabled;
        frame.PixelPerfect = this.PixelPerfect;
        this.cloneHelper(frame);
        
        return frame;
    }
}

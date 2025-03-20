package JGamePackage.JGame.Classes.UI;

import java.awt.Color;
import java.awt.Cursor;

import JGamePackage.JGame.Classes.UI.Internal.UIButtonInternal;

public class UIButton extends UIFrame implements UIButtonBase {
    public Color HoverColor = null;
    public boolean HoverEffectsEnabled = true;

    public boolean Disabled = false;

    private boolean isHovering = false;

    public UIButton() {
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
        Color curColor = (isHovering && HoverEffectsEnabled) ? (HoverColor != null ? HoverColor : BackgroundColor.darker()) : BackgroundColor;
        return new Color(curColor.getRed(), curColor.getGreen(), curColor.getBlue(), (int) (255*(1-BackgroundTransparency)));
    }

    @Override
    public UIButton Clone() {
        UIButton clone = cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected UIButton cloneWithoutChildren() {
        UIButton frame = new UIButton();
        this.cloneHelper(frame);
        frame.HoverColor = this.HoverColor;
        frame.HoverEffectsEnabled = this.HoverEffectsEnabled;
        return frame;
    }
}

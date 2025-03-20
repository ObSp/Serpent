package JGamePackage.JGame.Classes.UI;

import java.awt.Graphics2D;

import JGamePackage.JGame.Classes.Modifiers.ListLayout;
import JGamePackage.JGame.Classes.Rendering.RenderUtil;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class UIScrollFrame extends UIBase {

    public Vector2 ScrollOffset = Vector2.zero;

    /** A value representing the maximum value of the {@code ScrollOffset} property. If set to null,
     * no maximum value will be applied.
     */
    public Vector2 MaxScrollOffset = null;

    public UIScrollFrame() {
        super();
        this.ClipsDescendants = true;

        game.InputService.OnMouseScroll.Connect(amount -> {
            if (!game.InputService.IsMouseInUIBaseBounds(this)) return;

            Vector2 listSize = new Vector2(Double.MAX_VALUE);
            ListLayout list = GetChildOfClass(ListLayout.class);
            if (list != null) {
                listSize = list.GetAbsoluteListSize().subtract(0, GetAbsoluteSize().Y);
            }


            ScrollOffset = new Vector2(0, Math.min(Math.max(ScrollOffset.Y + amount * 10, 0), listSize.Y));
            if (MaxScrollOffset != null) {
                ScrollOffset = new Vector2(0, Math.min(ScrollOffset.Y, MaxScrollOffset.Y));
            }
            ScrollOffset = new Vector2(0, Math.max(ScrollOffset.Y, 0));
        });
    }

    @Override
    public void render(Graphics2D graphics) {
        Vector2 renderPos = GetAbsolutePosition();
        Vector2 renderSize = GetAbsoluteSize();

        RenderUtil.drawRectangle(this, renderSize, renderPos, GetBackgroundRenderColor());
    }
    
    @Override
    public UIFrame Clone() {
        UIFrame clone = cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected UIFrame cloneWithoutChildren() {
        UIFrame frame = new UIFrame();
        this.cloneHelper(frame);
        return frame;
    }
}

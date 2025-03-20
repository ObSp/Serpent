package JGamePackage.JGame.Classes.World;

import java.awt.Graphics2D;

import JGamePackage.JGame.Classes.Rendering.RenderUtil;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class Box2D extends WorldBase {
    
    @Override
    public void render(Graphics2D graphics) {
        if (this.Transparency >= 1) return;

        Vector2 renderSize = GetRenderSize();
        Vector2 renderPosition = GetRenderPosition();

        if (!game.Camera.AreBoundsInCameraBounds(renderSize, renderPosition)) return;

        RenderUtil.drawRectangle(this, renderSize, renderPosition, GetRenderColor());
    }

    @Override
    public Box2D Clone() {
        Box2D clone = cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected Box2D cloneWithoutChildren() {
        Box2D frame = new Box2D();
        this.cloneHelper(frame);
        return frame;
    }
}

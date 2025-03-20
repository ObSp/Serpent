package JGamePackage.JGame.Classes.World;

import java.awt.Color;

import JGamePackage.JGame.Classes.Modifiers.AspectRatioConstraint;
import JGamePackage.JGame.Classes.Rendering.Renderable;
import JGamePackage.JGame.Types.Constants.Constants;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public abstract class WorldBase extends Renderable{

    /**The position of the object, measured in pixels.
     * 
     */
    public Vector2 Position = Vector2.zero;

    /**The size of the object, measured in pixels.
     * 
     */
    public Vector2 Size = new Vector2(100);

    /**Where the object's "center" will be when rendered. Some common values are: <p>
     * {@code (0,0)} - The top left corner of the object, <p>
     * {@code (.5,.5)} - The center of the object,<p>
     * {@code (1,1)} - The bottom right corner of the object.<p>
     * 
     */
    public Vector2 Pivot = new Vector2(0);

    /**The background color of the object.
     * 
     */
    public Color FillColor = Color.white;

    /**Whether or not the object will be rendered.
     * 
     */
    public boolean Visible = true;

    /**The transparency of the object.
     * 
     */
    public double Transparency = 0;


    //--PIVOTS--//
    protected double getHorizontalPivotOffset() {
        return Size.X*Pivot.X;
    }

    protected double getVerticalPivotOffset() {
        return Size.Y*Pivot.Y;
    }

    public Vector2 GetPivotOffset() {
        return new Vector2(getHorizontalPivotOffset(), getVerticalPivotOffset());
    }

    //--RENDER STUFF--//
    public Vector2 GetRenderPosition() {
        return game.Camera.GetWorldBaseRenderPosition(this);
    }

    public Vector2 GetRenderSize() {
        AspectRatioConstraint aspectConstr = this.GetChildOfClass(AspectRatioConstraint.class);
        Vector2 realSize = game.Camera.GetWorldBaseRenderSize(this);

        if (aspectConstr != null) {
            if (aspectConstr.DominantAxis == Constants.Vector2Axis.X) {
                realSize = new Vector2(realSize.X, realSize.X/aspectConstr.AspectRatio);
            } else {
                realSize = new Vector2(realSize.Y*aspectConstr.AspectRatio, realSize.Y);
            }
        }

        return realSize;
    }

    protected Color GetRenderColor() {
        return new Color(FillColor.getRed(), FillColor.getGreen(), FillColor.getBlue(), (int) (255*(1-Transparency)));
    }

    protected void cloneHelper(WorldBase base) {
        base.Pivot = this.Pivot;
        base.FillColor = this.FillColor;
        base.Transparency = this.Transparency;
        base.Name = this.Name;
        base.Position = this.Position;
        base.Rotation = this.Rotation;
        base.Size = this.Size;
        base.Visible = this.Visible;
        base.ZIndex = this.ZIndex;
        base.MouseTargetable = this.MouseTargetable;
    }
}

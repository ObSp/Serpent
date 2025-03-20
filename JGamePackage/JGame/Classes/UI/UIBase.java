package JGamePackage.JGame.Classes.UI;

import java.awt.Color;

import JGamePackage.JGame.Classes.Instance;
import JGamePackage.JGame.Classes.Modifiers.AspectRatioConstraint;
import JGamePackage.JGame.Classes.Modifiers.ListLayout;
import JGamePackage.JGame.Classes.Rendering.Renderable;
import JGamePackage.JGame.Classes.UI.Internal.UIBaseInternal;
import JGamePackage.JGame.Types.Constants.Constants;
import JGamePackage.JGame.Types.PointObjects.UDim2;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public abstract class UIBase extends Renderable {

    /**The position of the object in 2D space, represented by a UDim2.
     * 
     */
    public UDim2 Position = UDim2.zero;

    /**The size of the object in 2D space, represented by a UDim2.
     * 
     */
    public UDim2 Size = UDim2.fromScale(.1, .1);

    /**Determines what point of the UIBase will be rendered at the {@code UIBase.Position} <p>
     * {@code (0,0)} - The top left corner of the object, <p>
     * {@code (.5,.5)} - The center of the object,<p>
     * {@code (1,1)} - The bottom right corner of the object.<p>
     * 
     */
    public Vector2 AnchorPoint = Vector2.zero;

    /**The background color of the object.
     * 
     */
    public Color BackgroundColor = Color.white;

    /**The transparency of the background of the object.
     * 
     */
    public double BackgroundTransparency = 0;

    /**Whether or not the object and its descenants will be rendered.
     * 
     */
    public boolean Visible = true;

    /**If true, creates a clipping area the size of this UIBase, such that it cuts of any pixels outside of the clipping area.
     * 
     */
    public boolean ClipsDescendants = false;

    /**Whether or not the object can be detected as the current mouse target.
     * If set to false, the mouse will ignore this object, effectively making it
     * "pass through."
     * 
     */
    public boolean MouseTargetable = true;

    //--ABSOLUTE STUFF--//
    public Vector2 GetAbsolutePosition() {
        Instance parentInstance = this.GetParent();

        Vector2 realPos = null;

        if (parentInstance == null) 
            return Position.ToVector2(game.Services.WindowService.GetWindowSize()).subtract(GetAnchorPointOffset());

        ListLayout layout = parentInstance.GetChildOfClass(ListLayout.class);
        Vector2 scrollOffset = Vector2.zero;
        if (parentInstance instanceof UIScrollFrame) {
            scrollOffset = ((UIScrollFrame) parentInstance).ScrollOffset;
        }

        if (layout != null) {
            realPos = UIBaseInternal.computePositionWithUIList(this, layout, game, scrollOffset);
        } else {
            if (!(parentInstance instanceof UIBase)) {
                realPos = Position.ToVector2(game.Services.WindowService.GetWindowSize()).subtract(GetAnchorPointOffset());
            } else {
                realPos = Position.ToVector2(((UIBase) parentInstance).GetAbsoluteSize()).add(((UIBase) parentInstance).GetAbsolutePosition()).subtract(GetAnchorPointOffset()).add(scrollOffset);
            }
        }

        return realPos;
    }

    public Vector2 GetAbsoluteSize() {
        Instance parentInstance = this.GetParent();
        Vector2 realSize = null;
        
        if (!(parentInstance instanceof UIBase)) {
            realSize = Size.ToVector2(game.Services.WindowService.GetWindowSize());
        } else {
            realSize = Size.ToVector2(((UIBase) parentInstance).GetAbsoluteSize());
        }

        AspectRatioConstraint aspectConstr = this.GetChildOfClass(AspectRatioConstraint.class);

        if (aspectConstr != null) {
            if (aspectConstr.DominantAxis == Constants.Vector2Axis.X) {
                realSize = new Vector2(realSize.X, realSize.X/aspectConstr.AspectRatio);
            } else {
                realSize = new Vector2(realSize.Y*aspectConstr.AspectRatio, realSize.Y);
            }
        }

        return realSize;
    }

    public double GetAbsoluteRotation() {
        Instance parentInstance = this.GetParent();
        if (!(parentInstance instanceof UIBase)) return Rotation;

        return Rotation + ((UIBase) parentInstance).GetAbsoluteRotation();
    }

    //--ANCHOR POINTS--//
    protected double getHorizontalAnchorOffset() {
        return GetAbsoluteSize().X*AnchorPoint.X;
    }

    protected double getVerticalAnchorOffset() {
        return GetAbsoluteSize().Y*AnchorPoint.Y;
    }

    public Vector2 GetAnchorPointOffset() {
        return new Vector2(getHorizontalAnchorOffset(), getVerticalAnchorOffset());
    }


    protected Color GetBackgroundRenderColor() {
        return new Color(BackgroundColor.getRed(), BackgroundColor.getGreen(), BackgroundColor.getBlue(), (int) (255*(1-BackgroundTransparency)));
    }


    //Internal method for UIBase instances to duplicate their basic UIBase properties
    protected void cloneHelper(UIBase base) {
        base.AnchorPoint = this.AnchorPoint;
        base.BackgroundColor = this.BackgroundColor;
        base.BackgroundTransparency = this.BackgroundTransparency;
        base.Name = this.Name;
        base.Position = this.Position;
        base.Rotation = this.Rotation;
        base.Size = this.Size;
        base.Visible = this.Visible;
        base.ZIndex = this.ZIndex;
        base.MouseTargetable = this.MouseTargetable;
    }

    public abstract UIBase Clone();
}

package JGamePackage.JGame.Classes.Modifiers;

import JGamePackage.JGame.Types.Constants.Constants;

public class CornerEffect extends Modifier {
    public double Radius = .1;

    /**The {@code Vector2Axis} that the arc size of the UICorner is dependant on.
     * 
     */
    public Constants.Vector2Axis RelativeTo = Constants.Vector2Axis.Y;

    @Override
    public CornerEffect Clone() {
        CornerEffect clone = this.cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected CornerEffect cloneWithoutChildren() {
        CornerEffect clone = new CornerEffect();
        clone.Radius = this.Radius;
        clone.RelativeTo = this.RelativeTo;
        return clone;
    }
}

package JGamePackage.JGame.Classes.Modifiers;

import JGamePackage.JGame.Types.Constants.Constants;

public class AspectRatioConstraint extends Modifier {
    public double AspectRatio = 1;

    public Constants.Vector2Axis DominantAxis = Constants.Vector2Axis.Y;

    @Override
    public AspectRatioConstraint Clone() {
        AspectRatioConstraint clone = this.cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected AspectRatioConstraint cloneWithoutChildren() {
        AspectRatioConstraint clone = new AspectRatioConstraint();
        clone.AspectRatio = this.AspectRatio;
        clone.DominantAxis = this.DominantAxis;
        return clone;
    }
}

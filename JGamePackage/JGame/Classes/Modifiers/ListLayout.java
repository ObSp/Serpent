package JGamePackage.JGame.Classes.Modifiers;

import JGamePackage.JGame.Classes.UI.UIBase;
import JGamePackage.JGame.Types.Constants.Constants;
import JGamePackage.JGame.Types.PointObjects.UDim2;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class ListLayout extends Modifier {
    public Constants.ListAlignment ItemAlignment = Constants.ListAlignment.Vertical;

    public UDim2 Padding = UDim2.fromAbsolute(5, 5);

    public Vector2 GetAbsoluteListSize() {
        if (!(GetParent() instanceof UIBase)) return Vector2.zero;

        Vector2 paddingVec2 = Padding.ToVector2(this.<UIBase>GetParent().GetAbsoluteSize());
        Vector2 curSize = new Vector2(0);

        for (UIBase v : GetParent().GetChildrenOfClass(UIBase.class)) {
            Vector2 absSize = v.GetAbsoluteSize();
            double newX = curSize.X;
            if (absSize.X > curSize.X) {
                newX = absSize.X;
            }

            curSize = new Vector2(newX, curSize.Y + absSize.Y + paddingVec2.Y);
        }

        return curSize;
    }

    @Override
    public ListLayout Clone() {
        ListLayout clone = this.cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected ListLayout cloneWithoutChildren() {
        ListLayout clone = new ListLayout();
        clone.Padding = this.Padding;
        clone.ItemAlignment = this.ItemAlignment;
        return clone;
    }
}

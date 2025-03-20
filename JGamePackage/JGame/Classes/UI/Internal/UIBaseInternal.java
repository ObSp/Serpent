package JGamePackage.JGame.Classes.UI.Internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Classes.Modifiers.ListLayout;
import JGamePackage.JGame.Classes.UI.UIBase;
import JGamePackage.JGame.Types.Constants.Constants;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class UIBaseInternal {

    public static Vector2 computePositionWithUIList(UIBase inst, ListLayout layout, JGame game, Vector2 offset) {
        UIBase[] childrenOfClass = inst.GetParent().GetChildrenOfClass(UIBase.class);
        Vector2 paddingVec2 = layout.Padding.ToVector2(((UIBase) inst.GetParent()).GetAbsoluteSize());
        boolean isHorizontal = layout.ItemAlignment == Constants.ListAlignment.Horizontal;

        List<UIBase> asList = new ArrayList<>(Arrays.asList(childrenOfClass));
        for (int i = asList.size() - 1; i >= 0; i --) {
            if (!asList.get(i).Visible) {
                asList.remove(i);
            }
        }

        int posInChildren = asList.indexOf(inst);
        if (posInChildren == -1) {
            return new Vector2(-1000, -1000);
        }

        if (posInChildren == 0) {
            return ((UIBase) inst.GetParent()).GetAbsolutePosition().subtract(offset.multiply(2)).subtract(inst.GetAnchorPointOffset());
        } else {
            UIBase childBefore = childrenOfClass[posInChildren - 1];
            Vector2 absSize = childBefore.GetAbsoluteSize();

            return childBefore.GetAbsolutePosition().add(isHorizontal ? absSize.X : 0, !isHorizontal ? absSize.Y : 0).add(paddingVec2.multiply(isHorizontal ? 1 : 0, !isHorizontal ? 1 : 0));
        }
    }

    

}

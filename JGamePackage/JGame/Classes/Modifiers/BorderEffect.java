package JGamePackage.JGame.Classes.Modifiers;

import java.awt.Color;

public class BorderEffect extends Modifier {
    public int Width = 5;

    public Color BorderColor = Color.black;

    @Override
    public BorderEffect Clone() {
        BorderEffect clone = this.cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected BorderEffect cloneWithoutChildren() {
        BorderEffect clone = new BorderEffect();
        clone.Width = this.Width;
        clone.BorderColor = this.BorderColor;
        return clone;
    }
}

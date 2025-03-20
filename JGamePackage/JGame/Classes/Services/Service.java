package JGamePackage.JGame.Classes.Services;

import JGamePackage.JGame.Classes.Instance;

public abstract class Service extends Instance {
    @Override
    public Service Clone() {
        return null;
    }

    @Override
    public Service cloneWithoutChildren() {
        return null;
    }
}

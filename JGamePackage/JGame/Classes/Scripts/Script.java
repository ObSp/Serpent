package JGamePackage.JGame.Classes.Scripts;

import java.lang.reflect.InvocationTargetException;

import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;

public class Script extends ScriptBase {
    private Class<? extends WritableScript> writableClass;

    public Script() {
        super();
    }
    
    public Script(Class<? extends WritableScript> writableClass) {
        this.writableClass = writableClass;
    }

    public Class<? extends WritableScript> GetWritableClass() {
        return writableClass;
    }

    public void SetWritableClass(Class<? extends WritableScript> writableClass) {
        this.writableClass = writableClass;
    }

    @Override
    public Script Clone() {
        Script newScript = cloneWithoutChildren();
        this.cloneHierarchyToNewParent(newScript);
        return newScript;
    }

    @Override
    protected Script cloneWithoutChildren() {
        Script newScript;

        try {
            newScript = this.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            ErrorCloneFailed.Throw(new String[] {this.getClass().getSimpleName(), e.getMessage()});
            return null;
        }

        return newScript;
    }
    
    
}

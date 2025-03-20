package JGamePackage.JGame.Classes.Scripts;

import java.lang.reflect.InvocationTargetException;

import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;

public class Script extends ScriptBase {
    public Class<? extends WritableScript> WritableClass;
    private String writableClassName; //For serialization

    public Script() {
        super();
    }
    
    public Script(Class<? extends WritableScript> writableClass) {
        this.WritableClass = writableClass;
        this.writableClassName = writableClass.getName();
    }

    public void SetWritableClassName(String path) {
        writableClassName = path;
    }

    public String GetWritableClassName() {
        return writableClassName;
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

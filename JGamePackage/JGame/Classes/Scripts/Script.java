package JGamePackage.JGame.Classes.Scripts;

import java.lang.reflect.InvocationTargetException;

import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.lib.CustomError.CustomError;

public class Script extends ScriptBase {
    private static CustomError WarningRunningWritableSetFailed = new CustomError("Unable to set the running writable script instance of script %s: a running writable script instance is already attached to this script,", CustomError.WARNING, "JGamePackage");

    public Class<? extends WritableScript> WritableClass;
    private WritableScript runningInstance;
    private String writableClassName; //For serialization

    public boolean Disabled = false;

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

    public void SetRunningWritableScriptInstance(WritableScript s) {
        if (runningInstance != null) {
            WarningRunningWritableSetFailed.Throw(this.Name);
            return;
        }
        runningInstance = s;
    }

    public WritableScript GetRunningWritableScriptInstance() {
        return runningInstance;
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

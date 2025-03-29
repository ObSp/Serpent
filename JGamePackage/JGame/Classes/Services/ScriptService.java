package JGamePackage.JGame.Classes.Services;

import java.util.ArrayList;

import JGamePackage.JGame.Classes.Scripts.Script;
import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.lib.CustomError.CustomError;
import JGamePackage.lib.Signal.SignalWrapper;

public class ScriptService extends Service {
    private CustomError WarningScriptLoadFail = new CustomError("Error while trying to load Script %s, the loading of this script will be skipped.", CustomError.WARNING, "JGamePackage");

    private ArrayList<Script> loadedScripts = new ArrayList<>();
    private ArrayList<WritableScript> loadedWritables = new ArrayList<>();

    private void loadScript(Script script) {
        if (script.Disabled) return;
        WritableScript writScript;
        try {
            writScript = script.WritableClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            WarningScriptLoadFail.Throw(new String[] {script.Name});
            return;
        }

        writScript.game = game;
        writScript.script = script;
        writScript.world = game.WorldNode;

        loadedScripts.add(script);
        loadedWritables.add(writScript);

        script.SetRunningWritableScriptInstance(writScript);

        new Thread(writScript::Start).start();
    }

    private void loadCreatedScripts() {
        for (Script desc : game.WorldNode.GetDescendantsOfClass(Script.class)) {
            loadScript(desc);
        }

        for (Script desc : game.UINode.GetDescendantsOfClass(Script.class)) {
            loadScript(desc);
        }

        for (Script desc : game.ScriptNode.GetDescendantsOfClass(Script.class)) {
            loadScript(desc);
        }

        for (Script desc : game.StorageNode.GetDescendantsOfClass(Script.class)) {
            loadScript(desc);
        }
    }

    private void attachScriptLoadOnCreationConnection() {
        game.WorldNode.DescendantAdded.Connect(inst-> {
            if (!(inst instanceof Script)) return;
            loadScript((Script) inst);
        });

        game.UINode.DescendantAdded.Connect(inst-> {
            if (!(inst instanceof Script)) return;
            loadScript((Script) inst);
        });

        game.ScriptNode.DescendantAdded.Connect(inst-> {
            if (!(inst instanceof Script)) return;
            loadScript((Script) inst);
        });

        game.StorageNode.DescendantAdded.Connect(inst-> {
            if (!(inst instanceof Script)) return;
            loadScript((Script) inst);
        });
    }

    private void runScriptsOnTick(double dt) {
        for (WritableScript script : loadedWritables) {
            new Thread(()->script.Tick(dt)).start();
        }
    }

    /**Attempts to load the given script. Note that all scripts are automatically
     * loaded upon creation, so this method should only be used to load scripts that
     * have WritableClasses which are not set using the Script constructor.
     * 
     * @param script
     */
    public void LoadScript(Script script) {
        loadScript(script);
    }

    public ScriptService(SignalWrapper<Double> onTick, boolean init) {
        if (!init) return; //dont initialize scripts because of startParams
        loadCreatedScripts();
        attachScriptLoadOnCreationConnection();

        onTick.Connect(this::runScriptsOnTick);
    }

    
}
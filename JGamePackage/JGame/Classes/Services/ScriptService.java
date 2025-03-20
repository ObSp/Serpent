package JGamePackage.JGame.Classes.Services;

import java.io.File;
import java.util.ArrayList;

import JGamePackage.JGame.Classes.Scripts.Script;
import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.lib.CustomError.CustomError;
import JGamePackage.lib.Signal.SignalWrapper;

public class ScriptService extends Service {
    private CustomError ErrorNoDotJGame = new CustomError("Unable to load scripts: unable to find %s.", CustomError.ERROR, "JGamePackage");
    private CustomError WarningScriptLoadFail = new CustomError("Error while trying to load Script %s, the loading of this script will be skipped.", CustomError.WARNING, "JGamePackage");

    private ArrayList<Script> loadedScripts = new ArrayList<>();
    private ArrayList<WritableScript> loadedWritables = new ArrayList<>();

    private void loadScript(Script script) {
        WritableScript writScript;
        try {
            writScript = script.GetWritableClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            WarningScriptLoadFail.Throw(new String[] {script.Name});
            return;
        }

        writScript.game = game;
        writScript.script = script;

        loadedScripts.add(script);
        loadedWritables.add(writScript);

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

    public ScriptService(SignalWrapper<Double> onTick, boolean init) {
        if (!init) return; //dont initialize scripts because of startParams
        loadCreatedScripts();
        attachScriptLoadOnCreationConnection();

        onTick.Connect(this::runScriptsOnTick);
    }

    @SuppressWarnings("unused")
    private ProjectRepresentation traceDotJGameDir() {
        File dotJGame = new File("/.jgame");

        if (!dotJGame.exists()) {
            ErrorNoDotJGame.Throw(".jgame directory");
            return null;
        }

        File projJson = new File("/.jgame/project.json");

        if (!projJson.exists()) {
            ErrorNoDotJGame.Throw("project.json file");
            return null;
        }

        return new ProjectRepresentation(dotJGame, projJson);
    }

    record ProjectRepresentation(File dotJGame, File projectJSON) {}
}
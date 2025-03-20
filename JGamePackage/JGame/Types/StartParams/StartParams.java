package JGamePackage.JGame.Types.StartParams;

public class StartParams {
    public boolean initializeWindow = true;
    public boolean loadScripts = true;
    public boolean autoUpdate = true;

    public StartParams() {}

    public StartParams(boolean initWindow) {
        initializeWindow = initWindow;
    }

    public StartParams(boolean initWindow, boolean loadScripts) {
        initializeWindow = initWindow;
        this.loadScripts = loadScripts;
    }

    public StartParams(boolean initWindow, boolean loadScripts, boolean autoUpdate) {
        initializeWindow = initWindow;
        this.loadScripts = loadScripts;
        this.autoUpdate = autoUpdate;
    }
}

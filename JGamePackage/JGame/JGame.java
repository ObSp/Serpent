package JGamePackage.JGame;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import JGamePackage.JGame.Classes.Instance;
import JGamePackage.JGame.Classes.Misc.Camera;
import JGamePackage.JGame.Classes.Misc.ScriptNode;
import JGamePackage.JGame.Classes.Misc.StorageNode;
import JGamePackage.JGame.Classes.Misc.UINode;
import JGamePackage.JGame.Classes.Misc.WorldNode;
import JGamePackage.JGame.Classes.Rendering.Renderer;
import JGamePackage.JGame.Classes.Services.InputService;
import JGamePackage.JGame.Classes.Services.ScriptService;
import JGamePackage.JGame.Classes.Services.SerializationService;
import JGamePackage.JGame.Classes.Services.StudioService;
import JGamePackage.JGame.Classes.Services.TimeService;
import JGamePackage.JGame.Classes.Services.WindowService;
import JGamePackage.JGame.Types.Services.ServiceContainer;
import JGamePackage.JGame.Types.StartParams.StartParams;
import JGamePackage.lib.Promise.Promise;
import JGamePackage.lib.Signal.Signal;
import JGamePackage.lib.Signal.SignalWrapper;

public class JGame {
    public static JGame CurrentGame;
    

    //--NUMBERS--//
    public double SecondsPerTick = 1.0/60.0;

    //--BASE NODES--//
    public final WorldNode WorldNode;
    public final UINode UINode;
    public final StorageNode StorageNode;
    public final ScriptNode ScriptNode;

    //--SIGNALS--//
    private Signal<Double> ontick = new Signal<>();
    private final SignalWrapper<Double> servicesOnTick = new SignalWrapper<>(ontick);

    //--SERVICES--//
    public final ServiceContainer Services;

    public final InputService InputService;
    public final TimeService TimeService;
    public final WindowService WindowService;
    public final ScriptService ScriptService;
    public final SerializationService SerializationService;
    public final StudioService StudioService;

    //--CAMERA--//
    public Camera Camera;

    //--RENDERING--//
    private final Renderer renderer;

    //--WINDOW--//
    private JFrame gameWindow;



    //--CONSTRUCTORS--//
    public JGame() {
        this(new StartParams());
    }

    public JGame(StartParams params) {
        JGame.CurrentGame = this;

        WorldNode = new WorldNode();
        UINode = new UINode();
        StorageNode = new StorageNode();
        ScriptNode = new ScriptNode();

        gameWindow = new JFrame("JGame");

        Camera = new Camera();

        Services = new ServiceContainer(servicesOnTick, params);
        InputService = Services.InputService;
        TimeService = Services.TimeService;
        WindowService = Services.WindowService;
        ScriptService = Services.ScriptService;
        SerializationService = Services.SerializationService;
        StudioService = Services.StudioService;

        renderer = new Renderer(this);

        if (this.StudioService.IsStudioProject()) {
            this.SerializationService.ReadInstanceArrayFromFile(".jgame/world.json", null);
        }

        Promise.await(this.start(params));
    }


    //--INITIALIZATION--//
    private Promise start(StartParams params) {
        return new Promise(self ->{
            if (params.initializeWindow) {
                gameWindow.setSize(500, 500);
                gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
                gameWindow.setIconImage(new ImageIcon("JGamePackage\\JGame\\Assets\\icon.png").getImage());
                gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameWindow.setLocationRelativeTo(null);

                gameWindow.setVisible(true);
            }

            
            gameWindow.add(renderer);

            self.resolve();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            run();
        });
    }


    //--TICK CYCLE--//
    private void run() {
        double lastTick = curSeconds();

        while (true) {
            double curSeconds = curSeconds();
            if (curSeconds-lastTick>=SecondsPerTick) {
                double delta = curSeconds - lastTick;
                tick(delta);
                lastTick = delta + (curSeconds() - delta);
            }
        }
    }

    private void tick(double dt) {
        ontick.Fire(dt);
        render();
    }

    private void render() {
        gameWindow.repaint();
    }

    private double curSeconds() {
        return ((double)System.currentTimeMillis())/1000;
    }

    public Instance[] GetAllNodeDescendants() {
        Instance[] worldNodeDesc = this.WorldNode.GetDescendants();
        Instance[] uiNodeDesc = this.UINode.GetDescendants();
        Instance[] storageNodeDesc = this.StorageNode.GetDescendants();
        Instance[] scriptNodeDesc = this.ScriptNode.GetDescendants();

        Instance[] allInstances = new Instance[worldNodeDesc.length + uiNodeDesc.length + storageNodeDesc.length + scriptNodeDesc.length];
        for (int i = 0; i < worldNodeDesc.length; i++) {
            allInstances[i] = worldNodeDesc[i];
        }
        for (int i = 0; i < uiNodeDesc.length; i++) {
            allInstances[i + worldNodeDesc.length] = uiNodeDesc[i];
        }
        for (int i = 0; i < storageNodeDesc.length; i++) {
            allInstances[i + worldNodeDesc.length + uiNodeDesc.length] = storageNodeDesc[i];
        }
        for (int i = 0; i < scriptNodeDesc.length; i++) {
            allInstances[i + worldNodeDesc.length + uiNodeDesc.length + storageNodeDesc.length] = scriptNodeDesc[i];
        }
        return allInstances;
    }


    //--MISC--//
    public JFrame GetWindow(){
        return gameWindow;
    }
}
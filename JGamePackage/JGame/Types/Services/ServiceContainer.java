package JGamePackage.JGame.Types.Services;

import JGamePackage.JGame.Classes.Services.InputService;
import JGamePackage.JGame.Classes.Services.ScriptService;
import JGamePackage.JGame.Classes.Services.SerializationService;
import JGamePackage.JGame.Classes.Services.Service;
import JGamePackage.JGame.Classes.Services.StudioService;
import JGamePackage.JGame.Classes.Services.TimeService;
import JGamePackage.JGame.Classes.Services.WindowService;
import JGamePackage.JGame.Types.StartParams.StartParams;
import JGamePackage.lib.Signal.SignalWrapper;

public class ServiceContainer {
    public final Service[] Services;

    public final InputService InputService;
    public final TimeService TimeService;
    public final WindowService WindowService;
    public final ScriptService ScriptService;
    public final SerializationService SerializationService;
    public final StudioService StudioService;

    public ServiceContainer(SignalWrapper<Double> onTick, StartParams startParams) {
        TimeService = new TimeService(onTick);
        WindowService = new WindowService(onTick);
        InputService = new InputService(onTick);
        ScriptService = new ScriptService(onTick, startParams.loadScripts);
        SerializationService = new SerializationService();
        StudioService = new StudioService(startParams.autoUpdate);

        Services = new Service[] {InputService, TimeService, WindowService, ScriptService};
    }
}

package JGamePackage.lib.Signal;


/**A wrapper class for the {@code Signal} class. This class is designed to be used on the developer-end,
 * giving them a more developer friendly object to work with. This class excludes the {@code Fire()} method
 * in favor of it being a one-way communication class.
 * 
 */
public class VoidSignalWrapper extends AbstractSignalWrapper{
    private VoidSignal sig;

    public VoidSignalWrapper(){
        sig = new VoidSignal();
    }

    public VoidSignalWrapper(VoidSignal signal){
        sig = signal;
    }


    public VoidSignal.VoidConnection Connect(Runnable callback){
        return sig.Connect(callback);
    }

    public VoidSignal.VoidConnection Once(Runnable callback){
        return sig.Once(callback);
    }
}

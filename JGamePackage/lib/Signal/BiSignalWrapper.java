package JGamePackage.lib.Signal;

import java.util.function.BiConsumer;

/**A wrapper class for the {@code Signal} class. This class is designed to be used on the developer-end,
 * giving them a more developer friendly object to work with. This class excludes the {@code Fire()} method
 * in favor of it being a one-way communication class.
 * 
 */
public class BiSignalWrapper<T,U> extends AbstractSignalWrapper{
    private BiSignal<T,U> sig;

    public BiSignalWrapper(){
        sig = new BiSignal<>();
    }

    public BiSignalWrapper(BiSignal<T,U> signal){
        sig = signal;
    }


    public BiSignal<T,U>.BiConnection Connect(BiConsumer<T,U> callback){
        return sig.Connect(callback);
    }

    public BiSignal<T,U>.BiConnection Once(BiConsumer<T,U> callback){
        return sig.Once(callback);
    }
}

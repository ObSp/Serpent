package JGamePackage.lib.Signal;

import java.util.function.Consumer;

/**A wrapper class for the {@code Signal} class. This class is designed to be used on the developer-end,
 * giving them a more developer friendly object to work with. This class excludes the {@code Fire()} method
 * in favor of it being a one-way communication class.
 * 
 */
public class SignalWrapper<T> extends AbstractSignalWrapper{
    private Signal<T> sig;

    public SignalWrapper(){
        sig = new Signal<>();
    }

    public SignalWrapper(Signal<T> signal){
        sig = signal;
    }

    /**Connects the given callback to this Signal's event and returns a {@code Connection} object representing it.
     * 
     * @param callback : The function to connect to this Signal's event
     * @return A new connection representing it
     */
    public Signal<T>.Connection Connect(Consumer<T> callback){
        return sig.Connect(callback);
    }

    /**Connects the given callback to this Signal's event and returns a {@code Connection} that will be disconnected after the next time this Signal is fired.
     * 
     * @param callback : The function to connect to this Signal's event
     * @return A new connection representing it
     */
    public Signal<T>.Connection Once(Consumer<T> callback){
        return sig.Once(callback);
    }

    /**Yields the current thread until this Signal has been fired and returns the argument this Signal was fired with.
     * 
     * @return The argument this Signal was fired with.
     */
    public T Wait(){
        return sig.Wait();
    }
}

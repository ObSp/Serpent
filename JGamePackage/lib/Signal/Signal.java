package JGamePackage.lib.Signal;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Signal<T extends Object> extends AbstractSignal {
    
    public ArrayList<Connection> _connections = new ArrayList<>();
    public ArrayList<Connection> _onces = new ArrayList<>();

    //Wait method
    private T fireArg;
    private Object waitMutex = new Object();
    private int fireCount = 0;

    /**Connects the given callback to this Signal's event and returns a {@code Connection} object representing it.
     * 
     * @param callback : The function to connect to this Signal's event
     * @return A new connection representing it
     */
    public Connection Connect(Consumer<T> callback){
        Connection con = new Connection(callback, this);
        _connections.add(con);
        return con;
    }

    /**Connects the given callback to this Signal's event and returns a {@code Connection} that will be disconnected after the next time this Signal is fired.
     * 
     * @param callback : The function to connect to this Signal's event
     * @return A new connection representing it
     */
    public Connection Once(Consumer<T> callback){
        Connection con = new Connection(callback, this);
        _onces.add(con);
        return con;
    }

    /**Yields the current thread until this Signal has been fired and returns the argument this Signal was fired with.
     * 
     * @return The argument this Signal was fired with.
     */
    public T Wait(){
        int lastFireCount = this.fireCount;

        synchronized (waitMutex){
            while (lastFireCount == this.fireCount){
                try {waitMutex.wait();} catch (InterruptedException e) {}
            }
        }
        return fireArg;
    }

    

    /**Calls all connected functions with the given arguments and disconnects all Connections connected with {@code Signal.Once()}.
     * 
     * @param arg1
     * @param arg2
     */
    public void Fire(T arg1){
        fireArg = arg1;
        fireCount++;
        for (int i = 0; i < _connections.size(); i++){
            _connections.get(i)._call(arg1);
        }

        for (int i = _onces.size()-1; i > -1; i--){
            Connection c = _onces.get(i);
            c._call(arg1);
            c.Connected = false;
        }

        this._onces = new ArrayList<>();

        synchronized (waitMutex){
            waitMutex.notify();
        }
    }

    /**A class representing a connection between a {@code Signal} and a function
    * 
    */
    public class Connection extends AbstractConnection{
        private Consumer<T> callback;
        private Signal<T> parent;
   
        /**A boolean representing whether or not this Connection is currently connected to a Signal.
        * 
        */
        public boolean Connected = true;
   
        /**Constructs a new {@code Connection} with the given callback and parent {@code Signal}.
        * 
        * @param callback
        */
        public Connection(Consumer<T> callback, Signal<T> parent){
            this.callback = callback;
            this.parent = parent;
        }

   
        public void _call(T arg1){
            if (!Connected) {
               throw new Error("Unable to call an already disconnected Connection");
            }


           callback.accept(arg1);
        }
   
       /**Disconnects this Connection from the parent signal so it won't be called again
        * 
        */
        public void Disconnect(){
           if (parent._connections.contains(this)){
               parent._connections.remove(this);
           }
   
           if (parent._onces.contains(this)){
               parent._onces.remove(this);
           }
   
           Connected = false;
        }
   
    }
}
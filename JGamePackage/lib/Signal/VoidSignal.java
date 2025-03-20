package JGamePackage.lib.Signal;

import java.util.ArrayList;

public class VoidSignal extends AbstractSignal {
    
    public ArrayList<VoidConnection> _connections = new ArrayList<>();
    public ArrayList<VoidConnection> _onces = new ArrayList<>();

    //Wait method
    private Object waitMutex = new Object();
    private int fireCount = 0;

    /**Connects the given callback to this Signal's event and returns a {@code Connection} object representing it.
     * 
     * @param callback : The function to connect to this Signal's event
     * @return A new connection representing it
     */
    public VoidConnection Connect(Runnable callback){
        VoidConnection con = new VoidConnection(callback, this);
        _connections.add(con);
        return con;
    }

    /**Connects the given callback to this Signal's event and returns a {@code Connection} that will be disconnected after the next time this Signal is fired.
     * 
     * @param callback : The function to connect to this Signal's event
     * @return A new connection representing it
     */
    public VoidConnection Once(Runnable callback){
        VoidConnection con = new VoidConnection(callback, this);
        _onces.add(con);
        return con;
    }


    /**Yields the current thread until this Signal has been fired.
     * 
     */
    public void Wait(){
        int lastFireCount = this.fireCount;

        synchronized (waitMutex){
            while (lastFireCount == this.fireCount){
                try {waitMutex.wait();} catch (InterruptedException e) {}
            }
        }
    }

    /**Calls all connected functions with the given arguments and disconnects all Connections connected with {@code Signal.Once()}.
     * 
     * @param arg1
     * @param arg2
     */
    public void Fire(){
        fireCount++;

        for (VoidConnection con : _connections){
            con._call();
        }

        for (int i = _onces.size()-1; i > -1; i--){
            _onces.get(i)._call();
            _onces.get(i).Disconnect();
        }

        synchronized (waitMutex){
            waitMutex.notify();
        }
    }


    public /**A class representing a connection between a {@code Signal} and a function
    * 
    */
   class VoidConnection extends AbstractConnection {
       private Runnable callback;
       private VoidSignal parent;
   
       /**A boolean representing whether or not this Connection is currently connected to a Signal.
        * 
        */
       public boolean Connected = true;
   
       /**Constructs a new {@code Connection} with the given callback and parent {@code Signal}.
        * 
        * @param callback
        */
       public VoidConnection(Runnable callback, VoidSignal parent){
           this.callback = callback;
           this.parent = parent;
       }
   
       public void _call(){
           if (!Connected) {
               throw new Error("Unable to call an already disconnected Connection");
           }
           callback.run();
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

       public void Reconnect() {
            Connected = true;
            parent._connections.add(this);
       }
   
   }


}
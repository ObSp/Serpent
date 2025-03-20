package JGamePackage.JGame.Classes.Rendering;

import java.awt.Graphics2D;

import JGamePackage.JGame.Classes.Instance;
import JGamePackage.lib.Signal.VoidSignal;

public abstract class Renderable extends Instance {
    public final VoidSignal Mouse1Down = new VoidSignal();
    public final VoidSignal Mouse1Up = new VoidSignal();

    public final VoidSignal Mouse2Down = new VoidSignal();
    public final VoidSignal Mouse2Up = new VoidSignal();
    
    public final VoidSignal MouseEnter = new VoidSignal();
    public final VoidSignal MouseLeave = new VoidSignal();
    
    public double Rotation = 0;

    public int ZIndex = 0;

    public boolean MouseTargetable = true;

    public abstract void render(Graphics2D graphics);
}

package Classes;

import JGamePackage.JGame.Types.PointObjects.Vector2;

public class Util {
    
    public static double lookAt(Vector2 origin, Vector2 target) {
        double xDiff = target.X-origin.X;
        if (xDiff == 0){ //not sure if this can cause any potential errors, but avoids the arithmetic "tried to divide by zere/NaN" error
            return 0.0;
        }
        double yDiff = target.Y-origin.Y;
        return Math.atan(yDiff/xDiff);
    }
}

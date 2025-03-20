package JGamePackage.lib.Utils;

public class ExtendedMath {
    
    public static double lerp(double a, double b, double t) {
        return (1-t) * a + t*b;
    }
}

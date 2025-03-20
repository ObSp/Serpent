package JGamePackage.JGame.Types.PointObjects;

import JGamePackage.lib.Utils.ExtendedMath;

public class UDim {
    public static final UDim zero = new UDim(0, 0);

    public final double Scale;
    public final double Absolute;

    public UDim(double scale, double absolute) {
        this.Scale = scale;
        this.Absolute = absolute;
    }

    public UDim lerp(UDim goal, double t) {
        return new UDim(ExtendedMath.lerp(this.Scale, goal.Scale, t), ExtendedMath.lerp(this.Absolute, goal.Absolute, t));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UDim) {
            UDim other = (UDim) obj;
            return other.Scale == this.Scale && other.Absolute == this.Absolute;
        }
        return false;
    }
}

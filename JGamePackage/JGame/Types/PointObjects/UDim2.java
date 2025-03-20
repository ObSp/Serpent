package JGamePackage.JGame.Types.PointObjects;

public class UDim2 extends BasePoint {

    public static final UDim2 zero = UDim2.fromScale(0, 0);

    public static UDim2 fromScale(double xScale, double yScale) {
        return new UDim2(xScale, 0, yScale, 0);
    }

    public static UDim2 fromAbsolute(double xAbsolute, double yAbsolute) {
        return new UDim2(0, xAbsolute, 0, yAbsolute);
    }

    
    public static UDim2 scaleFromVector2(Vector2 vector2, Vector2 screenSize) {
        return UDim2.fromScale(vector2.X/screenSize.X, vector2.Y/screenSize.Y);
    }


    public final UDim X;
    public final UDim Y;


    public UDim2(double xScale, double xAbsolute, double yScale, double yAbsolute) {
        this.X = new UDim(xScale, xAbsolute);
        this.Y = new UDim(yScale, yAbsolute);
    }

    public UDim2(UDim x, UDim y) {
        this.X = x;
        this.Y = y;
    }


    public UDim2 lerp(UDim2 goal, double t) {
        return new UDim2(X.lerp(goal.X, t), Y.lerp(goal.Y, t));
    }

    
    public UDim2 add(UDim2 other) {
        return new UDim2(this.X.Scale + other.X.Scale, this.X.Absolute + other.X.Absolute,
            this.Y.Scale + other.Y.Scale, this.Y.Absolute + other.Y.Absolute);
    }

    public UDim2 subtract(UDim2 other) {
        return new UDim2(this.X.Scale - other.X.Scale, this.X.Absolute - other.X.Absolute,
            this.Y.Scale - other.Y.Scale, this.Y.Absolute - other.Y.Absolute);
    }

    public UDim2 multiply(UDim2 other) {
        return new UDim2(this.X.Scale * other.X.Scale, this.X.Absolute * other.X.Absolute,
            this.Y.Scale * other.Y.Scale, this.Y.Absolute * other.Y.Absolute);
    }

    public UDim2 divide(UDim2 other) {
        return new UDim2(this.X.Scale/other.X.Scale, this.X.Absolute/other.X.Absolute,
            this.Y.Scale/other.Y.Scale, this.Y.Absolute/other.Y.Absolute);
    }


    /**Converts this UDim2 into a Vector2 by multiplying
     * X.Scale and Y.Scale by totalSize.X and totalSize.Y and then
     * adding X.Absolute and Y.Absolute to their respective components.
     * 
     * @param totalSize
     * @return This UDim2 converted to a Vector2
     */
    public Vector2 ToVector2(Vector2 totalSize) {
        double scaleXAbsolute = this.X.Scale * totalSize.X;
        double scaleYAbsolute = this.Y.Scale * totalSize.Y;
        return new Vector2(scaleXAbsolute + this.X.Absolute, scaleYAbsolute + this.Y.Absolute);
    }

    public UDim2 ToScale() {
        return new UDim2(X.Scale, 0, Y.Scale, 0);
    }

    public UDim2 ToAbsolute() {
        return new UDim2(0, X.Absolute, 0, Y.Absolute);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UDim2) {
            UDim2 other = (UDim2) obj;
            return other.X.equals(this.X) && other.Y.equals(this.Y);
        }
        return false;
    }

    @Override
    public String toString() {
        return "({"+X.Scale+", "+X.Absolute+"}, {"+Y.Scale+", "+Y.Absolute+"})";
    }
}

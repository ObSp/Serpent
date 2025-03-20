package JGamePackage.JGame.Types.PointObjects;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import JGamePackage.JGame.Types.Constants.Constants;
import JGamePackage.lib.CustomError.CustomError;
import JGamePackage.lib.Utils.ExtendedMath;

public class Vector2 extends BasePoint {

    private static final CustomError ErrorAccessIndexOutOfBounds = new CustomError("Index %s out of bounds for Vector2 index access. Valid indices are 0(X) and 1(Y).", CustomError.ERROR, "Vector2");

    /**Shorthand for {@code Vector2(0,0)}.*/
    public static final Vector2 zero = new Vector2(0, 0);
    /**Shorthand for {@code Vector2(.5,.5)} */
    public static final Vector2 half = new Vector2(.5, .5);
    /**Shorthand for {@code Vector2(1,1)} */
    public static final Vector2 one = new Vector2(1, 1);
    /**Shorthand for {@code Vector2(-1,0)}.*/
    public static final Vector2 left = new Vector2(-1, 0);
    /**Shorthand for {@code Vector2(0,1)}. */
    public static final Vector2 up = new Vector2(0, -1);
    /**Shorthand for {@code Vector2(1,0)}. */
    public static final Vector2 right = new Vector2(1, 0);
    /**Shorthand for {@code Vector2(0,-1)}.*/
    public static final Vector2 down = new Vector2(0, 1);

    /**Converts a {@code String} to a {@code Vector2}. The input string must follow this format:
     * (X, Y). Note that whitespace and parantheses don't matter, only the comma is required.
     * 
     * @param str : The input string, following the format specified above
     * @return A new Vector2
     */
    public static Vector2 fromString(String str){
        String stripped = str.replace("(", "").replace(")", "").replace(" ", "");

        String[] split = stripped.split(",");

        double xCoord = Double.parseDouble(split[0]);
        double yCoord = Double.parseDouble(split[1]);

        return new Vector2(xCoord, yCoord);
    }

    /**The X component of this Vector2.
     * 
     */
    public final double X;
    /**The Y component of this Vector2.
     * 
     */
    public final double Y;

    /**Creates a new Vector2 with the specified X and Y components.
     * 
     * @param x
     * @param y
     */
    public Vector2(double x, double y){
        X = x;
        Y = y;
    }

    /**Creates a new Vector2 and sets the Vector2's X and Y components equal to the {@code n} parameter.
     * 
     * @param n : The number to set the X and Y coordinates to
     */
    public Vector2(double n){
        X = n;
        Y = n;
    }

    public Vector2 add(Vector2 other){
        return new Vector2(X+other.X, Y+other.Y);
    }

    public Vector2 add(double n){
        return new Vector2(X+n, Y+n);
    }

    public Vector2 add(double x, double y){
        return new Vector2(X+x, Y+y);
    }


    public Vector2 subtract(Vector2 other){
        return new Vector2(X-other.X, Y-other.Y);
    }

    public Vector2 subtract(double n){
        return new Vector2(X-n, Y-n);
    }

    public Vector2 subtract(double x, double y) {
        return new Vector2(X-x, Y-y);
    }

    public Vector2 multiply(Vector2 other){
        return new Vector2(X*other.X, Y*other.Y);
    }

    public Vector2 multiply(double n) {
        return new Vector2(X*n, Y*n);
    }
    
    public Vector2 multiply(double x, double y) {
        return new Vector2(X*x, Y*y);
    }


    public Vector2 divide(Vector2 other) {
        return new Vector2(X/other.X, Y/other.Y);
    }

    public Vector2 divide(double n) {
        return new Vector2(X/n, Y/n);
    }

    public Vector2 divide(double x, double y) {
        return new Vector2(X/x, Y/y);
    }

    public Vector2 abs() {
        return new Vector2(Math.abs(X), Math.abs(Y));
    }

    public Vector2 negative() {
        return new Vector2(-X, -Y);
    }

    public Vector2 normalized() {
        double x = this.X;
        double y = this.Y;

        double mag = magnitude();
        if (mag > 1E-05){
            x /= mag;
            y /= mag;
            
        } else {
            y = 0;
            x = 0;
        }
        
        return new Vector2(x, y);
    }

    /**Returns a {@code Vector2} with the X and Y components formatted to the specified format
     * 
     * @return
     */
    public Vector2 formatted(String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        df.setRoundingMode(RoundingMode.CEILING);
        
        double newX = Double.valueOf(df.format(X));
        double newY = Double.valueOf(df.format(Y));

        return new Vector2(newX, newY);
    }



    public boolean isZero(){
        return X==0 && Y==0;
    }

    public double magnitude() {
        return Math.sqrt(X*X + Y*Y);
    }

    public Vector2 lerp(Vector2 goal, double t) {
        double newX = ExtendedMath.lerp(X, goal.X, t);
        double newY = ExtendedMath.lerp(Y, goal.Y, t);
        return new Vector2(newX, newY);
    }



    /**Returns a Vector2 with its X and Y components shifted so that the new Vector2's
     * origin is at the {@code origin} parameter.
     * 
     * @param origin
     * @return
     */
    public Vector2 toLocalSpace(Vector2 origin) {
        return origin.add(this);
    }

    public double getAxisFromIndex(int index) {
        if (index == 0) return X;
        if (index == 1) return Y;
        ErrorAccessIndexOutOfBounds.Throw(new String[] {index+""});
        return 0; //will never reach this, just to satisfy compiler
    }

    public double getAxisFromVector2Axis(Constants.Vector2Axis axis) {
        if (axis == Constants.Vector2Axis.X) return X;
        if (axis == Constants.Vector2Axis.Y) return Y;
        ErrorAccessIndexOutOfBounds.Throw(new String[] {axis+""});
        return 0; //will never reach this, just to satisfy compiler
    }

    @Override
    public String toString(){
        return "("+X+", "+Y+")";
    }

    @Override
    public Vector2 clone(){
        return new Vector2(X, Y);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;

        if (!(obj instanceof Vector2)) return false;

        Vector2 other = (Vector2) obj;

        return X==other.X && Y == other.Y;
    }
}
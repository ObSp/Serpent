package Classes;

import java.awt.Color;

import JGamePackage.JGame.Types.PointObjects.Vector2;

public class Values {
    public static final double CAMERA_SHAKE_FACTOR = 10;

    public static final Vector2 BULLET_SIZE = new Vector2(20, 5);
    public static final Color BULLET_COLOR = rgb(255, 196, 20);
    public static final double BULLET_VELOCITY = 50;

    private static Color rgb(int r, int g, int b) {
        return new Color(r, g, b);
    }
}

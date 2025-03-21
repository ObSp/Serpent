import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import JGamePackage.JGame.JGame;

public class Serpent {
    static JGame game = new JGame();

    public static void main(String[] args) {
        game.WindowService.BackgroundColor = new Color(102, 153, 255);// (102, 204, 255);
        game.GetWindow()
                .setCursor(game.GetWindow().getToolkit().createCustomCursor(
                        new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                        new Point(),
                        null));
    }
}

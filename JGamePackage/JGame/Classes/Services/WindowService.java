package JGamePackage.JGame.Classes.Services;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import JGamePackage.JGame.Types.PointObjects.Vector2;
import JGamePackage.lib.Signal.SignalWrapper;

public class WindowService extends Service {
    public Color BackgroundColor = Color.gray;

    private Vector2 screenSize;

    public WindowService(SignalWrapper<Double> onTick) {
        onTick.Connect(dt->{
            screenSize = new Vector2(GetWindowWidth(), GetWindowHeight());
        });
    }

    public Vector2 GetWindowSize(){
        return screenSize != null ? screenSize : new Vector2(GetWindowWidth(), GetWindowHeight());
    }

    public int GetWindowHeight(){
        checkForInit();
        return game.GetWindow().getContentPane().getHeight();
    }

    public int GetWindowWidth(){
        checkForInit();
        return game.GetWindow().getContentPane().getWidth();
    }

    public Vector2 GetScreenSize() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        return new Vector2(d.getWidth(), d.getHeight());
    }

    public void SetWindowTitle(String newTitle){
        game.GetWindow().setTitle(newTitle);
    }

    public void SetWindowIcon(String path){
        game.GetWindow().setIconImage(new ImageIcon(path).getImage());
    }

    public void SetWindowIcon(Image image){
        game.GetWindow().setIconImage(image);
    }

    public void SetFullscreen(boolean fullscreen) {
        JFrame gameWindow = game.GetWindow();
        gameWindow.dispose();
        gameWindow.setUndecorated(fullscreen);
        gameWindow.setVisible(true);
    }

    public boolean IsFullscreen() {
        return game.GetWindow().isUndecorated();
    }

    public void SetMouseCursor(int cursorID) {
        game.GetWindow().getContentPane().setCursor(new Cursor(cursorID));
    }

    private void checkForInit() {
        if (game.Services.TimeService.GetElapsedTicks() == 0)
            game.Services.TimeService.WaitTicks(1);
    }
}

package JGamePackage.JGame.Classes.Rendering;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Classes.UI.UIBase;
import JGamePackage.JGame.Classes.World.WorldBase;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class Renderer extends JPanel {
    private JGame game;

    public Renderer(JGame game) {
        this.game = game;
    }

    private void sortRenderableArrayByZIndex(Renderable[] arr) {
        int size = arr.length;
        for (int i = 0; i < size - 1; i++) {
            if (arr[i] == null)
                continue;

            int mindex = i;
            for (int j = i + 1; j < size; j++) {
                if (arr[j] == null)
                    continue;
                if (arr[j].ZIndex < arr[mindex].ZIndex)
                    mindex = j;
            }

            Renderable itemAtIndex = arr[i];
            arr[i] = arr[mindex];
            arr[mindex] = itemAtIndex;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        RenderUtil.g = g2d;

        RenderWorld(g2d);
        RenderUI(g2d);
    }

    private void renderUIRecursive(UIBase[] curChildren, Graphics2D g) {
        sortRenderableArrayByZIndex(curChildren);
        for (UIBase child : curChildren) {
            if (!child.Visible) continue;
            Vector2 renderPos = child.GetAbsolutePosition();
            Vector2 renderSize = child.GetAbsoluteSize();
            
            double rotation = child.Rotation;
            int rotPointX = (int) (renderPos.X + .5 * renderSize.X);
            int rotPointY = (int) (renderPos.Y + .5 * renderSize.Y);

            AffineTransform previous = g.getTransform();
            AffineTransform rotated = new AffineTransform();
            rotated.rotate(rotation, rotPointX, rotPointY);

            g.transform(rotated);

            //rotation
            child.render(g);

            if (child.ClipsDescendants) {
                g.setClip((int) renderPos.X, (int) renderPos.Y, (int) renderSize.X, (int) renderSize.Y);
            }

            g.setTransform(previous);
            renderUIRecursive(child.GetChildrenOfClass(UIBase.class), g);

            if (child.ClipsDescendants) {
                g.setClip(null);
            }
        }
    }

    public void RenderUI(Graphics2D g) {
        renderUIRecursive(game.UINode.GetChildrenOfClass(UIBase.class), g);
    }

    private void renderWorldRecursive(WorldBase[] curChildren, Graphics2D g) {
        //sort by ZIndex
        sortRenderableArrayByZIndex(curChildren);
        for (WorldBase child : curChildren) {
            if (!child.Visible) continue;

            Vector2 renderPos = child.GetRenderPosition();
            Vector2 renderSize = child.GetRenderSize();
            
            double rotation = child.Rotation;
            int rotPointX = (int) (renderPos.X + child.Pivot.X * renderSize.X);
            int rotPointY = (int) (renderPos.Y + child.Pivot.Y * renderSize.Y);

            AffineTransform previous = g.getTransform();
            AffineTransform rotated = new AffineTransform();
            rotated.rotate(rotation, rotPointX, rotPointY);

            g.transform(rotated);

            //rotation
            child.render(g);

            g.setTransform(previous);

            renderWorldRecursive(child.GetChildrenOfClass(WorldBase.class), g);
        }
    } 

    public void RenderWorld(Graphics2D g) {
        g.setColor(game.Services.WindowService.BackgroundColor);
        g.fillRect(0, 0, game.Services.WindowService.GetWindowWidth(), game.Services.WindowService.GetWindowHeight());

        Vector2 cameraPos = game.Services.WindowService.GetWindowSize().divide(2);

        g.rotate(game.Camera.Rotation, cameraPos.X, cameraPos.Y);

        renderWorldRecursive(game.WorldNode.GetChildrenOfClass(WorldBase.class), g);

        g.rotate(-game.Camera.Rotation, cameraPos.X, cameraPos.Y);
    }
}
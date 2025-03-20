package JGamePackage.JGame.Classes.World;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import JGamePackage.JGame.Classes.Abstracts.AbstractImage;
import JGamePackage.JGame.Classes.Rendering.RenderUtil;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class Image2D extends WorldBase implements AbstractImage{
    /**The BufferedImage that is rendered with this object.
     * 
     */
    public transient BufferedImage Image;

    private String imagePath = "JGamePackage\\JGame\\Assets\\imageDefault.png";

    public boolean PixelPerfect = false;

    public Image2D() {
        SetImage(imagePath);
    }

    public void SetImage(String path) {
        try {
            this.Image = ImageIO.read(new File(path));
            imagePath = path;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String GetImagePath() {
        return imagePath;
    }

    @Override
    public void render(Graphics2D graphics) {
        Vector2 renderSize = GetRenderSize();
        Vector2 renderPosition = GetRenderPosition();

        if (!game.Camera.AreBoundsInCameraBounds(renderSize, renderPosition)) return;

        if (this.Transparency < 1) {
            RenderUtil.drawRectangle(this, renderSize, renderPosition, GetRenderColor());
        }

        boolean pixelPerfect = PixelPerfect;

        if (game.Camera.GlobalPixelPerfect) {
            pixelPerfect = true;
        }

        RenderUtil.drawImage(this, renderSize, renderPosition, Image, pixelPerfect);
    }

    @Override
    public Image2D Clone() {
        Image2D clone = cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected Image2D cloneWithoutChildren() {
        Image2D img = new Image2D();
        img.SetImage(this.imagePath);
        img.PixelPerfect = this.PixelPerfect;
        this.cloneHelper(img);
        return img;
    }
}

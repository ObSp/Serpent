package JGamePackage.JGame.Classes.Misc;

import JGamePackage.JGame.Classes.Instance;
import JGamePackage.JGame.Classes.World.WorldBase;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class Camera extends Instance {
    /**The world offset of the camera.
     * 
     */
    public Vector2 Position = Vector2.zero;

    /**Affects the value all PixelPerfect values of Images upon creation
     * 
     */
    public boolean GlobalPixelPerfect = false;

    /**The Z-Offset factor of the Camera. Increasing this will increase the camera's view size.
     * 
     */
    public double DepthFactor = 1;

    /**The rotation of the camera, in radians.
     * 
     */
    public double Rotation = 0;

    private void checkDepthFactor() {
        DepthFactor = Math.max(DepthFactor, 0.0000001);
    }


    public Vector2 GetWorldBaseRenderSize(WorldBase object) {
       checkDepthFactor();
        return new Vector2(Math.floor(object.Size.X/DepthFactor), Math.floor(object.Size.Y/DepthFactor));
    }

    private Vector2 getWorldBaseTopLeftCorner(WorldBase object) {
        return object.Position.subtract(object.GetPivotOffset());
    }

    private Vector2 getCenterPos() {
        return game.Services.WindowService.GetWindowSize().divide(2);
    }
    
    public Vector2 GetWorldBaseRenderPosition(WorldBase object) {
        checkDepthFactor();
        Vector2 posWithOffset = getWorldBaseTopLeftCorner(object).subtract(this.Position);
        return new Vector2(Math.floor(posWithOffset.X/DepthFactor), Math.floor(posWithOffset.Y/DepthFactor)).add(getCenterPos()).subtract(0, game.Services.WindowService.IsFullscreen() ? 50 : 0);//Position.add(object.Position.subtract(object.GetPivotOffset())).divide(DepthFactor).add(game.Services.WindowService.GetWindowSize().divide(2));
    }


    public boolean AreBoundsInCameraBounds(Vector2 size, Vector2 position) {
        Vector2 screenSize = game.Services.WindowService.GetWindowSize();

        double left = 0;
        double top = 0;
        double right = left + screenSize.X;
        double bottom = top + screenSize.Y;

        Vector2 topLeft = position;
        double otherLeft = topLeft.X;
        double otherRight = topLeft.X+size.X;
        double otherTop = topLeft.Y;
        double otherBottom = otherTop+size.Y;

        boolean visibleLeft = otherRight > left;
        boolean visibleRight = otherLeft < right;
        boolean visibleTop = otherBottom > top;
        boolean visibleBottom = otherTop < bottom;

        return visibleLeft && visibleRight && visibleTop && visibleBottom;
    }

    @Override
    public Camera Clone() {
        Camera clone = cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected Camera cloneWithoutChildren() {
        Camera cam = new Camera();
        cam.Position = this.Position;
        cam.Rotation = this.Rotation;
        cam.DepthFactor = this.DepthFactor;
        return cam;
    }
}

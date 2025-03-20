package JGamePackage.JGame.Classes.World;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import JGamePackage.JGame.Classes.Rendering.RenderUtil;
import JGamePackage.JGame.Types.Constants.Constants;
import JGamePackage.JGame.Types.PointObjects.Vector2;

public class Text2D extends WorldBase{

    public String Text = "";

    /** Scales the text so that the text height will always be the same as {@code Size.Y}
     * 
     */
    public boolean TextScaled = false;
    
    public Color TextColor = Color.black;
    public double TextTransparency = 0;

    public Constants.HorizontalTextAlignment HorizontalTextAlignment = Constants.HorizontalTextAlignment.Center;

    public Constants.VerticalTextAlignment VerticalTextAlignment = Constants.VerticalTextAlignment.Center;

    public int FontSize = 10;
    public String FontName = "Arial";
    public int FontStyle = Font.PLAIN;

    public Font CustomFont = null;

    @Override
    public void render(Graphics2D g) {
        if (Text == null) return;

        Vector2 renderSize = GetRenderSize();
        Vector2 renderPos = GetRenderPosition();

        if (!game.Camera.AreBoundsInCameraBounds(renderSize, renderPos)) return;

        if (Transparency < 1) {
            RenderUtil.drawRectangle(this, renderSize, renderPos, GetRenderColor());
        }

        if (Text == null || Text.equals("")) return;

        RenderUtil.drawText(Text, renderSize, renderPos, GetTextRenderColor(), FontSize, FontStyle, FontName, CustomFont, TextScaled, HorizontalTextAlignment, VerticalTextAlignment);
    }

    private Color GetTextRenderColor(){
        return new Color(TextColor.getRed(), TextColor.getGreen(), TextColor.getBlue(), (int) (255*(1-TextTransparency)));
    }

    @Override
    public Text2D Clone() {
        Text2D clone = cloneWithoutChildren();
        this.cloneHierarchyToNewParent(clone);
        return clone;
    }

    @Override
    protected Text2D cloneWithoutChildren() {
        Text2D text = new Text2D();
        this.cloneHelper(text);
        text.Text = this.Text;
        text.TextColor = this.TextColor;
        text.TextTransparency = this.TextTransparency;
        text.HorizontalTextAlignment = this.HorizontalTextAlignment;
        text.VerticalTextAlignment = this.VerticalTextAlignment;
        text.FontSize = this.FontSize;
        text.FontName = this.FontName;
        text.FontStyle = this.FontStyle;
        text.TextScaled = this.TextScaled;
        text.CustomFont = this.CustomFont;
        return text;
    }
}

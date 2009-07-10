package netbang.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

public class InfoText extends Clickable {
    String text;
    Rectangle2D textbounds;
    Color color;
    Color currentcolor;
    int alpha;

    public InfoText(Polygon p, String text, Color color) {
        super(p, null);
        animation = FADEIN;
        this.text = text;
        this.color = color;
        currentcolor = new Color(color.getRGB() & 0x00FFFFFF);//This clears off the alpha channel to start
    }
    /**
     * Creates a new infotext at the specified location. The bounding
     * polygon is just the bounding rectangle.
     * @param x
     * @param y
     * @param text
     * @param color
     */
    public InfoText(int x, int y, String text, Color color) {
        super(new Polygon( new int[] {x}, new int[]{y}, 1) , null);
        animation = FADEIN;
        this.text = text;
        this.color = color;
        currentcolor = new Color(color.getRGB() & 0x00FFFFFF);//This clears off the alpha channel to start
    }
    /**
     * Fades the InfoText.
     *
     * @see netbang.gui.Clickable#fade(int)
     */
    public void fade() {
        System.out.println(color.getAlpha());
        alpha = color.getAlpha() + 26 * ((animation & FADEIN) != 0 ? 1 : -1);
        if (alpha > 255)
            alpha = 255;
        if (alpha < 0)
            alpha = 0;
        currentcolor = new Color((color.getRGB() & 0x00FFFFFF) | alpha << 24);
        if ((animation & FADEIN)!=0 && alpha == 255)
            animation &= ~FADEIN;
        else if ((animation & FADEOUT)!=0 && alpha == 0)
            animation &= ~FADEOUT;
    }

    public void paint(Graphics2D g) {
        Color origcolor = g.getColor();
        if (textbounds == null) {
            textbounds = g.getFont().getStringBounds(text, g.getFontRenderContext());
            bounds = new Polygon(new int[] {(int) textbounds.getMinX(),(int) textbounds.getMinX(),
                    (int) textbounds.getMaxX(), (int) textbounds.getMaxX() },
                    new int[] {(int) textbounds.getMinY(),(int) textbounds.getMaxY(),
                    (int) textbounds.getMinY(), (int) textbounds.getMaxY()}, 4);
        }
        g.fill3DRect(rect.x, rect.y, rect.width, rect.height, true);
        g.setColor(color);
        g.drawString(text, rect.x + (rect.width - (int) textbounds.getWidth())
                / 2, rect.y + (rect.height + (int) textbounds.getHeight()) / 2);
        g.setColor(origcolor);
        
    }

}

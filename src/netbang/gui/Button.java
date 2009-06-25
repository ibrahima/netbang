package netbang.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Button extends Clickable {
    String text;
    Rectangle2D textbounds;

    public Button(Polygon p, BufferedImage srcimg, String text) {
        super(p, srcimg);
        draggable = false;
        this.text = text;
    }

    public void paint(Graphics2D g) {
        Color temp = g.getColor();
        if (textbounds == null) {
            textbounds = g.getFont().getStringBounds(text,
                    g.getFontRenderContext());
        }
        g.fill3DRect(rect.x, rect.y, rect.width, rect.height, true);
        g.setColor(Color.BLACK);
        g.drawString(text, rect.x + (rect.width - (int) textbounds.getWidth())
                / 2, rect.y + (rect.height + (int) textbounds.getHeight()) / 2);
        if (img != null)
            g.drawImage(img, rect.x, rect.y, null);
        g.setColor(temp);
    }

    int textHeight(String message, Graphics2D graphics) {
        if (message == null)
            return -1;
        int lineheight = (int) graphics.getFont().getStringBounds("|",
                graphics.getFontRenderContext()).getHeight();
        return message.split("\n").length * lineheight;
    }

    int textWidth(String message, Graphics2D graphics) {
        if (message == null)
            return -1;
        String[] lines = message.split("\n");
        int width = 0;
        for (int i = 0; i < lines.length; i++) {
            int w = (int) graphics.getFont().getStringBounds(lines[i],
                    graphics.getFontRenderContext()).getWidth();
            if (width < w)
                width = w;
        }
        return width;
    }

    public void fade() {
        // TODO Auto-generated method stub

    }
}

package ucbang.gui;

import java.awt.Color;
import java.awt.Polygon;

public class InfoText extends Clickable {
	String text;
	Color color;
	Color currentcolor;
	int alpha;

	public InfoText(Polygon p, String text, Color color) {
		super(p, null);
		animation = Animations.FADEIN;
		this.text = text;
		this.color = color;
		currentcolor = new Color(color.getRGB() & ~(255 << 24));
	}

	/**
	 * Fades the InfoText.
	 * 
	 * @param amount
	 *            1 for fade in, -1 for fade out
	 * @see ucbang.gui.Clickable#fade(int)
	 */
	public void fade() {
		System.out.println(color.getAlpha());
		alpha = color.getAlpha() + 26
				* (animation == Animations.FADEIN ? 1 : 0);
		if (alpha > 255)
			alpha = 255;
		if (alpha < 0)
			alpha = 0;
		currentcolor = new Color((color.getRGB() & ~(255 << 24)) | alpha << 24);
		if (animation == Animations.FADEIN && alpha == 255)
			animating = false;
		else if (animation == Animations.FADEOUT && alpha == 0)
			animating = false;
	}

}

package ucbang.gui;

import java.awt.Color;
import java.awt.Polygon;

public class InfoText extends Clickable {
	String text;
	Color color;
	Color currentcolor;
	Color bg;
	public InfoText(Polygon p, String text, Color color, Color background) {
		super(p, null);
		animation = Animations.FADEIN;
		this.text = text;
		this.color = color;
		bg = background;
		currentcolor = background;
	}

	/**
	 * Fades the InfoText.
	 * @param amount 1 for fade in, -1 for fade out
	 * @see ucbang.gui.Clickable#fade(int)
	 */
	public void fade(int amount) {
		System.out.println(color.getAlpha());
		if(amount>0){
			
		}else{
			
		}
	}

}

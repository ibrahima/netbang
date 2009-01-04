package ucbang.gui;

import java.awt.Color;
import java.awt.Polygon;

public class InfoText extends Clickable {
	String text;
	Color color;
	public InfoText(Polygon p, String text, Color color) {
		super(p, null);
		animation = Animations.FADEIN;
		this.text = text;
		this.color = color;
	}

	public void fade(int amount) {
		
	}

}

package ucbang.gui;
import java.awt.Graphics2D;

import ucbang.core.*;
public class CardDisplayer {
	Card myCard;
	Graphics2D graphics;
	int x, y;
	public CardDisplayer(Card card, Graphics2D g, int x, int y){
		myCard=card;
		graphics=g;
		this.x=x;
		this.y=y;
	}
	void paint(){
		//TODO: Draw card on the graphics object at (x,y)
	}
}

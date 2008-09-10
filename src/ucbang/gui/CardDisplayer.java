package ucbang.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;

import ucbang.core.*;

public class CardDisplayer {
	Card myCard;
	int x, y;
	boolean faceup=false;
	Image image;
	public CardDisplayer(Card card, int x, int y) {
		myCard = card;
		this.x = x;
		this.y = y;
		image= Toolkit.getDefaultToolkit().getImage("images/cards/bang/p_serif.jpg"); 
		System.out.println(image);
		/*try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		//TODO: Load images, probably going to need a huge list of card image names
	}

	void paint(Graphics2D graphics) {
		// TODO: Draw card on the graphics object at (x,y)
		graphics.drawRoundRect(x, y, 55, 85, 5, 5);
		graphics.drawImage(image, x, y, null);
	}
}

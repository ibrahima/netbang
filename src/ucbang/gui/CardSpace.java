package ucbang.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import ucbang.core.Card;

/**
 * Contains a card and a rectangle
 */
public class CardSpace extends Clickable {
	public Card card;
	public boolean field;
	HandSpace hs;
	Color inner;
	Color outer;

	/**
	 * @param c
	 *            The card this CardSpace describes
	 * @param p
	 *            The bounds of the card
	 * @param player
	 *            The player who owns the card
	 * @param f
	 *            Whether the card is on the field
	 * @param img
	 *            The image for the card
	 * @param hand
	 *            The parent container of the card
	 */
	public CardSpace(Card c, Polygon p, int player, boolean f,
			BufferedImage img, HandSpace hand) {
		super(p, img);
		card = c;
		playerid = player;
		switch (c.location) {
		case 0:
			inner = Color.GRAY;
			break;
		case 1:
			if (c.type == 5)
				inner = new Color(100, 100, 200);
			else {
				inner = ((c.name != "JAIL" || c.name != "DYNAMITE") ? inner = new Color(
						100, 200, 100)
						: new Color(100, 100, 200));
			}
			break;
		default:
			inner = new Color(200, 100, 100);
		}
		switch (playerid) {
		case 0:
			outer = Color.RED;
			break;
		case 1:
			outer = Color.BLUE;
			break;
		case 2:
			outer = Color.CYAN;
			break;
		case 3:
			outer = Color.MAGENTA;
			break;
		case 4:
			outer = Color.YELLOW;
			break;
		case 5:
			outer = Color.ORANGE;
			break;
		case 6:
			outer = Color.GREEN;
			break;
		case 7:
			outer = Color.LIGHT_GRAY;
			break;
		case 8:
			outer = Color.WHITE;
			break;
		case 9:
			outer = Color.PINK;
			break;
		default:
			outer = Color.BLACK;
			break;
		}
		field = f;
		hs = hand;
	}

	public void paint(Graphics2D g) {
		if (animating)
			animate();
		Color temp = g.getColor();
		// g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 7, 7);
		// g.setColor(Color.BLACK);
		// g.fillRoundRect(rect.x + 1, rect.y + 1, rect.width-2, rect.height-2,
		// 6, 6);
		g.setColor(inner);
		g.fillPolygon(bounds);
		g.setColor(outer);
		g.drawPolygon(bounds);
		if (theta != 0.0) {
			AffineTransform tempy = g.getTransform();
			g.translate(rect.getCenterX(), rect.getCenterY());
			;
			at = new AffineTransform();
			at.setToRotation(theta);
			at.translate(-30, -45);
			g.transform(at);
			g.drawImage(img, 2, 3, null);
			g.setTransform(tempy);
		} else {
			g.drawImage(img, rect.x + 2, rect.y + 3, null);
		}
		g.setColor(temp);

	}

	public void fade() {
		// TODO Auto-generated method stub

	}
}
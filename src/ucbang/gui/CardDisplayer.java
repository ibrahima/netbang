package ucbang.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import ucbang.core.*;

public class CardDisplayer {
	Card myCard;
	int x, y;
	boolean faceup=false;
	Image image;
	static HashMap<String,Image> cards = new HashMap<String,Image>();
	public CardDisplayer(Card card, int x, int y) {
		myCard = card;
		this.x = x;
		this.y = y;
		image= Toolkit.getDefaultToolkit().getImage("images/cards/bang/p_serif.jpg");
		if(cards==null)
			loadImages();
		/*try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		//TODO: Load images, probably going to need a huge list of card image names
	}

	private void loadImages() {
		cards.put("APPALOOSA", Toolkit.getDefaultToolkit().getImage("images/cards/bang/appalossa.jpg"));
		cards.put("BANG", Toolkit.getDefaultToolkit().getImage("images/cards/bang/bang.jpg"));
		cards.put("BARREL", Toolkit.getDefaultToolkit().getImage("images/cards/bang/barel.jpg"));
		cards.put("BEER", Toolkit.getDefaultToolkit().getImage("images/cards/bang/pivo.jpg"));
		cards.put("BIBLE", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_bible.jpg"));
		cards.put("BRAWL", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/rvacka.jpg"));
		cards.put("BUFFALO_RIFLE", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/buffalorifle.jpg"));			
		cards.put("CAN_CAN", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_cancan.jpg"));		
		cards.put("CAT_BALLOU", Toolkit.getDefaultToolkit().getImage("images/cards/bang/catbalou.jpg"));		
		cards.put("CONESTOGA", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_conestoga.jpg"));		
		cards.put("DERRINGER", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_derringer.jpg"));		
		cards.put("DODGE", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/dodge.jpg"));		
		cards.put("DUEL", Toolkit.getDefaultToolkit().getImage("images/cards/bang/duel.jpg"));		
		cards.put("DYNAMITE", Toolkit.getDefaultToolkit().getImage("images/cards/bang/dynamite.jpg"));		
		cards.put("GATLING", Toolkit.getDefaultToolkit().getImage("images/cards/bang/kulomet.jpg"));
		cards.put("GENERAL_STORE", Toolkit.getDefaultToolkit().getImage("images/cards/bang/hokynarstvi.jpg"));	
		cards.put("HOWITZER", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_howitzer.jpg"));		
		cards.put("INDIANS", Toolkit.getDefaultToolkit().getImage("images/cards/bang/indiani.jpg"));		
		cards.put("IRON_PLATE", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_ironplate.jpg"));		
		cards.put("JAIL", Toolkit.getDefaultToolkit().getImage("images/cards/bang/vezeni.jpg"));
		cards.put("KNIFE", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_knife.jpg"));
		cards.put("MISS", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/vedle.jpg"));
		cards.put("MUSTANG", Toolkit.getDefaultToolkit().getImage("images/cards/bang/mustang.jpg"));
		cards.put("PANIC", Toolkit.getDefaultToolkit().getImage("images/cards/bang/panic.jpg"));
		cards.put("PEPPERBOX", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/pepperbox.jpg"));
		cards.put("PONY_EXPRESS", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_ponyexpress.jpg"));
		cards.put("PUNCH", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/punch.jpg"));
		cards.put("RAG_TIME", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/ragtime.jpg"));
		cards.put("REMINGTON", Toolkit.getDefaultToolkit().getImage("images/cards/bang/b_remington.jpg"));
		cards.put("REV_CARBINE", Toolkit.getDefaultToolkit().getImage("images/cards/bang/b_carabina.jpg"));
		cards.put("SALOON", Toolkit.getDefaultToolkit().getImage("images/cards/bang/saloon.jpg"));
		cards.put("SCHOFIELD", Toolkit.getDefaultToolkit().getImage("images/cards/bang/b_schofield.jpg"));
		cards.put("SILVER", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/silver.jpg"));
		cards.put("SOMBRERO", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_sombrero.jpg"));
		cards.put("SPRINGFIELD", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/springfield.jpg"));
		cards.put("STAGECOACH", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_bible.jpg"));//TODO
		cards.put("TEN_GALLON_HAT", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_gallonhat.jpg"));
		cards.put("TEQUILA", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/tequila.jpg"));
		cards.put("VOLCANIC", Toolkit.getDefaultToolkit().getImage("images/cards/bang/b_vulcanic.jpg"));
		cards.put("WELLS_FARGO", Toolkit.getDefaultToolkit().getImage("images/cards/bang/wellsfargo.jpg"));
		cards.put("WHISKY", Toolkit.getDefaultToolkit().getImage("images/cards/dodge/z_bible.jpg"));//TODO
		cards.put("WINCHESTER", Toolkit.getDefaultToolkit().getImage("images/cards/bang/b_winchester.jpg"));
	}

	void paint(Graphics2D graphics) {
		// TODO: Draw card on the graphics object at (x,y)
		graphics.drawRoundRect(x, y, 55, 85, 5, 5);
		graphics.drawImage(image, x, y, null);
	}
}

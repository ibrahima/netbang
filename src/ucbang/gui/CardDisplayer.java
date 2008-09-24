package ucbang.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;

import ucbang.core.*;

public class CardDisplayer {
	boolean faceup=false;
	LinkedHashMap<String,Image> cards = new LinkedHashMap<String,Image>();
	public CardDisplayer() {
		loadImages();
		/*try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	private void loadImages() {
		cards.put("APPALOOSA", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/appalossa.jpg")));
		cards.put("BANG", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/bang.jpg")));
		cards.put("BARREL", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/barel.jpg")));
		cards.put("BEER", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/pivo.jpg")));
		cards.put("BIBLE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_bible.jpg")));
		cards.put("BRAWL", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/rvacka.jpg")));
		cards.put("BUFFALO_RIFLE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/buffalorifle.jpg")));			
		cards.put("CAN_CAN", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_cancan.jpg")));		
		cards.put("CANTEEN", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_cannten.jpg")));		
		cards.put("CAT_BALLOU", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/catbalou.jpg")));		
		cards.put("CONESTOGA", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_conestoga.jpg")));		
		cards.put("DERRINGER", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_derringer.jpg")));		
		cards.put("DODGE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/dodge.jpg")));		
		cards.put("DUEL", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/duel.jpg")));		
		cards.put("DYNAMITE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/dynamite.jpg")));		
		cards.put("GATLING", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/kulomet.jpg")));
		cards.put("GENERAL_STORE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/hokynarstvi.jpg")));	
		cards.put("HOWITZER", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_howitzer.jpg")));	
		cards.put("HIDEOUT", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/hideout.jpg")));		
		cards.put("INDIANS", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/indiani.jpg")));		
		cards.put("IRON_PLATE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_ironplate.jpg")));		
		cards.put("JAIL", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/vezeni.jpg")));
		cards.put("KNIFE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_knife.jpg")));
		cards.put("MISS", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/vedle.jpg")));
		cards.put("MUSTANG", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/mustang.jpg")));
		cards.put("PANIC", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/panika.jpg")));
		cards.put("PEPPERBOX", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/pepperbox.jpg")));
		cards.put("PONY_EXPRESS", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_ponyexpress.jpg")));
		cards.put("PUNCH", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/punch.jpg")));
		cards.put("RAG_TIME", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/ragtime.jpg")));
		cards.put("REMINGTON", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/b_remington.jpg")));
		cards.put("REV_CARBINE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/b_carabina.jpg")));
		cards.put("SALOON", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/saloon.jpg")));
		cards.put("SCHOFIELD", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/b_schofield.jpg")));
		cards.put("SILVER", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/silver.jpg")));
		cards.put("SOMBRERO", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_sombrero.jpg")));
		cards.put("SPRINGFIELD", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/springfield.jpg")));
		cards.put("STAGECOACH", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/dostavnik.jpg")));//TODO
		cards.put("TEN_GALLON_HAT", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/z_gallonhat.jpg")));
		cards.put("TEQUILA", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/tequila.jpg")));
		cards.put("VOLCANIC", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/b_vulcanic.jpg")));
		cards.put("WELLS_FARGO", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/wellsfargo.jpg")));
		cards.put("WHISKY", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/dodge/whisky.jpg")));
		cards.put("WINCHESTER", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/cards/bang/b_winchester.jpg")));
                cards.put("BART_CASSIDY", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/bartcassidy.jpg")));
                cards.put("BLACK_JACK", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/blackjack.jpg")));
                cards.put("CALAMITY_JANET",Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/calamityjanet.jpg")));
                cards.put("EL_GRINGO", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/elgringo.jpg")));
                cards.put("JESSE_JONES", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/jessejones.jpg")));
                cards.put("JOURDONNAIS", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/jourdonnais.jpg")));
                cards.put("KIT_CARLSON", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/kitcarlson.jpg")));
                cards.put("LUCKY_DUKE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/luckyduke.jpg")));
                cards.put("PAUL_REGRET", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/paulregret.jpg")));
                cards.put("PEDRO_RAMIREZ", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/pedroramirez.jpg")));
                cards.put("ROSE_DOOLAN", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/rosedoolan.jpg")));
                cards.put("SID_KETCHUM", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/sidketchum.jpg")));
                cards.put("SLAB_THE_KILLER", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/slabthekiller.jpg")));
                cards.put("SUZY_LAFAYETTE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/suzylafayette.jpg")));
                cards.put("VULTURE_SAM", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/vulturesam.jpg")));
                cards.put("WILLY_THE_KID", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/bang/willythekid.jpg")));
                cards.put("APACHE_KID", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/apachekid.jpg")));
                cards.put("BELLE_STAR", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/bellestar.jpg")));
                cards.put("BILL_NOFACE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/billnoface.jpg")));
                cards.put("CHUCK_WENGAM", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/chuckwengam.jpg")));
                cards.put("DOC_HOLYDAY", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/docholyday.jpg")));
                cards.put("ELENA_FUENTE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/elenafuente.jpg")));
                cards.put("GREG_DIGGER", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/gregdigger.jpg")));
                cards.put("HERB_HUNTER", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/herbhunter.jpg")));
                cards.put("JOSE_DELGADO", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/josedelgado.jpg")));
                cards.put("MOLLY_STARK", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/mollystark.jpg")));
                cards.put("PAT_BRENNAN", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/patbrennan.jpg")));
                cards.put("PIXIE_PETE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/pixiepete.jpg")));
                cards.put("SEAN_MALLORY", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/seanmallory.jpg")));
                cards.put("TEQUILA_JOE", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/tequilajoe.jpg")));
                cards.put("VERA_CUSTER", Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/char/dodge/veracuster.jpg")));
	}

	public void paint(String card, Graphics2D graphics, int x, int y){
		if(cards.containsKey(card)){
			graphics.drawRoundRect(x, y, 55, 85, 5, 5);
			graphics.drawImage(cards.get(card), x, y, null);
		}else{
			System.out.println("Card "+card+" not found");
		}
	}
}

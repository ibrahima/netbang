package ucbang.gui;


import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;

public class CardDisplayer {
	boolean faceup=false;
	LinkedHashMap<String,BufferedImage> cards = new LinkedHashMap<String,BufferedImage>();
	AffineTransform rotate = AffineTransform.getRotateInstance(Math.PI/2);//initialized to PI/2 rotation
	public CardDisplayer() {
		loadImages();
	}

	private void loadImages() {
		try {
			cards.put("BACK", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/back.jpg")));
			cards.put("BULLETBACK", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/bulletback.jpg")));
			cards.put("APPALOOSA", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/appalossa.jpg")));
			cards.put("BANG", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/bang.jpg")));
			cards.put("BARREL", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/barel.jpg")));
			cards.put("BEER", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/pivo.jpg")));
			cards.put("BIBLE", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_bible.jpg")));
			cards.put("BRAWL", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/rvacka.jpg")));
			cards.put("BUFFALO_RIFLE", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/buffalorifle.jpg")));			
			cards.put("CAN_CAN", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_cancan.jpg")));		
			cards.put("CANTEEN", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_cannten.jpg")));		
			cards.put("CAT_BALLOU", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/catbalou.jpg")));		
			cards.put("CONESTOGA", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_conestoga.jpg")));		
			cards.put("DERRINGER", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_derringer.jpg")));		
			cards.put("DODGE", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/dodge.jpg")));		
			cards.put("DUEL", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/duel.jpg")));		
			cards.put("DYNAMITE", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/dynamite.jpg")));		
			cards.put("GATLING", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/kulomet.jpg")));
			cards.put("GENERAL_STORE", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/hokynarstvi.jpg")));	
			cards.put("HOWITZER", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_howitzer.jpg")));	
			cards.put("HIDEOUT", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/hideout.jpg")));		
			cards.put("INDIANS", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/indiani.jpg")));		
			cards.put("IRON_PLATE", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_ironplate.jpg")));		
			cards.put("JAIL", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/vezeni.jpg")));
			cards.put("KNIFE", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_knife.jpg")));
			cards.put("MISS", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/vedle.jpg")));
			cards.put("MUSTANG", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/mustang.jpg")));
			cards.put("PANIC", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/panika.jpg")));
			cards.put("PEPPERBOX", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/pepperbox.jpg")));
			cards.put("PONY_EXPRESS", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_ponyexpress.jpg")));
			cards.put("PUNCH", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/punch.jpg")));
			cards.put("RAG_TIME", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/ragtime.jpg")));
			cards.put("REMINGTON", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/b_remington.jpg")));
			cards.put("REV_CARBINE", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/b_carabina.jpg")));
			cards.put("SALOON", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/saloon.jpg")));
			cards.put("SCHOFIELD", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/b_schofield.jpg")));
			cards.put("SILVER", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/silver.jpg")));
			cards.put("SOMBRERO", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_sombrero.jpg")));
			cards.put("SPRINGFIELD", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/springfield.jpg")));
			cards.put("STAGECOACH", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/dostavnik.jpg")));
			cards.put("TEN_GALLON_HAT", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/z_gallonhat.jpg")));
			cards.put("TEQUILA", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/tequila.jpg")));
			cards.put("VOLCANIC", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/b_vulcanic.jpg")));
			cards.put("WELLS_FARGO", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/wellsfargo.jpg")));
			cards.put("WHISKY", ImageIO.read(ClassLoader.getSystemResource("images/cards/dodge/whisky.jpg")));
			cards.put("WINCHESTER", ImageIO.read(ClassLoader.getSystemResource("images/cards/bang/b_winchester.jpg")));
			cards.put("BART_CASSIDY", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/bartcassidy.jpg")));
			cards.put("BLACK_JACK", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/blackjack.jpg")));
			cards.put("CALAMITY_JANET",ImageIO.read(ClassLoader.getSystemResource("images/char/bang/calamityjanet.jpg")));
			cards.put("EL_GRINGO", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/elgringo.jpg")));
			cards.put("JESSE_JONES", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/jessejones.jpg")));
			cards.put("JOURDONNAIS", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/jourdonnais.jpg")));
			cards.put("KIT_CARLSON", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/kitcarlson.jpg")));
			cards.put("LUCKY_DUKE", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/luckyduke.jpg")));
			cards.put("PAUL_REGRET", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/paulregret.jpg")));
			cards.put("PEDRO_RAMIREZ", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/pedroramirez.jpg")));
			cards.put("ROSE_DOOLAN", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/rosedoolan.jpg")));
			cards.put("SID_KETCHUM", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/sidketchum.jpg")));
			cards.put("SLAB_THE_KILLER", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/slabthekiller.jpg")));
			cards.put("SUZY_LAFAYETTE", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/suzylafayette.jpg")));
			cards.put("VULTURE_SAM", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/vulturesam.jpg")));
			cards.put("WILLY_THE_KID", ImageIO.read(ClassLoader.getSystemResource("images/char/bang/willythekid.jpg")));
			cards.put("APACHE_KID", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/apachekid.jpg")));
			cards.put("BELLE_STAR", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/bellestar.jpg")));
			cards.put("BILL_NOFACE", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/billnoface.jpg")));
			cards.put("CHUCK_WENGAM", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/chuckwengam.jpg")));
			cards.put("DOC_HOLYDAY", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/docholyday.jpg")));
			cards.put("ELENA_FUENTE", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/elenafuente.jpg")));
			cards.put("GREG_DIGGER", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/gregdigger.jpg")));
			cards.put("HERB_HUNTER", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/herbhunter.jpg")));
			cards.put("JOSE_DELGADO", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/josedelgado.jpg")));
			cards.put("MOLLY_STARK", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/mollystark.jpg")));
			cards.put("PAT_BRENNAN", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/patbrennan.jpg")));
			cards.put("PIXIE_PETE", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/pixiepete.jpg")));
			cards.put("SEAN_MALLORY", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/seanmallory.jpg")));
			cards.put("TEQUILA_JOE", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/tequilajoe.jpg")));
			cards.put("VERA_CUSTER", ImageIO.read(ClassLoader.getSystemResource("images/char/dodge/veracuster.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getImage(String card){
		return cards.get(card);
	}
}

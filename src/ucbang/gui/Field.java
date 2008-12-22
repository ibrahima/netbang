package ucbang.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import ucbang.core.Card;
import ucbang.core.Deck;
import ucbang.core.Player;

import ucbang.network.Client;

public class Field implements MouseListener, MouseMotionListener{
	Client client;
	public BSHashMap<Card, Clickable> clickies = new BSHashMap<Card, Clickable>();
	CardDisplayer cd;
	Point pointOnCard;
	Clickable movingCard;
	Card clicked;
	ArrayList<Card> pick;
	public ArrayList<HandSpace> handPlacer = new ArrayList<HandSpace>(); //to avoid npe
	String description;
	Point describeWhere;
	long lastMouseMoved = System.currentTimeMillis();
	int tooltipWidth = 0;
	int tooltipHeight = 0;
	Point hoverpoint;

	public Field(CardDisplayer cd, Client c) {
		this.cd=cd;
		client = c;
	}
	/**
	 * Adds a card to the specified location, owned by the specified player
	 * <p>This method specifies the location of the card to be placed, so for most
	 * cases it should not be used. Use add(Card, int, boolean) whenever possible</p>
	 * @param card The card to be added
	 * @param x The x coordinate of the location
	 * @param y The y coordinate of the location
	 * @param player The player who owns the card
	 * @param field Whether the card is in the field or not
	 */
	public void add(Card card, int x, int y, int player, boolean field){
		clickies.put(card, new CardSpace(card, rectToPoly(x,y,60,90), player, field, cd.getImage(card.name), null));
	}
	/**
	 * Removes the last card in the hand of a player, used when
	 * the player is an opponent whose hand is unknown and they just
	 * played a card
	 * @param player the player whose hand to remove a card from
	 */
	public void removeLast(int player){
		clickies.remove(handPlacer.get(player).removeLast().card);
	}
	/**
	 * Adds a card to the field owned by the specified player
	 * <p>This method is "smart" and can locate cards automatically.
	 * Use this whenever players exist</p>
	 * @param card
	 * @param player
	 * @param field
	 */
	public void add(Card card, int player, boolean field){
		if(client.id==player)
			System.out.println("Client has "+client.player.hand.size()+"cards in his hand.");
		if(card.type==1){//this a character card
			int x=350;
			int y=200;
			clickies.put(card, new CardSpace(card, rectToPoly(x, y,60,90), player, false, cd.getImage(card.name), null));
		}else{
			HandSpace hs = handPlacer.get(player);
			int fieldoffset = (field?100:0);
			double handoffset = 30*(!field?client.players.get(player).hand.size():client.players.get(player).field.size());
			int xoffset = (int)(handoffset * Math.sin(hs.theta))+(int)(fieldoffset*Math.sin(hs.theta));
			int yoffset = (int)(handoffset * Math.cos(hs.theta))+(int)(fieldoffset*Math.cos(hs.theta));
			int x=(int) hs.rect.x+hs.rect.width-xoffset;
			int y=(int) hs.rect.y+yoffset;
			CardSpace cs = new CardSpace(card, rectToPoly(x,y, 60,90), player, field, cd.getImage(card.name), hs);
			clickies.put(card, cs);
			hs.addCard(cs);
			if(hs.autoSort) hs.sortHandSpace();
		}
	}

	int textHeight(String message, Graphics2D graphics){
		if(message==null)return -1;
		int lineheight=(int)graphics.getFont().getStringBounds("|", graphics.getFontRenderContext()).getHeight();
		return message.split("\n").length*lineheight;
	}
	int textWidth(String message, Graphics2D graphics){
		if(message==null)return -1;
		String[] lines = message.split("\n");
		int width=0;
		for(int i=0;i<lines.length;i++){
			int w=(int)graphics.getFont().getStringBounds(lines[i], graphics.getFontRenderContext()).getWidth();
			if(width<w)
				width=w;
		}
		return width;
	}
	void improvedDrawString(String message, int x, int y, Graphics2D graphics){
		int lineheight=(int)graphics.getFont().getStringBounds("|", graphics.getFontRenderContext()).getHeight();
		if(message==null)
			return;
		String[] lines = message.split("\n");
		for(int i=0;i<lines.length;i++){
			graphics.drawString(lines[i], x, y+i*lineheight);
		}
	}
	public void paint(Graphics2D graphics){
		for(HandSpace hs : handPlacer){
			if(hs.autoSort)
				graphics.fill(hs.rect);
			else
				graphics.draw(hs.rect);
		}
		//draw HP cards first
		/*Iterator<CardSpace> it = hpcards.iterator();
		while(it.hasNext()){
			CardSpace hp = it.next();
			cd.paint("BULLETBACK", graphics, hp.rect.x, hp.rect.y, hp.rect.width, hp.rect.height, Color.BLUE, Color.GRAY);
		}*/
		Iterator<Clickable> iter = clickies.values().iterator();
		ArrayList<CardSpace> Char = new ArrayList<CardSpace>();
		ArrayList<CardSpace> Bullet = new ArrayList<CardSpace>();
		while(iter.hasNext()){
			Clickable temp = iter.next();
			if(temp instanceof CardSpace){
				CardSpace crd = (CardSpace)temp;
				if(crd.card.name=="BULLETBACK"){
					Bullet.add(crd);
				}
				else if(crd.card.type==1){
					Char.add(crd);
				}
				else{
					Color inner;
					switch(crd.card.location){
					case 0:
						inner=Color.BLACK;
						break;
					case 1:
						if(crd.card.type==5)
							inner=new Color(100,100,200);
						else{
							inner = ((crd.card.name!="JAIL"||crd.card.name!="DYNAMITE")?inner=new Color(100,200,100):new Color(100,100,200));
						}
						break;
					default:
						inner=new Color(200,100,100);
					}
					//Color outer=client.id==1?Color.RED:Color.BLUE;
					Color outer;
					switch(crd.playerid){
					case 0: outer = Color.RED; break;
					case 1: outer = Color.BLUE; break;
					case 2: outer = Color.CYAN; break;
					case 3: outer = Color.MAGENTA; break;
					case 4: outer = Color.YELLOW; break;
					case 5: outer = Color.ORANGE; break;
					case 6: outer = Color.GREEN; break;
					case 7: outer = Color.LIGHT_GRAY; break;
					case 8: outer = Color.WHITE; break;
					case 9: outer = Color.PINK; break;
					default: outer = Color.BLACK; break;
					}
					/*/cd.paint(crd.card.name, graphics, crd.rect.x, crd.rect.y, crd.rect.width, temp.rect.height, 
							inner,outer);/*/
					crd.paint(graphics);//TODO:DEFAULT PAINTER
				}
			}else if(temp instanceof HandSpace){
				HandSpace hs = (HandSpace)temp;
				graphics.draw3DRect(hs.rect.x, hs.rect.y, hs.rect.width, hs.rect.height, true);
			}else{
				System.out.println("WTF");
			}
		}

		for(CardSpace crd:Bullet){
			crd.paint(graphics);
		}
		for(CardSpace crd:Char){
			crd.paint(graphics);
		}

		if(description==null&&System.currentTimeMillis()-lastMouseMoved>1000){
			//create description
			StringBuilder temp = new StringBuilder();
			Clickable cl = binarySearchCardAtPoint(hoverpoint);
			if (cl instanceof CardSpace) {
				CardSpace cs = (CardSpace) cl;
				if (cs != null && cs.card != null){
					if(cs.card.type==1||cs.card.name.equals("BULLETBACK")){
						temp.append(client.players.get(cs.playerid).name + "\n");
						if(client.players.get(cs.playerid).maxLifePoints>0)
							temp.append(client.players.get(cs.playerid).lifePoints +"HP\n");;
							//TODO: add distance to tooltip?
					}
					if(!cs.card.name.equals("BULLETBACK"))
						temp.append(cs.card.name.replace('_', ' '));
					if(!cs.card.description.equals(""))
						temp.append(" - "+cs.card.description);
					description = temp.toString();
					describeWhere = hoverpoint;
					tooltipWidth = textWidth(description, graphics);
					tooltipHeight = textHeight(description, graphics);
					if(describeWhere.x+tooltipWidth>client.gui.width){
						describeWhere.x = client.gui.width - tooltipWidth - 5;
					}
				}
			}
		}
		if(description!=null){
			Rectangle2D bounds=graphics.getFont().getStringBounds(description, graphics.getFontRenderContext());
			Color temp=graphics.getColor();
			graphics.setColor(Color.YELLOW);
			graphics.fill3DRect(describeWhere.x, describeWhere.y-(int)bounds.getHeight()+32, tooltipWidth, tooltipHeight,false);
			graphics.setColor(Color.BLACK);
			improvedDrawString(description, describeWhere.x, describeWhere.y+30,graphics);
			graphics.setColor(temp);
		}
	}
	public Clickable binarySearchCardAtPoint(Point ep){
		//bsearch method
		int start;
		int end;

		ArrayList<Clickable> al = clickies.values(); //search the values arrayList for...
		if(al.isEmpty()||ep==null)return null;
		int a = 0, b = al.size(), index = al.size() / 2;

		while (a != b) {
			if (ep.y > al.get(index).rect.y + 85) { // the "start" is the value of the card whose bottom is closest to the cursor (and on the cursor)
				a = index + 1;
			} else {
				b = index;
			}
			index = a + (b - a) / 2;
		}
		start = a;
		a = 0;
		b = al.size();
		index = al.size() / 2;
		while (a != b) {
			if (ep.y > al.get(index).rect.y) { // the "end" is the value of the card whose top is closest to the cursor (and on the cursor)
				a = index + 1;
			} else {
				b = index;
			}
			index = a + (b - a) / 2;
		}
		end = a - 1;
		for (int n = end; n>= start; n--) {
			Clickable s = al.get(n);
			if (s.bounds.contains(ep.x, ep.y)) {
				return al.get(n);
			}
		}
		return null;
	}
	public void start2(){
		handPlacer = new ArrayList<HandSpace>(client.numPlayers);
		double theta;
		HandSpace hs = null;
		clear();
		for(int player = 0; player<client.numPlayers; player++){
			theta = -(player-client.id)*(2*Math.PI/client.numPlayers)-Math.PI/2;
			int hsx = client.gui.width/2+(int)((client.gui.width-150)/2*Math.cos(theta));
			int hsy = 280-(int)(220*Math.sin(theta));
			hs = new HandSpace(rectToPoly(hsx, hsy,10,10), player, theta);
			handPlacer.add(hs);
			Card chara=null;
			if(client.players.get(player).character>=0){
				System.out.println(player+":"+Deck.Characters.values()[client.players.get(player).character]);
				chara = new Card(Deck.Characters.values()[client.players.get(player).character]);
			}else if(client.id==player){
				System.out.println(player+":"+Deck.Characters.values()[client.player.character]);
				chara = new Card(Deck.Characters.values()[client.player.character]);
			}
			if(chara!=null){
				int x=(int) hs.rect.x-60;
				int y=(int) hs.rect.y;
				CardSpace csp = new CardSpace(chara,rectToPoly(x,y-60,60,90), player, false, cd.getImage(chara.name), hs);
				//generate HP card
				Card hp = new Card(Deck.CardName.BULLETBACK);
				CardSpace hps = new CardSpace(hp, rectToPoly(x+
						10 * client.players.get(player).maxLifePoints,y-60,60,90),player, false, cd.getImage(hp.name), hs);
				hps.rotate(Math.PI/2);
				hps.move(x + 10 * client.players.get(player).maxLifePoints, y - 60);
				hps.setPartner(csp);
				csp.setPartner(hps);
				//hps.rotate(1);
				clickies.put(hp, hps);
				clickies.put(chara, csp);
				hs.setCharHP(csp, hps);
			}
		}
	}
	public void clear(){
		pointOnCard = null;
		movingCard = null;
		clickies.clear();
	}

	public void mouseClicked(MouseEvent e) {
		Point ep=e.getPoint();

		if(new Rectangle(760, 560, 40, 40).contains(ep)){
			if(client.prompting&&!client.forceDecision){
				client.outMsgs.add("Prompt:-1");
				client.prompting = false;
			}
			return;
		}
		Clickable cl = binarySearchCardAtPoint(ep);
		if (cl instanceof CardSpace) {
			CardSpace cs = (CardSpace) cl;
			if (cs != null && cs.card != null){
				if (e.getButton() == MouseEvent.BUTTON3) cs.rotate(cs.theta+Math.PI/4);
				if(cs.playerid != -1){}
				//client.gui.appendText(String.valueOf(client.players.get(cs.playerid).hand.indexOf(cs.card))+" "+(cs.hs!=null?String.valueOf(cs.hs.cards.indexOf(cs)):""));
				else if(pick != null){}
				//client.gui.appendText(String.valueOf(pick.contains(cs.card)));
			}
			else
				return;
			if (e.getButton() == MouseEvent.BUTTON3) {
				//Put right click stuff here, or not
			}else if (client.prompting){
				if(pick!= null && pick.contains(cs.card)) {
					if (cs.card.type == 1) {
						client.outMsgs.add("Prompt:"
								+ pick.indexOf(cs.card));
						client.player.hand.clear(); //you just picked a character card
						clear();
					} else {
						client.outMsgs.add("Prompt:" + pick.indexOf(cs.card));
					}
					pick = null;
					client.prompting = false;
				} else if(client.forceDecision==false){
					//it's your turn
					if(client.targetingPlayer){
						if(cs.card.type==1||cs.card.name=="BULLETBACK"){
							client.targetingPlayer = false;   
							client.prompting = false;
							client.outMsgs.add("Prompt:" + cs.playerid);
						}
					}
					else if(client.nextPrompt==-1){
						Player p = client.players.get(cs.playerid);
						if(cs.card.location==0){
							client.nextPrompt = p.hand.indexOf(cs.card);
							//client.gui.appendText("Index of card is "+client.nextPrompt);
						}
						else{
							client.nextPrompt = ((0-client.players.get(cs.playerid).field.indexOf(cs.card))-3);
							//client.gui.appendText("lol "+client.nextPrompt);
						}
						client.outMsgs.add("Prompt:"+p.id);
					}
					else{
						client.outMsgs.add("Prompt:" + ((0-client.player.field.indexOf(cs.card))-3));
						pick = null;
						client.prompting = false;
					}
				}
				else{
					System.out.println("i was prompting, but a bad card was given");
				}
			}
		}
		else if(cl == null){
			for(HandSpace cs : handPlacer)
				if(cs.rect.contains(e.getPoint())){
					cl=cs;
				}
			if(cl!=null){
				if(e.getButton()==MouseEvent.BUTTON1)
					((HandSpace)cl).sortHandSpace();
				if(e.getButton()==MouseEvent.BUTTON3)
					((HandSpace)cl).autoSort = !((HandSpace)cl).autoSort;
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		movingCard = binarySearchCardAtPoint(e.getPoint());

		if(movingCard==null){//placer handler
			for(HandSpace cs : handPlacer)
				if(cs.rect.contains(e.getPoint())){
					movingCard=cs;
				}
		}
		if(movingCard!=null){
			pointOnCard = new Point(e.getPoint().x-movingCard.rect.x, e.getPoint().y-movingCard.rect.y);
			//System.out.println("picked up card");
		}
	}
	public void mouseReleased(MouseEvent e) {
		if(movingCard!=null){
			//System.out.println("card dropped");
		}
		movingCard = null;
		description = null;
	}

	public void mouseDragged(MouseEvent e) {
		//System.out.println("dragging");
		lastMouseMoved = System.currentTimeMillis();
		if(movingCard!=null){
			if(movingCard instanceof CardSpace && ((CardSpace)movingCard).animating)
				return;
			movingCard.move(Math.max(0, Math.min(e.getPoint().x-pointOnCard.x,client.gui.getWidth()-55)),Math.max(0, Math.min(e.getPoint().y-pointOnCard.y,client.gui.getHeight()-85))); //replace boundaries with width()/height() of frame?
		}
		else{
			//System.out.println("not dragging");
		}
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {
		lastMouseMoved = System.currentTimeMillis();
		hoverpoint=e.getPoint();
		description=null;
	}
	/**
	 * Scales all objects to the newly resized coordinates.
	 * @param width
	 * @param height
	 * @param width2
	 * @param height2
	 */
	public void resize(int width, int height, int width2, int height2) {
		ArrayList<Clickable> stuff = clickies.values();
		Iterator<Clickable> iter = stuff.iterator();
		for(HandSpace hs: handPlacer){
			hs.move(hs.rect.x*width2/width, hs.rect.y*height2/height);
		}
	}
	/**
	 * Sets the given players's HP and updates the bullet display accordingly
	 * @param playerid the id of the player whose HP changed
	 * @param lifePoints the amount of HP the player lost
	 */
	public void setHP(int playerid, int lifePoints) {
		if(lifePoints == 0) //bug when saloon is played when you have full hp
			return;
		CardSpace hpc = handPlacer.get(playerid).hp;
		hpc.translate(-10*lifePoints, 0);
	}

	public Polygon rectToPoly(int x, int y, int width, int height){
		int[] xs = {x, x, x+width, x+width};
		int[] ys = {y, y+height, y+height, y};
		Polygon temp = new Polygon(xs, ys, 4);
		return temp;
	}
}

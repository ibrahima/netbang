package ucbang.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import ucbang.core.Card;
import ucbang.core.Deck;
import ucbang.network.Client;

public class Field implements MouseListener, MouseMotionListener{
	Client client;
	public BSHashMap<Card, Clickable> clickies = new BSHashMap<Card, Clickable>();
	CardDisplayer cd;
	Point pointOnCard;
	Clickable movingCard;
	Card clicked;
	ArrayList<Card> pick;
	ArrayList<HandSpace> handPlacer = new ArrayList<HandSpace>(); //to avoid npe
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
		clickies.put(card, new CardSpace(card, new Rectangle(x,y,60,90), player, field));
	}
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
		int xoffset = (player==client.id?30*(client.player.hand.size()-1):30*(client.players.get(player).hand.size()-1));
		if(card.type==1){//this a character card
			int x=350;
			int y=200;
			clickies.put(card, new CardSpace(card, new Rectangle(x, y,60,90), player, false));
		}else{
			HandSpace hs = handPlacer.get(player);
			int x=(int) hs.rect.x+hs.rect.width+xoffset;
			int y=(int) hs.rect.y+(field?(player==client.id?-100:100):0); //more trinarytrinary fun!
			CardSpace cs = new CardSpace(card, new Rectangle(x, y,60,90), player, field);
			clickies.put(card, cs);
                        if(field == false)
                            hs.addCard(cs);
		}
	}
	int textHeight(String message, Graphics2D graphics){
		int lineheight=(int)graphics.getFont().getStringBounds("|", graphics.getFontRenderContext()).getHeight();
		return message.split("\n").length*lineheight;
	}
	int textWidth(String message, Graphics2D graphics){
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
		String[] lines = message.split("\n");
		for(int i=0;i<lines.length;i++){
			graphics.drawString(lines[i], x, y+i*lineheight);
		}
	}
	public void paint(Graphics2D graphics){
		for(HandSpace hs : handPlacer)
			graphics.draw(hs.rect);
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
                                            else
                                                    inner=new Color(100,200,100);
                                            break;
                                    default:
                                            inner=new Color(200,100,100);
                                    }
                                    Color outer=client.id==1?Color.RED:Color.BLUE;
                                    cd.paint(crd.card.name, graphics, crd.rect.x, crd.rect.y, crd.rect.width, temp.rect.height, 
                                                            inner,outer);
                                }
			}else if(temp instanceof HandSpace){
				HandSpace hs = (HandSpace)temp;
				graphics.draw3DRect(hs.rect.x, hs.rect.y, hs.rect.width, hs.rect.height, true);
			}else{
				System.out.println("WTF");
			}
		}
                
                for(CardSpace crd:Bullet){
                    cd.paint(crd.card.name, graphics, crd.rect.x, crd.rect.y, crd.rect.width, crd.rect.height, Color.BLACK, Color.BLACK);
                }
                for(CardSpace crd:Char){
                    cd.paint(crd.card.name, graphics, crd.rect.x, crd.rect.y, crd.rect.width, crd.rect.height, Color.BLACK, Color.BLACK);
                }
                
		if(description==null&&System.currentTimeMillis()-lastMouseMoved>1000){
			//create description
			Clickable cl = binarySearchCardAtPoint(hoverpoint);
			if (cl instanceof CardSpace) {
				CardSpace cs = (CardSpace) cl;
				if (cs != null && cs.card != null){
					if(cs.card.description.equals(""))
						description = cs.card.name.replace('_', ' ');
					else
						description = cs.card.name+" - "+cs.card.description;
					describeWhere = hoverpoint;
					tooltipWidth = textWidth(description, graphics);
					tooltipHeight = textHeight(description, graphics);
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
			if (s.rect.contains(ep.x, ep.y)) {
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
			theta = (player-client.id)*(2*Math.PI/client.numPlayers)-Math.PI/2;
			hs=new HandSpace(new Rectangle((client.gui.width-100)/2+(int)((client.gui.width-300)*Math.cos(theta)),
					280-(int)(220*Math.sin(theta)),10,10), player);
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
				int x=(int) hs.rect.x-90;
				int y=(int) hs.rect.y;
				CardSpace csp = new CardSpace(chara,new Rectangle(x,y,60,90), player, false);
				//generate HP card
				Card hp = new Card(Deck.CardName.BULLETBACK);
				CardSpace hps = new CardSpace(hp, new Rectangle(x+
						10 * client.players.get(player).maxLifePoints,y+30,90,60),player, false);
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
		////the ugly proxy skip turn button
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
				System.out.println("Clicked on " + cs.card.name + (pick!=null?"whose index is " + pick.indexOf(cs.card):"; not picking"));
			}
			else
				return;
			if (e.getButton() == MouseEvent.BUTTON3) {
				//Put right click stuff here, or not
			}else if (client.prompting){
				if(pick.contains(cs.card)) {
					System.out.println("000000000000000000000000000000 "+pick.size()+" "+client.player.hand.size());
					System.out.println("sending prompt...");
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
					client.gui.appendText("INDEX IN FIELD OF CARD IS"+client.player.field.indexOf(cs.card));
					client.outMsgs.add("Prompt:" + -(client.player.field.indexOf(cs.card)+3));
					pick = null;
					client.prompting = false;
				}
				else{
					System.out.println("i was prompting, but a bad card was given");
				}
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
			movingCard.move(Math.max(0, Math.min(e.getPoint().x-pointOnCard.x,745)),Math.max(0, Math.min(e.getPoint().y-pointOnCard.y,515))); //replace boundaries with width()/height() of frame?
		}
		else{
			//System.out.println("not dragging");
		}
	}

	public class BSHashMap<K,V> extends HashMap<K,V>{
		ArrayList<V> occupied = new ArrayList<V>();

		public V put(K key, V value){
			occupied.add(value);
			return super.put(key, value);
		}

		public ArrayList<V> values(){
			ArrayList<V> al = new ArrayList<V>();
			Collections.sort(occupied, new Comparator(){
				public int compare(Object o1, Object o2) {
					return ((Comparable<Object>)o1).compareTo(o2);
				}
			});
			al.addAll(occupied);
			return al;
		}
		public void clear(){
			occupied.clear();
			super.clear();
		}
		public V remove(Object o){
			occupied.remove(get(o));
			V oo = super.remove(o);
			return oo;
		}
	}

	/*
	 * Contains a card and a rectangle
	 */
	private class CardSpace extends Clickable{
		public Card card;
		public boolean field;
		/**
		 * @param c The card this CardSpace describes
		 * @param r The bounds of the card
		 * @param player The player who owns the card
		 * @param f Whether the card is on the field
		 * @param partner The parent container of the card
		 */
		public CardSpace(Card c, Rectangle r, int player, boolean f){
			super(r);
			card = c;
			rect = r;
			playerid = player;
			field = f;
		}
	}

	private class HandSpace extends Clickable{
		ArrayList<CardSpace> cards = new ArrayList<CardSpace>();
		CardSpace character, hp;
		public HandSpace(Rectangle r, int player){
			super(r);
			playerid = player;
		}
		public void setCharHP(CardSpace character, CardSpace hp){
			this.character = character;
			this.hp = hp;
		}
		public void addCard(CardSpace card){
			cards.add(card);
		}
		public CardSpace removeLast(){
			return cards.remove(cards.size()-1);
		}
		public void move(int x, int y){
			int dx = x-rect.x;
			int dy = y-rect.y;
			super.move(x, y);
			Iterator<CardSpace> iter = cards.iterator();
			while(iter.hasNext()){
				iter.next().translate(dx, dy);
			}
			if(character!=null)character.translate(dx, dy);
			if(hp!=null)hp.translate(dx, dy);
		}
		public void translate(int dx, int dy){
			super.translate(dx, dy);
			Iterator<CardSpace> iter = cards.iterator();
			while(iter.hasNext()){
				iter.next().translate(dx, dy);
			}
			if(character!=null)character.translate(dx, dy);
			if(hp!=null)hp.translate(dx, dy);
		}
	}
	private abstract class Clickable implements Comparable<Clickable>{
		public Rectangle rect;
		public int location; //position of card on field or in hand
		public int playerid;
		public AffineTransform at;
		private int oldrotation=0;
		private Clickable partner;
		public Clickable(Rectangle r){
			rect=r;
		}
		public int compareTo(Clickable o) {
			if(o.rect.getLocation().y!=rect.getLocation().y)
				return ((Integer)rect.getLocation().y).compareTo(o.rect.getLocation().y);
			else
				return ((Integer)rect.getLocation().x).compareTo(o.rect.getLocation().x);
		}
		public void move(int x, int y){
			int dx = x-rect.x;
			int dy = y-rect.y;
			if(at!=null)at.translate(rect.x-x, rect.y-y);
				rect.setLocation(x, y);
			if(partner!=null){
				partner.translate(dx, dy);
			}
		}
		public void setPartner(Clickable partner){
			this.partner=partner;
		}
		public void rotate(int quadrant){//rotates in terms of 90 degree increments. call with 0 to reset.
			int realrotation=quadrant-oldrotation;
			if(realrotation>0 && realrotation<4){
				if(this instanceof CardSpace){
					cd.rotateImage(((CardSpace)this).card.name, quadrant);
				}
				at = AffineTransform.getQuadrantRotateInstance(realrotation, rect.x+rect.width/2, rect.y+rect.height/2);
				oldrotation=quadrant;
				PathIterator iter = rect.getPathIterator(at);
				int i=0;
				float[] pts= new float[6];
				int newx=-1, newy=-1, newwidth=-1, newheight=-1;
				while(!iter.isDone()){
					int type = iter.currentSegment(pts);
					switch(type){
					case PathIterator.SEG_MOVETO :
						//temp.add((int)pts[0],(int)pts[1]);
						//System.out.println(pts[0]+","+pts[1]);
						break;
					case PathIterator.SEG_LINETO :
						if(i==1){
							newx=(int) pts[0];//misnomers for this part lol.
							newy=(int) pts[1];
						}else if(i==3){
							newwidth=(int)Math.abs(newx-(int)pts[0]);
							newheight=(int)Math.abs(newy-(int)pts[1]);
							newx=(int) pts[0];
							newy=(int) pts[1];
						}
						break;
					}
					i++;
					iter.next();
				}
				rect = new Rectangle(newx, newy, newwidth, newheight);
				System.out.println(rect);
				at=null;
			}else{
				//at=null;
			}
		}
		public void translate(int dx, int dy){
			rect.translate(dx, dy);
		}
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {
		lastMouseMoved = System.currentTimeMillis();
		hoverpoint=e.getPoint();
		description=null;
	}
	public void resize(int width, int height, int width2, int height2) {
		ArrayList<Clickable> stuff = clickies.values();
		Iterator<Clickable> iter = stuff.iterator();
		while(iter.hasNext()){
			Clickable temp = iter.next();
			temp.move(temp.rect.x*width2/width, temp.rect.y*height2/height);
		}
		
	}
	public void setHP(int playerid, int lifePoints) {
		CardSpace hpc = handPlacer.get(playerid).hp;
		hpc.move(hpc.rect.x-10*lifePoints, hpc.rect.y);
	}
}

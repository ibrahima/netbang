package ucbang.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
	public BSHashMap<Card, Clickable> cards = new BSHashMap<Card, Clickable>();
	CardDisplayer cd;
	Point pointOnCard;
	Clickable movingCard;
	Card clicked;
	ArrayList<Card> pick;
        
	ArrayList<CardSpace> handPlacer = new ArrayList<CardSpace>(); //to avoid npe
	ArrayList<CardSpace> characters = new ArrayList<CardSpace>();
	String description;
	Point describeWhere;
        
	public Field(CardDisplayer cd, Client c) {
		this.cd=cd;
                client = c;
	}
	public void add(Card card, int x, int y, int player){
		cards.put(card, new CardSpace(card, new Rectangle(x,y,60,90), player));
	}

	public void add(Card card, int player){
		int xoffset = (player==client.id?30*(client.player.hand.size()-1):30*(client.players.get(player).hand.size()-1));
                
		if(card.type==1){//this a character card
			int x=350;
			int y=200;
			cards.put(card, new CardSpace(card, new Rectangle(x, y,60,90), player));
			System.out.println("Field added a character");
		}else{
			int x=(int) handPlacer.get(player).rect.x+handPlacer.get(player).rect.width+xoffset;
			int y=(int) handPlacer.get(player).rect.y;
			cards.put(card, new CardSpace(card, new Rectangle(x, y,60,90), player));		
			System.out.println("Field added a card on its own! Magic!");
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
                for(CardSpace cs : handPlacer)
                    graphics.draw(cs.rect);
		Iterator<Clickable> iter = cards.values().iterator();
		while(iter.hasNext()){
			Clickable temp = iter.next();
			if(temp instanceof CardSpace){
				CardSpace crd = (CardSpace)temp;
				cd.paint(crd.card.name ,graphics, temp.rect.x, temp.rect.y,
						(crd.card.location==0?Color.BLACK:(crd.card.location==1?(crd.card.type==5?new Color(100,100,200):new Color(100,200,100)):new Color(200,100,100))),
						client.id==1?Color.RED:Color.BLUE); //replace this last parameter
				//this thing is really ugly, please make it less so. Readability of code is good. Just because ? exists
				//doesn't mean you have to use it for stuff like this, lol. Please define the colors above with nested
				//ifs or switches or soemthing, and then use them.			
			}

		}
		if(description!=null){
			Rectangle2D bounds=graphics.getFont().getStringBounds(description, graphics.getFontRenderContext());
			Color temp=graphics.getColor();
			graphics.setColor(Color.YELLOW);
			graphics.fill3DRect(describeWhere.x, describeWhere.y-(int)bounds.getHeight()+32, textWidth(description, graphics), textHeight(description, graphics),false);
			graphics.setColor(Color.BLACK);
			improvedDrawString(description, describeWhere.x, describeWhere.y+30,graphics);
			graphics.setColor(temp);
		}
	}
        public Clickable binarySearchCardAtPoint(Point ep){
            //bsearch method
             int start;
             int end;

             ArrayList<Clickable> al = cards.values(); //search the values arrayList for...

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
            handPlacer = new ArrayList<CardSpace>(client.numPlayers);
            double theta;
            for(int player = 0; player<client.numPlayers; player++){
                theta = (player-client.id)*(2*Math.PI/client.numPlayers)-Math.PI/2;
                handPlacer.add(new CardSpace(null, new Rectangle(350+(int)(250*Math.cos(theta)),280-(int)(220*Math.sin(theta)),10,10), player));  
            }
            clear();
            for(int i=0;i<client.players.size();i++){
            	if(i!=client.id)
            		System.out.println(i+":"+Deck.Characters.values()[client.players.get(i).character]);
            	else
            		System.out.println(i+":"+Deck.Characters.values()[client.player.character]);
            }
        }
        public void clear(){
            Point pointOnCard = null;
            CardSpace movingCard = null;
            cards.clear();
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
					if (cs != null && cs.card != null)
						System.out.println("Clicked on " + cs.card.name);
					else
						return;
					if (e.getButton() == MouseEvent.BUTTON3) {
						System.out.println(cs.card.description);
						description = cs.card.description;
						describeWhere = ep;
					} else if (client.prompting && pick.contains(cs.card)) {
						System.out.println("sending prompt...");
						if (cs.card.type == 1) {
							client.outMsgs.add("Prompt:"
									+ pick.indexOf(cs.card));
							client.player.hand.clear(); //you just picked a character card
							clear();
						} else {
							client.outMsgs.add("Prompt:"
									+ pick.indexOf(cs.card));
						}
						client.prompting = false;
					} else { //TODO: debug stuff
						if (client.prompting) {
							System.out.println("i was prompting");
							if (!client.player.hand.contains(cs.card)) {
								System.out
										.println("but the arraylist didn't contain the card i was looking for!");
								System.out.println(cs.card + " "
										+ client.player.hand);
							}
						}
					}
				}
	}

	public void mousePressed(MouseEvent e) {
	    movingCard = binarySearchCardAtPoint(e.getPoint());
	    
	    if(movingCard==null){//placer handler
	        for(CardSpace cs : handPlacer)
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

        public CardSpace(Card c, Rectangle r, int player){
            card = c;
            rect = r;
            playerid = player;
        }
        
    }
    private class HandSpace extends Clickable{
    	ArrayList<CardSpace> cards = new ArrayList<CardSpace>();
    	public HandSpace(Rectangle r, int player){
    		rect=r;
    		playerid=player;
    	}
    	public void addCard(CardSpace card){
    		cards.add(card);
    	}
    	public void move(int x, int y){
    		super.move(x, y);
    		Iterator<CardSpace> iter = cards.iterator();
    		while(iter.hasNext()){
    			iter.next().move(x, y);
    		}
    	}
    }
    private abstract class Clickable implements Comparable<Clickable>{
        public Rectangle rect;
        public int location; //position of card on field or in hand
        public int playerid;
        public int compareTo(Clickable o) {
            if(o.rect.getLocation().y!=rect.getLocation().y)
                return ((Integer)rect.getLocation().y).compareTo(o.rect.getLocation().y);
            else
                return ((Integer)rect.getLocation().x).compareTo(o.rect.getLocation().x);
        }
        public void move(int x, int y){
        	rect.setLocation(x, y);
        }
    }

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}

package ucbang.core;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedHashMap;
import ucbang.gui.*;
public class Field {
	Player[] players;//in the future it should get the cards directly from players?
	LinkedHashMap<Card,Point> cards = new LinkedHashMap<Card,Point>();
	CardDisplayer cd;
	public Field(CardDisplayer cd) {
		this.cd=cd;
	}
	public void add(Card card, Point point){
		cards.put(card,point);
	}
	public void paint(Graphics2D graphics){
		Iterator<Card> iter = cards.keySet().iterator();
		while(iter.hasNext()){
			Card temp=iter.next();
			cd.paint(temp.name,graphics,cards.get(temp).x,cards.get(temp).y);
		}
	}
}

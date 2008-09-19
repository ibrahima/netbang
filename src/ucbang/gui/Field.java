package ucbang.gui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.LinkedHashMap;

import ucbang.core.Card;
import ucbang.core.Player;
import ucbang.core.Deck.CardName;
public class Field implements MouseListener{
	Player[] players;//in the future it should get the cards directly from players?
	LinkedHashMap<Card,Point> cards = new LinkedHashMap<Card,Point>();
	CardDisplayer cd;
	public Field(CardDisplayer cd) {
		this.cd=cd;
	}
	public void add(Card card, int x, int y){
		cards.put(card,new Point(x,y));
	}
	public void paint(Graphics2D graphics){
		Iterator<Card> iter = cards.keySet().iterator();
		while(iter.hasNext()){
			Card temp=iter.next();
			cd.paint(temp.name,graphics,cards.get(temp).x,cards.get(temp).y);
		}
	}
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Point ep=e.getPoint();
		Iterator<Card> iter = cards.keySet().iterator();
		while(iter.hasNext()){
			Card temp = iter.next();
			Point p = cards.get(temp);
			if(ep.x>p.x&&ep.x<p.x+55&&ep.y>p.y&&ep.y<p.y+85){
				System.out.println("Clicked on " + temp);
			}
		}
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
}

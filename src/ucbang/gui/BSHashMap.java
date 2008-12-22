package ucbang.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import ucbang.core.Card;

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
		if(o instanceof Card){
			CardSpace cs =(CardSpace)get(o);
			if(cs==null){
				//client.gui.appendText("WTFWTFWTF");
			}
			if(cs.hs != null){
				if(!cs.field)
					cs.hs.cards.remove(cs);
				else
					cs.hs.fieldCards.remove(cs);
				if(cs.hs.autoSort){
					cs.hs.sortHandSpace();
				}
			}
			//System.out.println(cs.card.name+" "+cs.playerid+" "+(cs.hs==null)+" "+handPlacer.get(cs.playerid).fieldCards.contains(cs));
		}                        
		occupied.remove(get(o));
		V oo = super.remove(o);
		return oo;
	}
}
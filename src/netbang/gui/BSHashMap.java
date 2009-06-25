package netbang.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class BSHashMap<K, V> extends HashMap<K, V> {
    /**
     * 
     */
    private static final long serialVersionUID = -8116258538167187433L;
    ArrayList<V> occupied = new ArrayList<V>();

    public V put(K key, V value) {
        occupied.add(value);
        return super.put(key, value);
    }

    public ArrayList<V> values() {
        ArrayList<V> al = new ArrayList<V>();
        Collections.sort(occupied, new Comparator<V>() {
            public int compare(V o1, V o2) {
                return ((Comparable<V>) o1).compareTo(o2);
            }
        });
        al.addAll(occupied);
        return al;
    }

    public void clear() {
        occupied.clear();
        super.clear();
    }

    public V remove(Object o) {
        /*if (o instanceof Card) {
            CardSpace cs = (CardSpace)get(o);
            if (cs == null) {
                System.out.println("ERROR");
            }
            if (cs.hs != null) {
                if (!cs.field)
                    cs.hs.cards.remove(cs);
                else
                    cs.hs.fieldCards.remove(cs);
                if (cs.hs.autoSort) {
                    cs.hs.sortHandSpace();
                }
            }
            // System.out.println(cs.card.name+" "+cs.playerid+" "+(cs.hs==null)+" "+handPlacer.get(cs.playerid).fieldCards.contains(cs));
        }*/
        occupied.remove(get(o));
        V oo = super.remove(o);
        return oo;
    }
}
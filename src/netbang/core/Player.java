package netbang.core;

import java.util.ArrayList;

public class Player {
    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int id; // temporary probably
    public String name;
    public Constants.Role role;
    public int maxLifePoints;

    public int bangs = 0;
    
    //public int weaponRange; // only counts for Bang! cards OBSOLETE
    //public int realRange; // also counts for Panics, etc.
    //public int distance; // your protection against other player's Bang!s
        
    public ArrayList<Card> hand = new ArrayList<Card>();
    public ArrayList<Card> field = new ArrayList<Card>();

    public int lifePoints;
    public int character = -1; // default -1 = no character
        public Card characterCard; // default -1 = no character
    /*public String toString(){
        return name;
    }*/
}

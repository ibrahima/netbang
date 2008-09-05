package ucbang.core;

import java.util.ArrayList;

public class Player {
    public Player() {
    }
    
    String name;
    public Enum role;
    
    int weaponRange; //only counts for Bang! cards
    int realRange; //also counts for Panics, etc.
    int distance; //your protection against other player's Bang!s
    ArrayList<Card> hand = new ArrayList<Card>();
    Field field;
    
    int lifePoints;
    int specialDraw;
    
    int character = -1; //default -1 = no character
}

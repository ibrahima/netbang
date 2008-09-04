package ucbang.core;

import java.util.ArrayList;

public class Player {
    public Player() {
    }
    
    String name;
    Enum role;
    
    int weaponRange; //only counts for Bang! cards
    int realRange; //also counts for Panics, etc.
    int distance; //your protection against other player's Bang!s
    ArrayList<Card> hand = new ArrayList<Card>();
    Field field;
    
    
}

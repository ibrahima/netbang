package ucbang.core;

import java.util.ArrayList;
import java.util.Arrays;

public class Bang {
    public Bang() {
        start(4);
    }
    
    public static void main(String[] args){
        new Bang();
    }
    
    public Player[] players;
    
    public int turn;
    
    public ArrayList<Card> drawPile = new ArrayList<Card>(); //the card on the bottom in stored in index 0, the card on top is stored in index size()-1
    public ArrayList<Card> discardPile = new ArrayList<Card>();
    
    public enum CardName {BANG, BEER, MISS, PANIC, CAT_BALLOU, VOLCANIC};
    public enum Role {SHERIFF, DEPUTY, OUTLAW, RENEGADE};
    
    /**
     * Create p players.
     * Create a draw pile.
     * Show everyone their roles.
     * Give them a choice between two character cards.
     * Give sheriff the first turn.
     * Draw cards equal to the number of life points.
     * Sheriff gets an additional card.
     * @param p
     */
    public void start(int p){
        turn = 0;
        
        //Create Players
        players = new Player[p];
        Arrays.fill(players, new Player());
        
        //Create a drawPile
        Enum[] cards = {CardName.BANG, CardName.BANG, CardName.BANG, 
            CardName.BANG, CardName.BANG, CardName.BANG, CardName.BANG, 
            CardName.BANG, CardName.BANG, CardName.BANG, CardName.BANG, 
            CardName.BEER, CardName.BEER, CardName.BEER, CardName.PANIC, 
            CardName.PANIC, CardName.PANIC, CardName.CAT_BALLOU, 
            CardName.VOLCANIC};
        ArrayList<Enum> allCards = new ArrayList<Enum>();
        for(Enum e: cards)
            allCards.add(e);
        while(allCards.size()>0){
            drawPile.add(new Card(allCards.remove((int)(Math.random()*allCards.size()))));
        }
        
        //Assign roles
        ArrayList<Enum> roles = new ArrayList<Enum>();
        switch(p){
            case 4:
                roles.add(Role.SHERIFF); roles.add(Role.OUTLAW); 
                roles.add(Role.OUTLAW); roles.add(Role.RENEGADE); break;
            case 5:
                roles.add(Role.SHERIFF); roles.add(Role.OUTLAW); 
                roles.add(Role.OUTLAW); roles.add(Role.RENEGADE); 
                roles.add(Role.DEPUTY); break;
            case 6:
                roles.add(Role.SHERIFF); roles.add(Role.OUTLAW); 
                roles.add(Role.OUTLAW); roles.add(Role.RENEGADE); 
                roles.add(Role.DEPUTY); roles.add(Role.OUTLAW); break;
            case 7:
                roles.add(Role.SHERIFF); roles.add(Role.OUTLAW); 
                roles.add(Role.OUTLAW); roles.add(Role.RENEGADE); 
                roles.add(Role.DEPUTY); roles.add(Role.OUTLAW); 
                roles.add(Role.DEPUTY); break;
            case 8:
                roles.add(Role.SHERIFF); roles.add(Role.OUTLAW); 
                roles.add(Role.OUTLAW); roles.add(Role.RENEGADE); 
                roles.add(Role.DEPUTY); roles.add(Role.OUTLAW); 
                roles.add(Role.OUTLAW); roles.add(Role.RENEGADE); break;
            default: 
                System.out.print("Bad number of players!"); System.exit(0); break;
        }
        for(int n=0; n<players.length; n++)
            players[n].role = roles.remove((int)(Math.random()*roles.size()));
        for(Card s: drawPile)
            System.out.print(s.name+" ");
        System.out.print("\n");
        
        playerDrawCard(players[0], 10);
        for(Card s: drawPile)
            System.out.print(s.name+" ");
        System.out.print("\n");
        for(Card s: players[0].hand)
            System.out.print(s.name+" ");
        System.out.print("\n");
    }
    
    public void nextTurn(){
    
    }
    
    /**
     * Plays a card
     */
    public void playCard(){
    
    }
    
    /**
     * Adds the top n card(s) of the drawPile to Player p's hand
     * @param p, n
     * @return
     */
    public void playerDrawCard(Player p, int n){
        for(int m=0; m<n; m++)
            p.hand.add(drawCard());
    }
    
    /**
     * Flips the top card of the drawPile. This card is then put in the discard 
     * pile. Used for barrels and other effects.
     * @return
     */
    public Card flipCard(){
        Card c = drawCard();
        discardPile.add(drawCard());
        return c;
    }
    
    /**
     * Draws one card. This card is either returned to the flipCard method or
     * the playerDrawCard method.
     * @param
     * @return Card
     */
    public Card drawCard(){
        if(drawPile.size()==0){
            shuffleDeck();
        }
        return drawPile.remove(drawPile.size()-1);
    }
    
    /**
     * Shuffles the discard pile back into the deck.
     * Only used when draw pile is empty
     */
    public void shuffleDeck(){
        
    }
}

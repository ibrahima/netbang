package ucbang.core;

import java.util.ArrayList;
import java.util.Arrays;

import ucbang.gui.ClientGUI;

public class Bang {
    public Bang() {
        gui = new ClientGUI();
        gui.setVisible(true);
        start(1);
    }
    
    public static void main(String[] args){
        new Bang();
    }
    
    public Player[] players;
    public int numPlayers;
    
    public int turn;
    
    public ArrayList<Card> drawPile = new ArrayList<Card>(); //the card on the bottom in stored in index 0, the card on top is stored in index size()-1
    public ArrayList<Card> discardPile = new ArrayList<Card>();
    
    public enum CardName {BANG, MISS, BEER, BARREL, DUEL, INDIANS, GATLING, DYNAMITE, SALOON, WELLS_FARGO, STAGECOACH, GENERAL_STORE, CAT_BALLOU, PANIC, JAIL, APPALOOSA, MUSTANG, VOLCANIC, SCHOFIELD, REMINGTON, REV_CARBINE, WINCHESTER};
    public enum Characters {BART_CASSIDY, BLACK_JACK, CALAMITY_JANET, EL_GRINGO, JESSE_JONES, JOURDONNAIS, KIT_CARLSON, LUCKY_DUKE, PAUL_REGRET, PEDRO_RAMIREZ, ROSE_DOOLAN, SID_KETCHUM, SLAB_THE_KILLER, SUZY_LAFAYETTE, VULTURE_SAM, WILLY_THE_KID, APACHE_KID, BELLE_STAR, BILL_NOFACE, CHUCK_WENGAM, DOC_HOLYDAY, ELENA_FUENTE, GREG_DIGGER, HERB_HUNTER, JOSE_DELGADO, MOLLY_STARK, PAT_BRENNAN, PIXIE_PETE, SEAN_MALLORY, TEQUILA_JOE, VERA_CUSTER};
    public enum Role {SHERIFF, DEPUTY, OUTLAW, RENEGADE};
    
    public ClientGUI gui;
    
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
        //Create Players
        numPlayers = p;
        players = new Player[p];
        Arrays.fill(players, new Player());
        
        //Assign roles
        ArrayList<Enum> roles = new ArrayList<Enum>();
        switch(p){
            case 1: //DEBUG MODE
                roles.add(Role.SHERIFF); break;
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
        
        //Assign character cards
        ArrayList<Enum> charList = new ArrayList<Enum>();
        for(Enum e: Characters.values())
            charList.add(e);
        for(Player player: players){
            drawPile.add(new Card(charList.remove((int)(Math.random()*charList.size()))));
            drawPile.add(new Card(charList.remove((int)(Math.random()*charList.size()))));
            playerDrawCard(player, 2);
        }
        
        //Make players choose characters; wait
        for(Player player: players){
            //doesn't prompt all players at the same time
            System.out.println("1. " + player.hand.get(0).name + " HP: " + player.hand.get(0).special);
            System.out.println("2. " + player.hand.get(1).name + " HP: " + player.hand.get(1).special);
            Card c = player.hand.get(gui.promptChooseCard(player.hand));
            player.character = c.ordinal;
            player.lifePoints = c.special; //special is hp for char cards
            playerDiscardHand(player);
        }
        while(!areCharactersChosen()){
            try{
                    Thread.sleep(300); //don't check too often
            }
            catch(InterruptedException e){}
        }
        
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
        
        //draw cards equal to lifepoints
        for(Player player: players){
            playerDrawCard(player, player.lifePoints);
        }
        
        System.out.print("Cards in draw pile: ");
        for(Card s: drawPile)
            System.out.print(s.name+" ");
        System.out.print("\nCards in hand: ");
        for(Card s: players[0].hand)
            System.out.print(s.name+" ");
        System.out.print("\nYou are: " + Characters.values()[players[0].character] + "\n");
        
        //Give Sheriff the first turn (turn 0)
        for(int n=0; n<p; n++){
            if(players[n].role==Role.SHERIFF){
                turn=n-1;
                break;
            }
        }
        nextTurn(); 
    }
    
    public void nextTurn(){
        turn++;
        
        
        //check if player is dead
        int oldturn = turn;
        while(players[turn%numPlayers].lifePoints==0&&turn-oldturn<numPlayers){
            turn++;
        }
        if(turn-oldturn>=numPlayers){
            //that guy wins!
            //TODO: add check to see if the only remaining players who are all on the same team
        }
        
        //check jail/dynamite
        
        //draw two cards
        if(players[turn%numPlayers].specialDraw==0){
            playerDrawCard(players[turn%numPlayers], 2);
        }
        else{
            //Yuck, there's alot of characters with this ability
        }
    }
    
    /**
     * Plays a card. This is one of the functions used to connect the GUI to the game.
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
     * Discards Player p's hand
     */
    public void playerDiscardHand(Player p){
        for(int n=p.hand.size()-1; n>=0; n--)
            playerDiscardCard(p, n);
    }
    
    /**
     * Discards card n in Player p's hand
     */
    public void playerDiscardCard(Player p, int n){
        Card c = p.hand.get(n);
        //is card a character card
        if(c.type==1){
            p.hand.remove(c);
        }
        else{
            discardPile.add(c);
        }
            
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
        if(drawPile.size()>0){
            System.out.println("Error: did not need to shuffleDeck()");
            return;
        }
        while(discardPile.size()>0){
            drawPile.add(discardPile.remove((int)Math.random()*discardPile.size()));
        }
    }

    public boolean areCharactersChosen(){
        for(Player p:players){
            if(p.character==-1){
                return false;
            }
        }
        return true;
    }
}

package ucbang.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Card {
    public Card() {
    }
    
    public static enum play{DAMAGE, HEAL, MISS, DRAW, STEAL, DISCARD}; //played cards
    public static enum field{DAMAGE, HEAL, MISS, DRAW, STEAL, DISCARD, DYNAMITE, GUN, HORSE_RUN, HORSE_CHASE}; //field cards
    
    public Card(Enum e) {
        this.e = e;
        ordinal = e.ordinal();
        name = e.toString();
        if(e instanceof Bang.Characters){
            type = 1;
            int[] threehp = new int[]{3, 6, 8, 16, 21, 27, 28, 30};
            if(Arrays.binarySearch(threehp, ordinal)>=0 && ordinal == threehp[Arrays.binarySearch(threehp, ordinal)]){ //awkward way of doing contains
                special = 3;
            }
            else
                special = 4;
        }
        else{
            //TODO: find out what kind of card it is
         
            switch((Bang.CardName)e){
                //put all direct damage cards here
                case PUNCH: type = 2; target = 2; range = 1; effect = play.DAMAGE.ordinal(); break;
                case BANG: type = 2; special = 1; target = 2; effect = play.DAMAGE.ordinal(); break;
                case GATLING: type = 2; target = 4; effect = play.DAMAGE.ordinal(); break;
                case HOWITZER: type = 3; target = 4; effect = play.DAMAGE.ordinal(); break;
                case INDIANS: type = 2; target = 4; effect = play.DAMAGE.ordinal(); break;
                case KNIFE: type = 2; target = 2; range = 1; effect = play.DAMAGE.ordinal(); break;
                case BUFFALO_RIFLE: type = 3; target = 2; effect = play.DAMAGE.ordinal(); break;
                case SPRINGFIELD: type = 2; target = 2; discardToPlay = true; effect = play.DAMAGE.ordinal(); break;
                case PEPPERBOX: type = 3; target = 2; effect = play.DAMAGE.ordinal(); break;
                case DERRINGER: effect = play.DAMAGE.ordinal(); effect2 = play.DRAW.ordinal(); break;
                
                case DUEL: break;
                case DYNAMITE: break;
                
                case MISS: effect = play.MISS.ordinal(); break;
                case DODGE: effect = play.MISS.ordinal(); break;
                case BIBLE: effect = play.MISS.ordinal(); break;
                case IRON_PLATE: effect = play.MISS.ordinal(); break;
                case SOMBRERO: effect = play.MISS.ordinal(); break;
                case TEN_GALLON_HAT: effect = play.MISS.ordinal(); break;
            
                case BARREL: break;
                
                case WELLS_FARGO: type = 2; range = 3; effect = play.DRAW.ordinal(); break;
                case STAGECOACH: type = 2; range = 2; effect = play.DRAW.ordinal(); break;
                case CONESTOGA: type = 3; range = 2; effect = play.DRAW.ordinal(); break;
                case PONY_EXPRESS: type = 3; range = 3; effect = play.DRAW.ordinal(); break;
                case GENERAL_STORE: type = 2; range = 1; effect = play.DRAW.ordinal(); break; //fix general store
                
                case JAIL: break;
                
                case APPALOOSA: break;
                case MUSTANG: break;
                case HIDEOUT: break;
                case SILVER: break;
                
                case BEER: break;
                case TEQUILA: break;
                case WHISKY: break;
                case CANTEEN: break;
                case SALOON: break;
                
                case BRAWL: break;
                case CAN_CAN: break;
                case RAG_TIME: break;
                case PANIC: break;
                case CAT_BALLOU: break;
                
                case VOLCANIC: break;
                case SCHOFIELD: break;
                case REMINGTON: break;
                case REV_CARBINE: break;
                case WINCHESTER: break;
                
                default: break; //special = 1; type = 2; effect = play.DAMAGE.ordinal(); break; //all cards left untreated are treated as bangs
            }
        }
    }
    
    public Enum e;
        public String name; 
        public int ordinal;
    
    public int type; //1 = char, 2 = play, 3 = field, 4 = miss
    public int target; //1 = self, 2 = choose 1 player, 3 = all, 4 = all others
    public int effect; //1 = deal damage, 2 = heal, 3 = miss, 4 = draw
    public int effect2; //secondary effects only affect player
    public int special; //HP for char cards, ???? for other cards, 1 for beer and bangs, 1 for miss, 2 for dodge
    public boolean discardToPlay; //cards that need a discard to play
    public int range; //used for guns and panic and #cards drawn
}

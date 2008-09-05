package ucbang.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Card {
    public Card() {
    }
    
    public Card(Enum e) {
        ordinal = e.ordinal();
        name = e.toString();
        if(e instanceof Bang.Characters){
            type = 1;
            if(Arrays.binarySearch(new int[]{3, 6, 8, 16, 21, 27, 28, 30}, ordinal)!=-1){
                special = 3;
            }
            else
                special = 4;
        }
        else{
            //TODO: find out what kind of card it is
         
            /*switch(ordinal){
                case 0:
                    name = e.toString(); break;
                case 1:
                    name = e.toString(); break;
                case 2:
                    name = e.toString(); break;
                case 3:
                    name = e.toString(); break;
                case 4:
                    name = e.toString(); break;
                case 5:
                    name = e.toString(); break;
                default:  
                    name = e.toString(); break;
            }*/
        }
    }
    
    public String name; 
    public int ordinal;
    public int type; //1 = char, 2 = play, 3 = field
    public int special; //HP for char cards, ???? for other cards
}

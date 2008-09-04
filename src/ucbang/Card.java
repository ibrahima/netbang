package ucbang;

public class Card {
    public Card() {
    }
    
    public Card(Enum e) {
        switch(e.ordinal()){
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
        }
    }
    
    public String name; 
    
}

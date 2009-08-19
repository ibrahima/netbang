package netbang.core;

public class Constants {
    public Constants() {
    }

    public static enum Role {SHERIFF, DEPUTY, OUTLAW, RENEGADE};
    /**
     * The following constants describe the netbang message protocol.
     */
    /**
     * This string is sent when a player wants to discard a card.
     */
    public final String DISCARD = "discard";
    
}

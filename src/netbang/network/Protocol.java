/**
 * 
 */
package netbang.network;

/**
 * @author Ibrahim
 *	This class holds constants that describe the netbang network protocol.
 */
public class Protocol {

    /**
     * This string is used to signify that the client is read
     * to receive more messages from the server.
     */
    public static final String ACK = "Ready";

    /**
     * This enum describes the various messagetypes that are sent
     * @author Ibrahim
     */
    public enum MessageType{
    	/**This string is used for connection information*/
    	CONNECTION("Connection"),
    	/**This string is used for general information*/
    	INFOMSG("InfoMsg"),
    	/**This string is sent to inform the client about the number of players*/
    	PLAYERS("Players"),
    	/**This string is sent when a player sends a chat message*/
    	CHAT("Chat"),
    	/**This string is sent when a player joins the game*/
    	PLAYERJOIN("PlayerJoin"),
    	/**This string is sent when a player leaves the game*/
    	PLAYERLEAVE("PlayerLeave"),
    	/**This string is sent by the server to prompt the player*/
    	PROMPT("Prompt"),
    	/**This string is sent when the player draws cards*/
    	DRAW("Draw"),
    	/**This string is sent when a player discards cards*/
    	DISCARD("Discard"),
    	/**This string is sent when some game information changes*/
    	SETINFO("SetInfo");
    	public String value;
    	private MessageType(String value) {
    		this.value = value;
		}
    	public String toString(){
    		return this.value;
    	}
    }
}

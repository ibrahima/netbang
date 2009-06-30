package netbang.network;

import java.awt.Color;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import netbang.core.Card;
import netbang.core.Deck;
import netbang.core.Player;
import netbang.core.Deck.CardName;
import netbang.gui.CardDisplayer;
import netbang.gui.Clickable;
import netbang.gui.ClientGUI;
import netbang.gui.Field;


public class Client extends Thread {
    String name = "";
    public int id;
    public int numPlayers = 0; // should be deprecated soon in favor of
    Socket socket = null;
    int port = 12345;
    String host = "127.0.0.1";
    boolean connected = false;
    public LinkedList<String> outMsgs = new LinkedList<String>();
    public ClientGUI gui;
    public ArrayList<Player> players = new ArrayList<Player>();
    public Player player;
    public Field field;
    ClientThread t;
    int turn;
    public boolean running;
    public boolean prompting;
    public boolean forceDecision;
    public boolean targetingPlayer;
    public int nextPrompt = -2; //this value will be returned the next time the client is prompted to do something
    
    boolean guiEnabled;
    public boolean redraw = true;
    
    public ArrayList<Card> specialHand = new ArrayList<Card>(); //for general store or when players die and their hands are revealed
    public ArrayList<Card> discardpile = new ArrayList<Card>(); //only used for drawing the discard pile, for aesthetic effect
    /**
     * Constructs a client to the Bang server on the specified host.
     * <p>Note: guiEnabled is intended for bots and the like. As such there is no
     * use of it yet, but there will be eventually.</p>
     * @param host the host to connect to
     * @param guiEnabled whether the GUI is enabled
     */
    public Client(String host, boolean guiEnabled) {
        String s = ClientGUI.promptChooseName();
        if(s == null){
            new ServerBrowser();
            return;
        }
        new Client(host, guiEnabled, s);
    }
    /**
     * Constructs a client to the Bang server on the specified host, with the specified name.
     * <p>Note: guiEnabled is intended for bots and the like. As such there is no
     * use of it yet, but there will be eventually.</p>
     * @param host the host to connect to
     * @param guiEnabled whether the GUI is enabled
     * @param name the name of the client
     */
    public Client(String host, boolean guiEnabled, String name) {
        running = true;
        this.host = host;
        this.name = name;
        this.guiEnabled = guiEnabled;
        if (guiEnabled){
            gui = new ClientGUI(numPlayers++, this);
            field = new Field(new CardDisplayer(), this);
            gui.getContentPane().addMouseListener(field);
            gui.getContentPane().addMouseMotionListener(field);
            // Begin testing card field stuffs
            CardName[] cards = CardName.values();
            int x = 65;
            int y = 0;
            for (int i = 0; i < cards.length; i++) {
                field.add(new Card(cards[i]), x, y, 0, false);
                x += 60;
                if (x > 750) {
                    y += 90;
                    x = 65;
                }
            }
        }
        player = new Player(id, name);
        this.start();
    }

    public static void main(String[] Args) {
        if (Args.length == 0)
            new Client("127.0.0.1", true);
        else if (Args.length == 1)
            new Client(Args[0], true);
        else if (Args.length == 2)
            if (Args[1].equals("Dummy"))
                new Client(Args[0], false);
            else
                new Client(Args[0], true, Args[1]);
    }

    /**
     * Gives the name of the local client
     * @return the name of the client
     */
    public String getPlayerName() {
        return name;
    }

    void promptName() {
        System.out.println("Choosing a new name");
        name = ClientGUI.promptChooseName();
        if(name!= null && name.length()==0)
            name = ClientGUI.promptChooseName();    
        synchronized (name) {
            name.notifyAll();
        }
    }

    public void run() {
        try {
            socket = new Socket(host, port);
        } catch (Exception e) {
            System.err.println(e + "\nServer Socket Error!");
        }
        t = new ClientThread(socket, this);
        while (running) {
            try{
                Thread.sleep(10);
                if(guiEnabled&&(redraw  || Clickable.numAnimating > 0)){ gui.repaint(); redraw = false; }
                else if(System.currentTimeMillis() - field.lastMouseMoved > 1000){ // hackish?
                    field.drawDescription();
                }
            }
            catch(InterruptedException e){
                
            }
        }
        
        //process has been killed
        quit();
    }
    
    public void quit(){
        gui.dispose();
        gui = null;
        System.out.println("Exiting");
    }

    protected void print(Object stuff) {
        if (gui != null)
            gui.appendText("Client "+name+": " + stuff);
        else
            System.out.println("Client "+name+": " + stuff);
    }

    void addMsg(String msg) {
        synchronized (outMsgs) {
            outMsgs.add(msg);
        }
    }

    /**
     * Sends the specified chat message to the server
     * @param chat the chat message
     */
    public void addChat(String chat) {
        addMsg("Chat:" + chat);
    }
    /**
     * Prompts the player to start
     * @return
     */
    protected int promptStart() {
        gui.appendText("Host has requested the game be started", 
                Color.BLUE);
        return gui.promptYesNo("Host has sent a request to start game", 
                            "Start game?");
    }
    /**
     * Prompts the player to play a card
     */
    protected void promptPlayCard() {
        gui.promptChooseCard(player.hand, "", "", 
                               true);
    }

}

class ClientThread extends Thread {
    Socket server;
    BufferedReader in;
    BufferedWriter out;
    Client client;
    String buffer;
    boolean namesent = false;

    // int response = -2; //-1 is cancel

    public ClientThread(Socket theServer, Client c) {
        server = theServer;
        this.client = c;
        try {
            out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
            in =  new BufferedReader(new InputStreamReader(server.getInputStream()));
        } catch (Exception e1) {
            try {
                if (server != null) // is it closing too soon some times?
                    server.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        this.start();
    }

    public void run() {
        while (!server.isClosed() && client.running) {
            try {
                if (client.name != null && out != null && !client.connected && 
                    !namesent) {
                    out.write("Name:" + client.name);
                    out.newLine();
                    out.flush();
                    namesent = true;
                }
                synchronized (client.outMsgs) {
                    if (!client.outMsgs.isEmpty()) {
                        Iterator<String> iter = client.outMsgs.iterator();
                        while (iter.hasNext()) {
                            out.write(iter.next());
                            out.newLine();
                            iter.remove();
                        }
                    }
                }
                out.flush();
                if (in.ready()) {
                    client.redraw = true;
                    buffer = (String)in.readLine();
                    String[] temp = buffer.split(":", 2);
                    String messagetype = temp[0];
                    String messagevalue = temp[1];
                    if (messagetype.equals("Connection")) {
                        System.out.println(messagevalue);
                        if (!client.connected && 
                            messagevalue.equals("Successfully connected.")) {
                            client.connected = true;
                            if(client.guiEnabled)
                                client.gui.setTitle("NetBang - " + client.name + 
                                           " - Connected to server on " + 
                                           server.getInetAddress());
                        } else if (!client.connected && 
                                   messagevalue.equals("Name taken!")) {
                            System.out.println(this + 
                                               ": Connection refused because name was taken");
                            namesent = false;
                            client.promptName();
                            
                            //quit if no name entered
                            if(client.name == null){
                                client.quit();
                                return;
                            }
                        }
                    } else if (messagetype.equals("Chat")) {
                        if(client.guiEnabled)
                            client.gui.appendText(messagevalue);
                        else
                            print(messagevalue);
                    } else if (messagetype.equals("InfoMsg")) {
                        String[] temp1 = messagevalue.split(":");
                        client.gui.appendText(temp1[0], (Integer.valueOf(temp1[1])==0)?Color.BLUE:Color.RED);
                        client.outMsgs.add("Ready");
                    } else if (messagetype.equals("Players")) {
                        String[] ppl = messagevalue.split(",");
                        for (int i = 0; i < ppl.length; i++) {
                            if (ppl[i] != null && !ppl[i].isEmpty()) {
                                client.players.add(new Player(i, ppl[i]));
                            }
                        }
                    } else if (messagetype.equals("PlayerJoin")) {
                        client.players.add(new Player(client.players.size(), messagevalue));
                        System.out.println("added "+messagevalue);
                    } else if (messagetype.equals("PlayerLeave")) {
                        if(client.player.maxLifePoints>0){ //game is started
                            client.gui.appendText("A player has left the game. Game cannot continue. Server shutting down.");
                            break;
                        }
                        for(Player p : client.players)
                            if(p.name.equals(messagevalue)){
                                client.players.remove(p);
                                System.out.println("removed "+p.name);
                                break;
                            }
                    } else if (messagetype.equals("Prompt")) {
                        if(!processPrompt(messagevalue)){
                		    System.out.println("WTF do i do with " + messagevalue);
                		    Thread.dumpStack();
                		}

                    } else if (messagetype.equals("Draw")) {
                        String[] temp1 = messagevalue.split(":");
                        int n = temp1.length;
                        if (Integer.valueOf(temp1[0]) == client.id) {
                            for (int m = 2; m < n; m++) {
                                if (temp1[1].equals("Character")) {
                                    Card card = 
                                        new Card(Deck.Characters.valueOf(temp1[m]));
                                    if(client.guiEnabled)
                                        client.field.add(card, 150+80*m, 200, client.id, false);
                                    client.player.hand.add(card);
                                } else {
                                    Card card = 
                                        new Card(Deck.CardName.valueOf(temp1[m]));
                                    if(client.guiEnabled)
                                        client.field.add(card, client.id, false);
                                    client.player.hand.add(card);
                                }
                            }
                        } else {
                            client.gui.appendText("Player " + temp1[0] + " drew " + 
                                             temp1[1] + "cards.", Color.GREEN);
                            for(int i=0;i<Integer.valueOf(temp1[1]);i++){
                                Card card = new Card(Deck.CardName.BACK);
                                client.field.add(card, Integer.valueOf(temp1[0]), false);
                                client.players.get(Integer.valueOf(temp1[0])).hand.add(card);
                            }
                        }
                        client.outMsgs.add("Ready");
                    } else if (messagetype.equals("SetInfo")) { // note: a bit of a
                        // misnomer for lifepoints, just adds or subtracts that amount
                        // set information about hand and stuff
                        String[] infofields = messagevalue.split(":");
                        int tid = Integer.valueOf(infofields[1]);
                        String infotype = infofields[0];
                        Player ptemp = null;
                        if(!processInfo(infotype, infofields, tid, ptemp)){
                		    System.out.println("WTF do i do with " + infotype + ":" + infofields[1]);
                		    Thread.dumpStack();
                		}
                        client.outMsgs.add("Ready");
                    }
                }
            } catch (Exception e) {
                if (e != null && e.getMessage() != null && 
                    e.getMessage().equals("Connection reset")) {
                    print("Connection to server lost");
                    try {
                        finalize();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
            try {
                sleep(45);
            } catch (InterruptedException e) {
            }
        }
        try {
            this.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("Server connection closed");
    }

	/**
	 * Processes a prompt request from the server
	 * @param messagevalue
	 * @return True if the message was properly processed, false otherwise
	 */
	private boolean processPrompt(String messagevalue) {
		Boolean processed = false;
		if(client.nextPrompt!=-2){
			processed = true;
		    client.outMsgs.add("Prompt:" + client.nextPrompt);
		    client.nextPrompt = -2;
		}
		// received a prompt from host to start
		else if (messagevalue.equals("Start")) {
			processed = true;
		    client.outMsgs.add("Prompt:" + 
		                  client.promptStart());
		} else if (messagevalue.equals("PlayCard")) {
			processed = true;
		    client.promptPlayCard();
		} else if (messagevalue.equals("PlayCardUnforced")) {
			processed = true;
		    client.gui.promptChooseCard(client.player.hand, "", "", 
		                           false);
		} else if (messagevalue.equals("PickCardTarget")) {
			processed = true;
		    client.gui.promptTargetCard("", "", //null should be ALL cards.
		                           false);
		    client.nextPrompt = -1;
		} else if (messagevalue.equals("GeneralStore")) {
			processed = true;
		    client.gui.promptChooseCard(client.specialHand, "", "", true);
		} else if (messagevalue.equals("ChooseCharacter")) {
			processed = true;
		    client.promptPlayCard();
		} else if (messagevalue.equals("PickTarget")) {
			processed = true;
		    //System.out.println("I am player " + c.id + ", prompting = " + c.prompting);
		    //c.outMsgs.add("Prompt:" + (1 - c.id));
		    client.gui.promptChooseCard(null, "", "", false);
		    client.targetingPlayer = true;
		}
		return processed;
	}

	/**
	 * Processes a setinfo request from the server.
	 * @param type
	 * @param fields
	 * @param playerid
	 * @param player
	 * @return True if the message was properly processed, false otherwise
	 */
	private Boolean processInfo(String type, String[] fields, int playerid,
			Player player) {
		Boolean processed = false;
		if (type.equals("newPlayer")) {
			processed = true;
		    client.id = playerid;
		    client.player = new Player(playerid, client.name); //TODO: This line seems fishy, doesn't this recreate the player twice?
		    client.numPlayers = Integer.valueOf(fields[2]);
		    client.players.set(client.id, client.player);
		} else{
		    if(client.id == playerid){
		            player = client.player;
		    }else if(playerid < client.players.size() && playerid >= 0){
		            player = client.players.get(playerid);
		    }
		}
		if (type.equals("role")) {
			processed = true;
		    if(client.guiEnabled)
		        if (playerid == client.id) {
		            client.field.clear();
		            client.player.role = 
		                    Deck.Role.values()[Integer.valueOf(fields[2])];
		            client.gui.appendText("You are a " + client.player.role.name(), Color.YELLOW);
		        } else {
		            if (Integer.valueOf(fields[2]) == 0)
		                client.gui.appendText("Player " + fields[1] + 
		                                 " is the " + 
		                                 Deck.Role.values()[Integer.valueOf(fields[2])].name(), 
		                                 Color.YELLOW);
		            else //only shown when player is killed
		                client.gui.appendText("Player " + fields[1] + 
		                                 " was a " + 
		                                 Deck.Role.values()[Integer.valueOf(fields[2])].name(), 
		                                 Color.YELLOW);
		        }
		    else{
		        if (playerid == client.id) {
		            client.field.clear();
		            client.player.role = 
		                    Deck.Role.values()[Integer.valueOf(fields[2])];
		        }
		    }
		    
		} else if (type.equals("maxHP")) {
			processed = true;
		    if(client.guiEnabled)
		        client.gui.appendText("Player " + fields[1] + 
		                     " has a maxHP of " + fields[2], 
		                     Color.RED);
		    player.maxLifePoints=Integer.valueOf(fields[2]);
		    player.lifePoints=Integer.valueOf(fields[2]);
		        //this should match the above block
		    if(playerid + 1 == client.numPlayers && client.guiEnabled)
		        client.field.start2();
		} else if (type.equals("HP")) {
			processed = true;
		    player.lifePoints+=Integer.valueOf(fields[2]).intValue();
		    if(client.guiEnabled){
		        client.gui.appendText("Player " + fields[1] + 
		                " life points changed by " + 
		                fields[2], Color.RED);
		        client.field.setHP(playerid,player.lifePoints);
		    }else
		        client.print("Player " + fields[1] + 
		                " life points changed by " + 
		                fields[2]);
		} else if (type.equals("PutInField")) {
			processed = true;
	        client.gui.appendText("Player "+fields[1]+" added "+fields[2]+" to the field.");
	        Card card;
	        if(playerid==client.id){
	            if(fields.length==4){
	                card = client.player.hand.get(Integer.valueOf(fields[3]));
	                card.location = 1;
	                client.field.remove(playerid, Integer.valueOf(fields[3]));
	                client.player.hand.remove(card);
	            } else{
	                card = new Card(Deck.CardName.values()[Integer.valueOf(fields[2])]);
	            }
	        }
	        else{
	            if(fields.length==4){
	                card = new Card(CardName.valueOf(fields[2]));
	                card.location = 1;
	                client.field.remove(playerid,(int)Integer.valueOf(fields[3]));
	                client.players.get(playerid).hand.remove((int)Integer.valueOf(fields[3]));
	            }
	            else{
	                card = new Card(Deck.CardName.values()[Integer.valueOf(fields[2])]);
	                card.location = 1;
	            }
	        }
	        client.players.get(playerid).field.add(card);
	        client.field.add(card, playerid, true);
		} else if(type.equals("GeneralStore")){
			processed = true;
		    System.out.println("General Store!!!!!");
	        for(Card card: client.specialHand){
	            client.field.clickies.remove(card);
	        }
	        client.specialHand.clear();
	        
	        for(int n = 2; n<fields.length; n++){
	            Card card = new Card(Deck.CardName.valueOf(fields[n]));
	            client.specialHand.add(card);
	            client.field.add(card, client.gui.width/2-120+n*30, client.gui.height/2, -1, false);
	        }
		} else if (type.equals("turn")) {
			processed = true;
		    client.turn = playerid;
		    if (client.turn % client.numPlayers == client.id) {
		        client.gui.appendText("It's your move!!!!!! Time to d-d-d-d-d-duel!", Color.CYAN);
		    }
		} else if (type.equals("discard")) {
		    //TODO: Keep track of discard pile on client side
			processed = true;
		    if(playerid==client.id){
		        client.field.remove(playerid, Integer.valueOf(fields[2]));
		        String cname = client.player.hand.remove(Integer.valueOf(fields[2]).intValue()).name;
		        client.gui.appendText("You discarded:" + cname);
		        client.discardpile.add(new Card(Deck.CardName.valueOf(cname)));
		    }
		    else{
		        System.out.println(client.players.get(playerid).name + " discarded " + client.players.get(playerid).hand.get(Integer.valueOf(fields[2])));
		        client.field.remove(playerid, Integer.valueOf(fields[2]));
		        client.players.get(playerid).hand.remove(Integer.valueOf(fields[2]).intValue());
		        client.gui.appendText("Player "+playerid+" discarded:" + (fields.length==4?fields[3]:"card #"+fields[2]));
		        if(fields.length==4){
		            client.discardpile.add(new Card(Deck.CardName.valueOf(fields[3])));
		        }
		    }
		} else if (type.equals("fieldDiscard")) {
		    //TODO: Keep track of discard pile on client side
			processed = true;
		    if(playerid==client.id){
		        client.gui.appendText("REMOVING:" + Integer.valueOf(fields[2]).intValue()+ " "+client.player.field.get(Integer.valueOf(fields[2]).intValue())+" "+client.player.field.size());
		        client.field.remove(playerid, Integer.valueOf(fields[2]));
		        String cname =client.player.field.remove(Integer.valueOf(fields[2]).intValue()).name;
		        client.gui.appendText("You discarded:" + cname);
		        client.discardpile.add(new Card(Deck.CardName.valueOf(cname)));
		    }
		    else{
		        client.field.remove(playerid, Integer.valueOf(fields[2]));
		        client.players.get(playerid).field.remove(Integer.valueOf(fields[2]).intValue());
		        client.gui.appendText("Player "+playerid+" discarded:" + fields[3]);
		        client.discardpile.add(new Card(Deck.CardName.valueOf(fields[3])));
		    }
		}
		else if (type.equals("CardPlayed")) {
		    //TODO: Keep track of discard pile on client side
			processed = true;			
		    String s = "";
		    s = "Player " + fields[1] + " played " + fields[2] + (fields.length == 4 ? " at player " + fields[3] : "");
		    client.gui.appendText(s);
		    if(!fields[2].equals("no miss"))
		        client.discardpile.add(new Card(Deck.CardName.valueOf(fields[2])));
		} else if (type.equals("id")) { //TODO: remove safely? <-- what does this mean?
			processed = true;
		    client.id = playerid;
		} else if (type.equals("character")) {
			processed = true;
		    if (playerid == client.id){
		        client.player.character = Integer.valueOf(fields[2]);
		        client.players.get(playerid).character=Integer.valueOf(fields[2]);
		    }
		    else {
		        client.gui.appendText("Player " + fields[1] + " chose " + Deck.Characters.values()[Integer.valueOf(fields[2])], Color.YELLOW);
		        client.players.get(playerid).character=Integer.valueOf(fields[2]);
		        client.field.add(new Card(Deck.Characters.values()[Integer.valueOf(fields[2])]), playerid, false);
		    }
		}
		return processed;
	}

    protected void finalize() throws Throwable {
        try {
            out.write("/quit");
            out.flush();
            in.close();
            out.close();
            server.close();
        } catch (Exception e) {
        }
    }

    void print(Object stuff) {
        if (client.gui != null)
            client.gui.appendText("ClientThread:" + stuff);
        else
            System.out.println("ClientThread:" + stuff);
    }
}

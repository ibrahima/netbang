package ucbang.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server extends Thread{
	HashMap<String,Socket> players = new HashMap<String,Socket>();
	protected HashMap<String,String> messages = new HashMap<String,String>();	
	static int numPlayers;
	ServerSocket me;
	
	void print(Object stuff){
    	System.out.println("Server:"+stuff);
    }
	public Server(int port){
		try{
			me=new ServerSocket(port);
		}
		catch(IOException e){
			System.err.println("Server Socket Error!\n"+e);
		}
		print("Game server is listening to port "+port);
		this.start();
	}

	public static void main(String Args[]){
		new Server(12345);
	}
	public void run(){
		while(true) {
			try {
        		//////print("Waiting for connections.");
        		Socket client = me.accept();
        		//print("Accepted a connection from: "+ client.getInetAddress());
        		connection c = new connection(client, this);
        		numPlayers++;
       		} 
       		catch(Exception e) {e.printStackTrace();}
     	}
	}
}

class connection extends Thread{
	//sends HashMap of stuff to clients, gets client's updated positions
	Socket client;
	ObjectInputStream in;
	ObjectOutputStream out;
	
	Server myServer;
	String name="";
	String buffer;
	boolean connected=false;
	void print(Object stuff){
    	System.out.println("Server:"+stuff);
    }
	public connection(Socket theClient, Server myServer){
		client=theClient;

		this.myServer=myServer;
		try {
      		in= new ObjectInputStream(client.getInputStream());
      		out = new ObjectOutputStream(client.getOutputStream());
     	}
     	catch(Exception e1) {
     		
     		e1.printStackTrace();
        	try {
           		client.close();
        	}
        	catch(Exception e) {
           		e.printStackTrace();
         	}
         //return;
     	}
		try
		{
			buffer=(String)in.readObject();
			name = buffer;
			print(name+"("+client.getInetAddress()+") has joined the game.");
			out.writeObject("Successfully connected.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
     	this.start();
	}
	public synchronized void run(){		
		while(!client.isClosed()){
			try {
				buffer=(String)in.readObject();//This line also gives off errors at home but not school...
				System.out.println(buffer);
	         	if(myServer.messages.containsKey(name)){
	         		out.writeObject(myServer.messages.get(name));
	         		myServer.messages.remove(name);
	         	}/**/
	         	else{
	         		out.writeObject(null);
	         	}	         	
	         	out.flush();

	      }
	      catch(Exception e) {
	      	if(e!=null&&e.getMessage()!=null&&e.getMessage().equals("Connection reset"))
	      		try{finalize();}catch(Throwable t){}
			else
	      		e.printStackTrace();
	      }
		}
	}
	protected void finalize() throws Throwable{
		//map.remove(name);
		print(name+"("+client.getInetAddress()+") has left the game.");
		try{in.close();
	    out.close();
		client.close(); }
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
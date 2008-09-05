package ucbang.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server extends Thread{
	HashMap<String,ServerThread> players = new HashMap<String,ServerThread>();
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
        		ServerThread c = new ServerThread(client, this);
        		numPlayers++;
       		} 
       		catch(Exception e) {e.printStackTrace();}
     	}
	}
}

class ServerThread extends Thread{
	//sends HashMap of stuff to clients, gets client's updated positions
	Socket client;
	BufferedReader in;
	BufferedWriter out;
	
	Server myServer;
	String name="";
	String buffer;
	boolean connected=false;
	void print(Object stuff){
    	System.out.println("Server:"+stuff);
    }
	public ServerThread(Socket theClient, Server myServer){
		client=theClient;

		this.myServer=myServer;
		try {
      		in= new BufferedReader(new InputStreamReader(client.getInputStream()));
      		out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
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
			buffer=(String)in.readLine();
			name = buffer;
			print(name+"("+client.getInetAddress()+") has joined the game.");
			out.write("Successfully connected.");
			out.newLine();
			out.flush();
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
				if(in.ready()){
					buffer=(String)in.readLine();//This line also gives off errors at home but not school...
					System.out.println("Server received "+buffer);					
				}
	         	if(myServer.messages.containsKey(name)){
	         		out.write(myServer.messages.get(name));
	         		out.newLine();
	         		myServer.messages.remove(name);
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
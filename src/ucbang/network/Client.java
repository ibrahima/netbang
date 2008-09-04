package ucbang.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;


public class Client extends Thread{
	String name;
	static int players=0;
	Socket socket=null;
	Random r = new Random();
	int port=12345;
	String host ="127.0.0.1";
	boolean connected=false;
	LinkedList<String> msgs = new LinkedList<String>();
	public Client(String host, boolean guiEnabled) {
		this.host=host;
		players++;
		name="Test client"+players;
		this.start();
		
	}
	public Client(String host, boolean guiEnabled, String name) {
		this.host=host;
		players++;
		this.name=name;
		this.start();
		
	}

	public static void main(String Args[]){
		if(Args[1]==null)
			new Client(Args[0],true);
		else{
			if(Args[1].equals("Dummy")){
				new Client(Args[0],false);
			}
			else
				new Client(Args[0],true).name=Args[1];
		}	
	}
	public String getPlayerName(){
		return name;
	}
	public void run(){
		try{
			socket = new Socket(host,port);
		}
		catch(Exception e){
			System.err.println(e+"\nServer Socket Error!");
		}
		//////print("Connection made with "+socket);
		new connector(socket, name, this);
		
		while(true){
			try
			{
				sleep(45);
			}
			catch(InterruptedException e){}
		}
	}
	void print(Object stuff){
    	/*if(gui!=null)
    		gui.addMsg("Client:"+stuff);
    	else*/
    		System.out.println("Client:"+stuff);
    }
	void addMsg(String msg){
		synchronized(msgs){
			msgs.add(msg);
		}
	}

}

class connector extends Thread{
	Socket server;
	String name;
	//Ship old;
	ObjectInputStream in;
	ObjectOutputStream out;
	Client c;
	String buffer;
	public connector(Socket theServer, String theName, Client c){
		server=theServer;
		name=theName;
		this.c=c;
		try {
  			out = new ObjectOutputStream(server.getOutputStream());
  			in= new ObjectInputStream(server.getInputStream());
 		}
 		catch(Exception e1) {
     		try {
        		server.close();
     		}
     		catch(Exception e) {
     		}
     	return;
     	}
     	this.start();
     	
	}
	public synchronized void run(){
		try
		{
			out.writeObject(name);
		} 
		catch(IOException e)
		{
			e.printStackTrace();
		}
		while(!server.isClosed()){
			try {
				if(name!=null&&out!=null&&!c.connected){
					out.writeObject(name);
		         	out.flush();
				}
	         	out.reset();
	         	buffer=(String)in.readObject();
	         	if(!c.connected&&buffer.equals("Successfully connected.")){
	         		c.connected=true;
	         		System.out.println("Successfully connected to server on "+server.getInetAddress());
	         	}
	      }
	      catch(Exception e) {
	      	if(e!=null&&e.getMessage()!=null&&e.getMessage().equals("Connection reset"))
	      	{
	      		print("Connection to server lost");
	      			try{finalize();}catch(Throwable t){}
	      	}
	      	e.printStackTrace();
	      	//try{sleep(1000);}
	      	//catch(InterruptedException e1){}
	      }
		try
			{
				sleep(45);
			}
			catch(InterruptedException e){}
		
		}
	}
  	protected void finalize() throws Throwable{	
         	try{
         		in.close();
         		out.close();
         		server.close();
         	}
         	catch(Exception e){}
    }
    void print(Object stuff){
    	/*if(gui!=null)
    		gui.addMsg("Client:"+stuff);
    	else */
    		System.out.println("Client:"+stuff);
    }
}

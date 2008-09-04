package ucbang.network;

import java.nio.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class Client extends Thread{
	String name;
	static int players=0;
	Socket socket=null;
	Random r = new Random();
		
	int port=12345;
	String host ="127.0.0.1";
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
		new connector(socket, name);
		
		while(true){
			try
			{
				sleep(45);
			}
			catch(InterruptedException e){}
		}
	}
	synchronized void print(Object stuff){
    	/*if(gui!=null)
    		gui.addMsg("Client:"+stuff);
    	else*/
    		System.out.println("Client:"+stuff);
    }

}

class connector extends Thread{
	//sends HashMap of stuff to clients
	Socket server;
	String name;
	//Ship old;
	ObjectInputStream in;
	ObjectOutputStream out;
	public connector(Socket theServer, String theName)
	{
		server=theServer;
		name=theName;
			try {
      			out = new ObjectOutputStream(server.getOutputStream());
      			in= new ObjectInputStream(server.getInputStream());
     		}
     		catch(Exception e1) {
         		try {
            		server.close();
         		}
         		catch(Exception e) {
           			//////print(e.getMessage());
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
				if(name!=null&&out!=null)out.writeObject(name);//This line is giving off errors
	        	//print("Client "+name+" just wrote its ship which is "+me+" at "+java.text.DateFormat.getTimeInstance(java.text.DateFormat.FULL).format(new java.util.Date()));
	         	out.flush();
	         	
	         	out.reset();
	      }
	      catch(Exception e) {
	      	//////print("Something bad happened in client")	;
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

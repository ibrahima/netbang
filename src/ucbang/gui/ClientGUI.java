package ucbang.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ucbang.core.Card;
import ucbang.core.Player;

public class ClientGUI extends JFrame implements KeyListener{
    BufferStrategy strategy;
    public Player player;
    int p;
    StringBuilder chat;
    boolean chatting=false;
    
    public ClientGUI() {
    }
    
    public ClientGUI(int p) {
        this.p = p;
        chat=new StringBuilder();
        //set window sizes
        setPreferredSize(new Dimension(800,600));
        setSize(new Dimension(800,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
     	this.setIgnoreRepaint(true);
     	this.setVisible(true);
    	this.createBufferStrategy(2);
    	strategy=this.getBufferStrategy();
    }
       
    public void paint(Graphics g){
		Graphics2D graphics;
		try {
			graphics = (Graphics2D) strategy.getDrawGraphics();
		} catch (Exception e) {
			return;
		}
		//fill background w/ dark green
		graphics.setColor(Color.GREEN);
		graphics.fillRect(0, 0, 800, 400);
		graphics.setColor(new Color(100,0,0));
		graphics.fillRect(0, 400, 800, 600);
		if(chatting){
			graphics.setColor(Color.WHITE);
			graphics.drawString(chat.toString(), 10, 550);
		}

		graphics.dispose();
		//paint backbuffer to window
		strategy.show();
	}
    public void update(){
		paint(this.getGraphics());

    }
    
    public int promptChooseCharacter(ArrayList<Card> al){
        return promptChooseCard(al, "Who do you want to be? You are a(n) " + player.role, "Choose your character!");
    }
    
    
    /**
     * Asks the player to choose a card. This is used for many instances.
     * TODO: replace al with ID of the player.
     * @param al
     * @return
     */
    public int promptChooseCard(ArrayList<Card> al, String str1, String str2){
    	Card[] temp = new Card[al.size()];
    	temp = al.toArray(temp);
    	String[] options=new String[temp.length];
    	for(int i=0;i<temp.length;i++){
    		options[i]=((Card)temp[i]).name;
    	}
        int n = -1;
        while(n==-1)
                n = JOptionPane.showOptionDialog(this,
		str1,
		str2,
		JOptionPane.YES_NO_OPTION,
		JOptionPane.QUESTION_MESSAGE,
		null,
		options,
		options[0]);
        return n;
    }
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar()=='\n'){
			chatting=!chatting;
			if(!chatting&&chat.length()>0){
				System.out.println("Sent chat message "+chat);
				chat.delete(0, chat.length());
				//TODO: Actually send chats.
			}
		}else if(chatting){
			chat.append(e.getKeyChar());
		}
	}
}

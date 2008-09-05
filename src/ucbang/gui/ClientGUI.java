package ucbang.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ucbang.core.Card;
import ucbang.core.Player;

public class ClientGUI extends JFrame implements KeyListener{
	BufferStrategy strategy;
    int player;
    StringBuilder chat;
    boolean chatting=false;
    public ClientGUI() {
    }
    public ClientGUI(int p) {
        player = p;
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
    /**
     * Asks the player to choose a card
     * @param al
     * @return
     */
    public int promptChooseCard(ArrayList<Card> al){
    	Card[] temp = new Card[al.size()];
    	temp = al.toArray(temp);
    	String[] options=new String[temp.length];
    	for(int i=0;i<temp.length;i++){
    		options[i]=((Card)temp[i]).name;
    	}
		int n = JOptionPane.showOptionDialog(this,
		"Who do you want to be?",
		"Choose your character!",
		JOptionPane.YES_NO_CANCEL_OPTION,
		JOptionPane.QUESTION_MESSAGE,
		null,
		options,
		options[0]);

        return n;
    }
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
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

package ucbang.gui;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame{
    public GUI() {
        //set window sizes
        setPreferredSize(new Dimension(800,600));
        setSize(new Dimension(800,600));
        
        //create panels
        fields = new JPanel();
        fields.setPreferredSize(new Dimension(400, 400));
        
            //TODO: create real backgrounds
        fields.setBackground(new Color(100,0,0));
        
        chat = new JPanel();
        chat.setPreferredSize(new Dimension(800, 200));
        chat.setLayout(new GridBagLayout());
        
            //TODO: create real backgrounds
        chat.setBackground(new Color(0,100,0));
        
        //create text field, button, and area
        JTextField message = new JTextField();
        JButton send = new JButton();
        JTextArea text = new JTextArea();
        
        //add stuff to JPanel chat
        //everyone loves gridbags....
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridwidth = 1;
        c.gridx=0;
        c.ipady=0;
        c.ipadx=0;
        c.insets = new Insets(0,0,0,0);
        
        chat.add(message, c);
        c.gridx=1;
        chat.add(send, c);
        c.gridx=0;
        c.gridwidth=2;
        c.gridy=1;
        chat.add(text, c);
        
        
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(fields);
        getContentPane().add(chat);
        
        validate();
    }
       
    JPanel fields;
    JPanel chat;
        JTextField message;
        JButton send;
        JTextArea text;
}
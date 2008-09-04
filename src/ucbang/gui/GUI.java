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
        
            //TODO: create real backgrounds
        chat.setBackground(new Color(0,100,0));
        
        JTextArea text = new JTextArea();
        
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(fields);
        getContentPane().add(chat);
        
        validate();
    }
       
    JPanel fields;
    JPanel chat;
        JTextArea text;
}
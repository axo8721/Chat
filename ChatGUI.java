import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLEditorKit;

/**
 * 
 * The class that creates and manages the GUI for the chat
 *
 */
public class ChatGUI extends JFrame implements ActionListener,KeyListener{
	
	private JTextField textField;           //The textfield used to send messages
	private MyTextPane textDisplay;         //Displays the chat
	private JButton sendButton;             //Button used to send messages
	private JButton exitButton;             //Closes the chat
	private JButton newchatbutton;          //Starts a new chat
	private JButton kickbutton;             //For the kick-menu
	private JButton filebutton;             //For sending files
	private JScrollPane chatScroller;       //The chat is put in this JScrollpane to become scrollable
	private JPanel topPanel;                //The top of the GUI, includes the exit-Button and something else
	private JPanel bottomPanel;             //The bottom of the GUI, includes the send-Button and the field where you write messages
	private boolean online;					//False if the User presses Close, otherwise True
	private Core localcore;                 //Local Core
	private String lastWritten;             //HAGGEHHAAGGE
	
	/**
	 * Creates a new chatGUI
	 */
	public ChatGUI(){
		
		/*
		 * Creates stuff
		 */
		textField = new JTextField(40);           
		textField.addKeyListener(this);
	
		sendButton = new JButton("Send");
		sendButton.addActionListener(this);
		exitButton = new JButton("Exit");
		exitButton.addActionListener(this);
		newchatbutton = new JButton("New Chat");
		newchatbutton.addActionListener(this);
		kickbutton = new JButton("Kick Clients");
		kickbutton.addActionListener(this);
		kickbutton.setEnabled(false);
		filebutton = new JButton("Send a File!");
		filebutton.addActionListener(this);
		filebutton.setEnabled(false);
		
		/*
		 * Creates the top panel 
		 */
		topPanel = new JPanel();                     
		topPanel.setLayout(new FlowLayout());
		topPanel.add(filebutton);
		topPanel.add(kickbutton);
		topPanel.add(newchatbutton);
		topPanel.add(exitButton);
		
		/*
		 * Creates the bottom panel 
		 */
		bottomPanel = new JPanel();                     
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(textField);
		bottomPanel.add(sendButton);
		
		/*
		 * Enables the GUI
		 */
		online = true;
		
		/*
		 * Creates the Chat-Window itself
		 */
		textDisplay = new MyTextPane();
		textDisplay.setEditable(false);
		HTMLEditorKit kit = new HTMLEditorKit();
        textDisplay.setEditorKit(kit);
        DefaultCaret caret = (DefaultCaret)textDisplay.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatScroller = new JScrollPane(textDisplay);
		chatScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatScroller.setPreferredSize(new Dimension(200, 400));
		chatScroller.setMinimumSize(new Dimension(10, 10));
		
		/*
		 * Finalizes the chatGUI
		 */
		setLayout(new BorderLayout());
		add(chatScroller,BorderLayout.CENTER);
		add(topPanel,BorderLayout.PAGE_START);
		add(bottomPanel,BorderLayout.PAGE_END);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		
	}
	
	/**
	 * Returns the value of the field online
	 * @return online - a boolean
	 */
	public boolean isOnline(){
		return online;
	}
	
	/**
	 * Updates the Chat-window
	 * 
	 * @param HTML - A String given in valid HTML
	 */
	public void updateChat(String HTML){
		textDisplay.setText(HTML);
	}
	
	/**
	 * Closes the GUI and terminates the Chat
	 */
	public void close(){
		System.out.println("Goodbye!");
		System.exit(1);
	}
	
	/**
	 * Sets the field localcore to the desired core
	 * @param coreIn - The desired Core
	 */
	public void setCore(Core coreIn){
		localcore = coreIn;	
	}
	
	public void actionPerformed(ActionEvent e){
		  
		  /*
		   * To send messages
		   */
		  if (e.getSource() == sendButton) {
			  triggerSend();
			  
		  /*
		   * To exit the GUI and the whole program
		   */
		  } else if (e.getSource() == exitButton){
			  online = false;
			  localcore.sendRawMessage("<disconnect/>");     //Notify your chat-friends
			  close();
			  
		  /*
		   * To start a new chat in the same program
		   */
		  }else if (e.getSource() == newchatbutton){
			 new RunChat().start();
			 
		  /*
		   * To Kick someone
		   */
		  }else if (e.getSource() == kickbutton){
			  ((ServerCore) localcore).kick();
		  
		  
		  /*
		   * To send a file
		   */
		  }else if (e.getSource() == filebutton){
			  
		  }
	 }
    
	public void keyPressed(KeyEvent e) {
		 /*
		  * To make messages sendable with the enter-key
		  */
		 if (e.getKeyCode()==KeyEvent.VK_ENTER){
			 triggerSend();
	      } else if (e.getKeyCode()==KeyEvent.VK_UP){
	    	  textField.setText(lastWritten);
	      } else if (e.getKeyCode()==KeyEvent.VK_DOWN){
	    	  textField.setText("");
	      }
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub	
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}
	
	public void triggerSend() {
		 String message = textField.getText();
		  textField.setText("");
		  if (!message.equals("")){
			  if (message.equals("Hagge")){
				  textDisplay.HaggeShow = 1;
				  textDisplay.repaint();
			  } else if (message.equals("Ta Bort Hagge") || message.equals("-Hagge")){
				  textDisplay.HaggeShow = 0;
				  textDisplay.repaint();
			  }
			  lastWritten = message;                   //Makes it possible to retreive last message written by up-key
			  message = XMLtoHTML.fixTags(message);    //Prevents tag-abuse
			  localcore.addToLog(message);             //Logs what has been said in the chat
			  localcore.sendMessage(message);          //Actually sends the message
			  localcore.updateGUI();                   //Updates the GUI with the new log.
		
		  }
	}
	
	public void enableKickandFile(){
		kickbutton.setEnabled(true);
		filebutton.setEnabled(true);
	}
	
	public void disableKickandFile(){
		kickbutton.setEnabled(false);
		filebutton.setEnabled(false);
	}
	

}

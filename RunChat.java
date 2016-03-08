import javax.swing.JOptionPane;

/**
 * The Main Constructor for the Chat-program, only task is to find out if the user
 * is a Client or a Server and then proceed to start the "Cores" accordingly... (Of course
 * initializes the GUI as well).
 */
public class RunChat extends Thread {
	private ChatGUI GUI;            
	private boolean isserver;
	
	/**
	 * Starts a new thread for every chat you want
	 */
	public void run(){
		
		/*Start the GUI*/
		GUI = new ChatGUI();                                                             
		int n = JOptionPane.showConfirmDialog(GUI, "Do you want to host your chat?"
				                              , "Chat", JOptionPane.YES_NO_OPTION);
	    isserver = n==0;       
	    
	    /*Asks relevant questions for server or client*/
		OptionPanel Pane = new OptionPanel(isserver, GUI);                            
		
		/*Starts a  ServerCore or a ClientCore*/
		if (isserver) {															       
			ServerCore server = new ServerCore(GUI, Pane.getUser());
			
		} else {
			ClientCore client = new ClientCore(GUI, Pane.getUser());
		}
	}
 
	/*Starts a new Chat*/
	public static void main(String[] args){                             
		new RunChat().start();
		
	}
}
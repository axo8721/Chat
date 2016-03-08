import java.net.*;
import java.io.*;

import javax.swing.JOptionPane;

/**
 * 
 * @author abaathe
 *
 *The ClientCore class is used as the communication source between you and your friends if you decide to be a client
 */
public class ClientCore extends Core implements Runnable {
	
	private ServerSocket serverSocket;                      //The ServerSocket to communicate with                                    									
	private int adminport;									//The port for the serverSocket
	private Socket clientSocket;							//The Cores own socket 
	private PrintWriter out;                                
	private BufferedReader in;
	private String hostaddress;								//The IP of the host of the Chat (The Server)
	private ServerCore host = null;
	private String connectedName; 
	private String greeting;
	private String greetingIn;
	private String command;
	public boolean goodConnection = false;
	
	/**
	 * Constructor for the ClientCore
	 * @param GUIIn  - The ChatGUI that should be connected to this Core
	 * @param clientIn  - The User using this Core
	 */
    public ClientCore(ChatGUI GUIIn, User clientIn) {
    	
    	//Initializing 
    	GUI = GUIIn;
    	localuser = clientIn; 
    	adminport = localuser.getPort();
    	hostaddress = localuser.getIP();
    	
    	GUI.setCore(this);
    	
        clientSocket = null;

        out = null;
        in = null;
        
      //Establishing a connection
        System.out.println("Connecting"); System.out.println("Connecting."); System.out.println("Connecting.."); System.out.println("Connecting...");
        
        try {
            clientSocket = new Socket(hostaddress, adminport);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.\n" + e);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to host.\n" + e);
            System.exit(1);
        }

        System.out.println("Connection successful!");
        
        //Communicate while you want to communicate
        greet();
        run();
   }
    
    public ClientCore(ChatGUI GUIIn, User clientIn, Socket clientSocketIn, ServerCore hostIn) {
    	
    	//Initializing 
    	GUI = GUIIn;
    	localuser = clientIn;
    	host = hostIn;
    	adminport = localuser.getPort();
    	hostaddress = localuser.getIP();
    	
    	GUI.setCore(host);
    	
        clientSocket = clientSocketIn;
        
        try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Error with streams");
			System.exit(-1);
		}
        
   }
 
    public void run(){
	   while(GUI.isOnline()){
       	   try {
       		    String line = in.readLine();
				if (line != null){
					command = XMLtoHTML.findTask(line);
					if (host != null){
						if(command.equals("text")){
							if(goodConnection){
								host.addHTMLToLog(XMLtoHTML.messageToHTML(line));
								host.forwardMessage(line, this);
								host.updateGUI();
							} else {
								allowBadConnection();
							}
							
						}else if (command.equals("request")){
							allowConnection(line);
						}else if(command.equals("disconnect")){
							host.addHTMLToLog(XMLtoHTML.findDisconnect(line));
							host.forwardMessage(line, this);
							host.updateGUI();
							host.delete(this);
							break;
						}
					}else{
						if(command.equals("text")){
							addHTMLToLog(XMLtoHTML.messageToHTML(line));
							updateGUI();
						}else if(command.equals("norequest")){
							addHTMLToLog("<p>You are forever alone</p>");
							updateGUI();
							break;
						}else if(command.equals("disconnect")){
							addHTMLToLog(XMLtoHTML.findDisconnect(line));
							updateGUI();
							break;
						
						}
					}
				}else{
					break;
				}
			} catch (IOException e) {
				System.err.println("Closing down, Server Disconnected");
				break;
			}
       }
	   close();
    }
   
   public void sendMessage(String messageIn){
	   String xmlmessage = XMLtoHTML.messageToXML(messageIn, localuser);
	   out.println(xmlmessage);
	  
   }
   
   public void forwardMessage(String xmlmessageIn){
	   out.println(xmlmessageIn);
   }
   
   public void greet(){
	   greeting = localuser.getName() + " wants to connect!";
	   sendRawMessage("<request>" + greeting + "</request>");
   }
   
   public void sendRawMessage(String commandIn){
		out.println("<message sender=\"" + localuser.getName() + "\">" + commandIn + "</message>");
   }
   
   public String getName(){
	   return connectedName;
   }
   
   public void allowConnection(String line){
	   goodConnection = true;
	   greetingIn = XMLtoHTML.findGreeting(line);
	   if (greetingIn.endsWith(" wants to connect!")){
		   int endName =greetingIn.indexOf(" wants");
	   	   connectedName = greetingIn.substring(0,endName);
	   } else {
		   connectedName = "Unknown";
	   }
	   boolean allowed = requestPanel(greetingIn);
	   if (!allowed){
		   sendRawMessage("<request reply=\"no\"></request>");
		   host.delete(this);
	   }
   }
   
   public void allowBadConnection(){
	   goodConnection = true;
	   connectedName = "Unknown";
	   boolean allowed = requestPanel("It seems that a less advanced chat wants to connect");
	   if (!allowed){
		   sendMessage("Hi! You can't connect to this advanced chat");
		   sendMessage("<disconnect/>");
		   sendRawMessage("<disconnect/>");
		   host.delete(this);
	   }
   }
   
   public boolean requestPanel(String request){
		
		int requestgranted = JOptionPane.showConfirmDialog(GUI, request + "\n Accept?", "Chat", JOptionPane.YES_NO_OPTION);
	    return (requestgranted==0);
				
	}
   
   public void close(){
	   System.out.println("Closing Down");
	   try {
		in.close();
		out.close();
	   } catch (IOException e) {
		 System.err.println("Socket could not be closed");
		 e.printStackTrace();
	   }
	   
   }
   
   public String toString(){
	   return(connectedName);
	   
   }
   
   
}

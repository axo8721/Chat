import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.LinkedList;

import javax.swing.*;

public class ServerCore extends Core implements ActionListener{
	
	private ServerSocket serverSocket;
	private LinkedList<ClientCore> clientCores;
	private Socket clientSocket;
	private PrintWriter out;
    private BufferedReader in;
	private int usercount = 1;
	private static int usersallowed = 5;
	private int adminport;
	//private Timer pingTimer;
	private boolean receivingAllowed = true;

	public ServerCore(ChatGUI GUIIn, User adminIn){
		
		GUI = GUIIn;
		localuser = adminIn;
		GUI.setCore(this);
		//pingTimer = new Timer(100, this);
		
		adminport = localuser.getPort();
		
		try {
		    serverSocket = new ServerSocket(adminport);
		} catch (IOException e) {
			System.out.println("Could not listen on port: " + Integer.toString(adminport));
			System.exit(-1);
		}
		
		clientCores = new LinkedList<ClientCore>();
		
		while(GUI.isOnline()){
			
			//if (receivingAllowed){
				
				clientSocket = null;
				connect();
				usercount++;
				
				//pingTimer.setRepeats(false);
				clientCores.add(new ClientCore(GUI, localuser, clientSocket, this));
				System.out.println("Hitta ny användare");
				Thread startthread = new Thread(clientCores.getLast());
				startthread.start();
				//changeReceivingState();
				//pingTimer.start();
				
			
			
				if(usercount > 1){
					GUI.enableKickandFile();
				}
			//}
		}
	}
	
	public void connect(){
		try {
		    clientSocket = serverSocket.accept();
		} catch (IOException e) {
		    System.out.println("Accept failed:" + Integer.toString(adminport));
		    System.exit(-1);
		}
		
		try{
		    out = new PrintWriter(
					  clientSocket.getOutputStream(), true);
		}catch(IOException e){
		    System.out.println("getOutputStream failed: " + e);
		    System.exit(1);
		}
		
		try{
		    in = new BufferedReader(new InputStreamReader(
		            clientSocket.getInputStream()));
		}catch(IOException e){
		    System.out.println("getInputStream failed: " + e);
		    System.exit(1);
		}

		System.out.println("Connection Established: " 
				   + clientSocket.getInetAddress());
		
	}
	
	public void sendMessage(String messageIn){
		   for (ClientCore c:clientCores){
			   c.sendMessage(messageIn);   
		   }
		   
	   }
	
	public void sendRawMessage(String messageIn){
		   for (ClientCore c:clientCores){
			   c.sendRawMessage(messageIn);   
		   }
		   
	   }
	
	public void forwardMessage(String messageIn, ClientCore CoreFrom){
		for (ClientCore c:clientCores){
				if (!c.equals(CoreFrom)){
					c.forwardMessage(messageIn);   
				} 
		   }	
	}
	
	public void kick(){
		ClientCore kickedCore = OptionPanel.kickmessage(clientCores,GUI);
		if(kickedCore != null){
			sendRawMessage("<text color=\"#FF0000\"> " + kickedCore.toString() + " was kicked from this chat </text>");
			kickedCore.sendMessage("<disconnect/>");
			kickedCore.sendRawMessage("<disconnect/>");
			delete(kickedCore);
		}		
	}
		
	public void delete(ClientCore clientcore){
		clientcore.close();
		clientCores.remove(clientcore);
		
		System.out.println(clientCores);
		usercount -= 1;
		if (usercount == 1){
			GUI.disableKickandFile();
		}
		
	}
	
	public void  changeReceivingState(){
		System.out.println("TEST");
		receivingAllowed = (!receivingAllowed); 
	}
	
	public void actionPerformed(ActionEvent e) {
		//pingTimer.stop();
		if(!clientCores.getLast().goodConnection){
			clientCores.getLast().allowBadConnection();			
		}
		
		System.out.println("Hej");
		changeReceivingState();
		
				
	}

}


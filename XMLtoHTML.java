import java.util.Scanner;


public abstract class XMLtoHTML {
	
	private static StringBuilder HTMLBuilder;
	private static Scanner scanner;
	private static String text;
	private static String sender;
	private static boolean legalMessage = false;
	
	
	/**
	 * Converts the chat-message, given in XML to HTML
	 * 
	 * @param XML  - The received message (A String)
	 * @return HTML - The HTML that should be shown for the message received.
	 */
	public static String messageToHTML(String XML){
		
		/*
		 * Prevent tag-abuse
		 */
		XML = fixMessage(XML);
		
		/*
		 * Initialization, using StringBuilder for speed and 
		 * scanner to easily find tags
		 */
		HTMLBuilder = new StringBuilder();                                                          
		scanner = new Scanner(XML);                                                                
		scanner.useDelimiter("<"); 
		
		/*
		 * Only process text between message-tags
		 */
		while (!scanner.hasNext("/message>")){                                                    
			text = scanner.next();                                                             
			if (text.startsWith("message")){                                                      
				legalMessage = true;															
				sender = text.substring(16,text.length()-2);   //Gets the sender from the message attribute sender
			}
			
			/*
			 * Checks if a message is valid (has message-tags) then retrieves
			 * the desired text color and formats the message between text-tags
			 * to valid HTML-format
			 */
			if (legalMessage){
				if(text.startsWith("text")){
					if (text.substring(12,13).equals("#")){  //Colors in hexadecimal both with and without #
						HTMLBuilder.append("<p style = \"color:" + text.substring(12,21));                             
							HTMLBuilder.append(">>> " + sender + ": ");
							HTMLBuilder.append(text.substring(21,text.length()));
					}else{
						HTMLBuilder.append("<p style = \"color:" + "#" + text.substring(12,20));                           
							HTMLBuilder.append(">>> " + sender + ": ");
							HTMLBuilder.append(text.substring(20,text.length()));
					}
				}
			}
		}
			
		scanner.close();   //Resource saving
		
		/*
		 * Finalizes the message in HTML and returns it
		 */
		HTMLBuilder.append("</p>");                        							
		String HTML = HTMLBuilder.toString(); 
		return(HTML);
	}
	
	/**
	 * Finalizes the HTML to be displayed in the Chat
	 * 
	 * @param HTMLMessages - The HTML of the messages to be displayed given as String 
	 * (Often received from messageToHTML)
	 * @return - A string that is the HTML to be displayed in the Chat
	 */
	public static String finalHTML(String HTMLMessages){                        
		return ("<!DOCTYPE html><html><body>" + HTMLMessages + "</body></html>" );
	}
	
	/**
	 * Formats a text-message to a non-embarrassing sendable XML-format 
	 * @param messageIn - The message that wants to be sent, given as a String
	 * @param user - The User sending the message (A User)
	 * @return message - A String in XML-format ready to be sent
	 */
	public static String messageToXML(String messageIn, User user){
		String message = "";
		message += "<message sender=\"";
		message += (user.getName() + "\">");  //The senders name
		message += "<text color=\"";
		message += user.getColor();           //The senders text-color
		message += "\">";
		message += messageIn;
		message += "</text></message>";
		return (message);	
	}
	
	/**
	 * Prevents tag-abuse in received messages, as the norm in 
	 * HTML, < is transformed into &lt; and > is transformed into &gt;
	 * @param XML - The message in XML that needs a tag-makeover (A string)
	 * @return A string that has no tags in the text that can mess up the tag-parser 
	 * or the HTML formatter
	 */
	public static String fixMessage(String XML){
		String message = "";
		int beginning = XML.indexOf("<text color=\"");
		beginning += 22;
		int end = XML.lastIndexOf("</text>");
		
		/*
		 * Everything between text-tags should be text
		 */
		message = XML.substring(beginning,end);
		message = message.replace("<", "&lt;");  //HTML-norm
		message = message.replace(">", "&gt;");  //HTML-norm
		String messagefinal = XML.substring(0,beginning) + message + XML.substring(end, XML.length());
		return messagefinal;
	}
	
	/**
	 * Makes sure that our messages that we send won't mess up
	 * other chat-programs XML-parsers or HTML-formatters
	 * @param message - A String. The message written by our user
	 * @return A message without explicit tags (A String)
	 */
	public static String fixTags(String message){
		message = message.replace("&","&amp;"); //HTML-standard
		message = message.replace("<", "&lt;"); //HTML-standard
		message = message.replace(">", "&gt;"); //HTML-standard
		
		return(message);
	}
	
	/**
	 * Checks what instructions are given by incoming messages
	 * @param messageIn - The incoming message, given as a String
	 * @return A String which is then used by the Core to decide what to do
	 */
	public static String findTask(String messageIn){
		
		/*
		 * A text-message was received
		 */
		if (messageIn.contains("<text") && messageIn.contains("</text>")){
			return("text");	
		/*
		 * A request-message was received (used for joining new chats as a client)
		 */
		}else if (messageIn.contains("<request>") && messageIn.contains("</request>")){
			return("request");
		
		/*
		 * A request-declination was received	
		 */
		}else if(messageIn.contains("<request reply=\"no\">")){
			return("norequest");
		
		/*
		 * A Disconnect-notification was received 
		 */
		}else if(messageIn.contains("<disconnect/>") || messageIn.contains("<disconnect />")){
			return("disconnect");
		/*
		 * Future development
		 */
		}else{
			return("Not implemented");
		}
	}
	
	/**
	 * In a request-message a greeting is often attached between request-tags
	 * this method retrieves that greeting
	 * @param request - The request-message with tags
	 * @return greeting - The greeting between request-tags
	 */
	public static String findGreeting(String request){
		int beginning = request.indexOf("request>");
		int end = request.indexOf("</request>");
		return(request.substring(beginning+8,end));
	}
	
	/**
	 * Finds which chatmember disconnected and returns a HTML-friendly 
	 * log-off message
	 * @param disconnectMessage - The disconnect-message received
	 * @return HTMLdisconnectMessage - A string.
	 */
	public static String findDisconnect(String disconnectMessage){
		int beginning = disconnectMessage.indexOf("sender=");
		int end = disconnectMessage.indexOf(">");
		String HTMLdisconnectMessage = "<p><b>" + disconnectMessage.substring(beginning+8,end-1) + " is now offline" + "</b></p>";
		return(HTMLdisconnectMessage);
	}
	
}

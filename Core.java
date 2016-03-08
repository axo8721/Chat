
public abstract class Core {
	protected ChatGUI GUI;
	protected StringBuilder chatlog = new StringBuilder(); 
	protected User localuser;
	
	public void addToLog(String messageIn){
		messageIn = messageIn.replace("<", "&lt;");
		messageIn = messageIn.replace(">", "&gt;");
		String message = "";
		message += "<p style = \"color:";
		message += localuser.getColor();
		message += "\">";
		message += (">>> " + localuser.getName() + ": ");
		message += messageIn;
		message += "</p>";
		chatlog.append(message);
		
	}
	
	public void addHTMLToLog(String HTMLIn){
		chatlog.append(HTMLIn);
		
	}
	
	public void updateGUI(){
		GUI.updateChat(XMLtoHTML.finalHTML(chatlog.toString()));
	}
	
	public abstract void sendMessage(String messageIn);
	
	public abstract void sendRawMessage(String messageIn);
	
	
 
	
}

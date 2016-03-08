import java.awt.*;
import java.net.InetAddress;

public class User {
	private String userIP;
	private String name;
	private String color;
	private int port;
	
	public User(String IPin, String nameIn, String colorIn, int portIn){
		userIP = IPin;
		name = nameIn;
		color = colorIn;
		port = portIn;
	}
	
	
	public User(String nameIn, String colorIn, int portIn){
		this(null, nameIn, colorIn, portIn);
	}
	
	public String getIP(){
		return(userIP);
	}
	
	public String getName(){
		return(name);
	}
	
	public String getColor(){
		return(color);
	}
	
	public int getPort(){
		return(port);
	}
}

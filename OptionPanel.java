import java.awt.Color;
import java.util.LinkedList;

import javax.swing.*;

public class OptionPanel{
	private JTextField namefield;
	private JTextField adressfield;
	private JTextField portfield;
	private JComboBox colorfield;
	private JPanel optionpane;
	private ChatGUI GUI;
	private User user;
	private static final String[] coloroptions = {"Black", "Red", "Green", "Blue", "Pink", "Orange", "Yellow", "Turquoise", "Purple"}; 
	private static final String[] hexas = {"#000000", "#FF0000", "#00FF00", "#0000FF", "#FF69B4", "#FF7502", "#FFFF00", "#40E0D0", "#800080"};
	
	public OptionPanel(boolean isServer, ChatGUI inGUI){
		GUI = inGUI;
		if (isServer){
			user = serverQuestion();
		}else{
			user = clientQuestion();
		}
	}
	
	public User serverQuestion(){
		namefield = new JTextField("Axel");
	    portfield = new JTextField("1337");
	    colorfield = new JComboBox(coloroptions);
	    colorfield.setEditable(true);
	    
	    namefield.setColumns(10);
	    portfield.setColumns(5);

	    optionpane = new JPanel();
	    optionpane.add(new JLabel("Name:"));
	    optionpane.add(namefield);
	    optionpane.add(Box.createHorizontalStrut(15)); // a spacer
	    optionpane.add(new JLabel("Port:"));
	    optionpane.add(portfield);
	    optionpane.add(new JLabel("Color (Hexadecimal):"));
	    optionpane.add(colorfield);

	    int result = JOptionPane.showConfirmDialog(GUI, optionpane, 
	             "Vital Questions", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	       return (new User(XMLtoHTML.fixTags(namefield.getText()), getColor(), StrtoInt(portfield.getText())));
	    }else{
	    	System.exit(1);
	    	return(null);
	    }
	}
	
	public User clientQuestion(){
		namefield = new JTextField("Patric");
	    adressfield = new JTextField("Karmosin-09");
	    portfield = new JTextField("1337");
	    colorfield = new JComboBox(coloroptions);
	    colorfield.setEditable(true);
	    
	    namefield.setColumns(10);
	    portfield.setColumns(4);
	    adressfield.setColumns(11);

	    optionpane = new JPanel();
	    optionpane.add(new JLabel("Name:"));
	    optionpane.add(namefield);
	    optionpane.add(Box.createHorizontalStrut(15)); // a spacer
	    optionpane.add(new JLabel("IP:"));
	    optionpane.add(adressfield);
	    optionpane.add(Box.createHorizontalStrut(15)); // a spacer
	    optionpane.add(new JLabel("Port:"));
	    optionpane.add(portfield);
	    optionpane.add(new JLabel("Color (Hexadecimal):"));
	    optionpane.add(colorfield);

	    int result = JOptionPane.showConfirmDialog(GUI, optionpane, 
	             "Vital Questions", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
		    return (new User(adressfield.getText(), XMLtoHTML.fixTags(namefield.getText()), getColor() , StrtoInt(portfield.getText())));
		}else{
		    System.exit(1);
		    return(null);
	    }
	}
	
	private String getColor(){
		String colorString = (String) colorfield.getSelectedItem();
		for (int i = 0;i<coloroptions.length; i++){
			if(colorString.equals(coloroptions[i])){
				return(hexas[i]);				
			}	
		}
		return(colorString);
	}
	
	public static int StrtoInt(String numberstr){
		int numberint = 0;
		try{
			numberint = Integer.parseInt(numberstr);
		}catch(NumberFormatException e) {
		    System.out.println(e);
		    System.exit(1);
		}
		return numberint;
	}
	
	public User getUser(){
		return(user);
	}

	public static ClientCore kickmessage(LinkedList<ClientCore> cores,ChatGUI GUIin){
		JPanel kickpanel = new JPanel();
		JComboBox kickOptions = new JComboBox(cores.toArray());
		kickpanel.add(new JLabel("Clients you may kick:"));
		kickpanel.add(kickOptions);
		
		int result = JOptionPane.showConfirmDialog(GUIin, kickpanel, 
	             "Kick Options", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	       return ((ClientCore) kickOptions.getSelectedItem());
	    }else{
	    	return(null);
	    }
	}
}

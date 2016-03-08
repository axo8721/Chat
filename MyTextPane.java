import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JTextPane;

public class MyTextPane extends JTextPane {
	int HaggeShow = 0;	
	
        public MyTextPane() {
            super();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (HaggeShow == 1){
	        	BufferedImage img = null;
	        	setOpaque(false);
	        	try {
	        	    img = ImageIO.read(new File("Hagge.jpg"));
	        	} catch (IOException e) {
	        		System.out.println("Fail");
	        	}
	        	
	            g.drawImage(img, 0, 0, this);
	
	
	            super.paintComponent(g);
            } else {
            	setOpaque(true);
            	super.paintComponent(g);
            }
        }
    }
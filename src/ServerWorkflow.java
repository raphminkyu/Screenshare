import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * Runnable class extending thread to process each client
 * Reads BufferedImage from each client and prints them on each respective JPanel 
 * 
 * @author Raph, Lan
 *
 */
public class ServerWorkflow extends Thread {

    Socket socket;
    JPanel panel;
    int counter; //order of client connected
    HashMap<Integer, Boolean> firstTime; //check if a screenshot has been take

    public ServerWorkflow(Socket socket, int counter, JPanel panel) {
    	
       this.socket = socket;
       this.panel=panel;
       this.counter= counter;
       firstTime = new HashMap<>();
       firstTime.put(counter, false);
    }
    
    public Socket getSocket() {
    	return socket;
    }
    
    public int getPort() {
    	return socket.getPort();
    }

    /**
     * run method
     */
    public void run() {
    	try {
    		while(true) {
    			processBackground();
    		}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}

    }
    /**
     * Recieves image from client socket and changes background of the respective panel
     * 
     * @throws IOException
     */
    private void processBackground() throws IOException {
//   System.out.println("processing background");
        BufferedImage image = ImageIO.read(socket.getInputStream());
    	
    	if(image!=null) {
    		System.out.println(image);
    		changeBackground(image);
        	image.flush();
    	}
    	
    
    }
    
    /**
     * Helper method to dynamically change JPanel
     * 
     * @param img
     */
    private void changeBackground(BufferedImage img) {

    	

        //resized height, width set temporarily for test purpose
        JLabel label = null;
        ImageIcon icon = new ImageIcon(img);
        
    	GridBagConstraints c = new GridBagConstraints();
      	c.fill = GridBagConstraints.VERTICAL;
      	c.gridy=1;
    	
    	if (!firstTime.get(counter)) {

    		label = new JLabel(resizeImageIcon(icon, panel.getWidth()*0.6,  panel.getHeight()*0.6));
            panel.add(label,c);   
            firstTime.replace(counter, true);
        } else {
    	    panel.removeAll();
            JTextArea text = new JTextArea(String.valueOf(counter) + "th client");

            panel.add(text, c);
            label = new JLabel(resizeImageIcon(icon, panel.getWidth()*0.6,  panel.getHeight()*0.6));
    	    panel.add(label,c);
        }



    	panel.updateUI();
    }
    
    /**
     * Helper method to resize Image into an icon
     * 
     * @return resized icon
     */
    private ImageIcon resizeImageIcon(ImageIcon icon,double d, double e) {
    	Image image = icon.getImage(); // transform it 
    	Image newimg = image.getScaledInstance((int) d, (int) e,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    	ImageIcon icon_2 = new ImageIcon(newimg);  // transform it back
    	return icon_2;
    }
   
}

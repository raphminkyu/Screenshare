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
    Map<Integer, JPanel> panels;
    int counter; //order of client connected
    HashMap<Integer, Boolean> firstTime; //check if a screenshot has been take

    public ServerWorkflow(Socket socket, int counter, Map<Integer, JPanel> panels) {
    	
       this.socket = socket;
       this.panels=panels;
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
			processBackground();
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
   
        BufferedImage image = ImageIO.read(socket.getInputStream());
    	System.out.println(image);
    
    	changeBackground(image);
    	image.flush();
    
    }
    
    /**
     * Helper method to dynamically change JPanel
     * 
     * @param img
     */
    private void changeBackground(BufferedImage img) {

    	JPanel panel = panels.get(counter);

        //resized height, width set temporarily for test purpose
        JLabel label = null;
    	if (!firstTime.get(counter)) {
            ImageIcon icon = new ImageIcon(img);
    	    label = new JLabel(resizeImageIcon(icon, 600, 400));
            panel.add(label);
            firstTime.replace(counter, true);
        } else {
    	    panel.remove(label);
            ImageIcon icon = new ImageIcon(img);
            label = new JLabel(resizeImageIcon(icon, 600, 400));
    	    panel.add(label);
        }



    	panel.repaint();
    }
    
    /**
     * Helper method to resize Image into an icon
     * 
     * @return resized icon
     */
    private ImageIcon resizeImageIcon(ImageIcon icon,int x, int y) {
    	Image image = icon.getImage(); // transform it 
    	Image newimg = image.getScaledInstance(x, y,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    	ImageIcon icon_2 = new ImageIcon(newimg);  // transform it back
    	return icon_2;
    }
   
}

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

    public ServerWorkflow(Socket socket, int counter, Map<Integer, JPanel> panels) {
    	
       this.socket = socket;
       this.panels=panels;
       this.counter= counter;

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
    
    }
    
    /**
     * Helper method to dynamically change JPanel
     * 
     * @param img
     */
    private void changeBackground(BufferedImage img) {

    	JPanel panel = panels.get(counter);
    	
        ImageIcon icon = new ImageIcon(img);
        //resized height, width set temporaily for test purpose
    	panel.add(new JLabel(resizeImageIcon(icon, 100,100)));
    	panel.repaint();
    }
    
    /**
     * Helper method to resize Image into an icon
     * 
     * @returns resized icon
     */
    private ImageIcon resizeImageIcon(ImageIcon icon,int x, int y) {
    	Image image = icon.getImage(); // transform it 
    	Image newimg = image.getScaledInstance(x, y,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
    	ImageIcon icon_2 = new ImageIcon(newimg);  // transform it back
    	return icon_2;
    }
   
}

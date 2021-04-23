import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * Simple client for COMP352 Final Project
 * Takes a screenshot of the client's desktop and writes it to socket
 * @author raph, lan
 *
 */
public class Client {

	static Socket socket;
	static OutputStream outputStream;

	/**
	 * Establishes socket connection and opens JFrame
	 * @param argv: none
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws AWTException
	 */
    public static void main(String argv[]) throws UnknownHostException, IOException, AWTException {
    	
    	socket  = new Socket("localhost", 3000);
	    outputStream = socket.getOutputStream();
	    JFrame();
        
    }
    
    /**
     * 
     * @return 
     * @throws AWTException
     */
    public static BufferedImage getScreenshot() throws AWTException {
    
    	Robot robot = new Robot();
    	Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
        
        return screenFullImage;
    }
    
    /**
     * Opens JFrame, with a button
     */
    public static void JFrame() {
    	
    	 JFrame frame = new JFrame();
    	 JPanel header = new JPanel();
    	 
    	 JButton button = null;
		try {
			button = makeButton();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
    	 header.add(button);

    	 frame.add(header);
    	 frame.pack();
         frame.setVisible(true);
  
    }
    
    /**
     * Clicking the button will take screenshot and write it to socket
     * Will be automated without the use of buttons soon
     * 
     * @return button
     * @throws AWTException
     */
    public static JButton makeButton() throws AWTException {
    	 Robot robot = new Robot();
    	 
    	JButton button = new JButton("client");
    	button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    

                    Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                    BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                    System.out.println("screenshot taken");

                    ImageIO.write(screenFullImage, "jpg", outputStream);
                    System.out.println("screenshot done");

                    Thread.sleep(0);
                    
//                    bufferedOutputStream.flush();
                    System.out.println("screenshot donesssss");
                }
               catch (InterruptedException e1) {
					
					e1.printStackTrace();
				} catch (IOException e1) {
				
					e1.printStackTrace();
			}
            }
        });
    	 return button;
    }
   

}

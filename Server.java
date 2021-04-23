import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * Server code for COMP352 Final Project
 * Multithreaded server with a jframe that allows multiple clients to connect and share each client's screen
 * 
 * Last editted 4/22/21
 * @author raph, lan
 *
 */


public class Server {
	
	
	 static Map<Integer, JPanel>  panels; //maps each panel to local variable counter in main
	 static ServerSocket server;
	 static JFrame frame;
	 static int numClients;
	
	 /**
	  * 
	  * @param args[0]: max number of clients able to connect
	  * @throws IOException
	  * @throws NoSuchAlgorithmException
	  */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
    	numClients=Integer.parseInt(args[0]);
    	panels = new HashMap<Integer, JPanel>();
    	server = new ServerSocket(3000);
    	Socket socket= null;
        
        jFrame();
        
        //counter used to track the order of client connected and maps to JPanel
        int counter = 0;
        while(true) {
            try {
                socket = server.accept();
                System.out.println("A client connected. " + socket.getPort() );
                ServerWorkflow swf = new ServerWorkflow(socket, counter, panels);
                counter++;
                swf.start();
            }catch(Exception e) {
                e.printStackTrace();
            }      
        }
    }
    /**
     * Creates a jframe for the server to broadcast screens from multiple clients
     * Contains the header (just a button) + panel * numClients
     */
    public static void jFrame() {
    	// width currently set to 600 for test purposes
    	int width = 600;
    	frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //mainPanel containing header and screenshot Panels
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(width, JFrame.MAXIMIZED_VERT));
        
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(width, 40));
        header.setMaximumSize(new Dimension(width, 40));
        header.setBackground(Color.red);    
        
        JTextArea textArea = new JTextArea("Server JFrame");
        
        header.add(textArea);    
        mainPanel.add(header);

        //vertical size not determined but automatically scaled
         for(int i =0;i<numClients;i++) {
          	JPanel panel = new JPanel();
//          panel.getContentPane.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
          	
      	    panel.setBorder(BorderFactory.createLineBorder(Color.black));
      	    JTextArea text = new JTextArea(String.valueOf(i) + "th client");
//          panel.setPreferredSize(new Dimension(width, 190));
          	
          	panel.add(text, BorderLayout.NORTH);	
          	mainPanel.add(panel);
          	
          	panels.put(i, panel);
          }
         
          frame.getContentPane().add(mainPanel);
          frame.pack();
          frame.setVisible(true); 
    }
    

}
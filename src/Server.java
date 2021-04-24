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
                ServerWorkflow swf = new ServerWorkflow(socket, counter, panels.get(counter));
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
        //needed to use frame.getWidth
        frame.pack();

        //mainPanel containing header and screenshot Panels
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(width, JFrame.MAXIMIZED_VERT));
        mainPanel.setMinimumSize(new Dimension(width, 100));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
        
        int headerHeight = 40;
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(width, headerHeight));
        header.setMaximumSize(new Dimension(width, headerHeight));
        header.setBackground(Color.red);    
        
        JTextArea textArea = new JTextArea("Server JFrame");
        frame.add(mainPanel);
        frame.pack();
        frame.isVisible();
        mainPanel.setVisible(true);
        header.add(textArea);    
        mainPanel.add(header);
        System.out.println(mainPanel.getHeight());
        //vertical size not determined but automatically scaled
         for(int i =0;i<numClients;i++) {
        	
        
          	JPanel panel = new JPanel();
//          GridBagLayout to determine layout and add image/text verticallu
          	panel.setLayout(new GridBagLayout());
          	GridBagConstraints c = new GridBagConstraints();
          	c.fill = GridBagConstraints.VERTICAL;
          	c.gridy=0;
          	
          	//screensize used to determine max size of panel
          	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
          	int screenHeight = screenSize.height;
          	panel.setMaximumSize(new Dimension(width, (screenHeight - headerHeight-10)/numClients));
          	panel.setBorder(BorderFactory.createLineBorder(Color.black));
      	    JTextArea text = new JTextArea(String.valueOf(i) + "th client");

          	panel.add(text, c);
          	mainPanel.add(panel);
          	
          	panels.put(i, panel);
          }
         
          frame.add(mainPanel);
          frame.pack();
          frame.setVisible(true); 
    }
    

}
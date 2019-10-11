package client;

import java.io.IOException;

import javax.swing.SwingUtilities;

/**
 * Starts client 
 */
public class Client {
    
    /**
     * Build GUI and Controller for client
     * @param userName of client
     * @param hostName of server
     * @param portNumber
     * @throws IOException 
     */
    public static void startClient(String userName, String hostName, int portNumber) throws IOException {

        final ClientController controller = new ClientController(userName, hostName, portNumber);
         
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ClientGUI view = new ClientGUI();
                view.setController(controller);
                controller.setView(view);
                
                new Thread(controller).start();    
                
                // Display the window.
                view.setSize(450, 300);
                view.setVisible(true);
            }
        });
    }
    
    /**
     * Initialize popup to start board
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PopupGUI(false);
                } catch (Exception e) {
                    System.out.println("Connection refused.");
                    runWithError();
                }
            }
            public void runWithError() {
                try {
                    new PopupGUI(true);
                } catch (Exception e) {
                    System.out.println("Connection refused.");
                    runWithError();
                }
            }
        });
    }
    
}

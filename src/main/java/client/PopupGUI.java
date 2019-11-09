package client;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.Server;

/**
 * popup for user to set username and join server
 * User inputs IP and port and username they want to connect with
 * 
 * GUI Testing:
 * We checked that the following were true-
 * the window displays a textfield under a "Username" label. The textfield is preset with the user's account name.
 * There is another textfield under a "Server Address" label. This field is preset with "localhost"
 * The final textfield is under a "Port" label and is preset to "4444".
 * Then there is a "cancel" and "join" button. The cancel button cancels the program. The joing button joins the server.
 * If the server is incorrect, the GUI should display a red error label in the window. 
 */
public class PopupGUI extends JFrame {
  
    private static final long serialVersionUID = 1L;

    public PopupGUI(Boolean incorrectServer) throws Exception {
        JTextField username = new JTextField(System.getProperty("user.name"));
        JTextField ip = new JTextField("localhost");
        JTextField port = new JTextField(Integer.toString(Server.DEFAULT_PORT));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username"));
        panel.add(username);
        panel.add(new JLabel("Server Address"));
        panel.add(ip);
        panel.add(new JLabel("Port"));
        panel.add(port);
        if (incorrectServer) {
            JLabel errorLabel = new JLabel("Incorrect Server Address or Port");
            errorLabel.setForeground(Color.red);
            panel.add(errorLabel);
        }
        int result = JOptionPane.showConfirmDialog(null, panel, "Connect To Server",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String userName = username.getText();
            String portName = ip.getText();
            int portNumber = Integer.parseInt(port.getText());
            
            Client.startClient(userName, portName, portNumber);
        } 
    }

}

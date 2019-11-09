package dialogs;

import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ErrorDialog
{
    private JPanel panel = new JPanel(new GridLayout(0, 1));
    private Frame parent;
    private String title;

    /**
     * Constructor
     *
     * @param parent the frame which is our parent
     * @param title the title for the Dialog box
     * @param body the text to display as an error
     */

    public ErrorDialog(Frame parent, String title, String body)
    {
        JLabel errorLabel = new JLabel(body);
        errorLabel.setForeground(Color.red);
        panel.add(errorLabel);
        this.parent = parent;
        this.title = title;
    }

    public void show(){
        JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
}

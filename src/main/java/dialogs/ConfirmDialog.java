package dialogs;

import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConfirmDialog
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

    public ConfirmDialog(Frame parent, String title, String body)
    {
        panel.add(new JLabel(body));
        this.parent = parent;
        this.title = title;
    }

    public boolean result(){
        int result = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}

package dialogs;

import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ErrorDialog
{
    private Frame parent;
    private String title;
    private String message;

    /**
     * Constructor
     *
     * @param parent the frame which is our parent
     * @param title the title for the Dialog box
     * @param message the text to display as an error
     */

    public ErrorDialog(Frame parent, String title, String message)
    {
        this.parent = parent;
        this.title = title;
        this.message = message;
    }

    public void show(){
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
}

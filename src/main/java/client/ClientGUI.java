package client;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import models.BoardModel;
import models.UserTableModel;
import name.BoardIdentifier;
import name.Identifiable;
import name.LayerIdentifier;
import stroke.StrokeProperties;
import stroke.StrokeType;
import stroke.StrokeTypeBasic;
import stroke.StrokeTypeFur;
import stroke.StrokeTypePressure;
import stroke.StrokeTypeProc6;
import stroke.StrokeTypeProc7;
import stroke.StrokeTypeSquares;
import util.ColorSquareIcon;
import util.MineralNames;
import util.Utils;
import canvas.layer.Layer;
import canvas.layer.LayerAdjustment;
import canvas.layer.LayerProperties;

/**
 * GUI for client
 * 
 * GUI Testing:
 * As we ran our application we expected to see the following:
 * When it starts, there should be menu bar with grey empty window. 
 * Under "File" in the menu bar there are 3 options:
 *  -"New Board" 
 *  -"Join Board" A submenu that lists other whiteboards on the server.
 *  -"Save to pgn" currently grayed out
 * When "New Board" is clicked, a popup asks for a board name and board dimensions. It then creates this board on the screen.
 * In the case of "Join Baord", a board should appear with work already done by other clients.
 * A side bar appears next to the canvas and the "Save to png" menu item becomes clickable.
 * If the paint button is selected in the sidebar, the user should be able to paint on the canvas.
 * Clicking the stroke button should reveal a slider that alters the stroke width. When dragging the slider, the brush cursor
 * should change size (in on mac) and the stroke button icon should change size. Clicking the color button should change the
 * brushes drawing color and the color in the button icon. Selecting items in the brush dropdown should change the way the 
 * mouse draws on the screen. Changing the symmetry values will add symmetry as the user draws. Selecting the paint bucket icon will
 * turn on fill mode and allow the user to fill selections of the painting (in the same manner as MS Paint). Only the color option
 * effects the fill mode. Clicking the eraser allows the user to erase items on the canvas. Only stroke size effects how the erase works.
 * Clicking the dropper tool allows the user to select a color on the canvas by clicking the pixel. Once the user picks a color, the picker
 * turns off and switched to the brush. No two tools can be selected at the same time. Selecting one should deselect the others.
 * In the layers panel, the current layers should be visible. Clicking the new layer button should add a new layer. The layer will be added 
 * above the other layers. Drawing should only occur on the layer selected. If a layer is higher than another, the the associated paint, will
 * be above the paint on the other layer. Clicking the up and down arrows will move the selected layer up and down. Unchecking the visible checkbox 
 * on the selected layer will turn off its visibility. Changing the opacity slider will change the opacity of the selected layer. 
 * Each layer should have its own unique opacity.
 * The chat client should show the currently connected users. Typing a message in the text box and pressing send will send the message to other
 * users in the same session. If other users send messages, you should see the messages in the chat.
 * Finally, if you press the "Save to png" button, a dialog should popup with a file browsers. When a locations is selected, the image
 * should save in that location.
 * 
 * Icons are modifications of icons found at www.iconfinder.com
 */
class ClientGUI extends JFrame{
    private static final long serialVersionUID = -8313236674630578250L;
    
    private final Container container;
    private final GroupLayout layout;
    private JPanel canvas;
    
    private final boolean isWindows;
    
    private final JMenuItem save;
    
    private final JMenu joinBoardSubmenu;
    private final JButton colorButton;
    private final JToggleButton dropperToggle;
    private final JColorChooser colorChooser;
    private final JButton strokeButton;
    private final JSlider strokeSlider;
    private final JToggleButton eraseToggle;
    private final JToggleButton fillToggle;
    private final JToggleButton brushToggle;
    private final JToggleButton cloneToggle;
    private final JComboBox<StrokeType> strokeDropdown;
    private final JSlider symetrySlider;
    private JSlider opacitySlider;
    
    private final StrokeType[] strokeTypes;
    
    private Cursor brushCursor;
    private Cursor fillCursor;
    private Cursor dropperCursor;
    private Cursor eraserCursor;
    private final Image iconImage;
    
    private final JPanel sidebar;
    private final JPanel chatBar;
    private String title="Whiteboard: Interactive Drawing Tool";
    
    private final JTable userTable;
    private final JTextArea chatText;
    private final UserTableModel userTableModel;
    
    private final Vector<Vector<Object>> layerVector;
    private final LayerTableModel layerTableModel;
    private final JTable layerTable;
    
    private static final int STROKE_MAX = 20;
    private static final int STROKE_MIN = 1;
    private static final int STROKE_INIT = StrokeProperties.DEFAULT_STROKE_WIDTH;
    
    private ClientController controller;
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public ClientGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");  
        setResizable(false);
        
        // Set the title
        setTitle(title);
        
        this.container = this.getContentPane();
        this.layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);    
        
        // Create Cursors
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        this.iconImage = toolkit.getImage(getResource("cursor.png"));
        Point hotSpot = new Point(16,16);
        this.brushCursor = toolkit.createCustomCursor(iconImage, hotSpot, "circleBrush");

        Image fillImage = toolkit.getImage(getResource("fillCursor.png"));
        hotSpot = new Point(2,30);
        this.fillCursor = toolkit.createCustomCursor(fillImage, hotSpot, "fillCursor");

        fillImage = toolkit.getImage(getResource("eyedropperCursor.png"));
        hotSpot = new Point(2,30);
        this.dropperCursor = toolkit.createCustomCursor(fillImage, hotSpot, "dropperCursor");

        fillImage = toolkit.getImage(getResource("eraserCursor.png"));
        hotSpot = new Point(2,2);
        this.eraserCursor = toolkit.createCustomCursor(fillImage, hotSpot, "eraserCursor");

        // Create swing objects
        this.colorButton = new JButton("Color");
        this.colorButton.setIcon(new ColorSquareIcon(10, Color.black));
        this.colorButton.setFocusPainted(false);
        this.colorButton.setHorizontalAlignment(SwingConstants.LEFT);
        this.colorChooser = new JColorChooser();
        this.strokeButton = new JButton("Stroke");
        this.strokeButton.setFocusPainted(false);
        this.strokeButton.setIcon(new ColorSquareIcon(STROKE_INIT, Color.black));

        this.strokeButton.setHorizontalAlignment(SwingConstants.LEFT);
        this.strokeSlider = new JSlider(JSlider.HORIZONTAL, STROKE_MIN, STROKE_MAX, STROKE_INIT);

        this.dropperToggle = new JToggleButton(new ImageIcon(((new ImageIcon(getResource("eyedropperIcon.png"))).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.dropperToggle.setDisabledIcon(new ImageIcon(((new ImageIcon(getResource("eyedropperSelectedIcon.png"))).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.dropperToggle.setFocusPainted(false);
        this.dropperToggle.setPreferredSize(new Dimension(40, 40));
        this.eraseToggle = new JToggleButton(new ImageIcon(((new ImageIcon(getResource("eraserIcon.png"))).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.eraseToggle.setDisabledIcon(new ImageIcon(((new ImageIcon(getResource("eraserSelectedIcon.png"))).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.eraseToggle.setPreferredSize(new Dimension(40,40));
        this.eraseToggle.setFocusPainted(false);
        this.fillToggle = new JToggleButton(new ImageIcon(((new ImageIcon(getResource("fillIcon.png"))).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.fillToggle.setDisabledIcon(new ImageIcon(((new ImageIcon(getResource("fillSelectedIcon.png"))).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.fillToggle.setFocusPainted(false);
        this.fillToggle.setPreferredSize(new Dimension(40,40));
        this.cloneToggle = new JToggleButton(new ImageIcon(((new ImageIcon(getResource("stampIcon.png"))).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.cloneToggle.setDisabledIcon(new ImageIcon(((new ImageIcon(getResource("stampSelectedIcon.png"))).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.cloneToggle.setFocusPainted(false);
        this.cloneToggle.setPreferredSize(new Dimension(40,40));
        this.brushToggle = new JToggleButton(new ImageIcon(((new ImageIcon(getResource("brushIcon.png"))).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.brushToggle.setDisabledIcon(new ImageIcon(((new ImageIcon(getResource("brushSelectedIcon.png"))).getImage())
                .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        this.brushToggle.setPreferredSize(new Dimension(40,40));
        this.brushToggle.setFocusPainted(false);
        this.brushToggle.setEnabled(false);
        this.brushToggle.setSelected(true);

        this.symetrySlider = new JSlider(JSlider.HORIZONTAL, 1, 7, 1);
        this.symetrySlider.setMinorTickSpacing(1);
        this.symetrySlider.setPaintTicks(true);
        this.symetrySlider.setSnapToTicks(true);

        //Build stroke dropdown
        this.strokeTypes = new StrokeType[6];
            strokeTypes[0] = new StrokeTypeBasic();
            strokeTypes[1] = new StrokeTypePressure();
            strokeTypes[2] = new StrokeTypeProc6();
            strokeTypes[3] = new StrokeTypeSquares();
            strokeTypes[4] = new StrokeTypeFur();
            strokeTypes[5] = new StrokeTypeProc7();
            // Other strokes that can be added by uncommenting and changing the stroketypes array size
            //strokeTypes[6] = new StrokeTypeSpray();
            //strokeTypes[7] = new StrokeTypeProc2();
            //strokeTypes[8] = new StrokeTypeProc1();
            //strokeTypes[9] = new StrokeTypeProc3();
            //strokeTypes[10] = new StrokeTypeProc4();
            //strokeTypes[11] = new StrokeTypeProc5();


       this.strokeDropdown = new JComboBox<StrokeType>(strokeTypes);
       this.strokeDropdown.setFocusable(false);

        // Join Board submenu.
        this.joinBoardSubmenu = new JMenu("Join Board");
        this.save = new JMenuItem("Save to png", KeyEvent.VK_T);

        //Create Sidebar objects
        this.sidebar = new JPanel();
        this.chatBar = new JPanel();
        this.chatText = new JTextArea();
        this.chatText.setEditable(false);
        this.userTableModel = new UserTableModel();
        this.userTable = new JTable(this.userTableModel);

        this.layerVector = new Vector<Vector<Object>>();
        this.layerTableModel = new LayerTableModel(layerVector);
        this.layerTable = new JTable(this.layerTableModel);
        this.layerTable.setRowHeight(50);
        this.layerTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        this.layerTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        this.layerTable.setShowVerticalLines(false);
        this.layerTable.setSelectionBackground(Color.LIGHT_GRAY);
        this.layerTable.setSelectionForeground(Color.BLACK);
        this.layerTable.setGridColor(Color.GRAY);
        this.layerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        setSideBar();

        if (isWindows) {
            this.brushCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            this.colorButton.setPreferredSize(new Dimension(80,30));
            this.strokeButton.setPreferredSize(new Dimension(90, 30));
            this.strokeDropdown.setPreferredSize(new Dimension(120,30));
        }
        else {
            this.colorButton.setPreferredSize(new Dimension(60,30));
            this.strokeButton.setPreferredSize(new Dimension(70, 30));
            this.strokeDropdown.setPreferredSize(new Dimension(100,30));
        }


        // Create and set up the content pane.
        buildMenuBar();
        setContentPaneGUI(null);

    }

    private URL getResource(String resourcePath) {
        return this.getClass().getClassLoader().getResource(resourcePath);
    }

    public void setController(ClientController controller) {
        assert this.controller == null;
        this.controller = controller;

    }

    /**
     * Builds sidebar from components
     */
    private void setSideBar() {

        JPanel brushPanel = new JPanel();
        brushPanel.add(strokeButton);
        brushPanel.add(colorButton);
        brushPanel.add(strokeDropdown);

        JPanel sliderPanel = new JPanel();
        sliderPanel.add(new JLabel("Symmetry"));
        sliderPanel.add(symetrySlider);

        JPanel allBrushPanel = new JPanel();
        allBrushPanel.setLayout(new BoxLayout(allBrushPanel, BoxLayout.Y_AXIS));
        allBrushPanel.add(brushPanel);
        allBrushPanel.add(sliderPanel);
        TitledBorder brushPanelBorder = BorderFactory.createTitledBorder("Brush Settings");
        allBrushPanel.setBorder(brushPanelBorder);

        layerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
                if (layerTableModel.getRowCount() <= 0)
                	return;
                setOpacitySlider(((LayerProperties)layerTableModel.getValueAt(layerTable.getSelectedRow(), 2)).getOpacity());
			}
        });

        JPanel layerPanel = new JPanel();
        layerPanel.setLayout(new BoxLayout(layerPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(layerTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(120,150));
        layerPanel.add(scrollPane);
        JPanel layerButtons = new JPanel();
        opacitySlider = new JSlider(1,100,100);
        JButton newLayerButton = new JButton(new ImageIcon(((new ImageIcon(getResource("newPage.png"))).getImage())
                .getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
        newLayerButton.setPreferredSize(new Dimension(20,20));
        newLayerButton.setFocusable(false);
        newLayerButton.setMnemonic(java.awt.event.KeyEvent.VK_L);
        JButton upButton = new JButton(new ImageIcon(((new ImageIcon(getResource("upArrowIcon.png"))).getImage())
                .getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
        upButton.setPreferredSize(new Dimension(20,20));
        upButton.setFocusable(false);
        JButton downButton = new JButton(new ImageIcon(((new ImageIcon(getResource("downArrowIcon.png"))).getImage())
                .getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH))); 
        downButton.setPreferredSize(new Dimension(20,20));
        downButton.setFocusable(false);
        layerButtons.add(new JLabel("Opacity"));
        layerButtons.add(opacitySlider);
        layerButtons.add(newLayerButton);
        layerButtons.add(upButton);
        layerButtons.add(downButton);
        layerPanel.add(layerButtons);
        TitledBorder layersBorder = BorderFactory.createTitledBorder("Layers");
        layerPanel.setBorder(layersBorder);
        
        JPanel toolBar = new JPanel();
        toolBar.add(brushToggle);
        toolBar.add(fillToggle);
        //uncomment if clone implemented
        //toolBar.add(cloneToggle);
        toolBar.add(eraseToggle);
        toolBar.add(dropperToggle);
        TitledBorder toolBorder = BorderFactory.createTitledBorder("Tools");
        toolBar.setBorder(toolBorder);
        
        setChatClient();
        TitledBorder chatBorder = BorderFactory.createTitledBorder("Chat Client");
        chatBar.setBorder(chatBorder);
        
        sidebar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.weighty = 1;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(5,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        sidebar.add(allBrushPanel, c);
        
        c.gridx = 0;
        c.gridy = 1;
        sidebar.add(toolBar, c); 
        
        c.gridx = 0;
        c.gridy = 2;
        sidebar.add(layerPanel, c); 
        
        c.gridx = 0;
        c.gridy = 3;
        sidebar.add(chatBar, c);
        
        newLayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int layerNum = layerTable.getRowCount();
                JTextField layerName = new JTextField("Layer " + layerNum);
                JPanel panel = new JPanel(new GridLayout(0, 1));
                
                panel.add(new JLabel("Layer Name"));
                panel.add(layerName);
                
                int result = JOptionPane.showConfirmDialog(null, panel, "Create Layer",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    LayerIdentifier layerIdentifier = new LayerIdentifier(Utils.generateId(), layerName.getText());
                    controller.addLayer(layerIdentifier);
                }
            }
        });
        
        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	controller.adjustLayer(selectedLayer(), LayerAdjustment.UP);	
            }
        });
        
        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	controller.adjustLayer(selectedLayer(), LayerAdjustment.DOWN);
            }
        });
        
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setColor(e);
            }
        });
        
        opacitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            	double value = opacitySlider.getValue()/100.0;

        		LayerProperties properties = selectedLayer();
        		properties.setOpacity(value);
        		controller.adjustLayer(properties, LayerAdjustment.PROPERTIES);
            }
        });

        strokeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popup = new JPopupMenu();
                popup.add(strokeSlider);
                popup.pack();
                Point pos = new Point();
                Dimension size = popup.getPreferredSize();
                pos.x = (strokeButton.getWidth()/2 - size.width/2);
                pos.y = (strokeButton.getHeight());
                popup.show(strokeButton, pos.x, pos.y);
            }
        });
        
        strokeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateCursor();
                setStroke();
            }
        });
        
        strokeDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setStrokeType((StrokeType)strokeDropdown.getSelectedItem());
            }
        });
        
        symetrySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.setSymetry(symetrySlider.getValue());;
            }
        });
        
        brushToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (brushToggle.isSelected()) {
                    setBrushToggles(true,false,false,false,false);
                    brushToggle.setEnabled(false);
                    updateCursor();
                }
                else {
                    brushToggle.setEnabled(true);
                }
            }
        });
        
        cloneToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (cloneToggle.isSelected()) {
                    cloneToggle.setEnabled(false);
                    setBrushToggles(false,false,true,false,false);
                }
                else {
                    cloneToggle.setEnabled(true);
                }
            }
        });
        
        eraseToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (eraseToggle.isSelected()) {
                    eraseToggle.setEnabled(false);
                    setBrushToggles(false,false,false,true,false);
                    controller.setEraserOn(true);
                    canvas.setCursor(eraserCursor);
                }
                else {
                    eraseToggle.setEnabled(true);
                    controller.setEraserOn(false);
                }
            }
        });
        
        fillToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (fillToggle.isSelected()) {
                    fillToggle.setEnabled(false);
                    setBrushToggles(false,true,false,false,false);
                    controller.setFillOn(true);
                    canvas.setCursor(fillCursor);
                }
                else {
                    fillToggle.setEnabled(true);
                    controller.setFillOn(false);
                }
            }
        });
        
        dropperToggle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (dropperToggle.isSelected()) {
                    dropperToggle.setEnabled(false);
                    setBrushToggles(false,false,false,false,true);
                    try {
                        final Robot robot = new Robot();
                        canvas.setCursor(dropperCursor);

                        controller.setStrokeWidth(0);
                        MouseListener mouseListener = new MouseListener() {
                            
                        	@Override
                            public void mouseClicked(MouseEvent e) {
                                PointerInfo pointer;
                                pointer = MouseInfo.getPointerInfo();
                                Point coord = pointer.getLocation();
                                Color color = robot.getPixelColor((int)coord.getX(), (int)coord.getX());
                                colorButton.setIcon(new ColorSquareIcon(10, color));
                                controller.setStrokeColor(color);
                                canvas.removeMouseListener(this); 
                                brushToggle.setSelected(true);
                                updateCursor();
                                setStroke();
                            }
                            @Override
                            public void mouseEntered(MouseEvent arg0) { }
    
                            @Override
                            public void mouseExited(MouseEvent arg0) { }
    
                            @Override
                            public void mousePressed(MouseEvent arg0) { }
    
                            @Override
                            public void mouseReleased(MouseEvent arg0) { }
                        };
                        canvas.addMouseListener(mouseListener);
                    } catch (AWTException e1) {
                        e1.printStackTrace();
                    }
                } 
                else {
                    dropperToggle.setEnabled(true);
                }  
            }
        });
        
    }
    
    private void setBrushToggles(boolean brush, boolean fill, boolean clone, boolean eraser, boolean dropper) {
        brushToggle.setSelected(brush);
        cloneToggle.setSelected(clone);
        eraseToggle.setSelected(eraser);
        fillToggle.setSelected(fill);
        dropperToggle.setSelected(dropper);
    }
    
    /**
     * Builds chat client panel
     */
    private void setChatClient() {
        chatBar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        final JTextField inputTextField = new JTextField();
        JButton sendButton = new JButton("Send");
        JScrollPane scroll = new JScrollPane (chatText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        chatBar.setLayout(new GridBagLayout());
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,0,0,0);
        c.ipadx = 120;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        chatBar.add(userTable, c);
        
        c.insets = new Insets(10,0,0,0);
        c.ipady = 60;
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 1;
        chatBar.add(scroll, c);

        c.insets = new Insets(5,0,0,0);
        c.ipady = 0;
        c.gridx = 0;
        c.gridy = 2;
        chatBar.add(inputTextField, c);
        
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0,0,0,0);
        c.weightx = 0.2;
        c.gridx = 0;
        c.gridy = 3;
        chatBar.add(sendButton, c);


        
        ActionListener enterText = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sendMessage(inputTextField.getText());
                inputTextField.setText("");
            }
        };
        
        sendButton.addActionListener(enterText);
        inputTextField.addActionListener(enterText);
        
        
    }
    
    /**
     * Builds the top menu bar
     */
    private void buildMenuBar() {     
        // Create the menu bar.
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem newBoard = new JMenuItem("New Board", KeyEvent.VK_T);
        this.save.setEnabled(false);
        
        // Build the first menu.
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        
        
        this.save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        
        this.save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCanvas();
            }
        });
        
        newBoard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newBoard.getAccessibleContext().setAccessibleDescription("Create a new Board");
        
        newBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBoardAction();
            }
        });

        menu.add(newBoard);
        menu.add(joinBoardSubmenu);
        menu.add(this.save);
        setJMenuBar(menuBar);
    }
    

    
    private void setBoardNames(BoardIdentifier[] boardNames) {
    	joinBoardSubmenu.removeAll();
        if (boardNames == null) {
        	return;
        }
        
        for (final BoardIdentifier boardName : boardNames) {
            JMenuItem subMenuItem = new JMenuItem(boardName.name());        

            subMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    joinBoardAction(boardName); 
                } 
            });
            
            joinBoardSubmenu.add(subMenuItem);
        }
    }
    
    private void setColor(ActionEvent e) {
        colorChooser.setPreviewPanel(new JPanel());
        AbstractColorChooserPanel[] oldPanels = colorChooser.getChooserPanels();
        for (int i = 0; i < oldPanels.length; i++) {
          String clsName = oldPanels[i].getClass().getName();
          if (clsName.equals("javax.swing.colorchooser.DefaultSwatchChooserPanel")) {
            colorChooser.removeChooserPanel(oldPanels[i]);
          }
        }
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Color color = colorChooser.getColor();
                colorButton.setIcon(new ColorSquareIcon(10, color));
                controller.setStrokeColor(color);
            }
          };
        
        JDialog dialog = JColorChooser.createDialog((Component) (e.getSource()), "Color Picker", false, colorChooser, okListener, null);
        dialog.setVisible(true);
        
    }
    
    private void setStroke() {
        strokeButton.setIcon(new ColorSquareIcon(Math.max((int)(strokeSlider.getValue() * 3/5), 2), Color.black));
        controller.setStrokeWidth(strokeSlider.getValue());
    }
    
    private void setOpacitySlider(double opacity) {
    	opacitySlider.setValue((int) (opacity*100));
    }
    
    private void setContentPaneGUI(BoardModel model) {
        if (model != null) {
            this.save.setEnabled(true);
            this.canvas = model;
            updateCursor();
            container.removeAll();
            Group horizontal = layout.createSequentialGroup();
            horizontal.addGroup(layout.createParallelGroup(LEADING)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(canvas)
                            .addComponent(sidebar)));
            layout.setHorizontalGroup(horizontal);
            
            Group vertical = layout.createSequentialGroup();
            vertical.addGroup(layout.createParallelGroup(BASELINE)
                    .addComponent(canvas)
                    .addComponent(sidebar));
            layout.setVerticalGroup(vertical);
            this.pack();
        }
    }
    
    /**
     * Update the brush cursor circle to resize according the the stroke.
     * The resizing cursor is not supported on Windows, defaults to crosshair.
     */
    private void updateCursor() {
        if (!isWindows) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            int strokeWidth = strokeSlider.getValue();
            Image scaledIcon = iconImage.getScaledInstance(strokeWidth, strokeWidth, Image.SCALE_SMOOTH);
            Point hotSpot = new Point(strokeWidth/2, strokeWidth/2);
            brushCursor = toolkit.createCustomCursor(scaledIcon, hotSpot, "circleBrush");
            this.canvas.setCursor(brushCursor);
        }
        else {
            this.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }
    
    private void newBoardAction() {
        
        JTextField inputBoardName = new JTextField(MineralNames.getName());
        JTextField widthName = new JTextField("512");
        JTextField heightName = new JTextField("512");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        panel.add(new JLabel("Whiteboard Name"));
        panel.add(inputBoardName);
        panel.add(new JLabel("Width (px)"));
        panel.add(widthName);
        panel.add(new JLabel("Height (px)"));
        panel.add(heightName);
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Create Board",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            BoardIdentifier boardName = new BoardIdentifier(Utils.generateId(), inputBoardName.getText());
            this.setTitle(title+ " - " + boardName.name());
            controller.generateNewBoard(boardName, Integer.parseInt(widthName.getText()),
                						Integer.parseInt(heightName.getText()));

        } 
        
    }
    
    private void saveCanvas() {
        BufferedImage bi = new BufferedImage(controller.getBoardWidth(), controller.getBoardHeight(), BufferedImage.TYPE_INT_ARGB); 
        Graphics g = bi.createGraphics();
        canvas.paint(g);
        g.dispose();
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try{
                ImageIO.write(bi,"png",file);
                }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void joinBoardAction(BoardIdentifier boardName) {
        this.setTitle(boardName.name());
        controller.connectToBoard(boardName);
    }
    
    /**
     * Set the current model and allow the user to draw to the screen.
     * @param model
     */
    public void setModel(final BoardModel model) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setContentPaneGUI(model);
                setLayers(model.canvas().layers());
            }
        });
    }

    /**
     * Update the list of users from the current model.
     */
	public void setUserList(Identifiable[] users) {
        assert users.length > 0;
        final Object[][] tableData = new Object[users.length][1];
        
        for (int i = 0; i < users.length; i++) {        
            tableData[i][0] = users[i].identifier().name();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                userTableModel.updateData(tableData);
                pack();
            }
        });
    }
    
    /**
     * Update the list of boards.
     * @param boards
     */
    public void updateBoardList(final BoardIdentifier[] boards) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setBoardNames(boards);
            }
        });
    }
    
    public void setLayers(final Layer[] layers) {        
    	SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	LayerProperties selectedLayer = layerTable.getRowCount() == 0 ?
	        			layers[0].layerProperties() : selectedLayer();
	        	int newSelectedRowNum=0;
	        	int oldNumRows = layerTable.getRowCount();
	    	    layerTableModel.clearTable();
	        	
	        	for (int i = layers.length -1; i >= 0 ; i--) {
	        		Layer l = layers[i];
	    	        BufferedImage img = layers[i].image();
	    	        Image newimg = img.getScaledInstance(45, 45,  java.awt.Image.SCALE_SMOOTH);  
	    	        ImageIcon newIcon = new ImageIcon(newimg); 
	    	        
	    	        if (l.layerProperties().layerIdentifier().equals(selectedLayer.layerIdentifier()))
	    	        	newSelectedRowNum=layers.length - i - 1;
	    	        Vector<Object> newLayer = new Vector<Object>();
	    	        newLayer.add(l.layerProperties().getVisibility());
	    	        newLayer.add(newIcon);
	    	        newLayer.add(l.layerProperties().clone());
	 
	    	        layerTableModel.addRow(newLayer);
	    	        layerTable.revalidate();
	        	}
	        	if (layerTable.getRowCount() == oldNumRows) 
	        		layerTable.setRowSelectionInterval(newSelectedRowNum, newSelectedRowNum);
	        	else
	        		layerTable.setRowSelectionInterval(0, 0);
	        	
	        }
	    });
    }
    
    public LayerProperties selectedLayer() {
    	int selectedRow = layerTable.getSelectedRow();
    	if (selectedRow == -1)
    		selectedRow = 0;
    	
    	return (LayerProperties) layerTableModel.getValueAt(selectedRow, 2);
    }
    /**
     * Add new chat message
     * @param string
     */
    public void addChatLine(final String string) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	chatText.append(string + "\n");
            }
        });
    }
    
    
    public class LayerTableModel extends AbstractTableModel {
        private static final long serialVersionUID = -1985093762031490019L;
        
        private Vector<Vector<Object>> data;
        private final String[] COLUMN_NAMES = new String[] {"Visible", "Thumbnail", "Name"};
        private final Class<?>[] COLUMN_TYPES = new Class<?>[] {Boolean.class, Icon.class, LayerProperties.class};
        
        public LayerTableModel(Vector<Vector<Object>> data) {
            this.data = data;
        }
        
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        public int getRowCount() {
            return data.size();
        }

        public String getColumnName(int col) {
            return COLUMN_NAMES[col];
        }

        public Object getValueAt(int row, int col) {
            return data.get(row).get(col);
        }
        
        public boolean isCellEditable(int row, int column){
            if (column == 0) {
                return true;
            }
            else {
                return false;
            }
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return COLUMN_TYPES[columnIndex];
        }

        public void setValueAt(Object value, int row, int col) {
            if(col == 0){
        		LayerProperties properties = selectedLayer();
        		properties.setVisibility((boolean)value);
        		controller.adjustLayer(properties, LayerAdjustment.PROPERTIES);
            } else {
                data.get(row).set(col, value);
            }
            fireTableCellUpdated(row, col);
        } 
        
        /**
         * Adds new row to the table.
         * @param row The row should have a boolean, image, and layer name, in that order
         */
        public void addRow(Vector<Object> row) {
            data.add(row);
            fireTableRowsInserted(0, getRowCount() - 1);
        }
        
        /**
         * Clears the table
         */
		public void clearTable() {
			data=new Vector<Vector<Object>>();
			fireTableDataChanged();
		}


    }
    
}


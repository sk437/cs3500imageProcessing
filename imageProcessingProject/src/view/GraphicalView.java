package view;

import controller.ImageProcessingController;
import controller.ProcessingController;
import imageasgraph.FixedSizeGraph;
import imageasgraph.Node;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import layeredimage.LayeredImage;
import layeredimage.ViewModel;

public class GraphicalView extends JFrame implements View, ActionListener {

  private final ImageProcessingController controller;
  private ViewModel display;
  private String currentImageName;
  private String currentLayerName;

  private JScrollPane mainScrollPane;
  private JPanel mainPanel;
  private ImageIcon imagePanel;
  private JPanel layerSelectors;
  private ButtonGroup layers;
  private JPanel layerMoveUppers;
  private JPanel layerMoveDowners;
  private JPanel layerCopiers;
  private JPanel layerSaveAsImagers;


  public GraphicalView() {
    super();
    setTitle("Image Processing Interactive Interface");
    setSize(500, 500);

    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainScrollPane = new JScrollPane(mainPanel);

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
    JButton fileOpen = new JButton("Load");
    fileOpen.setActionCommand("Load File");
    fileOpen.addActionListener(this);
    topPanel.add(fileOpen);
    JButton fileSave = new JButton("Save");
    fileSave.setActionCommand("Save File");
    fileSave.addActionListener(this);
    topPanel.add(fileSave);
    JButton export = new JButton("Export as Image");
    export.setActionCommand("Export as Image");
    export.addActionListener(this);
    topPanel.add(export);
    JButton execute = new JButton("Execute Script");
    execute.setActionCommand("Execute a Script");
    execute.addActionListener(this);
    topPanel.add(execute);
    JScrollPane topScrollPane = new JScrollPane(topPanel);

    imagePanel = new ImageIcon();
    JLabel imageHolder = new JLabel();
    imageHolder.setLayout(new BorderLayout());
    imageHolder.setIcon(imagePanel);
    JScrollPane imageScrollPane = new JScrollPane(imageHolder);

    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
    JButton blur = new JButton("Blur");
    blur.setActionCommand("Blur");
    blur.addActionListener(this);
    leftPanel.add(blur);
    JButton sharpen = new JButton("Sharpen");
    sharpen.setActionCommand("Sharpen");
    sharpen.addActionListener(this);
    leftPanel.add(sharpen);
    JButton greyscale = new JButton("Greyscale");
    greyscale.setActionCommand("Greyscale");
    greyscale.addActionListener(this);
    leftPanel.add(greyscale);
    JButton sepia = new JButton("Sepia");
    sepia.setActionCommand("Sepia");
    sepia.addActionListener(this);
    leftPanel.add(sepia);
    JScrollPane leftScrollPane = new JScrollPane(leftPanel);

    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
    JPanel addLoadLayer = new JPanel();
    addLoadLayer.setLayout(new BoxLayout(addLoadLayer, BoxLayout.LINE_AXIS));
    JButton addNewLayer = new JButton("New Layer");
    addNewLayer.setActionCommand("Add New Layer");
    addNewLayer.addActionListener(this);
    addLoadLayer.add(addNewLayer);
    JButton loadLayer = new JButton("Load Image");
    loadLayer.setActionCommand("Load Image As Layer");
    loadLayer.addActionListener(this);
    addLoadLayer.add(loadLayer);
    rightPanel.add(addLoadLayer);
    JPanel layerInterface = new JPanel();
    layerInterface.setLayout(new BoxLayout(layerInterface, BoxLayout.LINE_AXIS));
    layerSelectors = new JPanel();
    layerSelectors.setLayout(new BoxLayout(layerSelectors, BoxLayout.PAGE_AXIS));
    layerInterface.add(layerSelectors);
    layers = new ButtonGroup();
    layerMoveUppers = new JPanel();
    layerMoveUppers.setLayout(new BoxLayout(layerMoveUppers, BoxLayout.PAGE_AXIS));
    layerInterface.add(layerMoveUppers);
    layerMoveDowners = new JPanel();
    layerMoveDowners.setLayout(new BoxLayout(layerMoveDowners, BoxLayout.PAGE_AXIS));
    layerInterface.add(layerMoveDowners);
    layerCopiers = new JPanel();
    layerCopiers.setLayout(new BoxLayout(layerCopiers, BoxLayout.PAGE_AXIS));
    layerInterface.add(layerCopiers);
    layerSaveAsImagers = new JPanel();
    layerSaveAsImagers.setLayout(new BoxLayout(layerSaveAsImagers, BoxLayout.PAGE_AXIS));
    layerInterface.add(layerSaveAsImagers);
    rightPanel.add(layerInterface);
    JScrollPane rightScrollPane = new JScrollPane(rightPanel);


    mainPanel.add(topScrollPane, BorderLayout.NORTH);
    mainPanel.add(imageScrollPane, BorderLayout.CENTER);
    mainPanel.add(rightScrollPane, BorderLayout.EAST);
    mainPanel.add(leftScrollPane, BorderLayout.WEST);
    add(mainScrollPane);
    this.controller = new ProcessingController(this);
  }

  @Override
  public void renderException(String message) throws IllegalArgumentException {
    if (message == null) {
      throw new IllegalArgumentException("Null Message");
    }
    JOptionPane.showMessageDialog(GraphicalView.this,message,"Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e == null) {
      throw new IllegalArgumentException("Null action");
    }
    switch (e.getActionCommand()) {
      case "Load File":
        final JFileChooser fChooser = new JFileChooser(".");
        fChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retValue = fChooser.showOpenDialog(GraphicalView.this);
        if (retValue == JFileChooser.APPROVE_OPTION) {
          File f = fChooser.getSelectedFile();

          String commandToExecute =
              "create-layered-image " + f.getName() + " " + f.getPath().replaceAll(" ", ">");
          this.controller.runCommands(commandToExecute);
          this.display = controller.getReferenceToImage(f.getName());
          this.currentImageName = f.getName();
          this.imagePanel.setImage(this.display.getImageRepresentation());
          this.updateLayerButtons();
          SwingUtilities.updateComponentTreeUI(mainPanel);
        }
        break;
      default:
        this.handleLayerCommand(e);
    }
  }

  private void handleLayerCommand(ActionEvent e) {
    if (e.getActionCommand().startsWith("Select ")) {
      String commandSecondPart;
      try {
        commandSecondPart = e.getActionCommand().split(" ")[1];
      } catch (IndexOutOfBoundsException exception) {
        return;
      }
      for (String layerName : display.getLayerNames()) {
        if (commandSecondPart.equals(layerName)) {
          this.currentLayerName = layerName;
          this.imagePanel.setImage(this.display.getImageRepresentation());
          SwingUtilities.updateComponentTreeUI(mainPanel);
          return;
        }
      }
    }
    if (e.getActionCommand().startsWith("Move Up ")) {
      String commandSecondPart;
      try {
        commandSecondPart = e.getActionCommand().split(" ")[2];
      } catch (IndexOutOfBoundsException exception) {
        return;
      }
      int counter = 0;
      for (String layerName : display.getLayerNames()) {
        if (commandSecondPart.equals(layerName)) {
          if (counter != 0) {
            String commandToExecute = "move-layer " + this.currentImageName + " " + layerName + " " + (counter - 1);
            this.controller.runCommands(commandToExecute);
            this.updateLayerButtons();
            this.imagePanel.setImage(this.display.getImageRepresentation());
            SwingUtilities.updateComponentTreeUI(mainPanel);
            return;
          }
        }
        counter += 1;
      }
    }
    if (e.getActionCommand().startsWith("Move Down ")) {
      String commandSecondPart;
      try {
        commandSecondPart = e.getActionCommand().split(" ")[2];
      } catch (IndexOutOfBoundsException exception) {
        return;
      }
      int counter = 0;
      for (String layerName : display.getLayerNames()) {
        if (commandSecondPart.equals(layerName)) {
          if (counter != display.getLayerNames().size() - 1) {
            String commandToExecute = "move-layer " + this.currentImageName + " " + layerName + " " + (counter + 1);
            this.controller.runCommands(commandToExecute);
            this.updateLayerButtons();
            this.imagePanel.setImage(this.display.getImageRepresentation());
            SwingUtilities.updateComponentTreeUI(mainPanel);
            return;
          }
        }
        counter += 1;
      }
    }
    if (e.getActionCommand().startsWith("Copy ")) {
      String commandSecondPart;
      try {
        commandSecondPart = e.getActionCommand().split(" ")[1];
      } catch (IndexOutOfBoundsException exception) {
        return;
      }
      for (String layerName : display.getLayerNames()) {
        if (commandSecondPart.equals(layerName)) {
          String newLayer = JOptionPane.showInputDialog("Enter the name of the new layer");
          if (newLayer == null) {
            return;
          }
          String commandToExecute = "copy-layer " + this.currentImageName + " " + newLayer + " " + layerName;
          this.controller.runCommands(commandToExecute);
          this.updateLayerButtons();
          this.imagePanel.setImage(this.display.getImageRepresentation());
          SwingUtilities.updateComponentTreeUI(mainPanel);
          return;
        }
      }
    }

  }

  private void updateLayerButtons() {
    this.layerSelectors.removeAll();
    this.layers = new ButtonGroup();
    this.layerMoveUppers.removeAll();
    this.layerMoveDowners.removeAll();
    this.layerSaveAsImagers.removeAll();
    this.layerCopiers.removeAll();
    for (String layerName : this.display.getLayerNames()) {
      JRadioButton nextLayerSelector = new JRadioButton(layerName);
      nextLayerSelector.setActionCommand("Select " + layerName);
      nextLayerSelector.addActionListener(this);
      layers.add(nextLayerSelector);
      layerSelectors.add(nextLayerSelector);
      JButton nextMoveUpper = new JButton("↑");
      nextMoveUpper.setActionCommand("Move Up " + layerName);
      nextMoveUpper.addActionListener(this);
      layerMoveUppers.add(nextMoveUpper);
      JButton nextMoveDowner = new JButton("↓");
      nextMoveDowner.setActionCommand("Move Down " + layerName);
      nextMoveDowner.addActionListener(this);
      layerMoveDowners.add(nextMoveDowner);
      JButton nextCopier = new JButton("Copy");
      nextCopier.setActionCommand("Copy " + layerName);
      nextCopier.addActionListener(this);
      layerCopiers.add(nextCopier);
      JButton nextSaveAsImager = new JButton("Save");
      nextSaveAsImager.setActionCommand("Save Layer as Image " + layerName);
      nextSaveAsImager.addActionListener(this);
      layerSaveAsImagers.add(nextSaveAsImager);
    }
  }
}

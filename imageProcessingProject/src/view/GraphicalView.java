package view;

import controller.ImageProcessingController;
import controller.ProcessingController;
import imageasgraph.FixedSizeGraph;
import imageasgraph.InputType;
import imageasgraph.Node;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import layeredimage.LayeredImage;
import layeredimage.ViewModel;
import layeredimage.blend.AbstractBlend;

/**
 * Represents a GUI interface that allows a user to perform all supported functionality
 * for editing a layered image, and see a preview of their work as they make changes.
 */
public class GraphicalView extends JFrame implements View, ActionListener {

  private final ImageProcessingController controller;
  private ViewModel display;
  private String currentImageName;
  private String currentLayerName;

  private JScrollPane mainScrollPane;
  private JPanel mainPanel;
  private ImageIcon imagePanel;
  private JPanel layerSelectors;
  private JMenu layerMenu;
  private JMenuItem newLayer;
  private JMenuItem loadLayer;
  private JMenuItem saveCurrentLayer;
  private ButtonGroup layers;
  private JPanel layerMoveUppers;
  private JPanel layerMoveDowners;
  private JPanel layerCopiers;
  private JPanel layerShowers;
  private JPanel layerHiders;
  private JMenu tabs;
  private JMenuBar menuBar;

  /**
   * Constructs a new graphical view - initializes all unchanging JFrame components as well
   * as the overall framework of the GUI, and creates a new controller which references this as it's
   * view and which will actually hold and edit a model.
   */
  public GraphicalView() {
    super();
    setTitle("Image Processing Interactive Interface");
    setSize(750, 750);

    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainScrollPane = new JScrollPane(mainPanel);

    menuBar = new JMenuBar();
    menuBar.setName("Menus");
    JMenu file = new JMenu("File");
    JMenuItem load = new JMenuItem("Load");
    load.setActionCommand("Load File");
    load.addActionListener(this);
    file.add(load);
    JMenuItem save = new JMenuItem("Save");
    save.setActionCommand("Save File");
    save.addActionListener(this);
    file.add(save);
    JMenuItem exportMenuItem = new JMenuItem("Export");
    exportMenuItem.setActionCommand("Export as Image");
    exportMenuItem.addActionListener(this);
    file.add(exportMenuItem);
    JMenuItem executeMenuItem = new JMenuItem("Execute");
    executeMenuItem.setActionCommand("Execute Script");
    executeMenuItem.addActionListener(this);
    file.add(executeMenuItem);
    menuBar.add(file);

    JMenu edit = new JMenu("Edit");
    JMenu applyMutator = new JMenu("Apply Mutator");
    JMenuItem applyBlur = new JMenuItem("Blur");
    applyBlur.setActionCommand("Blur");
    applyBlur.addActionListener(this);
    applyMutator.add(applyBlur);
    JMenuItem applySharpen = new JMenuItem("Sharpen");
    applySharpen.setActionCommand("Sharpen");
    applySharpen.addActionListener(this);
    applyMutator.add(applySharpen);
    JMenuItem applyGreyscale = new JMenuItem("Greyscale");
    applyGreyscale.setActionCommand("Greyscale");
    applyGreyscale.addActionListener(this);
    applyMutator.add(applyGreyscale);
    JMenuItem applySepia = new JMenuItem("Sepia");
    applySepia.setActionCommand("Sepia");
    applySepia.addActionListener(this);
    applyMutator.add(applySepia);
    edit.add(applyMutator);
    menuBar.add(edit);

    layerMenu = new JMenu("Layer");
    newLayer = new JMenuItem("New Layer");
    newLayer.setActionCommand("Add New Layer");
    newLayer.addActionListener(this);
    layerMenu.add(newLayer);
    loadLayer = new JMenuItem("Load Image");
    loadLayer.setActionCommand("Load Image As Layer");
    loadLayer.addActionListener(this);
    layerMenu.add(loadLayer);
    saveCurrentLayer = new JMenuItem("Save Current Layer");
    saveCurrentLayer.setActionCommand("Load Image As Layer");
    saveCurrentLayer.addActionListener(this);
    layerMenu.add(saveCurrentLayer);
    menuBar.add(layerMenu);

    tabs = new JMenu("Current Images");
    menuBar.add(tabs);

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
    JButton saveLayer = new JButton("Save Current");
    saveLayer.setActionCommand("Save Current Layer");
    saveLayer.addActionListener(this);
    addLoadLayer.add(saveLayer);
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
    layerShowers = new JPanel();
    layerShowers.setLayout(new BoxLayout(layerShowers, BoxLayout.PAGE_AXIS));
    layerInterface.add(layerShowers);
    layerHiders = new JPanel();
    layerHiders.setLayout(new BoxLayout(layerHiders, BoxLayout.PAGE_AXIS));
    layerInterface.add(layerHiders);
    rightPanel.add(layerInterface);
    JScrollPane rightScrollPane = new JScrollPane(rightPanel);

    mainPanel.add(imageScrollPane, BorderLayout.CENTER);
    mainPanel.add(rightScrollPane, BorderLayout.EAST);
    mainPanel.add(leftScrollPane, BorderLayout.WEST);
    mainPanel.add(menuBar, BorderLayout.NORTH);
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
      case "Load File": {
        final JFileChooser fChooser = new JFileChooser(".");
        fChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retValue = fChooser.showOpenDialog(GraphicalView.this);
        if (retValue == JFileChooser.APPROVE_OPTION) {
          File f = fChooser.getSelectedFile();

          String commandToExecute =
              "create-layered-image " + f.getName() + " " + f.getPath().replaceAll(" ", ">");
          this.controller.runCommands(commandToExecute);
          try {
            this.display = controller.getReferenceToImage(f.getName());
          } catch (IllegalArgumentException exception) {
            this.renderException(exception.getMessage());
          }
          this.currentImageName = f.getName();
          this.currentLayerName = null;
          this.imagePanel.setImage(this.display.getImageRepresentation());
          this.updateLayerButtons();
          this.updateTabs();
          SwingUtilities.updateComponentTreeUI(mainPanel);
        }
        break;
      }
      case "Blur":
        if (!(this.currentImageName == null || this.currentLayerName == null)) {
          String commandToExecute = "apply-mutator blur " + this.currentImageName + " " + this.currentLayerName;
          this.controller.runCommands(commandToExecute);
          this.imagePanel.setImage(this.display.getImageRepresentation());
          SwingUtilities.updateComponentTreeUI(mainPanel);
        }
        break;
      case "Sharpen":
        if (!(this.currentImageName == null || this.currentLayerName == null)) {
          String commandToExecute = "apply-mutator sharpen " + this.currentImageName + " " + this.currentLayerName;
          this.controller.runCommands(commandToExecute);
          this.imagePanel.setImage(this.display.getImageRepresentation());
          SwingUtilities.updateComponentTreeUI(mainPanel);
        }
        break;
      case "Greyscale":
        if (!(this.currentImageName == null || this.currentLayerName == null)) {
          String commandToExecute = "apply-mutator greyscale " + this.currentImageName + " " + this.currentLayerName;
          this.controller.runCommands(commandToExecute);
          this.imagePanel.setImage(this.display.getImageRepresentation());
          SwingUtilities.updateComponentTreeUI(mainPanel);
        }
        break;
      case "Sepia":
        if (!(this.currentImageName == null || this.currentLayerName == null)) {
          String commandToExecute = "apply-mutator sepia " + this.currentImageName + " " + this.currentLayerName;
          this.controller.runCommands(commandToExecute);
          this.imagePanel.setImage(this.display.getImageRepresentation());
          SwingUtilities.updateComponentTreeUI(mainPanel);
        }
        break;
      case "Save Current Layer": {
        if (!(this.currentImageName == null || this.currentLayerName == null)) {
          final JFileChooser fChooser = new JFileChooser(".");
          int retValue = fChooser.showSaveDialog(GraphicalView.this);
          if (retValue == JFileChooser.APPROVE_OPTION) {
            File f = fChooser.getSelectedFile();

            String commandToExecute =
                "save " + this.currentImageName + " " + this.currentLayerName + " "
                    + f.getName().substring(f.getName().lastIndexOf(".") + 1) + " "
                    + f.getPath().replaceAll(" ", ">").substring(0, f.getPath().lastIndexOf(".") + 1);
            this.controller.runCommands(commandToExecute);
            this.updateLayerButtons();
            SwingUtilities.updateComponentTreeUI(mainPanel);
          }
        }
        break;
      }
      case "Add New Layer": {
        String newLayer = JOptionPane.showInputDialog("Enter the name of the new layer");
        String commandToExecute = "add-layer " + this.currentImageName + " " + newLayer;
        this.controller.runCommands(commandToExecute);
        this.updateLayerButtons();
        SwingUtilities.updateComponentTreeUI(mainPanel);
        break;
      }
      case "Load Image As Layer": {
        final JFileChooser fChooser = new JFileChooser(".");
        //NOTE: NO FILTER AS VIEW SHOULD NOT BE COUPLED TO WHAT TYPES OF FILES ARE SUPPORTED.
        int retValue = fChooser.showOpenDialog(GraphicalView.this);
        if (retValue == JFileChooser.APPROVE_OPTION) {
          File f = fChooser.getSelectedFile();
          String newLayer = JOptionPane.showInputDialog("Enter the name of the new layer");
          String commandToExecute =
              "add-image-as-layer " + this.currentImageName + " " + newLayer + " "
                  + f.getPath().replaceAll(" ", ">");
          this.controller.runCommands(commandToExecute);
          this.imagePanel.setImage(this.display.getImageRepresentation());
          this.updateLayerButtons();
          SwingUtilities.updateComponentTreeUI(mainPanel);
        }
        break;
      }
      case "Save File": {
        if (!(this.currentImageName == null)) {
          final JFileChooser fChooser = new JFileChooser(".");
          int retValue = fChooser.showSaveDialog(GraphicalView.this);
          if (retValue == JFileChooser.APPROVE_OPTION) {
            File f = fChooser.getSelectedFile();

            String commandToExecute =
                "save-layered " + this.currentImageName + " " + f.getPath().replaceAll(" ", ">");
            this.controller.runCommands(commandToExecute);
            this.updateLayerButtons();
            SwingUtilities.updateComponentTreeUI(mainPanel);
          }
        }
        break;
      }
      case "Export as Image": {
        if (!(this.currentImageName == null)) {
          String[] allBlendTypes = AbstractBlend.getBlendTypes();
          String blendType = allBlendTypes[JOptionPane.showOptionDialog(this, "Choose Blend Type",
              "Blend Choices", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, allBlendTypes, allBlendTypes[0])];
          final JFileChooser fChooser = new JFileChooser(".");
          int retValue = fChooser.showSaveDialog(GraphicalView.this);
          if (retValue == JFileChooser.APPROVE_OPTION) {
            File f = fChooser.getSelectedFile();

            String commandToExecute =
                "save-as-image " + this.currentImageName + " "
                    + blendType + " "
                    + f.getName().substring(f.getName().lastIndexOf(".") + 1) + " "
                    + f.getPath().replaceAll(" ", ">").substring(0, f.getPath().lastIndexOf(".") + 1);
            this.controller.runCommands(commandToExecute);
            this.updateLayerButtons();
            SwingUtilities.updateComponentTreeUI(mainPanel);
          }
        }
        break;
      }
      case "Execute Script": {
        final JFileChooser fChooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fChooser.setFileFilter(filter);
        int retValue = fChooser.showOpenDialog(GraphicalView.this);
        if (retValue == JFileChooser.APPROVE_OPTION) {
          File f = fChooser.getSelectedFile();
          FileReader newRead;
          try {
            newRead = new FileReader(f);
          } catch (IOException fileError) {
            this.renderException(fileError.getMessage());
            return;
          }
          Scanner scanner = new Scanner(newRead);
          StringBuilder commandToExecute = new StringBuilder();
          while (scanner.hasNext()) {
            commandToExecute.append(scanner.nextLine()).append("\n");
          }

          this.controller.runCommands(commandToExecute.toString());
          this.imagePanel.setImage(this.display.getImageRepresentation());
          this.updateLayerButtons();
          this.updateTabs();
          SwingUtilities.updateComponentTreeUI(mainPanel);
        }
        break;
      }
      default:
        this.handleLayerCommand(e);
        this.handleTabCommand(e);
    }
  }

  /**
   * Given an action event, will process it as a dynamic tab-selecting action: if it begins with
   * "Change Tab ", and then the name of a image currently existing within the model, this will set
   * the image currently being edited to that image and refresh layer buttons and menu items.
   * @param e The action event to be handled
   */
  private void handleTabCommand(ActionEvent e) {
    if (e.getActionCommand().startsWith("Change Tab ")) {
      String commandSecondPart;
      try {
        commandSecondPart = e.getActionCommand().split(" ")[2];
      } catch (IndexOutOfBoundsException exception) {
        return;
      }
      for (String imageName : this.controller.getLayeredImageNames()) {
        if (commandSecondPart.equals(imageName)) {
          this.currentImageName = imageName;
          try {
            this.display = controller.getReferenceToImage(imageName);
          } catch (IllegalArgumentException exception) {
            this.renderException(exception.getMessage());
          }
          this.currentLayerName = null;
          this.imagePanel.setImage(this.display.getImageRepresentation());
          this.updateLayerButtons();
          SwingUtilities.updateComponentTreeUI(mainPanel);
          return;
        }
      }
    }
  }

  /**
   * Given an action event, will process it as a dynamic method that does something to a currently
   * existing layer. Currently handles selecting layers, moving them up or down, copying a particular
   * layer, and showing or hiding individual layers.
   * @param e The action event to be processed
   */
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
    if (e.getActionCommand().startsWith("Show ")) {
      String commandSecondPart;
      try {
        commandSecondPart = e.getActionCommand().split(" ")[1];
      } catch (IndexOutOfBoundsException exception) {
        return;
      }
      for (String layerName : display.getLayerNames()) {
        if (commandSecondPart.equals(layerName)) {
          String commandToExecute = "update-visibility " + this.currentImageName + " " + layerName + " true";
          this.controller.runCommands(commandToExecute);
          this.updateLayerButtons();
          this.imagePanel.setImage(this.display.getImageRepresentation());
          SwingUtilities.updateComponentTreeUI(mainPanel);
          return;
        }
      }
    }
    if (e.getActionCommand().startsWith("Hide ")) {
      String commandSecondPart;
      try {
        commandSecondPart = e.getActionCommand().split(" ")[1];
      } catch (IndexOutOfBoundsException exception) {
        return;
      }
      for (String layerName : display.getLayerNames()) {
        if (commandSecondPart.equals(layerName)) {
          String commandToExecute = "update-visibility " + this.currentImageName + " " + layerName + " false";
          this.controller.runCommands(commandToExecute);
          this.updateLayerButtons();
          this.imagePanel.setImage(this.display.getImageRepresentation());
          SwingUtilities.updateComponentTreeUI(mainPanel);
          return;
        }
      }
    }
  }

  /**
   * Refreshes the currently available layer menu buttons and freestanding buttons, so that they
   * match the currently existing layers of the currently displaying image.
   */
  private void updateLayerButtons() {
    this.layerSelectors.removeAll();
    this.layers = new ButtonGroup();
    this.layerMoveUppers.removeAll();
    this.layerMoveDowners.removeAll();
    this.layerShowers.removeAll();
    this.layerHiders.removeAll();
    this.layerCopiers.removeAll();
    this.layerMenu.removeAll();
    layerMenu.add(newLayer);
    layerMenu.add(loadLayer);
    layerMenu.add(saveCurrentLayer);
    for (String layerName : this.display.getLayerNames()) {
      JMenu nextLayerMenu = new JMenu(layerName);
      JMenuItem nextSetAsCurrent = new JMenuItem("Set as current");
      nextSetAsCurrent.setActionCommand("Select " + layerName);
      nextSetAsCurrent.addActionListener(this);
      nextLayerMenu.add(nextSetAsCurrent);
      JMenuItem nextMoveUp = new JMenuItem("Move Up");
      nextMoveUp.setActionCommand("Move Up " + layerName);
      nextMoveUp.addActionListener(this);
      nextLayerMenu.add(nextMoveUp);
      JMenuItem nextMoveDown = new JMenuItem("Move Down");
      nextMoveDown.setActionCommand("Move Down " + layerName);
      nextMoveDown.addActionListener(this);
      nextLayerMenu.add(nextMoveDown);
      JMenuItem nextCopy = new JMenuItem("Copy");
      nextCopy.setActionCommand("Copy " + layerName);
      nextCopy.addActionListener(this);
      nextLayerMenu.add(nextCopy);
      JMenuItem nextShow = new JMenuItem("Show");
      nextShow.setActionCommand("Show " + layerName);
      nextShow.addActionListener(this);
      nextLayerMenu.add(nextShow);
      JMenuItem nextHide = new JMenuItem("Hide");
      nextHide.setActionCommand("Hide " + layerName);
      nextHide.addActionListener(this);
      nextLayerMenu.add(nextHide);
      layerMenu.add(nextLayerMenu);

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
      JButton nextShower = new JButton("Show");
      nextShower.setActionCommand("Show " + layerName);
      nextShower.addActionListener(this);
      layerShowers.add(nextShower);
      JButton nextHider = new JButton("Hide");
      nextHider.setActionCommand("Hide " + layerName);
      nextHider.addActionListener(this);
      layerHiders.add(nextHider);
    }
  }

  /**
   * Updates the image selection buttons so that they accurately represent the existing
   * models within the controller.
   */
  private void updateTabs() {
    this.tabs.removeAll();
    for (String imageName : this.controller.getLayeredImageNames()) {
      JMenuItem nextName = new JMenuItem(imageName);
      nextName.setActionCommand("Change Tab " + imageName);
      nextName.addActionListener(this);
      tabs.add(nextName);
    }
  }

  public JMenu getFileMenu() {
    return this.menuBar.getMenu(0);
  }
}

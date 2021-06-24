import static org.junit.Assert.assertEquals;
import controller.ImageProcessingController;
import imageasgraph.FixedSizeGraph;
import imageasgraph.Node;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import view.GraphicalView;
import view.View;

/**
 * For testing dynamically produced actions from the GraphicalView buttons that correspond to
 * layers.
 */
public class TestLayerTabActions {

  private JMenu getFileMenu(GraphicalView fromView) {
    return this.getMenu(fromView, 0);
  }

  private JMenu getMenu(GraphicalView fromView, int index) {
    return ((JMenuBar)(((JPanel)(((JScrollPane)(fromView.getRootPane().getContentPane().getComponent(0))).getViewport().getComponent(0))).getComponent(3))).getMenu(index);
  }

  /**
   * Given a controller and the name of an image contained by that controller, saves the image
   * and then loads it as a layeredImage so everything that is necessary for testing can be
   * reached.
   * @param toSaveFrom The view from which to save the image
   * @param toSaveAs The name of the image to be saved as
   * @return The LayeredImage representation of the image to be loaded
   * @throws IllegalArgumentException If either input is null, or if there is an issue fetching the image
   */
  private LayeredImage getViewableForTesting(GraphicalView toSaveFrom, String toSaveAs) throws IllegalArgumentException, AWTException {
    if (toSaveFrom == null) {
      throw new IllegalArgumentException("Null input");
    }
    try{Thread.sleep(500);}catch(InterruptedException e) {};
    this.openSaveFileRobot(toSaveFrom, toSaveAs, false);
    try{Thread.sleep(500);}catch(InterruptedException e) {};
    return new LayeredImageV0(toSaveAs);
  }

  /**
   * Creates a Robot that automates clicking and loading/saving files.
   * @param toOpenOn View to open files on
   * @param fileToOpen File to open up
   * @param isOpen If opening versus saving
   * @throws AWTException Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void openSaveFileRobot(GraphicalView toOpenOn, String fileToOpen, boolean isOpen) throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToOpen == null) {
      throw new IllegalArgumentException("Null input");
    }
    try{Thread.sleep(300);}catch(InterruptedException e) {};
    toOpenOn.setVisible(true);
    JMenu fileMenu = this.getFileMenu(toOpenOn);
    Point p = fileMenu.getLocationOnScreen();
    Robot fileOpener = new Robot();
    fileOpener.mouseMove(p.x + fileMenu.getWidth() / 2, p.y + fileMenu.getHeight() / 2);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    Point p2;
    if (isOpen) {
      p2 = fileMenu.getItem(0).getLocationOnScreen();
    } else {
      p2 = fileMenu.getItem(1).getLocationOnScreen();
    }
    fileOpener.mouseMove(p2.x + fileMenu.getItem(0).getWidth() / 2, p2.y + fileMenu.getItem(0).getHeight() / 2);
    fileOpener.delay(500);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(500);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(500);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.mouseMove(300, 460);
    fileOpener.delay(500);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    for (Character c : fileToOpen.toCharArray()) {
      fileOpener.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
      fileOpener.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
    }
    fileOpener.delay(100);
    fileOpener.mouseMove(500, 515);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    try{Thread.sleep(600);}catch(InterruptedException e) {};
  }

  private void cloneSecondLayerRobot(GraphicalView toCloneIn, String nameForClone)
      throws AWTException {
    try{Thread.sleep(300);}catch(InterruptedException e) {};
    toCloneIn.setVisible(true);
    Robot cloner = new Robot();
    cloner.mouseMove(575, 125);
    cloner.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    cloner.delay(100);
    cloner.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    cloner.delay(100);
    cloner.mouseMove(900, 525);
    cloner.delay(100);
    cloner.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    cloner.delay(100);
    cloner.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    cloner.delay(100);
    for (Character c : nameForClone.toCharArray()) {
      cloner.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
      cloner.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
    }
    cloner.delay(100);
    cloner.mouseMove(900, 550);
    cloner.delay(100);
    cloner.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    cloner.delay(100);
    cloner.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    try{Thread.sleep(600);}catch(InterruptedException e) {};
  }

  @Test
  public void testMoveUp() throws AWTException {
    GraphicalView forTesting = new GraphicalView();
    try {
      this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
    } catch(AWTException e) {
    }
    LayeredImage currentState = this.getViewableForTesting(forTesting, "outputImages/moveupdelete0");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")), currentState.getLayerNames());
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Move Up red-layer"));
    assertEquals(currentState.getLayer(2), currentState.getLayer("red-layer"));
    currentState = this.getViewableForTesting(forTesting, "outputImages/moveupdelete1");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "red-layer", "blue-layer")), currentState.getLayerNames());
    assertEquals(currentState.getLayer(1), currentState.getLayer("red-layer"));
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Move Up invisible-layer"));
    currentState = this.getViewableForTesting(forTesting, "outputImages/moveupdelete2");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "red-layer", "blue-layer")), currentState.getLayerNames());
    assertEquals(currentState.getLayer(0), currentState.getLayer("invisible-layer"));
    forTesting.setVisible(false);
  }

  @Test
  public void testMoveDown() throws AWTException {
    GraphicalView forTesting = new GraphicalView();
    try {
      this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
    } catch(AWTException e) {
    }
    LayeredImage currentState = this.getViewableForTesting(forTesting, "outputImages/movedowndelete0");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")), currentState.getLayerNames());
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Move Down invisible-layer"));
    assertEquals(currentState.getLayer(0), currentState.getLayer("invisible-layer"));
    currentState = this.getViewableForTesting(forTesting, "outputImages/movedowndelete1");
    assertEquals(new ArrayList<String>(Arrays.asList("blue-layer", "invisible-layer", "red-layer")), currentState.getLayerNames());
    assertEquals(currentState.getLayer(1), currentState.getLayer("invisible-layer"));
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Move Down red-layer"));
    currentState = this.getViewableForTesting(forTesting, "outputImages/movedowndelete2");
    assertEquals(new ArrayList<String>(Arrays.asList("blue-layer", "invisible-layer", "red-layer")), currentState.getLayerNames());
    assertEquals(currentState.getLayer(2), currentState.getLayer("red-layer"));
    forTesting.setVisible(false);
  }

  @Test
  public void testShowLayer() throws AWTException {
    GraphicalView forTesting = new GraphicalView();
    try {
      this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
    } catch(AWTException e) {
    }
    LayeredImage currentState = this.getViewableForTesting(forTesting, "outputImages/showdelete0");
    assertEquals(false, currentState.getVisibility("invisible-layer"));
    assertEquals(true, currentState.getVisibility("red-layer"));
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Show invisible-layer"));
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Show red-layer"));
    currentState = this.getViewableForTesting(forTesting, "outputImages/showdelete1");
    assertEquals(true, currentState.getVisibility("invisible-layer"));
    assertEquals(true, currentState.getVisibility("red-layer"));
    forTesting.setVisible(false);
  }

  @Test
  public void testHideLayer() throws AWTException {
    GraphicalView forTesting = new GraphicalView();
    try {
      this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
    } catch(AWTException e) {
    }
    LayeredImage currentState = this.getViewableForTesting(forTesting, "outputImages/hidedelete0");
    assertEquals(false, currentState.getVisibility("invisible-layer"));
    assertEquals(true, currentState.getVisibility("red-layer"));
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Hide invisible-layer"));
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Hide red-layer"));
    currentState = this.getViewableForTesting(forTesting, "outputImages/hidedelete1");
    assertEquals(false, currentState.getVisibility("invisible-layer"));
    assertEquals(false, currentState.getVisibility("red-layer"));
    forTesting.setVisible(false);
  }

  @Test
  public void testCopyLayer() throws AWTException {
    GraphicalView forTesting = new GraphicalView();
    try {
      this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
    } catch(AWTException e) {
    }
    LayeredImage currentState = this.getViewableForTesting(forTesting, "outputImages/copydelete0");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")), currentState.getLayerNames());
    assertEquals(3, currentState.getNumLayers());
    this.cloneSecondLayerRobot(forTesting, "blue-layer-clone");
    currentState = this.getViewableForTesting(forTesting, "outputImages/copydelete1");
    assertEquals(new ArrayList<String>(Arrays.asList("blue-layer-clone", "invisible-layer", "blue-layer", "red-layer")), currentState.getLayerNames());
    assertEquals(4, currentState.getNumLayers());
    FixedSizeGraph individual = currentState.getLayer("blue-layer-clone");
    for (Node n : individual) {
      assertEquals(255, n.getOpacity());
      assertEquals(0, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(255, n.getBlue());
    }
    this.cloneSecondLayerRobot(forTesting, "invisible-layer-clone");
    currentState = this.getViewableForTesting(forTesting, "outputImages/copydelete2");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer-clone", "blue-layer-clone", "invisible-layer", "blue-layer", "red-layer")), currentState.getLayerNames());
    assertEquals(5, currentState.getNumLayers());
    individual = currentState.getLayer("invisible-layer-clone");
    for (Node n : individual) {
      assertEquals(0, n.getOpacity());
      assertEquals(0, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
    }
  }

  @Test
  public void testTabActions() throws AWTException {
    GraphicalView forTesting = new GraphicalView();
    try {
      this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
      this.openSaveFileRobot(forTesting, "outputImages/misc", true);
    } catch(AWTException e) {
    }
    LayeredImage currentState = this.getViewableForTesting(forTesting, "outputImages/tabdelete0");
    assertEquals(new ArrayList<String>(Arrays.asList("galaxy", "rainbow", "birb")), currentState.getLayerNames());
    assertEquals(1024, currentState.getWidth());
    assertEquals(768, currentState.getHeight());
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Change Tab examplelayeredimage"));
    currentState = this.getViewableForTesting(forTesting, "outputImages/tabdelete1");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")), currentState.getLayerNames());
    assertEquals(20, currentState.getWidth());
    assertEquals(20, currentState.getHeight());
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Change Tab examplelayeredimage"));
    currentState = this.getViewableForTesting(forTesting, "outputImages/tabdelete2");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")), currentState.getLayerNames());
    assertEquals(20, currentState.getWidth());
    assertEquals(20, currentState.getHeight());
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Change Tab misc"));
    currentState = this.getViewableForTesting(forTesting, "outputImages/tabdelete3");
    assertEquals(new ArrayList<String>(Arrays.asList("galaxy", "rainbow", "birb")), currentState.getLayerNames());
    assertEquals(1024, currentState.getWidth());
    assertEquals(768, currentState.getHeight());
    forTesting.setVisible(false);
  }

}

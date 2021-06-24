import static org.junit.Assert.assertEquals;
import controller.ImageProcessingController;
import java.awt.AWTException;
import java.awt.Component;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
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
    JMenu fileMenu = toOpenOn.getFileMenu();
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

  @Test
  public void testMoveUp() throws AWTException {
    //Nonexistent layer
    GraphicalView forTesting = new GraphicalView();
    try {
      this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
    } catch(AWTException e) {
    }
    LayeredImage currentState = this.getViewableForTesting(forTesting, "currentstate0");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")), currentState.getLayerNames());
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Move Up red-layer"));
    assertEquals(currentState.getLayer(2), currentState.getLayer("red-layer"));
    currentState = this.getViewableForTesting(forTesting, "currentstate1");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "red-layer", "blue-layer")), currentState.getLayerNames());
    assertEquals(currentState.getLayer(1), currentState.getLayer("red-layer"));
    forTesting.setVisible(false);
  }

  @Test
  public void testMoveDown() throws AWTException {
    //Nonexistent layer
    GraphicalView forTesting = new GraphicalView();
    try {
      this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
    } catch(AWTException e) {
    }
    LayeredImage currentState = this.getViewableForTesting(forTesting, "currentstate2");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")), currentState.getLayerNames());
    forTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Move Down invisible-layer"));
    assertEquals(currentState.getLayer(0), currentState.getLayer("invisible-layer"));
    currentState = this.getViewableForTesting(forTesting, "currentstate3");
    assertEquals(new ArrayList<String>(Arrays.asList("blue-layer", "invisible-layer", "red-layer")), currentState.getLayerNames());
    assertEquals(currentState.getLayer(1), currentState.getLayer("invisible-layer"));
  }

}

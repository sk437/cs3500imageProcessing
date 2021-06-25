import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import controller.ImageProcessingController;
import controller.ProcessingController;
import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.Node;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import view.GraphicalView;
import view.View;

/**
 * Tests methods and relevant view options for a graphical GUI view. NOTE: The showView method
 * relies on inputs from System.in, but as this can not be * simulated, we instead test the outputs
 * and the inputs are handled in the controller tests, so * refer to other tests (such as those for
 * input/output behavior tested in the PreviousTesting.TestCommand and * TestLanguageSyntax
 * classes). NOTE 2: Render Exception creates a pop-up here, so this can not be tested. As long as
 * the message given is not null, it is displayed.
 */
public class TestGraphicalView {

  private JMenu getFileMenu(GraphicalView fromView) {
    return this.getMenu(fromView, 0);
  }

  private JMenu getMenu(GraphicalView fromView, int index) {
    return ((JMenuBar) (((JPanel) (((JScrollPane) (fromView.getRootPane().getContentPane()
        .getComponent(0))).getViewport().getComponent(0))).getComponent(3))).getMenu(index);
  }

  private final GraphicalView test = new GraphicalView();

  /**
   * Given a controller and the name of an image contained by that controller, saves the image and
   * then loads it as a layeredImage so everything that is necessary for testing can be reached.
   *
   * @param toSaveFrom The view from which to save the image
   * @param toSaveAs   The name of the image to be saved as
   * @return The LayeredImage representation of the image to be loaded
   * @throws IllegalArgumentException If either input is null, or if there is an issue fetching the
   *                                  image
   */
  private LayeredImage getViewableForTesting(GraphicalView toSaveFrom, String toSaveAs)
      throws IllegalArgumentException, AWTException {
    if (toSaveFrom == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      Thread.sleep(500);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    this.openSaveFileRobot(toSaveFrom, toSaveAs, false);
    try {
      Thread.sleep(500);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    return new LayeredImageV0(toSaveAs);
  }

  /**
   * Given a controller and the name of an image contained by that controller, saves an image and
   * then loads it as a GraphOfPixels so everything that is necessary for testing can be reached.
   *
   * @param toSaveFrom The view from which to save the image
   * @param toSaveAs   The name of the image to be saved as
   * @return The LayeredImage representation of the image to be loaded
   * @throws IllegalArgumentException If either input is null, or if there is an issue fetching the
   *                                  image
   */
  private GraphOfPixels getViewableSingleImageForTesting(GraphicalView toSaveFrom, String toSaveAs)
      throws IllegalArgumentException, AWTException {
    if (toSaveFrom == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      Thread.sleep(500);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    this.saveFileSingleImageRobot(toSaveFrom, toSaveAs);
    try {
      Thread.sleep(500);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    return ImageToGraphConverter.convertImage(toSaveAs);
  }

  /**
   * Given a controller and the name of an image contained by that controller, saves a layered image
   * as a single image and then loads it as a GraphOfPixels so everything that is necessary for
   * testing can be reached.
   *
   * @param toSaveFrom The view from which to save the image
   * @param toSaveAs   The name of the image to be saved as
   * @return The LayeredImage representation of the image to be loaded
   * @throws IllegalArgumentException If either input is null, or if there is an issue fetching the
   *                                  image
   */
  private GraphOfPixels getViewableSingleLayeredForTesting(GraphicalView toSaveFrom,
      String toSaveAs) throws IllegalArgumentException, AWTException {
    if (toSaveFrom == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      Thread.sleep(500);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    this.saveLayeredAsImageRobot(toSaveFrom, toSaveAs);
    try {
      Thread.sleep(500);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    return ImageToGraphConverter.convertImage(toSaveAs);
  }

  /**
   * Creates a Robot that automates clicking and loading/saving files.
   *
   * @param toOpenOn   View to open files on
   * @param fileToOpen File to open up
   * @param isOpen     If opening versus saving
   * @throws AWTException             Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void openSaveFileRobot(GraphicalView toOpenOn, String fileToOpen, boolean isOpen)
      throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToOpen == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      Thread.sleep(300);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    toOpenOn.setVisible(true);
    Robot fileOpener = new Robot();
    JMenu fileMenu = this.getFileMenu(toOpenOn);
    Point p = fileMenu.getLocationOnScreen();
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
    fileOpener.mouseMove(p2.x + fileMenu.getItem(0).getWidth() / 2,
        p2.y + fileMenu.getItem(0).getHeight() / 2);
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
    try {
      Thread.sleep(600);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

  }

  /**
   * Creates a Robot that automates saving layers as images.
   *
   * @param toOpenOn   View to open files on
   * @param fileToSave File to save
   * @throws AWTException             Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void saveFileSingleImageRobot(GraphicalView toOpenOn, String fileToSave)
      throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToSave == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      Thread.sleep(300);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    toOpenOn.setVisible(true);
    Robot fileOpener = new Robot();
    JMenu fileMenu = this.getFileMenu(toOpenOn);
    JMenu editMenu = this.getMenu(toOpenOn, 1);
    JMenu layerMenu = this.getMenu(toOpenOn, 2);
    Point p = fileMenu.getLocationOnScreen();
    int moveRight = p.x + fileMenu.getWidth() + editMenu.getWidth() + layerMenu.getWidth() / 2;
    fileOpener.mouseMove(moveRight, p.y + layerMenu.getHeight() / 2);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    Point p2 = layerMenu.getItem(2).getLocationOnScreen();
    fileOpener.mouseMove(p2.x + layerMenu.getItem(0).getWidth() / 2,
        p2.y + layerMenu.getItem(0).getHeight() / 2);
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
    for (Character c : fileToSave.toCharArray()) {
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
    try {
      Thread.sleep(600);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

  }

  /**
   * Creates a Robot that automates opening a file and adding a layer.
   *
   * @param toOpenOn   View to open files on
   * @param fileToOpen File to open
   * @throws AWTException             Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void openFileInputNameRobot(GraphicalView toOpenOn, String fileToOpen, String layerToAdd)
      throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToOpen == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      Thread.sleep(300);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    toOpenOn.setVisible(true);
    Robot fileOpener = new Robot();
    JMenu fileMenu = this.getFileMenu(toOpenOn);
    JMenu editMenu = this.getMenu(toOpenOn, 1);
    JMenu layerMenu = this.getMenu(toOpenOn, 2);
    Point p = fileMenu.getLocationOnScreen();
    int moveRight = p.x + fileMenu.getWidth() + editMenu.getWidth() + layerMenu.getWidth() / 2;
    fileOpener.mouseMove(moveRight, p.y + layerMenu.getHeight() / 2);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    Point p2 = layerMenu.getItem(1).getLocationOnScreen();
    fileOpener.mouseMove(p2.x + layerMenu.getItem(0).getWidth() / 2,
        p2.y + layerMenu.getItem(0).getHeight() / 2);
    fileOpener.delay(500);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(500);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(500);

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

    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.mouseMove(900, 525);
    fileOpener.delay(500);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    for (Character c : layerToAdd.toCharArray()) {
      fileOpener.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
      fileOpener.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
    }
    fileOpener.delay(100);
    fileOpener.mouseMove(900, 550);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(600);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

  }

  /**
   * Creates a Robot that automates opening a file and adding a layer.
   *
   * @param toOpenOn   View to open files on
   * @param fileToOpen File to open
   * @throws AWTException             Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void openFileAddLayerRobot(GraphicalView toOpenOn, String fileToOpen, String layerToAdd)
      throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToOpen == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      Thread.sleep(300);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    toOpenOn.setVisible(true);
    Robot fileOpener = new Robot();
    JMenu fileMenu = this.getFileMenu(toOpenOn);
    JMenu editMenu = this.getMenu(toOpenOn, 1);
    JMenu layerMenu = this.getMenu(toOpenOn, 2);
    Point p = fileMenu.getLocationOnScreen();
    int moveRight = p.x + fileMenu.getWidth() + editMenu.getWidth() + layerMenu.getWidth() / 2;
    fileOpener.mouseMove(moveRight, p.y + layerMenu.getHeight() / 2);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    Point p2 = layerMenu.getItem(0).getLocationOnScreen();
    fileOpener.mouseMove(p2.x + layerMenu.getItem(0).getWidth() / 2,
        p2.y + layerMenu.getItem(0).getHeight() / 2);
    fileOpener.delay(500);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(500);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(500);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.mouseMove(900, 525);
    fileOpener.delay(500);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    for (Character c : layerToAdd.toCharArray()) {
      fileOpener.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
      fileOpener.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
    }
    fileOpener.delay(100);
    fileOpener.mouseMove(900, 550);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(600);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

  }


  /**
   * Creates a Robot that automates saving layers as images.
   *
   * @param toOpenOn   View to open files on
   * @param fileToSave File to save
   * @throws AWTException             Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void saveLayeredAsImageRobot(GraphicalView toOpenOn, String fileToSave)
      throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToSave == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      Thread.sleep(300);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    toOpenOn.setVisible(true);
    Robot fileOpener = new Robot();
    JMenu fileMenu = this.getFileMenu(toOpenOn);
    Point p = fileMenu.getLocationOnScreen();
    fileOpener.mouseMove(p.x + fileMenu.getWidth() / 2, p.y + fileMenu.getHeight() / 2);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    Point p2 = fileMenu.getItem(2).getLocationOnScreen();
    fileOpener.mouseMove(p2.x + fileMenu.getItem(0).getWidth() / 2,
        p2.y + fileMenu.getItem(0).getHeight() / 2);
    fileOpener.delay(500);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(500);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(500);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    fileOpener.mouseMove(400, 400);
    fileOpener.delay(500);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);

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
    for (Character c : fileToSave.toCharArray()) {
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
    try {
      Thread.sleep(600);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

  }


  /**
   * Creates a Robot that automates saving layers as images.
   *
   * @param toOpenOn   View to open files on
   * @param fileToOpen File to open
   * @throws AWTException             Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void executeScriptRobot(GraphicalView toOpenOn, String fileToOpen)
      throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToOpen == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      Thread.sleep(300);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    toOpenOn.setVisible(true);
    Robot fileOpener = new Robot();
    JMenu fileMenu = this.getFileMenu(toOpenOn);
    Point p = fileMenu.getLocationOnScreen();
    fileOpener.mouseMove(p.x + fileMenu.getWidth() / 2, p.y + fileMenu.getHeight() / 2);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    Point p2 = fileMenu.getItem(3).getLocationOnScreen();
    fileOpener.mouseMove(p2.x + fileMenu.getItem(0).getWidth() / 2,
        p2.y + fileMenu.getItem(0).getHeight() / 2);
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
    try {
      Thread.sleep(600);
    } catch (InterruptedException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailRenderExceptionNull() {
    test.renderException(null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailActionPerformedNull() {
    test.actionPerformed(null);
  }


  @Test
  public void testLoadAction() throws AWTException {
    ActionEvent testLoad = new ActionEvent(test, 0, "Load File");

    try {
      this.openSaveFileRobot(test, "outputImages/exampleLayeredImage", true);
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }
    LayeredImage currentState = this.getViewableForTesting(test, "delete0");
    assertEquals(20, currentState.getWidth());
    assertEquals(20, currentState.getHeight());
    assertEquals(3, currentState.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
        currentState.getLayerNames());
  }


  @Test
  public void testBlurAction() throws AWTException {
    ActionEvent testBlur = new ActionEvent(test, 0, "Blur");

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }
    test.actionPerformed(new ActionEvent(test, 0, "Select secondLayer"));
    test.actionPerformed(testBlur);
    LayeredImage currentState = this.getViewableForTesting(test, "delete1");
    assertEquals(1, currentState.getWidth());
    assertEquals(1, currentState.getHeight());
    assertEquals(2, currentState.getNumLayers());
    assertEquals(28, currentState.getLayer(1).getPixelAt(0, 0).getRed());
    assertEquals(28, currentState.getLayer(1).getPixelAt(0, 0).getGreen());
    assertEquals(28, currentState.getLayer(1).getPixelAt(0, 0).getBlue());
    assertEquals(new ArrayList<String>(Arrays.asList("firstLayer", "secondLayer")),
        currentState.getLayerNames());
  }


  @Test
  public void testSharpenAction() throws AWTException {
    ActionEvent testSharpen = new ActionEvent(test, 0, "Sharpen");

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }
    test.actionPerformed(new ActionEvent(test, 0, "Select secondLayer"));
    test.actionPerformed(testSharpen);
    LayeredImage currentState = this.getViewableForTesting(test, "delete2");
    assertEquals(1, currentState.getWidth());
    assertEquals(1, currentState.getHeight());
    assertEquals(2, currentState.getNumLayers());
    assertEquals(111, currentState.getLayer(1).getPixelAt(0, 0).getRed());
    assertEquals(111, currentState.getLayer(1).getPixelAt(0, 0).getGreen());
    assertEquals(111, currentState.getLayer(1).getPixelAt(0, 0).getBlue());
    assertEquals(new ArrayList<String>(Arrays.asList("firstLayer", "secondLayer")),
        currentState.getLayerNames());
  }

  @Test
  public void testSepiaAction() throws AWTException {
    ActionEvent testSepia = new ActionEvent(test, 0, "Sepia");

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }
    test.actionPerformed(new ActionEvent(test, 0, "Select secondLayer"));
    test.actionPerformed(testSepia);
    LayeredImage currentState = this.getViewableForTesting(test, "delete3");
    assertEquals(1, currentState.getWidth());
    assertEquals(1, currentState.getHeight());
    assertEquals(2, currentState.getNumLayers());
    assertEquals(150, currentState.getLayer(1).getPixelAt(0, 0).getRed());
    assertEquals(134, currentState.getLayer(1).getPixelAt(0, 0).getGreen());
    assertEquals(104, currentState.getLayer(1).getPixelAt(0, 0).getBlue());
    assertEquals(new ArrayList<String>(Arrays.asList("firstLayer", "secondLayer")),
        currentState.getLayerNames());
  }

  @Test
  public void testGreyscaleAction() throws AWTException {
    ActionEvent testGreyscale = new ActionEvent(test, 0, "Greyscale");

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }
    test.actionPerformed(new ActionEvent(test, 0, "Select secondLayer"));
    test.actionPerformed(testGreyscale);
    LayeredImage currentState = this.getViewableForTesting(test, "delete4");
    assertEquals(1, currentState.getWidth());
    assertEquals(1, currentState.getHeight());
    assertEquals(2, currentState.getNumLayers());
    assertEquals(111, currentState.getLayer(1).getPixelAt(0, 0).getRed());
    assertEquals(111, currentState.getLayer(1).getPixelAt(0, 0).getGreen());
    assertEquals(111, currentState.getLayer(1).getPixelAt(0, 0).getBlue());
    assertEquals(new ArrayList<String>(Arrays.asList("firstLayer", "secondLayer")),
        currentState.getLayerNames());
  }


  @Test
  public void testSaveLayer() throws AWTException {

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }
    test.actionPerformed(new ActionEvent(test, 0, "Select secondLayer"));
    GraphOfPixels currentState = this.getViewableSingleImageForTesting(test, "delete5.png");
    assertEquals(1, currentState.getWidth());
    assertEquals(1, currentState.getHeight());
    assertEquals(111, currentState.getPixelAt(0, 0).getRed());
    assertEquals(111, currentState.getPixelAt(0, 0).getGreen());
    assertEquals(111, currentState.getPixelAt(0, 0).getBlue());
  }


  @Test
  public void testAddNewLayer() throws AWTException {

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    try {
      this.openFileAddLayerRobot(test, "outputImages/testSaveLayered", "layer");
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }
    LayeredImage currentState = this.getViewableForTesting(test, "delete6");
    assertEquals(3, currentState.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("layer", "firstLayer", "secondLayer")),
        currentState.getLayerNames());
  }


  @Test
  public void testImportLayer() throws AWTException {

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    LayeredImage currentState = this.getViewableForTesting(test, "delete6");
    assertEquals(2, currentState.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("firstLayer", "secondLayer")),
        currentState.getLayerNames());

    try {
      this.openFileInputNameRobot(test, "outputImages/testSaveLayered/firstLayer.png", "layer");
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }
    currentState = this.getViewableForTesting(test, "delete7");
    assertEquals(3, currentState.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("layer", "firstLayer", "secondLayer")),
        currentState.getLayerNames());
  }


  @Test
  public void testSaveLayeredAsImage() throws AWTException {

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    GraphOfPixels currentState = this.getViewableSingleLayeredForTesting(test, "delete8.png");
    assertEquals(1, currentState.getWidth());
    assertEquals(1, currentState.getHeight());
    assertEquals(50, currentState.getPixelAt(0, 0).getRed());
    assertEquals(50, currentState.getPixelAt(0, 0).getGreen());
    assertEquals(50, currentState.getPixelAt(0, 0).getBlue());
  }


  @Test
  public void testExecuteScript() throws AWTException {

    try {
      this.executeScriptRobot(test, "outputImages/TestLayeredCreate.txt");
    } catch (AWTException ignored) {
      //These do not impede testing process, nor do they interrupt it, so we ignore them.
    }

    LayeredImage currentState = this.getViewableForTesting(test, "delete9");
    assertEquals(1, currentState.getWidth());
    assertEquals(1, currentState.getHeight());
    assertEquals(1, currentState.getNumLayers());
    assertEquals(new ArrayList<String>(Collections.singletonList("secondLayer")),
        currentState.getLayerNames());
    assertEquals(111, currentState.getLayer(0).getPixelAt(0, 0).getRed());
    assertEquals(111, currentState.getLayer(0).getPixelAt(0, 0).getGreen());
    assertEquals(111, currentState.getLayer(0).getPixelAt(0, 0).getBlue());
  }

  @Test
  public void testControllerViewConnection() {
    MockGraphicalView mock = new MockGraphicalView();
    ImageProcessingController controller = mock.getController();
    mock.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Load File"));
    assertEquals(new ArrayList<String>(Collections.singletonList("loadFileReceived")),
        controller.getLayeredImageNames());
    mock.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Blur"));
    assertEquals(new ArrayList<String>(Arrays.asList("loadFileReceived", "blurReceived")),
        controller.getLayeredImageNames());
    mock.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Sharpen"));
    assertEquals(
        new ArrayList<String>(Arrays.asList("loadFileReceived", "blurReceived", "sharpenReceived")),
        controller.getLayeredImageNames());
    mock.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Greyscale"));
    assertEquals(new ArrayList<String>(
            Arrays.asList("loadFileReceived", "blurReceived", "sharpenReceived",
                "greyscaleReceived")),
        controller.getLayeredImageNames());
    mock.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Sepia"));
    assertEquals(new ArrayList<String>(Arrays
        .asList("sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived",
            "greyscaleReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
        "Save Current Layer"));
    assertEquals(new ArrayList<String>(Arrays
        .asList("sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived",
            "greyscaleReceived", "saveCurrentLayerReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Add New Layer"));
    assertEquals(new ArrayList<String>(Arrays
            .asList("sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived",
                "greyscaleReceived", "addNewLayerReceived", "saveCurrentLayerReceived")),
        controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
        "Load Image As Layer"));
    assertEquals(new ArrayList<String>(Arrays
        .asList("sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived",
            "greyscaleReceived", "loadImageAsLayerReceived", "addNewLayerReceived",
            "saveCurrentLayerReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Save File"));
    assertEquals(new ArrayList<String>(Arrays
        .asList("saveFileReceived", "sepiaReceived", "loadFileReceived", "blurReceived",
            "sharpenReceived", "greyscaleReceived", "loadImageAsLayerReceived",
            "addNewLayerReceived", "saveCurrentLayerReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
        "Export as Image"));
    assertEquals(new ArrayList<String>(Arrays
            .asList("saveFileReceived", "sepiaReceived", "loadFileReceived", "blurReceived",
                "sharpenReceived", "greyscaleReceived", "exportAsImageReceived",
                "loadImageAsLayerReceived", "addNewLayerReceived", "saveCurrentLayerReceived")),
        controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
        "Execute Script"));
    assertEquals(new ArrayList<String>(Arrays
        .asList("saveFileReceived", "sepiaReceived", "loadFileReceived", "blurReceived",
            "sharpenReceived", "greyscaleReceived", "exportAsImageReceived",
            "loadImageAsLayerReceived", "executeScriptReceived", "addNewLayerReceived",
            "saveCurrentLayerReceived")), controller.getLayeredImageNames());
    MockGraphicalView mockForDynamicTesting = new MockGraphicalView();
    ImageProcessingController controller2 = mockForDynamicTesting.getController();
    mockForDynamicTesting.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
            "Move Up Some Layer"));
    assertEquals(new ArrayList<String>(Collections.singletonList("moveUpDynamicCommandReceived")),
        controller2.getLayeredImageNames());
    mockForDynamicTesting.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
            "Move Down Some Layer"));
    assertEquals(new ArrayList<String>(
            Arrays.asList("moveUpDynamicCommandReceived", "moveDownDynamicCommandReceived")),
        controller2.getLayeredImageNames());
    mockForDynamicTesting.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
            "Copy Some Layer"));
    assertEquals(new ArrayList<String>(Arrays
        .asList("copyDynamicCommandReceived", "moveUpDynamicCommandReceived",
            "moveDownDynamicCommandReceived")), controller2.getLayeredImageNames());
    mockForDynamicTesting.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
            "Show Some Layer"));
    assertEquals(new ArrayList<String>(Arrays
            .asList("copyDynamicCommandReceived", "moveUpDynamicCommandReceived",
                "moveDownDynamicCommandReceived", "showDynamicCommandReceived")),
        controller2.getLayeredImageNames());
    mockForDynamicTesting.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
            "Hide Some Layer"));
    assertEquals(new ArrayList<String>(Arrays
        .asList("hideDynamicCommandReceived", "copyDynamicCommandReceived",
            "moveUpDynamicCommandReceived", "moveDownDynamicCommandReceived",
            "showDynamicCommandReceived")), controller2.getLayeredImageNames());
    mockForDynamicTesting.actionPerformed(
        new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
            "Change Tab to Some Image"));
    assertEquals(new ArrayList<String>(Arrays
            .asList("hideDynamicCommandReceived", "changeTabDynamicCommandReceived",
                "copyDynamicCommandReceived", "moveUpDynamicCommandReceived",
                "moveDownDynamicCommandReceived", "showDynamicCommandReceived")),
        controller2.getLayeredImageNames());
  }

  /**
   * For testing dynamically produced actions from the GraphicalView buttons that correspond to
   * layers.
   */
  public static class TestLayerTabActions {

    private JMenu getFileMenu(GraphicalView fromView) {
      return this.getMenu(fromView, 0);
    }

    private JMenu getMenu(GraphicalView fromView, int index) {
      return ((JMenuBar) (((JPanel) (((JScrollPane) (fromView.getRootPane().getContentPane()
          .getComponent(0))).getViewport().getComponent(0))).getComponent(3))).getMenu(index);
    }

    /**
     * Given a controller and the name of an image contained by that controller, saves the image and
     * then loads it as a layeredImage so everything that is necessary for testing can be reached.
     *
     * @param toSaveFrom The view from which to save the image
     * @param toSaveAs   The name of the image to be saved as
     * @return The LayeredImage representation of the image to be loaded
     * @throws IllegalArgumentException If either input is null, or if there is an issue fetching
     *                                  the image
     */
    private LayeredImage getViewableForTesting(GraphicalView toSaveFrom, String toSaveAs)
        throws IllegalArgumentException, AWTException {
      if (toSaveFrom == null) {
        throw new IllegalArgumentException("Null input");
      }
      try {
        Thread.sleep(500);
      } catch (InterruptedException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }

      this.openSaveFileRobot(toSaveFrom, toSaveAs, false);
      try {
        Thread.sleep(500);
      } catch (InterruptedException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }

      return new LayeredImageV0(toSaveAs);
    }

    /**
     * Creates a Robot that automates clicking and loading/saving files.
     *
     * @param toOpenOn   View to open files on
     * @param fileToOpen File to open up
     * @param isOpen     If opening versus saving
     * @throws AWTException             Thrown when automating
     * @throws IllegalArgumentException If any arguments are null
     */
    private void openSaveFileRobot(GraphicalView toOpenOn, String fileToOpen, boolean isOpen)
        throws AWTException, IllegalArgumentException {
      if (toOpenOn == null || fileToOpen == null) {
        throw new IllegalArgumentException("Null input");
      }
      try {
        Thread.sleep(300);
      } catch (InterruptedException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }

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
      fileOpener.mouseMove(p2.x + fileMenu.getItem(0).getWidth() / 2,
          p2.y + fileMenu.getItem(0).getHeight() / 2);
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
      try {
        Thread.sleep(600);
      } catch (InterruptedException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }

    }

    private void cloneSecondLayerRobot(GraphicalView toCloneIn, String nameForClone)
        throws AWTException {
      try {
        Thread.sleep(300);
      } catch (InterruptedException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }

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
      try {
        Thread.sleep(600);
      } catch (InterruptedException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }

    }

    @Test
    public void testMoveUp() throws AWTException {
      GraphicalView forTesting = new GraphicalView();
      try {
        this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
      } catch (AWTException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }
      LayeredImage currentState = this
          .getViewableForTesting(forTesting, "outputImages/moveupdelete0");
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
          currentState.getLayerNames());
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Move Up red-layer"));
      assertEquals(currentState.getLayer(2), currentState.getLayer("red-layer"));
      currentState = this.getViewableForTesting(forTesting, "outputImages/moveupdelete1");
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "red-layer", "blue-layer")),
          currentState.getLayerNames());
      assertEquals(currentState.getLayer(1), currentState.getLayer("red-layer"));
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Move Up invisible-layer"));
      currentState = this.getViewableForTesting(forTesting, "outputImages/moveupdelete2");
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "red-layer", "blue-layer")),
          currentState.getLayerNames());
      assertEquals(currentState.getLayer(0), currentState.getLayer("invisible-layer"));
      forTesting.setVisible(false);
    }

    @Test
    public void testMoveDown() throws AWTException {
      GraphicalView forTesting = new GraphicalView();
      try {
        this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
      } catch (AWTException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }
      LayeredImage currentState = this
          .getViewableForTesting(forTesting, "outputImages/movedowndelete0");
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
          currentState.getLayerNames());
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Move Down invisible-layer"));
      assertEquals(currentState.getLayer(0), currentState.getLayer("invisible-layer"));
      currentState = this.getViewableForTesting(forTesting, "outputImages/movedowndelete1");
      assertEquals(
          new ArrayList<String>(Arrays.asList("blue-layer", "invisible-layer", "red-layer")),
          currentState.getLayerNames());
      assertEquals(currentState.getLayer(1), currentState.getLayer("invisible-layer"));
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Move Down red-layer"));
      currentState = this.getViewableForTesting(forTesting, "outputImages/movedowndelete2");
      assertEquals(
          new ArrayList<String>(Arrays.asList("blue-layer", "invisible-layer", "red-layer")),
          currentState.getLayerNames());
      assertEquals(currentState.getLayer(2), currentState.getLayer("red-layer"));
      forTesting.setVisible(false);
    }

    @Test
    public void testShowLayer() throws AWTException {
      GraphicalView forTesting = new GraphicalView();
      try {
        this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
      } catch (AWTException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }
      LayeredImage currentState = this
          .getViewableForTesting(forTesting, "outputImages/showdelete0");
      assertFalse(currentState.getVisibility("invisible-layer"));
      assertTrue(currentState.getVisibility("red-layer"));
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Show invisible-layer"));
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Show red-layer"));
      currentState = this.getViewableForTesting(forTesting, "outputImages/showdelete1");
      assertTrue(currentState.getVisibility("invisible-layer"));
      assertTrue(currentState.getVisibility("red-layer"));
      forTesting.setVisible(false);
    }

    @Test
    public void testHideLayer() throws AWTException {
      GraphicalView forTesting = new GraphicalView();
      try {
        this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
      } catch (AWTException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }
      LayeredImage currentState = this
          .getViewableForTesting(forTesting, "outputImages/hidedelete0");
      assertFalse(currentState.getVisibility("invisible-layer"));
      assertTrue(currentState.getVisibility("red-layer"));
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Hide invisible-layer"));
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Hide red-layer"));
      currentState = this.getViewableForTesting(forTesting, "outputImages/hidedelete1");
      assertFalse(currentState.getVisibility("invisible-layer"));
      assertFalse(currentState.getVisibility("red-layer"));
      forTesting.setVisible(false);
    }

    @Test
    public void testCopyLayer() throws AWTException {
      GraphicalView forTesting = new GraphicalView();
      try {
        this.openSaveFileRobot(forTesting, "outputImages/exampleLayeredImage", true);
      } catch (AWTException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }
      LayeredImage currentState = this
          .getViewableForTesting(forTesting, "outputImages/copydelete0");
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
          currentState.getLayerNames());
      assertEquals(3, currentState.getNumLayers());
      this.cloneSecondLayerRobot(forTesting, "blue-layer-clone");
      currentState = this.getViewableForTesting(forTesting, "outputImages/copydelete1");
      assertEquals(new ArrayList<String>(
              Arrays.asList("blue-layer-clone", "invisible-layer", "blue-layer", "red-layer")),
          currentState.getLayerNames());
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
      assertEquals(new ArrayList<String>(Arrays
          .asList("invisible-layer-clone", "blue-layer-clone", "invisible-layer", "blue-layer",
              "red-layer")), currentState.getLayerNames());
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
      } catch (AWTException ignored) {
        //These do not impede testing process, nor do they interrupt it, so we ignore them.
      }
      LayeredImage currentState = this.getViewableForTesting(forTesting, "outputImages/tabdelete0");
      assertEquals(new ArrayList<String>(Arrays.asList("galaxy", "rainbow", "birb")),
          currentState.getLayerNames());
      assertEquals(1024, currentState.getWidth());
      assertEquals(768, currentState.getHeight());
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Change Tab examplelayeredimage"));
      currentState = this.getViewableForTesting(forTesting, "outputImages/tabdelete1");
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
          currentState.getLayerNames());
      assertEquals(20, currentState.getWidth());
      assertEquals(20, currentState.getHeight());
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Change Tab examplelayeredimage"));
      currentState = this.getViewableForTesting(forTesting, "outputImages/tabdelete2");
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
          currentState.getLayerNames());
      assertEquals(20, currentState.getWidth());
      assertEquals(20, currentState.getHeight());
      forTesting.actionPerformed(
          new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED,
              "Change Tab misc"));
      currentState = this.getViewableForTesting(forTesting, "outputImages/tabdelete3");
      assertEquals(new ArrayList<String>(Arrays.asList("galaxy", "rainbow", "birb")),
          currentState.getLayerNames());
      assertEquals(1024, currentState.getWidth());
      assertEquals(768, currentState.getHeight());
      forTesting.setVisible(false);
    }

  }

  /**
   * Represents a mock version of a graphical view which can receive action events and pass them on
   * to an accessible controller, used for testing the link between a view and controller.
   */
  public static class MockGraphicalView implements View, ActionListener {

    private final ImageProcessingController controller;
    private final Appendable errors;

    /**
     * Constructs a new TestGraphicalView.MockGraphicalView
     */
    public MockGraphicalView() {
      controller = new ProcessingController(this);
      this.errors = new StringBuilder();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e == null) {
        throw new IllegalArgumentException("Null action");
      }
      switch (e.getActionCommand()) {
        case "Load File":
          controller.runCommands("create-layered-image loadFileReceived 1 1");
          break;
        case "Blur":
          controller.runCommands("create-layered-image blurReceived 1 1");
          break;
        case "Sharpen":
          controller.runCommands("create-layered-image sharpenReceived 1 1");
          break;
        case "Greyscale":
          controller.runCommands("create-layered-image greyscaleReceived 1 1");
          break;
        case "Sepia":
          controller.runCommands("create-layered-image sepiaReceived 1 1");
          break;
        case "Save Current Layer":
          controller.runCommands("create-layered-image saveCurrentLayerReceived 1 1");
          break;
        case "Add New Layer":
          controller.runCommands("create-layered-image addNewLayerReceived 1 1");
          break;
        case "Load Image As Layer":
          controller.runCommands("create-layered-image loadImageAsLayerReceived 1 1");
          break;
        case "Save File":
          controller.runCommands("create-layered-image saveFileReceived 1 1");
          break;
        case "Export as Image":
          controller.runCommands("create-layered-image exportAsImageReceived 1 1");
          break;
        case "Execute Script":
          controller.runCommands("create-layered-image executeScriptReceived 1 1");
          break;
        default:
          if (e.getActionCommand().startsWith("Move Up ")) {
            controller.runCommands("create-layered-image moveUpDynamicCommandReceived 1 1");
          } else if (e.getActionCommand().startsWith("Move Down ")) {
            controller.runCommands("create-layered-image moveDownDynamicCommandReceived 1 1");
          } else if (e.getActionCommand().startsWith("Copy ")) {
            controller.runCommands("create-layered-image copyDynamicCommandReceived 1 1");
          } else if (e.getActionCommand().startsWith("Show ")) {
            controller.runCommands("create-layered-image showDynamicCommandReceived 1 1");
          } else if (e.getActionCommand().startsWith("Hide ")) {
            controller.runCommands("create-layered-image hideDynamicCommandReceived 1 1");
          } else if (e.getActionCommand().startsWith("Change Tab ")) {
            controller.runCommands("create-layered-image changeTabDynamicCommandReceived 1 1");
          }
      }
    }

    @Override
    public void renderException(String message) throws IllegalArgumentException {
      if (message == null) {
        throw new IllegalArgumentException("Null message");
      }
      try {
        this.errors.append(message);
      } catch (IOException e) {
        throw new IllegalArgumentException("Could not write message");
      }
    }

    @Override
    public void showView() {
      //Does not do anything for the MockView.
    }

    /**
     * Returns a reference to this view's associated controller, so it's state can be assessed for
     * testing.
     *
     * @return A reference to this view's associated controller
     */
    public ImageProcessingController getController() {
      return this.controller;
    }
  }
}

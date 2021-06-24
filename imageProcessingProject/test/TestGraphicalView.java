import static org.junit.Assert.assertEquals;

import controller.ImageProcessingController;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import view.GraphicalView;

/**
 * Tests methods and relevant view options for a graphical GUI view. NOTE: The showView method
 * relies on inputs from System.in, but as this can not be * simulated, we instead test the outputs
 * and the inputs are handled in the controller tests, so * refer to other tests (such as those for
 * input/output behavior tested in the TestCommand and * TestLanguageSyntax classes). NOTE 2: Render
 * Exception creates a pop-up here, so this can not be tested. As long as the message given is not
 * null, it is displayed.
 */
public class TestGraphicalView {

  private JMenu getFileMenu(GraphicalView fromView) {
    return this.getMenu(fromView, 0);
  }

  private JMenu getMenu(GraphicalView fromView, int index) {
    return ((JMenuBar)(((JPanel)(((JScrollPane)(fromView.getRootPane().getContentPane().getComponent(0))).getViewport().getComponent(0))).getComponent(3))).getMenu(index);
  }

  private final GraphicalView test = new GraphicalView();

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
   * Given a controller and the name of an image contained by that controller, saves an image
   * and then loads it as a GraphOfPixels so everything that is necessary for testing can be
   * reached.
   * @param toSaveFrom The view from which to save the image
   * @param toSaveAs The name of the image to be saved as
   * @return The LayeredImage representation of the image to be loaded
   * @throws IllegalArgumentException If either input is null, or if there is an issue fetching the image
   */
  private GraphOfPixels getViewableSingleImageForTesting(GraphicalView toSaveFrom, String toSaveAs) throws IllegalArgumentException, AWTException {
    if (toSaveFrom == null) {
      throw new IllegalArgumentException("Null input");
    }
    try{Thread.sleep(500);}catch(InterruptedException e) {};
    this.saveFileSingleImageRobot(toSaveFrom, toSaveAs);
    try{Thread.sleep(500);}catch(InterruptedException e) {};
    return ImageToGraphConverter.convertImage(toSaveAs);
  }

  /**
   * Given a controller and the name of an image contained by that controller, saves a layered image as a single image
   * and then loads it as a GraphOfPixels so everything that is necessary for testing can be
   * reached.
   * @param toSaveFrom The view from which to save the image
   * @param toSaveAs The name of the image to be saved as
   * @return The LayeredImage representation of the image to be loaded
   * @throws IllegalArgumentException If either input is null, or if there is an issue fetching the image
   */
  private GraphOfPixels getViewableSingleLayeredForTesting(GraphicalView toSaveFrom, String toSaveAs) throws IllegalArgumentException, AWTException {
    if (toSaveFrom == null) {
      throw new IllegalArgumentException("Null input");
    }
    try{Thread.sleep(500);}catch(InterruptedException e) {};
    this.saveLayeredAsImageRobot(toSaveFrom, toSaveAs);
    try{Thread.sleep(500);}catch(InterruptedException e) {};
    return ImageToGraphConverter.convertImage(toSaveAs);
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

  /**
   * Creates a Robot that automates saving layers as images.
   * @param toOpenOn View to open files on
   * @param fileToSave File to save
   * @throws AWTException Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void saveFileSingleImageRobot(GraphicalView toOpenOn, String fileToSave) throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToSave == null) {
      throw new IllegalArgumentException("Null input");
    }
    try{Thread.sleep(300);}catch(InterruptedException e) {};
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
    fileOpener.mouseMove(p2.x + layerMenu.getItem(0).getWidth() / 2, p2.y + layerMenu.getItem(0).getHeight() / 2);
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
    try{Thread.sleep(600);}catch(InterruptedException e) {};
  }

  /**
   * Creates a Robot that automates opening a file and adding a layer.
   * @param toOpenOn View to open files on
   * @param fileToOpen File to open
   * @throws AWTException Thrown when automatingnewlayer
   * @throws IllegalArgumentException If any arguments are null
   */
  private void openFileInputNameRobot(GraphicalView toOpenOn, String fileToOpen, String layerToAdd) throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToOpen == null) {
      throw new IllegalArgumentException("Null input");
    }
    try{Thread.sleep(300);}catch(InterruptedException e) {};
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
    fileOpener.mouseMove(p2.x + layerMenu.getItem(0).getWidth() / 2, p2.y + layerMenu.getItem(0).getHeight() / 2);
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
    try{Thread.sleep(600);}catch(InterruptedException e) {};
  }

  /**
   * Creates a Robot that automates opening a file and adding a layer.
   * @param toOpenOn View to open files on
   * @param fileToOpen File to open
   * @throws AWTException Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void openFileAddLayerRobot(GraphicalView toOpenOn, String fileToOpen, String layerToAdd) throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToOpen == null) {
      throw new IllegalArgumentException("Null input");
    }
    try{Thread.sleep(300);}catch(InterruptedException e) {};
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
    fileOpener.mouseMove(p2.x + layerMenu.getItem(0).getWidth() / 2, p2.y + layerMenu.getItem(0).getHeight() / 2);
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
    try{Thread.sleep(600);}catch(InterruptedException e) {};
  }


  /**
   * Creates a Robot that automates saving layers as images.
   * @param toOpenOn View to open files on
   * @param fileToSave File to save
   * @throws AWTException Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void saveLayeredAsImageRobot(GraphicalView toOpenOn, String fileToSave) throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToSave == null) {
      throw new IllegalArgumentException("Null input");
    }
    try{Thread.sleep(300);}catch(InterruptedException e) {};
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
    fileOpener.mouseMove(p2.x + fileMenu.getItem(0).getWidth() / 2, p2.y + fileMenu.getItem(0).getHeight() / 2);
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
    try{Thread.sleep(600);}catch(InterruptedException e) {};
  }


  /**
   * Creates a Robot that automates saving layers as images.
   * @param toOpenOn View to open files on
   * @param fileToOpen File to open
   * @throws AWTException Thrown when automating
   * @throws IllegalArgumentException If any arguments are null
   */
  private void executeScriptRobot(GraphicalView toOpenOn, String fileToOpen) throws AWTException, IllegalArgumentException {
    if (toOpenOn == null || fileToOpen == null) {
      throw new IllegalArgumentException("Null input");
    }
    try{Thread.sleep(300);}catch(InterruptedException e) {};
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
    } catch(AWTException e) {
    }
    LayeredImage currentState = this.getViewableForTesting(test, "delete0");
    assertEquals(20, currentState.getWidth());
    assertEquals(20, currentState.getHeight());
    assertEquals(3, currentState.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")), currentState.getLayerNames());
  }





  @Test
  public void testBlurAction() throws AWTException {
    ActionEvent testBlur = new ActionEvent(test, 0, "Blur");

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch(AWTException e) {
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
    assertEquals(new ArrayList<String>(Arrays.asList("firstLayer", "secondLayer")), currentState.getLayerNames());
  }



  @Test
  public void testSharpenAction() throws AWTException {
    ActionEvent testSharpen = new ActionEvent(test, 0, "Sharpen");

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch(AWTException e) {
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
    assertEquals(new ArrayList<String>(Arrays.asList("firstLayer", "secondLayer")), currentState.getLayerNames());
  }

  @Test
  public void testSepiaAction() throws AWTException {
    ActionEvent testSepia = new ActionEvent(test, 0, "Sepia");

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch(AWTException e) {
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
    assertEquals(new ArrayList<String>(Arrays.asList("firstLayer", "secondLayer")), currentState.getLayerNames());
  }

  @Test
  public void testGreyscaleAction() throws AWTException {
    ActionEvent testGreyscale = new ActionEvent(test, 0, "Greyscale");

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch(AWTException e) {
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
    assertEquals(new ArrayList<String>(Arrays.asList("firstLayer", "secondLayer")), currentState.getLayerNames());
  }



  @Test
  public void testSaveLayer() throws AWTException {

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch(AWTException e) {
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
    } catch(AWTException e) {
    }

    try {
      this.openFileAddLayerRobot(test, "outputImages/testSaveLayered", "layer");
    } catch(AWTException e) {
    }
    LayeredImage currentState = this.getViewableForTesting(test, "delete6");
    assertEquals(3, currentState.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("layer", "firstLayer", "secondLayer")), currentState.getLayerNames());
  }



  @Test
  public void testImportLayer() throws AWTException {

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch(AWTException e) {
    }

    LayeredImage currentState = this.getViewableForTesting(test, "delete6");
    assertEquals(2, currentState.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("firstLayer", "secondLayer")), currentState.getLayerNames());

    try {
      this.openFileInputNameRobot(test, "outputImages/testSaveLayered/firstLayer.png", "layer");
    } catch(AWTException e) {
    }
    currentState = this.getViewableForTesting(test, "delete7");
    assertEquals(3, currentState.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("layer", "firstLayer", "secondLayer")), currentState.getLayerNames());
  }


  @Test
  public void testSaveLayeredAsImage() throws AWTException {

    try {
      this.openSaveFileRobot(test, "outputImages/testSaveLayered", true);
    } catch(AWTException e) {
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
    } catch(AWTException e) {
    }

    LayeredImage currentState = this.getViewableForTesting(test, "delete9");
    assertEquals(1, currentState.getWidth());
    assertEquals(1, currentState.getHeight());
    assertEquals(1, currentState.getNumLayers());
    assertEquals(new ArrayList<String>(Collections.singletonList("secondLayer")), currentState.getLayerNames());
    assertEquals(111, currentState.getLayer(0).getPixelAt(0, 0).getRed());
    assertEquals(111, currentState.getLayer(0).getPixelAt(0, 0).getGreen());
    assertEquals(111, currentState.getLayer(0).getPixelAt(0, 0).getBlue());
  }

  @Test
  public void testControllerViewConnection() {
    MockGraphicalView mock = new MockGraphicalView();
    ImageProcessingController controller = mock.getController();
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Load File"));
    assertEquals(new ArrayList<String>(Arrays.asList("loadFileReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Blur"));
    assertEquals(new ArrayList<String>(Arrays.asList("loadFileReceived", "blurReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Sharpen"));
    assertEquals(new ArrayList<String>(Arrays.asList("loadFileReceived", "blurReceived", "sharpenReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Greyscale"));
    assertEquals(new ArrayList<String>(Arrays.asList("loadFileReceived", "blurReceived", "sharpenReceived", "greyscaleReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Sepia"));
    assertEquals(new ArrayList<String>(Arrays.asList("sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived", "greyscaleReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Save Current Layer"));
    assertEquals(new ArrayList<String>(Arrays.asList("sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived", "greyscaleReceived", "saveCurrentLayerReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Add New Layer"));
    assertEquals(new ArrayList<String>(Arrays.asList("sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived", "greyscaleReceived", "addNewLayerReceived", "saveCurrentLayerReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Load Image As Layer"));
    assertEquals(new ArrayList<String>(Arrays.asList("sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived", "greyscaleReceived", "loadImageAsLayerReceived", "addNewLayerReceived", "saveCurrentLayerReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Save File"));
    assertEquals(new ArrayList<String>(Arrays.asList("saveFileReceived", "sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived", "greyscaleReceived", "loadImageAsLayerReceived", "addNewLayerReceived", "saveCurrentLayerReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Export as Image"));
    assertEquals(new ArrayList<String>(Arrays.asList("saveFileReceived", "sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived", "greyscaleReceived","exportAsImageReceived", "loadImageAsLayerReceived", "addNewLayerReceived", "saveCurrentLayerReceived")), controller.getLayeredImageNames());
    mock.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Execute Script"));
    assertEquals(new ArrayList<String>(Arrays.asList("saveFileReceived", "sepiaReceived", "loadFileReceived", "blurReceived", "sharpenReceived", "greyscaleReceived","exportAsImageReceived", "loadImageAsLayerReceived", "executeScriptReceived", "addNewLayerReceived", "saveCurrentLayerReceived")), controller.getLayeredImageNames());
    MockGraphicalView mockForDynamicTesting = new MockGraphicalView();
    ImageProcessingController controller2 = mockForDynamicTesting.getController();
    mockForDynamicTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Move Up Some Layer"));
    assertEquals(new ArrayList<String>(Arrays.asList("moveUpDynamicCommandReceived")), controller2.getLayeredImageNames());
    mockForDynamicTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Move Down Some Layer"));
    assertEquals(new ArrayList<String>(Arrays.asList("moveUpDynamicCommandReceived", "moveDownDynamicCommandReceived")), controller2.getLayeredImageNames());
    mockForDynamicTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Copy Some Layer"));
    assertEquals(new ArrayList<String>(Arrays.asList("copyDynamicCommandReceived", "moveUpDynamicCommandReceived", "moveDownDynamicCommandReceived")), controller2.getLayeredImageNames());
    mockForDynamicTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Show Some Layer"));
    assertEquals(new ArrayList<String>(Arrays.asList("copyDynamicCommandReceived", "moveUpDynamicCommandReceived", "moveDownDynamicCommandReceived", "showDynamicCommandReceived")), controller2.getLayeredImageNames());
    mockForDynamicTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Hide Some Layer"));
    assertEquals(new ArrayList<String>(Arrays.asList("hideDynamicCommandReceived", "copyDynamicCommandReceived", "moveUpDynamicCommandReceived", "moveDownDynamicCommandReceived", "showDynamicCommandReceived")), controller2.getLayeredImageNames());
    mockForDynamicTesting.actionPerformed(new ActionEvent(new JButton("For Testing"), ActionEvent.ACTION_PERFORMED, "Change Tab to Some Image"));
    assertEquals(new ArrayList<String>(Arrays.asList("hideDynamicCommandReceived", "changeTabDynamicCommandReceived", "copyDynamicCommandReceived", "moveUpDynamicCommandReceived", "moveDownDynamicCommandReceived", "showDynamicCommandReceived")), controller2.getLayeredImageNames());
  }



  @Test
  public void getFileMenu() {
  }
}

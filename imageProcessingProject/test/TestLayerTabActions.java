
import controller.ImageProcessingController;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractButton;
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
   * @param controller The controller which contains the image
   * @param imageName The name of the image to be loaded
   * @return The LayeredImage representation of the image to be loaded
   * @throws IllegalArgumentException If either input is null, or if there is an issue fetching the image
   */
  private LayeredImage getViewableForTesting(ImageProcessingController controller, String imageName) throws IllegalArgumentException {
    if (controller == null || imageName == null) {
      throw new IllegalArgumentException("Null input");
    }
    controller.runCommands("save-layered " + imageName + "outputImages/tempForTesting");
    return new LayeredImageV0("outputImages/tempForTesting");
  }

  private void openFileRobot(GraphicalView toOpenOn, String fileToOpen) throws AWTException {
    try {Thread.sleep(500);}catch (InterruptedException e) {};
    JMenu fileMenu = toOpenOn.getFileMenu();
    Point p = fileMenu.getLocationOnScreen();
    Robot fileOpener = new Robot();
    fileOpener.mouseMove(p.x + fileMenu.getWidth() / 2, p.y + fileMenu.getHeight() / 2);
    fileOpener.delay(100);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(100);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    Point p2 = fileMenu.getItem(0).getLocationOnScreen();
    fileOpener.mouseMove(p2.x + fileMenu.getItem(0).getWidth() / 2, p2.y + fileMenu.getItem(0).getHeight() / 2);
    fileOpener.delay(500);
    fileOpener.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(500);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.delay(500);
    fileOpener.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    fileOpener.mouseMove(300, 460);
    fileOpener.delay(100);
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
    fileOpener.delay(10000);
  }

  @Test
  public void testMoveUp() {
    //Nonexistent layer
    GraphicalView forTesting = new GraphicalView();
    forTesting.setVisible(true);
    try {
      this.openFileRobot(forTesting, "outputImages/exampleLayeredImage");
    } catch(AWTException e) {
    }
  }
}

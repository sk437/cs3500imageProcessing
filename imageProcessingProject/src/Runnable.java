import controller.ImageProcessingController;
import controller.ProcessingController;

/**
 * Class used to create a runnable JAR file that will run the two written example scripts.
 */
public class Runnable {

  /**
   * Reads the ExampleSingleImage and ExampleLayeredImage scripts, and executes them.
   * @param args not used
   */
  public static void main(String[] args) {
    ImageProcessingController script0 = new ProcessingController("res/ExampleSingleImage", System.out);
    ImageProcessingController script1 = new ProcessingController("res/ExampleLayeredImage", System.out);
    script0.run();
    script1.run();
  }
}

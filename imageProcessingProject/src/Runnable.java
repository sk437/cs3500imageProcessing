import controller.ImageProcessingController;
import controller.ProcessingController;

/**
 * Class used to create a runnable JAR file that will run the two written example scripts.
 */
public class Runnable {

  /**
   * Reads the ExampleSingleImage and ExampleLayeredImage scripts, and executes them.
   *
   * @param args not used
   */
  public static void main(String[] args) {
    ImageProcessingController script0 = new ProcessingController("ExampleSingleImage.txt",
        System.out);
    ImageProcessingController script1 = new ProcessingController("ExampleLayeredImage.txt",
        System.out);
    script0.run();
    script1.run();
  }
}

import controller.ImageProcessingController;
import controller.ProcessingController;
import view.CommandLineTextView;
import view.GraphicalView;
import view.View;

/**
 * Represents a class intended to provide a runnable program for our Image Processor.
 */
public class Program {

  /**
   * Runs a main method that initializes a different view based on given args.
   *
   * @param args Either "-interactive" to start up a GUI, "-script" and a file path to execute a
   *             text file at given file location, or "-text" to start an in command line
   *             interactive script
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("No execution method specified");
    }
    switch (args[0]) {
      case "-script":
        if (args.length != 2) {
          throw new IllegalArgumentException("Invalid Number of Arguments");
        }
        ImageProcessingController script = new ProcessingController(args[1],
            System.out);
        script.run();
        break;
      case "-text":
        View textView = new CommandLineTextView(System.out);
        textView.showView();
        break;
      case "-interactive":
        GraphicalView.setDefaultLookAndFeelDecorated(false);
        GraphicalView frame = new GraphicalView();

        frame.showView();
        break;
      default:
        throw new IllegalArgumentException("Invalid arguments given");
    }
  }
}

import controller.ImageProcessingController;
import controller.ProcessingController;
import java.awt.Image;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import view.CommandLineTextView;
import view.GraphicalView;
import view.View;

public class GraphicsMain {

  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("No execution method specified");
    }
    switch(args[0]) {
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


    /*
    try {
      // Set cross-platform Java L&F (also called "Metal")
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

      //UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());

      //   UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
      //    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      //    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
      //    {
      //       if ("Nimbus".equals(info.getName())) {
      //          UIManager.setLookAndFeel(info.getClassName());
      //         break;
      //    }
      // }
    } catch (UnsupportedLookAndFeelException e) {
      // handle exception
    } catch (ClassNotFoundException e) {
      // handle exception
    } catch (InstantiationException e) {
      // handle exception
    } catch (IllegalAccessException e) {
      // handle exception
    } catch (Exception e) {
    }

     */

  }

}

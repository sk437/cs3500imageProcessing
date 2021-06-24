import controller.ImageProcessingController;
import controller.ProcessingController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import layeredimage.blend.AbstractBlend;
import view.GraphicalView;
import view.View;

/**
 * Represents a mock version of a graphical view which can receive action events and pass
 * them on to an accessible controller, used for testing the link between a view and controller.
 */
public class MockGraphicalView implements View, ActionListener {
  private final ImageProcessingController controller;
  private final Appendable errors;

  /**
   * Constructs a new MockGraphicalView
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
        }
        else if (e.getActionCommand().startsWith("Move Down ")) {
          controller.runCommands("create-layered-image moveDownDynamicCommandReceived 1 1");
        }
        else if (e.getActionCommand().startsWith("Copy ")) {
          controller.runCommands("create-layered-image copyDynamicCommandReceived 1 1");
        }
        else if (e.getActionCommand().startsWith("Show ")) {
          controller.runCommands("create-layered-image showDynamicCommandReceived 1 1");
        }
        else if (e.getActionCommand().startsWith("Hide ")) {
          controller.runCommands("create-layered-image hideDynamicCommandReceived 1 1");
        }
        else if (e.getActionCommand().startsWith("Change Tab ")) {
          controller.runCommands("create-layered-image changeTabDynamicCommandReceived 1 1");
        }
    }
  }
  @Override
  public void renderException(String message) throws IllegalArgumentException {
    if (message == null) {
      throw new IllegalArgumentException("Null message");
    }
    try {this.errors.append(message);} catch (IOException e) {
      throw new IllegalArgumentException("Could not write message");
    }
  }

  @Override
  public void showView() {
  }

  /**
   * Returns a reference to this view's associated controller, so it's state can be
   * assessed for testing.
   * @return A reference to this view's associated controller
   */
  public ImageProcessingController getController() {
    return this.controller;
  }
}

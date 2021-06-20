package view;

import controller.ImageProcessingController;
import controller.ProcessingController;
import javax.swing.JFrame;
import layeredimage.ViewModel;

public class GraphicalView extends JFrame implements View {

  private final ImageProcessingController controller;
  private ViewModel display;
  private String currentImageName;
  private String currentLayerName;


  public GraphicalView() {
    this.controller = new ProcessingController(this);
  }

  @Override
  public void renderException(String message) throws IllegalArgumentException {
    //TODO: Determine how to show error message pop-ups with JFrame
  }
}

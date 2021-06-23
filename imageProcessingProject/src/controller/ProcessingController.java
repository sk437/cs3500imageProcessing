package controller;

import imageasgraph.GraphOfPixels;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import layeredimage.LayeredImage;
import layeredimage.ViewModel;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.ParsedCommand;
import view.TextErrorView;
import view.View;

/**
 * Represents a controller object to execute scripts based on inputs.
 */
public class ProcessingController implements ImageProcessingController {

  private final View view;
  private final Readable input;
  private final HashMap<String, GraphOfPixels> singleImages;
  private final HashMap<String, LayeredImage> layeredImages;

  /**
   * Creates a controller instance using a readable input.
   *
   * @param input  The Readable input for the program
   * @param output The Appendable where information is outputted
   * @throws IllegalArgumentException If any parameters are null
   */
  public ProcessingController(Readable input, Appendable output) throws IllegalArgumentException {
    if (input == null || output == null) {
      throw new IllegalArgumentException("One or more inputs are null");
    }
    this.view = new TextErrorView(output);
    this.input = input;
    this.singleImages = new HashMap<String, GraphOfPixels>();
    this.layeredImages = new HashMap<String, LayeredImage>();
  }

  /**
   * Creates a controller instance using a String file.
   *
   * @param fileInput The String input for the program representing a file
   * @param output    The Appendable where information is outputted
   * @throws IllegalArgumentException If any parameters are null
   */
  public ProcessingController(String fileInput, Appendable output) throws IllegalArgumentException {
    if (fileInput == null || output == null) {
      throw new IllegalArgumentException("One or more inputs are null");
    }
    this.view = new TextErrorView(output);
    FileReader newScript;
    try {
      newScript = new FileReader(fileInput);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found");
    }
    this.input = newScript;
    this.singleImages = new HashMap<String, GraphOfPixels>();
    this.layeredImages = new HashMap<String, LayeredImage>();
  }

  /**
   * Creates a new constructor object.
   * Throws an exception if this kind of controller is run. This controller is simply acting as a script handler rather than an information medium.
   * @param view The interactive or text view for the program
   * @throws IllegalArgumentException If view is null or if after construction this way, the controller is run
   */
  public ProcessingController(View view) throws IllegalArgumentException{
    if (view == null) {
      throw new IllegalArgumentException("Null view provided");
    }
    this.view = view;
    this.input = null;
    this.singleImages = new HashMap<String, GraphOfPixels>();
    this.layeredImages = new HashMap<String, LayeredImage>();
  }

  /**
   * Creates a new constructor object.
   * Throws an exception if this kind of controller is run. This controller is simply acting as a script handler rather than an information medium.
   * @param view The interactive or text view for the program
   * @throws IllegalArgumentException If view is null or if after construction this way, the controller is run
   */
  public ProcessingController(View view, Readable in) throws IllegalArgumentException{
    if (view == null) {
      throw new IllegalArgumentException("Null view provided");
    }
    this.view = view;
    this.input = in;
    this.singleImages = new HashMap<String, GraphOfPixels>();
    this.layeredImages = new HashMap<String, LayeredImage>();
  }


  @Override
  public void run() {
    this.singleImages.clear();
    this.layeredImages.clear();
    if (this.input == null) {
      throw new IllegalArgumentException("Null input, cannot be run this way");
    }
    Scanner scanner = new Scanner(this.input);
    this.runCommandsFromScanner(scanner);
  }

  @Override
  public ViewModel getReferenceToImage(String imageName) throws IllegalArgumentException {
    if (imageName == null) {
      throw new IllegalArgumentException("Null image name given");
    }
    if (!layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("Given image does not exist");
    }

    return this.layeredImages.get(imageName);
  }

  @Override
  public void runCommands(String commands) {
    if (commands == null) {
      this.view.renderException("Null command given");
      return;
    }
    Scanner scanner = new Scanner(commands);
    this.runCommandsFromScanner(scanner);
  }

  @Override
  public List<String> getLayeredImageNames() {
    return new ArrayList<String>(this.layeredImages.keySet());
  }

  /**
   * Given a scanner over a set of input, runs every command contained in that input.
   * @param scanner The scanner which is reading the input
   */
  private void runCommandsFromScanner(Scanner scanner) {
    if (scanner == null) {
      this.view.renderException("Null scanner given for processing");
      return;
    }
    LanguageSyntax parser = new LanguageSyntaxImpl();
    int counter = 0;
    while (scanner.hasNext()) {
      String nextCommand = scanner.nextLine();
      if (nextCommand.length() != 0 && nextCommand.charAt(0) != '#') {
        if (nextCommand.equals("quit")) {
          this.view.renderException("Image Processor Quit");
          return;
        }

        try {
          ParsedCommand toExecute = parser.parseCommand(nextCommand);
          toExecute.execute(singleImages, layeredImages);
          toExecute.alterLanguageState(parser);
        } catch (IllegalArgumentException e) {
          this.view.renderException("Invalid line " + counter + ": " + e.getMessage() + "\n");
        }
      }
      counter += 1;
    }
  }
}

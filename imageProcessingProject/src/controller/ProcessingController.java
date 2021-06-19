package controller;

import imageasgraph.GraphOfPixels;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;
import view.ErrorView;
import view.TextErrorView;

/**
 * Represents a controller object to execute scripts based on inputs.
 */
public class ProcessingController implements ImageProcessingController {

  private final ErrorView view;
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


  @Override
  public void run() {
    this.singleImages.clear();
    this.layeredImages.clear();
    Scanner scanner = new Scanner(this.input);
    LanguageSyntax parser = new LanguageSyntaxImpl();
    int counter = 0;
    while (scanner.hasNext()) {
      String nextCommand = scanner.nextLine();
      if (nextCommand.length() != 0 && nextCommand.charAt(0) != '#') {
        if (nextCommand.equals("quit")) {
          this.view.renderMessage("Image Processor Quit");
          return;
        }

        try {
          ParsedCommand toExecute = parser.parseCommand(nextCommand);
          toExecute.execute(singleImages, layeredImages);
          toExecute.alterLanguageState(parser);
        } catch (IllegalArgumentException e) {
          this.view.renderMessage("Invalid line " + counter + ": " + e.getMessage() + "\n");
        }
      }
      counter += 1;
    }
  }
}

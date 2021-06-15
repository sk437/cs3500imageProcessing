package scriptlanguage.parsedcommands.blend;

import imageasgraph.GraphOfPixels;
import imageasgraph.OutputType;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.blend.BasicBlend;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which saves an existing layered image as the result of applying a basic
 * blend to that image.
 */
public class BasicBlendCommand implements ParsedCommand {
  private final String imageToBlend;
  private final OutputType outputType;
  private final String fileName;

  /**
   * Constructs a new BasicBlendCommand, which will blend an image of the given name and save it
   * as a file of the given name and type.
   * @param imageToBlend The name of the layered image to be blended
   * @param outputType The type of file to be outputted
   * @param fileName The name of the file to be outputted
   * @throws IllegalArgumentException If given a null input
   */
  public BasicBlendCommand(String imageToBlend, String outputType, String fileName) throws IllegalArgumentException {
    if (imageToBlend == null || outputType == null || fileName == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageToBlend = imageToBlend;
    this.outputType = OutputType.convertString(outputType);
    this.fileName = fileName;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (!layeredImages.containsKey(imageToBlend)) {
      throw new IllegalArgumentException("The layered image this command is supposed to blend does"
          + " not exist");
    }
    layeredImages.get(imageToBlend).saveAsImage(new BasicBlend(), outputType, fileName);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}

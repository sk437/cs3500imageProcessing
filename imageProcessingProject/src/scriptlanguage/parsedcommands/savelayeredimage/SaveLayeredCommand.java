package scriptlanguage.parsedcommands.savelayeredimage;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which saves  a layered image, as an image file
 * of a specified name.
 */
public class SaveLayeredCommand implements ParsedCommand {
  private final String imageToSave;
  private final String fileName;

  public SaveLayeredCommand(String imageToSave, String fileName) {
    if (imageToSave == null || fileName == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    this.imageToSave = imageToSave;
    this.fileName = fileName;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (!layeredImages.containsKey(this.imageToSave)) {
      throw new IllegalArgumentException("Image to save does not exist");
    }
    layeredImages.get(this.imageToSave).saveAsLayeredImage(this.fileName);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
    // This command does not impact the current state of the language
  }
}

package scriptlanguage.parsedcommands.save;

import imageasgraph.GraphOfPixels;
import imageasgraph.OutputType;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which saves either an image, or a layer of a layered image, as an image file
 * of a specified name and type.
 */
public class SaveCommand implements ParsedCommand {
  private final String imageToSave;
  private final String layerToSave;
  private final String fileName;
  private final OutputType outputType;

  /**
   * Creates a new SaveCommand, which will save the existing image with the given name, or the layer
   * with the given name of the existing layered image with the given name, as a file with the given
   * fileName.
   * @param imageToSave The name of the image to save, or the layered image which contains the layer
   *                    to be saved.
   * @param layerToSave The name of the layer to be saved, might be null if this command saves a
   *                    graph and not a layer of a layered image.
   * @param fileName The name of the file the image will be saved as
   * @throws IllegalArgumentException if given a null imageToSave or fileName
   */
  public SaveCommand(String imageToSave, String layerToSave, String fileName)  throws IllegalArgumentException {
    if (imageToSave == null || fileName == null) {
      throw new IllegalArgumentException("Null imageToSave or fileName");
    }
    this.imageToSave = imageToSave;
    this.layerToSave = layerToSave;
    this.fileName = fileName;
    String[] splitFile = fileName.split("\\.");
    String extension = splitFile[splitFile.length - 1];
    this.outputType = OutputType.convertString(extension);

  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs.containsKey(imageToSave)) {
      graphs.get(imageToSave).writeToFile(this.outputType, this.fileName);
    }
    else if (layeredImages.containsKey(imageToSave)) {
      if (layerToSave == null) {
        throw new IllegalArgumentException("Null layer");
      }
      layeredImages.get(imageToSave).getLayer(layerToSave).writeToFile(this.outputType, this.fileName);
    }
    else {
      throw new IllegalArgumentException("The image to be saved does not exist");
    }
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
    // This command does not impact the current state of the language
  }
}

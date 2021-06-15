package scriptlanguage.parsedcommands.save;

import imageasgraph.GraphOfPixels;
import imageasgraph.OutputType;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class SaveCommand implements ParsedCommand {
  private final String imageToSave;
  private final String layerToSave;
  private final String fileName;
  private final OutputType outputType;

  public SaveCommand(String imageToSave, String layerToSave, String fileName) {
    this.imageToSave = imageToSave;
    this.layerToSave = layerToSave;
    this.fileName = fileName;
    String[] splitFile = fileName.split("\\.");
    String extension = splitFile[splitFile.length - 1];
    this.outputType = OutputType.convertString(extension);

  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    if (graphs.containsKey(imageToSave)) {
      graphs.get(imageToSave).writeToFile(this.outputType, this.fileName);
    }
    else if (layeredImages.containsKey(imageToSave)) {
      layeredImages.get(imageToSave).getLayer(layerToSave).writeToFile(this.outputType, this.fileName);
    }
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}

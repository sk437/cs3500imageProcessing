package scriptlanguage.parsedcommands.creategraph;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.InputType;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class CreateFromImageCommand implements ParsedCommand {
  private final String imageName;
  private final GraphOfPixels processedFile;

  public CreateFromImageCommand(String imageName, String fileName) {
    this.imageName = imageName;
    String[] splitFile = fileName.split("\\.");
    String extension = splitFile[splitFile.length - 1];
    InputType input = InputType.convertString(extension);
    if (input.equals(InputType.ppm)) {
      this.processedFile = ImageToGraphConverter.convertPPM(fileName);
    }
    else {
      this.processedFile = ImageToGraphConverter.convertComplexImage(fileName);
    }
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    if (graphs.containsKey(imageName) || layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("There is already an image with that name");
    }
    graphs.put(imageName, processedFile);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}

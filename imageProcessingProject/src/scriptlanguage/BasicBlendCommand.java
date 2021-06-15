package scriptlanguage;

import imageasgraph.GraphOfPixels;
import imageasgraph.OutputType;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.blend.BasicBlend;
import scriptlanguage.parsedcommands.ParsedCommand;

public class BasicBlendCommand implements ParsedCommand {
  private final String imageToBlend;
  private final OutputType outputType;
  private final String fileName;

  public BasicBlendCommand(String imageToBlend, String outputType, String fileName) {
    this.imageToBlend = imageToBlend;
    this.outputType = OutputType.convertString(outputType);
    this.fileName = fileName;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    layeredImages.get(imageToBlend).saveAsImage(new BasicBlend(), outputType, fileName);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}

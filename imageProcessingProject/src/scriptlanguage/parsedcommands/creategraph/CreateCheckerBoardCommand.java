package scriptlanguage.parsedcommands.creategraph;

import imageInput.imageprogram.CheckerBoard;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import pixel.SimplePixel;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class CreateCheckerBoardCommand implements ParsedCommand {
  private final GraphOfPixels checkerBoard;
  private final String imageName;

  public CreateCheckerBoardCommand(String imageName, int tileSize, int numTiles, int r0, int g0,
      int b0, int r1, int g1, int b1) {
    if (imageName == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.checkerBoard = ImageToGraphConverter.convertProgram(new CheckerBoard(tileSize, numTiles, new SimplePixel(r0, g0, b0),
        new SimplePixel(r1, g1, b1)));
    this.imageName = imageName;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    if (graphs.containsKey(imageName) || layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("There is already an image with that name");
    }
    graphs.put(imageName, checkerBoard);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}

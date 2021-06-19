package scriptlanguage.parsedcommands.creategraph;

import imageInput.CheckerBoard;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import pixel.SimplePixel;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command to create a new graph image from a specified checkerboard command.
 */
public class CreateCheckerBoardCommand implements ParsedCommand {

  private final GraphOfPixels checkerBoard;
  private final String imageName;

  /**
   * Creates a new CheckerboardImage command, which will add the checkerboard specified by the
   * integer parameters and save it as a new image with the given name.
   *
   * @param imageName The name this image will be saved by
   * @param tileSize  The size of the tiles in this checkerboard
   * @param numTiles  The number of tiles in this checkerboard
   * @param r0        The red value of one color of the checkerboard
   * @param g0        The green value of one color of the checkerboard
   * @param b0        The blue value of one color of the checkerboard
   * @param r1        The red value of the other color of the checkerboard
   * @param g1        The green value of the other color of the checkerboard
   * @param b1        The blue value of the other color of the checkerboard
   * @throws IllegalArgumentException If given a null imageName
   */
  public CreateCheckerBoardCommand(String imageName, int tileSize, int numTiles, int r0, int g0,
      int b0, int r1, int g1, int b1) throws IllegalArgumentException {
    if (imageName == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.checkerBoard = ImageToGraphConverter
        .convertProgram(new CheckerBoard(tileSize, numTiles, new SimplePixel(r0, g0, b0),
            new SimplePixel(r1, g1, b1)));
    this.imageName = imageName;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (graphs.containsKey(imageName) || layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("There is already an image with that name");
    }
    graphs.put(imageName, checkerBoard);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}

package scriptlanguage.parsedcommands.updatecolor;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import pixel.SimplePixel;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which updates the color of a pixel within an image, or within a layer of a
 * layered image.
 */
public class UpdateColorCommand implements ParsedCommand {
  private final String imageToUpdate;
  private final String layerToUpdate;
  private final int pixelX;
  private final int pixelY;
  private final int newOpacity;
  private final SimplePixel newColors;

  /**
   * Creates a new UpdateColorCommand, which will update the pixel at the specified coordinates of the
   * image with the given name, or the layer with the given name of the layered image with the given name,
   * to be as specified.
   * @param imageToUpdate The name of the image, or layered image containing the layer, to modify
   * @param layerToUpdate The name of the layer to modify, may be null if this command modifies
   *                      a non-layered image
   * @param pixelX The x coordinate of the pixel to be updated
   * @param pixelY The y coordinate of teh pixel to be updated
   * @param newOpacity The value to set the opacity of the pixel to
   * @param newR The value to set the red value of the pixel to
   * @param newG The value to set the green value of the pixel to
   * @param newB THe value to set the blue value of the pixel to
   * @throws IllegalArgumentException If given a null imageToUpdate
   */
  public UpdateColorCommand(String imageToUpdate, String layerToUpdate, int pixelX, int pixelY,
      int newOpacity, int newR, int newG, int newB) throws IllegalArgumentException {
    if (imageToUpdate == null) {
      throw new IllegalArgumentException("Null imageTOUpdate");
    }
    this.imageToUpdate = imageToUpdate;
    this.layerToUpdate = layerToUpdate;
    this.pixelX = pixelX;
    this.pixelY = pixelY;
    this.newOpacity = newOpacity;
    this.newColors = new SimplePixel(newR, newG, newB);
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (graphs.containsKey(imageToUpdate)) {
      graphs.get(imageToUpdate).getPixelAt(pixelX, pixelY).updateColors(newColors);
      graphs.get(imageToUpdate).getPixelAt(pixelX, pixelY).setOpacity(newOpacity);
    }
    else if (layeredImages.containsKey(imageToUpdate)) {
      if (layerToUpdate == null) {
        throw new IllegalArgumentException("Null layer");
      }
      layeredImages.get(imageToUpdate).getLayer(layerToUpdate).getPixelAt(pixelX, pixelY).updateColors(newColors);
      layeredImages.get(imageToUpdate).getLayer(layerToUpdate).getPixelAt(pixelX, pixelY).setOpacity(newOpacity);
    }
    else {
      throw new IllegalArgumentException("The image this command is supposed to modify does not exist");
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

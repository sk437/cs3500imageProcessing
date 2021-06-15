package scriptlanguage.parsedcommands.updatecolor;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import pixel.SimplePixel;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class UpdateColorCommand implements ParsedCommand {
  private final String imageToUpdate;
  private final String layerToUpdate;
  private final int pixelX;
  private final int pixelY;
  private final int newOpacity;
  private final SimplePixel newColors;

  public UpdateColorCommand(String imageToUpdate, String layerToUpdate, int pixelX, int pixelY,
      int newOpacity, int newR, int newG, int newB) {
    this.imageToUpdate = imageToUpdate;
    this.layerToUpdate = layerToUpdate;
    this.pixelX = pixelX;
    this.pixelY = pixelY;
    this.newOpacity = newOpacity;
    this.newColors = new SimplePixel(newR, newG, newB);
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    if (graphs.containsKey(imageToUpdate)) {
      graphs.get(imageToUpdate).getPixelAt(pixelX, pixelY).updateColors(newColors);
      graphs.get(imageToUpdate).getPixelAt(pixelX, pixelY).setOpacity(newOpacity);
    }
    else if (layeredImages.containsKey(imageToUpdate)) {
      layeredImages.get(imageToUpdate).getLayer(layerToUpdate).getPixelAt(pixelX, pixelY).updateColors(newColors);
      layeredImages.get(imageToUpdate).getLayer(layerToUpdate).getPixelAt(pixelX, pixelY).setOpacity(newOpacity);
    }
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}

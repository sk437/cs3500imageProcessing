package scriptlanguage.parsedcommands.loadlayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class LoadLayerCommand implements ParsedCommand {
  private final String layerToLoad;

  public LoadLayerCommand(String layerToLoad) {
    this.layerToLoad = layerToLoad;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    //Execution does nothing, as this command only alters the state of the language
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    toAlter.setCurrentLayer(layerToLoad);
  }
}

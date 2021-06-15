package scriptlanguage.parsedcommands.load;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class LoadCommand implements ParsedCommand {
  private final String imageToLoad;

  public LoadCommand(String imageToLoad) {
    this.imageToLoad = imageToLoad;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    //Execution does nothing, as this command only alters the state of the language
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    toAlter.setCurrentImage(imageToLoad);
    toAlter.setCurrentLayer(null);
  }
}

package scriptlanguage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Defines the language which parses commands given by an input script, and executes the command
 * given in that script on a given model.
 */
public class LanguageSyntaxImpl implements LanguageSyntax {
  private static final HashMap<String, Command> commands;
  private String currentImage = null;
  private String currentLayer = null;
  static {
    HashMap<String, Command> temp = new HashMap<String, Command>();
    temp.put("create-image", Command.createImage);
    temp.put("create-layered-image", Command.createLayeredImage);
    temp.put("update-color", Command.updateColor);
    temp.put("apply-mutator", Command.applyMutator);
    temp.put("save", Command.save);
    temp.put("load", Command.load);
    temp.put("set-current-layer", Command.setCurrentLayer);
    temp.put("add-layer", Command.addLayer);
    temp.put("copy-layer", Command.copyLayer);
    temp.put("add-image-as-layer", Command.addImageAsLayer);
    temp.put("move-layer", Command.moveLayer);
    temp.put("remove-layer", Command.removeLayer);
    temp.put("save-as-image", Command.saveAsImage);
    temp.put("update-visibility", Command.updateVisibility);
    commands = temp;
  }

  @Override
  public ParsedCommand parseCommand(String inputLine) {
    ArrayList<String> inputs = new ArrayList<String>(Arrays.asList(inputLine.split(" ")));
    String cmd = inputs.remove(0);
    ParsedCommand toReturn = commands.get(cmd).returnExecutable(inputs, this.currentImage, this.currentLayer);
    toReturn.alterLanguageState(this);
    return toReturn;
  }

  @Override
  public void setCurrentImage(String newImage) {
    this.currentImage = newImage;
  }

  @Override
  public void setCurrentLayer(String newLayer) {
    this.currentLayer = newLayer;
  }
}

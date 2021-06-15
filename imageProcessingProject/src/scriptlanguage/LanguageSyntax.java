package scriptlanguage;

import scriptlanguage.parsedcommands.ParsedCommand;

public interface LanguageSyntax {
  ParsedCommand parseCommand(String inputLine);
  void setCurrentImage(String newImage);
  void setCurrentLayer(String newLayer);
}

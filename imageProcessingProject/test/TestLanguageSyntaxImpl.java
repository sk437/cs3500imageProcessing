import static org.junit.Assert.assertTrue;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.addimagelayer.AddImageLayerCommand;
import scriptlanguage.parsedcommands.addlayer.AddLayerCommand;
import scriptlanguage.parsedcommands.applymutator.BlurCommand;
import scriptlanguage.parsedcommands.applymutator.GreyscaleCommand;
import scriptlanguage.parsedcommands.applymutator.SepiaCommand;
import scriptlanguage.parsedcommands.applymutator.SharpenCommand;
import scriptlanguage.parsedcommands.blend.BasicBlendCommand;
import scriptlanguage.parsedcommands.copylayer.CopyLayerCommand;
import scriptlanguage.parsedcommands.creategraph.CreateCheckerBoardCommand;
import scriptlanguage.parsedcommands.creategraph.CreateCopyCommand;
import scriptlanguage.parsedcommands.creategraph.CreateEmptyImageCommand;
import scriptlanguage.parsedcommands.creategraph.CreateFromImageCommand;
import scriptlanguage.parsedcommands.creategraph.CreateTransparentCommand;
import scriptlanguage.parsedcommands.createlayered.CreateNewLayeredImageCommand;
import scriptlanguage.parsedcommands.createlayered.ImportNewLayeredImageCommand;
import scriptlanguage.parsedcommands.load.LoadCommand;
import scriptlanguage.parsedcommands.loadlayer.LoadLayerCommand;
import scriptlanguage.parsedcommands.movelayer.MoveLayerCommand;
import scriptlanguage.parsedcommands.removelayer.RemoveLayerByNameCommand;
import scriptlanguage.parsedcommands.save.SaveCommand;
import scriptlanguage.parsedcommands.savelayeredimage.SaveLayeredCommand;
import scriptlanguage.parsedcommands.updatecolor.UpdateColorCommand;
import scriptlanguage.parsedcommands.updatevisibility.UpdateVisibilityCommand;

/**
 * Tests whether commands are parsed correctly and correct function objects are returned.
 */
public class TestLanguageSyntaxImpl {

  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private LanguageSyntax test;

  private String createFromImage;
  private String createTransparent;
  private String createCheckerboard;
  private String createEmpty;
  private String createCopy;

  private String createLayeredImageFromFile;
  private String createLayeredImageBlank;

  private String updateColorGraphImage;
  private String updateColorLayered;
  private String updateColorCurrentImage;
  private String updateColorAllCurrents;

  private String applyMutatorBlur;
  private String applyMutatorSharpen;
  private String applyMutatorSepia;
  private String applyMutatorGreyscale;

  private String saveImage;
  private String saveLayer;
  private String saveLayered;
  private String saveAsImage;

  private String load;
  private String setCurrentLayer;

  private String addLayer;
  private String copyLayer;
  private String addImageAsLayer;
  private String moveLayer;
  private String removeLayer;

  private String updateVisibility;

  /**
   * Initializes many possible String commands.
   */
  public void setUp() {
    graphs = new HashMap<String, GraphOfPixels>();

    layeredImages = new HashMap<String, LayeredImage>();

    test = new LanguageSyntaxImpl();

    createFromImage = "create-image from-image new outputImages/birb.jpg";
    createTransparent = "create-image transparent new 5 7";
    createCheckerboard = "create-image checkerboard new 1 36 0 0 0 255 255 255";
    createEmpty = "create-image empty new";
    createCopy = "create-image copy existingCopy existingImage";

    createLayeredImageFromFile = "create-layered-image newLayered outputImages/misc";
    createLayeredImageBlank = "create-layered-image newLayered 1024 768";

    updateColorGraphImage = "update-color existingImage none 500 500 123 50 50 50";
    updateColorLayered = "update-color existingLayered birb 500 500 123 50 50 50";
    updateColorCurrentImage = "update-color existingImage 500 500 123 50 50 50";
    updateColorAllCurrents = "update-color 500 500 123 50 50 50";

    applyMutatorBlur = "apply-mutator blur existingImage none";
    applyMutatorSharpen = "apply-mutator sharpen existingImage none";
    applyMutatorSepia = "apply-mutator sepia existingImage none";
    applyMutatorGreyscale = "apply-mutator greyscale existingImage none";

    saveImage = "save existingImage none png outputImages/birbNEW";
    saveLayer = "save existingLayered birb png outputImages/birbLayerNEW";
    saveLayered = "save-layered existingLayered outputImages/misc4";
    saveAsImage = "save-as-image existingLayered basic png outputImages/misc4";

    load = "load existingLayered";
    setCurrentLayer = "set-current-layer birb";

    addLayer = "add-layer existingLayered new";
    copyLayer = "copy-layer existingLayered new birb";
    addImageAsLayer = "add-image-as-layer existingLayered vulture outputImages/vulture.png";
    moveLayer = "move-layer existingLayered birb 0";
    removeLayer = "remove-layer existingLayered galaxy";

    updateVisibility = "update-visibility existingLayered rainbow false";
  }

  @Test
  public void testParseCommand() {
    this.setUp();
    assertTrue(test.parseCommand(createFromImage) instanceof CreateFromImageCommand);
    assertTrue(test.parseCommand(createTransparent) instanceof CreateTransparentCommand);
    assertTrue(test.parseCommand(createCheckerboard) instanceof CreateCheckerBoardCommand);
    assertTrue(test.parseCommand(createEmpty) instanceof CreateEmptyImageCommand);
    assertTrue(test.parseCommand(createCopy) instanceof CreateCopyCommand);

    assertTrue(
        test.parseCommand(createLayeredImageFromFile) instanceof ImportNewLayeredImageCommand);
    assertTrue(test.parseCommand(createLayeredImageBlank) instanceof CreateNewLayeredImageCommand);

    assertTrue(test.parseCommand(updateColorGraphImage) instanceof UpdateColorCommand);
    assertTrue(test.parseCommand(updateColorLayered) instanceof UpdateColorCommand);
    test.parseCommand(load).alterLanguageState(test);
    test.parseCommand(setCurrentLayer).alterLanguageState(test);
    assertTrue(test.parseCommand(updateColorCurrentImage) instanceof UpdateColorCommand);
    assertTrue(test.parseCommand(updateColorAllCurrents) instanceof UpdateColorCommand);

    assertTrue(test.parseCommand(applyMutatorBlur) instanceof BlurCommand);
    assertTrue(test.parseCommand(applyMutatorSharpen) instanceof SharpenCommand);
    assertTrue(test.parseCommand(applyMutatorSepia) instanceof SepiaCommand);
    assertTrue(test.parseCommand(applyMutatorGreyscale) instanceof GreyscaleCommand);

    assertTrue(test.parseCommand(saveImage) instanceof SaveCommand);
    assertTrue(test.parseCommand(saveLayer) instanceof SaveCommand);
    assertTrue(test.parseCommand(saveLayered) instanceof SaveLayeredCommand);
    assertTrue(test.parseCommand(saveAsImage) instanceof BasicBlendCommand);

    assertTrue(test.parseCommand(load) instanceof LoadCommand);
    assertTrue(test.parseCommand(setCurrentLayer) instanceof LoadLayerCommand);

    assertTrue(test.parseCommand(addLayer) instanceof AddLayerCommand);
    assertTrue(test.parseCommand(copyLayer) instanceof CopyLayerCommand);
    assertTrue(test.parseCommand(addImageAsLayer) instanceof AddImageLayerCommand);
    assertTrue(test.parseCommand(moveLayer) instanceof MoveLayerCommand);
    assertTrue(test.parseCommand(removeLayer) instanceof RemoveLayerByNameCommand);

    assertTrue(test.parseCommand(updateVisibility) instanceof UpdateVisibilityCommand);

  }


  @Test(expected = IllegalArgumentException.class)
  public void testNullInput() {
    this.setUp();
    test.parseCommand(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyInput() {
    this.setUp();
    test.parseCommand("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCommandInput() {
    this.setUp();
    test.parseCommand("non-existant-command existingImage 500 5271 2470");
  }

  @Test
  public void setCurrentImage() {
    this.setUp();
    graphs.put("existingImage", ImageToGraphConverter.convertImage("outputImages/birb.jpg"));
    layeredImages.put("existingLayered", new LayeredImageV0("outputImages/misc"));
    //If current gets properly assigned, no error message should be thrown in any of these.
    //The scripts would throw errors if there were null values left for the current variables.
    test.parseCommand(setCurrentLayer).alterLanguageState(test);

    test.parseCommand(updateColorCurrentImage).execute(graphs, layeredImages);
  }

  @Test
  public void setCurrentLayer() {
    this.setUp();
    graphs.put("existingImage", ImageToGraphConverter.convertImage("outputImages/birb.jpg"));
    layeredImages.put("existingLayered", new LayeredImageV0("outputImages/misc"));
    //If current gets properly assigned, no error message should be thrown in any of these.
    //The scripts would throw errors if there were null values left for the current variables.
    test.parseCommand(load).alterLanguageState(test);

    test.parseCommand(setCurrentLayer).alterLanguageState(test);

    test.parseCommand(updateColorAllCurrents).execute(graphs, layeredImages);
  }
}

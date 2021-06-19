import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import scriptlanguage.Command;
import scriptlanguage.ParsedCommand.AddImageLayerCommand;
import scriptlanguage.ParsedCommand.AddLayerCommand;
import scriptlanguage.ParsedCommand.BlurCommand;
import scriptlanguage.ParsedCommand.GreyscaleCommand;
import scriptlanguage.ParsedCommand.SepiaCommand;
import scriptlanguage.ParsedCommand.SharpenCommand;
import scriptlanguage.ParsedCommand.BasicBlendCommand;
import scriptlanguage.ParsedCommand.CopyLayerCommand;
import scriptlanguage.ParsedCommand.CreateCheckerBoardCommand;
import scriptlanguage.ParsedCommand.CreateCopyCommand;
import scriptlanguage.ParsedCommand.CreateEmptyImageCommand;
import scriptlanguage.ParsedCommand.CreateFromImageCommand;
import scriptlanguage.ParsedCommand.CreateTransparentCommand;
import scriptlanguage.ParsedCommand.CreateNewLayeredImageCommand;
import scriptlanguage.ParsedCommand.ImportNewLayeredImageCommand;
import scriptlanguage.ParsedCommand.LoadCommand;
import scriptlanguage.ParsedCommand.LoadLayerCommand;
import scriptlanguage.ParsedCommand.MoveLayerCommand;
import scriptlanguage.ParsedCommand.RemoveLayerByNameCommand;
import scriptlanguage.ParsedCommand.SaveCommand;
import scriptlanguage.ParsedCommand.SaveLayeredCommand;
import scriptlanguage.ParsedCommand.UpdateColorCommand;
import scriptlanguage.ParsedCommand.UpdateVisibilityCommand;

/**
 * Tests that given specific inputs, correct Command objects are returned.
 */
public class TestCommand {

  //TESTING CREATING IMAGE COMMANDS.

  @Test
  public void testCreateImage() {
    List<String> checkerboardInputs = new ArrayList<String>(
        Arrays.asList("checkerboard", "new", "1", "36", "0", "0", "0", "255", "255", "255"));
    assertTrue(Command.createImage.returnExecutable(
        checkerboardInputs, null, null) instanceof CreateCheckerBoardCommand);

    List<String> emptyInputs = new ArrayList<String>(
        Arrays.asList("empty", "new"));
    assertTrue(Command.createImage.returnExecutable(
        emptyInputs, null, null) instanceof CreateEmptyImageCommand);

    List<String> transparentInputs = new ArrayList<String>(
        Arrays.asList("transparent", "new", "5", "7"));
    assertTrue(Command.createImage.returnExecutable(
        transparentInputs, null, null) instanceof CreateTransparentCommand);

    List<String> copyInputs = new ArrayList<String>(
        Arrays.asList("copy", "existingCopy", "existingImage"));
    assertTrue(
        Command.createImage.returnExecutable(copyInputs, null, null) instanceof CreateCopyCommand);

    List<String> fromImageInputs = new ArrayList<String>(
        Arrays.asList("from-image", "new", "outputImages/birb.jpg"));
    assertTrue(Command.createImage.returnExecutable(
        fromImageInputs, null, null) instanceof CreateFromImageCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateCheckerboardInvalidNumbers() {
    List<String> checkerboardInputs = new ArrayList<String>(
        Arrays.asList("checkerboard", "new", "NOT", "36", "VALID", "0",
            "INT", "255", "HERE", "255"));
    Command.createImage.returnExecutable(checkerboardInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateTransparentInvalidNumbers() {
    List<String> transparentInputs = new ArrayList<String>(
        Arrays.asList("transparent", "new", "5", "NOT VALID"));
    Command.createImage.returnExecutable(transparentInputs, null, null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateImageNullList() {
    Command.createImage.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateImageNullElementInList() {
    List<String> fromImageInputs = new ArrayList<String>(
        Arrays.asList("from-image", null, "outputImages/birb.jpg"));
    Command.createImage.returnExecutable(fromImageInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateImageUnrecognizedImageType() {
    List<String> fromImageInputs = new ArrayList<String>(
        Arrays.asList("UNKNOWN", "new", "outputImages/birb.jpg"));
    Command.createImage.returnExecutable(fromImageInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateCheckerboardTooLittleInputs() {
    List<String> checkerboardInputs = new ArrayList<String>(
        Arrays.asList("checkerboard", "new", "255", "255", "255"));

    Command.createImage.returnExecutable(checkerboardInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateCheckerboardTooManyInputs() {
    List<String> checkerboardInputs = new ArrayList<String>(
        Arrays.asList("checkerboard", "new", "too", "many", "1", "36", "0", "0", "0", "255", "255",
            "255"));
    Command.createImage.returnExecutable(checkerboardInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateEmptyTooLittleInputs() {
    List<String> emptyInputs = new ArrayList<String>(
        Collections.singletonList("empty"));

    Command.createImage.returnExecutable(emptyInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateEmptyTooManyInputs() {
    List<String> emptyInputs = new ArrayList<String>(
        Arrays.asList("empty", "new", "hi"));
    Command.createImage.returnExecutable(emptyInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateTransparentTooLittleInputs() {
    List<String> transparentInputs = new ArrayList<String>(
        Arrays.asList("transparent", "new", "7"));

    Command.createImage.returnExecutable(transparentInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateTransparentTooManyInputs() {
    List<String> transparentInputs = new ArrayList<String>(
        Arrays.asList("transparent", "new", "EXTRA", "5", "7"));
    Command.createImage.returnExecutable(transparentInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateCopyTooLittleInputs() {
    List<String> copyInputs = new ArrayList<String>(
        Arrays.asList("copy", "existingImage"));

    Command.createImage.returnExecutable(copyInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateCopyTooManyInputs() {
    List<String> copyInputs = new ArrayList<String>(
        Arrays.asList("copy", "EXTRA", "existingCopy", "existingImage"));
    Command.createImage.returnExecutable(copyInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateFromTooLittleInputs() {
    List<String> fromImageInputs = new ArrayList<String>(
        Arrays.asList("from-image", "new"));

    Command.createImage.returnExecutable(fromImageInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateFromTooManyInputs() {
    List<String> fromImageInputs = new ArrayList<String>(
        Arrays.asList("from-image", "new", "EXTRA", "outputImages/birb.jpg"));
    Command.createImage.returnExecutable(fromImageInputs, null, null);
  }

  //TESTING CREATE LAYERED IMAGES COMMANDS.

  @Test
  public void testCreateLayeredImage() {
    List<String> fromInputs = new ArrayList<String>(
        Arrays.asList("newLayered", "outputImages/misc"));
    assertTrue(Command.createLayeredImage
        .returnExecutable(fromInputs, null, null) instanceof ImportNewLayeredImageCommand);

    List<String> createNewInputs = new ArrayList<String>(
        Arrays.asList("newLayered", "1024", "768"));
    assertTrue(Command.createLayeredImage
        .returnExecutable(createNewInputs, null, null) instanceof CreateNewLayeredImageCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateLayeredInvalidNumbers() {
    List<String> createNewInputs = new ArrayList<String>(
        Arrays.asList("newLayered", "NOT", "768"));
    Command.createLayeredImage.returnExecutable(createNewInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateLayeredNullList() {
    Command.createLayeredImage.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateLayeredNullElementInList() {
    List<String> fromInputs = new ArrayList<String>(
        Arrays.asList(null, "outputImages/misc"));
    Command.createLayeredImage.returnExecutable(fromInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateLayeredTooLittleInputs() {
    List<String> fromInputs = new ArrayList<String>(
        Collections.singletonList("newLayered"));
    Command.createLayeredImage.returnExecutable(fromInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCreateLayeredTooManyInputs() {
    List<String> fromInputs = new ArrayList<String>(
        Arrays.asList("newLayered", "EXTRA", "outputImages/misc"));
    Command.createLayeredImage.returnExecutable(fromInputs, null, null);
  }

  //TESTING UPDATING COLOR COMMANDS.


  @Test
  public void testUpdateColor() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("existingImage", "none", "500", "500", "123", "50", "50", "50"));
    assertTrue(Command.updateColor
        .returnExecutable(noCurrentsSingle, null, null) instanceof UpdateColorCommand);

    List<String> noCurrentsLayered = new ArrayList<String>(
        Arrays.asList("existingImage", "birb", "500", "500", "123", "50", "50", "50"));
    assertTrue(Command.updateColor
        .returnExecutable(noCurrentsLayered, null, null) instanceof UpdateColorCommand);

    List<String> currentLayer = new ArrayList<String>(
        Arrays.asList("existingImage", "500", "500", "123", "50", "50", "50"));
    assertTrue(Command.updateColor
        .returnExecutable(currentLayer, null, "birb") instanceof UpdateColorCommand);

    List<String> allCurrents = new ArrayList<String>(
        Arrays.asList("500", "500", "123", "50", "50", "50"));
    assertTrue(Command.updateColor
        .returnExecutable(allCurrents, "existingImage", "birb") instanceof UpdateColorCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateColorNoCurrent() {
    List<String> allCurrents = new ArrayList<String>(
        Arrays.asList("500", "500", "123", "50", "50", "50"));
    Command.applyMutator.returnExecutable(allCurrents, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateColorNoCurrentLayer() {
    List<String> currentLayer = new ArrayList<String>(
        Arrays.asList("existingImage", "500", "500", "123", "50", "50", "50"));
    Command.applyMutator.returnExecutable(currentLayer, "existingImage", null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateColorsInvalidNumbers() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("existingImage", "none", "NOT", "A", "VALID", "INTEGER", "HERE", "50"));
    Command.updateColor.returnExecutable(noCurrentsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateColorsInvalidNumbersCurrent() {
    List<String> allCurrents = new ArrayList<String>(
        Arrays.asList("NOT", "A", "VALID", "INTEGER", "HERE", "50"));
    Command.updateColor.returnExecutable(allCurrents, "existingImage", "birb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateColorsInvalidNumbersCurrentLayer() {
    List<String> currentLayer = new ArrayList<String>(
        Arrays.asList("existingImage", "NOT", "A", "VALID", "INTEGER", "HERE", "50"));
    Command.updateColor.returnExecutable(currentLayer, null, "birb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateColorNullList() {
    Command.updateColor.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateColorNullElementInList() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("existingImage", null, "500", "500", "123", "50", "50", "50"));
    Command.updateColor.returnExecutable(noCurrentsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateColorTooLittleInputs() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Collections.singletonList("existingImage"));
    Command.updateColor.returnExecutable(noCurrentsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateColorTooManyInputs() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("existingImage", "none", "EXTRA", "500", "500", "123", "50", "50", "50"));
    Command.updateColor.returnExecutable(noCurrentsSingle, null, null);
  }

  //TESTING APPLY MUTATOR COMMANDS.

  @Test
  public void testApplyMutator() {
    List<String> blurInputsSingle = new ArrayList<String>(
        Arrays.asList("blur", "existingImage", "none"));
    assertTrue(
        Command.applyMutator.returnExecutable(blurInputsSingle, null, null) instanceof BlurCommand);

    List<String> blurInputsLayered = new ArrayList<String>(
        Arrays.asList("blur", "existingImage", "birb"));
    assertTrue(Command.applyMutator
        .returnExecutable(blurInputsLayered, null, null) instanceof BlurCommand);

    List<String> blurInputsCurrentLayer = new ArrayList<String>(
        Arrays.asList("blur", "existingImage"));
    assertTrue(Command.applyMutator
        .returnExecutable(blurInputsCurrentLayer, null, "birb") instanceof BlurCommand);

    List<String> blurInputsAllCurrents = new ArrayList<String>(
        Collections.singletonList("blur"));
    assertTrue(Command.applyMutator
        .returnExecutable(blurInputsAllCurrents, "existingImage", "birb") instanceof BlurCommand);

    List<String> sharpenInputsSingle = new ArrayList<String>(
        Arrays.asList("sharpen", "existingImage", "none"));
    assertTrue(Command.applyMutator
        .returnExecutable(sharpenInputsSingle, null, null) instanceof SharpenCommand);

    List<String> sharpenInputsLayered = new ArrayList<String>(
        Arrays.asList("sharpen", "existingImage", "birb"));
    assertTrue(Command.applyMutator
        .returnExecutable(sharpenInputsLayered, null, null) instanceof SharpenCommand);

    List<String> sharpenInputsCurrentLayer = new ArrayList<String>(
        Arrays.asList("sharpen", "existingImage"));
    assertTrue(Command.applyMutator
        .returnExecutable(sharpenInputsCurrentLayer, null, "birb") instanceof SharpenCommand);

    List<String> sharpenInputsAllCurrents = new ArrayList<String>(
        Collections.singletonList("sharpen"));
    assertTrue(Command.applyMutator.returnExecutable(sharpenInputsAllCurrents, "existingImage",
        "birb") instanceof SharpenCommand);

    List<String> sepiaInputsSingle = new ArrayList<String>(
        Arrays.asList("sepia", "existingImage", "none"));
    assertTrue(Command.applyMutator
        .returnExecutable(sepiaInputsSingle, null, null) instanceof SepiaCommand);

    List<String> sepiaInputsLayered = new ArrayList<String>(
        Arrays.asList("sepia", "existingImage", "none"));
    assertTrue(Command.applyMutator
        .returnExecutable(sepiaInputsLayered, null, null) instanceof SepiaCommand);

    List<String> sepiaInputsCurrentLayer = new ArrayList<String>(
        Arrays.asList("sepia", "existingImage"));
    assertTrue(Command.applyMutator
        .returnExecutable(sepiaInputsCurrentLayer, null, "birb") instanceof SepiaCommand);

    List<String> sepiaInputsAllCurrents = new ArrayList<String>(
        Collections.singletonList("sepia"));
    assertTrue(Command.applyMutator
        .returnExecutable(sepiaInputsAllCurrents, "existingImage", "birb") instanceof SepiaCommand);

    List<String> greyscaleInputsSingle = new ArrayList<String>(
        Arrays.asList("greyscale", "existingCopy", "existingImage"));
    assertTrue(Command.applyMutator
        .returnExecutable(greyscaleInputsSingle, null, null) instanceof GreyscaleCommand);

    List<String> greyscaleInputsLayered = new ArrayList<String>(
        Arrays.asList("greyscale", "existingImage", "none"));
    assertTrue(Command.applyMutator
        .returnExecutable(greyscaleInputsLayered, null, null) instanceof GreyscaleCommand);

    List<String> greyscaleInputsCurrentLayer = new ArrayList<String>(
        Arrays.asList("greyscale", "existingImage"));
    assertTrue(Command.applyMutator
        .returnExecutable(greyscaleInputsCurrentLayer, null, "birb") instanceof GreyscaleCommand);

    List<String> greyscaleInputsAllCurrents = new ArrayList<String>(
        Collections.singletonList("greyscale"));
    assertTrue(Command.applyMutator.returnExecutable(greyscaleInputsAllCurrents, "existingImage",
        "birb") instanceof GreyscaleCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailBlurNoCurrentImage() {
    List<String> blurInputsSingle = new ArrayList<String>(
        Collections.singletonList("blur"));
    Command.applyMutator.returnExecutable(blurInputsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSharpenNoCurrentImage() {
    List<String> sharpenInputsSingle = new ArrayList<String>(
        Collections.singletonList("sharpen"));
    Command.applyMutator.returnExecutable(sharpenInputsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSepiaNoCurrentImage() {
    List<String> sepiaInputsSingle = new ArrayList<String>(
        Collections.singletonList("sepia"));
    Command.applyMutator.returnExecutable(sepiaInputsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailGreyscaleNoCurrentImage() {
    List<String> greyscaleInputsSingle = new ArrayList<String>(
        Collections.singletonList("greyscale"));
    Command.applyMutator.returnExecutable(greyscaleInputsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailApplyMutatorNullList() {
    Command.applyMutator.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailApplyMutatorNullElementInList() {
    List<String> blurInputsSingle = new ArrayList<String>(
        Arrays.asList("blur", null, "none"));
    Command.applyMutator.returnExecutable(blurInputsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailApplyMutatorUnrecognizedMutatorType() {
    List<String> blurInputsSingle = new ArrayList<String>(
        Arrays.asList("UNKNOWN", "existingImage", "none"));
    Command.applyMutator.returnExecutable(blurInputsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailApplyMutatorTooLittleInputs() {
    List<String> noInputs = new ArrayList<String>();
    Command.applyMutator.returnExecutable(noInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailBlurTooManyInputs() {
    List<String> blurInputs = new ArrayList<String>(
        Arrays.asList("blur", "EXTRA", "existingImage", "none"));
    Command.applyMutator.returnExecutable(blurInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSharpenTooManyInputs() {
    List<String> sharpenInputs = new ArrayList<String>(
        Arrays.asList("sharpen", "EXTRA", "new", "hi"));
    Command.applyMutator.returnExecutable(sharpenInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSepiaTooManyInputs() {
    List<String> sepiaInputs = new ArrayList<String>(
        Arrays.asList("sepia", "EXTRA", "existingImage", "none"));
    Command.applyMutator.returnExecutable(sepiaInputs, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailGreyscaleTooManyInputs() {
    List<String> greyscaleInputs = new ArrayList<String>(
        Arrays.asList("greyscale", "EXTRA", "existingCopy", "existingImage"));
    Command.applyMutator.returnExecutable(greyscaleInputs, null, null);
  }

  //TESTING SAVE COMMAND INPUTS

  @Test
  public void testSave() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("existingImage", "none", "png", "outputImages/birbNEW"));
    assertTrue(Command.save.returnExecutable(noCurrentsSingle, null, null) instanceof SaveCommand);

    List<String> noCurrentsLayered = new ArrayList<String>(
        Arrays.asList("existingImage", "birb", "png", "outputImages/birbLayerNEW"));
    assertTrue(Command.save.returnExecutable(noCurrentsLayered, null, null) instanceof SaveCommand);

    List<String> currentLayer = new ArrayList<String>(
        Arrays.asList("existingImage", "png", "outputImages/birbNEW"));
    assertTrue(Command.save.returnExecutable(currentLayer, null, "birb") instanceof SaveCommand);

    List<String> allCurrents = new ArrayList<String>(
        Arrays.asList("png", "outputImages/birbNEW"));
    assertTrue(
        Command.save.returnExecutable(allCurrents, "existingImage", "birb") instanceof SaveCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveNoCurrentImage() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("png", "outputImages/birbNEW"));
    Command.save.returnExecutable(noCurrentsSingle, null, "birb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveNullList() {
    Command.save.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveNullElementInList() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("existingImage", "none", null, "outputImages/birbNEW"));
    Command.save.returnExecutable(noCurrentsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveTooLittleInputs() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Collections.singletonList("existingImage"));
    Command.save.returnExecutable(noCurrentsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveTooManyInputs() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("existingImage", "none", "png", "EXTRA", "outputImages/birbNEW"));
    Command.save.returnExecutable(noCurrentsSingle, null, null);
  }

  //TESTING SAVE LAYERED COMMANDS.

  @Test
  public void testSaveLayered() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("existingImage", "outputImages/misc4"));
    assertTrue(Command.saveLayered
        .returnExecutable(noCurrentsSingle, null, null) instanceof SaveLayeredCommand);

    List<String> currentImage = new ArrayList<String>(
        Collections.singletonList("outputImages/misc4"));
    assertTrue(Command.saveLayered
        .returnExecutable(currentImage, "existingImage", "birb") instanceof SaveLayeredCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredNullList() {
    Command.saveLayered.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredNullElementInList() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("existingImage", null));
    Command.saveLayered.returnExecutable(noCurrentsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredTooLittleInputs() {
    List<String> noCurrentsSingle = new ArrayList<String>();
    Command.saveLayered.returnExecutable(noCurrentsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredTooManyInputs() {
    List<String> noCurrentsSingle = new ArrayList<String>(
        Arrays.asList("existingImage", "outputImages/misc4", "EXTRA"));
    Command.saveLayered.returnExecutable(noCurrentsSingle, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredNoCurrentImage() {
    List<String> saveCurrents = new ArrayList<String>(
        Collections.singletonList("outputImages/misc4"));
    Command.saveLayered.returnExecutable(saveCurrents, null, "birb");
  }

  //TESTING SAVE LAYERED AS IMAGE COMMANDS.

  @Test
  public void testSaveLayeredAsImage() {
    List<String> noCurrentsSave = new ArrayList<String>(
        Arrays.asList("existingImage", "basic", "png", "outputImages/misc4"));
    assertTrue(Command.saveAsImage
        .returnExecutable(noCurrentsSave, null, null) instanceof BasicBlendCommand);

    List<String> currentImageSave = new ArrayList<String>(
        Arrays.asList("basic", "png", "outputImages/misc4"));
    assertTrue(Command.saveAsImage
        .returnExecutable(currentImageSave, "existingImage", "birb") instanceof BasicBlendCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredFullAsImageUnknownBlend() {
    List<String> noCurrentsSave = new ArrayList<String>(
        Arrays.asList("existingImage", "UNKNOWN", "png", "outputImages/misc4"));
    Command.saveAsImage.returnExecutable(noCurrentsSave, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredCurrentsAsImageUnknownBlend() {
    List<String> currentImageSave = new ArrayList<String>(
        Arrays.asList("UNKNOWN", "png", "outputImages/misc4"));
    Command.saveAsImage.returnExecutable(currentImageSave, "existingImage", "birb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredAsImageNullList() {
    Command.saveAsImage.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredAsImageNullElementInList() {
    List<String> noCurrentsSave = new ArrayList<String>(
        Arrays.asList("existingImage", null, "png", "outputImages/misc4"));
    Command.saveAsImage.returnExecutable(noCurrentsSave, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredAsImageTooLittleInputs() {
    List<String> noCurrentsSave = new ArrayList<String>(
        Arrays.asList("existingImage", "basic"));
    Command.saveAsImage.returnExecutable(noCurrentsSave, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredAsImageTooManyInputs() {
    List<String> noCurrentsSave = new ArrayList<String>(
        Arrays.asList("EXTRA", "existingImage", "outputImages/misc4", "EXTRA"));
    Command.saveAsImage.returnExecutable(noCurrentsSave, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSaveLayeredAsImageNoCurrentImage() {
    List<String> saveCurrents = new ArrayList<String>(
        Arrays.asList("basic", "png", "outputImages/misc4"));
    Command.saveLayered.returnExecutable(saveCurrents, null, "birb");
  }

  //TESTING LOAD IMAGE COMMANDS.

  @Test
  public void testLoad() {
    List<String> load = new ArrayList<String>(
        Collections.singletonList("existingImage"));
    assertTrue(Command.load.returnExecutable(load, null, null) instanceof LoadCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailLoadAsImageNullList() {
    Command.load.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailLoadNullElementInList() {
    List<String> noCurrentsSave = new ArrayList<String>(
        Collections.singletonList(null));
    Command.load.returnExecutable(noCurrentsSave, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailLoadTooLittleInputs() {
    List<String> load = new ArrayList<String>();
    Command.load.returnExecutable(load, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailLoadTooManyInputs() {
    List<String> noCurrentsSave = new ArrayList<String>(
        Arrays.asList("EXTRA", "existingImage"));
    Command.load.returnExecutable(noCurrentsSave, null, null);
  }

  //TESTING SET CURRENT LAYER COMMANDS.

  @Test
  public void testSetCurrentLayer() {
    List<String> setCurrentLayer = new ArrayList<String>(
        Collections.singletonList("birb"));
    assertTrue(Command.setCurrentLayer
        .returnExecutable(setCurrentLayer, null, null) instanceof LoadLayerCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSetCurrentLayerAsImageNullList() {
    Command.setCurrentLayer.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSetCurrentLayerNullElementInList() {
    List<String> setCurrentLayer = new ArrayList<String>(
        Collections.singletonList(null));
    Command.setCurrentLayer.returnExecutable(setCurrentLayer, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSetCurrentLayerTooLittleInputs() {
    List<String> setCurrentLayer = new ArrayList<String>();
    Command.setCurrentLayer.returnExecutable(setCurrentLayer, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailSetCurrentLayerTooManyInputs() {
    List<String> setCurrentLayer = new ArrayList<String>(
        Arrays.asList("EXTRA", "existingImage"));
    Command.setCurrentLayer.returnExecutable(setCurrentLayer, null, null);
  }

  //TESTING ADD LAYER COMMANDS.

  @Test
  public void testAddLayer() {
    List<String> addLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "new"));
    assertTrue(Command.addLayer
        .returnExecutable(addLayerNonCurrent, null, null) instanceof AddLayerCommand);

    List<String> addLayerWithCurrent = new ArrayList<String>(
        Collections.singletonList("new"));
    assertTrue(Command.addLayer
        .returnExecutable(addLayerWithCurrent, "existingImage", null) instanceof AddLayerCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailAddLayerNoCurrent() {
    List<String> addLayerWithCurrent = new ArrayList<String>(
        Collections.singletonList("new"));
    Command.addLayer.returnExecutable(addLayerWithCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailAddLayerAsImageNullList() {
    Command.addLayer.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailAddLayerNullElementInList() {
    List<String> addLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", null));
    Command.addLayer.returnExecutable(addLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailAddLayerTooLittleInputs() {
    List<String> addLayerNonCurrent = new ArrayList<String>();
    Command.addLayer.returnExecutable(addLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailAddLayerTooManyInputs() {
    List<String> addLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("EXTRA", "existingImage", "new"));
    Command.addLayer.returnExecutable(addLayerNonCurrent, null, null);
  }

  //TESTING COPY LAYER COMMANDS.

  @Test
  public void testCopyLayer() {
    List<String> copyLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "new", "birb"));
    assertTrue(Command.copyLayer
        .returnExecutable(copyLayerNonCurrent, null, null) instanceof CopyLayerCommand);

    List<String> copyLayerWithCurrent = new ArrayList<String>(
        Arrays.asList("new", "birb"));
    assertTrue(Command.copyLayer
        .returnExecutable(copyLayerWithCurrent, "existingImage", null) instanceof CopyLayerCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCopyLayerNoCurrent() {
    List<String> copyLayerWithCurrent = new ArrayList<String>(
        Arrays.asList("new", "birb"));
    Command.copyLayer.returnExecutable(copyLayerWithCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCopyLayerAsImageNullList() {
    Command.copyLayer.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCopyLayerNullElementInList() {
    List<String> copyLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", null, "birb"));
    Command.copyLayer.returnExecutable(copyLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCopyLayerTooLittleInputs() {
    List<String> copyLayerNonCurrent = new ArrayList<String>(
        Collections.singletonList("birb"));
    Command.copyLayer.returnExecutable(copyLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCopyLayerTooManyInputs() {
    List<String> addLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "EXTRA", "new", "birb"));
    Command.copyLayer.returnExecutable(addLayerNonCurrent, null, null);
  }

  //TESTING ADD IMAGE AS LAYER COMMANDS.

  @Test
  public void testAddImageAsLayer() {
    List<String> addImageLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "vulture", "outputImages/vulture.png"));
    assertTrue(Command.addImageAsLayer
        .returnExecutable(addImageLayerNonCurrent, null, null) instanceof AddImageLayerCommand);

    List<String> addImageLayerWithCurrent = new ArrayList<String>(
        Arrays.asList("vulture", "outputImages/vulture.png"));
    assertTrue(Command.addImageAsLayer.returnExecutable(addImageLayerWithCurrent, "existingImage",
        null) instanceof AddImageLayerCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailAddImageAsLayerNoCurrent() {
    List<String> addImageLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("vulture", "outputImages/vulture.png"));
    Command.addImageAsLayer.returnExecutable(addImageLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailAddImageAsLayerNullList() {
    Command.addImageAsLayer.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailAddImageAsLayerNullElementInList() {
    List<String> addImageLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "vulture", null));
    Command.addImageAsLayer.returnExecutable(addImageLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailAddImageAsLayerTooLittleInputs() {
    List<String> addImageLayerNonCurrent = new ArrayList<String>(
        Collections.singletonList("outputImages/vulture.png"));
    Command.addImageAsLayer.returnExecutable(addImageLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailAddImageAsLayerTooManyInputs() {
    List<String> addImageLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "EXTRA", "vulture", "outputImages/vulture.png"));
    Command.addImageAsLayer.returnExecutable(addImageLayerNonCurrent, null, null);
  }

  //TESTING MOVE LAYER COMMANDS.

  @Test
  public void testMoveLayer() {
    List<String> moveLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "birb", "0"));
    assertTrue(Command.moveLayer
        .returnExecutable(moveLayerNonCurrent, null, null) instanceof MoveLayerCommand);

    List<String> moveLayerWithCurrent = new ArrayList<String>(
        Arrays.asList("birb", "0"));
    assertTrue(Command.moveLayer
        .returnExecutable(moveLayerWithCurrent, "existingImage", null) instanceof MoveLayerCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailMoveLayerNoCurrent() {
    List<String> moveLayerWithCurrent = new ArrayList<String>(
        Arrays.asList("birb", "0"));
    Command.moveLayer.returnExecutable(moveLayerWithCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailMoveLayerInvalidDestinationCurrent() {
    List<String> moveLayerWithCurrent = new ArrayList<String>(
        Arrays.asList("birb", "NOT AN INTEGER"));
    Command.moveLayer.returnExecutable(moveLayerWithCurrent, "existingImage", null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailMoveLayerInvalidDestination() {
    List<String> moveLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "birb", "NOT AN INTEGER"));
    Command.moveLayer.returnExecutable(moveLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailMoveLayerNullList() {
    Command.moveLayer.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailMoveLayerNullElementInList() {
    List<String> moveLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", null, null));
    Command.moveLayer.returnExecutable(moveLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailMoveLayerTooLittleInputs() {
    List<String> moveLayerNonCurrent = new ArrayList<String>(
        Collections.singletonList("0"));
    Command.moveLayer.returnExecutable(moveLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailMoveLayerTooManyInputs() {
    List<String> moveLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "EXTRA", "birb", "0"));
    Command.moveLayer.returnExecutable(moveLayerNonCurrent, null, null);
  }

  //TESTING REMOVE LAYER COMMANDS.

  @Test
  public void testRemoveLayer() {
    List<String> removeLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "galaxy"));
    assertTrue(Command.removeLayer
        .returnExecutable(removeLayerNonCurrent, null, null) instanceof RemoveLayerByNameCommand);

    List<String> removeLayerWithCurrent = new ArrayList<String>(
        Collections.singletonList("galaxy"));
    assertTrue(Command.removeLayer.returnExecutable(removeLayerWithCurrent, "existingImage",
        null) instanceof RemoveLayerByNameCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailRemoveLayerNoCurrent() {
    List<String> removeLayerWithCurrent = new ArrayList<String>(
        Collections.singletonList("galaxy"));
    Command.removeLayer.returnExecutable(removeLayerWithCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailRemoveLayerNullList() {
    Command.removeLayer.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailRemoveLayerNullElementInList() {
    List<String> removeLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", null));
    Command.removeLayer.returnExecutable(removeLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailRemoveLayerTooLittleInputs() {
    List<String> removeLayerNonCurrent = new ArrayList<String>();
    Command.removeLayer.returnExecutable(removeLayerNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailRemoveLayerTooManyInputs() {
    List<String> removeLayerNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "galaxy", "EXTRA"));
    Command.removeLayer.returnExecutable(removeLayerNonCurrent, null, null);
  }

  //TESTING UPDATE VISIBILITY COMMANDS.

  @Test
  public void testUpdateVisibility() {
    List<String> updateVisibilityNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "rainbow", "false"));
    assertTrue(Command.updateVisibility.returnExecutable(updateVisibilityNonCurrent, null,
        null) instanceof UpdateVisibilityCommand);

    List<String> updateVisibilityCurrentLayer = new ArrayList<String>(
        Arrays.asList("rainbow", "false"));
    assertTrue(Command.updateVisibility.returnExecutable(updateVisibilityCurrentLayer, null,
        "rainbow") instanceof UpdateVisibilityCommand);

    List<String> updateVisibilityWithCurrent = new ArrayList<String>(
        Collections.singletonList("false"));
    assertTrue(Command.updateVisibility
        .returnExecutable(updateVisibilityWithCurrent, "existingImage",
            "rainbow") instanceof UpdateVisibilityCommand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateVisibilityInvalidBoolean() {
    List<String> updateVisibilityNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "rainbow", "NOT A BOOLEAN"));
    Command.updateVisibility.returnExecutable(updateVisibilityNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateVisibilityInvalidBooleanCurrent() {
    List<String> updateVisibilityWithCurrent = new ArrayList<String>(
        Collections.singletonList("NOT A BOOLEAN"));
    Command.updateVisibility
        .returnExecutable(updateVisibilityWithCurrent, "existingImage", "rainbow");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateVisibilityInvalidBooleanCurrentLayer() {
    List<String> updateVisibilityCurrentLayer = new ArrayList<String>(
        Arrays.asList("existingImage", "NOT A BOOLEAN"));
    Command.updateVisibility.returnExecutable(updateVisibilityCurrentLayer, null, "rainbow");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateVisibilityNoCurrent() {
    List<String> updateVisibilityNonCurrent = new ArrayList<String>(
        Collections.singletonList("rainbow"));
    Command.updateVisibility.returnExecutable(updateVisibilityNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateVisibilityNoCurrentLayer() {
    List<String> updateVisibilityNonCurrent = new ArrayList<String>(
        Arrays.asList("rainbow", "false"));
    Command.updateVisibility.returnExecutable(updateVisibilityNonCurrent, null, null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateVisibilityNullList() {
    Command.updateVisibility.returnExecutable(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateVisibilityNullElementInList() {
    List<String> updateVisibilityNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", null, "false"));
    Command.updateVisibility.returnExecutable(updateVisibilityNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateVisibilityTooLittleInputs() {
    List<String> updateVisibilityNonCurrent = new ArrayList<String>();
    Command.updateVisibility.returnExecutable(updateVisibilityNonCurrent, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailUpdateVisibilityTooManyInputs() {
    List<String> updateVisibilityNonCurrent = new ArrayList<String>(
        Arrays.asList("existingImage", "rainbow", "EXTRA", "false"));
    Command.updateVisibility.returnExecutable(updateVisibilityNonCurrent, null, null);
  }
}

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import controller.ImageProcessingController;
import controller.ProcessingController;
import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.InputType;
import imageasgraph.Node;
import imageasgraph.Node.EmptyNode;
import imageasgraph.Node.PixelNode;
import imageasgraph.OutputType;
import imageasgraph.Utils;
import imageinput.CheckerBoard;
import imageinput.ImageProgram;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0.LayeredImageIterator;
import layeredimage.LayeredImageV0;
import layeredimage.blend.BasicBlend;
import mutators.Mutator;
import mutators.Mutator.BlurFilter;
import mutators.Mutator.GreyscaleTransform;
import mutators.Mutator.SepiaTransform;
import mutators.Mutator.SharpenFilter;
import mutators.matrices.Matrix;
import mutators.matrices.MatrixImpl;
import org.junit.Test;
import pixel.PixelAsColors;
import pixel.SimplePixel;
import scriptlanguage.Command;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.ParsedCommand;
import scriptlanguage.ParsedCommand.AddImageLayerCommand;
import scriptlanguage.ParsedCommand.AddLayerCommand;
import scriptlanguage.ParsedCommand.BasicBlendCommand;
import scriptlanguage.ParsedCommand.BlurCommand;
import scriptlanguage.ParsedCommand.CopyLayerCommand;
import scriptlanguage.ParsedCommand.CreateCheckerBoardCommand;
import scriptlanguage.ParsedCommand.CreateCopyCommand;
import scriptlanguage.ParsedCommand.CreateEmptyImageCommand;
import scriptlanguage.ParsedCommand.CreateFromImageCommand;
import scriptlanguage.ParsedCommand.CreateNewLayeredImageCommand;
import scriptlanguage.ParsedCommand.CreateTransparentCommand;
import scriptlanguage.ParsedCommand.GreyscaleCommand;
import scriptlanguage.ParsedCommand.ImportNewLayeredImageCommand;
import scriptlanguage.ParsedCommand.LoadCommand;
import scriptlanguage.ParsedCommand.LoadLayerCommand;
import scriptlanguage.ParsedCommand.MoveLayerCommand;
import scriptlanguage.ParsedCommand.RemoveLayerByNameCommand;
import scriptlanguage.ParsedCommand.SaveCommand;
import scriptlanguage.ParsedCommand.SaveLayeredCommand;
import scriptlanguage.ParsedCommand.SepiaCommand;
import scriptlanguage.ParsedCommand.SharpenCommand;
import scriptlanguage.ParsedCommand.UpdateColorCommand;
import scriptlanguage.ParsedCommand.UpdateVisibilityCommand;
import view.CommandLineTextView;
import view.ErrorView;
import view.ErrorView.TextErrorView;
import view.View;

/**
 * Holds all prior test classes for older methods/structures.
 */
public class PreviousTesting {

  /**
   * To test creating a checkerboard image.
   */
  public static class TestCheckerboard {

    @Test(expected = IllegalArgumentException.class)
    public void testNullPixel0() {
      ImageProgram c0 = new CheckerBoard(5, 16,
          new SimplePixel(null), new SimplePixel(0, 1, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPixel1() {
      ImageProgram c0 = new CheckerBoard(5, 16,
          new SimplePixel(0, 1, 2), new SimplePixel(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroTileSize() {
      ImageProgram c0 = new CheckerBoard(0, 16,
          new SimplePixel(3, 4, 5), new SimplePixel(0, 1, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTileSize() {
      ImageProgram c0 = new CheckerBoard(-7, 16,
          new SimplePixel(3, 4, 5), new SimplePixel(0, 1, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroNumTiles() {
      ImageProgram c0 = new CheckerBoard(3, 0,
          new SimplePixel(3, 4, 5), new SimplePixel(0, 1, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumTiles() {
      ImageProgram c0 = new CheckerBoard(2, -4,
          new SimplePixel(3, 4, 5), new SimplePixel(0, 1, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPerfectSquareNumTiles() {
      ImageProgram c0 = new CheckerBoard(2, 17,
          new SimplePixel(3, 4, 5), new SimplePixel(0, 1, 2));
    }

    @Test
    public void testValid() {
      PixelAsColors red = new SimplePixel(255, 0, 0);
      PixelAsColors black = new SimplePixel(0, 0, 0);
      ImageProgram c0 = new CheckerBoard(4, 4, red, black);
      ArrayList<ArrayList<PixelAsColors>> cArray = c0.getImage();
      assertEquals(red, cArray.get(0).get(0));
      assertEquals(red, cArray.get(0).get(1));
      assertEquals(red, cArray.get(0).get(2));
      assertEquals(red, cArray.get(0).get(3));
      assertEquals(black, cArray.get(0).get(4));
      assertEquals(black, cArray.get(0).get(5));
      assertEquals(black, cArray.get(0).get(6));
      assertEquals(black, cArray.get(0).get(7));
      assertEquals(red, cArray.get(1).get(0));
      assertEquals(red, cArray.get(1).get(1));
      assertEquals(red, cArray.get(1).get(2));
      assertEquals(red, cArray.get(1).get(3));
      assertEquals(black, cArray.get(1).get(4));
      assertEquals(black, cArray.get(1).get(5));
      assertEquals(black, cArray.get(1).get(6));
      assertEquals(black, cArray.get(1).get(7));
    }


  }

  /**
   * For testing Color Transformation methods and effects of mutations.
   */
  public static class TestColorTransformations {

    private GraphOfPixels graphExample1;
    private Mutator greyscale;
    private Mutator sepia;

    /**
     * For initializing test variables. Initialized with 16 as RGB for convenient testing.
     */
    private void setUp() {
      this.graphExample1 = ImageToGraphConverter.createEmptyGraph();
      this.graphExample1.insertColumn(0);
      this.graphExample1.insertColumn(1);
      this.graphExample1.insertRow(0);
      this.graphExample1.insertRow(1);
      this.graphExample1.getPixelAt(0, 0).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample1.getPixelAt(1, 0).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample1.getPixelAt(2, 0).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample1.getPixelAt(0, 1).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample1.getPixelAt(1, 1).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample1.getPixelAt(2, 1).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample1.getPixelAt(0, 2).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample1.getPixelAt(1, 2).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample1.getPixelAt(2, 2).updateColors(
          new SimplePixel(55, 111, 222));

      greyscale = new GreyscaleTransform();
      sepia = new SepiaTransform();
    }

    @Test
    public void testGreyscaleMutate() {
      this.setUp();
      greyscale.apply(graphExample1);

      for (int i = 0; i < graphExample1.getWidth(); i += 1) {
        for (int j = 0; j < graphExample1.getHeight(); j += 1) {
          assertEquals(107, graphExample1.getPixelAt(i, j).getRed());
          assertEquals(107, graphExample1.getPixelAt(i, j).getGreen());
          assertEquals(107, graphExample1.getPixelAt(i, j).getBlue());
        }
      }
    }

    @Test
    public void testSepiaMutate() {
      this.setUp();
      sepia.apply(graphExample1);

      for (int i = 0; i < graphExample1.getWidth(); i += 1) {
        for (int j = 0; j < graphExample1.getHeight(); j += 1) {
          assertEquals(149, graphExample1.getPixelAt(i, j).getRed());
          assertEquals(133, graphExample1.getPixelAt(i, j).getGreen());
          assertEquals(103, graphExample1.getPixelAt(i, j).getBlue());
        }
      }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreyscaleNullGraph() {
      this.setUp();
      greyscale.apply(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSepiaNullGraph() {
      this.setUp();
      sepia.apply(null);
    }

  }

  /**
   * Tests that given specific inputs, correct Command objects are returned.
   */
  public static class TestCommand {

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
          Command.createImage
              .returnExecutable(copyInputs, null, null) instanceof CreateCopyCommand);

      List<String> fromImageInputs = new ArrayList<String>(
          Arrays.asList("from-image", "new", "outputImages/birb.jpg"));
      assertTrue(Command.createImage.returnExecutable(
          fromImageInputs, null, null) instanceof CreateFromImageCommand);

      List<String> fromImageInputsDecoded = new ArrayList<String>(
          Arrays.asList("from-image", "new", "outputImages/Space>Folder/example.ppm"));
      assertTrue(Command.createImage.returnExecutable(
          fromImageInputsDecoded, null, null) instanceof CreateFromImageCommand);
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
          Arrays
              .asList("checkerboard", "new", "too", "many", "1", "36", "0", "0", "0", "255", "255",
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

      List<String> fromInputsDecoded = new ArrayList<String>(
          Arrays.asList("newLayered", "outputImages/Space>Folder/testSaveLayeredAlter"));
      assertTrue(Command.createLayeredImage
          .returnExecutable(fromInputsDecoded, null, null) instanceof ImportNewLayeredImageCommand);

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
          Command.applyMutator
              .returnExecutable(blurInputsSingle, null, null) instanceof BlurCommand);

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
          .returnExecutable(sepiaInputsAllCurrents, "existingImage",
              "birb") instanceof SepiaCommand);

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
      assertTrue(
          Command.save.returnExecutable(noCurrentsSingle, null, null) instanceof SaveCommand);

      List<String> noCurrentsSingleDecoded = new ArrayList<String>(
          Arrays.asList("existingImage", "none", "ppm", "outputImages/Space>Folder/delete"));
      assertTrue(
          Command.save
              .returnExecutable(noCurrentsSingleDecoded, null, null) instanceof SaveCommand);
      HashMap<String, GraphOfPixels> testGraphs = new HashMap<String, GraphOfPixels>();
      testGraphs.put("existingImage", ImageToGraphConverter.convertPPM("outputImages/example.ppm"));
      HashMap<String, LayeredImage> testLayered = new HashMap<String, LayeredImage>();
      Command.save.returnExecutable(noCurrentsSingleDecoded, null, null)
          .execute(testGraphs, testLayered);

      List<String> noCurrentsLayered = new ArrayList<String>(
          Arrays.asList("existingImage", "birb", "png", "outputImages/birbLayerNEW"));
      assertTrue(
          Command.save.returnExecutable(noCurrentsLayered, null, null) instanceof SaveCommand);

      List<String> currentLayer = new ArrayList<String>(
          Arrays.asList("existingImage", "png", "outputImages/birbNEW"));
      assertTrue(Command.save.returnExecutable(currentLayer, null, "birb") instanceof SaveCommand);

      List<String> allCurrents = new ArrayList<String>(
          Arrays.asList("png", "outputImages/birbNEW"));
      assertTrue(
          Command.save
              .returnExecutable(allCurrents, "existingImage", "birb") instanceof SaveCommand);
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

      List<String> noCurrentsSingleDecoded = new ArrayList<String>(
          Arrays.asList("existingImage", "outputImages/Space>Folder/delete2"));
      assertTrue(Command.saveLayered
          .returnExecutable(noCurrentsSingleDecoded, null, null) instanceof SaveLayeredCommand);
      HashMap<String, GraphOfPixels> testGraphs = new HashMap<String, GraphOfPixels>();
      HashMap<String, LayeredImage> testLayered = new HashMap<String, LayeredImage>();
      testLayered.put("existingImage", new LayeredImageV0("outputImages/exampleLayeredImage"));
      Command.saveLayered.returnExecutable(noCurrentsSingleDecoded, null, null)
          .execute(testGraphs, testLayered);

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

      List<String> noCurrentsSaveDecoded = new ArrayList<String>(
          Arrays.asList("existingImage", "basic", "png", "outputImages/Space>Folder/delete3"));
      assertTrue(Command.saveAsImage
          .returnExecutable(noCurrentsSave, null, null) instanceof BasicBlendCommand);
      HashMap<String, GraphOfPixels> testGraphs = new HashMap<String, GraphOfPixels>();
      HashMap<String, LayeredImage> testLayered = new HashMap<String, LayeredImage>();
      testLayered.put("existingImage", new LayeredImageV0("outputImages/exampleLayeredImage"));
      Command.saveAsImage.returnExecutable(noCurrentsSaveDecoded, null, null)
          .execute(testGraphs, testLayered);

      List<String> currentImageSave = new ArrayList<String>(
          Arrays.asList("basic", "png", "outputImages/misc4"));
      assertTrue(Command.saveAsImage
          .returnExecutable(currentImageSave, "existingImage",
              "birb") instanceof BasicBlendCommand);
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
          .returnExecutable(copyLayerWithCurrent, "existingImage",
              null) instanceof CopyLayerCommand);
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
          .returnExecutable(moveLayerWithCurrent, "existingImage",
              null) instanceof MoveLayerCommand);
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

  /**
   * Represents a class for sample testing the methods of a View that would function from Command
   * Line. NOTE: The showView method relies on inputs from System.in, but as this can not be
   * simulated, we instead test the outputs and the inputs are handled in the controller tests, so
   * refer to other tests (such as those for input/output behavior tested in the TestCommand and
   * TestLanguageSyntax classes).
   */
  public static class TestCommandLineTextView {

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingViewWithNullOutput() {
      View failView = new CommandLineTextView(null);
    }


    /**
     * Tests whether render message will properly write message into output.
     */
    @Test
    public void testRenderException() {
      Appendable out = new StringBuilder();
      View testView = new CommandLineTextView(out);
      testView.renderException("Hello World!");
      assertEquals("Hello World!", out.toString());
    }

    /**
     * Tests whether exception thrown when message given to render exception is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFailRenderNull() {
      Appendable out = new StringBuilder();
      View testView = new CommandLineTextView(out);
      testView.renderException(null);
    }

    /**
     * Tests whether exception is thrown when writing a message to the output fails (given bad
     * Appendable).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFailedMessageRender() {
      Appendable out = new MockFailAppendable();
      View testView = new CommandLineTextView(out);
      testView.renderException("THIS WILL FAIL.");
    }
  }

  /**
   * For testing Filter methods and effects of mutations.
   */
  public static class TestFilters {

    private GraphOfPixels graphExample1;
    private GraphOfPixels graphExample2;
    private Mutator blur;
    private Mutator sharpen;

    /**
     * For initializing test variables. Initialized with 16 as RGB for convenient testing.
     */
    private void setUp() {
      this.graphExample1 = ImageToGraphConverter.createEmptyGraph();
      this.graphExample1.insertColumn(0);
      this.graphExample1.insertColumn(1);
      this.graphExample1.insertRow(0);
      this.graphExample1.insertRow(1);
      this.graphExample1.getPixelAt(0, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(1, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(2, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(0, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(1, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(2, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(0, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(1, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(2, 2).updateColors(
          new SimplePixel(16, 16, 16));

      this.graphExample2 = ImageToGraphConverter.createEmptyGraph();
      this.graphExample2.insertColumn(0);
      this.graphExample2.insertColumn(1);
      this.graphExample2.insertColumn(2);
      this.graphExample2.insertColumn(3);
      this.graphExample2.insertRow(0);
      this.graphExample2.insertRow(1);
      this.graphExample2.insertRow(2);
      this.graphExample2.insertRow(3);
      this.graphExample2.getPixelAt(0, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(1, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(2, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(3, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(4, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(0, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(1, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(2, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(3, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(4, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(0, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(1, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(2, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(3, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(4, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(0, 3).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(1, 3).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(2, 3).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(3, 3).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(4, 3).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(0, 4).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(1, 4).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(2, 4).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(3, 4).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(4, 4).updateColors(
          new SimplePixel(16, 16, 16));

      blur = new BlurFilter();
      sharpen = new SharpenFilter();
    }

    @Test
    public void testBlurMutate() {
      this.setUp();
      blur.apply(graphExample1);

      assertEquals(9, graphExample1.getPixelAt(0, 0).getRed());
      assertEquals(9, graphExample1.getPixelAt(0, 0).getGreen());
      assertEquals(9, graphExample1.getPixelAt(0, 0).getBlue());

      assertEquals(12, graphExample1.getPixelAt(1, 0).getRed());
      assertEquals(12, graphExample1.getPixelAt(1, 0).getGreen());
      assertEquals(12, graphExample1.getPixelAt(1, 0).getBlue());

      assertEquals(9, graphExample1.getPixelAt(2, 0).getRed());
      assertEquals(9, graphExample1.getPixelAt(2, 0).getGreen());
      assertEquals(9, graphExample1.getPixelAt(2, 0).getBlue());

      assertEquals(12, graphExample1.getPixelAt(0, 1).getRed());
      assertEquals(12, graphExample1.getPixelAt(0, 1).getGreen());
      assertEquals(12, graphExample1.getPixelAt(0, 1).getBlue());

      assertEquals(16, graphExample1.getPixelAt(1, 1).getRed());
      assertEquals(16, graphExample1.getPixelAt(1, 1).getGreen());
      assertEquals(16, graphExample1.getPixelAt(1, 1).getBlue());

      assertEquals(12, graphExample1.getPixelAt(2, 1).getRed());
      assertEquals(12, graphExample1.getPixelAt(2, 1).getGreen());
      assertEquals(12, graphExample1.getPixelAt(2, 1).getBlue());

      assertEquals(9, graphExample1.getPixelAt(0, 2).getRed());
      assertEquals(9, graphExample1.getPixelAt(0, 2).getGreen());
      assertEquals(9, graphExample1.getPixelAt(0, 2).getBlue());

      assertEquals(12, graphExample1.getPixelAt(1, 2).getRed());
      assertEquals(12, graphExample1.getPixelAt(1, 2).getGreen());
      assertEquals(12, graphExample1.getPixelAt(1, 2).getBlue());

      assertEquals(9, graphExample1.getPixelAt(2, 2).getRed());
      assertEquals(9, graphExample1.getPixelAt(2, 2).getGreen());
      assertEquals(9, graphExample1.getPixelAt(2, 2).getBlue());
    }

    @Test
    public void testSharpenMutate() {
      this.setUp();
      sharpen.apply(graphExample2);

      assertEquals(18, graphExample2.getPixelAt(0, 0).getRed());
      assertEquals(18, graphExample2.getPixelAt(0, 0).getGreen());
      assertEquals(18, graphExample2.getPixelAt(0, 0).getBlue());

      assertEquals(24, graphExample2.getPixelAt(1, 0).getRed());
      assertEquals(24, graphExample2.getPixelAt(1, 0).getGreen());
      assertEquals(24, graphExample2.getPixelAt(1, 0).getBlue());

      assertEquals(18, graphExample2.getPixelAt(2, 0).getRed());
      assertEquals(18, graphExample2.getPixelAt(2, 0).getGreen());
      assertEquals(18, graphExample2.getPixelAt(2, 0).getBlue());

      assertEquals(24, graphExample2.getPixelAt(3, 0).getRed());
      assertEquals(24, graphExample2.getPixelAt(3, 0).getGreen());
      assertEquals(24, graphExample2.getPixelAt(3, 0).getBlue());

      assertEquals(18, graphExample2.getPixelAt(4, 0).getRed());
      assertEquals(18, graphExample2.getPixelAt(4, 0).getGreen());
      assertEquals(18, graphExample2.getPixelAt(4, 0).getBlue());

      assertEquals(24, graphExample2.getPixelAt(0, 1).getRed());
      assertEquals(24, graphExample2.getPixelAt(0, 1).getGreen());
      assertEquals(24, graphExample2.getPixelAt(0, 1).getBlue());

      assertEquals(34, graphExample2.getPixelAt(1, 1).getRed());
      assertEquals(34, graphExample2.getPixelAt(1, 1).getGreen());
      assertEquals(34, graphExample2.getPixelAt(1, 1).getBlue());

      assertEquals(26, graphExample2.getPixelAt(2, 1).getRed());
      assertEquals(26, graphExample2.getPixelAt(2, 1).getGreen());
      assertEquals(26, graphExample2.getPixelAt(2, 1).getBlue());

      assertEquals(34, graphExample2.getPixelAt(3, 1).getRed());
      assertEquals(34, graphExample2.getPixelAt(3, 1).getGreen());
      assertEquals(34, graphExample2.getPixelAt(3, 1).getBlue());

      assertEquals(24, graphExample2.getPixelAt(4, 1).getRed());
      assertEquals(24, graphExample2.getPixelAt(4, 1).getGreen());
      assertEquals(24, graphExample2.getPixelAt(4, 1).getBlue());

      assertEquals(18, graphExample2.getPixelAt(0, 2).getRed());
      assertEquals(18, graphExample2.getPixelAt(0, 2).getGreen());
      assertEquals(18, graphExample2.getPixelAt(0, 2).getBlue());

      assertEquals(26, graphExample2.getPixelAt(1, 2).getRed());
      assertEquals(26, graphExample2.getPixelAt(1, 2).getGreen());
      assertEquals(26, graphExample2.getPixelAt(1, 2).getBlue());

      assertEquals(16, graphExample2.getPixelAt(2, 2).getRed());
      assertEquals(16, graphExample2.getPixelAt(2, 2).getGreen());
      assertEquals(16, graphExample2.getPixelAt(2, 2).getBlue());

      assertEquals(26, graphExample2.getPixelAt(3, 2).getRed());
      assertEquals(26, graphExample2.getPixelAt(3, 2).getGreen());
      assertEquals(26, graphExample2.getPixelAt(3, 2).getBlue());

      assertEquals(18, graphExample2.getPixelAt(4, 2).getRed());
      assertEquals(18, graphExample2.getPixelAt(4, 2).getGreen());
      assertEquals(18, graphExample2.getPixelAt(4, 2).getBlue());

      assertEquals(24, graphExample2.getPixelAt(0, 3).getRed());
      assertEquals(24, graphExample2.getPixelAt(0, 3).getGreen());
      assertEquals(24, graphExample2.getPixelAt(0, 3).getBlue());

      assertEquals(34, graphExample2.getPixelAt(1, 3).getRed());
      assertEquals(34, graphExample2.getPixelAt(1, 3).getGreen());
      assertEquals(34, graphExample2.getPixelAt(1, 3).getBlue());

      assertEquals(26, graphExample2.getPixelAt(2, 3).getRed());
      assertEquals(26, graphExample2.getPixelAt(2, 3).getGreen());
      assertEquals(26, graphExample2.getPixelAt(2, 3).getBlue());

      assertEquals(34, graphExample2.getPixelAt(3, 3).getRed());
      assertEquals(34, graphExample2.getPixelAt(3, 3).getGreen());
      assertEquals(34, graphExample2.getPixelAt(3, 3).getBlue());

      assertEquals(24, graphExample2.getPixelAt(4, 3).getRed());
      assertEquals(24, graphExample2.getPixelAt(4, 3).getGreen());
      assertEquals(24, graphExample2.getPixelAt(4, 3).getBlue());

      assertEquals(18, graphExample2.getPixelAt(0, 4).getRed());
      assertEquals(18, graphExample2.getPixelAt(0, 4).getGreen());
      assertEquals(18, graphExample2.getPixelAt(0, 4).getBlue());

      assertEquals(24, graphExample2.getPixelAt(1, 4).getRed());
      assertEquals(24, graphExample2.getPixelAt(1, 4).getGreen());
      assertEquals(24, graphExample2.getPixelAt(1, 4).getBlue());

      assertEquals(18, graphExample2.getPixelAt(2, 4).getRed());
      assertEquals(18, graphExample2.getPixelAt(2, 4).getGreen());
      assertEquals(18, graphExample2.getPixelAt(2, 4).getBlue());

      assertEquals(24, graphExample2.getPixelAt(3, 4).getRed());
      assertEquals(24, graphExample2.getPixelAt(3, 4).getGreen());
      assertEquals(24, graphExample2.getPixelAt(3, 4).getBlue());

      assertEquals(18, graphExample2.getPixelAt(4, 2).getRed());
      assertEquals(18, graphExample2.getPixelAt(4, 2).getGreen());
      assertEquals(18, graphExample2.getPixelAt(4, 2).getBlue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlurNullGraph() {
      this.setUp();
      blur.apply(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSharpenNullGraph() {
      this.setUp();
      sharpen.apply(null);
    }

  }

  /**
   * For testing Graph methods that do not involve reading or writing files.
   */
  public static class TestGraphMethods {

    private GraphOfPixels graphExample;
    private GraphOfPixels graphExample1;
    private GraphOfPixels graphExample2;
    private GraphOfPixels graphExample3;
    private Mutator blur;
    private Mutator sharpen;
    private Mutator sepia;
    private Mutator greyscale;

    /**
     * For initializing test variables.
     */
    private void setUp() {
      this.graphExample = ImageToGraphConverter.createEmptyGraph();
      this.graphExample.insertColumn(0);
      this.graphExample.insertColumn(1);
      this.graphExample.insertRow(0);
      this.graphExample.insertRow(1);
      this.graphExample.getPixelAt(0, 0).updateColors(
          new SimplePixel(1, 1, 1));
      this.graphExample.getPixelAt(1, 0).updateColors(
          new SimplePixel(2, 2, 2));
      this.graphExample.getPixelAt(2, 0).updateColors(
          new SimplePixel(3, 3, 3));
      this.graphExample.getPixelAt(0, 1).updateColors(
          new SimplePixel(4, 4, 4));
      this.graphExample.getPixelAt(1, 1).updateColors(
          new SimplePixel(5, 5, 5));
      this.graphExample.getPixelAt(2, 1).updateColors(
          new SimplePixel(6, 6, 6));
      this.graphExample.getPixelAt(0, 2).updateColors(
          new SimplePixel(7, 7, 7));
      this.graphExample.getPixelAt(1, 2).updateColors(
          new SimplePixel(8, 8, 8));
      this.graphExample.getPixelAt(2, 2).updateColors(
          new SimplePixel(9, 9, 9));
    }

    /**
     * For initializing test variables. Initialized with 16 as RGB for convenient testing.
     */
    private void setUpFilter() {
      this.graphExample1 = ImageToGraphConverter.createEmptyGraph();
      this.graphExample1.insertColumn(0);
      this.graphExample1.insertColumn(1);
      this.graphExample1.insertRow(0);
      this.graphExample1.insertRow(1);
      this.graphExample1.getPixelAt(0, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(1, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(2, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(0, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(1, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(2, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(0, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(1, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample1.getPixelAt(2, 2).updateColors(
          new SimplePixel(16, 16, 16));

      this.graphExample2 = ImageToGraphConverter.createEmptyGraph();
      this.graphExample2.insertColumn(0);
      this.graphExample2.insertColumn(1);
      this.graphExample2.insertColumn(2);
      this.graphExample2.insertColumn(3);
      this.graphExample2.insertRow(0);
      this.graphExample2.insertRow(1);
      this.graphExample2.insertRow(2);
      this.graphExample2.insertRow(3);
      this.graphExample2.getPixelAt(0, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(1, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(2, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(3, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(4, 0).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(0, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(1, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(2, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(3, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(4, 1).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(0, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(1, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(2, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(3, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(4, 2).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(0, 3).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(1, 3).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(2, 3).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(3, 3).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(4, 3).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(0, 4).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(1, 4).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(2, 4).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(3, 4).updateColors(
          new SimplePixel(16, 16, 16));
      this.graphExample2.getPixelAt(4, 4).updateColors(
          new SimplePixel(16, 16, 16));

      blur = new BlurFilter();
      sharpen = new SharpenFilter();
      sepia = new SepiaTransform();
      greyscale = new GreyscaleTransform();
    }

    /**
     * For initializing test variables. Initialized with 16 as RGB for convenient testing.
     */
    private void setUpTransforms() {
      this.graphExample3 = ImageToGraphConverter.createEmptyGraph();
      this.graphExample3.insertColumn(0);
      this.graphExample3.insertColumn(1);
      this.graphExample3.insertRow(0);
      this.graphExample3.insertRow(1);
      this.graphExample3.getPixelAt(0, 0).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample3.getPixelAt(1, 0).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample3.getPixelAt(2, 0).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample3.getPixelAt(0, 1).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample3.getPixelAt(1, 1).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample3.getPixelAt(2, 1).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample3.getPixelAt(0, 2).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample3.getPixelAt(1, 2).updateColors(
          new SimplePixel(55, 111, 222));
      this.graphExample3.getPixelAt(2, 2).updateColors(
          new SimplePixel(55, 111, 222));

      sepia = new SepiaTransform();
      greyscale = new GreyscaleTransform();
    }

    //TESTING MUTATIONS.

    @Test
    public void testBlurMutate() {
      this.setUpFilter();
      graphExample1.applyMutator(blur);

      assertEquals(9, graphExample1.getPixelAt(0, 0).getRed());
      assertEquals(9, graphExample1.getPixelAt(0, 0).getGreen());
      assertEquals(9, graphExample1.getPixelAt(0, 0).getBlue());

      assertEquals(12, graphExample1.getPixelAt(1, 0).getRed());
      assertEquals(12, graphExample1.getPixelAt(1, 0).getGreen());
      assertEquals(12, graphExample1.getPixelAt(1, 0).getBlue());

      assertEquals(9, graphExample1.getPixelAt(2, 0).getRed());
      assertEquals(9, graphExample1.getPixelAt(2, 0).getGreen());
      assertEquals(9, graphExample1.getPixelAt(2, 0).getBlue());

      assertEquals(12, graphExample1.getPixelAt(0, 1).getRed());
      assertEquals(12, graphExample1.getPixelAt(0, 1).getGreen());
      assertEquals(12, graphExample1.getPixelAt(0, 1).getBlue());

      assertEquals(16, graphExample1.getPixelAt(1, 1).getRed());
      assertEquals(16, graphExample1.getPixelAt(1, 1).getGreen());
      assertEquals(16, graphExample1.getPixelAt(1, 1).getBlue());

      assertEquals(12, graphExample1.getPixelAt(2, 1).getRed());
      assertEquals(12, graphExample1.getPixelAt(2, 1).getGreen());
      assertEquals(12, graphExample1.getPixelAt(2, 1).getBlue());

      assertEquals(9, graphExample1.getPixelAt(0, 2).getRed());
      assertEquals(9, graphExample1.getPixelAt(0, 2).getGreen());
      assertEquals(9, graphExample1.getPixelAt(0, 2).getBlue());

      assertEquals(12, graphExample1.getPixelAt(1, 2).getRed());
      assertEquals(12, graphExample1.getPixelAt(1, 2).getGreen());
      assertEquals(12, graphExample1.getPixelAt(1, 2).getBlue());

      assertEquals(9, graphExample1.getPixelAt(2, 2).getRed());
      assertEquals(9, graphExample1.getPixelAt(2, 2).getGreen());
      assertEquals(9, graphExample1.getPixelAt(2, 2).getBlue());
    }

    @Test
    public void testSharpenMutate() {
      this.setUpFilter();
      graphExample2.applyMutator(sharpen);

      assertEquals(18, graphExample2.getPixelAt(0, 0).getRed());
      assertEquals(18, graphExample2.getPixelAt(0, 0).getGreen());
      assertEquals(18, graphExample2.getPixelAt(0, 0).getBlue());

      assertEquals(24, graphExample2.getPixelAt(1, 0).getRed());
      assertEquals(24, graphExample2.getPixelAt(1, 0).getGreen());
      assertEquals(24, graphExample2.getPixelAt(1, 0).getBlue());

      assertEquals(18, graphExample2.getPixelAt(2, 0).getRed());
      assertEquals(18, graphExample2.getPixelAt(2, 0).getGreen());
      assertEquals(18, graphExample2.getPixelAt(2, 0).getBlue());

      assertEquals(24, graphExample2.getPixelAt(3, 0).getRed());
      assertEquals(24, graphExample2.getPixelAt(3, 0).getGreen());
      assertEquals(24, graphExample2.getPixelAt(3, 0).getBlue());

      assertEquals(18, graphExample2.getPixelAt(4, 0).getRed());
      assertEquals(18, graphExample2.getPixelAt(4, 0).getGreen());
      assertEquals(18, graphExample2.getPixelAt(4, 0).getBlue());

      assertEquals(24, graphExample2.getPixelAt(0, 1).getRed());
      assertEquals(24, graphExample2.getPixelAt(0, 1).getGreen());
      assertEquals(24, graphExample2.getPixelAt(0, 1).getBlue());

      assertEquals(34, graphExample2.getPixelAt(1, 1).getRed());
      assertEquals(34, graphExample2.getPixelAt(1, 1).getGreen());
      assertEquals(34, graphExample2.getPixelAt(1, 1).getBlue());

      assertEquals(26, graphExample2.getPixelAt(2, 1).getRed());
      assertEquals(26, graphExample2.getPixelAt(2, 1).getGreen());
      assertEquals(26, graphExample2.getPixelAt(2, 1).getBlue());

      assertEquals(34, graphExample2.getPixelAt(3, 1).getRed());
      assertEquals(34, graphExample2.getPixelAt(3, 1).getGreen());
      assertEquals(34, graphExample2.getPixelAt(3, 1).getBlue());

      assertEquals(24, graphExample2.getPixelAt(4, 1).getRed());
      assertEquals(24, graphExample2.getPixelAt(4, 1).getGreen());
      assertEquals(24, graphExample2.getPixelAt(4, 1).getBlue());

      assertEquals(18, graphExample2.getPixelAt(0, 2).getRed());
      assertEquals(18, graphExample2.getPixelAt(0, 2).getGreen());
      assertEquals(18, graphExample2.getPixelAt(0, 2).getBlue());

      assertEquals(26, graphExample2.getPixelAt(1, 2).getRed());
      assertEquals(26, graphExample2.getPixelAt(1, 2).getGreen());
      assertEquals(26, graphExample2.getPixelAt(1, 2).getBlue());

      assertEquals(16, graphExample2.getPixelAt(2, 2).getRed());
      assertEquals(16, graphExample2.getPixelAt(2, 2).getGreen());
      assertEquals(16, graphExample2.getPixelAt(2, 2).getBlue());

      assertEquals(26, graphExample2.getPixelAt(3, 2).getRed());
      assertEquals(26, graphExample2.getPixelAt(3, 2).getGreen());
      assertEquals(26, graphExample2.getPixelAt(3, 2).getBlue());

      assertEquals(18, graphExample2.getPixelAt(4, 2).getRed());
      assertEquals(18, graphExample2.getPixelAt(4, 2).getGreen());
      assertEquals(18, graphExample2.getPixelAt(4, 2).getBlue());

      assertEquals(24, graphExample2.getPixelAt(0, 3).getRed());
      assertEquals(24, graphExample2.getPixelAt(0, 3).getGreen());
      assertEquals(24, graphExample2.getPixelAt(0, 3).getBlue());

      assertEquals(34, graphExample2.getPixelAt(1, 3).getRed());
      assertEquals(34, graphExample2.getPixelAt(1, 3).getGreen());
      assertEquals(34, graphExample2.getPixelAt(1, 3).getBlue());

      assertEquals(26, graphExample2.getPixelAt(2, 3).getRed());
      assertEquals(26, graphExample2.getPixelAt(2, 3).getGreen());
      assertEquals(26, graphExample2.getPixelAt(2, 3).getBlue());

      assertEquals(34, graphExample2.getPixelAt(3, 3).getRed());
      assertEquals(34, graphExample2.getPixelAt(3, 3).getGreen());
      assertEquals(34, graphExample2.getPixelAt(3, 3).getBlue());

      assertEquals(24, graphExample2.getPixelAt(4, 3).getRed());
      assertEquals(24, graphExample2.getPixelAt(4, 3).getGreen());
      assertEquals(24, graphExample2.getPixelAt(4, 3).getBlue());

      assertEquals(18, graphExample2.getPixelAt(0, 4).getRed());
      assertEquals(18, graphExample2.getPixelAt(0, 4).getGreen());
      assertEquals(18, graphExample2.getPixelAt(0, 4).getBlue());

      assertEquals(24, graphExample2.getPixelAt(1, 4).getRed());
      assertEquals(24, graphExample2.getPixelAt(1, 4).getGreen());
      assertEquals(24, graphExample2.getPixelAt(1, 4).getBlue());

      assertEquals(18, graphExample2.getPixelAt(2, 4).getRed());
      assertEquals(18, graphExample2.getPixelAt(2, 4).getGreen());
      assertEquals(18, graphExample2.getPixelAt(2, 4).getBlue());

      assertEquals(24, graphExample2.getPixelAt(3, 4).getRed());
      assertEquals(24, graphExample2.getPixelAt(3, 4).getGreen());
      assertEquals(24, graphExample2.getPixelAt(3, 4).getBlue());

      assertEquals(18, graphExample2.getPixelAt(4, 2).getRed());
      assertEquals(18, graphExample2.getPixelAt(4, 2).getGreen());
      assertEquals(18, graphExample2.getPixelAt(4, 2).getBlue());
    }

    @Test
    public void testSepiaMutate() {
      this.setUpTransforms();
      graphExample3.applyMutator(sepia);

      for (int i = 0; i < graphExample3.getWidth(); i += 1) {
        for (int j = 0; j < graphExample3.getHeight(); j += 1) {
          assertEquals(149, graphExample3.getPixelAt(i, j).getRed());
          assertEquals(133, graphExample3.getPixelAt(i, j).getGreen());
          assertEquals(103, graphExample3.getPixelAt(i, j).getBlue());
        }
      }
    }

    @Test
    public void testGreyscaleMutate() {
      this.setUpTransforms();
      graphExample3.applyMutator(greyscale);

      for (int i = 0; i < graphExample3.getWidth(); i += 1) {
        for (int j = 0; j < graphExample3.getHeight(); j += 1) {
          assertEquals(107, graphExample3.getPixelAt(i, j).getRed());
          assertEquals(107, graphExample3.getPixelAt(i, j).getGreen());
          assertEquals(107, graphExample3.getPixelAt(i, j).getBlue());
        }
      }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMutation() {
      this.setUpFilter();
      graphExample1.applyMutator(null);
    }

    //TESTING OTHER GRAPH METHODS.


    @Test
    public void testGetDimensions() {
      this.setUp();
      assertEquals(3, this.graphExample.getHeight());
      assertEquals(3, this.graphExample.getWidth());
      this.graphExample.insertRow(2);
      assertEquals(4, this.graphExample.getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidX() {
      this.setUp();
      this.graphExample.getPixelAt(5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidY() {
      this.setUp();
      this.graphExample.getPixelAt(0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNegativeX() {
      this.setUp();
      this.graphExample.getPixelAt(-5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNegativeY() {
      this.setUp();
      this.graphExample.getPixelAt(0, -5);
    }

    @Test
    public void testGetNode() {
      this.setUp();
      assertEquals(1, this.graphExample.getPixelAt(0, 0).getRed());
      assertEquals(1, this.graphExample.getPixelAt(0, 0).getGreen());
      assertEquals(1, this.graphExample.getPixelAt(0, 0).getBlue());
      assertEquals(3, this.graphExample.getPixelAt(2, 0).getRed());
      assertEquals(3, this.graphExample.getPixelAt(2, 0).getGreen());
      assertEquals(3, this.graphExample.getPixelAt(2, 0).getBlue());
      assertEquals(9, this.graphExample.getPixelAt(2, 2).getRed());
      assertEquals(9, this.graphExample.getPixelAt(2, 2).getGreen());
      assertEquals(9, this.graphExample.getPixelAt(2, 2).getBlue());
      assertEquals(4, this.graphExample.getPixelAt(1, 1).getLeft().getRed());
      assertEquals(6, this.graphExample.getPixelAt(1, 1).getRight().getRed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertRowInvalidPosition() {
      this.setUp();
      this.graphExample.insertRow(7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertColumnInvalidPosition() {
      this.setUp();
      this.graphExample.insertColumn(6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertRowNegativePosition() {
      this.setUp();
      this.graphExample.insertRow(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertColumnNegativePosition() {
      this.setUp();
      this.graphExample.insertColumn(-2);
    }

    @Test
    public void testInsertRow() {
      this.setUp();
      assertEquals(3, this.graphExample.getHeight());
      assertEquals(3, this.graphExample.getWidth());
      this.graphExample.insertRow(1);
      assertEquals(4, this.graphExample.getHeight());
      assertEquals(3, this.graphExample.getWidth());
      assertEquals(255, this.graphExample.getPixelAt(0, 2).getRed());
      assertEquals(255, this.graphExample.getPixelAt(1, 2).getRed());
      assertEquals(255, this.graphExample.getPixelAt(2, 2).getRed());
      assertEquals(4, this.graphExample.getPixelAt(0, 2).getAbove().getRed());
      assertEquals(5, this.graphExample.getPixelAt(1, 2).getAbove().getRed());
      assertEquals(6, this.graphExample.getPixelAt(2, 2).getAbove().getRed());
      assertEquals(7, this.graphExample.getPixelAt(0, 2).getBelow().getRed());
      assertEquals(8, this.graphExample.getPixelAt(1, 2).getBelow().getRed());
      assertEquals(9, this.graphExample.getPixelAt(2, 2).getBelow().getRed());
      this.setUp();
      this.graphExample.insertColumn(1);
      assertEquals(3, this.graphExample.getHeight());
      assertEquals(4, this.graphExample.getWidth());
      assertEquals(255, this.graphExample.getPixelAt(2, 0).getRed());
      assertEquals(255, this.graphExample.getPixelAt(2, 1).getRed());
      assertEquals(255, this.graphExample.getPixelAt(2, 2).getRed());
      assertEquals(2, this.graphExample.getPixelAt(2, 0).getLeft().getRed());
      assertEquals(5, this.graphExample.getPixelAt(2, 1).getLeft().getRed());
      assertEquals(8, this.graphExample.getPixelAt(2, 2).getLeft().getRed());
      assertEquals(3, this.graphExample.getPixelAt(2, 0).getRight().getRed());
      assertEquals(6, this.graphExample.getPixelAt(2, 1).getRight().getRed());
      assertEquals(9, this.graphExample.getPixelAt(2, 2).getRight().getRed());
      this.graphExample.insertColumn(3);
      assertEquals(3, this.graphExample.getHeight());
      assertEquals(5, this.graphExample.getWidth());
      assertEquals(255, this.graphExample.getPixelAt(4, 0).getRed());
      assertEquals(255, this.graphExample.getPixelAt(4, 1).getRed());
      assertEquals(255, this.graphExample.getPixelAt(4, 2).getRed());
      assertEquals(3, this.graphExample.getPixelAt(4, 0).getLeft().getRed());
      assertEquals(6, this.graphExample.getPixelAt(4, 1).getLeft().getRed());
      assertEquals(9, this.graphExample.getPixelAt(4, 2).getLeft().getRed());
      assertEquals(new EmptyNode(), this.graphExample.getPixelAt(4, 0).getRight());
      assertEquals(new EmptyNode(), this.graphExample.getPixelAt(4, 1).getRight());
      assertEquals(new EmptyNode(), this.graphExample.getPixelAt(4, 2).getRight());
      this.graphExample.insertRow(2);
      assertEquals(4, this.graphExample.getHeight());
      assertEquals(5, this.graphExample.getWidth());
      assertEquals(255, this.graphExample.getPixelAt(0, 3).getRed());
      assertEquals(255, this.graphExample.getPixelAt(1, 3).getRed());
      assertEquals(255, this.graphExample.getPixelAt(2, 3).getRed());
      assertEquals(255, this.graphExample.getPixelAt(3, 3).getRed());
      assertEquals(255, this.graphExample.getPixelAt(4, 3).getRed());
      assertEquals(7, this.graphExample.getPixelAt(0, 3).getAbove().getRed());
      assertEquals(8, this.graphExample.getPixelAt(1, 3).getAbove().getRed());
      assertEquals(255, this.graphExample.getPixelAt(2, 3).getAbove().getRed());
      assertEquals(9, this.graphExample.getPixelAt(3, 3).getAbove().getRed());
      assertEquals(255, this.graphExample.getPixelAt(4, 3).getAbove().getRed());
      assertEquals(new EmptyNode(), this.graphExample.getPixelAt(0, 3).getBelow());
      assertEquals(new EmptyNode(), this.graphExample.getPixelAt(1, 3).getBelow());
      assertEquals(new EmptyNode(), this.graphExample.getPixelAt(2, 3).getBelow());
      assertEquals(new EmptyNode(), this.graphExample.getPixelAt(3, 3).getBelow());
      assertEquals(new EmptyNode(), this.graphExample.getPixelAt(4, 3).getBelow());
    }

    /**
     * A class to test the iterator for GraphsOfPixels.
     */
    public static class TestGraphIterator {

      private GraphOfPixels newGraph;

      /**
       * Initializes test cases for Graph Iterator.
       */

      @Test
      public void testIterator() {
        this.newGraph = ImageToGraphConverter.createEmptyGraph();
        this.newGraph.insertRow(0);
        Node n0 = newGraph.getPixelAt(0, 0);
        Node n1 = newGraph.getPixelAt(0, 1);
        Iterator<Node> graphIter = newGraph.iterator();

        assertTrue(graphIter.hasNext());
        assertEquals(n0, graphIter.next());
        assertTrue(graphIter.hasNext());
        assertEquals(n1, graphIter.next());
        assertFalse(graphIter.hasNext());
      }

      @Test(expected = NoSuchElementException.class)
      public void testIteratorFail() {
        this.newGraph = ImageToGraphConverter.createEmptyGraph();
        this.newGraph.insertRow(0);
        Node n = newGraph.getPixelAt(0, 0);
        Iterator<Node> graphIter = newGraph.iterator();
        graphIter.next();
        graphIter.next();
        //If next is called once more, it should throw an error
        graphIter.next();
      }
    }

    /**
     * For testing Graph methods that involve writing files.
     */
    public static class TestWritingGraph {

      private GraphOfPixels graphExample;

      /**
       * For initializing test variables.
       */
      private void setUp() {
        this.graphExample = ImageToGraphConverter.createEmptyGraph();
        this.graphExample.insertColumn(0);
        this.graphExample.insertColumn(1);
        this.graphExample.insertRow(0);
        this.graphExample.insertRow(1);
        this.graphExample.getPixelAt(0, 0).updateColors(
            new SimplePixel(1, 1, 1));
        this.graphExample.getPixelAt(1, 0).updateColors(
            new SimplePixel(2, 2, 2));
        this.graphExample.getPixelAt(2, 0).updateColors(
            new SimplePixel(3, 3, 3));
        this.graphExample.getPixelAt(0, 1).updateColors(
            new SimplePixel(4, 4, 4));
        this.graphExample.getPixelAt(1, 1).updateColors(
            new SimplePixel(5, 5, 5));
        this.graphExample.getPixelAt(2, 1).updateColors(
            new SimplePixel(6, 6, 6));
        this.graphExample.getPixelAt(0, 2).updateColors(
            new SimplePixel(7, 7, 7));
        this.graphExample.getPixelAt(1, 2).updateColors(
            new SimplePixel(8, 8, 8));
        this.graphExample.getPixelAt(2, 2).updateColors(
            new SimplePixel(9, 9, 9));
      }

      @Test
      public void testWriteToFile() {
        this.setUp();
        this.graphExample.writeToFile(OutputType.ppm, "outputImages/graphExample");

        GraphOfPixels testWritten = ImageToGraphConverter
            .convertPPM("outputImages/graphExample.ppm");

        assertEquals(3, testWritten.getWidth());
        assertEquals(3, testWritten.getHeight());

        Node node0 = testWritten.getPixelAt(0, 0);
        Node node1 = testWritten.getPixelAt(1, 0);
        Node node2 = testWritten.getPixelAt(2, 0);
        Node node3 = testWritten.getPixelAt(0, 1);
        Node node4 = testWritten.getPixelAt(1, 1);
        Node node5 = testWritten.getPixelAt(2, 1);
        Node node6 = testWritten.getPixelAt(0, 2);
        Node node7 = testWritten.getPixelAt(1, 2);
        Node node8 = testWritten.getPixelAt(2, 2);

        assertEquals(1, node0.getRed());
        assertEquals(1, node0.getGreen());
        assertEquals(1, node0.getBlue());

        assertEquals(2, node1.getRed());
        assertEquals(2, node1.getGreen());
        assertEquals(2, node1.getBlue());

        assertEquals(3, node2.getRed());
        assertEquals(3, node2.getGreen());
        assertEquals(3, node2.getBlue());

        assertEquals(4, node3.getRed());
        assertEquals(4, node3.getGreen());
        assertEquals(4, node3.getBlue());

        assertEquals(5, node4.getRed());
        assertEquals(5, node4.getGreen());
        assertEquals(5, node4.getBlue());

        assertEquals(6, node5.getRed());
        assertEquals(6, node5.getGreen());
        assertEquals(6, node5.getBlue());

        assertEquals(7, node6.getRed());
        assertEquals(7, node6.getGreen());
        assertEquals(7, node6.getBlue());

        assertEquals(8, node7.getRed());
        assertEquals(8, node7.getGreen());
        assertEquals(8, node7.getBlue());

        assertEquals(9, node8.getRed());
        assertEquals(9, node8.getGreen());
        assertEquals(9, node8.getBlue());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testWriteFailNullFileType() {
        this.setUp();
        this.graphExample.writeToFile(null, "outputImages/graphExample");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testWriteFailNullFileName() {
        this.setUp();
        this.graphExample.writeToFile(OutputType.ppm, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testWriteFailInvalidFileName() {
        this.setUp();
        //Error if adding any extension.
        this.graphExample.writeToFile(OutputType.ppm, "nonExistentFolder/graphExample.ppm");
      }
    }
  }

  /**
   * For testing conversions of images into graphs and methods.
   */
  public static class TestImageToGraphConverter {

    private ImageProgram checkerboard;

    /**
     * Initializes example graphs or programs for testing.
     */
    private void setUp() {
      PixelAsColors red = new SimplePixel(255, 0, 0);
      PixelAsColors black = new SimplePixel(0, 0, 0);
      this.checkerboard = new CheckerBoard(1, 9, red, black);


    }

    @Test
    public void testGenerateCheckerboard() {
      this.setUp();
      GraphOfPixels testCheckerboard = ImageToGraphConverter.convertProgram(checkerboard);

      assertEquals(3, testCheckerboard.getWidth());
      assertEquals(3, testCheckerboard.getHeight());

      assertEquals(255, testCheckerboard.getPixelAt(0, 0).getRed());
      assertEquals(0, testCheckerboard.getPixelAt(0, 0).getGreen());
      assertEquals(0, testCheckerboard.getPixelAt(0, 0).getBlue());

      assertEquals(0, testCheckerboard.getPixelAt(1, 0).getRed());
      assertEquals(0, testCheckerboard.getPixelAt(1, 0).getBlue());
      assertEquals(0, testCheckerboard.getPixelAt(1, 0).getBlue());

      assertEquals(255, testCheckerboard.getPixelAt(2, 0).getRed());
      assertEquals(0, testCheckerboard.getPixelAt(2, 0).getGreen());
      assertEquals(0, testCheckerboard.getPixelAt(2, 0).getBlue());

      assertEquals(0, testCheckerboard.getPixelAt(0, 1).getRed());
      assertEquals(0, testCheckerboard.getPixelAt(0, 1).getGreen());
      assertEquals(0, testCheckerboard.getPixelAt(0, 1).getBlue());

      assertEquals(255, testCheckerboard.getPixelAt(1, 1).getRed());
      assertEquals(0, testCheckerboard.getPixelAt(1, 1).getGreen());
      assertEquals(0, testCheckerboard.getPixelAt(1, 1).getBlue());

      assertEquals(0, testCheckerboard.getPixelAt(2, 1).getRed());
      assertEquals(0, testCheckerboard.getPixelAt(2, 1).getGreen());
      assertEquals(0, testCheckerboard.getPixelAt(2, 1).getBlue());

      assertEquals(255, testCheckerboard.getPixelAt(0, 2).getRed());
      assertEquals(0, testCheckerboard.getPixelAt(0, 2).getGreen());
      assertEquals(0, testCheckerboard.getPixelAt(0, 2).getBlue());

      assertEquals(0, testCheckerboard.getPixelAt(1, 2).getRed());
      assertEquals(0, testCheckerboard.getPixelAt(1, 2).getGreen());
      assertEquals(0, testCheckerboard.getPixelAt(1, 2).getBlue());

      assertEquals(255, testCheckerboard.getPixelAt(2, 2).getRed());
      assertEquals(0, testCheckerboard.getPixelAt(2, 2).getGreen());
      assertEquals(0, testCheckerboard.getPixelAt(2, 2).getBlue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateProgramNull() {
      this.setUp();
      GraphOfPixels testCheckerboard = ImageToGraphConverter.convertProgram(null);
    }

    @Test
    public void testCreateEmptyGraph() {
      GraphOfPixels testEmpty = ImageToGraphConverter.createEmptyGraph();

      assertEquals(1, testEmpty.getWidth());
      assertEquals(1, testEmpty.getHeight());

      assertEquals(255, testEmpty.getPixelAt(0, 0).getRed());
      assertEquals(255, testEmpty.getPixelAt(0, 0).getGreen());
      assertEquals(255, testEmpty.getPixelAt(0, 0).getBlue());
    }


    @Test
    public void testReadPPM() {
      //example.ppm is a 2 pixel by 2 pixel image in the outputImages folder with the RGB values:
      //Top Left: (123, 123, 123)
      //Top Right: (211, 211, 211)
      //Bottom Left: (112, 112, 112)
      //Bottom Right: (121, 121, 121)

      GraphOfPixels testRead = ImageToGraphConverter.convertPPM("outputImages/example.ppm");

      //Test that graph read is correctly written.

      assertEquals(2, testRead.getWidth());
      assertEquals(2, testRead.getHeight());

      assertEquals(123, testRead.getPixelAt(0, 0).getRed());
      assertEquals(123, testRead.getPixelAt(0, 0).getGreen());
      assertEquals(123, testRead.getPixelAt(0, 0).getBlue());

      assertEquals(211, testRead.getPixelAt(1, 0).getRed());
      assertEquals(211, testRead.getPixelAt(1, 0).getGreen());
      assertEquals(211, testRead.getPixelAt(1, 0).getBlue());

      assertEquals(112, testRead.getPixelAt(0, 1).getRed());
      assertEquals(112, testRead.getPixelAt(0, 1).getGreen());
      assertEquals(112, testRead.getPixelAt(0, 1).getBlue());

      assertEquals(121, testRead.getPixelAt(1, 1).getRed());
      assertEquals(121, testRead.getPixelAt(1, 1).getGreen());
      assertEquals(121, testRead.getPixelAt(1, 1).getBlue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadNullFail() {
      GraphOfPixels testRead = ImageToGraphConverter.convertPPM(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadInvalidFileNameFail() {
      //Reads from nonexistent file:
      GraphOfPixels testRead = ImageToGraphConverter.convertPPM("thisFileDoesNotExist");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadInvalidLocationFail() {
      //Reads from nonexistent file:
      GraphOfPixels testRead = ImageToGraphConverter.convertPPM("nonExistentFolder/example.ppm");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadInvalidFileFail() {
      //Reads from nonexistent file:
      GraphOfPixels testRead = ImageToGraphConverter.convertPPM("outputImages/invalid.ppm");
    }

    /**
     * For testing the new functionality involving reading and writing files from existing classes.
     */
    public static class TestReadWriteNewFunctionality {

      // NOTE: Tests involving JPG files have ranges, because JPGS do not convert RGB values exactly
      @Test(expected = IllegalArgumentException.class)
      public void testConvertStringInputNull() {
        InputType.convertString(null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertStringInputInvalid() {
        InputType.convertString("PNG");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertStringInputInvalid1() {
        InputType.convertString("JPG");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertStringInputInvalid2() {
        InputType.convertString("PPM");
      }

      @Test
      public void testConvertStringInput() {
        assertEquals(InputType.png, InputType.convertString("png"));
        assertEquals(InputType.ppm, InputType.convertString("ppm"));
        assertEquals(InputType.jpeg, InputType.convertString("jpg"));
        assertEquals(InputType.jpeg, InputType.convertString("jpeg"));
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertStringOutputNull() {
        OutputType.convertString(null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertStringOutputInvalid() {
        OutputType.convertString("PNG");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertStringOutputInvalid1() {
        OutputType.convertString("JPG");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertStringOutputInvalid2() {
        OutputType.convertString("PPM");
      }

      @Test
      public void testConvertStringOutput() {
        assertEquals(OutputType.png, OutputType.convertString("png"));
        assertEquals(OutputType.ppm, OutputType.convertString("ppm"));
        assertEquals(OutputType.jpeg, OutputType.convertString("jpg"));
        assertEquals(OutputType.jpeg, OutputType.convertString("jpeg"));
      }

      @Test(expected = IllegalArgumentException.class)
      public void testCreateTransparentZeroWidth() {
        ImageToGraphConverter.createTransparentGraph(0, 5);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testCreateTransparentZeroHeight() {
        ImageToGraphConverter.createTransparentGraph(33, 0);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testCreateTransparentInvalidWidth() {
        ImageToGraphConverter.createTransparentGraph(-2, 5);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testCreateTransparentInvalidHeight() {
        ImageToGraphConverter.createTransparentGraph(1, -5);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testCreateTransparentBothZero() {
        ImageToGraphConverter.createTransparentGraph(0, 0);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testCreateTransparentBothInvalid() {
        ImageToGraphConverter.createTransparentGraph(-23, -7);
      }

      @Test
      public void testCreateTransparent() {
        GraphOfPixels transparent = ImageToGraphConverter.createTransparentGraph(5, 6);
        assertEquals(5, transparent.getWidth());
        assertEquals(6, transparent.getHeight());
        for (Node n : transparent) {
          assertEquals(n.getOpacity(), 0);
          assertEquals(n.getRed(), 0);
          assertEquals(n.getGreen(), 0);
          assertEquals(n.getBlue(), 0);
        }
        GraphOfPixels transparent2 = ImageToGraphConverter.createTransparentGraph(15, 7);
        assertEquals(15, transparent2.getWidth());
        assertEquals(7, transparent2.getHeight());
        for (Node n : transparent2) {
          assertEquals(n.getOpacity(), 0);
          assertEquals(n.getRed(), 0);
          assertEquals(n.getGreen(), 0);
          assertEquals(n.getBlue(), 0);
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testCreateCopyNullInput() {
        ImageToGraphConverter.createCopyOfGraph(null);
      }

      @Test
      public void testCreateCopy() {
        GraphOfPixels graphToCopy0 = ImageToGraphConverter.createTransparentGraph(5, 6);
        graphToCopy0.getPixelAt(3, 3).updateColors(new SimplePixel(10, 15, 20));
        GraphOfPixels graphToCopy1 = ImageToGraphConverter
            .convertPPM("outputImages/graphExample.ppm");
        GraphOfPixels copy0 = ImageToGraphConverter.createCopyOfGraph(graphToCopy0);
        GraphOfPixels copy1 = ImageToGraphConverter.createCopyOfGraph(graphToCopy1);
        Iterator<Node> graph0Iter = graphToCopy0.iterator();
        Iterator<Node> copy0Iter = copy0.iterator();
        while (graph0Iter.hasNext()) {
          Node graph0Next = graph0Iter.next();
          Node copy0Next = copy0Iter.next();
          graph0Next.setOpacity(1); // Done so that colors can be read properly
          copy0Next.setOpacity(1);
          assertEquals(copy0Next.getOpacity(), graph0Next.getOpacity());
          assertEquals(copy0Next.getRed(), graph0Next.getRed());
          assertEquals(copy0Next.getGreen(), graph0Next.getGreen());
          assertEquals(copy0Next.getBlue(), graph0Next.getBlue());
          graph0Next.setOpacity(0);
          copy0Next.setOpacity(0);
        }
        Iterator<Node> graph1Iter = graphToCopy1.iterator();
        Iterator<Node> copy1Iter = copy1.iterator();
        while (graph1Iter.hasNext()) {
          Node graph1Next = graph1Iter.next();
          Node copy1Next = copy1Iter.next();
          assertEquals(copy1Next.getOpacity(), graph1Next.getOpacity());
          assertEquals(copy1Next.getRed(), graph1Next.getRed());
          assertEquals(copy1Next.getGreen(), graph1Next.getGreen());
          assertEquals(copy1Next.getBlue(), graph1Next.getBlue());
        }
        graphToCopy0.getPixelAt(0, 0).updateColors(new SimplePixel(255, 13, 15));
        graphToCopy0.getPixelAt(0, 0).setOpacity(13);
        assertEquals(0, copy0.getPixelAt(0, 0).getRed());
        assertEquals(0, copy0.getPixelAt(0, 0).getGreen());
        assertEquals(0, copy0.getPixelAt(0, 0).getBlue());
        assertEquals(0, copy0.getPixelAt(0, 0).getOpacity());
        copy1.getPixelAt(0, 0).updateColors(new SimplePixel(15, 16, 17));
        assertEquals(1, graphToCopy1.getPixelAt(0, 0).getRed());
        assertEquals(1, graphToCopy1.getPixelAt(0, 0).getGreen());
        assertEquals(1, graphToCopy1.getPixelAt(0, 0).getBlue());
        assertEquals(255, graphToCopy1.getPixelAt(0, 0).getOpacity());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertComplexNull() {
        ImageToGraphConverter.convertComplexImage(null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertComplexInvalidFileType() {
        ImageToGraphConverter.convertComplexImage("outputImages/conversionTest.ppm");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertComplexNonExistentFile() {
        ImageToGraphConverter.convertComplexImage("atlantis.png");
      }

      @Test
      public void testConvertComplex() {
        GraphOfPixels convertedJPG = ImageToGraphConverter
            .convertComplexImage("outputImages/conversionTest.jpg");
        assertEquals(3, convertedJPG.getWidth());
        assertEquals(3, convertedJPG.getHeight());
        for (Node n : convertedJPG) {
          assertEquals(255, n.getRed(), 21);
          assertEquals(0, n.getGreen());
          assertEquals(0, n.getBlue());
          assertEquals(255, n.getOpacity());
        }
        GraphOfPixels convertedPNG = ImageToGraphConverter
            .convertComplexImage("outputImages/conversionTest.png");
        assertEquals(3, convertedPNG.getWidth());
        assertEquals(3, convertedPNG.getHeight());
        for (Node n : convertedPNG) {
          assertEquals(255, n.getRed());
          assertEquals(0, n.getGreen());
          assertEquals(0, n.getBlue());
          assertEquals(255, n.getOpacity());
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertFileNullInput() {
        ImageToGraphConverter.convertImage(null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertFileInvalidFileType() {
        ImageToGraphConverter.convertImage("outputImages/TestScript.txt");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testConvertFileNonexistentFile() {
        ImageToGraphConverter.convertImage("yo.png");
      }

      @Test
      public void testConvertFile() {
        GraphOfPixels convertedJPG = ImageToGraphConverter
            .convertImage("outputImages/conversionTest.jpg");
        assertEquals(3, convertedJPG.getWidth());
        assertEquals(3, convertedJPG.getHeight());
        for (Node n : convertedJPG) {
          assertEquals(255, n.getRed(), 21);
          assertEquals(0, n.getGreen());
          assertEquals(0, n.getBlue());
          assertEquals(255, n.getOpacity());
        }
        GraphOfPixels convertedPNG = ImageToGraphConverter
            .convertImage("outputImages/conversionTest.png");
        assertEquals(3, convertedPNG.getWidth());
        assertEquals(3, convertedPNG.getHeight());
        for (Node n : convertedPNG) {
          assertEquals(255, n.getRed());
          assertEquals(0, n.getGreen());
          assertEquals(0, n.getBlue());
          assertEquals(255, n.getOpacity());
        }
        GraphOfPixels convertedPPM = ImageToGraphConverter
            .convertImage("outputImages/conversionTest.ppm");
        assertEquals(3, convertedPPM.getWidth());
        assertEquals(3, convertedPPM.getHeight());
        for (Node n : convertedPPM) {
          assertEquals(255, n.getRed());
          assertEquals(0, n.getGreen());
          assertEquals(0, n.getBlue());
          assertEquals(255, n.getOpacity());
        }
      }

      @Test
      public void testWritePNG() {
        GraphOfPixels example = ImageToGraphConverter.convertImage("outputImages/example.ppm");
        example.writeToFile(OutputType.png, "outputImages/example");
        GraphOfPixels writtenFile = ImageToGraphConverter.convertImage("outputImages/example.png");

        assertEquals(2, writtenFile.getHeight());
        assertEquals(2, writtenFile.getWidth());
        assertEquals(123, writtenFile.getPixelAt(0, 0).getRed());
        assertEquals(123, writtenFile.getPixelAt(0, 0).getGreen());
        assertEquals(123, writtenFile.getPixelAt(0, 0).getBlue());
        assertEquals(211, writtenFile.getPixelAt(1, 0).getRed());
        assertEquals(211, writtenFile.getPixelAt(1, 0).getGreen());
        assertEquals(211, writtenFile.getPixelAt(1, 0).getBlue());
        assertEquals(112, writtenFile.getPixelAt(0, 1).getRed());
        assertEquals(112, writtenFile.getPixelAt(0, 1).getGreen());
        assertEquals(112, writtenFile.getPixelAt(0, 1).getBlue());
        assertEquals(121, writtenFile.getPixelAt(1, 1).getRed());
        assertEquals(121, writtenFile.getPixelAt(1, 1).getGreen());
        assertEquals(121, writtenFile.getPixelAt(1, 1).getBlue());
      }

      @Test
      public void testWriteJPG() {
        GraphOfPixels example = ImageToGraphConverter.convertImage("outputImages/example.ppm");
        example.writeToFile(OutputType.jpeg, "outputImages/example");
        GraphOfPixels writtenFile = ImageToGraphConverter.convertImage("outputImages/example.jpeg");

        assertEquals(2, writtenFile.getHeight());
        assertEquals(2, writtenFile.getWidth());
        assertEquals(123, writtenFile.getPixelAt(0, 0).getRed(), 21);
        assertEquals(123, writtenFile.getPixelAt(0, 0).getGreen(), 21);
        assertEquals(123, writtenFile.getPixelAt(0, 0).getBlue(), 21);
        assertEquals(211, writtenFile.getPixelAt(1, 0).getRed(), 21);
        assertEquals(211, writtenFile.getPixelAt(1, 0).getGreen(), 21);
        assertEquals(211, writtenFile.getPixelAt(1, 0).getBlue(), 21);
        assertEquals(112, writtenFile.getPixelAt(0, 1).getRed(), 21);
        assertEquals(112, writtenFile.getPixelAt(0, 1).getGreen(), 21);
        assertEquals(112, writtenFile.getPixelAt(0, 1).getBlue(), 21);
        assertEquals(121, writtenFile.getPixelAt(1, 1).getRed(), 21);
        assertEquals(121, writtenFile.getPixelAt(1, 1).getGreen(), 21);
        assertEquals(121, writtenFile.getPixelAt(1, 1).getBlue(), 21);
      }
    }
  }

  /**
   * Tests whether commands are parsed correctly and correct function objects are returned.
   */
  public static class TestLanguageSyntaxImpl {

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
    private void setUp() {
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
      assertTrue(
          test.parseCommand(createLayeredImageBlank) instanceof CreateNewLayeredImageCommand);

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

      //ALL FUNCTIONALITY WAS TESTED
      assertTrue(graphs.containsKey("existingImage"));
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

      //ALL FUNCTIONALITY WAS TESTED
      assertTrue(graphs.containsKey("existingImage"));
    }
  }

  /**
   * For testing the Layered Image class and related classes.
   */
  public static class TestLayeredImage {

    private LayeredImage layeredImage0;

    /**
     * Initializes testing variables.
     */
    protected void setUp() {
      this.layeredImage0 = new LayeredImageV0(10, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroWidthConstructor() {
      new LayeredImageV0(0, 14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroHeightConstructor() {
      new LayeredImageV0(13, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBothZeroConstructor() {
      new LayeredImageV0(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWidthConstructor() {
      new LayeredImageV0(-15, 55);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeHeightConstructor() {
      new LayeredImageV0(67, -12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBothNegativeConstructor() {
      new LayeredImageV0(-15, -7);
    }

    @Test
    public void testWidthHeightConstructor() {
      this.setUp();
      assertEquals(11, this.layeredImage0.getHeight());
      assertEquals(10, this.layeredImage0.getWidth());
      assertEquals(0, this.layeredImage0.getNumLayers());
      assertEquals(new ArrayList<String>(), this.layeredImage0.getLayerNames());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFileConstructor() {
      new LayeredImageV0(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonExistentFileConstructor() {
      new LayeredImageV0("doesn't exist");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFileTypeConstructor() {
      new LayeredImageV0("outputImages/example.png");
    }

    @Test
    public void testFromFileConstructor() {
      LayeredImage fromFile = new LayeredImageV0("outputImages/exampleLayeredImage");
      assertEquals(20, fromFile.getWidth());
      assertEquals(20, fromFile.getHeight());
      assertEquals(3, fromFile.getNumLayers());
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
          fromFile.getLayerNames());
      assertEquals(false, fromFile.getVisibility("invisible-layer"));
      assertEquals(true, fromFile.getVisibility("blue-layer"));
      assertEquals(true, fromFile.getVisibility("red-layer"));
      for (Node n : fromFile.getLayer("blue-layer")) {
        assertEquals(255, n.getOpacity());
        assertEquals(0, n.getRed());
        assertEquals(0, n.getGreen());
        assertEquals(255, n.getBlue());
      }
      for (Node n : fromFile.getLayer("red-layer")) {
        assertEquals(255, n.getOpacity());
        assertEquals(255, n.getRed());
        assertEquals(0, n.getGreen());
        assertEquals(0, n.getBlue());
      }
      assertEquals(0, fromFile.getLayer(0).getPixelAt(0, 0).getOpacity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLayerNullInput() {
      this.setUp();
      this.layeredImage0.addLayer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLayerInvalidInput() {
      this.setUp();
      this.layeredImage0.addLayer("more than one word");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLayerInvalidInputNewLine() {
      this.setUp();
      this.layeredImage0.addLayer("more\nthan\none\nline");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExistingLayer() {
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      exampleImage.addLayer("red-layer");
    }

    @Test
    public void testAddValidLayer() {
      this.setUp();
      assertEquals(0, this.layeredImage0.getNumLayers());
      assertEquals(new ArrayList<String>(), this.layeredImage0.getLayerNames());
      this.layeredImage0.addLayer("new-layer");
      assertEquals(1, this.layeredImage0.getNumLayers());
      assertEquals(new ArrayList<String>(Arrays.asList("new-layer")),
          this.layeredImage0.getLayerNames());
      assertEquals(11, this.layeredImage0.getLayer("new-layer").getHeight());
      assertEquals(10, this.layeredImage0.getLayer("new-layer").getWidth());
      assertEquals(this.layeredImage0.getLayer("new-layer"), this.layeredImage0.getLayer(0));
      for (Node n : this.layeredImage0.getLayer("new-layer")) {
        assertEquals(0, n.getOpacity());
        assertEquals(0, n.getRed());
        assertEquals(0, n.getGreen());
        assertEquals(0, n.getBlue());
      }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLayerNameCopy() {
      this.setUp();
      this.layeredImage0.addLayer(null, "irrelevant");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCopyNameCopy() {
      this.setUp();
      this.layeredImage0.addLayer("new-layer", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBothNullCopy() {
      this.setUp();
      this.layeredImage0.addLayer(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultipleWordCopy() {
      this.setUp();
      this.layeredImage0.addLayer("two words", "this");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultipleWordNewlineCopy() {
      this.setUp();
      this.layeredImage0.addLayer("two\nwords", "idk");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAlreadyExistingLayerNameCopy() {
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      exampleImage.addLayer("red-layer", "blue-layer");
    }

    @Test(expected = IllegalArgumentException.class)
    public void copyNonExistentLayer() {
      this.setUp();
      this.layeredImage0.addLayer("new-layer", "yo");
    }

    @Test
    public void testAddCopy() {
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      assertEquals(3, exampleImage.getNumLayers());
      assertEquals(20, exampleImage.getHeight());
      assertEquals(20, exampleImage.getWidth());
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
          exampleImage.getLayerNames());
      exampleImage.addLayer("copy-of-red-layer", "red-layer");
      assertEquals(4, exampleImage.getNumLayers());
      assertEquals(20, exampleImage.getLayer("copy-of-red-layer").getHeight());
      assertEquals(20, exampleImage.getLayer("copy-of-red-layer").getWidth());
      assertEquals(exampleImage.getLayer("copy-of-red-layer"), exampleImage.getLayer(0));
      assertEquals(new ArrayList<String>(
              Arrays.asList("copy-of-red-layer", "invisible-layer", "blue-layer", "red-layer")),
          exampleImage.getLayerNames());
      for (Node n : exampleImage.getLayer("copy-of-red-layer")) {
        assertEquals(255, n.getOpacity());
        assertEquals(255, n.getRed());
        assertEquals(0, n.getGreen());
        assertEquals(0, n.getBlue());
      }
      exampleImage.getLayer("red-layer").getPixelAt(0, 0).updateColors(new SimplePixel(0, 255, 0));
      assertEquals(0, exampleImage.getLayer("copy-of-red-layer").getPixelAt(0, 0).getGreen());
      exampleImage.getLayer("copy-of-red-layer").getPixelAt(1, 0)
          .updateColors(new SimplePixel(0, 255, 0));
      assertEquals(0, exampleImage.getLayer("red-layer").getPixelAt(1, 0).getGreen());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoveLayerNullLayerName() {
      this.setUp();
      this.layeredImage0.moveLayer(null, 213);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoveLayerNonExistentLayerName() {
      this.setUp();
      this.layeredImage0.moveLayer("layer", 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeToIndex() {
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      exampleImage.moveLayer("red-layer", -12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLargeIndex() {
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      exampleImage.moveLayer("red-layer", 4);
    }

    @Test
    public void testMoveLayer() {
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      assertEquals(3, exampleImage.getNumLayers());
      assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("invisible-layer"));
      assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("blue-layer"));
      assertEquals(exampleImage.getLayer(2), exampleImage.getLayer("red-layer"));
      exampleImage.moveLayer("red-layer", 0);
      assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("red-layer"));
      assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("invisible-layer"));
      assertEquals(exampleImage.getLayer(2), exampleImage.getLayer("blue-layer"));
      exampleImage.moveLayer("invisible-layer", 2);
      assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("red-layer"));
      assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("blue-layer"));
      assertEquals(exampleImage.getLayer(2), exampleImage.getLayer("invisible-layer"));
      exampleImage.moveLayer("invisible-layer", 2);
      assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("red-layer"));
      assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("blue-layer"));
      assertEquals(exampleImage.getLayer(2), exampleImage.getLayer("invisible-layer"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVisibilityNullInput() {
      this.setUp();
      this.layeredImage0.setVisibility(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVisibilityNonexistentLayerName() {
      this.setUp();
      this.layeredImage0.setVisibility("notThere", false);
    }

    @Test
    public void testSetVisibility() {
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      assertEquals(false, exampleImage.getVisibility("invisible-layer"));
      exampleImage.setVisibility("invisible-layer", true);
      assertEquals(true, exampleImage.getVisibility("invisible-layer"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVisibilityNullInput() {
      this.setUp();
      this.layeredImage0.getVisibility(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVisibilityNonexistentInput() {
      this.setUp();
      this.layeredImage0.getVisibility("no");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVisibilityNegativeIndex() {
      this.setUp();
      this.layeredImage0.getVisibility(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVisibilityTooLargeIndex() {
      this.setUp();
      this.layeredImage0.getVisibility(12);
    }

    @Test
    public void testGetVisibility() {
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      assertEquals(false, exampleImage.getVisibility("invisible-layer"));
      assertEquals(true, exampleImage.getVisibility("red-layer"));
      assertEquals(false, exampleImage.getVisibility(0));
      assertEquals(true, exampleImage.getVisibility(2));
    }

    @Test
    public void testNumLayers() {
      this.setUp();
      assertEquals(0, this.layeredImage0.getNumLayers());
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      assertEquals(3, exampleImage.getNumLayers());
    }

    @Test
    public void testGetLayerNames() {
      this.setUp();
      assertEquals(new ArrayList<String>(), this.layeredImage0.getLayerNames());
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
          exampleImage.getLayerNames());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullInput() {
      this.setUp();
      this.layeredImage0.removeLayer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentLayer() {
      this.setUp();
      this.layeredImage0.removeLayer("no");
    }

    @Test
    public void testRemoveLayer() {
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      assertEquals(3, exampleImage.getNumLayers());
      assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("invisible-layer"));
      assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("blue-layer"));
      assertEquals(exampleImage.getLayer(2), exampleImage.getLayer("red-layer"));
      exampleImage.removeLayer("invisible-layer");
      assertEquals(2, exampleImage.getNumLayers());
      assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("red-layer"));
      assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("blue-layer"));
      exampleImage.removeLayer("blue-layer");
      assertEquals(1, exampleImage.getNumLayers());
      assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("red-layer"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLayerNullInput() {
      this.setUp();
      this.layeredImage0.getLayer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNonexistentLayer() {
      this.setUp();
      this.layeredImage0.getLayer("yo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNegativeLayer() {
      this.setUp();
      this.layeredImage0.getLayer(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLayerTooLargeIndex() {
      this.setUp();
      this.layeredImage0.getLayer(1);
    }

    @Test
    public void testGetLayer() {
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      assertEquals(255, exampleImage.getLayer("red-layer").getPixelAt(0, 0).getRed());
      assertEquals(0, exampleImage.getLayer("red-layer").getPixelAt(0, 0).getGreen());
      assertEquals(0, exampleImage.getLayer("red-layer").getPixelAt(0, 0).getBlue());
      assertEquals(0, exampleImage.getLayer("blue-layer").getPixelAt(0, 0).getRed());
      assertEquals(0, exampleImage.getLayer("blue-layer").getPixelAt(0, 0).getGreen());
      assertEquals(255, exampleImage.getLayer("blue-layer").getPixelAt(0, 0).getBlue());
      assertEquals(255, exampleImage.getLayer(2).getPixelAt(0, 0).getRed());
      assertEquals(0, exampleImage.getLayer(2).getPixelAt(0, 0).getGreen());
      assertEquals(0, exampleImage.getLayer(2).getPixelAt(0, 0).getBlue());
      assertEquals(0, exampleImage.getLayer(1).getPixelAt(0, 0).getRed());
      assertEquals(0, exampleImage.getLayer(1).getPixelAt(0, 0).getGreen());
      assertEquals(255, exampleImage.getLayer(1).getPixelAt(0, 0).getBlue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadImageAsLayerNullLayerName() {
      this.setUp();
      this.layeredImage0.loadImageAsLayer(null, "idk.ppm");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadImageAsLayerNullFileName() {
      this.setUp();
      this.layeredImage0.loadImageAsLayer("new", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadImageAsLayerBothNull() {
      this.setUp();
      this.layeredImage0.loadImageAsLayer(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadImageAsLayerInvalidLayerName() {
      this.setUp();
      this.layeredImage0.loadImageAsLayer("two words", "outputImages/example.ppm");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadImageAsLayerInvalidLayerNameNewLine() {
      this.setUp();
      this.layeredImage0.loadImageAsLayer("two\nwords", "outputImages/example.ppm");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadImageAsLayerNonExistentFile() {
      this.setUp();
      this.layeredImage0.loadImageAsLayer("new", "atlantis.png");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadImageAsLayerInvalidFileType() {
      this.setUp();
      this.layeredImage0.loadImageAsLayer("new", "outputImages/exampleLayeredImage/layerdata.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImageAsLayerWrongImageSize() {
      this.setUp();
      this.layeredImage0.loadImageAsLayer("new", "example.ppm");
    }

    @Test
    public void testLoadImageAsLayer() {
      LayeredImage example = new LayeredImageV0(2, 2);
      assertEquals(0, example.getNumLayers());
      assertEquals(new ArrayList<String>(), example.getLayerNames());
      example.loadImageAsLayer("new", "outputImages/example.ppm");
      assertEquals(1, example.getNumLayers());
      assertEquals(new ArrayList<String>(Arrays.asList("new")), example.getLayerNames());
      assertEquals(123, example.getLayer("new").getPixelAt(0, 0).getRed());
      assertEquals(123, example.getLayer("new").getPixelAt(0, 0).getGreen());
      assertEquals(123, example.getLayer("new").getPixelAt(0, 0).getBlue());
      assertEquals(211, example.getLayer("new").getPixelAt(1, 0).getRed());
      assertEquals(211, example.getLayer("new").getPixelAt(1, 0).getGreen());
      assertEquals(211, example.getLayer("new").getPixelAt(1, 0).getBlue());
      assertEquals(112, example.getLayer("new").getPixelAt(0, 1).getRed());
      assertEquals(112, example.getLayer("new").getPixelAt(0, 1).getGreen());
      assertEquals(112, example.getLayer("new").getPixelAt(0, 1).getBlue());
      assertEquals(121, example.getLayer("new").getPixelAt(1, 1).getRed());
      assertEquals(121, example.getLayer("new").getPixelAt(1, 1).getGreen());
      assertEquals(121, example.getLayer("new").getPixelAt(1, 1).getBlue());
      example.loadImageAsLayer("newPNG", "outputImages/example2.png");
      assertEquals(2, example.getNumLayers());
      assertEquals(new ArrayList<String>(Arrays.asList("newPNG", "new")), example.getLayerNames());
      assertEquals(55, example.getLayer("newPNG").getPixelAt(0, 0).getRed());
      assertEquals(66, example.getLayer("newPNG").getPixelAt(0, 0).getGreen());
      assertEquals(77, example.getLayer("newPNG").getPixelAt(0, 0).getBlue());
      assertEquals(55, example.getLayer("newPNG").getPixelAt(1, 0).getRed());
      assertEquals(66, example.getLayer("newPNG").getPixelAt(1, 0).getGreen());
      assertEquals(77, example.getLayer("newPNG").getPixelAt(1, 0).getBlue());
      assertEquals(55, example.getLayer("newPNG").getPixelAt(0, 1).getRed());
      assertEquals(66, example.getLayer("newPNG").getPixelAt(0, 1).getGreen());
      assertEquals(77, example.getLayer("newPNG").getPixelAt(0, 1).getBlue());
      assertEquals(55, example.getLayer("newPNG").getPixelAt(1, 1).getRed());
      assertEquals(66, example.getLayer("newPNG").getPixelAt(1, 1).getGreen());
      assertEquals(77, example.getLayer("newPNG").getPixelAt(1, 1).getBlue());
      example.loadImageAsLayer("newJPG", "outputImages/example.jpeg");
      assertEquals(3, example.getNumLayers());
      assertEquals(new ArrayList<String>(Arrays.asList("newJPG", "newPNG", "new")),
          example.getLayerNames());
      assertEquals(123, example.getLayer("newJPG").getPixelAt(0, 0).getRed(), 21);
      assertEquals(123, example.getLayer("newJPG").getPixelAt(0, 0).getGreen(), 21);
      assertEquals(123, example.getLayer("newJPG").getPixelAt(0, 0).getBlue(), 21);
      assertEquals(211, example.getLayer("newJPG").getPixelAt(1, 0).getRed(), 21);
      assertEquals(211, example.getLayer("newJPG").getPixelAt(1, 0).getGreen(), 21);
      assertEquals(211, example.getLayer("newJPG").getPixelAt(1, 0).getBlue(), 21);
      assertEquals(112, example.getLayer("newJPG").getPixelAt(0, 1).getRed(), 21);
      assertEquals(112, example.getLayer("newJPG").getPixelAt(0, 1).getGreen(), 21);
      assertEquals(112, example.getLayer("newJPG").getPixelAt(0, 1).getBlue(), 21);
      assertEquals(121, example.getLayer("newJPG").getPixelAt(1, 1).getRed(), 21);
      assertEquals(121, example.getLayer("newJPG").getPixelAt(1, 1).getGreen(), 21);
      assertEquals(121, example.getLayer("newJPG").getPixelAt(1, 1).getBlue(), 21);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsImageNullBlendType() {
      this.setUp();
      this.layeredImage0.saveAsImage(null, OutputType.png, "newImage");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsImageNullOutputType() {
      this.setUp();
      this.layeredImage0.saveAsImage(new BasicBlend(), null, "newImage");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsImageNullFileName() {
      this.setUp();
      this.layeredImage0.saveAsImage(new BasicBlend(), OutputType.png, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsImageAllNull() {
      this.setUp();
      this.layeredImage0.saveAsImage(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsImageNoLayer() {
      this.setUp();
      this.layeredImage0.saveAsImage(new BasicBlend(), OutputType.png, "yo");
    }

    @Test
    public void testSaveAsImage() {
      LayeredImage someOverlap = new LayeredImageV0(4, 1);
      someOverlap.addLayer("blue-layer");
      someOverlap.getLayer("blue-layer").getPixelAt(0, 0).setOpacity(255);
      someOverlap.getLayer("blue-layer").getPixelAt(1, 0).setOpacity(255);
      someOverlap.getLayer("blue-layer").getPixelAt(2, 0).setOpacity(255);
      someOverlap.getLayer("blue-layer").getPixelAt(3, 0).setOpacity(255);
      someOverlap.getLayer("blue-layer").getPixelAt(0, 0).updateColors(new SimplePixel(0, 0, 255));
      someOverlap.getLayer("blue-layer").getPixelAt(1, 0).updateColors(new SimplePixel(0, 0, 255));
      someOverlap.getLayer("blue-layer").getPixelAt(2, 0).updateColors(new SimplePixel(0, 0, 255));
      someOverlap.getLayer("blue-layer").getPixelAt(3, 0).updateColors(new SimplePixel(0, 0, 255));
      someOverlap.addLayer("green-layer");
      someOverlap.getLayer("green-layer").getPixelAt(0, 0).setOpacity(255);
      someOverlap.getLayer("green-layer").getPixelAt(1, 0).setOpacity(255);
      someOverlap.getLayer("green-layer").getPixelAt(2, 0).setOpacity(255);
      someOverlap.getLayer("green-layer").getPixelAt(0, 0).updateColors(new SimplePixel(0, 255, 0));
      someOverlap.getLayer("green-layer").getPixelAt(1, 0).updateColors(new SimplePixel(0, 255, 0));
      someOverlap.getLayer("green-layer").getPixelAt(2, 0).updateColors(new SimplePixel(0, 255, 0));
      someOverlap.addLayer("red-layer");
      someOverlap.getLayer("red-layer").getPixelAt(0, 0).setOpacity(255);
      someOverlap.getLayer("red-layer").getPixelAt(1, 0).setOpacity(255);
      someOverlap.getLayer("red-layer").getPixelAt(0, 0).updateColors(new SimplePixel(255, 0, 0));
      someOverlap.getLayer("red-layer").getPixelAt(1, 0).updateColors(new SimplePixel(255, 0, 0));
      someOverlap.addLayer("purple-layer");
      someOverlap.getLayer("purple-layer").getPixelAt(0, 0).setOpacity(255);
      someOverlap.getLayer("purple-layer").getPixelAt(0, 0)
          .updateColors(new SimplePixel(255, 0, 255));
      someOverlap.saveAsImage(new BasicBlend(), OutputType.png, "outputImages/layeredPNG");
      someOverlap.saveAsImage(new BasicBlend(), OutputType.ppm, "outputImages/layeredPPM");
      someOverlap.saveAsImage(new BasicBlend(), OutputType.jpeg, "outputImages/layeredJPG");
      GraphOfPixels result = ImageToGraphConverter.convertImage("outputImages/layeredPNG.png");
      assertEquals(255, result.getPixelAt(0, 0).getRed());
      assertEquals(0, result.getPixelAt(0, 0).getGreen());
      assertEquals(255, result.getPixelAt(0, 0).getBlue());
      assertEquals(255, result.getPixelAt(1, 0).getRed());
      assertEquals(0, result.getPixelAt(1, 0).getGreen());
      assertEquals(0, result.getPixelAt(1, 0).getBlue());
      assertEquals(0, result.getPixelAt(2, 0).getRed());
      assertEquals(255, result.getPixelAt(2, 0).getGreen());
      assertEquals(0, result.getPixelAt(2, 0).getBlue());
      assertEquals(0, result.getPixelAt(3, 0).getRed());
      assertEquals(0, result.getPixelAt(3, 0).getGreen());
      assertEquals(255, result.getPixelAt(3, 0).getBlue());
      result = ImageToGraphConverter.convertImage("outputImages/layeredPPM.ppm");
      assertEquals(255, result.getPixelAt(0, 0).getRed());
      assertEquals(0, result.getPixelAt(0, 0).getGreen());
      assertEquals(255, result.getPixelAt(0, 0).getBlue());
      assertEquals(255, result.getPixelAt(1, 0).getRed());
      assertEquals(0, result.getPixelAt(1, 0).getGreen());
      assertEquals(0, result.getPixelAt(1, 0).getBlue());
      assertEquals(0, result.getPixelAt(2, 0).getRed());
      assertEquals(255, result.getPixelAt(2, 0).getGreen());
      assertEquals(0, result.getPixelAt(2, 0).getBlue());
      assertEquals(0, result.getPixelAt(3, 0).getRed());
      assertEquals(0, result.getPixelAt(3, 0).getGreen());
      assertEquals(255, result.getPixelAt(3, 0).getBlue());
      result = ImageToGraphConverter.convertImage("outputImages/layeredJPG.jpeg");
      assertEquals(255, result.getPixelAt(0, 0).getRed(), 21);
      assertEquals(0, result.getPixelAt(0, 0).getGreen(), 21);
      assertEquals(141, result.getPixelAt(0, 0).getBlue());
      assertEquals(255, result.getPixelAt(1, 0).getRed(), 21);
      assertEquals(0, result.getPixelAt(1, 0).getGreen(), 21);
      assertEquals(116, result.getPixelAt(1, 0).getBlue(), 21);
      assertEquals(60, result.getPixelAt(2, 0).getRed(), 21);
      assertEquals(186, result.getPixelAt(2, 0).getGreen(), 21);
      assertEquals(185, result.getPixelAt(2, 0).getBlue(), 21);
      assertEquals(0, result.getPixelAt(3, 0).getRed(), 21);
      assertEquals(65, result.getPixelAt(3, 0).getGreen(), 21);
      assertEquals(64, result.getPixelAt(3, 0).getBlue(), 21);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsLayeredImageNullInput() {
      this.setUp();
      this.layeredImage0.saveAsLayeredImage(null);
    }

    @Test
    public void testSaveAsLayeredImage() {
      LayeredImage testLayered = new LayeredImageV0(20, 20);
      testLayered.addLayer("red-layer");
      testLayered.addLayer("blue-layer");
      testLayered.addLayer("invisible-layer");
      testLayered.setVisibility("invisible-layer", false);
      FixedSizeGraph redLayer = testLayered.getLayer("red-layer");
      for (Node n : redLayer) {
        n.setOpacity(255);
        n.updateColors(new SimplePixel(255, 0, 0));
      }
      FixedSizeGraph blueLayer = testLayered.getLayer("blue-layer");
      for (Node n : blueLayer) {
        n.setOpacity(255);
        n.updateColors(new SimplePixel(0, 0, 255));
      }
      testLayered.saveAsLayeredImage("outputImages/exampleLayeredImage");
      LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
      assertEquals(20, exampleImage.getWidth());
      assertEquals(20, exampleImage.getHeight());
      assertEquals(3, exampleImage.getNumLayers());
      assertEquals(
          new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
          exampleImage.getLayerNames());
      assertEquals(false, exampleImage.getVisibility("invisible-layer"));
      assertEquals(true, exampleImage.getVisibility("blue-layer"));
      assertEquals(true, exampleImage.getVisibility("red-layer"));
      for (Node n : exampleImage.getLayer("blue-layer")) {
        assertEquals(255, n.getOpacity());
        assertEquals(0, n.getRed());
        assertEquals(0, n.getGreen());
        assertEquals(255, n.getBlue());
      }
      for (Node n : exampleImage.getLayer("red-layer")) {
        assertEquals(255, n.getOpacity());
        assertEquals(255, n.getRed());
        assertEquals(0, n.getGreen());
        assertEquals(0, n.getBlue());
      }
      assertEquals(0, exampleImage.getLayer(0).getPixelAt(0, 0).getOpacity());
    }

    /**
     * For testing the LayeredImageIterator class.
     */
    public static class TestLayeredImageIter {

      @Test(expected = IllegalArgumentException.class)
      public void testNullConstructor() {
        new LayeredImageIterator(null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testNoMoreItems() {
        Iterator<FixedSizeGraph> testIter = new LayeredImageV0(10, 10).iterator();
        testIter.next();
      }

      @Test
      public void testIterator() {
        LayeredImage forIteration = new LayeredImageV0("outputImages/exampleLayeredImage");
        forIteration.moveLayer("red-layer", 0);
        Iterator<FixedSizeGraph> testIter = forIteration.iterator();
        assertEquals(true, testIter.hasNext());
        FixedSizeGraph layer0 = testIter.next();
        for (Node n : layer0) {
          assertEquals(255, n.getOpacity());
          assertEquals(255, n.getRed());
          assertEquals(0, n.getGreen());
          assertEquals(0, n.getBlue());
        }
        assertEquals(true, testIter.hasNext());
        FixedSizeGraph layer1 = testIter.next();
        for (Node n : layer1) {
          assertEquals(0, n.getOpacity());
          assertEquals(0, n.getRed());
          assertEquals(0, n.getGreen());
          assertEquals(0, n.getBlue());
        }
        FixedSizeGraph layer2 = testIter.next();
        for (Node n : layer2) {
          assertEquals(255, n.getOpacity());
          assertEquals(0, n.getRed());
          assertEquals(0, n.getGreen());
          assertEquals(255, n.getBlue());
        }
        assertEquals(false, testIter.hasNext());
      }
    }
  }

  /**
   * To test the MatrixImpl class.
   */
  public static class TestMatrix {

    private Matrix m0;
    private Matrix m1;
    private Matrix m2;
    private Matrix m3;

    /**
     * To initialize test variables.
     */
    private void setUp() {
      this.m0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0)), 2, 2);
      this.m1 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
          3, 2);
      this.m2 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
          2, 3);
      this.m3 = new MatrixImpl(
          new ArrayList<Double>(Arrays.asList(1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8,
              9.9)), 3, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
      Matrix mat0 = new MatrixImpl(null, 2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDouble() {
      ArrayList<Double> withNull = new ArrayList<Double>();
      withNull.add(null);
      Matrix mat = new MatrixImpl(withNull, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientValues() {
      Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0)), 2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooManyValues() {
      Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0)),
          1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroWidth() {
      Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0)), 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroHeight() {
      Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0)), 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWidth() {
      Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0)), -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeHeight() {
      Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0)), 2, -1);
    }

    @Test
    public void testConstruction() {
      Matrix mat0 = new MatrixImpl(
          new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
          2, 3);
      assertEquals(2, mat0.getWidth());
      assertEquals(3, mat0.getHeight());
      assertEquals(1.0, mat0.getValue(0, 0), 0.1);
      assertEquals(2.0, mat0.getValue(1, 0), 0.1);
      assertEquals(3.0, mat0.getValue(0, 1), 0.1);
      assertEquals(4.0, mat0.getValue(1, 1), 0.1);
      assertEquals(5.0, mat0.getValue(0, 2), 0.1);
      assertEquals(6.0, mat0.getValue(1, 2), 0.1);
    }

    @Test
    public void testGetWidth() {
      this.setUp();
      assertEquals(2, m0.getWidth());
      assertEquals(3, m1.getWidth());
    }

    @Test
    public void getHeight() {
      this.setUp();
      assertEquals(2, m0.getHeight());
      assertEquals(2, m1.getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetXOutOfBounds() {
      this.setUp();
      this.m0.getValue(5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetYOutOfBounds() {
      this.setUp();
      this.m0.getValue(0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNegativeX() {
      this.setUp();
      this.m0.getValue(-3, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNegativeY() {
      this.setUp();
      this.m0.getValue(0, -2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNegativeBoth() {
      this.setUp();
      this.m0.getValue(-3, -2);
    }

    @Test
    public void testGetValue() {
      this.setUp();
      assertEquals(1.0, this.m0.getValue(0, 0), 0.1);
      assertEquals(2.0, this.m1.getValue(1, 0), 0.1);
      assertEquals(4.0, this.m2.getValue(1, 1), 0.1);
      assertEquals(9.9, this.m3.getValue(2, 2), 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSecond() {
      this.setUp();
      this.m0.matrixMultiply(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFullyInvalidMultiplication() {
      this.setUp();
      this.m0.matrixMultiply(m3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMultiplication() {
      this.setUp();
      this.m0.matrixMultiply(m2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMultiplicationOrder() {
      this.setUp();
      this.m1.matrixMultiply(m0);
    }

    @Test
    public void testMultiplication() {
      this.setUp();
      Matrix result = this.m0.matrixMultiply(m1);
      assertEquals(3, result.getWidth());
      assertEquals(2, result.getHeight());
      assertEquals(9.0, result.getValue(0, 0), 0.1);
      assertEquals(12.0, result.getValue(1, 0), 0.1);
      assertEquals(15.0, result.getValue(2, 0), 0.1);
      assertEquals(19.0, result.getValue(0, 1), 0.1);
      assertEquals(26.0, result.getValue(1, 1), 0.1);
      assertEquals(33.0, result.getValue(2, 1), 0.1);
      Matrix result2 = this.m2.matrixMultiply(m1);
      assertEquals(3, result2.getWidth());
      assertEquals(3, result2.getHeight());
      assertEquals(9.0, result2.getValue(0, 0), 0.1);
      assertEquals(12.0, result2.getValue(1, 0), 0.1);
      assertEquals(15.0, result2.getValue(2, 0), 0.1);
      assertEquals(19.0, result2.getValue(0, 1), 0.1);
      assertEquals(26.0, result2.getValue(1, 1), 0.1);
      assertEquals(33.0, result2.getValue(2, 1), 0.1);
      assertEquals(29.0, result2.getValue(0, 2), 0.1);
      assertEquals(40.0, result2.getValue(1, 2), 0.1);
      assertEquals(51.0, result2.getValue(2, 2), 0.1);
    }
  }

  /**
   * For testing the two implementations of the Node interface.
   */
  public static class TestNode {

    private Node empty0;
    private Node pn0;
    private Node pn1;
    private Node pn2;
    private GraphOfPixels graphForTestingNeighbors;

    /**
     * Initializes variables for testing.
     */
    private void setUp() {
      this.empty0 = new EmptyNode();
      this.pn0 = new PixelNode(new SimplePixel(3, 4, 5));
      this.pn1 = new PixelNode(new SimplePixel(4, 5, 6));
      this.pn2 = new PixelNode(new SimplePixel(5, 6, 7));
      this.graphForTestingNeighbors = ImageToGraphConverter.createEmptyGraph();
      this.graphForTestingNeighbors.insertColumn(0);
      this.graphForTestingNeighbors.insertColumn(1);
      this.graphForTestingNeighbors.insertRow(0);
      this.graphForTestingNeighbors.insertRow(1);
      this.graphForTestingNeighbors.getPixelAt(0, 0).updateColors(
          new SimplePixel(1, 1, 1));
      this.graphForTestingNeighbors.getPixelAt(1, 0).updateColors(
          new SimplePixel(2, 2, 2));
      this.graphForTestingNeighbors.getPixelAt(2, 0).updateColors(
          new SimplePixel(3, 3, 3));
      this.graphForTestingNeighbors.getPixelAt(0, 1).updateColors(
          new SimplePixel(4, 4, 4));
      this.graphForTestingNeighbors.getPixelAt(1, 1).updateColors(
          new SimplePixel(5, 5, 5));
      this.graphForTestingNeighbors.getPixelAt(2, 1).updateColors(
          new SimplePixel(6, 6, 6));
      this.graphForTestingNeighbors.getPixelAt(0, 2).updateColors(
          new SimplePixel(7, 7, 7));
      this.graphForTestingNeighbors.getPixelAt(1, 2).updateColors(
          new SimplePixel(8, 8, 8));
      this.graphForTestingNeighbors.getPixelAt(2, 2).updateColors(
          new SimplePixel(9, 9, 9));
    }

    @Test
    public void testConstructionEmpty() {
      this.setUp();
      assertEquals(new EmptyNode(), new EmptyNode());
      assertEquals(0, this.empty0.getBlue());
      assertEquals(0, this.empty0.getGreen());
      assertEquals(0, this.empty0.getRed());
    }

    @Test
    public void testConstructionPixel() {
      this.setUp();
      SimplePixel p0 = new SimplePixel(5, 6, 7);
      Node n0 = new PixelNode(p0);
      assertEquals(5, n0.getRed());
      assertEquals(6, n0.getGreen());
      assertEquals(7, n0.getBlue());
      p0.setRGB(7, 7, 7);
      assertEquals(5, n0.getRed());
      assertEquals(6, n0.getGreen());
      assertEquals(7, n0.getBlue());
      assertEquals(this.empty0, n0.getAbove());
      assertEquals(this.empty0, n0.getBelow());
      assertEquals(this.empty0, n0.getLeft());
      assertEquals(this.empty0, n0.getRight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullPixel() {
      Node n0 = new PixelNode(null);
    }

    @Test
    public void testGetColors() {
      this.setUp();
      // FOR EMPTY
      assertEquals(0, this.empty0.getBlue());
      assertEquals(0, this.empty0.getGreen());
      assertEquals(0, this.empty0.getRed());
      // FOR PIXELNODE
      assertEquals(3, this.pn0.getRed());
      assertEquals(5, this.pn1.getGreen());
      assertEquals(7, this.pn2.getBlue());
    }

    @Test
    public void testUpdateColors() {
      this.setUp();
      // FOR EMPTY
      this.empty0.updateColors(new SimplePixel(8, 9, 20));
      assertEquals(0, this.empty0.getRed());
      assertEquals(0, this.empty0.getGreen());
      assertEquals(0, this.empty0.getBlue());
      // FOR PIXEL
      PixelAsColors newPixel = new SimplePixel(6, 7, 8);
      this.pn0.updateColors(newPixel);
      assertEquals(6, pn0.getRed());
      assertEquals(7, pn0.getGreen());
      assertEquals(8, pn0.getBlue());
      newPixel.setRGB(25, 25, 25);
      assertEquals(6, pn0.getRed());
      assertEquals(7, pn0.getGreen());
      assertEquals(8, pn0.getBlue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUpdateEmpty() {
      Node n0 = new EmptyNode();
      n0.updateColors(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUpdatePixel() {
      Node n0 = new PixelNode(new SimplePixel(1, 2, 23));
      n0.updateColors(null);
    }

    @Test
    public void testEditColors() {
      this.setUp();
      // FOR EMPTY
      this.empty0.editColors(5, -7, 33);
      assertEquals(0, this.empty0.getRed());
      assertEquals(0, this.empty0.getGreen());
      assertEquals(0, this.empty0.getBlue());
      // FOR PIXELNODE
      this.pn0.editColors(-300, 10, 251);
      assertEquals(0, this.pn0.getRed());
      assertEquals(14, this.pn0.getGreen());
      assertEquals(255, this.pn0.getBlue());
      this.pn1.editColors(10, 300, -17);
      assertEquals(14, this.pn1.getRed());
      assertEquals(255, this.pn1.getGreen());
      assertEquals(0, this.pn1.getBlue());
      this.pn2.editColors(777, -82, 11);
      assertEquals(255, this.pn2.getRed());
      assertEquals(0, this.pn2.getGreen());
      assertEquals(18, this.pn2.getBlue());
    }

    @Test
    public void testGetNeighbors() {
      this.setUp();
      // FOR EMPTYNODE
      assertEquals(new EmptyNode(), empty0.getAbove());
      assertEquals(new EmptyNode(), empty0.getBelow());
      assertEquals(new EmptyNode(), empty0.getLeft());
      assertEquals(new EmptyNode(), empty0.getRight());
      assertEquals(new EmptyNode(), empty0.getNearby(-2, 0));
      assertEquals(new EmptyNode(), empty0.getNearby(0, 2));
      // FOR PIXELNODE
      Node forNeighbors = this.graphForTestingNeighbors.getPixelAt(1, 1);
      assertEquals(this.graphForTestingNeighbors.getPixelAt(1, 0),
          forNeighbors.getAbove());
      assertEquals(this.graphForTestingNeighbors.getPixelAt(0, 1),
          forNeighbors.getLeft());
      assertEquals(this.graphForTestingNeighbors.getPixelAt(2, 1),
          forNeighbors.getRight());
      assertEquals(this.graphForTestingNeighbors.getPixelAt(1, 2),
          forNeighbors.getBelow());
      assertEquals(this.graphForTestingNeighbors.getPixelAt(0, 0),
          forNeighbors.getNearby(-1, 1));
      assertEquals(this.graphForTestingNeighbors.getPixelAt(2, 2),
          forNeighbors.getNearby(1, -1));
      assertEquals(new EmptyNode(),
          forNeighbors.getNearby(2, 0));
    }

    @Test
    public void testGetOpacity() {
      this.setUp();
      //New nodes are created with no transparency.
      assertEquals(255, pn0.getOpacity());
      assertEquals(3, pn0.getRed());
      assertEquals(4, pn0.getGreen());
      assertEquals(5, pn0.getBlue());

      pn0.setOpacity(123);
      assertEquals(123, pn0.getOpacity());
      assertEquals(3, pn0.getRed());
      assertEquals(4, pn0.getGreen());
      assertEquals(5, pn0.getBlue());

      //Empty nodes have max transparency.
      assertEquals(0, empty0.getOpacity());
      assertEquals(0, empty0.getRed());
      assertEquals(0, empty0.getGreen());
      assertEquals(0, empty0.getBlue());
    }

    @Test
    public void testSetOpacity() {
      this.setUp();
      //Ensure new node has no transparency.
      assertEquals(255, pn0.getOpacity());
      assertEquals(3, pn0.getRed());
      assertEquals(4, pn0.getGreen());
      assertEquals(5, pn0.getBlue());

      pn0.setOpacity(123);
      assertEquals(123, pn0.getOpacity());
      assertEquals(3, pn0.getRed());
      assertEquals(4, pn0.getGreen());
      assertEquals(5, pn0.getBlue());

      pn0.setOpacity(0);
      assertEquals(0, pn0.getOpacity());
      assertEquals(0, pn0.getRed());
      assertEquals(0, pn0.getGreen());
      assertEquals(0, pn0.getBlue());

      //Empty nodes ALWAYS have max transparency.
      assertEquals(0, empty0.getOpacity());
      assertEquals(0, empty0.getRed());
      assertEquals(0, empty0.getGreen());
      assertEquals(0, empty0.getBlue());
      empty0.setOpacity(123);
      assertEquals(0, empty0.getOpacity());
      assertEquals(0, empty0.getRed());
      assertEquals(0, empty0.getGreen());
      assertEquals(0, empty0.getBlue());
    }


    @Test
    public void testIsTransparent() {
      this.setUp();
      //New nodes are created with no transparency.
      assertFalse(pn0.isTransparent());

      pn0.setOpacity(123);
      assertFalse(pn0.isTransparent());

      //Empty nodes have max transparency.
      assertTrue(empty0.isTransparent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPixelNegativeOpacity() {
      this.setUp();
      pn0.setOpacity(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPixelTooLargeOpacity() {
      this.setUp();
      pn0.setOpacity(10000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyNegativeOpacity() {
      this.setUp();
      empty0.setOpacity(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyTooLargeOpacity() {
      this.setUp();
      empty0.setOpacity(10000);
    }

  }

  /**
   * Tests that the Controller class properly gets constructed and properly handles inputs and
   * outputs.
   */
  public static class TestProcessingController {

    private ImageProcessingController testReadable;
    private ImageProcessingController testFile;
    private Appendable testOutputReadable;
    private Appendable testOutputFile;


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullInputString() {
      ImageProcessingController fail = new ProcessingController((String) null, System.out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullInputReadable() {
      ImageProcessingController fail = new ProcessingController((Readable) null, System.out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionReadableNullOutput() {
      ImageProcessingController fail = new ProcessingController(new InputStreamReader(System.in),
          null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionFileNullOutput() {
      ImageProcessingController fail = new ProcessingController("outputImages/TestScript.txt",
          null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionInvalidFile() {
      ImageProcessingController fail = new ProcessingController("non-existent", System.out);
    }

    /**
     * Initializes test structures and proxies.
     */
    private void setUp() {
      String testInputContent = "testScript\n"
          + "\n"
          + "#Test Command\n"
          + "create-image from-image test1 outputImages/conversionTest.ppm\n"
          + "load test1\n"
          + "apply-mutator greyscale\n"
          + "save test1 png outputImages/testControllerReadable\n";
      Readable testInput = new StringReader(testInputContent);
      testOutputReadable = new StringBuilder();
      testOutputFile = new StringBuilder();
      testReadable = new ProcessingController(testInput, testOutputReadable);
      testFile = new ProcessingController("outputImages/TestScriptFile.txt", testOutputFile);
    }

    @Test
    public void testRunFile() {
      this.setUp();
      testFile.run();
      assertEquals(
          "Invalid line 0: Unsupported command given\n"
              + "Image Processor Quit",
          testOutputFile.toString());
      LayeredImage newLayeredImage = new LayeredImageV0("outputImages/testControllerFile");
      assertEquals(2, newLayeredImage.getNumLayers());
      assertEquals(1024, newLayeredImage.getWidth());
      assertEquals(768, newLayeredImage.getHeight());

      assertEquals("birb", newLayeredImage.getLayerNames().get(1));
      assertEquals("rainbow", newLayeredImage.getLayerNames().get(0));
      assertEquals(1024, newLayeredImage.getWidth());

      assertTrue(newLayeredImage.getVisibility("birb"));
      assertTrue(newLayeredImage.getVisibility("rainbow"));

      GraphOfPixels newImageAsSingle = ImageToGraphConverter
          .convertImage("outputImages/testControllerFile/testControllerFile.png");
      assertEquals(1024, newImageAsSingle.getWidth());
      assertEquals(768, newImageAsSingle.getHeight());
    }

    @Test
    public void testRunReadable() {
      this.setUp();
      testReadable.run();
      assertEquals(
          "Invalid line 0: Unsupported command given\n", testOutputReadable.toString());
      GraphOfPixels newImage = ImageToGraphConverter
          .convertImage("outputImages/testControllerReadable.png");
      assertEquals(3, newImage.getWidth());
      assertEquals(3, newImage.getHeight());
      assertEquals(54, newImage.getPixelAt(0, 0).getRed());
      assertEquals(54, newImage.getPixelAt(0, 0).getGreen());
      assertEquals(54, newImage.getPixelAt(0, 0).getBlue());
    }

  }

  /**
   * Holds test classes for all other script command objects.
   */
  public static class TestScriptCommand {

    /**
     * Tests the functionality of moving a layer in a layered image to a different position through
     * scripts.
     */
    public static class TestMoveLayerCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private ParsedCommand failExecutableNonExistentImage;
      private ParsedCommand failExecutableNonExistentLayer;
      private ParsedCommand failExecutableNegativeIndex;
      private ParsedCommand failExecutableIndexTooLarge;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        LayeredImage ex1 = new LayeredImageV0(1, 1);
        ex1.addLayer("secondLayer");
        ex1.addLayer("firstLayer");
        layeredImages.put("existing", ex1);
        newExecutableCommand = new MoveLayerCommand("existing", "secondLayer", 0);
        failExecutableNonExistentImage = new MoveLayerCommand("non-existent", "secondLayer", 0);
        failExecutableNonExistentLayer = new MoveLayerCommand("existing", "non-existent", 0);
        failExecutableNegativeIndex = new MoveLayerCommand("existing", "non-existent", -3);
        failExecutableIndexTooLarge = new MoveLayerCommand("existing", "non-existent", 2);
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new MoveLayerCommand(null, "secondLayer", 0);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullLayerName() {
        ParsedCommand fail = new MoveLayerCommand("existing", null, 0);
      }


      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        //GetLayerNames iterates through a layered image and PRINTS THE LAYERS FROM TOP TO BOTTOM
        assertEquals(2, layeredImages.get("existing").getNumLayers());
        assertEquals("firstLayer", layeredImages.get("existing").getLayerNames().get(0));
        newExecutableCommand.execute(graphs, layeredImages);
        assertEquals(2, layeredImages.get("existing").getNumLayers());
        assertEquals("secondLayer", layeredImages.get("existing").getLayerNames().get(0));
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageDoesNotExist() {
        this.setUp();
        failExecutableNonExistentImage.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailLayerDoesNotExist() {
        this.setUp();
        failExecutableNonExistentLayer.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailDestIndexNegative() {
        this.setUp();
        failExecutableNegativeIndex.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailLayerDestIndexTooLarge() {
        this.setUp();
        failExecutableIndexTooLarge.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of creating a new layered image through scripts.
     */
    public static class TestCreateNewLayeredImageCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();

        newExecutableCommand = new CreateNewLayeredImageCommand("new", 1, 1);
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionInvalidWidth() {
        ParsedCommand fail = new CreateNewLayeredImageCommand("new", 0, 1);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionInvalidHeight() {
        ParsedCommand fail = new CreateNewLayeredImageCommand("new", 1, -3);
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new CreateNewLayeredImageCommand(null, 1, 1);
      }

      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertFalse(layeredImages.containsKey("new"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertTrue(layeredImages.containsKey("new"));
        LayeredImage newlyCreated = layeredImages.get("new");
        assertEquals(1, newlyCreated.getWidth());
        assertEquals(1, newlyCreated.getHeight());

        assertEquals(0, newlyCreated.getNumLayers());

      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageNameAlreadyExists() {
        this.setUp();
        newExecutableCommand.execute(graphs, layeredImages);
        //Running the same command again should cause a problem.
        newExecutableCommand.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of blending layers of an image into one image through scripts.
     */
    public static class TestBasicBlendCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newFailCommandNonExistingDest;
      private ParsedCommand newExecutableCommand;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        LayeredImage ex1 = new LayeredImageV0(2, 2);
        ex1.addLayer("new");
        ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
        ex1.getLayer(0).getPixelAt(1, 0).setOpacity(255);
        ex1.getLayer(0).getPixelAt(0, 1).setOpacity(255);
        ex1.getLayer(0).getPixelAt(1, 1).setOpacity(255);
        ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(1, 1, 1));
        ex1.getLayer(0).getPixelAt(1, 0).updateColors(new SimplePixel(2, 2, 2));
        ex1.getLayer(0).getPixelAt(0, 1).updateColors(new SimplePixel(3, 3, 3));
        ex1.getLayer(0).getPixelAt(1, 1).updateColors(new SimplePixel(4, 4, 4));
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        layeredImages.put("existing", ex1);

        newFailCommandNonExistingDest = new BasicBlendCommand("non-existing", "png",
            "outputImages/birb.jpg");
        newExecutableCommand = new BasicBlendCommand("existing", "png", "outputImages/testBlend");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageBlending() {
        ParsedCommand fail = new BasicBlendCommand(null, "new", "out");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullOutputType() {
        ParsedCommand fail = new BasicBlendCommand("existing", null, "out");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullFileName() {
        ParsedCommand fail = new BasicBlendCommand("existing", "new", null);
      }

      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        newExecutableCommand.execute(graphs, layeredImages);
        GraphOfPixels exported = ImageToGraphConverter.convertImage("outputImages/testBlend.png");
        assertEquals(1, exported.getPixelAt(0, 0).getRed());
        assertEquals(1, exported.getPixelAt(0, 0).getGreen());
        assertEquals(1, exported.getPixelAt(0, 0).getBlue());

        assertEquals(2, exported.getPixelAt(1, 0).getRed());
        assertEquals(2, exported.getPixelAt(1, 0).getGreen());
        assertEquals(2, exported.getPixelAt(1, 0).getBlue());

        assertEquals(3, exported.getPixelAt(0, 1).getRed());
        assertEquals(3, exported.getPixelAt(0, 1).getGreen());
        assertEquals(3, exported.getPixelAt(0, 1).getBlue());

        assertEquals(4, exported.getPixelAt(1, 1).getRed());
        assertEquals(4, exported.getPixelAt(1, 1).getGreen());
        assertEquals(4, exported.getPixelAt(1, 1).getBlue());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidDestination() {
        this.setUp();
        newFailCommandNonExistingDest.execute(graphs, layeredImages);
      }

      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of loading an image as the current image through scripts.
     */
    public static class TestLoadCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        LayeredImage ex1 = new LayeredImageV0(1, 1);
        ex1.addLayer("layer");
        ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
        ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(123, 123, 123));
        layeredImages.put("existing", ex1);

        newExecutableCommand = new LoadCommand("existing");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageToLoad() {
        ParsedCommand fail = new LoadCommand(null);
      }

      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class has NO EXECUTE FUNCTIONALITY. IT DOES ALTER LANGUAGE STATE.
        // so we should catch an IllegalArgumentException both before its execution, but not after!
        this.setUp();

        try {
          test.parseCommand("save-layered outputImages/updateCurrent")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
          //This catch clause should be run, allowing the program to continue.
        }

        test.parseCommand("set-current-layer layer").alterLanguageState(test);

        try {
          //This command should update the current reference and thus the below command should pass.
          //No exception in the try clause should be logged.
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
        } catch (IllegalArgumentException e) {
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          //This command should update the current reference and thus the below command should pass.
          //No exception in the try clause should be logged.
          test.parseCommand("save-layered outputImages/updateCurrent")
              .execute(graphs, layeredImages);
        } catch (IllegalArgumentException e) {
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");

        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //This command should fail to update the current reference, and thus the catch block
          //should be run.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of creating a new layered image from an existing one through
     * scripts.
     */
    public static class TestImportNewLayeredImageCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();

        newExecutableCommand = new ImportNewLayeredImageCommand("new", "outputImages/misc");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageSource() {
        ParsedCommand fail = new ImportNewLayeredImageCommand("new", null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionInvalidImageSource() {
        ParsedCommand fail = new ImportNewLayeredImageCommand("new", "where/what");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new ImportNewLayeredImageCommand(null, "outputImages/misc");
      }

      @Test
      public void testSuccessfulExecute() {
        //The misc layered image has 3 layers, one named birb,
        // one named galaxy, and one named rainbow
        //Only the galaxy layer is invisible
        //The size is 1024x768
        this.setUp();
        assertFalse(layeredImages.containsKey("new"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertTrue(layeredImages.containsKey("new"));
        LayeredImage newlyCreated = layeredImages.get("new");
        assertEquals(1024, newlyCreated.getWidth());
        assertEquals(768, newlyCreated.getHeight());

        assertEquals(3, newlyCreated.getNumLayers());
        assertTrue(newlyCreated.getLayerNames().contains("birb"));
        assertTrue(newlyCreated.getLayerNames().contains("galaxy"));
        assertTrue(newlyCreated.getLayerNames().contains("rainbow"));

        assertTrue(newlyCreated.getVisibility("birb"));
        assertFalse(newlyCreated.getVisibility("galaxy"));
        assertTrue(newlyCreated.getVisibility("rainbow"));

      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageNameAlreadyExists() {
        this.setUp();
        newExecutableCommand.execute(graphs, layeredImages);
        //Running the same command again should cause a problem.
        newExecutableCommand.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of creating an image from an existing one through scripts.
     */
    public static class TestCreateFromImageCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();

        newExecutableCommand = new CreateFromImageCommand("new", "outputImages/testBlend.png");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageSource() {
        ParsedCommand fail = new CreateFromImageCommand("new", null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionInvalidImageSource() {
        ParsedCommand fail = new CreateFromImageCommand("new", "where/what");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new CreateFromImageCommand(null, "outputImages/testBlend.png");
      }

      @Test
      public void testSuccessfulExecute() {
        //Note that testBlend is a 2x2 pixel image with r, g, and b values as follows:
        //1 for all channels at pixel (0, 0)
        //2 for all channels at pixel (1, 0)
        //3 for all channels at pixel (0, 1)
        //4 for all channels at pixel (1, 1)
        this.setUp();
        assertFalse(graphs.containsKey("new"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertTrue(graphs.containsKey("new"));
        GraphOfPixels newlyCreated = graphs.get("new");
        assertEquals(2, newlyCreated.getWidth());
        assertEquals(2, newlyCreated.getHeight());

        assertEquals(1, newlyCreated.getPixelAt(0, 0).getRed());
        assertEquals(1, newlyCreated.getPixelAt(0, 0).getGreen());
        assertEquals(1, newlyCreated.getPixelAt(0, 0).getBlue());

        assertEquals(2, newlyCreated.getPixelAt(1, 0).getRed());
        assertEquals(2, newlyCreated.getPixelAt(1, 0).getGreen());
        assertEquals(2, newlyCreated.getPixelAt(1, 0).getBlue());

        assertEquals(3, newlyCreated.getPixelAt(0, 1).getRed());
        assertEquals(3, newlyCreated.getPixelAt(0, 1).getGreen());
        assertEquals(3, newlyCreated.getPixelAt(0, 1).getBlue());

        assertEquals(4, newlyCreated.getPixelAt(1, 1).getRed());
        assertEquals(4, newlyCreated.getPixelAt(1, 1).getGreen());
        assertEquals(4, newlyCreated.getPixelAt(1, 1).getBlue());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageNameAlreadyExists() {
        this.setUp();
        newExecutableCommand.execute(graphs, layeredImages);
        //Running the same command again should cause a problem.
        newExecutableCommand.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of making an image greyscale through scripts.
     */
    public static class TestGreyscaleCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newFailCommandNonExistingDestImage;
      private ParsedCommand newFailCommandNonExistingDestLayer;
      private ParsedCommand newExecutableCommandSingle;
      private ParsedCommand newExecutableCommandLayer;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        LayeredImage ex1 = new LayeredImageV0(1024, 768);
        ex1.addLayer("new");
        ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
        ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(16, 32, 64));
        GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
        ex2.getPixelAt(0, 0).updateColors(new SimplePixel(16, 32, 64));

        graphs = new HashMap<String, GraphOfPixels>();
        graphs.put("existingImage", ex2);
        layeredImages = new HashMap<String, LayeredImage>();
        layeredImages.put("existing", ex1);

        newFailCommandNonExistingDestImage = new GreyscaleCommand("non-existing", "new");
        newFailCommandNonExistingDestLayer = new GreyscaleCommand("existing", "none");
        newExecutableCommandSingle = new GreyscaleCommand("existingImage", null);
        newExecutableCommandLayer = new GreyscaleCommand("existing", "new");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageAdding() {
        ParsedCommand fail = new GreyscaleCommand(null, "birb");
      }


      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getRed());
        assertEquals(32, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
        assertEquals(64, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
        newExecutableCommandSingle.execute(graphs, layeredImages);
        assertEquals(31, graphs.get("existingImage").getPixelAt(0, 0).getRed());
        assertEquals(31, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
        assertEquals(31, graphs.get("existingImage").getPixelAt(0, 0).getBlue());

        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
        assertEquals(32, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
        assertEquals(64, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
        newExecutableCommandLayer.execute(graphs, layeredImages);
        assertEquals(31, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
        assertEquals(31, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
        assertEquals(31, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidDestination() {
        this.setUp();
        newFailCommandNonExistingDestImage.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidSource() {
        this.setUp();
        newFailCommandNonExistingDestLayer.execute(graphs, layeredImages);
      }

      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommandSingle.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommandSingle.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of creating an empty image through scripts.
     */
    public static class TestCreateEmptyImageCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        newExecutableCommand = new CreateEmptyImageCommand("new");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new CreateEmptyImageCommand(null);
      }


      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertFalse(graphs.containsKey("new"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertTrue(graphs.containsKey("new"));
        GraphOfPixels newlyCreated = graphs.get("new");
        assertEquals(1, newlyCreated.getWidth());
        assertEquals(1, newlyCreated.getHeight());
        assertEquals(255, newlyCreated.getPixelAt(0, 0).getRed());
        assertEquals(255, newlyCreated.getPixelAt(0, 0).getGreen());
        assertEquals(255, newlyCreated.getPixelAt(0, 0).getBlue());
        assertEquals(255, newlyCreated.getPixelAt(0, 0).getOpacity());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageNameAlreadyExists() {
        this.setUp();
        newExecutableCommand.execute(graphs, layeredImages);
        //Running the same command again should cause a problem.
        newExecutableCommand.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of creating a checkerboard image through scripts.
     */
    public static class TestCreateCheckerBoardCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        newExecutableCommand = new CreateCheckerBoardCommand("existing", 1, 4, 0, 0, 0, 255, 255,
            255);
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new CreateCheckerBoardCommand(null, 1, 4, 0, 0, 0, 255, 255, 255);
      }


      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertFalse(graphs.containsKey("existing"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertTrue(graphs.containsKey("existing"));
        GraphOfPixels newlyCreated = graphs.get("existing");
        assertEquals(0, newlyCreated.getPixelAt(0, 0).getRed());
        assertEquals(0, newlyCreated.getPixelAt(0, 0).getGreen());
        assertEquals(0, newlyCreated.getPixelAt(0, 0).getBlue());

        assertEquals(255, newlyCreated.getPixelAt(1, 0).getRed());
        assertEquals(255, newlyCreated.getPixelAt(1, 0).getGreen());
        assertEquals(255, newlyCreated.getPixelAt(1, 0).getBlue());

        assertEquals(255, newlyCreated.getPixelAt(0, 1).getRed());
        assertEquals(255, newlyCreated.getPixelAt(0, 1).getGreen());
        assertEquals(255, newlyCreated.getPixelAt(0, 1).getBlue());

        assertEquals(0, newlyCreated.getPixelAt(1, 1).getRed());
        assertEquals(0, newlyCreated.getPixelAt(1, 1).getGreen());
        assertEquals(0, newlyCreated.getPixelAt(1, 1).getBlue());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageNameAlreadyExists() {
        this.setUp();
        newExecutableCommand.execute(graphs, layeredImages);
        newExecutableCommand.execute(graphs, layeredImages);
      }

      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of creating a copy of an existing image through scripts.
     */
    public static class TestCreateCopyCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private ParsedCommand failCommandNonExistingFile;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        GraphOfPixels ex1 = ImageToGraphConverter.createEmptyGraph();
        ex1.getPixelAt(0, 0).setOpacity(255);
        ex1.getPixelAt(0, 0).updateColors(new SimplePixel(123, 132, 231));

        graphs = new HashMap<String, GraphOfPixels>();
        graphs.put("original", ex1);
        layeredImages = new HashMap<String, LayeredImage>();
        newExecutableCommand = new CreateCopyCommand("copy", "original");
        failCommandNonExistingFile = new CreateCopyCommand("copy", "non-existent");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new CreateCopyCommand(null, "original");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageToCopy() {
        ParsedCommand fail = new CreateCopyCommand("copy", null);
      }


      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertFalse(graphs.containsKey("copy"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertTrue(graphs.containsKey("copy"));
        GraphOfPixels newlyCreated = graphs.get("copy");
        assertEquals(1, newlyCreated.getWidth());
        assertEquals(1, newlyCreated.getHeight());
        assertEquals(123, newlyCreated.getPixelAt(0, 0).getRed());
        assertEquals(132, newlyCreated.getPixelAt(0, 0).getGreen());
        assertEquals(231, newlyCreated.getPixelAt(0, 0).getBlue());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailCopyNameAlreadyExists() {
        this.setUp();
        newExecutableCommand.execute(graphs, layeredImages);
        //Running the same command again should cause a problem.
        newExecutableCommand.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailOriginalFileDoesNotExist() {
        this.setUp();
        failCommandNonExistingFile.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of blurring an image through scripts.
     */
    public static class TestBlurCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newFailCommandNonExistingDestImage;
      private ParsedCommand newFailCommandNonExistingDestLayer;
      private ParsedCommand newExecutableCommandSingle;
      private ParsedCommand newExecutableCommandLayer;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        LayeredImage ex1 = new LayeredImageV0(1024, 768);
        ex1.addLayer("new");
        ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
        ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(16, 16, 16));
        GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
        ex2.getPixelAt(0, 0).updateColors(new SimplePixel(16, 16, 16));

        graphs = new HashMap<String, GraphOfPixels>();
        graphs.put("existingImage", ex2);
        layeredImages = new HashMap<String, LayeredImage>();
        layeredImages.put("existing", ex1);

        newFailCommandNonExistingDestImage = new BlurCommand("non-existing", "new");
        newFailCommandNonExistingDestLayer = new BlurCommand("existing", "none");
        newExecutableCommandSingle = new BlurCommand("existingImage", null);
        newExecutableCommandLayer = new BlurCommand("existing", "new");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageAdding() {
        ParsedCommand fail = new BlurCommand(null, "birb");
      }


      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getRed());
        assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
        assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
        newExecutableCommandSingle.execute(graphs, layeredImages);
        assertEquals(4, graphs.get("existingImage").getPixelAt(0, 0).getRed());
        assertEquals(4, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
        assertEquals(4, graphs.get("existingImage").getPixelAt(0, 0).getBlue());

        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
        newExecutableCommandLayer.execute(graphs, layeredImages);
        assertEquals(4, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
        assertEquals(4, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
        assertEquals(4, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidDestination() {
        this.setUp();
        newFailCommandNonExistingDestImage.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidSource() {
        this.setUp();
        newFailCommandNonExistingDestLayer.execute(graphs, layeredImages);
      }

      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommandSingle.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommandSingle.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of creating a transparent image through scripts.
     */
    public static class TestCreateTransparentCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();

        newExecutableCommand = new CreateTransparentCommand("new", 1, 1);
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionInvalidWidth() {
        ParsedCommand fail = new CreateTransparentCommand("new", 0, 1);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionInvalidHeight() {
        ParsedCommand fail = new CreateTransparentCommand("new", 1, -3);
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new CreateTransparentCommand(null, 1, 1);
      }

      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertFalse(graphs.containsKey("new"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertTrue(graphs.containsKey("new"));
        GraphOfPixels newlyCreated = graphs.get("new");
        assertEquals(1, newlyCreated.getWidth());
        assertEquals(1, newlyCreated.getHeight());

        assertEquals(0, newlyCreated.getPixelAt(0, 0).getRed());
        assertEquals(0, newlyCreated.getPixelAt(0, 0).getGreen());
        assertEquals(0, newlyCreated.getPixelAt(0, 0).getBlue());
        assertEquals(0, newlyCreated.getPixelAt(0, 0).getOpacity());

      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageNameAlreadyExists() {
        this.setUp();
        newExecutableCommand.execute(graphs, layeredImages);
        //Running the same command again should cause a problem.
        newExecutableCommand.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of creating a copy of a layer in a layered image through scripts.
     */
    public static class TestCopyLayerCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private ParsedCommand failExecutableNoOriginal;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        LayeredImage ex1 = new LayeredImageV0(1, 1);
        ex1.addLayer("original");
        ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
        ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(123, 123, 123));
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        layeredImages.put("existing", ex1);

        newExecutableCommand = new CopyLayerCommand("existing", "copy", "original");
        failExecutableNoOriginal = new CopyLayerCommand("existing", "new", "non-existent");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new CopyLayerCommand(null, "copy", "original");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullLayerName() {
        ParsedCommand fail = new CopyLayerCommand("existing", null, "original");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullOriginalLayer() {
        ParsedCommand fail = new CopyLayerCommand("existing", "copy", null);
      }


      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertFalse(layeredImages.get("existing").getLayerNames().contains("copy"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertTrue(layeredImages.get("existing").getLayerNames().contains("copy"));
        FixedSizeGraph newlyCreated = layeredImages.get("existing").getLayer("copy");
        assertEquals(1, newlyCreated.getWidth());
        assertEquals(1, newlyCreated.getHeight());

        assertEquals(123, newlyCreated.getPixelAt(0, 0).getRed());
        assertEquals(123, newlyCreated.getPixelAt(0, 0).getGreen());
        assertEquals(123, newlyCreated.getPixelAt(0, 0).getBlue());

      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailOriginalFileDoesNotExist() {
        this.setUp();
        failExecutableNoOriginal.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageNameAlreadyExists() {
        this.setUp();
        newExecutableCommand.execute(graphs, layeredImages);
        //Running the same command again should cause a problem.
        newExecutableCommand.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of loading a layer as the current image through scripts.
     */
    public static class TestLoadLayerCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private ParsedCommand newExecutableCommandNullLayer;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        LayeredImage ex1 = new LayeredImageV0(1, 1);
        ex1.addLayer("layer");
        ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
        ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(123, 123, 123));
        layeredImages.put("existing", ex1);

        newExecutableCommand = new LoadLayerCommand("layer");
        newExecutableCommandNullLayer = new LoadLayerCommand(null);
      }


      @Test
      public void testAlterLanguageStateNewLayer() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class has NO EXECUTE FUNCTIONALITY. IT DOES ALTER LANGUAGE STATE.
        // so we should catch an IllegalArgumentException both before its execution, but not after!
        this.setUp();
        test.parseCommand("load existing").alterLanguageState(test);

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
          //This catch clause should be run, allowing the program to continue.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          //This command should update the current reference and thus the below command should pass.
          //No exception in the try clause should be logged.
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
        } catch (IllegalArgumentException e) {
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");

        }
      }

      @Test
      public void testAlterLanguageStateNull() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class has NO EXECUTE FUNCTIONALITY. IT DOES ALTER LANGUAGE STATE.
        // so we should catch an IllegalArgumentException both before its execution, but not after!
        this.setUp();
        test.parseCommand("load existing").alterLanguageState(test);
        newExecutableCommand.alterLanguageState(test);

        try {
          //We do not want to see an exception occur here
          // to show that the current values are filled.
          //This try clause should be run, allowing the program to continue.
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
        } catch (IllegalArgumentException e) {
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        }

        newExecutableCommandNullLayer.alterLanguageState(test);

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here
          // to show that the current values are filled.
          //This catch clause should be run, allowing the program to continue.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of adding a layer to a layered image through scripts.
     */
    public static class TestAddImageLayerCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newFailCommandNonExistingDest;
      private ParsedCommand newFailCommandNonExistingSource;
      private ParsedCommand newExecutableCommand;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        LayeredImage ex1 = new LayeredImageV0(1024, 768);
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        layeredImages.put("existing", ex1);

        newFailCommandNonExistingDest = new AddImageLayerCommand("non-existing", "new",
            "outputImages/birb.jpg");
        newFailCommandNonExistingSource = new AddImageLayerCommand("existing", "new", "nope");
        newExecutableCommand = new AddImageLayerCommand("existing", "new", "outputImages/birb.jpg");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageAdding() {
        ParsedCommand fail = new AddImageLayerCommand(null, "new", "outputImages/birb.jpg");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullLayerName() {
        ParsedCommand fail = new AddImageLayerCommand("existing", null, "outputImages/birb.jpg");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullFileLocation() {
        ParsedCommand fail = new AddImageLayerCommand("existing", "new", null);
      }

      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertFalse(layeredImages.get("existing").getLayerNames().contains("new"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertTrue(layeredImages.get("existing").getLayerNames().contains("new"));
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidDestination() {
        this.setUp();
        newFailCommandNonExistingDest.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidSource() {
        this.setUp();
        newFailCommandNonExistingSource.execute(graphs, layeredImages);
      }

      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of adding a layer to a layered image through scripts.
     */
    public static class TestAddLayerCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newFailCommandNonExistingDest;
      private ParsedCommand newFailCommandNonExistingSource;
      private ParsedCommand newExecutableCommand;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        LayeredImage ex1 = new LayeredImageV0(1024, 768);
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        layeredImages.put("existing", ex1);

        newFailCommandNonExistingDest = new AddLayerCommand("non-existing", "new");
        newExecutableCommand = new AddLayerCommand("existing", "new");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageAdding() {
        ParsedCommand fail = new AddLayerCommand(null, "new");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullLayerName() {
        ParsedCommand fail = new AddLayerCommand("existing", null);
      }

      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertFalse(layeredImages.get("existing").getLayerNames().contains("new"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertTrue(layeredImages.get("existing").getLayerNames().contains("new"));
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidDestination() {
        this.setUp();
        newFailCommandNonExistingDest.execute(graphs, layeredImages);
      }

      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of removing a layer in a layered image through scripts.
     */
    public static class TestRemoveLayerByNameCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private ParsedCommand failExecutableNonExistentImage;
      private ParsedCommand failExecutableNonExistentLayer;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        LayeredImage ex1 = new LayeredImageV0(1, 1);
        ex1.addLayer("secondLayer");
        ex1.getLayer("secondLayer").getPixelAt(0, 0).setOpacity(255);
        ex1.addLayer("firstLayer");
        layeredImages.put("existing", ex1);
        newExecutableCommand = new RemoveLayerByNameCommand("existing", "firstLayer");
        failExecutableNonExistentImage = new RemoveLayerByNameCommand("non-existent", "firstLayer");
        failExecutableNonExistentLayer = new RemoveLayerByNameCommand("existing", "non-existent");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new RemoveLayerByNameCommand(null, "firstLayer");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullLayerName() {
        ParsedCommand fail = new RemoveLayerByNameCommand("existing", null);
      }


      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        //GetLayerNames iterates through a layered image and PRINTS THE LAYERS FROM TOP TO BOTTOM
        assertEquals(2, layeredImages.get("existing").getNumLayers());
        assertEquals("firstLayer", layeredImages.get("existing").getLayerNames().get(0));
        assertEquals("secondLayer", layeredImages.get("existing").getLayerNames().get(1));
        newExecutableCommand.execute(graphs, layeredImages);
        assertEquals(1, layeredImages.get("existing").getNumLayers());
        assertEquals("secondLayer", layeredImages.get("existing").getLayerNames().get(0));
        //secondLayer has the only pixel with opacity 255, so we check if it moved to top.
        assertEquals(255, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getOpacity());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageDoesNotExist() {
        this.setUp();
        failExecutableNonExistentImage.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailLayerDoesNotExist() {
        this.setUp();
        failExecutableNonExistentLayer.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of saving empty images or specific layers through scripts.
     */
    public static class TestSaveCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommandSingle;
      private ParsedCommand newExecutableCommandLayer;
      private ParsedCommand failExecutableNonExistentImage;
      private ParsedCommand failExecutableNonExistentLayer;
      private ParsedCommand failExecutableNullLayer;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();

        layeredImages = new HashMap<String, LayeredImage>();
        LayeredImage ex1 = new LayeredImageV0(1, 1);
        ex1.addLayer("secondLayer");
        ex1.getLayer("secondLayer").getPixelAt(0, 0).setOpacity(214);
        ex1.getLayer("secondLayer").getPixelAt(0, 0).updateColors(new SimplePixel(111, 111, 111));
        ex1.addLayer("firstLayer");
        layeredImages.put("existing", ex1);

        graphs = new HashMap<String, GraphOfPixels>();
        GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
        ex2.getPixelAt(0, 0).setOpacity(255);
        ex2.getPixelAt(0, 0).updateColors(new SimplePixel(123, 123, 123));
        graphs.put("existingImage", ex2);

        newExecutableCommandSingle = new SaveCommand("existingImage", null, "ppm",
            "outputImages/testSaveSingle");
        newExecutableCommandLayer = new SaveCommand("existing", "secondLayer", "png",
            "outputImages/testSaveLayer");
        failExecutableNonExistentImage = new SaveCommand("what", null, "ppm",
            "outputImages/testSaveSingle");
        failExecutableNonExistentLayer = new SaveCommand("existing", "non-existent", "png",
            "outputImages/testSaveSingle");
        failExecutableNullLayer = new SaveCommand("existing", null, "png",
            "outputImages/testSaveSingle");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new SaveCommand(null, "none", "ppm", "outputImages/testSaveSingle");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullOutputType() {
        ParsedCommand fail = new SaveCommand("existingImage", "none", null,
            "outputImages/testSaveSingle");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullFileName() {
        ParsedCommand fail = new SaveCommand("existingImage", "none", "ppm", null);
      }

      @Test
      public void testSuccessfulExecuteSingleImage() {
        this.setUp();
        newExecutableCommandSingle.execute(graphs, layeredImages);
        GraphOfPixels exported = ImageToGraphConverter
            .convertPPM("outputImages/testSaveSingle.ppm");
        assertEquals(1, exported.getWidth());
        assertEquals(1, exported.getHeight());

        assertEquals(123, exported.getPixelAt(0, 0).getRed());
        assertEquals(123, exported.getPixelAt(0, 0).getGreen());
        assertEquals(123, exported.getPixelAt(0, 0).getBlue());
        assertEquals(255, exported.getPixelAt(0, 0).getOpacity());
      }

      @Test
      public void testSuccessfulExecuteLayer() {
        this.setUp();
        newExecutableCommandLayer.execute(graphs, layeredImages);
        GraphOfPixels exported = ImageToGraphConverter
            .convertImage("outputImages/testSaveLayer.png");
        assertEquals(1, exported.getWidth());
        assertEquals(1, exported.getHeight());

        assertEquals(111, exported.getPixelAt(0, 0).getRed());
        assertEquals(111, exported.getPixelAt(0, 0).getGreen());
        assertEquals(111, exported.getPixelAt(0, 0).getBlue());
        assertEquals(214, exported.getPixelAt(0, 0).getOpacity());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageDoesNotExist() {
        this.setUp();
        failExecutableNonExistentImage.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailLayerDoesNotExist() {
        this.setUp();
        failExecutableNonExistentLayer.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailNullLayerExistingImage() {
        this.setUp();
        failExecutableNullLayer.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommandSingle.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommandSingle.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of saving layered images through scripts.
     */
    public static class TestSaveLayeredCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommandSingle;
      private ParsedCommand failExecutableNonExistentImage;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        graphs = new HashMap<String, GraphOfPixels>();
        layeredImages = new HashMap<String, LayeredImage>();
        LayeredImage ex1 = new LayeredImageV0(1, 1);
        ex1.addLayer("secondLayer");
        ex1.getLayer("secondLayer").getPixelAt(0, 0).setOpacity(214);
        ex1.getLayer("secondLayer").getPixelAt(0, 0).updateColors(new SimplePixel(111, 111, 111));
        ex1.addLayer("firstLayer");
        ex1.getLayer("firstLayer").getPixelAt(0, 0).setOpacity(124);
        ex1.getLayer("firstLayer").getPixelAt(0, 0).updateColors(new SimplePixel(50, 50, 50));
        layeredImages.put("existing", ex1);

        newExecutableCommandSingle = new SaveLayeredCommand("existing",
            "outputImages/testSaveLayered");
        failExecutableNonExistentImage = new SaveLayeredCommand("what",
            "outputImages/testSaveLayered");
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new SaveLayeredCommand(null, "outputImages/testSaveLayered");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullFileName() {
        ParsedCommand fail = new SaveLayeredCommand("existing", null);
      }


      @Test
      public void testSuccessfulExecuteSingleImage() {
        this.setUp();
        newExecutableCommandSingle.execute(graphs, layeredImages);
        LayeredImage exported = new LayeredImageV0("outputImages/testSaveLayered");
        assertEquals(2, exported.getNumLayers());
        assertEquals(1, exported.getWidth());
        assertEquals(1, exported.getHeight());
        assertEquals("firstLayer", exported.getLayerNames().get(0));
        assertEquals("secondLayer", exported.getLayerNames().get(1));

        assertEquals(50, exported.getLayer(0).getPixelAt(0, 0).getRed());
        assertEquals(50, exported.getLayer(0).getPixelAt(0, 0).getGreen());
        assertEquals(50, exported.getLayer(0).getPixelAt(0, 0).getBlue());
        assertEquals(124, exported.getLayer(0).getPixelAt(0, 0).getOpacity());

        assertEquals(111, exported.getLayer(1).getPixelAt(0, 0).getRed());
        assertEquals(111, exported.getLayer(1).getPixelAt(0, 0).getGreen());
        assertEquals(111, exported.getLayer(1).getPixelAt(0, 0).getBlue());
        assertEquals(214, exported.getLayer(1).getPixelAt(0, 0).getOpacity());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageDoesNotExist() {
        this.setUp();
        failExecutableNonExistentImage.execute(graphs, layeredImages);
      }

      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommandSingle.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommandSingle.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of making an image sepia through scripts.
     */
    public static class TestSepiaCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newFailCommandNonExistingDestImage;
      private ParsedCommand newFailCommandNonExistingDestLayer;
      private ParsedCommand newExecutableCommandSingle;
      private ParsedCommand newExecutableCommandLayer;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        LayeredImage ex1 = new LayeredImageV0(1024, 768);
        ex1.addLayer("new");
        ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
        ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(16, 32, 64));
        GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
        ex2.getPixelAt(0, 0).updateColors(new SimplePixel(16, 32, 64));

        graphs = new HashMap<String, GraphOfPixels>();
        graphs.put("existingImage", ex2);
        layeredImages = new HashMap<String, LayeredImage>();
        layeredImages.put("existing", ex1);

        newFailCommandNonExistingDestImage = new SepiaCommand("non-existing", "new");
        newFailCommandNonExistingDestLayer = new SepiaCommand("existing", "none");
        newExecutableCommandSingle = new SepiaCommand("existingImage", null);
        newExecutableCommandLayer = new SepiaCommand("existing", "new");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageAdding() {
        ParsedCommand fail = new SepiaCommand(null, "birb");
      }


      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getRed());
        assertEquals(32, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
        assertEquals(64, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
        newExecutableCommandSingle.execute(graphs, layeredImages);
        assertEquals(43, graphs.get("existingImage").getPixelAt(0, 0).getRed());
        assertEquals(38, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
        assertEquals(30, graphs.get("existingImage").getPixelAt(0, 0).getBlue());

        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
        assertEquals(32, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
        assertEquals(64, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
        newExecutableCommandLayer.execute(graphs, layeredImages);
        assertEquals(43, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
        assertEquals(38, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
        assertEquals(30, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidDestination() {
        this.setUp();
        newFailCommandNonExistingDestImage.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidSource() {
        this.setUp();
        newFailCommandNonExistingDestLayer.execute(graphs, layeredImages);
      }

      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommandSingle.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommandSingle.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of sharpening an image through scripts.
     */
    public static class TestSharpenCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newFailCommandNonExistingDestImage;
      private ParsedCommand newFailCommandNonExistingDestLayer;
      private ParsedCommand newExecutableCommandSingle;
      private ParsedCommand newExecutableCommandLayer;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();
        LayeredImage ex1 = new LayeredImageV0(2, 1);
        ex1.addLayer("new");
        ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
        ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(16, 16, 16));
        ex1.getLayer(0).getPixelAt(1, 0).setOpacity(255);
        ex1.getLayer(0).getPixelAt(1, 0).updateColors(new SimplePixel(16, 16, 16));
        GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
        ex2.insertColumn(0);
        ex2.getPixelAt(0, 0).updateColors(new SimplePixel(16, 16, 16));
        ex2.getPixelAt(1, 0).updateColors(new SimplePixel(16, 16, 16));

        graphs = new HashMap<String, GraphOfPixels>();
        graphs.put("existingImage", ex2);
        layeredImages = new HashMap<String, LayeredImage>();
        layeredImages.put("existing", ex1);

        newFailCommandNonExistingDestImage = new SharpenCommand("non-existing", "new");
        newFailCommandNonExistingDestLayer = new SharpenCommand("existing", "none");
        newExecutableCommandSingle = new SharpenCommand("existingImage", null);
        newExecutableCommandLayer = new SharpenCommand("existing", "new");
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageAdding() {
        ParsedCommand fail = new SharpenCommand(null, "birb");
      }


      @Test
      public void testSuccessfulExecute() {
        this.setUp();
        assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getRed());
        assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
        assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
        newExecutableCommandSingle.execute(graphs, layeredImages);
        assertEquals(20, graphs.get("existingImage").getPixelAt(0, 0).getRed());
        assertEquals(20, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
        assertEquals(20, graphs.get("existingImage").getPixelAt(0, 0).getBlue());

        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getRed());
        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getGreen());
        assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getBlue());
        newExecutableCommandLayer.execute(graphs, layeredImages);
        assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
        assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
        assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
        assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getRed());
        assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getGreen());
        assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getBlue());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidDestination() {
        this.setUp();
        newFailCommandNonExistingDestImage.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailInvalidSource() {
        this.setUp();
        newFailCommandNonExistingDestLayer.execute(graphs, layeredImages);
      }

      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommandSingle.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommandSingle.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of updating the color of a pixel in an image or layer through
     * scripts.
     */
    public static class TestUpdateColorCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommandSingle;
      private ParsedCommand newExecutableCommandLayer;
      private ParsedCommand failExecutableNonExistentImage;
      private ParsedCommand failExecutableNonExistentLayer;
      private ParsedCommand failExecutableNullLayer;
      private ParsedCommand failExecutableTooSmallOpacity;
      private ParsedCommand failExecutableTooLargeOpacity;
      private ParsedCommand failExecutableNegativeX;
      private ParsedCommand failExecutableNegativeY;
      private ParsedCommand failExecutableTooLargeX;
      private ParsedCommand failExecutableTooLargeY;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();

        layeredImages = new HashMap<String, LayeredImage>();
        LayeredImage ex1 = new LayeredImageV0(1, 1);
        ex1.addLayer("secondLayer");
        ex1.addLayer("firstLayer");
        layeredImages.put("existing", ex1);

        graphs = new HashMap<String, GraphOfPixels>();
        GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
        graphs.put("existingImage", ex2);

        newExecutableCommandSingle = new UpdateColorCommand("existingImage", null, 0, 0, 75, 123,
            123,
            123);
        newExecutableCommandLayer =
            new UpdateColorCommand("existing", "secondLayer", 0, 0, 13, 32, 32, 32);
        failExecutableNonExistentImage =
            new UpdateColorCommand("what", null, 0, 0, 75, 123, 123, 123);
        failExecutableNonExistentLayer = new UpdateColorCommand("existing", "non-existent", 0, 0,
            75,
            123, 123, 123);
        failExecutableNullLayer = new UpdateColorCommand("existing", null, 0, 0, 75, 123, 123, 123);
        failExecutableTooSmallOpacity = new UpdateColorCommand("existingImage", null, 0, 0, -1, 123,
            123, 123);
        failExecutableTooLargeOpacity = new UpdateColorCommand("existingImage", null, 0, 0, 1000,
            123,
            123, 123);

        failExecutableNegativeX = new UpdateColorCommand("existingImage", null, -3, 0, 75, 123, 123,
            123);
        failExecutableNegativeY = new UpdateColorCommand("existingImage", null, 0, -3, 75, 123, 123,
            123);
        failExecutableTooLargeX = new UpdateColorCommand("existingImage", null, 100, 0, 75, 123,
            123,
            123);
        failExecutableTooLargeY = new UpdateColorCommand("existingImage", null, 0, 100, 75, 123,
            123,
            123);
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new UpdateColorCommand(null, "secondLayer", 0, 0, 75, 123, 123, 123);
      }


      @Test
      public void testSuccessfulExecuteSingleImage() {
        this.setUp();
        assertEquals(255, graphs.get("existingImage").getPixelAt(0, 0).getOpacity());
        assertEquals(255, graphs.get("existingImage").getPixelAt(0, 0).getRed());
        assertEquals(255, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
        assertEquals(255, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
        newExecutableCommandSingle.execute(graphs, layeredImages);
        assertEquals(75, graphs.get("existingImage").getPixelAt(0, 0).getOpacity());
        assertEquals(123, graphs.get("existingImage").getPixelAt(0, 0).getRed());
        assertEquals(123, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
        assertEquals(123, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
      }

      @Test
      public void testSuccessfulExecuteLayer() {
        this.setUp();
        assertEquals(0, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getOpacity());
        assertEquals(0, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getRed());
        assertEquals(0, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getGreen());
        assertEquals(0, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getBlue());
        newExecutableCommandLayer.execute(graphs, layeredImages);
        assertEquals(13, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getOpacity());
        assertEquals(32, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getRed());
        assertEquals(32, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getGreen());
        assertEquals(32, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getBlue());
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailImageDoesNotExist() {
        this.setUp();
        failExecutableNonExistentImage.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailLayerDoesNotExist() {
        this.setUp();
        failExecutableNonExistentLayer.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailNullLayerExistingImage() {
        this.setUp();
        failExecutableNullLayer.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailTooSmallOpacity() {
        this.setUp();
        failExecutableTooSmallOpacity.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailTooLargeOpacity() {
        this.setUp();
        failExecutableTooLargeOpacity.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailNegativeX() {
        this.setUp();
        failExecutableNegativeX.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailNegativey() {
        this.setUp();
        failExecutableNegativeY.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailTooLargeX() {
        this.setUp();
        failExecutableTooLargeX.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailTooLargeY() {
        this.setUp();
        failExecutableTooLargeY.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommandSingle.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommandSingle.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommandSingle.alterLanguageState(null);
      }
    }

    /**
     * Tests the functionality of updating the visibility of a layer in an image through scripts.
     */
    public static class TestUpdateVisibilityCommand {

      private HashMap<String, GraphOfPixels> graphs;
      private HashMap<String, LayeredImage> layeredImages;
      private ParsedCommand newExecutableCommand;
      private ParsedCommand failExecutableNonExistentImage;
      private ParsedCommand failExecutableNonExistentLayer;
      private ParsedCommand failExecutableExistingImageButSingle;
      private LanguageSyntax test;

      /**
       * A method to set up proxy graphs and layeredImages.
       */
      private void setUp() {
        test = new LanguageSyntaxImpl();

        layeredImages = new HashMap<String, LayeredImage>();
        LayeredImage ex1 = new LayeredImageV0(1, 1);
        ex1.addLayer("secondLayer");
        ex1.getLayer("secondLayer").getPixelAt(0, 0).setOpacity(214);
        ex1.getLayer("secondLayer").getPixelAt(0, 0).updateColors(new SimplePixel(111, 111, 111));
        ex1.addLayer("firstLayer");
        layeredImages.put("existing", ex1);

        graphs = new HashMap<String, GraphOfPixels>();
        GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
        ex2.getPixelAt(0, 0).setOpacity(255);
        ex2.getPixelAt(0, 0).updateColors(new SimplePixel(123, 123, 123));
        graphs.put("existingImage", ex2);

        newExecutableCommand = new UpdateVisibilityCommand("existing", "secondLayer", false);
        failExecutableNonExistentImage = new UpdateVisibilityCommand("non-existent", "secondLayer",
            false);
        failExecutableNonExistentLayer = new UpdateVisibilityCommand("existing", "non-existent",
            false);
        failExecutableExistingImageButSingle = new UpdateVisibilityCommand("existingImage",
            "secondLayer", false);
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullImageName() {
        ParsedCommand fail = new UpdateVisibilityCommand(null, "secondLayer", false);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailConstructionNullOutputType() {
        ParsedCommand fail = new UpdateVisibilityCommand("existing", null, false);
      }

      @Test
      public void testSuccessfulExecuteSingleImage() {
        this.setUp();
        assertTrue(layeredImages.get("existing").getVisibility("secondLayer"));
        newExecutableCommand.execute(graphs, layeredImages);
        assertFalse(layeredImages.get("existing").getVisibility("secondLayer"));
      }


      @Test(expected = IllegalArgumentException.class)
      public void testFailImageDoesNotExist() {
        this.setUp();
        failExecutableNonExistentImage.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailLayerDoesNotExist() {
        this.setUp();
        failExecutableNonExistentLayer.execute(graphs, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testFailNullLayerExistingImage() {
        this.setUp();
        failExecutableExistingImageButSingle.execute(graphs, layeredImages);
      }


      @Test
      public void testAlterLanguageState() {
        //Since the current image and field layers are private and inaccessible, we can test if they
        // have been updated through testing whether scripts
        // that rely on them pass without exception.
        //This class should have no effect on this,
        // so we should catch an exception both before and after.
        this.setUp();

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        newExecutableCommand.alterLanguageState(test);

        try {
          test.parseCommand("update-color existing 0 0 123 50 50 50")
              .execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }

        try {
          test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
          throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
        } catch (IllegalArgumentException e) {
          //We do not want to see an exception occur here to show that the current values are null.
        }
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullGraphs() {
        this.setUp();
        newExecutableCommand.execute(null, layeredImages);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testExecuteNullLayeredImages() {
        this.setUp();
        newExecutableCommand.execute(graphs, null);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testAlterStateNullGraphs() {
        this.setUp();
        newExecutableCommand.alterLanguageState(null);
      }
    }
  }

  /**
   * For running methods directly through a main method.
   */
  public static class TestSimpleGraphOfPixels {

    /**
     * The main method for running tests directly.
     *
     * @param args CommandLine args(not used)
     */
    public static void main(String[] args) {
      /*
      GraphOfPixels graph0 = ImageToGraphConverter.convertPPM("res/orange.ppm");
      GraphOfPixels graph1 = ImageToGraphConverter.convertPPM("res/pellegrino.ppm");
      graph0.applyMutator(new GreyscaleTransform());
      graph1.applyMutator(new GreyscaleTransform());
      graph0.writeToFile(OutputType.ppm, "res/orangeGreyscale");
      graph1.writeToFile(OutputType.ppm, "res/pellegrinoGreyscale");

      graph0 = ImageToGraphConverter.convertPPM("res/orange.ppm");
      graph1 = ImageToGraphConverter.convertPPM("res/pellegrino.ppm");
      graph0.applyMutator(new SepiaTransform());
      graph1.applyMutator(new SepiaTransform());
      graph0.writeToFile(OutputType.ppm, "res/orangeSepia");
      graph1.writeToFile(OutputType.ppm, "res/pellegrinoSepia");

      graph0 = ImageToGraphConverter.convertPPM("res/orange.ppm");
      graph1 = ImageToGraphConverter.convertPPM("res/pellegrino.ppm");
      graph0.applyMutator(new BlurFilter());
      graph1.applyMutator(new BlurFilter());
      graph0.writeToFile(OutputType.ppm, "res/orangeBlur");
      graph1.writeToFile(OutputType.ppm, "res/pellegrinoBlur");

      graph0 = ImageToGraphConverter.convertPPM("res/orange.ppm");
      graph1 = ImageToGraphConverter.convertPPM("res/pellegrino.ppm");
      graph0.applyMutator(new SharpenFilter());
      graph1.applyMutator(new SharpenFilter());
      graph0.writeToFile(OutputType.ppm, "res/orangeSharpen");
      graph1.writeToFile(OutputType.ppm, "res/pellegrinoSharpen");

       */
      /*
      LayeredImage layered0 = new LayeredImageV0(4,4);
      layered0.addLayer("THIS");
      layered0.addLayer("IS");
      layered0.addLayer("A");
      layered0.addLayer("TEST");
      layered0.addLayer("COPYBEFORE", "TEST");
      layered0.getLayer("THIS").getPixelAt(0,0).updateColors(new SimplePixel(255,0,0));
      layered0.getLayer("IS").getPixelAt(0,0).updateColors(new SimplePixel(155,0,0));
      layered0.getLayer("A").getPixelAt(0,0).updateColors(new SimplePixel(12,121,0));
      layered0.getLayer("TEST").getPixelAt(0,0).updateColors(new SimplePixel(33,0,11));
      layered0.addLayer("COPYAFTER","TEST");
      layered0.getLayer("THIS").getPixelAt(0,0).setOpacity(255);
      layered0.getLayer("IS").getPixelAt(0,0).setOpacity(255);
      layered0.getLayer("A").getPixelAt(0,0).setOpacity(255);
      layered0.getLayer("TEST").getPixelAt(0,0).setOpacity(255);
      layered0.getLayer("COPYBEFORE").getPixelAt(0,0).setOpacity(255);
      layered0.getLayer("COPYAFTER").getPixelAt(0,0).setOpacity(255);
      layered0.removeLayer("A");
      for (FixedSizeGraph g : layered0) {
        System.out.println(g.getPixelAt(0,0).getRed());
      }

      //GraphOfPixels graph2 = ImageToGraphConverter.convertComplexImage(
      "outputImages/birbBetter.jpg");
      //graph2.writeToFile(OutputType.jpeg, "outputImages/birbBetterJPG");

      LayeredImage layered1 = new LayeredImageV0(1024, 768);
      layered1.loadImageAsLayer("birb", "outputImages/birb.jpg");
      layered1.loadImageAsLayer("rainbow", "outputImages/rainbow.png");
      layered1.loadImageAsLayer("galaxy", "outputImages/galaxy.png");
      layered1.setVisibility("galaxy", false);
      layered1.saveAsImage(new BasicBlend(), OutputType.png, "outputImages/misc");
      layered1.saveAsLayeredImage("outputImages/misc");

      LayeredImage layered2 = new LayeredImageV0("outputImages/misc");
      layered2.saveAsLayeredImage("outputImages/misc2");

       */
      ImageProcessingController controller = new ProcessingController("outputImages/TestScript.txt",
          System.out);
      //controller.run();
      /*
      LayeredImage testLayered = new LayeredImageV0(20,20);
      testLayered.addLayer("red-layer");
      testLayered.addLayer("blue-layer");
      testLayered.addLayer("invisible-layer");
      testLayered.setVisibility("invisible-layer", false);
      FixedSizeGraph redLayer = testLayered.getLayer("red-layer");
      for (Node n : redLayer) {
        n.setOpacity(255);
        n.updateColors(new SimplePixel(255,0,0));
      }
      FixedSizeGraph blueLayer = testLayered.getLayer("blue-layer");
      for (Node n : blueLayer) {
        n.setOpacity(255);
        n.updateColors(new SimplePixel(0,0,255));
      }
      testLayered.saveAsLayeredImage("outputImages/exampleLayeredImage");
      */
      GraphOfPixels example2 = ImageToGraphConverter.convertImage("outputImages/example.ppm");
      for (Node n : example2) {
        n.updateColors(new SimplePixel(55, 66, 77));
      }
      example2.writeToFile(OutputType.png, "outputImages/example2");
    }
  }

  /**
   * For testing the SimplePixel implementation of a PixelAsColors.
   */
  public static class TestSimplePixel {

    private PixelAsColors pixel0;
    private PixelAsColors pixel1;
    private PixelAsColors pixel2;

    /**
     * To initialize example pixels for testing.
     */
    private void setUp() {
      this.pixel0 = new SimplePixel(255, 0, 0);
      this.pixel1 = new SimplePixel(0, 255, 0);
      this.pixel2 = new SimplePixel(0, 0, 255);
    }

    @Test
    public void testConstruction() {
      PixelAsColors p0 = new SimplePixel(new SimplePixel(22, 23, 24));
      assertEquals(22, p0.getRed());
      assertEquals(23, p0.getGreen());
      assertEquals(24, p0.getBlue());
      PixelAsColors p1 = new SimplePixel(new SimplePixel(-25, -27, -1));
      assertEquals(0, p1.getRed());
      assertEquals(0, p1.getGreen());
      assertEquals(0, p1.getBlue());
      PixelAsColors p2 = new SimplePixel(new SimplePixel(307, 333, 256));
      assertEquals(255, p2.getRed());
      assertEquals(255, p2.getGreen());
      assertEquals(255, p2.getBlue());
      PixelAsColors p3 = new SimplePixel(new SimplePixel(-12, 269, 34));
      assertEquals(0, p3.getRed());
      assertEquals(255, p3.getGreen());
      assertEquals(34, p3.getBlue());
      PixelAsColors p4 = new SimplePixel(p3);
      assertEquals(0, p4.getRed());
      assertEquals(255, p4.getGreen());
      assertEquals(34, p4.getBlue());
      assertEquals(true, p4.equals(p3));
      assertEquals(false, p4 == p3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPixelConstructor() {
      PixelAsColors p0 = new SimplePixel(null);
    }

    @Test
    public void testSet() {
      this.setUp();
      this.pixel1.setRed(25);
      assertEquals(25, this.pixel1.getRed());
      assertEquals(0, this.pixel1.getBlue());
      assertEquals(255, this.pixel1.getGreen());
      this.pixel0.setRed(-27);
      assertEquals(0, this.pixel0.getRed());
      assertEquals(0, this.pixel0.getBlue());
      assertEquals(0, this.pixel0.getGreen());
      this.pixel2.setRed(256);
      assertEquals(255, this.pixel2.getRed());
      assertEquals(255, this.pixel2.getBlue());
      assertEquals(0, this.pixel2.getGreen());
      this.setUp();
      this.pixel0.setGreen(37);
      assertEquals(255, this.pixel0.getRed());
      assertEquals(0, this.pixel0.getBlue());
      assertEquals(37, this.pixel0.getGreen());
      this.pixel1.setGreen(-1);
      assertEquals(0, this.pixel1.getRed());
      assertEquals(0, this.pixel1.getBlue());
      assertEquals(0, this.pixel1.getGreen());
      this.pixel2.setGreen(304);
      assertEquals(0, this.pixel2.getRed());
      assertEquals(255, this.pixel2.getBlue());
      assertEquals(255, this.pixel2.getGreen());
      this.pixel0.setRGB(-12, 47, 256);
      assertEquals(0, this.pixel0.getRed());
      assertEquals(47, this.pixel0.getGreen());
      assertEquals(255, this.pixel0.getBlue());
      this.pixel1.setRGB(13, 777, -1);
      assertEquals(13, this.pixel1.getRed());
      assertEquals(255, this.pixel1.getGreen());
      assertEquals(0, this.pixel1.getBlue());
      this.pixel2.setRGB(600, -55, 27);
      assertEquals(255, this.pixel2.getRed());
      assertEquals(0, this.pixel2.getGreen());
      assertEquals(27, this.pixel2.getBlue());
    }

    @Test
    public void testEdit() {
      this.setUp();
      this.pixel0.editRed(-27);
      assertEquals(228, pixel0.getRed());
      assertEquals(0, pixel0.getGreen());
      assertEquals(0, pixel0.getBlue());
      this.pixel0.editRed(350);
      assertEquals(255, pixel0.getRed());
      assertEquals(0, pixel0.getGreen());
      assertEquals(0, pixel0.getBlue());
      this.pixel0.editRed(-300);
      assertEquals(0, pixel0.getRed());
      assertEquals(0, pixel0.getGreen());
      assertEquals(0, pixel0.getBlue());
      this.pixel1.editGreen(-50);
      assertEquals(0, pixel1.getRed());
      assertEquals(205, pixel1.getGreen());
      assertEquals(0, pixel1.getBlue());
      this.pixel1.editGreen(-206);
      assertEquals(0, pixel1.getRed());
      assertEquals(0, pixel1.getGreen());
      assertEquals(0, pixel1.getBlue());
      this.pixel1.editGreen(32);
      assertEquals(0, pixel1.getRed());
      assertEquals(32, pixel1.getGreen());
      assertEquals(0, pixel1.getBlue());
      this.pixel1.editGreen(707);
      assertEquals(0, pixel1.getRed());
      assertEquals(255, pixel1.getGreen());
      assertEquals(0, pixel1.getBlue());
      this.pixel2.editBlue(-200);
      assertEquals(0, pixel2.getRed());
      assertEquals(0, pixel2.getGreen());
      assertEquals(55, pixel2.getBlue());
      this.pixel2.editBlue(350);
      assertEquals(0, pixel2.getRed());
      assertEquals(0, pixel2.getGreen());
      assertEquals(255, pixel2.getBlue());
      this.pixel2.editBlue(-300);
      assertEquals(0, pixel2.getRed());
      assertEquals(0, pixel2.getGreen());
      assertEquals(0, pixel2.getBlue());
      this.setUp();
      this.pixel0.editRGB(-300, 27, 707);
      assertEquals(0, pixel0.getRed());
      assertEquals(27, pixel0.getGreen());
      assertEquals(255, pixel0.getBlue());
      this.pixel1.editRGB(39, 606, -24);
      assertEquals(39, pixel1.getRed());
      assertEquals(255, pixel1.getGreen());
      assertEquals(0, pixel1.getBlue());
      this.pixel2.editRGB(256, -73, -30);
      assertEquals(255, pixel2.getRed());
      assertEquals(0, pixel2.getGreen());
      assertEquals(225, pixel2.getBlue());
    }

    @Test
    public void testEquals() {
      this.setUp();
      assertEquals(false, this.pixel0.equals(this.pixel1));
      assertEquals(true, this.pixel0.equals(this.pixel0));
      PixelAsColors similar = new SimplePixel(pixel0);
      assertEquals(true, this.pixel0.equals(similar));
      assertEquals(false, this.pixel0 == similar);
    }

  }

  /**
   * Represents a class for sample testing the methods of a View that would have function, but just
   * throws errors here. NOTE: The showView method relies on inputs from System.in, but as this can
   * not be simulated, we instead test the outputs and the inputs are handled in the controller
   * tests, so refer to other tests (such as those for input/output behavior tested in the
   * PreviousTesting.TestCommand and TestLanguageSyntax classes).
   */
  public static class TestTextErrorView {


    @Test(expected = IllegalArgumentException.class)
    public void testCreatingViewWithNullOutput() {
      ErrorView failView = new TextErrorView(null);
    }


    /**
     * Tests whether render message will properly write message into output.
     */
    @Test
    public void testRenderMessage() throws IOException {
      Appendable out = new StringBuilder();
      ErrorView testView = new TextErrorView(out);
      testView.renderException("Hello World!");
      assertEquals("Hello World!", out.toString());
    }

    /**
     * Tests whether exception is thrown when writing a message to the output fails (given bad
     * Appendable).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFailedMessageRender() throws IOException {
      Appendable out = new MockFailAppendable();
      ErrorView testView = new TextErrorView(out);
      testView.renderException("THIS WILL FAIL.");
    }


  }

  /**
   * Tests useful utility methods from Utils class.
   */
  public static class TestUtils {

    @Test
    public void testRoundDouble() {
      assertEquals(1, Utils.roundDouble(0.9999));
      assertEquals(1, Utils.roundDouble(1.0));
      assertEquals(1, Utils.roundDouble(0.5));
      assertEquals(0, Utils.roundDouble(0.435544));
    }
  }
}

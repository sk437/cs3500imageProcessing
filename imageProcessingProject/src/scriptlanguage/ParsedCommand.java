package scriptlanguage;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.OutputType;
import imageinput.CheckerBoard;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import layeredimage.blend.BasicBlend;
import mutators.Mutator.BlurFilter;
import mutators.Mutator.GreyscaleTransform;
import mutators.Mutator.SepiaTransform;
import mutators.Mutator.SharpenFilter;
import pixel.SimplePixel;

/**
 * Represents a command executable by a script.
 */
public interface ParsedCommand {

  /**
   * Executes this parsed command, using the given hashMaps to store newly created images, or to
   * access existing images depending on what the parsed command does.
   *
   * @param graphs        The graphs that currently exist
   * @param layeredImages The layered images that currently exist
   * @throws IllegalArgumentException If given null inputs, or if the command cannot be executed
   *                                  properly
   */
  void execute(HashMap<String, GraphOfPixels> graphs, HashMap<String, LayeredImage> layeredImages)
      throws IllegalArgumentException;

  /**
   * Alters the state of the given LanguageSyntax in the manner specified by this command.
   *
   * @param toAlter The LanguageSyntax object to be updated
   * @throws IllegalArgumentException If given a null input, or if the command fails to alter the
   *                                  language properly
   */
  void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException;

  /**
   * Represents a command which updates the visibility of an existing layer of an existing layered
   * image.
   */
  class UpdateVisibilityCommand implements ParsedCommand {

    private final String imageToUpdate;
    private final String layerToUpdate;
    private final boolean newVisibility;

    /**
     * Creates a new UpdateVisibilityCommand, which updates the layer of the given name in the
     * layered image of the given name to be the given boolean.
     *
     * @param imageToUpdate The name of the image to be modified
     * @param layerToUpdate The name of the layer to be modified
     * @param newVisibility The new visibility value to set
     * @throws IllegalArgumentException If given a null input
     */
    public UpdateVisibilityCommand(String imageToUpdate, String layerToUpdate,
        boolean newVisibility)
        throws IllegalArgumentException {
      if (imageToUpdate == null || layerToUpdate == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageToUpdate = imageToUpdate;
      this.layerToUpdate = layerToUpdate;
      this.newVisibility = newVisibility;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (!layeredImages.containsKey(imageToUpdate)) {
        throw new IllegalArgumentException(
            "The image this command is supposed to update does not exist");
      }
      layeredImages.get(imageToUpdate).setVisibility(layerToUpdate, newVisibility);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
      // This command does not impact the current state of the language
    }
  }

  /**
   * Represents a command which updates the color of a pixel within an image, or within a layer of a
   * layered image.
   */
  class UpdateColorCommand implements ParsedCommand {

    private final String imageToUpdate;
    private final String layerToUpdate;
    private final int pixelX;
    private final int pixelY;
    private final int newOpacity;
    private final SimplePixel newColors;

    /**
     * Creates a new UpdateColorCommand, which will update the pixel at the specified coordinates of
     * the image with the given name, or the layer with the given name of the layered image with the
     * given name, to be as specified.
     *
     * @param imageToUpdate The name of the image, or layered image containing the layer, to modify
     * @param layerToUpdate The name of the layer to modify, may be null if this command modifies a
     *                      non-layered image
     * @param pixelX        The x coordinate of the pixel to be updated
     * @param pixelY        The y coordinate of teh pixel to be updated
     * @param newOpacity    The value to set the opacity of the pixel to
     * @param newR          The value to set the red value of the pixel to
     * @param newG          The value to set the green value of the pixel to
     * @param newB          THe value to set the blue value of the pixel to
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
      } else if (layeredImages.containsKey(imageToUpdate)) {
        if (layerToUpdate == null) {
          throw new IllegalArgumentException("Null layer");
        }
        layeredImages.get(imageToUpdate).getLayer(layerToUpdate).getPixelAt(pixelX, pixelY)
            .updateColors(newColors);
        layeredImages.get(imageToUpdate).getLayer(layerToUpdate).getPixelAt(pixelX, pixelY)
            .setOpacity(newOpacity);
      } else {
        throw new IllegalArgumentException(
            "The image this command is supposed to modify does not exist");
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

  /**
   * Represents a command which saves a layered image, as an image file of a specified name.
   */
  class SaveLayeredCommand implements ParsedCommand {

    private final String imageToSave;
    private final String fileName;

    public SaveLayeredCommand(String imageToSave, String fileName) {
      if (imageToSave == null || fileName == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      this.imageToSave = imageToSave;
      this.fileName = fileName;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (!layeredImages.containsKey(this.imageToSave)) {
        throw new IllegalArgumentException("Image to save does not exist");
      }
      layeredImages.get(this.imageToSave).saveAsLayeredImage(this.fileName);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
      // This command does not impact the current state of the language
    }
  }

  /**
   * Represents a command which saves either an image, or a layer of a layered image, as an image
   * file of a specified name and type.
   */
  class SaveCommand implements ParsedCommand {

    private final String imageToSave;
    private final String layerToSave;
    private final String fileName;
    private final OutputType outputType;

    /**
     * Creates a new SaveCommand, which will save the existing image with the given name, or the
     * layer with the given name of the existing layered image with the given name, as a file with
     * the given fileName.
     *
     * @param imageToSave The name of the image to save, or the layered image which contains the
     *                    layer to be saved.
     * @param layerToSave The name of the layer to be saved, might be null if this command saves a
     *                    graph and not a layer of a layered image.
     * @param outputType  The type of image being outputted
     * @param fileName    The name of the file the image will be saved as
     * @throws IllegalArgumentException if given a null imageToSave or fileName
     */
    public SaveCommand(String imageToSave, String layerToSave, String outputType, String fileName)
        throws IllegalArgumentException {
      if (imageToSave == null || fileName == null) {
        throw new IllegalArgumentException("Null imageToSave or fileName");
      }
      this.imageToSave = imageToSave;
      this.layerToSave = layerToSave;
      this.fileName = fileName;
      this.outputType = OutputType.convertString(outputType);

    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (graphs.containsKey(imageToSave)) {
        graphs.get(imageToSave).writeToFile(this.outputType, this.fileName);
      } else if (layeredImages.containsKey(imageToSave)) {
        if (layerToSave == null) {
          throw new IllegalArgumentException("Null layer");
        }
        layeredImages.get(imageToSave).getLayer(layerToSave)
            .writeToFile(this.outputType, this.fileName);
      } else {
        throw new IllegalArgumentException("The image to be saved does not exist");
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

  /**
   * Creates a command which will remove a layer from an existing layered image.
   */
  class RemoveLayerByNameCommand implements ParsedCommand {

    private final String imageToUpdate;
    private final String layerToRemove;

    /**
     * Creates a new RemoveLayerByNameCommand, which will remove the layer with the given name from
     * the layered image with the given name.
     *
     * @param imageToUpdate The name of the layered image to be modified
     * @param layerToRemove The name of the layer to be removed
     * @throws IllegalArgumentException If either input is null
     */
    public RemoveLayerByNameCommand(String imageToUpdate, String layerToRemove)
        throws IllegalArgumentException {
      if (imageToUpdate == null || layerToRemove == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageToUpdate = imageToUpdate;
      this.layerToRemove = layerToRemove;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (!layeredImages.containsKey(imageToUpdate)) {
        throw new IllegalArgumentException(
            "the image this command is supposed to modify does not exist");
      }
      layeredImages.get(imageToUpdate).removeLayer(layerToRemove);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
      // This command does not impact the current state of the language
    }
  }

  /**
   * Represents a command which moves an existing layer in an existing layered image.
   */
  class MoveLayerCommand implements ParsedCommand {

    private final String imageToUpdate;
    private final String layerToMove;
    private final int indexToMove;

    /**
     * Creates a new MoveLayerCommand, which will move the layer with the given name in the given
     * layered image to the given index.
     *
     * @param imageToUpdate The name of the layered image to be modified
     * @param layerToMove   The name of the layer to move
     * @param indexToMove   The index to move that layer to
     * @throws IllegalArgumentException If inputs are null
     */
    public MoveLayerCommand(String imageToUpdate, String layerToMove, int indexToMove)
        throws IllegalArgumentException {
      if (imageToUpdate == null || layerToMove == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageToUpdate = imageToUpdate;
      this.layerToMove = layerToMove;
      this.indexToMove = indexToMove;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (!layeredImages.containsKey(imageToUpdate)) {
        throw new IllegalArgumentException(
            "The layered image this command is supposed to update does not exist");
      }
      layeredImages.get(imageToUpdate).moveLayer(layerToMove, indexToMove);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
      // This command does not impact the current state of the language
    }
  }

  /**
   * Represents a command which alters the default layer name of a syntax.
   */
  class LoadLayerCommand implements ParsedCommand {

    private final String layerToLoad;

    /**
     * Creates a new LoadLayerCommand, which will set the default layer name of a syntax to the
     * given string.
     *
     * @param layerToLoad The new default layer name
     */
    public LoadLayerCommand(String layerToLoad) {
      this.layerToLoad = layerToLoad;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      //Execution does nothing, as this command only alters the state of the language
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
      toAlter.setCurrentLayer(layerToLoad);
    }
  }

  /**
   * Represents a command which will change the default image name of a syntax, and reset the
   * default layer name of that syntax.
   */
  class LoadCommand implements ParsedCommand {

    private final String imageToLoad;

    /**
     * Creates a new LoadCommand, which will set the default image of an existing syntax to the
     * given imageName and reset the default layer name.
     *
     * @param imageToLoad The name of the new default image
     * @throws IllegalArgumentException If given a null input
     */
    public LoadCommand(String imageToLoad) throws IllegalArgumentException {
      if (imageToLoad == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageToLoad = imageToLoad;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      //Execution does nothing, as this command only alters the state of the language
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
      toAlter.setCurrentImage(imageToLoad);
      toAlter.setCurrentLayer(null);
    }
  }

  /**
   * Represents a command which will import a layered image file as a new layered image.
   */
  class ImportNewLayeredImageCommand implements ParsedCommand {

    private final String imageName;
    private final LayeredImage processedImage;

    /**
     * Creates a new ImportNewLayeredImageCommand, which creates a layered image of the given name
     * by reading a file of the given fileName.
     *
     * @param imageName The name of the image to be created
     * @param fileName  The name of the file to be read
     * @throws IllegalArgumentException If given a null input
     */
    public ImportNewLayeredImageCommand(String imageName, String fileName)
        throws IllegalArgumentException {
      if (imageName == null || fileName == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageName = imageName;
      this.processedImage = new LayeredImageV0(fileName);
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
      layeredImages.put(imageName, processedImage);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command which creates a new layered image, when given a name and dimensions for
   * that new layered image.
   */
  class CreateNewLayeredImageCommand implements ParsedCommand {

    private final String imageName;
    private final LayeredImage processedLayeredImage;

    /**
     * Creates a new CreateNewLayeredImageCommand, which will create a new layered image with the
     * given name and dimensions.
     *
     * @param imageName The name of the layered image to be created
     * @param width     The width of the layered image to be created
     * @param height    The height of the layered image to be created
     * @throws IllegalArgumentException If given a null imageName
     */
    public CreateNewLayeredImageCommand(String imageName, int width, int height)
        throws IllegalArgumentException {
      if (imageName == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageName = imageName;
      this.processedLayeredImage = new LayeredImageV0(width, height);
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
      layeredImages.put(imageName, processedLayeredImage);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command which creates a new, transparent image with specified dimensions.
   */
  class CreateTransparentCommand implements ParsedCommand {

    private final String imageName;
    private final GraphOfPixels processedImage;

    /**
     * Constructs a new CreateTransparentCommand, which creates a new transparent image with the
     * given name and dimensions.
     *
     * @param imageName The name of the image to be created
     * @param width     The width of the image to be created
     * @param height    The height of the image to be created
     * @throws IllegalArgumentException If given a null imageName
     */
    public CreateTransparentCommand(String imageName, int width, int height)
        throws IllegalArgumentException {
      if (imageName == null) {
        throw new IllegalArgumentException("Null imageName");
      }
      this.imageName = imageName;
      this.processedImage = ImageToGraphConverter.createTransparentGraph(width, height);
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
      graphs.put(imageName, processedImage);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command which creates a new image from an existing file.
   */
  class CreateFromImageCommand implements ParsedCommand {

    private final String imageName;
    private final GraphOfPixels processedFile;

    /**
     * Creates a new CreateFromImageCommand, which creates a new image of the given name by reading
     * the file of the given name.
     *
     * @param imageName The name of the image to be created
     * @param fileName  The name of the file to be read
     * @throws IllegalArgumentException If given a null input
     */
    public CreateFromImageCommand(String imageName, String fileName)
        throws IllegalArgumentException {
      if (imageName == null || fileName == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageName = imageName;
      this.processedFile = ImageToGraphConverter.convertImage(fileName);
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
      graphs.put(imageName, processedFile);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command which creates a new, empty image(not very useful as there is no script to
   * change the size of an existing image). //TODO DECIDE IF SOMETHING SHOULD BE DONE ABOUT THIS
   */
  class CreateEmptyImageCommand implements ParsedCommand {

    private final String imageName;

    /**
     * Constructs a new CreateEmptyImageCommand, which will create an empty image of the given
     * name.
     *
     * @param imageName The name of the image to be created
     * @throws IllegalArgumentException If given a null input
     */
    public CreateEmptyImageCommand(String imageName) throws IllegalArgumentException {
      if (imageName == null) {
        throw new IllegalArgumentException("Null imageName");
      }
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
      graphs.put(imageName, ImageToGraphConverter.createEmptyGraph());
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command which creates a new image as a copy of an existing image.
   */
  class CreateCopyCommand implements ParsedCommand {

    private final String imageName;
    private final String toCopy;

    /**
     * Creates a new CreateCopyCommand, which will create a new image with the given name by copying
     * an existing image of the other given name.
     *
     * @param imageName The name of the new image to be created
     * @param toCopy    The name of the image to be copied
     */
    public CreateCopyCommand(String imageName, String toCopy) {
      if (imageName == null || toCopy == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageName = imageName;
      this.toCopy = toCopy;
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
      if (!graphs.containsKey(toCopy)) {
        throw new IllegalArgumentException(
            "The image this command is supposed to copy does not exist");
      }
      graphs.put(imageName, ImageToGraphConverter.createCopyOfGraph(graphs.get(toCopy)));
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command to create a new graph image from a specified checkerboard command.
   */
  class CreateCheckerBoardCommand implements ParsedCommand {

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

  /**
   * Represents a command which will create a new layer on an existing layered image, with a given
   * name and layer to copy from that existing image.
   */
  class CopyLayerCommand implements ParsedCommand {

    private final String imageToAddTo;
    private final String layerToAdd;
    private final String layerToCopy;

    /**
     * Constructs a new CopyLayerCommand, which will copy the layer of the given name in the layered
     * image of the given name and give it the given name for the new layer.
     *
     * @param imageToAddTo The name of the layered image to be added to
     * @param layerToAdd   The name of the new layer to add
     * @param layerToCopy  The name of the existing layer to copy
     * @throws IllegalArgumentException If given a null input
     */
    public CopyLayerCommand(String imageToAddTo, String layerToAdd, String layerToCopy)
        throws IllegalArgumentException {
      if (imageToAddTo == null || layerToAdd == null || layerToCopy == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageToAddTo = imageToAddTo;
      this.layerToAdd = layerToAdd;
      this.layerToCopy = layerToCopy;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (!layeredImages.containsKey(imageToAddTo)) {
        throw new IllegalArgumentException("The image this command is supposed to alter does not"
            + " exist");
      }
      layeredImages.get(imageToAddTo).addLayer(layerToAdd, layerToCopy);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command which saves an existing layered image as the result of applying a basic
   * blend to that image.
   */
  class BasicBlendCommand implements ParsedCommand {

    private final String imageToBlend;
    private final OutputType outputType;
    private final String fileName;

    /**
     * Constructs a new BasicBlendCommand, which will blend an image of the given name and save it
     * as a file of the given name and type.
     *
     * @param imageToBlend The name of the layered image to be blended
     * @param outputType   The type of file to be outputted
     * @param fileName     The name of the file to be outputted
     * @throws IllegalArgumentException If given a null input
     */
    public BasicBlendCommand(String imageToBlend, String outputType, String fileName)
        throws IllegalArgumentException {
      if (imageToBlend == null || outputType == null || fileName == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageToBlend = imageToBlend;
      this.outputType = OutputType.convertString(outputType);
      this.fileName = fileName;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (!layeredImages.containsKey(imageToBlend)) {
        throw new IllegalArgumentException(
            "The layered image this command is supposed to blend does"
                + " not exist");
      }
      layeredImages.get(imageToBlend).saveAsImage(new BasicBlend(), outputType, fileName);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command to apply a sepia transformation to either a graph of pixels or layer of a
   * layered image.
   */
  class SharpenCommand implements ParsedCommand {

    private final String imageToUpdate;
    private final String layerToUpdate;

    /**
     * Constructs a new SharpenCommand, to be applied to an existing GraphOfPixels or layer of an
     * existing layered image.
     *
     * @param imageToUpdate The name of the image to be applied to
     * @param layerToUpdate The name of the layer to be applied to, may be null if applied to a
     *                      non-layered image
     * @throws IllegalArgumentException If given a null imageToUpdate
     */
    public SharpenCommand(String imageToUpdate, String layerToUpdate)
        throws IllegalArgumentException {
      if (imageToUpdate == null) {
        throw new IllegalArgumentException("Null image name");
      }
      this.imageToUpdate = imageToUpdate;
      this.layerToUpdate = layerToUpdate;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (graphs.containsKey(imageToUpdate)) {
        graphs.get(imageToUpdate).applyMutator(new SharpenFilter());
      } else if (layeredImages.containsKey(imageToUpdate)) {
        layeredImages.get(imageToUpdate).getLayer(layerToUpdate).applyMutator(new SharpenFilter());
      } else {
        throw new IllegalArgumentException("The image this command is supposed to sharpen does"
            + " not exist");
      }
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command which can be used to create a new layer on an existing layered image.
   */
  class AddImageLayerCommand implements ParsedCommand {

    private final String imageToAddTo;
    private final String layerToAdd;
    private final String fileToRead;

    /**
     * Constructs a new AddImageLayerCommand, which adds to an image of the given name a layer of
     * the given name by reading the file of the given filename.
     *
     * @param imageToAddTo The name of the of the image this command will add a layer to
     * @param layerToAdd   The name of the layer to be added
     * @param fileToRead   The name of the file to read and created into a layer
     * @throws IllegalArgumentException If given a null input
     */
    public AddImageLayerCommand(String imageToAddTo, String layerToAdd, String fileToRead)
        throws IllegalArgumentException {
      if (imageToAddTo == null || layerToAdd == null || fileToRead == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageToAddTo = imageToAddTo;
      this.layerToAdd = layerToAdd;
      this.fileToRead = fileToRead;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (!layeredImages.containsKey(imageToAddTo)) {
        throw new IllegalArgumentException("There is no image with the name this command will"
            + " add to.");
      }
      layeredImages.get(imageToAddTo).loadImageAsLayer(layerToAdd, fileToRead);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command to add a new, empty layer to an existing layeredImage.
   */
  class AddLayerCommand implements ParsedCommand {

    private final String imageToAddTo;
    private final String layerToAdd;

    /**
     * Constructs a new AddLayerCommand, which will add a layer of the given name to a layeredImage
     * with the given name.
     *
     * @param imageToAddTo The name of the image to be added to
     * @param layerToAdd   The name of the layer to be added
     * @throws IllegalArgumentException If given a null input
     */
    public AddLayerCommand(String imageToAddTo, String layerToAdd) throws IllegalArgumentException {
      if (imageToAddTo == null || layerToAdd == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageToAddTo = imageToAddTo;
      this.layerToAdd = layerToAdd;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (!layeredImages.containsKey(imageToAddTo)) {
        throw new IllegalArgumentException("The layered image this command is supposed to add to "
            + "does not exist");
      }
      layeredImages.get(imageToAddTo).addLayer(layerToAdd);
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command to apply a blur filter to either a graph of pixels or layer of a layered
   * image.
   */
  class BlurCommand implements ParsedCommand {

    private String imageToUpdate;
    private String layerToUpdate;

    /**
     * Constructs a new BlurCommand, to be applied to either an existing image or existing layer of
     * and existing image.
     *
     * @param imageToUpdate The name of the image to be blurred, or the layeredImage which contains
     *                      the layer to be blurred.
     * @param layerToUpdate The name of the layer to be blurred, might be null if this command is
     *                      applied to a non-layered image.
     * @throws IllegalArgumentException If given a null imageToUpdate
     */
    public BlurCommand(String imageToUpdate, String layerToUpdate) throws IllegalArgumentException {
      if (imageToUpdate == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.imageToUpdate = imageToUpdate;
      this.layerToUpdate = layerToUpdate;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (graphs.containsKey(imageToUpdate)) {
        graphs.get(imageToUpdate).applyMutator(new BlurFilter());
      } else if (layeredImages.containsKey(imageToUpdate)) {
        if (layerToUpdate == null) {
          throw new IllegalArgumentException("Null layer to be blurred");
        }
        layeredImages.get(imageToUpdate).getLayer(layerToUpdate).applyMutator(new BlurFilter());
      } else {
        throw new IllegalArgumentException(
            "The image this command is trying to blur does not exist");
      }
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command to apply a greyscale transformation to either a graph of pixels or layer
   * of a layered image.
   */
  class GreyscaleCommand implements ParsedCommand {

    private String imageToUpdate;
    private String layerToUpdate;

    /**
     * Constructs a new GreyscaleCommand, to be applied to an existing GraphOfPixels or layer of an
     * existing layered image.
     *
     * @param imageToUpdate The name of the image to be applied to
     * @param layerToUpdate The name of the layer to be applied to, may be null if applied to a
     *                      non-layered image
     * @throws IllegalArgumentException If given a null imageToUpdate
     */
    public GreyscaleCommand(String imageToUpdate, String layerToUpdate)
        throws IllegalArgumentException {
      if (imageToUpdate == null) {
        throw new IllegalArgumentException("Null image name");
      }
      this.imageToUpdate = imageToUpdate;
      this.layerToUpdate = layerToUpdate;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (graphs.containsKey(imageToUpdate)) {
        graphs.get(imageToUpdate).applyMutator(new GreyscaleTransform());
      } else if (layeredImages.containsKey(imageToUpdate)) {
        if (layerToUpdate == null) {
          throw new IllegalArgumentException("Null layer");
        }
        layeredImages.get(imageToUpdate).getLayer(layerToUpdate)
            .applyMutator(new GreyscaleTransform());
      } else {
        throw new IllegalArgumentException(
            "The image this command is supposed to Greyscale does not"
                + " exist");
      }
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Represents a command to apply a sepia transformation to either a graph of pixels or layer of a
   * layered image.
   */
  class SepiaCommand implements ParsedCommand {

    private String imageToUpdate;
    private String layerToUpdate;

    /**
     * Constructs a new SepiaCommand, to be applied to an existing GraphOfPixels or layer of an
     * existing layered image.
     *
     * @param imageToUpdate The name of the image to be applied to
     * @param layerToUpdate The name of the layer to be applied to, may be null if applied to a
     *                      non-layered image
     * @throws IllegalArgumentException If given a null imageToUpdate
     */
    public SepiaCommand(String imageToUpdate, String layerToUpdate)
        throws IllegalArgumentException {
      if (imageToUpdate == null) {
        throw new IllegalArgumentException("Null image name");
      }
      this.imageToUpdate = imageToUpdate;
      this.layerToUpdate = layerToUpdate;
    }

    @Override
    public void execute(HashMap<String, GraphOfPixels> graphs,
        HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
      if (graphs == null || layeredImages == null) {
        throw new IllegalArgumentException("Null inputs");
      }
      if (graphs.containsKey(imageToUpdate)) {
        graphs.get(imageToUpdate).applyMutator(new SepiaTransform());
      } else if (layeredImages.containsKey(imageToUpdate)) {
        if (layerToUpdate == null) {
          throw new IllegalArgumentException("Null layer");
        }
        layeredImages.get(imageToUpdate).getLayer(layerToUpdate).applyMutator(new SepiaTransform());
      } else {
        throw new IllegalArgumentException(
            "The image this command is supposed to blur does not exit");
      }
    }

    @Override
    public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
      // This command does not impact the current state of the language
      if (toAlter == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }
}

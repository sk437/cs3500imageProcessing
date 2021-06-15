package scriptlanguage;

import java.util.ArrayList;
import java.util.List;
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
import scriptlanguage.parsedcommands.createlayered.CreateNewLayeredImageCommand;
import scriptlanguage.parsedcommands.creategraph.CreateTransparentCommand;
import scriptlanguage.parsedcommands.createlayered.ImportNewLayeredImageCommand;
import scriptlanguage.parsedcommands.ParsedCommand;
import scriptlanguage.parsedcommands.load.LoadCommand;
import scriptlanguage.parsedcommands.loadlayer.LoadLayerCommand;
import scriptlanguage.parsedcommands.movelayer.MoveLayerCommand;
import scriptlanguage.parsedcommands.removelayer.RemoveLayerByNameCommand;
import scriptlanguage.parsedcommands.save.SaveCommand;
import scriptlanguage.parsedcommands.savelayeredimage.SaveLayeredCommand;
import scriptlanguage.parsedcommands.updatecolor.UpdateColorCommand;
import scriptlanguage.parsedcommands.updatevisibility.UpdateVisibilityCommand;

public enum Command {
  createImage {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) throws IllegalArgumentException {
      Command.assertNonNullInputs(inputs);
      if (inputs.size() < 2) {
        throw new IllegalArgumentException("Not enough inputs");
      }
      List<Integer> intInputs;
      switch(inputs.get(1)) {
        case "checkerboard":
          Command.assertValidNumInputs(10, inputs);
          intInputs = Command.convertIntegerInputs(2,10,inputs);
          return new CreateCheckerBoardCommand(inputs.get(0), intInputs.get(0), intInputs.get(1),
              intInputs.get(2), intInputs.get(3), intInputs.get(4), intInputs.get(5), intInputs.get(6),
              intInputs.get(7));
        case "empty":
          Command.assertValidNumInputs(2, inputs);
          return new CreateEmptyImageCommand(inputs.get(0));
        case "transparent":
          Command.assertValidNumInputs(4, inputs);
          intInputs = Command.convertIntegerInputs(2,4,inputs);
          return new CreateTransparentCommand(inputs.get(0), intInputs.get(0), intInputs.get(1));
        case "copy":
          Command.assertValidNumInputs(3,inputs);
          return new CreateCopyCommand(inputs.get(0), inputs.get(2));
        case "from-image":
          Command.assertValidNumInputs(3,inputs);
          return new CreateFromImageCommand(inputs.get(0), inputs.get(2));
        default:
          throw new IllegalArgumentException("Invalid type specified for this command");
      }
    }
  },
  createLayeredImage {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) throws IllegalArgumentException {
      Command.assertNonNullInputs(inputs);
      if (inputs.size() < 2) {
        throw new IllegalArgumentException("Invalid number of inputs");
      }
      switch(inputs.size()) {
        case 2:
          return new ImportNewLayeredImageCommand(inputs.get(0), inputs.get(1));
        case 3:
          List<Integer> intInputs = Command.convertIntegerInputs(1,3,inputs);
          return new CreateNewLayeredImageCommand(inputs.get(0), intInputs.get(0), intInputs.get(1));
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  },
  updateColor {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      List<Integer> intInputs;
      switch(inputs.size()) {
        case 6:
          if (currentImage == null) {
            throw new IllegalArgumentException("This command cannot be called with the given amount"
                + " of inputs, because there is no default image");
          }
          intInputs = Command.convertIntegerInputs(0,6,inputs);
          return new UpdateColorCommand(currentImage, currentLayer, intInputs.get(0), intInputs.get(1),
               intInputs.get(2), intInputs.get(3), intInputs.get(4), intInputs.get(5));
        case 7:
          intInputs = Command.convertIntegerInputs(1,7,inputs);
          return new UpdateColorCommand(inputs.get(0), currentLayer, intInputs.get(0), intInputs.get(1),
              intInputs.get(2), intInputs.get(3), intInputs.get(4), intInputs.get(5));
        case 8:
          intInputs = Command.convertIntegerInputs(2,8,inputs);
          return new UpdateColorCommand(inputs.get(0), inputs.get(1), intInputs.get(0), intInputs.get(1),
              intInputs.get(2), intInputs.get(3), intInputs.get(4), intInputs.get(5));
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  },
  applyMutator {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      if (inputs.size() < 1) {
        throw new IllegalArgumentException("Invalid number of inputs");
      }
      switch(inputs.get(0)) {
        case "blur":
          switch(inputs.size()) {
            case 1:
              if (currentImage == null) {
                throw new IllegalArgumentException("This command cannot be called with the given amount"
                    + " of inputs, because there is no default image");
              }
              return new BlurCommand(currentImage, currentLayer);
            case 2:
              return new BlurCommand(inputs.get(1), currentLayer);
            case 3:
              return new BlurCommand(inputs.get(1), inputs.get(2));
            default:
              throw new IllegalArgumentException("Invalid number of inputs");
          }
        case "sharpen":
          switch(inputs.size()) {
            case 1:
              if (currentImage == null) {
                throw new IllegalArgumentException("This command cannot be called with the given amount"
                    + " of inputs, because there is no default image");
              }
              return new SharpenCommand(currentImage, currentLayer);
            case 2:
              return new SharpenCommand(inputs.get(1), currentLayer);
            case 3:
              return new SharpenCommand(inputs.get(1), inputs.get(2));
            default:
              throw new IllegalArgumentException("Invalid number of inputs");
          }
        case "sepia":
          switch(inputs.size()) {
            case 1:
              if (currentImage == null) {
                throw new IllegalArgumentException("This command cannot be called with the given amount"
                    + " of inputs, because there is no default image");
              }
              return new SepiaCommand(currentImage, currentLayer);
            case 2:
              return new SepiaCommand(inputs.get(1), currentLayer);
            case 3:
              return new SepiaCommand(inputs.get(1), inputs.get(2));
            default:
              throw new IllegalArgumentException("Invalid number of inputs");
          }
        case "greyscale":
          switch(inputs.size()) {
            case 1:
              if (currentImage == null) {
                throw new IllegalArgumentException("This command cannot be called with the given amount"
                    + " of inputs, because there is no default image");
              }
              return new GreyscaleCommand(currentImage, currentLayer);
            case 2:
              return new GreyscaleCommand(inputs.get(1), currentLayer);
            case 3:
              return new GreyscaleCommand(inputs.get(1), inputs.get(2));
            default:
              throw new IllegalArgumentException("Invalid number of inputs");
          }
        default:
          throw new IllegalArgumentException("Unsupported mutator");
      }
    }
  },
  save {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      switch(inputs.size()) {
        case 2:
          if (currentImage == null) {
            throw new IllegalArgumentException("This command cannot be called with the given amount"
                + " of inputs, because there is no default image");
          }
          return new SaveCommand(currentImage, currentLayer, inputs.get(0), inputs.get(1));
        case 3:
          return new SaveCommand(inputs.get(0), currentLayer, inputs.get(1), inputs.get(2));
        case 4:
          return new SaveCommand(inputs.get(0), inputs.get(1), inputs.get(2), inputs.get(3));
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  },
  saveLayered {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) throws IllegalArgumentException {
      Command.assertNonNullInputs(inputs);
      switch(inputs.size()) {
        case 1:
          if (currentImage == null) {
            throw new IllegalArgumentException("This command cannot be called with the given amount"
                + " of inputs, because there is no default image");
          }
          return new SaveLayeredCommand(currentImage, inputs.get(0));
        case 2:
          return new SaveLayeredCommand(inputs.get(0), inputs.get(1));
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  },
  load {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      return new LoadCommand(inputs.get(0));
    }
  },
  setCurrentLayer {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      return new LoadLayerCommand(inputs.get(0));
    }
  },
  addLayer {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      switch(inputs.size()) {
        case 1:
          if (currentImage == null) {
            throw new IllegalArgumentException("This command cannot be called with the given amount"
                + " of inputs, because there is no default image");
          }
          return new AddLayerCommand(currentImage, inputs.get(0));
        case 2:
          return new AddLayerCommand(inputs.get(0), inputs.get(1));
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  },
  copyLayer {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) throws IllegalArgumentException {
      Command.assertNonNullInputs(inputs);
      switch(inputs.size()) {
        case 2:
          if (currentImage == null) {
            throw new IllegalArgumentException("This command cannot be called with the given amount"
                + " of inputs, because there is no default image");
          }
          return new CopyLayerCommand(currentImage, inputs.get(0), inputs.get(1));
        case 3:
          return new CopyLayerCommand(inputs.get(0), inputs.get(1), inputs.get(2));
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  },
  addImageAsLayer {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      switch(inputs.size()) {
        case 2:
          if (currentImage == null) {
            throw new IllegalArgumentException("This command cannot be called with the given amount"
                + " of inputs, because there is no default image");
          }
          return new AddImageLayerCommand(currentImage, inputs.get(0), inputs.get(1));
        case 3:
          return new AddImageLayerCommand(inputs.get(0), inputs.get(1), inputs.get(2));
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  },
  moveLayer {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      List<Integer> intInputs;
      switch(inputs.size()) {
        case 2:
          if (currentImage == null) {
            throw new IllegalArgumentException("This command cannot be called with the given amount"
                + " of inputs, because there is no default image");
          }
          intInputs = Command.convertIntegerInputs(1,2,inputs);
          return new MoveLayerCommand(currentImage, inputs.get(0), intInputs.get(0));
        case 3:
          intInputs = Command.convertIntegerInputs(2,3,inputs);
          return new MoveLayerCommand(inputs.get(0), inputs.get(1), intInputs.get(0));
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  },
  removeLayer {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      switch(inputs.size()) {
        case 1:
          if (currentImage == null) {
            throw new IllegalArgumentException("This command cannot be called with the given amount"
                + " of inputs, because there is no default image");
          }
          return new RemoveLayerByNameCommand(currentImage, inputs.get(0));
        case 2:
          return new RemoveLayerByNameCommand(inputs.get(0), inputs.get(1));
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  },
  saveAsImage {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      switch(inputs.size()) {
        case 3:
          switch(inputs.get(0)) {
            case "basic":
              if (currentImage == null) {
                throw new IllegalArgumentException("This command cannot be called with the given amount"
                    + " of inputs, because there is no default image");
              }
              return new BasicBlendCommand(currentImage, inputs.get(1), inputs.get(2));
            default:
              throw new IllegalArgumentException("Unsupported blend type");
          }
        case 4:
          switch(inputs.get(1)) {
            case "basic":
              return new BasicBlendCommand(inputs.get(0), inputs.get(2), inputs.get(3));
            default:
              throw new IllegalArgumentException("Unsupported blend type");
          }
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  },
  updateVisibility {
    @Override
    public ParsedCommand returnExecutable(List<String> inputs, String currentImage,
        String currentLayer) {
      Command.assertNonNullInputs(inputs);
      boolean newVisibility;
      switch(inputs.size()) {
        case 1:
          if (currentImage == null || currentLayer == null) {
            throw new IllegalArgumentException("This command cannot be called with the given amount"
                + " of inputs, because there is no default image and/or layer");
          }
          newVisibility = Boolean.parseBoolean(inputs.get(0));
          return new UpdateVisibilityCommand(currentImage, currentLayer, newVisibility);
        case 2:
          if (currentLayer == null) {
            throw new IllegalArgumentException("This command cannot be called with the given amount"
                + " of inputs, because there is no default layer");
          }
          newVisibility = Boolean.parseBoolean(inputs.get(1));
          return new UpdateVisibilityCommand(inputs.get(0), currentLayer, newVisibility);
        case 3:
          newVisibility = Boolean.parseBoolean(inputs.get(2));
          return new UpdateVisibilityCommand(inputs.get(0), inputs.get(1), newVisibility);
        default:
          throw new IllegalArgumentException("Invalid number of inputs");
      }
    }
  };

  /**
   * Given a list of inputs as well as default inputs, returns an executable command which can
   * be used to apply a command with the desired parameters to a collection of graphs and layered
   * images.
   * @param inputs The input parameters to be parsed
   * @param currentImage The default image input
   * @param currentLayer The default layer input
   * @return The processed executable command
   * @throws IllegalArgumentException If the given inputs are not valid for this type of command
   */
  public abstract ParsedCommand returnExecutable(List<String> inputs, String currentImage,
      String currentLayer) throws IllegalArgumentException;

  /**
   * Given a list of inputs and the expected length of that list, and throws an exception if the
   * length does not match expectation.
   * @param numExpected The expected length of the list
   * @param numActual The actual list of inputs
   * @throws IllegalArgumentException If the length of the inputs do not match
   */
  private static void assertValidNumInputs(int numExpected, List<String> numActual) throws IllegalArgumentException {
    if (numActual == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (numActual.size() != numExpected) {
      throw new IllegalArgumentException("Invalid number of inputs");
    }
  }

  /**
   * Given a list of inputs, throws an exception if either the list itself is null or any of the
   * elements of the list are null.
   * @param inputs The list of inputs to be processed
   * @throws IllegalArgumentException If there are any null elements within the given list
   */
  private static void assertNonNullInputs(List<String> inputs) throws IllegalArgumentException {
    if (inputs == null) {
      throw new IllegalArgumentException("Null input");
    }
    for (String s : inputs) {
      if (s == null) {
        throw new IllegalArgumentException("Null input");
      }
    }
  }

  /**
   * Given a list of inputs, returns a new list of integers by converting all elements in the given
   * list beginning at the start and going up to (but not including) the end index to integers,
   * throwing an exception if any of the inputs cannot be parsed.
   * @param start The start point for conversion
   * @param end The end point for conversion
   * @param inputs The inputs to be converted
   * @return The list of converted integers
   * @throws IllegalArgumentException If any of the given inputs cannot be properly parsed into integers,
   *                                  or if given a null list of inputs.
   */
  private static List<Integer> convertIntegerInputs(int start, int end, List<String> inputs) throws IllegalArgumentException {
    if (inputs == null) {
      throw new IllegalArgumentException("Null input");
    }
    List<Integer> intInputs = new ArrayList<Integer>();
    for (int i = start; i < end; i += 1) {
      try {
        intInputs.add(Integer.parseInt(inputs.get(i)));
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("One of the integer inputs is not a valid integer");
      }
    }
    return intInputs;
  }
}

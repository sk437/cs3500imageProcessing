import imageAsGraph.GraphOfPixels;
import imageAsGraph.ImageToGraphConverter;
import imageAsGraph.OutputType;
import imageInput.imageProgram.CheckerBoard;
import imageInput.imageProgram.ImageProgram;
import mutators.colorTransformations.GreyscaleTransform;
import mutators.colorTransformations.SepiaTransform;
import mutators.filters.BlurFilter;
import mutators.filters.SharpenFilter;
import pixel.SimplePixel;

/**
 * For running methods directly through a main method.
 */
public class TestSimpleGraphOfPixels {

  /**
   * The main method for running tests directly.
   *
   * @param args CommandLine args(not used)
   */
  public static void main(String[] args) {
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
  }
}

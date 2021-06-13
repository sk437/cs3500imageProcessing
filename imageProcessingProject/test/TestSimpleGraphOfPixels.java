import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.OutputType;
import mutators.colortransformations.GreyscaleTransform;
import mutators.colortransformations.SepiaTransform;
import mutators.filters.BlurFilter;
import mutators.filters.SharpenFilter;

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
    System.out.println("WAH");

    GraphOfPixels graph2 = ImageToGraphConverter.convertComplexImage("outputImages/birb.jpg");
    System.out.println(graph2.getWidth() + " " + graph2.getHeight());
    graph2.writeToFile(OutputType.ppm, "outputImages/birbPPM");
  }
}

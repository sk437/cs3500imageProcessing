import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.Node;
import imageasgraph.OutputType;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import mutators.colortransformations.GreyscaleTransform;
import mutators.colortransformations.SepiaTransform;
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
    GraphOfPixels graph2 = ImageToGraphConverter.convertComplexImage("outputImages/vulture.png");
    graph2.writeToFile(OutputType.ppm, "outputImages/vulturePPM");
  }
}

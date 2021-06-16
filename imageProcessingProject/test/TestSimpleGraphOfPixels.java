import controller.ImageProcessingController;
import controller.ProcessingController;
import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.Node;
import imageasgraph.OutputType;
import java.io.InputStreamReader;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import layeredimage.blend.BasicBlend;
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

    //GraphOfPixels graph2 = ImageToGraphConverter.convertComplexImage("outputImages/birbBetter.jpg");
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
    ImageProcessingController controller = new ProcessingController("outputImages/TestScript.txt", System.out);
    //controller.run();
    GraphOfPixels forTesting = ImageToGraphConverter.createTransparentGraph(3,3);
    for (Node n : forTesting) {
      n.setOpacity(255);
      n.updateColors(new SimplePixel(255,0,0));
    }
    forTesting.writeToFile(OutputType.ppm, "outputImages/conversionTest");

  }
}

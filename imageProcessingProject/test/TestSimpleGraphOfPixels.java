import imageAsGraph.GraphOfPixels;
import imageAsGraph.ImageToGraphConverter;
import imageAsGraph.OutputType;
import imageInput.imageProgram.CheckerBoard;
import imageInput.imageProgram.ImageProgram;
import pixel.SimplePixel;

public class TestSimpleGraphOfPixels {

    public static void main(String[] args) {
        ImageProgram checkers = new CheckerBoard(1,4,new SimplePixel(0, 0, 0),
                new SimplePixel(255, 255, 255));
        GraphOfPixels graph0 = ImageToGraphConverter.convertProgram(checkers);
        graph0.writeToFile(OutputType.ppm, "outputImages/checkers");
        GraphOfPixels graph1 = ImageToGraphConverter.convertPPM("src/imageInput/Koala.ppm");
        graph1.writeToFile(OutputType.ppm, "outputImages/koalaButBetter");
    }
}
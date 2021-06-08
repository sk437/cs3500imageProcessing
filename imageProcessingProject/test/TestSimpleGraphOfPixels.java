import imageAsGraph.GraphOfPixels;
import imageAsGraph.ImageToGraphConverter;
import imageAsGraph.OutputType;
import imageInput.imageProgram.CheckerBoard;
import imageInput.imageProgram.ImageProgram;
import mutators.colortransformations.GreyscaleTransform;
import mutators.colortransformations.SepiaTransform;
import mutators.filters.BlurFilter;
import mutators.filters.SharpenFilter;
import pixel.SimplePixel;

public class TestSimpleGraphOfPixels {
    public static void main(String[] args) {

        ImageProgram checkers = new CheckerBoard(50,36,new SimplePixel(0, 0, 0),
                new SimplePixel(255, 255, 255));
        GraphOfPixels graph0 = ImageToGraphConverter.convertProgram(checkers);
       /*
        graph0.writeToFile(OutputType.ppm, "outputImages/checkers");
        GraphOfPixels graph1 = ImageToGraphConverter.convertPPM("src/imageInput/Koala.ppm");
        graph1.applyMutator(new SharpenFilter());
        graph1.applyMutator(new SharpenFilter());
        graph1.writeToFile(OutputType.ppm, "outputImages/koalaButBetter");
        */
        GraphOfPixels graph2 = ImageToGraphConverter.convertPPM("outputImages/charl.ppm");
        graph2.applyMutator(new GreyscaleTransform());
        graph2.writeToFile(OutputType.ppm, "outputImages/charlButGreyscale");

        GraphOfPixels graph3 = ImageToGraphConverter.convertPPM("outputImages/charl.ppm");
        graph3.applyMutator(new SepiaTransform());
        graph3.writeToFile(OutputType.ppm, "outputImages/charlButSepia");

        GraphOfPixels graph4 = ImageToGraphConverter.convertPPM("outputImages/charl.ppm");
        graph4.applyMutator(new BlurFilter());
        graph4.writeToFile(OutputType.ppm, "outputImages/charlButBlur");

        GraphOfPixels graph5 = ImageToGraphConverter.convertPPM("outputImages/charl.ppm");
        graph5.applyMutator(new SharpenFilter());
        graph5.writeToFile(OutputType.ppm, "outputImages/charlButSharp");


        /*
        Matrix matrix0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0)), 2, 2);
        System.out.println(matrix0);
        Matrix matrix1 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)), 3, 2);
        System.out.println();
        System.out.println(matrix1);

        System.out.println(matrix0.matrixMultiply(matrix1));
        */
    }
}

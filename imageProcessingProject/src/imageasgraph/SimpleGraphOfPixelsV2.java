package imageasgraph;

/**
 * Second version of a Simple Graph of Pixels, implements new features for assignment 6.
 */
public class SimpleGraphOfPixelsV2 extends SimpleGraphOfPixels {

  @Override
  public void writeToFile(OutputType fileType, String fileName) throws IllegalArgumentException {
    if (fileType == null || fileName == null) {
      throw new IllegalArgumentException("One or both of the arguments is null");
    }
    switch(fileType) {
      case ppm:
        this.writePPM(fileName);
      default:
        throw new IllegalArgumentException("Unsupported fileType");
    }
  }
}

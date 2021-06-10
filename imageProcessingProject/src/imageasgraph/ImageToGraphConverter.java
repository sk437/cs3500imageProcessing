package imageasgraph;

import imageinput.imageprogram.ImageProgram;
import pixel.PixelAsColors;
import pixel.SimplePixel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contains methods which can convert a given image, of various types, into an image as a graph of
 * pixel nodes - used to create GraphOfPixels as a builder class.
 */
public class ImageToGraphConverter {

  /**
   * Converts the given image program to a graph of pixel nodes.
   *
   * @param prog The image program to be converted
   * @return The converted graph of pixel nodes
   * @throws IllegalArgumentException If given program is null
   */
  public static GraphOfPixels convertProgram(ImageProgram prog) throws IllegalArgumentException {
    if (prog == null) {
      throw new IllegalArgumentException("Null program given.");
    }
    return ImageToGraphConverter.convertFromDoubleArray(prog.getImage());
  }

  /**
   * Creates an empty(ish) graph, with only one white node at the top left.
   */
  public static GraphOfPixels createEmptyGraph() {
    AbstractGraphOfPixels toReturn = new SimpleGraphOfPixels();
    toReturn.addFirstNode(new PixelNode(new SimplePixel(255, 255, 255)));
    return toReturn;
  }

  /**
   * Converts the ppm file as specified by the given string to a graph of pixel nodes.
   *
   * @param fileName The name of the ppm file to be converted
   * @return The converted graph of pixel nodes
   * @throws IllegalArgumentException If the fileName cannot be found or if it is null
   */
  public static GraphOfPixels convertPPM(String fileName) throws IllegalArgumentException {
    if (fileName == null) {
      throw new IllegalArgumentException("Null fileName");
    }
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(fileName));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found");
    }
    StringBuilder builder = new StringBuilder();

    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid ppm file");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();

    ArrayList<ArrayList<PixelAsColors>> toReturn = new ArrayList<ArrayList<PixelAsColors>>();

    for (int i = 0; i < height; i++) {
      ArrayList<PixelAsColors> current = new ArrayList<PixelAsColors>();
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        current.add(new SimplePixel(r, g, b));
      }
      toReturn.add(current);
    }
    return convertFromDoubleArray(toReturn);
  }

  /**
   * Given a double array of pixels, creates a graph of equal size and with each node at x,y
   * corresponding to the color of the pixel in the double array at the same position.
   *
   * @param toConvert The double array of pixels to be converted - this must be rectangular, all of
   *                  it's rows must have the same length.
   * @return The generated graph
   * @throws IllegalArgumentException if the given double array is null, or if any of the interior
   *                                  arrays are null, or if any pixels are null.
   */
  private static GraphOfPixels convertFromDoubleArray(
      ArrayList<ArrayList<PixelAsColors>> toConvert) {
    if (toConvert == null) {
      throw new IllegalArgumentException("Null input");
    }
    if (toConvert.get(0) == null) {
      throw new IllegalArgumentException("Null interior array");
    }
    int rowWidth = toConvert.get(0).size();
    for (ArrayList<PixelAsColors> row : toConvert) {
      if (row == null) {
        throw new IllegalArgumentException("Null interior array");
      }
      if (row.size() != rowWidth) {
        throw new IllegalArgumentException("toConvert is not square");
      }
      for (PixelAsColors pixel : row) {
        if (pixel == null) {
          throw new IllegalArgumentException("Null pixel");
        }
      }
    }
    AbstractGraphOfPixels toReturn = new SimpleGraphOfPixels();
    toReturn.addFirstNode(new PixelNode(new SimplePixel(toConvert.get(0).get(0))));
    /*
    Creates a graph of the same size as the input, with all white nodes.
     */
    for (int col = 0; col < toConvert.get(0).size() - 1; col += 1) {
      toReturn.insertColumn(col);
    }
    for (int row = 0; row < toConvert.size() - 1; row += 1) {
      toReturn.insertRow(row);
    }
    /*
    Updates the colors of the newly created graphs to match the input.
     */
    int col = 0;
    int row = 0;
    for (Node n : toReturn) {
      n.updateColors(toConvert.get(row).get(col));
      col += 1;
      if (col == toReturn.getWidth()) {
        row += 1;
        col = 0;
      }
    }
    return toReturn;
  }


}

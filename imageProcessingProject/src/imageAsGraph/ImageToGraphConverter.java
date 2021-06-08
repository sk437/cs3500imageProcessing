package imageAsGraph;

import imageInput.imageProgram.ImageProgram;
import pixel.PixelAsColors;
import pixel.SimplePixel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contains methods which can convert a given image, of various types, into an image as a graph
 * of pixel nodes - used to create GraphOfPixels as a builder class.
 */
public class ImageToGraphConverter {

  //TODO IF TOO INEFFICIENT CAN REWRITE TO ADD EACH PIXEL INDIVIDUALLY
  /**
   * Converts the given image program to a graph of pixel nodes.
   * @param prog The image program to be converted
   * @return The converted graph of pixel nodes
   */
  public static GraphOfPixels convertProgram(ImageProgram prog) {
    return ImageToGraphConverter.convertFromDoubleArray(prog.getImage());
  }

  /**
   * Converts the ppm file as specified by the given string to a graph of pixel nodes.
   * @param fileName The name of the ppm file to be converted
   * @return The converted graph of pixel nodes
   * @throws IllegalArgumentException If the fileName cannot be found or if it is null
   */
  public static GraphOfPixels convertPPM(String fileName) throws IllegalArgumentException{
    if (fileName == null) {
      throw new IllegalArgumentException("Null fileName");
    }
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(fileName));
    }
    catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found");
    }
    StringBuilder builder = new StringBuilder();

    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0)!='#') {
        builder.append(s+System.lineSeparator());
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

    for (int i=0;i<height;i++) {
      ArrayList<PixelAsColors> current = new ArrayList<PixelAsColors>();
      for (int j=0;j<width;j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        current.add(new SimplePixel(r, g, b));
      }
      toReturn.add(current);
    }
    return convertFromDoubleArray(toReturn);
  }

  private static GraphOfPixels convertFromDoubleArray(ArrayList<ArrayList<PixelAsColors>> toConvert) {
    AbstractGraphOfPixels toReturn = new SimpleGraphOfPixels();
    toReturn.addFirstNode(new PixelNode(new SimplePixel(toConvert.get(0).get(0))));
    for (int col = 0; col < toConvert.get(0).size() - 1; col += 1) {
      toReturn.insertColumn(col);
    }
    for (int row = 0; row < toConvert.size() - 1; row += 1) {
      toReturn.insertRow(row);
    }
    for (int row = 0; row < toReturn.getHeight(); row += 1) {
      for (int col = 0; col < toReturn.getWidth(); col += 1) {
        toReturn.getPixelAt(col, row).updateColors(toConvert.get(row).get(col));
      }
    }
    return toReturn;
  }
}
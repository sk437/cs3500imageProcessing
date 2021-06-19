package imageasgraph;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.imageio.ImageIO;
import mutators.Mutator;
import pixel.PixelAsColors;
import pixel.SimplePixel;

/**
 * Represents an image as a Graph of Pixels.
 */
public class SimpleGraphOfPixels extends AbstractGraphOfPixels {

  private AbstractNode topLeft;
  private int width;
  private int height;

  SimpleGraphOfPixels() {
    this.topLeft = new EmptyNode();
    this.width = 0;
    this.height = 0;
  }

  /**
   * Asserts that this graph is not empty, and thus operations besides adding an initial node can be
   * done on it.
   */
  private void assertGraphNotEmpty() {
    if (width == 0 && height == 0) {
      throw new IllegalArgumentException("Graph is empty");
    }
  }

  @Override
  public void writeToFile(OutputType fileType, String fileName) throws IllegalArgumentException {
    if (fileType == null || fileName == null) {
      throw new IllegalArgumentException("One or both of the arguments is null");
    }
    switch (fileType) {
      case ppm:
        this.writePPM(fileName);
        break;
      case png:
        this.writePNG(fileName);
        break;
      case jpeg:
        this.writeJPG(fileName);
        break;
      default:
        throw new IllegalArgumentException("Unsupported fileType");
    }
  }

  /**
   * Writes this image as a ppm file to the specified output.
   *
   * @param fileName The name of the file which will become the output
   */
  protected void writePPM(String fileName) {
    File output = new File(fileName + ".ppm");
    FileWriter writer;
    PrintWriter printer = null;
    try {
      writer = new FileWriter(output);
      printer = new PrintWriter(writer);
      printer.append("P3\n");
      printer.append("# Generated from project\n");
      printer.append(this.width + " ");
      printer.append(this.height + "\n");
      printer.append(PixelAsColors.maxColor + "\n");
      for (Node currentNode : this) {
        printer.append(currentNode.getRed() + "\n");
        printer.append(currentNode.getGreen() + "\n");
        printer.append(currentNode.getBlue() + "\n");
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid file");
    } finally {
      if (printer != null) {
        printer.close();
      }
    }
  }

  @Override
  public void applyMutator(Mutator mutator) {
    this.assertGraphNotEmpty();
    if (mutator == null) {
      throw new IllegalArgumentException("Null mutator");
    }
    mutator.apply(this);
  }

  @Override
  public Node getPixelAt(int x, int y) throws IllegalArgumentException {
    this.assertGraphNotEmpty();
    if (x >= width || y >= height || x < 0 || y < 0) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    Node toReturn = this.topLeft;
    for (int row = 0; row < y; row += 1) {
      toReturn = toReturn.getBelow();
    }
    for (int column = 0; column < x; column += 1) {
      toReturn = toReturn.getRight();
    }
    return toReturn;
  }

  @Override
  public void insertRow(int below) throws IllegalArgumentException {
    this.assertGraphNotEmpty();
    if (below >= this.height || below < 0) {
      throw new IllegalArgumentException("Index not in bounds");
    }
    AbstractNode currentTop = this.topLeft;
    for (int row = 0; row < below; row += 1) {
      currentTop = currentTop.getBelowAsUpdatable();
    }
    AbstractNode currentLeft = new EmptyNode();
    PixelAsColors white = new SimplePixel(255, 255, 255);
    for (int col = 0; col < this.width; col += 1) {
      AbstractNode toAdd = new PixelNode(new SimplePixel(white));
      toAdd.updateAbove(currentTop);
      toAdd.updateBelow(currentTop.getBelowAsUpdatable());
      toAdd.updateLeft(currentLeft);
      toAdd.getBelowAsUpdatable().updateAbove(toAdd);
      currentTop.updateBelow(toAdd);
      currentLeft.updateRight(toAdd);
      currentLeft = toAdd;
      currentTop = currentTop.getRightAsUpdatable();
    }
    currentLeft.updateRight(new EmptyNode());
    this.height += 1;
  }

  @Override
  public void insertColumn(int after) throws IllegalArgumentException {
    this.assertGraphNotEmpty();

    if (after >= this.width || after < 0) {
      throw new IllegalArgumentException("Index not in bounds");
    }
    AbstractNode currentLeft = this.topLeft;
    for (int col = 0; col < after; col += 1) {
      currentLeft = currentLeft.getRightAsUpdatable();
    }
    AbstractNode currentTop = new EmptyNode();
    PixelAsColors white = new SimplePixel(255, 255, 255);
    for (int row = 0; row < this.height; row += 1) {
      AbstractNode toAdd = new PixelNode(new SimplePixel(white));
      toAdd.updateAbove(currentTop);
      toAdd.updateLeft(currentLeft);
      toAdd.updateRight(currentLeft.getRightAsUpdatable());
      toAdd.getRightAsUpdatable().updateLeft(toAdd);
      currentTop.updateBelow(toAdd);
      currentLeft.updateRight(toAdd);
      currentLeft = currentLeft.getBelowAsUpdatable();
      currentTop = toAdd;
    }
    currentTop.updateBelow(new EmptyNode());
    this.width += 1;
  }

  @Override
  void addFirstNode(AbstractNode n) throws IllegalArgumentException {
    if (n.equals(new EmptyNode())) {
      throw new IllegalArgumentException("Starting node cannot be empty");
    }
    if (width != 0 || height != 0) {
      throw new IllegalArgumentException("This graph already has a starting node");
    }
    this.topLeft = n;
    this.width = 1;
    this.height = 1;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public Iterator<Node> iterator() {
    return new GraphIterator(this.topLeft);
  }

  /**
   * Writes this Graph as a PNG image file with the given string as it's name.
   *
   * @param fileName The name of the image file to be created
   * @throws IllegalArgumentException If the name given is null
   */
  protected void writePNG(String fileName) throws IllegalArgumentException {
    if (fileName == null) {
      throw new IllegalArgumentException("Null fileName");
    }
    BufferedImage toReturn = this.createBufferedImageForOutPut();

    File outPut = new File(fileName + ".png");

    try {
      ImageIO.write(toReturn, "png", outPut);
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not write to file");
    }
  }

  /**
   * Writes this Graph as a JPG image file with the given string as it's name.
   *
   * @param fileName The name of the image file to be created
   * @throws IllegalArgumentException If the name given is null
   */
  protected void writeJPG(String fileName) throws IllegalArgumentException {
    if (fileName == null) {
      throw new IllegalArgumentException("Null fileName");
    }
    BufferedImage toReturn = this.createBufferedImageNoAlpha();
    File outPut = new File(fileName + ".jpeg");

    try {
      ImageIO.write(toReturn, "jpeg", outPut);
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not write to file");
    }
  }

  /**
   * Creates a buffered image which represents the image that this graph does, with all the ARGB
   * values copied over.
   *
   * @return The buffered image representation
   */
  protected BufferedImage createBufferedImageForOutPut() {
    BufferedImage toReturn = new BufferedImage(this.getWidth(), this.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    int col = 0;
    int row = 0;
    for (Node n : this) {
      int rgb = (n.getOpacity() << 24 | n.getRed() << 16 | n.getGreen() << 8 | n.getBlue());
      toReturn.setRGB(col, row, rgb);
      col += 1;
      if (col == this.getWidth()) {
        col = 0;
        row += 1;
      }
    }
    return toReturn;
  }

  /**
   * Creates a buffered image which represents the image that this graph does, with all the RGB
   * values copied over.
   *
   * @return The buffered image representation
   */
  protected BufferedImage createBufferedImageNoAlpha() {
    BufferedImage toReturn = new BufferedImage(this.getWidth(), this.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    int col = 0;
    int row = 0;
    for (Node n : this) {
      int rgb = (n.getRed() << 16 | n.getGreen() << 8 | n.getBlue());
      toReturn.setRGB(col, row, rgb);
      col += 1;
      if (col == this.getWidth()) {
        col = 0;
        row += 1;
      }
    }
    return toReturn;
  }

}

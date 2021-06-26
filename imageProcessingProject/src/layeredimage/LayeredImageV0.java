package layeredimage;

import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.Node;
import imageasgraph.OutputType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import layeredimage.blend.BasicBlend;
import layeredimage.blend.Blend;

/**
 * An implementation of a Layered Image that represents multiple images on multiple layers of a
 * structure that can be manipulated or processed individually.
 */
public class LayeredImageV0 implements LayeredImage {

  protected final Map<String, LayerData> layers;
  protected final int width;
  protected final int height;

  /**
   * Creates a new, empty LayeredImageV0.
   *
   * @param width  The width of the new LayeredImage
   * @param height The height of the new LayeredImage
   * @throws IllegalArgumentException If the width or height given is 0 or below
   */
  public LayeredImageV0(int width, int height) throws IllegalArgumentException {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Height or width is equal to or below 0");
    }
    this.layers = new HashMap<String, LayerData>();
    this.width = width;
    this.height = height;
  }

  /**
   * Creates a new LayeredImageV0 by loading a LayeredImage file of the given name.
   *
   * @param fileName The name of the file to be loaded
   * @throws IllegalArgumentException If fileName is null, or if it references an invalid file
   */
  public LayeredImageV0(String fileName) throws IllegalArgumentException {
    this.layers = new HashMap<String, LayerData>();
    if (fileName == null) {
      throw new IllegalArgumentException("Null fileName");
    }
    Scanner sc;

    File toRead = new File(fileName);
    Path absolutePath = Paths.get(toRead.getAbsolutePath());
    Path basePath = Paths.get(System.getProperty("user.dir"));
    Path relativePath = basePath.relativize(absolutePath);

    try {
      sc = new Scanner(new FileInputStream(relativePath + "/layerdata.txt"));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found");
    }

    this.width = sc.nextInt();
    this.height = sc.nextInt();

    while (sc.hasNext()) {
      String key = sc.next();
      int position = sc.nextInt();
      boolean visible = sc.nextBoolean();

      LayerData toAdd = new LayerData(
          ImageToGraphConverter.convertComplexImage(relativePath + "/" + key + ".png"), position,
          visible);
      this.layers.put(key, toAdd);
    }

  }

  /**
   * Asserts that the given layerName is non-null and is contained in this image.
   *
   * @param layerName The layer to be checked.
   * @throws IllegalArgumentException If the layerName given is null or not contained
   */
  private void assertLayerNameExists(String layerName) throws IllegalArgumentException {
    if (layerName == null) {
      throw new IllegalArgumentException("Null layerName");
    }
    if (!this.layers.containsKey(layerName)) {
      throw new IllegalArgumentException("Invalid layerName");
    }
  }

  /**
   * Asserts that the given layerName is non-null and is not contained in this image.
   *
   * @param layerName The layer to be checked.
   * @throws IllegalArgumentException If the layerName given is null or contained
   */
  private void assertLayerNameDoesntExist(String layerName) throws IllegalArgumentException {
    if (layerName == null) {
      throw new IllegalArgumentException("Null layerName");
    }
    if (this.layers.containsKey(layerName)) {
      throw new IllegalArgumentException("Invalid layerName");
    }
  }

  @Override
  public void addLayer(String layerName) throws IllegalArgumentException {
    this.assertLayerNameDoesntExist(layerName);
    if (layerName.split("\\s+").length != 1) {
      throw new IllegalArgumentException("A layerName cannot be more than one word");
    }
    LayerData newData = new LayerData(ImageToGraphConverter.createTransparentGraph(
        this.width, this.height), 0);
    for (LayerData info : this.layers.values()) {
      info.setPos(info.getPos() + 1);
    }
    this.layers.put(layerName, newData);
  }

  @Override
  public void addLayer(String layerName, String toCopy) throws IllegalArgumentException {
    this.assertLayerNameDoesntExist(layerName);
    if (layerName.split("\\s+").length != 1) {
      throw new IllegalArgumentException("A layerName cannot be more than one word");
    }
    this.assertLayerNameExists(toCopy);
    LayerData newData = new LayerData(ImageToGraphConverter.createCopyOfGraph(
        this.layers.get(toCopy).getImage()), 0);
    if (!this.layers.get(toCopy).getVisibility()) {
      newData.setVisibility(false);
    }
    for (LayerData info : this.layers.values()) {
      info.setPos(info.getPos() + 1);
    }
    this.layers.put(layerName, newData);
  }

  @Override
  public void moveLayer(String layerName, int toIndex) throws IllegalArgumentException {
    this.assertLayerNameExists(layerName);
    if (toIndex >= this.layers.size() || toIndex < 0) {
      throw new IllegalArgumentException("Index is out of range");
    }
    int oldIndex = this.layers.get(layerName).getPos();
    if (oldIndex == toIndex) {
      return;
    }
    boolean isDown = oldIndex - toIndex < 0;
    for (LayerData info : this.layers.values()) {
      if (isDown) {
        if (info.getPos() > oldIndex && info.getPos() <= toIndex) {
          info.setPos(info.getPos() - 1);
        }
      } else {
        if (info.getPos() >= toIndex && info.getPos() < oldIndex) {
          info.setPos(info.getPos() + 1);
        }
      }
    }
    this.layers.get(layerName).setPos(toIndex);
  }

  @Override
  public void setVisibility(String layerName, boolean isVisible) throws IllegalArgumentException {
    this.assertLayerNameExists(layerName);
    this.layers.get(layerName).setVisibility(isVisible);
  }

  @Override
  public boolean getVisibility(String layerName) throws IllegalArgumentException {
    this.assertLayerNameExists(layerName);
    return this.layers.get(layerName).getVisibility();
  }

  @Override
  public boolean getVisibility(int layerIndex) throws IllegalArgumentException {
    if (layerIndex >= this.layers.size() || layerIndex < 0) {
      throw new IllegalArgumentException("Index is out of range");
    }

    for (LayerData info : this.layers.values()) {
      if (info.getPos() == layerIndex) {
        return info.getVisibility();
      }
    }
    throw new IllegalArgumentException("Invalid index");
  }

  @Override
  public int getNumLayers() {
    return this.layers.size();
  }

  @Override
  public List<String> getLayerNames() {
    String[] toReturn = new String[this.layers.size()];
    for (LayerData info : this.layers.values()) {
      toReturn[info.getPos()] = this.getLayerNameAtPos(info.getPos());
    }
    return new ArrayList<String>(Arrays.asList(toReturn));
  }

  /**
   * Gets the layer name of the layer in this LayeredImage at given index.
   *
   * @param index The index of the layer to get the name from
   * @return The target layer name
   * @throws IllegalArgumentException If index is invalid
   */
  private String getLayerNameAtPos(int index) throws IllegalArgumentException {
    if (index < 0 || index > this.layers.size()) {
      throw new IllegalArgumentException("Index to get layer name does not exist");
    }
    String layerName = null;
    for (String layer : this.layers.keySet()) {
      if (this.layers.get(layer).getPos() == index) {
        layerName = layer;
      }
    }
    return layerName;
  }

  @Override
  public void removeLayer(String layerName) throws IllegalArgumentException {
    this.assertLayerNameExists(layerName);
    int removedIndex = this.layers.get(layerName).getPos();
    this.layers.remove(layerName);
    for (LayerData info : this.layers.values()) {
      if (info.getPos() > removedIndex) {
        info.setPos(info.getPos() - 1);
      }
    }
  }

  @Override
  public void loadImageAsLayer(String layerName, String fileName) throws IllegalArgumentException {
    this.assertLayerNameDoesntExist(layerName);
    if (layerName.split("\\s+").length != 1) {
      throw new IllegalArgumentException("A layerName cannot be more than one word");
    }
    if (fileName == null) {
      throw new IllegalArgumentException("Null file name");
    }
    for (LayerData info : this.layers.values()) {
      info.setPos(info.getPos() + 1);
    }
    GraphOfPixels newImage = ImageToGraphConverter.convertImage(fileName);
    this.layers.put(layerName, new LayerData(newImage, 0));
  }

  @Override
  public FixedSizeGraph getLayer(String layerName) throws IllegalArgumentException {
    this.assertLayerNameExists(layerName);
    return this.layers.get(layerName).getImage();
  }

  @Override
  public FixedSizeGraph getLayer(int layerIndex) throws IllegalArgumentException {
    if (layerIndex >= this.layers.size() || layerIndex < 0) {
      throw new IllegalArgumentException("Index is out of range");
    }

    for (LayerData info : this.layers.values()) {
      if (info.getPos() == layerIndex) {
        return info.getImage();
      }
    }
    throw new IllegalArgumentException("Invalid index");
  }

  @Override
  public void saveAsImage(Blend blendType, OutputType outputType, String fileName)
      throws IllegalArgumentException {
    if (blendType == null || outputType == null || fileName == null) {
      throw new IllegalArgumentException("Null input");
    }
    if (this.getNumLayers() == 0) {
      throw new IllegalArgumentException("Not enough layers to save this as an image");
    }
    FixedSizeGraph toReturn = blendType.blend(this);
    toReturn.writeToFile(outputType, fileName);
  }

  @Override
  public void saveAsLayeredImage(String fileName) throws IllegalArgumentException {
    if (fileName == null) {
      throw new IllegalArgumentException("Null input.");
    }
    try {
      Files.createDirectories(Paths.get(fileName));
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not create directory.");
    }

    for (String key : this.layers.keySet()) {
      this.layers.get(key).getImage().writeToFile(OutputType.png, fileName + "/" + key);
    }

    File output = new File(fileName + "/layerdata.txt");
    FileWriter writer;
    PrintWriter printer = null;
    try {
      writer = new FileWriter(output);
      printer = new PrintWriter(writer);
      printer.append(this.width + " " + this.height + "\n");
      for (String key : this.layers.keySet()) {
        LayerData info = this.layers.get(key);
        printer.append(key + " " + info.getPos() + " " + info.getVisibility() + "\n");
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
  public BufferedImage getImageRepresentation() {
    FixedSizeGraph basicBlended = new BasicBlend().blend(this);
    BufferedImage toReturn = new BufferedImage(basicBlended.getWidth(), basicBlended.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    int col = 0;
    int row = 0;
    for (Node n : basicBlended) {
      int rgb = (n.getOpacity() << 24 | n.getRed() << 16 | n.getGreen() << 8 | n.getBlue());
      toReturn.setRGB(col, row, rgb);
      col += 1;
      if (col == basicBlended.getWidth()) {
        col = 0;
        row += 1;
      }
    }
    return toReturn;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public Iterator<FixedSizeGraph> iterator() {
    return new LayeredImageIterator(this.layers);
  }

  /**
   * Represents an iterator that iterates through all the layers of a LayeredImage, in their order
   * as described by their positions.
   */
  public static class LayeredImageIterator implements Iterator<FixedSizeGraph> {

    private final List<FixedSizeGraph> layers;

    /**
     * Creates a new LayeredImageIterator over the layers provided.
     *
     * @param layers The layers to be iterated over
     */
    public LayeredImageIterator(Map<String, LayerData> layers) {
      if (layers == null) {
        throw new IllegalArgumentException("Null layers");
      }
      FixedSizeGraph[] layerArray = new FixedSizeGraph[layers.size()];
      for (LayerData info : layers.values()) {
        layerArray[info.getPos()] = info.getImage();
      }
      this.layers = new LinkedList<FixedSizeGraph>(Arrays.asList(layerArray));
    }

    @Override
    public boolean hasNext() {
      return this.layers.size() > 0;
    }

    @Override
    public FixedSizeGraph next() {
      if (!this.hasNext()) {
        throw new IllegalArgumentException("No more items");
      }
      return layers.remove(0);
    }
  }

  /**
   * Stores information about a layer - it's actual image, it's position in a layered image, and
   * whether or not it is visible.
   */
  static class LayerData {

    private final FixedSizeGraph image;
    private int pos;
    private boolean visibility;

    /**
     * Constructs a new LayerData about the given image, at the given position, that is initially
     * visible.
     *
     * @param image The image this LayerData represents
     * @param pos   The position of that image in a Layered Image
     * @throws IllegalArgumentException If the given image is null
     */
    LayerData(FixedSizeGraph image, int pos) throws IllegalArgumentException {
      if (image == null) {
        throw new IllegalArgumentException("Null image");
      }
      this.image = image;
      this.pos = pos;
      this.visibility = true;
    }


    /**
     * Constructs a new LayerData about the given image, at the given position, with the given
     * visibility.
     *
     * @param image      The image this LayerData represents
     * @param pos        The position of that image in a Layered Image
     * @param visibility The visibility of the image
     * @throws IllegalArgumentException If the given image is null
     */
    LayerData(FixedSizeGraph image, int pos, boolean visibility) throws IllegalArgumentException {
      if (image == null) {
        throw new IllegalArgumentException("Null image");
      }
      this.image = image;
      this.pos = pos;
      this.visibility = visibility;
    }

    /**
     * Returns a reference to the image of this layer.
     *
     * @return The image of this layer
     */
    FixedSizeGraph getImage() {
      return this.image;
    }

    /**
     * Returns the position of this layer in it's LayeredImage.
     *
     * @return The position of this layer
     */
    int getPos() {
      return this.pos;
    }

    /**
     * Returns whether or not this is a visible layer.
     *
     * @return The visibility of this layer
     */
    boolean getVisibility() {
      return this.visibility;
    }

    /**
     * Sets the position of this image to the given one, that given position should be valid for
     * it's layered image.
     *
     * @param newPos The new position
     */
    void setPos(int newPos) {
      this.pos = newPos;
    }

    /**
     * Sets the visibility of this image to the given boolean.
     *
     * @param newVisibility The new visibility for this image.
     */
    void setVisibility(boolean newVisibility) {
      this.visibility = newVisibility;
    }


  }
}

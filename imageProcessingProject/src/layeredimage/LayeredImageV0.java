package layeredimage;

import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.OutputType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import layeredimage.blend.Blend;

/**
 * An implementation of a Layered Image that represents multiple images on multiple layers of a
 * structure that can be manipulated or processed individually.
 */
public class LayeredImageV0 implements LayeredImage {

  protected final HashMap<String, LayerData> layers;
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

    try {
      sc = new Scanner(new FileInputStream(fileName + "/layerdata.txt"));
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
          ImageToGraphConverter.convertComplexImage(fileName + "/" + key + ".png"), position,
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
}

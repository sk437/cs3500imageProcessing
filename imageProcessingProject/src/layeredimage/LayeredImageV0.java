package layeredimage;

import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.OutputType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import layeredimage.blend.Blend;

/**
 * An implementation of a Layered Image ADD DETAILS LATER
 */
public class LayeredImageV0 implements LayeredImage {

  protected final HashMap<String, LayerData> layers;
  protected final int width;
  protected final int height;

  /**
   * Creates a new, empty LayeredImageV0.
   * @param width The width of the new LayeredImage
   * @param height The height of the new LayeredImage
   * @throws IllegalArgumentException If the width or height given is 0 or below
   */
  public LayeredImageV0(int width, int height) throws IllegalArgumentException{
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Height or width is equal to or below 0");
    }
    this.layers = new HashMap<String, LayerData>();
    this.width = width;
    this.height = height;
  }

  /**
   * Creates a new LayeredImageV0 by loading a LayeredImage file of the given name.
   * @param fileName The name of the file to be loaded
   * @throws IllegalArgumentException If fileName is null, or if it references an invalid file
   */
  public LayeredImageV0(String fileName) throws IllegalArgumentException {
    //TODO READ FILE
    this.layers = null; //TODO FOR COMPILATION REMOVE WHEN IMPLEMENTING
    this.width = -1;
    this.height = -1;
  }

  /**
   * Asserts that the given layerName is non-null and is contained in this image.
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
      }
      else {
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
  public int getNumLayers() {
    return this.layers.size();
  }

  @Override
  public List<String> getLayerNames() {
    ArrayList<String> toReturn = new ArrayList<String>();
    for (String name : this.layers.keySet()) {
      toReturn.add(name);
    }
    return toReturn;
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
    if (fileName == null) {
      throw new IllegalArgumentException("Null file name");
    }
    for (LayerData info : this.layers.values()) {
      info.setPos(info.getPos() + 1);
    }
    GraphOfPixels newImage = ImageToGraphConverter.convertComplexImage(fileName);
    this.layers.put(layerName, new LayerData(newImage, 0));
  }

  @Override
  public FixedSizeGraph getLayer(String layerName) throws IllegalArgumentException {
    this.assertLayerNameExists(layerName);
    return this.layers.get(layerName).getImage();
  }

  @Override
  public void saveAsImage(Blend blendType, OutputType outputType, String fileName)
      throws IllegalArgumentException {

  }

  @Override
  public void saveAsLayeredImage(String fileName) throws IllegalArgumentException {

  }

  @Override
  public Iterator<FixedSizeGraph> iterator() {
    return new LayeredImageIterator(this.layers);
  }
}

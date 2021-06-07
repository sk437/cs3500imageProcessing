package imageAsGraph;

import pixel.PixelAsColors;

/**
 * Represents a node in a graph that contains a pixel.
 */
public class PixelNode implements Node{

    private final Node[] neighbors;
    private final PixelAsColors pixel;

    /**
     * Constructs a new PixelNode, and initializes all of it's neighbors to be empty and it's pixel to hold a refernece
     * to the given pixel.
     */
    public PixelNode(PixelAsColors p) {
        this.pixel = p;
        this.neighbors = new Node[4];
        this.neighbors[0] = new EmptyNode();
        this.neighbors[1] = new EmptyNode();
        this.neighbors[2] = new EmptyNode();
        this.neighbors[3] = new EmptyNode();
    }

    @Override
    public int getBlue() {
        return this.pixel.getBlue();
    }

    @Override
    public int getGreen() {
        return this.pixel.getGreen();
    }

    @Override
    public int getRed() {
        return this.pixel.getRed();
    }

    @Override
    public void updateColors(PixelAsColors newColors) throws IllegalArgumentException {
        if (newColors == null) {
            throw new IllegalArgumentException("Null input");
        }
        this.pixel.setRGB(newColors.getRed(), newColors.getGreen(), newColors.getBlue());
    }

    @Override
    public void editColors(PixelAsColors colorDeltas) throws IllegalArgumentException {
        if (colorDeltas == null) {
            throw new IllegalArgumentException("Null input");
        }
        this.pixel.editRGB(colorDeltas.getRed(), colorDeltas.getGreen(), colorDeltas.getBlue());
    }

    @Override
    public Node getLeft() {
        return this.neighbors[0];
    }

    @Override
    public Node getRight() {
        return this.neighbors[1];
    }

    @Override
    public Node getAbove() {
        return this.neighbors[2];
    }

    @Override
    public Node getBelow() {
        return this.neighbors[3];
    }

    @Override
    public void updateLeft(Node other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
        this.neighbors[0] = other;
    }

    @Override
    public void updateRight(Node other) {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
        this.neighbors[1] = other;
    }

    @Override
    public void updateAbove(Node other) {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
        this.neighbors[2] = other;
    }

    @Override
    public void updateBelow(Node other) {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
        this.neighbors[3] = other;
    }
}

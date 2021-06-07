package imageAsGraph;

import pixel.PixelAsColors;

/**
 * Represents an empty node which does not contain a pixel value, used to represent the edges of a graph. This is used
 * to represent a sort of null node, where a real node does not have a neighbor, without actually using null.
 */
public class EmptyNode implements Node {

    @Override
    public int getBlue() {
        return 0;
    }

    @Override
    public int getGreen() {
        return 0;
    }

    @Override
    public int getRed() {
        return 0;
    }

    // NOTE: These methods do not do anything because this represents an empty node, which does not contain a reference
    // to a pixel and does not store neighbors, to avoid infinite repetition and null references.
    @Override
    public void updateColors(PixelAsColors newColors) throws IllegalArgumentException {
        if (newColors == null) {
            throw new IllegalArgumentException("Null input");
        }
    }

    @Override
    public void editColors(PixelAsColors colorDeltas) throws IllegalArgumentException {
        if (colorDeltas == null) {
            throw new IllegalArgumentException("Null input");
        }
    }

    @Override
    public Node getLeft() {
        return this;
    }

    @Override
    public Node getRight() {
        return this;
    }

    @Override
    public Node getAbove() {
        return this;
    }

    @Override
    public Node getBelow() {
        return this;
    }

    // NOTE: These methods do not do anything because this represents an empty node, which does not contain a reference
    // to a pixel and does not store neighbors, to avoid infinite repetition and null references.
    @Override
    public void updateLeft(Node other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
    }

    @Override
    public void updateRight(Node other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
    }

    @Override
    public void updateAbove(Node other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
    }

    @Override
    public void updateBelow(Node other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
    }
}

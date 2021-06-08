package imageAsGraph;

import pixel.PixelAsColors;

/**
 * Represents an empty node which does not contain a pixel value, used to represent the edges of a graph. This is used
 * to represent a sort of null node, where a real node does not have a neighbor, without actually using null.
 */
public class EmptyNode extends AbstractNode {

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
    void updateLeft(AbstractNode other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
    }

    @Override
    void updateRight(AbstractNode other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
    }

    @Override
    void updateAbove(AbstractNode other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
    }

    @Override
    void updateBelow(AbstractNode other) throws IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("Null input");
        }
    }

    @Override
    AbstractNode getLeftAsUpdatable() {
        return this;
    }

    @Override
    AbstractNode getRightAsUpdatable() {
        return this;
    }

    @Override
    AbstractNode getAboveAsUpdatable() {
        return this;
    }

    @Override
    AbstractNode getBelowAsUpdatable() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof EmptyNode)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return -1; // All Empty Nodes are equal, so they should all have the same hashCode
    }
}
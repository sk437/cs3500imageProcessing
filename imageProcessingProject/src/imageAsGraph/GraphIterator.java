package imageAsGraph;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates over all of the non-empty pixels in a graph object, meant to save time from just using a double for loop
 * and getting each pixel.
 */
class GraphIterator implements Iterator<Node> {
    private Node left;
    private Node top;

    /**
     * Constructs a new graph iterator, starting at the given node.
     * @param topLeft The node to start iterating from
     */
    public GraphIterator(Node topLeft) {
        this.left = topLeft;
        this.top = topLeft;
    }

    @Override
    public boolean hasNext() {
        return !(this.left.equals(new EmptyNode()));
    }

    @Override
    public Node next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException("No more nodes");
        }
        Node toReturn = this.left;
        this.left = this.left.getRight();
        if (this.left.equals(new EmptyNode())) {
            this.top = this.top.getBelow();
            this.left = this.top;
        }
        return toReturn;
    }
}

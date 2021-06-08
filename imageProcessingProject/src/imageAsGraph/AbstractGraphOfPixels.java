package imageAsGraph;

public abstract class AbstractGraphOfPixels implements GraphOfPixels {
    /**
     * If this graph is of size 0, 0, then initializes the first node to be the given one.
     * @param n The node to be the first node, must be non-empty
     */
    abstract void addFirstNode(AbstractNode n) throws IllegalArgumentException;
}

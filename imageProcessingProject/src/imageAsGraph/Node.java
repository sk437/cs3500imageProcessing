package imageAsGraph;

import pixel.PixelAsColors;

/**
 * Represents a particular pixel as a node in a graph of pixels, which can access it's neighbors
 * and which represents a pixel.
 */
public interface Node {
    /**
     * Gets the current blue value of this node.
     *
     * @return the current blue value
     */
    int getBlue();

    /**
     * Gets the current green value of this node.
     *
     * @return the current green value
     */
    int getGreen();

    /**
     * Gets the current red value of this node.
     *
     * @return the current red value
     */
    int getRed();

    /**
     * Updates the RGB values of this node to match the ones in the given PixelAsColors object.
     *
     * @param newColors A PixelAsColors object, which the RGB values of this node will be updated
     *                  to match
     * @throws IllegalArgumentException If given a null input
     */
    void updateColors(PixelAsColors newColors) throws IllegalArgumentException;

    /**
     * Updates the RGB values of this node by changing them by the RGB values of the given PixelAsColors object.
     *
     * @param colorDeltas A PixelAsColors object, which the RGB values of this node will be changed by
     * @throws IllegalArgumentException If given a null input or if any of the provided changes would result in
     *                                  an invalid pixel color
     */
    void editColors(PixelAsColors colorDeltas) throws IllegalArgumentException;

    /**
     * Returns the reference of the node just to the left of this node.
     *
     * @return A reference to the node just to the left of this one
     */
    Node getLeft();

    /**
     * Returns the reference of the node just to the right of this node.
     *
     * @return A reference to the node just to the right of this one
     */
    Node getRight();

    /**
     * Returns the reference of the node just above this node.
     *
     * @return A reference to the node just above this one
     */
    Node getAbove();

    /**
     * Returns the reference of the node just below this node.
     *
     * @return A reference to the node just below this one
     */
    Node getBelow();

    /**
     * Updates this node to consider the given node as the one to it's left.
     *
     * @param other a reference to the node which will be considered to the left of this one
     * @throws IllegalArgumentException If given a null input
     */
    void updateLeft(Node other) throws IllegalArgumentException;

    /**
     * Updates this node to consider the given node as the one to it's right.
     *
     * @param other a reference to the node which will be considered to the right of this one
     * @throws IllegalArgumentException If given a null input
     */
    void updateRight(Node other) throws IllegalArgumentException;

    /**
     * Updates this node to consider the given node as the one above it.
     *
     * @param other a reference to the node which will be considered above this one
     * @throws IllegalArgumentException If given a null input
     */
    void updateAbove(Node other) throws IllegalArgumentException;

    /**
     * Updates this node to consider the given node as the one below it.
     *
     * @param other a reference to the node which will be considered below this one
     * @throws IllegalArgumentException If given a null input
     */
    void updateBelow(Node other) throws IllegalArgumentException;
}

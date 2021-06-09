package pixel;

import java.util.Objects;

/**
 * A simple representation of pixel colors, which stores a red, blue, and green value.
 */
public class SimplePixel implements PixelAsColors {
    private int r;
    private int g;
    private int b;

    /**
     * Determines if the given value is a valid number for a color component
     *
     * @param c The color component to be tested
     * @return Whether or not it is a valid component
     */
    private int clamp(int c) {
        if (c < minColor) {
            return minColor;
        }
        else if (c > maxColor) {
            return maxColor;
        }
        return c;
    }

    /**
     * Constructs a new SimplePixel, with the given values as it's corresponding color components.
     *
     * @param red   The red component
     * @param green The green component
     * @param blue  The blue component
     */
    public SimplePixel(int red, int green, int blue) {
        this.r = this.clamp(red);
        this.g = this.clamp(green);
        this.b = this.clamp(blue);
    }

    /**
     * Constructs a copy of the given pixel.
     * @param pixel The pixel whose RGB values are to be copied.
     * @throws IllegalArgumentException If the given pixel is null
     */
    public SimplePixel(PixelAsColors pixel) throws IllegalArgumentException {
        if (pixel == null) {
            throw new IllegalArgumentException("Null pixel");
        }
        this.r = pixel.getRed();
        this.g = pixel.getGreen();
        this.b = pixel.getBlue();
    }

    @Override
    public int getBlue() {
        return b;
    }

    @Override
    public int getGreen() {
        return g;
    }

    @Override
    public int getRed() {
        return r;
    }

    @Override
    public void editBlue(int delta) {
        this.b = this.clamp(b + delta);
    }

    @Override
    public void editGreen(int delta) {
        this.g = this.clamp(g + delta);
    }

    @Override
    public void editRed(int delta) {
        this.r = this.clamp(r + delta);
    }

    @Override
    public void setBlue(int color) {
        this.b = this.clamp(color);
    }

    @Override
    public void setGreen(int color) throws IllegalArgumentException {
        this.g = this.clamp(color);
    }

    @Override
    public void setRed(int color) throws IllegalArgumentException {
        this.r = this.clamp(color);
    }

    @Override
    public void setRGB(int r, int g, int b) {
        this.setRed(r);
        this.setGreen(g);
        this.setBlue(b);
    }

    @Override
    public void editRGB(int deltaR, int deltaG, int deltaB) {
        this.editRed(deltaR);
        this.editGreen(deltaG);
        this.editBlue(deltaB);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimplePixel)) {
            return false;
        }
        SimplePixel that = (SimplePixel) o;
        return r == that.r && g == that.g && b == that.b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b);
    }
}

package pixel;

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
    private boolean invalidColor(int c) {
        if (c < minColor || c > maxColor) {
            return true;
        }
        return false;
    }

    /**
     * Constructs a new SimplePixel, with the given values as it's corresponding color components.
     *
     * @param red   The red component
     * @param green The green component
     * @param blue  The blue component
     */
    public SimplePixel(int red, int green, int blue) {
        if (this.invalidColor(red)) {
            throw new IllegalArgumentException("Invalid red value");
        }
        if (this.invalidColor(green)) {
            throw new IllegalArgumentException("Invalid green value");
        }
        if (this.invalidColor(blue)) {
            throw new IllegalArgumentException("Invalid blue color");
        }
        this.r = red;
        this.g = green;
        this.b = blue;
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
    public void editBlue(int delta) throws IllegalArgumentException {
        if (this.invalidColor(this.b + delta)) {
            throw new IllegalArgumentException("This change to this pixel's blue value would cause it to become invalid");
        }
        this.b += delta;
    }

    @Override
    public void editGreen(int delta) throws IllegalArgumentException {
        if (this.invalidColor(this.g + delta)) {
            throw new IllegalArgumentException("This change to this pixel's green value would cause it to become invalid");
        }
        this.g += delta;
    }

    @Override
    public void editRed(int delta) throws IllegalArgumentException {
        if (this.invalidColor(this.r + delta)) {
            throw new IllegalArgumentException("This change to this pixel's red value would cause it to become invalid");
        }
        this.r += delta;
    }

    @Override
    public void setBlue(int color) throws IllegalArgumentException {
        if (this.invalidColor(color)) {
            throw new IllegalArgumentException("Changing this pixel's blue valid to this would cause it to become invalid");
        }
        this.b = color;
    }

    @Override
    public void setGreen(int color) throws IllegalArgumentException {
        if (this.invalidColor(color)) {
            throw new IllegalArgumentException("Changing this pixel's green valid to this would cause it to become invalid");
        }
        this.g = color;
    }

    @Override
    public void setRed(int color) throws IllegalArgumentException {
        if (this.invalidColor(color)) {
            throw new IllegalArgumentException("Changing this pixel's red valid to this would cause it to become invalid");
        }
        this.r = color;
    }

    @Override
    public void setRGB(int r, int g, int b) throws IllegalArgumentException {
        this.setRed(r);
        this.setGreen(g);
        this.setBlue(b);
    }

    @Override
    public void editRGB(int deltaR, int deltaG, int deltaB) throws IllegalArgumentException {
        this.editRed(deltaR);
        this.editGreen(deltaG);
        this.editBlue(deltaB);
    }
}

package wtf.evolution.helpers;

public class BestColor {

    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public BestColor(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public BestColor(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public BestColor(int color) {
        this((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getRGB() {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

}

package fun.rich.client.utils.render;

import java.awt.*;

public class Colors {

    private Color outputColor;

    public Colors(int rgb) {
        outputColor = new Color(rgb);
    }

    public Colors(Color color) {
        outputColor = new Color(color.getRGB());
    }

    public Colors(String hex) {
        outputColor = Color.decode(hex);
    }

    public Colors setAlpha(int alpha) {
        int r = outputColor.getRed();
        int g = outputColor.getGreen();
        int b = outputColor.getBlue();

        outputColor = new Color(r, g, b, alpha);

        return this;
    }

    public Colors setRed(int red) {
        int g = outputColor.getGreen();
        int b = outputColor.getBlue();
        int a = outputColor.getAlpha();

        outputColor = new Color(red, g, b, a);

        return this;
    }

    public Colors setGreen(int green) {
        int r = outputColor.getRed();
        int b = outputColor.getBlue();
        int a = outputColor.getAlpha();

        outputColor = new Color(r, green, b, a);

        return this;
    }

    public Colors setBlue(int blue) {
        int r = outputColor.getRed();
        int g = outputColor.getGreen();
        int a = outputColor.getAlpha();

        outputColor = new Color(r, g, blue, a);

        return this;
    }

    public Color getColor() {
        return outputColor;
    }
}
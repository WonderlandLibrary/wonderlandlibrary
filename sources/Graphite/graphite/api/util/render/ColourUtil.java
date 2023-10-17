package graphite.api.util.render;

public class ColourUtil {
    public static int overwriteAlpha(final int colour, final int alphaComponent) {
        final int red = colour >> 16 & 0xFF;
        final int green = colour >> 8 & 0xFF;
        final int blue = colour & 0xFF;

        return ((alphaComponent & 0xFF) << 24) |
                ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8) |
                (blue & 0xFF);
    }

    public static int darker(final int colour, final double factor) {
        final int r = (int) ((colour >> 16 & 0xFF) * factor);
        final int g = (int) ((colour >> 8 & 0xFF) * factor);
        final int b = (int) ((colour & 0xFF) * factor);
        final int a = colour >> 24 & 0xFF;

        return ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF) |
                ((a & 0xFF) << 24);
    }
}

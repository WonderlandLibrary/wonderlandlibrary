package io.github.nevalackin.client.ui.cfont;

import io.github.nevalackin.client.util.render.DrawUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class MipMappedFontRenderer implements CustomFontRenderer {

    static final int MARGIN = 2;

    private static final int[] COLOR_CODE_COLORS = new int[32];

    static {
        for (int i = 0; i < 32; i++) {
            int thingy = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + thingy;
            int green = (i >> 1 & 1) * 170 + thingy;
            int blue = (i & 1) * 170 + thingy;
            if (i == 6) red += 85;
            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            COLOR_CODE_COLORS[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }

    private final Map<Integer, FontGlyphs> glyphs = new HashMap<>();

    public MipMappedFontRenderer(final FontGlyphs... fontGlyphsArray) {
        Arrays.stream(fontGlyphsArray).forEach(fontGlyphs -> this.glyphs.put(fontGlyphs.getWeight(), fontGlyphs));
    }

    private StaticallySizedImage[] getGlyphs(int weight) {
        return this.glyphs.get(weight).getGlyphs();
    }

    @Override
    public void draw(final CharSequence text, final double x, final double y, final double scale, final int weight, final int colour) {
        final int length;
        if (text == null || (length = text.length()) == 0) return;

        final StaticallySizedImage[] characters = this.getGlyphs(weight);

        double characterOffset = 0;

        // Store alpha mask
        final int alphaMask = (colour >> 24 & 255) << 24;
        // Keep track of prev char (for colour codes)
        char prev = 0;

        // Set colour
        int currentColour = colour;
        DrawUtil.glColour(currentColour);

        for (int i = 0; i < length; i++) {
            final char character = text.charAt(i);
            if (character <= 10 || character >= 256 || character == 127) continue;

            if (prev == '\247') {
                final int index = "0123456789ABCDEFKLMNOR".indexOf(character);

                if (index != -1 && index < 16) {
                    final int textColor = COLOR_CODE_COLORS[index] | alphaMask;

                    if (currentColour != textColor) {
                        DrawUtil.glColour(textColor);
                        currentColour = textColor;
                    }
                }

                prev = character;
                continue;
            }

            if (character != '\247') {
                if (character != 13) {
                    final StaticallySizedImage image = characters[character];
                    final double scaledWidth = image.getWidth() * scale;
                    image.draw(x + characterOffset - MARGIN * scale, y - MARGIN * scale, scaledWidth, image.getHeight() * scale);
                    characterOffset += scaledWidth - (MARGIN * 2) * scale;
                } else {
                    characterOffset += BASE_FONT * scale;
                }
            }

            prev = character;
        }
    }

    @Override
    public double getWidth(final CharSequence text, final double scale, final int weight) {
        final int length;
        if (text == null || (length = text.length()) == 0) return 0;

        double width = 0;
        final StaticallySizedImage[] characters = this.getGlyphs(weight);

        char prev = 0;

        for (int i = 0; i < length; i++) {
            final char character = text.charAt(i);

            if (prev != '\247' && character != '\247' && character > 10 && character != 127 && character < 256)
                width += (character != 13 ? characters[character].getWidth() - MARGIN * 2 : BASE_FONT) * scale;

            prev = character;
        }

        return width;
    }

    @Override
    public double getHeight(final CharSequence text, final double scale, final int weight) {
        final int length;
        if (text == null || (length = text.length()) == 0) return 0;

        double height = 0;

        final StaticallySizedImage[] characters = this.getGlyphs(weight);

        char prev = 0;

        for (int i = 0; i < length; i++) {
            final char character = text.charAt(i);

            if (prev != '\247' && character != '\247' && character != 13 && character > 10 && character != 127 && character < 256)
                height = Math.max(height, (characters[character].getHeight() - MARGIN * 2) * scale);

            prev = character;
        }

        return height;
    }
}

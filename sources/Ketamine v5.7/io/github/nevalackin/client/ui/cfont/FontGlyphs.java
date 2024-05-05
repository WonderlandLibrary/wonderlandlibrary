package io.github.nevalackin.client.ui.cfont;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static io.github.nevalackin.client.ui.cfont.MipMappedFontRenderer.BASE_FONT;
import static io.github.nevalackin.client.ui.cfont.MipMappedFontRenderer.MARGIN;

public final class FontGlyphs {

    private final Font font;
    private final int weight;
    private final StaticallySizedImage[] glyphs = new StaticallySizedImage[256];

    public FontGlyphs(Font font, int weight) {
        this.font = font.deriveFont(BASE_FONT);
        this.weight = weight;
        this.createGlyphs();
    }

    private void createGlyphs() {
        final BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = (Graphics2D) tempImage.getGraphics();
        final FontMetrics fontMetrics = graphics2D.getFontMetrics(this.font);

        for (int i = 11; i < 256; i++) {
            if (i == 127) continue;
            final char character = (char) i;

            final Rectangle2D characterBounds = fontMetrics.getStringBounds(String.valueOf(character), graphics2D);

            final int characterWidth = (int) StrictMath.ceil(characterBounds.getWidth()) + MARGIN * 2;
            final int characterHeight = (int) StrictMath.ceil(characterBounds.getHeight()) + MARGIN * 2;
            if (characterWidth <= 0 || characterHeight <= 0) continue;
            final BufferedImage characterImage = new BufferedImage(
                characterWidth, characterHeight,
                BufferedImage.TYPE_INT_ARGB);

            Graphics2D graphics = (Graphics2D) characterImage.getGraphics();
            graphics.setFont(this.font);
            // Fill background with clear rect
            graphics.setColor(new Color(0, 0, 0, 0));
            graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
            // Draw text in solid white
            graphics.setColor(Color.WHITE);

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            graphics.drawString(String.valueOf(character), MARGIN, MARGIN + fontMetrics.getAscent());

            this.glyphs[i] = new StaticallySizedImage(characterImage, true, 3);
        }
    }

    public Font getFont() {
        return font;
    }

    public int getWeight() {
        return weight;
    }

    public StaticallySizedImage[] getGlyphs() {
        return glyphs;
    }
}

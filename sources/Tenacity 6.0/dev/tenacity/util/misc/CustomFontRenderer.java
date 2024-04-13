package dev.tenacity.util.misc;

import java.awt.*;
import java.io.File;

public class CustomFontRenderer {
    private Font font;

    public CustomFontRenderer() {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/assets/minecraft/Tenacity/Fonts/OpenSans-Bold.ttf"));
            font = font.deriveFont(12f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawString(Graphics g, String text, int x, int y, int color) {
        g.setFont(font);
        g.setColor(new Color(color));
        g.drawString(text, x, y);
    }
}
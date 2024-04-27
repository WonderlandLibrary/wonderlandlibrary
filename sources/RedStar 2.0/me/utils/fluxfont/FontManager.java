package me.utils.fluxfont;

import java.awt.Font;
import java.io.InputStream;
import me.yarukon.font.Yarukon;

public class FontManager {
    public static Font getFont(String name, int size) {
        Font font;
        try {
            InputStream is = FontManager.class.getResourceAsStream("/assets/minecraft/pride/font/" + name);
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            System.out.println("Error loading font " + name);
            font = new Font("arial", 0, size);
        }
        return font;
    }

    static {
        new Yarukon();
    }
}

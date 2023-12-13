package me.yarukon.font;

import java.awt.Font;
import java.io.InputStream;

public class FontUtils {
   public static Font getFont(String fontName, float size) {
      Font font;
      try {
         InputStream is = FontUtils.class.getResourceAsStream("/assets/minecraft/Client/fonts/" + fontName);
         font = Font.createFont(0, is);
         font = font.deriveFont(0, size);
      } catch (Exception var4) {
         System.out.println("Error while loading font " + fontName + " - " + size + "!");
         font = new Font("Arial", 0, (int)size);
      }

      return font;
   }
}

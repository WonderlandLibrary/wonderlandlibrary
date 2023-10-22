package me.theresa.fontRenderer.font.effect;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import me.theresa.fontRenderer.font.Glyph;
import me.theresa.fontRenderer.font.UnicodeFont;

public interface Effect {
   void draw(BufferedImage var1, Graphics2D var2, UnicodeFont var3, Glyph var4);
}

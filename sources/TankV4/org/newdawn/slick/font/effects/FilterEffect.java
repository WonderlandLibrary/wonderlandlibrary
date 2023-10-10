package org.newdawn.slick.font.effects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.Glyph;

public class FilterEffect implements Effect {
   private BufferedImageOp filter;

   public FilterEffect() {
   }

   public FilterEffect(BufferedImageOp var1) {
      this.filter = var1;
   }

   public void draw(BufferedImage var1, Graphics2D var2, UnicodeFont var3, Glyph var4) {
      BufferedImage var5 = EffectUtil.getScratchImage();
      this.filter.filter(var1, var5);
      var1.getGraphics().drawImage(var5, 0, 0, (ImageObserver)null);
   }

   public BufferedImageOp getFilter() {
      return this.filter;
   }

   public void setFilter(BufferedImageOp var1) {
      this.filter = var1;
   }
}

package me.theresa.fontRenderer.font.effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.theresa.fontRenderer.font.Glyph;
import me.theresa.fontRenderer.font.UnicodeFont;
import me.theresa.fontRenderer.font.util.EffectUtil;

public class ColorEffect implements ConfigurableEffect {
   private Color color;

   public ColorEffect() {
      this.color = Color.white;
   }

   public ColorEffect(Color color) {
      this.color = Color.white;
      this.color = color;
   }

   public void draw(BufferedImage image, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph) {
      g.setColor(this.color);
      g.fill(glyph.getShape());
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      if (color == null) {
         throw new IllegalArgumentException("color cannot be null.");
      } else {
         this.color = color;
      }
   }

   public String toString() {
      return "Color";
   }

   public List getValues() {
      List values = new ArrayList();
      values.add(EffectUtil.colorValue("Color", this.color));
      return values;
   }

   public void setValues(List values) {
      Iterator var2 = values.iterator();

      while(var2.hasNext()) {
         Object o = var2.next();
         ConfigurableEffect.Value value = (ConfigurableEffect.Value)o;
         if (value.getName().equals("Color")) {
            this.setColor((Color)value.getObject());
         }
      }

   }
}

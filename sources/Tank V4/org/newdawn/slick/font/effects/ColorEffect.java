package org.newdawn.slick.font.effects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.Glyph;

public class ColorEffect implements ConfigurableEffect {
   private Color color;

   public ColorEffect() {
      this.color = Color.white;
   }

   public ColorEffect(Color var1) {
      this.color = Color.white;
      this.color = var1;
   }

   public void draw(BufferedImage var1, Graphics2D var2, UnicodeFont var3, Glyph var4) {
      var2.setColor(this.color);
      var2.fill(var4.getShape());
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("color cannot be null.");
      } else {
         this.color = var1;
      }
   }

   public String toString() {
      return "Color";
   }

   public List getValues() {
      ArrayList var1 = new ArrayList();
      var1.add(EffectUtil.colorValue("Color", this.color));
      return var1;
   }

   public void setValues(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ConfigurableEffect.Value var3 = (ConfigurableEffect.Value)var2.next();
         if (var3.getName().equals("Color")) {
            this.setColor((Color)var3.getObject());
         }
      }

   }
}

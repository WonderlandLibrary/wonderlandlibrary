package org.newdawn.slick.font.effects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.Glyph;

public class OutlineEffect implements ConfigurableEffect {
   private float width = 2.0F;
   private Color color;
   private int join;
   private Stroke stroke;

   public OutlineEffect() {
      this.color = Color.black;
      this.join = 2;
   }

   public OutlineEffect(int var1, Color var2) {
      this.color = Color.black;
      this.join = 2;
      this.width = (float)var1;
      this.color = var2;
   }

   public void draw(BufferedImage var1, Graphics2D var2, UnicodeFont var3, Glyph var4) {
      var2 = (Graphics2D)var2.create();
      if (this.stroke != null) {
         var2.setStroke(this.stroke);
      } else {
         var2.setStroke(this.getStroke());
      }

      var2.setColor(this.color);
      var2.draw(var4.getShape());
      var2.dispose();
   }

   public float getWidth() {
      return this.width;
   }

   public void setWidth(int var1) {
      this.width = (float)var1;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color var1) {
      this.color = var1;
   }

   public int getJoin() {
      return this.join;
   }

   public Stroke getStroke() {
      return (Stroke)(this.stroke == null ? new BasicStroke(this.width, 2, this.join) : this.stroke);
   }

   public void setStroke(Stroke var1) {
      this.stroke = var1;
   }

   public void setJoin(int var1) {
      this.join = var1;
   }

   public String toString() {
      return "Outline";
   }

   public List getValues() {
      ArrayList var1 = new ArrayList();
      var1.add(EffectUtil.colorValue("Color", this.color));
      var1.add(EffectUtil.floatValue("Width", this.width, 0.1F, 999.0F, "This setting controls the width of the outline. The glyphs will need padding so the outline doesn't get clipped."));
      var1.add(EffectUtil.optionValue("Join", String.valueOf(this.join), new String[][]{{"Bevel", "2"}, {"Miter", "0"}, {"Round", "1"}}, "This setting defines how the corners of the outline are drawn. This is usually only noticeable at large outline widths."));
      return var1;
   }

   public void setValues(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ConfigurableEffect.Value var3 = (ConfigurableEffect.Value)var2.next();
         if (var3.getName().equals("Color")) {
            this.color = (Color)var3.getObject();
         } else if (var3.getName().equals("Width")) {
            this.width = (Float)var3.getObject();
         } else if (var3.getName().equals("Join")) {
            this.join = Integer.parseInt((String)var3.getObject());
         }
      }

   }
}

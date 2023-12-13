package org.newdawn.slick.font.effects;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.Glyph;

public class GradientEffect implements ConfigurableEffect {
   private Color topColor;
   private Color bottomColor;
   private int offset;
   private float scale;
   private boolean cyclic;

   public GradientEffect() {
      this.topColor = Color.cyan;
      this.bottomColor = Color.blue;
      this.offset = 0;
      this.scale = 1.0F;
   }

   public GradientEffect(Color var1, Color var2, float var3) {
      this.topColor = Color.cyan;
      this.bottomColor = Color.blue;
      this.offset = 0;
      this.scale = 1.0F;
      this.topColor = var1;
      this.bottomColor = var2;
      this.scale = var3;
   }

   public void draw(BufferedImage var1, Graphics2D var2, UnicodeFont var3, Glyph var4) {
      int var5 = var3.getAscent();
      float var6 = (float)var5 * this.scale;
      float var7 = (float)(-var4.getYOffset() + var3.getDescent() + this.offset + var5 / 2) - var6 / 2.0F;
      var2.setPaint(new GradientPaint(0.0F, var7, this.topColor, 0.0F, var7 + var6, this.bottomColor, this.cyclic));
      var2.fill(var4.getShape());
   }

   public Color getTopColor() {
      return this.topColor;
   }

   public void setTopColor(Color var1) {
      this.topColor = var1;
   }

   public Color getBottomColor() {
      return this.bottomColor;
   }

   public void setBottomColor(Color var1) {
      this.bottomColor = var1;
   }

   public int getOffset() {
      return this.offset;
   }

   public void setOffset(int var1) {
      this.offset = var1;
   }

   public float getScale() {
      return this.scale;
   }

   public void setScale(float var1) {
      this.scale = var1;
   }

   public boolean isCyclic() {
      return this.cyclic;
   }

   public void setCyclic(boolean var1) {
      this.cyclic = var1;
   }

   public String toString() {
      return "Gradient";
   }

   public List getValues() {
      ArrayList var1 = new ArrayList();
      var1.add(EffectUtil.colorValue("Top color", this.topColor));
      var1.add(EffectUtil.colorValue("Bottom color", this.bottomColor));
      var1.add(EffectUtil.intValue("Offset", this.offset, "This setting allows you to move the gradient up or down. The gradient is normally centered on the glyph."));
      var1.add(EffectUtil.floatValue("Scale", this.scale, 0.0F, 1.0F, "This setting allows you to change the height of the gradient by apercentage. The gradient is normally the height of most glyphs in the font."));
      var1.add(EffectUtil.booleanValue("Cyclic", this.cyclic, "If this setting is checked, the gradient will repeat."));
      return var1;
   }

   public void setValues(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ConfigurableEffect.Value var3 = (ConfigurableEffect.Value)var2.next();
         if (var3.getName().equals("Top color")) {
            this.topColor = (Color)var3.getObject();
         } else if (var3.getName().equals("Bottom color")) {
            this.bottomColor = (Color)var3.getObject();
         } else if (var3.getName().equals("Offset")) {
            this.offset = (Integer)var3.getObject();
         } else if (var3.getName().equals("Scale")) {
            this.scale = (Float)var3.getObject();
         } else if (var3.getName().equals("Cyclic")) {
            this.cyclic = (Boolean)var3.getObject();
         }
      }

   }
}

package me.yarukon.palette;

import cn.hanabi.utils.MathUtils;
import cn.hanabi.utils.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class ColorValue {
   public static final ArrayList colorValues = new ArrayList();
   public String name;
   public float hue;
   public float saturation;
   public float brightness;
   public float alpha;
   public boolean hasAlpha;
   public boolean rainbow;
   public float rainbowSpeed;

   public ColorValue(String name, float hue, float saturation, float brightness, float alpha, boolean hasAlpha, boolean rainbow, float rainbowSpeed) {
      this.name = name;
      this.hue = hue;
      this.saturation = saturation;
      this.brightness = brightness;
      this.alpha = alpha;
      this.hasAlpha = hasAlpha;
      this.rainbow = rainbow;
      this.rainbowSpeed = rainbowSpeed;
      colorValues.add(this);
   }

   public int getColor() {
      return this.getColor(0L, true);
   }

   public int getColor(long timeOffsets, boolean alpha) {
      if (this.rainbow) {
         this.hue = (float)(Math.ceil((double)System.currentTimeMillis() / (15.1 - (double)this.rainbowSpeed) + (double)timeOffsets) % 360.0 / 360.0);
      }

      int color = Color.getHSBColor(this.hue, this.saturation, this.brightness).getRGB();
      return this.hasAlpha ? (alpha ? RenderUtil.reAlpha(color, MathUtils.clampValue(this.alpha, 0.0F, 1.0F)) : color) : color;
   }

   public static ColorValue getColorValueByName(String name) {
      Iterator var1 = colorValues.iterator();

      ColorValue v;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         v = (ColorValue)var1.next();
      } while(!v.name.equals(name));

      return v;
   }
}

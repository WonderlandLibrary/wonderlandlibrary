package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class DisplacementMapBuilder implements Builder {
   private int __set;
   private Effect input;
   private FloatMap mapData;
   private double offsetX;
   private double offsetY;
   private double scaleX;
   private double scaleY;
   private boolean wrap;

   protected DisplacementMapBuilder() {
   }

   public static DisplacementMapBuilder create() {
      return new DisplacementMapBuilder();
   }

   public void applyTo(DisplacementMap var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setInput(this.input);
      }

      if ((var2 & 2) != 0) {
         var1.setMapData(this.mapData);
      }

      if ((var2 & 4) != 0) {
         var1.setOffsetX(this.offsetX);
      }

      if ((var2 & 8) != 0) {
         var1.setOffsetY(this.offsetY);
      }

      if ((var2 & 16) != 0) {
         var1.setScaleX(this.scaleX);
      }

      if ((var2 & 32) != 0) {
         var1.setScaleY(this.scaleY);
      }

      if ((var2 & 64) != 0) {
         var1.setWrap(this.wrap);
      }

   }

   public DisplacementMapBuilder input(Effect var1) {
      this.input = var1;
      this.__set |= 1;
      return this;
   }

   public DisplacementMapBuilder mapData(FloatMap var1) {
      this.mapData = var1;
      this.__set |= 2;
      return this;
   }

   public DisplacementMapBuilder offsetX(double var1) {
      this.offsetX = var1;
      this.__set |= 4;
      return this;
   }

   public DisplacementMapBuilder offsetY(double var1) {
      this.offsetY = var1;
      this.__set |= 8;
      return this;
   }

   public DisplacementMapBuilder scaleX(double var1) {
      this.scaleX = var1;
      this.__set |= 16;
      return this;
   }

   public DisplacementMapBuilder scaleY(double var1) {
      this.scaleY = var1;
      this.__set |= 32;
      return this;
   }

   public DisplacementMapBuilder wrap(boolean var1) {
      this.wrap = var1;
      this.__set |= 64;
      return this;
   }

   public DisplacementMap build() {
      DisplacementMap var1 = new DisplacementMap();
      this.applyTo(var1);
      return var1;
   }
}

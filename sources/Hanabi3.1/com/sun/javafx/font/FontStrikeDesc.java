package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;

public class FontStrikeDesc {
   float[] matrix;
   float size;
   int aaMode;
   private int hash;

   public FontStrikeDesc(float var1, BaseTransform var2, int var3) {
      this.size = var1;
      this.aaMode = var3;
      this.matrix = new float[4];
      this.matrix[0] = (float)var2.getMxx();
      this.matrix[1] = (float)var2.getMxy();
      this.matrix[2] = (float)var2.getMyx();
      this.matrix[3] = (float)var2.getMyy();
   }

   public int hashCode() {
      if (this.hash == 0) {
         this.hash = this.aaMode + Float.floatToIntBits(this.size) + Float.floatToIntBits(this.matrix[0]) + Float.floatToIntBits(this.matrix[1]) + Float.floatToIntBits(this.matrix[2]) + Float.floatToIntBits(this.matrix[3]);
      }

      return this.hash;
   }

   public boolean equals(Object var1) {
      FontStrikeDesc var2 = (FontStrikeDesc)var1;
      return this.aaMode == var2.aaMode && this.matrix[0] == var2.matrix[0] && this.matrix[1] == var2.matrix[1] && this.matrix[2] == var2.matrix[2] && this.matrix[3] == var2.matrix[3] && this.size == var2.size;
   }
}

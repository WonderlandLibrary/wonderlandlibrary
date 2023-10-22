package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;

public abstract class CompoundImage {
   public static final int BORDER_SIZE_DEFAULT = 1;
   protected final int[] uSubdivision;
   protected final int[] u0;
   protected final int[] u1;
   protected final int[] vSubdivision;
   protected final int[] v0;
   protected final int[] v1;
   protected final int uSections;
   protected final int vSections;
   protected final int uBorderSize;
   protected final int vBorderSize;
   protected Image[] tiles;

   public CompoundImage(Image var1, int var2) {
      this(var1, var2, 1);
   }

   public CompoundImage(Image var1, int var2, int var3) {
      if (4 * var3 >= var2) {
         var3 = var2 / 4;
      }

      int var4 = var1.getWidth();
      int var5 = var1.getHeight();
      this.uBorderSize = var4 <= var2 ? 0 : var3;
      this.vBorderSize = var5 <= var2 ? 0 : var3;
      this.uSubdivision = subdivideUVs(var4, var2, this.uBorderSize);
      this.vSubdivision = subdivideUVs(var5, var2, this.vBorderSize);
      this.uSections = this.uSubdivision.length - 1;
      this.vSections = this.vSubdivision.length - 1;
      this.u0 = new int[this.uSections];
      this.u1 = new int[this.uSections];
      this.v0 = new int[this.vSections];
      this.v1 = new int[this.vSections];
      this.tiles = new Image[this.uSections * this.vSections];

      int var6;
      for(var6 = 0; var6 != this.vSections; ++var6) {
         this.v0[var6] = this.vSubdivision[var6] - this.uBorder(var6);
         this.v1[var6] = this.vSubdivision[var6 + 1] + this.dBorder(var6);
      }

      for(var6 = 0; var6 != this.uSections; ++var6) {
         this.u0[var6] = this.uSubdivision[var6] - this.lBorder(var6);
         this.u1[var6] = this.uSubdivision[var6 + 1] + this.rBorder(var6);
      }

      for(var6 = 0; var6 != this.vSections; ++var6) {
         for(int var7 = 0; var7 != this.uSections; ++var7) {
            this.tiles[var6 * this.uSections + var7] = var1.createSubImage(this.u0[var7], this.v0[var6], this.u1[var7] - this.u0[var7], this.v1[var6] - this.v0[var6]);
         }
      }

   }

   private int lBorder(int var1) {
      return var1 > 0 ? this.uBorderSize : 0;
   }

   private int rBorder(int var1) {
      return var1 < this.uSections - 1 ? this.uBorderSize : 0;
   }

   private int uBorder(int var1) {
      return var1 > 0 ? this.vBorderSize : 0;
   }

   private int dBorder(int var1) {
      return var1 < this.vSections - 1 ? this.vBorderSize : 0;
   }

   private static int[] subdivideUVs(int var0, int var1, int var2) {
      int var3 = var1 - var2 * 2;
      int var4 = (var0 - var2 * 2 + var3 - 1) / var3;
      int[] var5 = new int[var4 + 1];
      var5[0] = 0;
      var5[var4] = var0;

      for(int var6 = 1; var6 < var4; ++var6) {
         var5[var6] = var2 + var3 * var6;
      }

      return var5;
   }

   protected abstract Texture getTile(int var1, int var2, ResourceFactory var3);

   public void drawLazy(Graphics var1, Coords var2, float var3, float var4) {
      (new CompoundCoords(this, var2)).draw(var1, this, var3, var4);
   }
}

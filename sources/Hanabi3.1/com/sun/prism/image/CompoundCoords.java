package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;

public class CompoundCoords {
   private int xImg0;
   private int xImg1;
   private int yImg0;
   private int yImg1;
   private Coords[] tileCoords;

   public CompoundCoords(CompoundImage var1, Coords var2) {
      int var3 = find1(fastFloor(var2.u0), var1.uSubdivision);
      int var4 = find2(fastCeil(var2.u1), var1.uSubdivision);
      int var5 = find1(fastFloor(var2.v0), var1.vSubdivision);
      int var6 = find2(fastCeil(var2.v1), var1.vSubdivision);
      if (var3 >= 0 && var4 >= 0 && var5 >= 0 && var6 >= 0) {
         this.xImg0 = var3;
         this.xImg1 = var4;
         this.yImg0 = var5;
         this.yImg1 = var6;
         this.tileCoords = new Coords[(var4 - var3 + 1) * (var6 - var5 + 1)];
         float[] var7 = new float[var4 - var3];
         float[] var8 = new float[var6 - var5];

         int var9;
         for(var9 = var3; var9 < var4; ++var9) {
            var7[var9 - var3] = var2.getX((float)var1.uSubdivision[var9 + 1]);
         }

         for(var9 = var5; var9 < var6; ++var9) {
            var8[var9 - var5] = var2.getY((float)var1.vSubdivision[var9 + 1]);
         }

         var9 = 0;

         for(int var10 = var5; var10 <= var6; ++var10) {
            float var11 = (var10 == var5 ? var2.v0 : (float)var1.vSubdivision[var10]) - (float)var1.v0[var10];
            float var12 = (var10 == var6 ? var2.v1 : (float)var1.vSubdivision[var10 + 1]) - (float)var1.v0[var10];
            float var13 = var10 == var5 ? var2.y0 : var8[var10 - var5 - 1];
            float var14 = var10 == var6 ? var2.y1 : var8[var10 - var5];

            for(int var15 = var3; var15 <= var4; ++var15) {
               Coords var16 = new Coords();
               var16.v0 = var11;
               var16.v1 = var12;
               var16.y0 = var13;
               var16.y1 = var14;
               var16.u0 = (var15 == var3 ? var2.u0 : (float)var1.uSubdivision[var15]) - (float)var1.u0[var15];
               var16.u1 = (var15 == var4 ? var2.u1 : (float)var1.uSubdivision[var15 + 1]) - (float)var1.u0[var15];
               var16.x0 = var15 == var3 ? var2.x0 : var7[var15 - var3 - 1];
               var16.x1 = var15 == var4 ? var2.x1 : var7[var15 - var3];
               this.tileCoords[var9++] = var16;
            }
         }

      }
   }

   public void draw(Graphics var1, CompoundImage var2, float var3, float var4) {
      if (this.tileCoords != null) {
         ResourceFactory var5 = var1.getResourceFactory();
         int var6 = 0;

         for(int var7 = this.yImg0; var7 <= this.yImg1; ++var7) {
            for(int var8 = this.xImg0; var8 <= this.xImg1; ++var8) {
               Texture var9 = var2.getTile(var8, var7, var5);
               this.tileCoords[var6++].draw(var9, var1, var3, var4);
               var9.unlock();
            }
         }

      }
   }

   private static int find1(int var0, int[] var1) {
      for(int var2 = 0; var2 < var1.length - 1; ++var2) {
         if (var1[var2] <= var0 && var0 < var1[var2 + 1]) {
            return var2;
         }
      }

      return -1;
   }

   private static int find2(int var0, int[] var1) {
      for(int var2 = 0; var2 < var1.length - 1; ++var2) {
         if (var1[var2] < var0 && var0 <= var1[var2 + 1]) {
            return var2;
         }
      }

      return -1;
   }

   private static int fastFloor(float var0) {
      int var1 = (int)var0;
      return (float)var1 <= var0 ? var1 : var1 - 1;
   }

   private static int fastCeil(float var0) {
      int var1 = (int)var0;
      return (float)var1 >= var0 ? var1 : var1 + 1;
   }
}

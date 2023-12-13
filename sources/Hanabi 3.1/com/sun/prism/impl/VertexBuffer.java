package com.sun.prism.impl;

import com.sun.javafx.geom.transform.AffineBase;
import com.sun.prism.paint.Color;
import java.util.Arrays;

public final class VertexBuffer {
   protected static final int VERTS_PER_QUAD = 4;
   protected static final int FLOATS_PER_TC = 2;
   protected static final int FLOATS_PER_VC = 3;
   protected static final int FLOATS_PER_VERT = 7;
   protected static final int BYTES_PER_VERT = 4;
   protected static final int VCOFF = 0;
   protected static final int TC1OFF = 3;
   protected static final int TC2OFF = 5;
   protected int capacity;
   protected int index;
   protected byte r;
   protected byte g;
   protected byte b;
   protected byte a;
   protected byte[] colorArray;
   protected float[] coordArray;
   private final BaseContext ownerCtx;

   public VertexBuffer(BaseContext var1, int var2) {
      this.ownerCtx = var1;
      this.capacity = var2 * 4;
      this.index = 0;
      this.colorArray = new byte[this.capacity * 4];
      this.coordArray = new float[this.capacity * 7];
   }

   public final void setPerVertexColor(Color var1, float var2) {
      float var3 = var1.getAlpha() * var2;
      this.r = (byte)((int)(var1.getRed() * var3 * 255.0F));
      this.g = (byte)((int)(var1.getGreen() * var3 * 255.0F));
      this.b = (byte)((int)(var1.getBlue() * var3 * 255.0F));
      this.a = (byte)((int)(var3 * 255.0F));
   }

   public final void setPerVertexColor(float var1) {
      this.r = this.g = this.b = this.a = (byte)((int)(var1 * 255.0F));
   }

   public final void updateVertexColors(int var1) {
      for(int var2 = 0; var2 != var1; ++var2) {
         this.putColor(var2);
      }

   }

   private void putColor(int var1) {
      int var2 = var1 * 4;
      this.colorArray[var2 + 0] = this.r;
      this.colorArray[var2 + 1] = this.g;
      this.colorArray[var2 + 2] = this.b;
      this.colorArray[var2 + 3] = this.a;
   }

   public final void flush() {
      if (this.index > 0) {
         this.ownerCtx.drawQuads(this.coordArray, this.colorArray, this.index);
         this.index = 0;
      }

   }

   public final void rewind() {
      this.index = 0;
   }

   private void grow() {
      this.capacity *= 2;
      this.colorArray = Arrays.copyOf(this.colorArray, this.capacity * 4);
      this.coordArray = Arrays.copyOf(this.coordArray, this.capacity * 7);
   }

   public final void addVert(float var1, float var2) {
      if (this.index == this.capacity) {
         this.grow();
      }

      int var3 = 7 * this.index;
      this.coordArray[var3 + 0] = var1;
      this.coordArray[var3 + 1] = var2;
      this.coordArray[var3 + 2] = 0.0F;
      this.putColor(this.index);
      ++this.index;
   }

   public final void addVert(float var1, float var2, float var3, float var4) {
      if (this.index == this.capacity) {
         this.grow();
      }

      int var5 = 7 * this.index;
      this.coordArray[var5 + 0] = var1;
      this.coordArray[var5 + 1] = var2;
      this.coordArray[var5 + 2] = 0.0F;
      this.coordArray[var5 + 3] = var3;
      this.coordArray[var5 + 4] = var4;
      this.putColor(this.index);
      ++this.index;
   }

   public final void addVert(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (this.index == this.capacity) {
         this.grow();
      }

      int var7 = 7 * this.index;
      this.coordArray[var7 + 0] = var1;
      this.coordArray[var7 + 1] = var2;
      this.coordArray[var7 + 2] = 0.0F;
      this.coordArray[var7 + 3] = var3;
      this.coordArray[var7 + 4] = var4;
      this.coordArray[var7 + 5] = var5;
      this.coordArray[var7 + 6] = var6;
      this.putColor(this.index);
      ++this.index;
   }

   private void addVertNoCheck(float var1, float var2) {
      int var3 = 7 * this.index;
      this.coordArray[var3 + 0] = var1;
      this.coordArray[var3 + 1] = var2;
      this.coordArray[var3 + 2] = 0.0F;
      this.putColor(this.index);
      ++this.index;
   }

   private void addVertNoCheck(float var1, float var2, float var3, float var4) {
      int var5 = 7 * this.index;
      this.coordArray[var5 + 0] = var1;
      this.coordArray[var5 + 1] = var2;
      this.coordArray[var5 + 2] = 0.0F;
      this.coordArray[var5 + 3] = var3;
      this.coordArray[var5 + 4] = var4;
      this.putColor(this.index);
      ++this.index;
   }

   private void addVertNoCheck(float var1, float var2, float var3, float var4, float var5, float var6) {
      int var7 = 7 * this.index;
      this.coordArray[var7 + 0] = var1;
      this.coordArray[var7 + 1] = var2;
      this.coordArray[var7 + 2] = 0.0F;
      this.coordArray[var7 + 3] = var3;
      this.coordArray[var7 + 4] = var4;
      this.coordArray[var7 + 5] = var5;
      this.coordArray[var7 + 6] = var6;
      this.putColor(this.index);
      ++this.index;
   }

   private void ensureCapacityForQuad() {
      if (this.index + 4 > this.capacity) {
         this.ownerCtx.drawQuads(this.coordArray, this.colorArray, this.index);
         this.index = 0;
      }

   }

   public final void addQuad(float var1, float var2, float var3, float var4) {
      this.ensureCapacityForQuad();
      this.addVertNoCheck(var1, var2);
      this.addVertNoCheck(var1, var4);
      this.addVertNoCheck(var3, var2);
      this.addVertNoCheck(var3, var4);
   }

   public final void addQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      this.ensureCapacityForQuad();
      this.addVertNoCheck(var1, var2, var5, var6, var9, var10);
      this.addVertNoCheck(var1, var4, var5, var8, var9, var12);
      this.addVertNoCheck(var3, var2, var7, var6, var11, var10);
      this.addVertNoCheck(var3, var4, var7, var8, var11, var12);
   }

   public final void addMappedQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      this.ensureCapacityForQuad();
      this.addVertNoCheck(var1, var2, var5, var6);
      this.addVertNoCheck(var1, var4, var9, var10);
      this.addVertNoCheck(var3, var2, var7, var8);
      this.addVertNoCheck(var3, var4, var11, var12);
   }

   public final void addMappedQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20) {
      this.ensureCapacityForQuad();
      this.addVertNoCheck(var1, var2, var5, var6, var13, var14);
      this.addVertNoCheck(var1, var4, var9, var10, var17, var18);
      this.addVertNoCheck(var3, var2, var7, var8, var15, var16);
      this.addVertNoCheck(var3, var4, var11, var12, var19, var20);
   }

   public final void addQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, AffineBase var9) {
      this.addQuad(var1, var2, var3, var4, var5, var6, var7, var8);
      if (var9 != null) {
         int var10 = 7 * this.index - 7;
         var9.transform((float[])this.coordArray, var10 + 0, (float[])this.coordArray, var10 + 5, 1);
         var10 -= 7;
         var9.transform((float[])this.coordArray, var10 + 0, (float[])this.coordArray, var10 + 5, 1);
         var10 -= 7;
         var9.transform((float[])this.coordArray, var10 + 0, (float[])this.coordArray, var10 + 5, 1);
         var10 -= 7;
         var9.transform((float[])this.coordArray, var10 + 0, (float[])this.coordArray, var10 + 5, 1);
      }

   }

   public final void addSuperQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, boolean var9) {
      int var10 = this.index;
      if (var10 + 4 > this.capacity) {
         this.ownerCtx.drawQuads(this.coordArray, this.colorArray, var10);
         var10 = this.index = 0;
      }

      int var11 = 7 * var10;
      float[] var12 = this.coordArray;
      float var13 = var9 ? 1.0F : 0.0F;
      float var14 = var9 ? 0.0F : 1.0F;
      var12[var11] = var1;
      ++var11;
      var12[var11] = var2;
      ++var11;
      var12[var11] = 0.0F;
      ++var11;
      var12[var11] = var5;
      ++var11;
      var12[var11] = var6;
      ++var11;
      var12[var11] = var14;
      ++var11;
      var12[var11] = var13;
      ++var11;
      var12[var11] = var1;
      ++var11;
      var12[var11] = var4;
      ++var11;
      var12[var11] = 0.0F;
      ++var11;
      var12[var11] = var5;
      ++var11;
      var12[var11] = var8;
      ++var11;
      var12[var11] = var14;
      ++var11;
      var12[var11] = var13;
      ++var11;
      var12[var11] = var3;
      ++var11;
      var12[var11] = var2;
      ++var11;
      var12[var11] = 0.0F;
      ++var11;
      var12[var11] = var7;
      ++var11;
      var12[var11] = var6;
      ++var11;
      var12[var11] = var14;
      ++var11;
      var12[var11] = var13;
      ++var11;
      var12[var11] = var3;
      ++var11;
      var12[var11] = var4;
      ++var11;
      var12[var11] = 0.0F;
      ++var11;
      var12[var11] = var7;
      ++var11;
      var12[var11] = var8;
      ++var11;
      var12[var11] = var14;
      ++var11;
      var12[var11] = var13;
      ++var11;
      byte[] var15 = this.colorArray;
      byte var16 = this.r;
      byte var17 = this.g;
      byte var18 = this.b;
      byte var19 = this.a;
      int var20 = 4 * var10;
      var15[var20] = var16;
      ++var20;
      var15[var20] = var17;
      ++var20;
      var15[var20] = var18;
      ++var20;
      var15[var20] = var19;
      ++var20;
      var15[var20] = var16;
      ++var20;
      var15[var20] = var17;
      ++var20;
      var15[var20] = var18;
      ++var20;
      var15[var20] = var19;
      ++var20;
      var15[var20] = var16;
      ++var20;
      var15[var20] = var17;
      ++var20;
      var15[var20] = var18;
      ++var20;
      var15[var20] = var19;
      ++var20;
      var15[var20] = var16;
      ++var20;
      var15[var20] = var17;
      ++var20;
      var15[var20] = var18;
      ++var20;
      var15[var20] = var19;
      this.index = var10 + 4;
   }

   public final void addQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      int var9 = this.index;
      if (var9 + 4 > this.capacity) {
         this.ownerCtx.drawQuads(this.coordArray, this.colorArray, var9);
         var9 = this.index = 0;
      }

      int var10 = 7 * var9;
      float[] var11 = this.coordArray;
      var11[var10] = var1;
      ++var10;
      var11[var10] = var2;
      ++var10;
      var11[var10] = 0.0F;
      ++var10;
      var11[var10] = var5;
      ++var10;
      var11[var10] = var6;
      var10 += 3;
      var11[var10] = var1;
      ++var10;
      var11[var10] = var4;
      ++var10;
      var11[var10] = 0.0F;
      ++var10;
      var11[var10] = var5;
      ++var10;
      var11[var10] = var8;
      var10 += 3;
      var11[var10] = var3;
      ++var10;
      var11[var10] = var2;
      ++var10;
      var11[var10] = 0.0F;
      ++var10;
      var11[var10] = var7;
      ++var10;
      var11[var10] = var6;
      var10 += 3;
      var11[var10] = var3;
      ++var10;
      var11[var10] = var4;
      ++var10;
      var11[var10] = 0.0F;
      ++var10;
      var11[var10] = var7;
      ++var10;
      var11[var10] = var8;
      byte[] var12 = this.colorArray;
      byte var13 = this.r;
      byte var14 = this.g;
      byte var15 = this.b;
      byte var16 = this.a;
      int var17 = 4 * var9;
      var12[var17] = var13;
      ++var17;
      var12[var17] = var14;
      ++var17;
      var12[var17] = var15;
      ++var17;
      var12[var17] = var16;
      ++var17;
      var12[var17] = var13;
      ++var17;
      var12[var17] = var14;
      ++var17;
      var12[var17] = var15;
      ++var17;
      var12[var17] = var16;
      ++var17;
      var12[var17] = var13;
      ++var17;
      var12[var17] = var14;
      ++var17;
      var12[var17] = var15;
      ++var17;
      var12[var17] = var16;
      ++var17;
      var12[var17] = var13;
      ++var17;
      var12[var17] = var14;
      ++var17;
      var12[var17] = var15;
      ++var17;
      var12[var17] = var16;
      this.index = var9 + 4;
   }

   public final void addQuadVO(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      int var11 = this.index;
      if (var11 + 4 > this.capacity) {
         this.ownerCtx.drawQuads(this.coordArray, this.colorArray, var11);
         var11 = this.index = 0;
      }

      int var12 = 7 * var11;
      float[] var13 = this.coordArray;
      var13[var12] = var3;
      ++var12;
      var13[var12] = var4;
      ++var12;
      var13[var12] = 0.0F;
      ++var12;
      var13[var12] = var7;
      ++var12;
      var13[var12] = var8;
      var12 += 3;
      var13[var12] = var3;
      ++var12;
      var13[var12] = var6;
      ++var12;
      var13[var12] = 0.0F;
      ++var12;
      var13[var12] = var7;
      ++var12;
      var13[var12] = var10;
      var12 += 3;
      var13[var12] = var5;
      ++var12;
      var13[var12] = var4;
      ++var12;
      var13[var12] = 0.0F;
      ++var12;
      var13[var12] = var9;
      ++var12;
      var13[var12] = var8;
      var12 += 3;
      var13[var12] = var5;
      ++var12;
      var13[var12] = var6;
      ++var12;
      var13[var12] = 0.0F;
      ++var12;
      var13[var12] = var9;
      ++var12;
      var13[var12] = var10;
      byte[] var14 = this.colorArray;
      int var15 = 4 * var11;
      byte var16 = (byte)((int)(var1 * 255.0F));
      byte var17 = (byte)((int)(var2 * 255.0F));
      var14[var15] = var16;
      ++var15;
      var14[var15] = var16;
      ++var15;
      var14[var15] = var16;
      ++var15;
      var14[var15] = var16;
      ++var15;
      var14[var15] = var17;
      ++var15;
      var14[var15] = var17;
      ++var15;
      var14[var15] = var17;
      ++var15;
      var14[var15] = var17;
      ++var15;
      var14[var15] = var16;
      ++var15;
      var14[var15] = var16;
      ++var15;
      var14[var15] = var16;
      ++var15;
      var14[var15] = var16;
      ++var15;
      var14[var15] = var17;
      ++var15;
      var14[var15] = var17;
      ++var15;
      var14[var15] = var17;
      ++var15;
      var14[var15] = var17;
      this.index = var11 + 4;
   }

   public final void addMappedPgram(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, AffineBase var21) {
      this.addMappedPgram(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var18, var17, var20, var19, var20);
      int var22 = 7 * this.index - 7;
      var21.transform((float[])this.coordArray, var22 + 5, (float[])this.coordArray, var22 + 5, 1);
      var22 -= 7;
      var21.transform((float[])this.coordArray, var22 + 5, (float[])this.coordArray, var22 + 5, 1);
      var22 -= 7;
      var21.transform((float[])this.coordArray, var22 + 5, (float[])this.coordArray, var22 + 5, 1);
      var22 -= 7;
      var21.transform((float[])this.coordArray, var22 + 5, (float[])this.coordArray, var22 + 5, 1);
   }

   public final void addMappedPgram(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18) {
      int var19 = this.index;
      if (var19 + 4 > this.capacity) {
         this.ownerCtx.drawQuads(this.coordArray, this.colorArray, var19);
         var19 = this.index = 0;
      }

      int var20 = 7 * var19;
      float[] var21 = this.coordArray;
      var21[var20] = var1;
      ++var20;
      var21[var20] = var2;
      ++var20;
      var21[var20] = 0.0F;
      ++var20;
      var21[var20] = var9;
      ++var20;
      var21[var20] = var10;
      ++var20;
      var21[var20] = var17;
      ++var20;
      var21[var20] = var18;
      ++var20;
      var21[var20] = var5;
      ++var20;
      var21[var20] = var6;
      ++var20;
      var21[var20] = 0.0F;
      ++var20;
      var21[var20] = var13;
      ++var20;
      var21[var20] = var14;
      ++var20;
      var21[var20] = var17;
      ++var20;
      var21[var20] = var18;
      ++var20;
      var21[var20] = var3;
      ++var20;
      var21[var20] = var4;
      ++var20;
      var21[var20] = 0.0F;
      ++var20;
      var21[var20] = var11;
      ++var20;
      var21[var20] = var12;
      ++var20;
      var21[var20] = var17;
      ++var20;
      var21[var20] = var18;
      ++var20;
      var21[var20] = var7;
      ++var20;
      var21[var20] = var8;
      ++var20;
      var21[var20] = 0.0F;
      ++var20;
      var21[var20] = var15;
      ++var20;
      var21[var20] = var16;
      ++var20;
      var21[var20] = var17;
      ++var20;
      var21[var20] = var18;
      byte[] var22 = this.colorArray;
      byte var23 = this.r;
      byte var24 = this.g;
      byte var25 = this.b;
      byte var26 = this.a;
      int var27 = 4 * var19;
      var22[var27] = var23;
      ++var27;
      var22[var27] = var24;
      ++var27;
      var22[var27] = var25;
      ++var27;
      var22[var27] = var26;
      ++var27;
      var22[var27] = var23;
      ++var27;
      var22[var27] = var24;
      ++var27;
      var22[var27] = var25;
      ++var27;
      var22[var27] = var26;
      ++var27;
      var22[var27] = var23;
      ++var27;
      var22[var27] = var24;
      ++var27;
      var22[var27] = var25;
      ++var27;
      var22[var27] = var26;
      ++var27;
      var22[var27] = var23;
      ++var27;
      var22[var27] = var24;
      ++var27;
      var22[var27] = var25;
      ++var27;
      var22[var27] = var26;
      this.index = var19 + 4;
   }

   public final void addMappedPgram(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24) {
      int var25 = this.index;
      if (var25 + 4 > this.capacity) {
         this.ownerCtx.drawQuads(this.coordArray, this.colorArray, var25);
         var25 = this.index = 0;
      }

      int var26 = 7 * var25;
      float[] var27 = this.coordArray;
      var27[var26] = var1;
      ++var26;
      var27[var26] = var2;
      ++var26;
      var27[var26] = 0.0F;
      ++var26;
      var27[var26] = var9;
      ++var26;
      var27[var26] = var10;
      ++var26;
      var27[var26] = var17;
      ++var26;
      var27[var26] = var18;
      ++var26;
      var27[var26] = var5;
      ++var26;
      var27[var26] = var6;
      ++var26;
      var27[var26] = 0.0F;
      ++var26;
      var27[var26] = var13;
      ++var26;
      var27[var26] = var14;
      ++var26;
      var27[var26] = var21;
      ++var26;
      var27[var26] = var22;
      ++var26;
      var27[var26] = var3;
      ++var26;
      var27[var26] = var4;
      ++var26;
      var27[var26] = 0.0F;
      ++var26;
      var27[var26] = var11;
      ++var26;
      var27[var26] = var12;
      ++var26;
      var27[var26] = var19;
      ++var26;
      var27[var26] = var20;
      ++var26;
      var27[var26] = var7;
      ++var26;
      var27[var26] = var8;
      ++var26;
      var27[var26] = 0.0F;
      ++var26;
      var27[var26] = var15;
      ++var26;
      var27[var26] = var16;
      ++var26;
      var27[var26] = var23;
      ++var26;
      var27[var26] = var24;
      byte[] var28 = this.colorArray;
      byte var29 = this.r;
      byte var30 = this.g;
      byte var31 = this.b;
      byte var32 = this.a;
      int var33 = 4 * var25;
      var28[var33] = var29;
      ++var33;
      var28[var33] = var30;
      ++var33;
      var28[var33] = var31;
      ++var33;
      var28[var33] = var32;
      ++var33;
      var28[var33] = var29;
      ++var33;
      var28[var33] = var30;
      ++var33;
      var28[var33] = var31;
      ++var33;
      var28[var33] = var32;
      ++var33;
      var28[var33] = var29;
      ++var33;
      var28[var33] = var30;
      ++var33;
      var28[var33] = var31;
      ++var33;
      var28[var33] = var32;
      ++var33;
      var28[var33] = var29;
      ++var33;
      var28[var33] = var30;
      ++var33;
      var28[var33] = var31;
      ++var33;
      var28[var33] = var32;
      this.index = var25 + 4;
   }
}

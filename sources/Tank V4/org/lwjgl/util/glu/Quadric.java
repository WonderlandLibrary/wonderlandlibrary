package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Quadric {
   protected int drawStyle = 100012;
   protected int orientation = 100020;
   protected boolean textureFlag = false;
   protected int normals = 100000;

   protected void normal3f(float var1, float var2, float var3) {
      float var4 = (float)Math.sqrt((double)(var1 * var1 + var2 * var2 + var3 * var3));
      if (var4 > 1.0E-5F) {
         var1 /= var4;
         var2 /= var4;
         var3 /= var4;
      }

      GL11.glNormal3f(var1, var2, var3);
   }

   public void setDrawStyle(int var1) {
      this.drawStyle = var1;
   }

   public void setNormals(int var1) {
      this.normals = var1;
   }

   public void setOrientation(int var1) {
      this.orientation = var1;
   }

   public void setTextureFlag(boolean var1) {
      this.textureFlag = var1;
   }

   public int getDrawStyle() {
      return this.drawStyle;
   }

   public int getNormals() {
      return this.normals;
   }

   public int getOrientation() {
      return this.orientation;
   }

   public boolean getTextureFlag() {
      return this.textureFlag;
   }

   protected void TXTR_COORD(float var1, float var2) {
      if (this.textureFlag) {
         GL11.glTexCoord2f(var1, var2);
      }

   }

   protected float sin(float var1) {
      return (float)Math.sin((double)var1);
   }

   protected float cos(float var1) {
      return (float)Math.cos((double)var1);
   }
}

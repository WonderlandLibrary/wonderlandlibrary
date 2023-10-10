package org.newdawn.slick.fills;

import org.newdawn.slick.Color;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class GradientFill implements ShapeFill {
   private Vector2f none;
   private Vector2f start;
   private Vector2f end;
   private Color startCol;
   private Color endCol;
   private boolean local;

   public GradientFill(float var1, float var2, Color var3, float var4, float var5, Color var6) {
      this(var1, var2, var3, var4, var5, var6, false);
   }

   public GradientFill(float var1, float var2, Color var3, float var4, float var5, Color var6, boolean var7) {
      this(new Vector2f(var1, var2), var3, new Vector2f(var4, var5), var6, var7);
   }

   public GradientFill(Vector2f var1, Color var2, Vector2f var3, Color var4, boolean var5) {
      this.none = new Vector2f(0.0F, 0.0F);
      this.local = false;
      this.start = new Vector2f(var1);
      this.end = new Vector2f(var3);
      this.startCol = new Color(var2);
      this.endCol = new Color(var4);
      this.local = var5;
   }

   public GradientFill getInvertedCopy() {
      return new GradientFill(this.start, this.endCol, this.end, this.startCol, this.local);
   }

   public void setLocal(boolean var1) {
      this.local = var1;
   }

   public Vector2f getStart() {
      return this.start;
   }

   public Vector2f getEnd() {
      return this.end;
   }

   public Color getStartColor() {
      return this.startCol;
   }

   public Color getEndColor() {
      return this.endCol;
   }

   public void setStart(float var1, float var2) {
      this.setStart(new Vector2f(var1, var2));
   }

   public void setStart(Vector2f var1) {
      this.start = new Vector2f(var1);
   }

   public void setEnd(float var1, float var2) {
      this.setEnd(new Vector2f(var1, var2));
   }

   public void setEnd(Vector2f var1) {
      this.end = new Vector2f(var1);
   }

   public void setStartColor(Color var1) {
      this.startCol = new Color(var1);
   }

   public void setEndColor(Color var1) {
      this.endCol = new Color(var1);
   }

   public Color colorAt(Shape var1, float var2, float var3) {
      return this.local ? this.colorAt(var2 - var1.getCenterX(), var3 - var1.getCenterY()) : this.colorAt(var2, var3);
   }

   public Color colorAt(float var1, float var2) {
      float var3 = this.end.getX() - this.start.getX();
      float var4 = this.end.getY() - this.start.getY();
      float var5 = -var4;
      float var7 = var3 * var3 - var5 * var4;
      if (var7 == 0.0F) {
         return Color.black;
      } else {
         float var8 = var5 * (this.start.getY() - var2) - var3 * (this.start.getX() - var1);
         var8 /= var7;
         float var9 = var3 * (this.start.getY() - var2) - var4 * (this.start.getX() - var1);
         float var10000 = var9 / var7;
         float var10 = var8;
         if (var8 < 0.0F) {
            var10 = 0.0F;
         }

         if (var10 > 1.0F) {
            var10 = 1.0F;
         }

         float var11 = 1.0F - var10;
         Color var12 = new Color(1, 1, 1, 1);
         var12.r = var10 * this.endCol.r + var11 * this.startCol.r;
         var12.b = var10 * this.endCol.b + var11 * this.startCol.b;
         var12.g = var10 * this.endCol.g + var11 * this.startCol.g;
         var12.a = var10 * this.endCol.a + var11 * this.startCol.a;
         return var12;
      }
   }

   public Vector2f getOffsetAt(Shape var1, float var2, float var3) {
      return this.none;
   }
}

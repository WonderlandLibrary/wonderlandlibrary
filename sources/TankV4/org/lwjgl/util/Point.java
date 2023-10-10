package org.lwjgl.util;

import java.io.Serializable;

public final class Point implements ReadablePoint, WritablePoint, Serializable {
   static final long serialVersionUID = 1L;
   private int x;
   private int y;

   public Point() {
   }

   public Point(int var1, int var2) {
      this.setLocation(var1, var2);
   }

   public Point(ReadablePoint var1) {
      this.setLocation(var1);
   }

   public void setLocation(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public void setLocation(ReadablePoint var1) {
      this.x = var1.getX();
      this.y = var1.getY();
   }

   public void setX(int var1) {
      this.x = var1;
   }

   public void setY(int var1) {
      this.y = var1;
   }

   public void translate(int var1, int var2) {
      this.x += var1;
      this.y += var2;
   }

   public void translate(ReadablePoint var1) {
      this.x += var1.getX();
      this.y += var1.getY();
   }

   public void untranslate(ReadablePoint var1) {
      this.x -= var1.getX();
      this.y -= var1.getY();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Point)) {
         return super.equals(var1);
      } else {
         Point var2 = (Point)var1;
         return this.x == var2.x && this.y == var2.y;
      }
   }

   public String toString() {
      return this.getClass().getName() + "[x=" + this.x + ",y=" + this.y + "]";
   }

   public int hashCode() {
      int var1 = this.x + this.y;
      return var1 * (var1 + 1) / 2 + this.x;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public void getLocation(WritablePoint var1) {
      var1.setLocation(this.x, this.y);
   }
}

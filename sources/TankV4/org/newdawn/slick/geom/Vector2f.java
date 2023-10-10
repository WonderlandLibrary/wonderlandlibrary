package org.newdawn.slick.geom;

import org.newdawn.slick.util.FastTrig;

public class Vector2f {
   public float x;
   public float y;

   public strictfp Vector2f() {
   }

   public strictfp Vector2f(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
   }

   public strictfp Vector2f(double var1) {
      this.x = 1.0F;
      this.y = 0.0F;
      this.setTheta(var1);
   }

   public strictfp void setTheta(double var1) {
      if (var1 < -360.0D || var1 > 360.0D) {
         var1 %= 360.0D;
      }

      if (var1 < 0.0D) {
         var1 += 360.0D;
      }

      double var3 = this.getTheta();
      if (var1 < -360.0D || var1 > 360.0D) {
         var3 %= 360.0D;
      }

      if (var1 < 0.0D) {
         var3 += 360.0D;
      }

      float var5 = this.length();
      this.x = var5 * (float)FastTrig.cos(StrictMath.toRadians(var1));
      this.y = var5 * (float)FastTrig.sin(StrictMath.toRadians(var1));
   }

   public strictfp Vector2f add(double var1) {
      this.setTheta(this.getTheta() + var1);
      return this;
   }

   public strictfp Vector2f sub(double var1) {
      this.setTheta(this.getTheta() - var1);
      return this;
   }

   public strictfp double getTheta() {
      double var1 = StrictMath.toDegrees(StrictMath.atan2((double)this.y, (double)this.x));
      if (var1 < -360.0D || var1 > 360.0D) {
         var1 %= 360.0D;
      }

      if (var1 < 0.0D) {
         var1 += 360.0D;
      }

      return var1;
   }

   public strictfp float getX() {
      return this.x;
   }

   public strictfp float getY() {
      return this.y;
   }

   public strictfp Vector2f(Vector2f var1) {
      this(var1.getX(), var1.getY());
   }

   public strictfp Vector2f(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public strictfp void set(Vector2f var1) {
      this.set(var1.getX(), var1.getY());
   }

   public strictfp float dot(Vector2f var1) {
      return this.x * var1.getX() + this.y * var1.getY();
   }

   public strictfp Vector2f set(float var1, float var2) {
      this.x = var1;
      this.y = var2;
      return this;
   }

   public strictfp Vector2f getPerpendicular() {
      return new Vector2f(-this.y, this.x);
   }

   public strictfp Vector2f set(float[] var1) {
      return this.set(var1[0], var1[1]);
   }

   public strictfp Vector2f negate() {
      return new Vector2f(-this.x, -this.y);
   }

   public strictfp Vector2f negateLocal() {
      this.x = -this.x;
      this.y = -this.y;
      return this;
   }

   public strictfp Vector2f add(Vector2f var1) {
      this.x += var1.getX();
      this.y += var1.getY();
      return this;
   }

   public strictfp Vector2f sub(Vector2f var1) {
      this.x -= var1.getX();
      this.y -= var1.getY();
      return this;
   }

   public strictfp Vector2f scale(float var1) {
      this.x *= var1;
      this.y *= var1;
      return this;
   }

   public strictfp Vector2f normalise() {
      float var1 = this.length();
      if (var1 == 0.0F) {
         return this;
      } else {
         this.x /= var1;
         this.y /= var1;
         return this;
      }
   }

   public strictfp Vector2f getNormal() {
      Vector2f var1 = this.copy();
      var1.normalise();
      return var1;
   }

   public strictfp float lengthSquared() {
      return this.x * this.x + this.y * this.y;
   }

   public strictfp float length() {
      return (float)Math.sqrt((double)this.lengthSquared());
   }

   public strictfp void projectOntoUnit(Vector2f var1, Vector2f var2) {
      float var3 = var1.dot(this);
      var2.x = var3 * var1.getX();
      var2.y = var3 * var1.getY();
   }

   public strictfp Vector2f copy() {
      return new Vector2f(this.x, this.y);
   }

   public strictfp String toString() {
      return "[Vector2f " + this.x + "," + this.y + " (" + this.length() + ")]";
   }

   public strictfp float distance(Vector2f var1) {
      return (float)Math.sqrt((double)this.distanceSquared(var1));
   }

   public strictfp float distanceSquared(Vector2f var1) {
      float var2 = var1.getX() - this.getX();
      float var3 = var1.getY() - this.getY();
      return var2 * var2 + var3 * var3;
   }

   public strictfp int hashCode() {
      return 997 * (int)this.x ^ 991 * (int)this.y;
   }

   public strictfp boolean equals(Object var1) {
      if (!(var1 instanceof Vector2f)) {
         return false;
      } else {
         Vector2f var2 = (Vector2f)var1;
         return var2.x == this.x && var2.y == this.y;
      }
   }
}

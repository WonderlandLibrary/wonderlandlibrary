package me.theresa.fontRenderer.font.geom;

import java.io.Serializable;
import me.theresa.fontRenderer.font.log.FastTrig;

public class Vector2f implements Serializable {
   private static final long serialVersionUID = 1339934L;
   public float x;
   public float y;

   public strictfp Vector2f() {
   }

   public strictfp Vector2f(float[] coords) {
      this.x = coords[0];
      this.y = coords[1];
   }

   public strictfp Vector2f(double theta) {
      this.x = 1.0F;
      this.y = 0.0F;
      this.setTheta(theta);
   }

   public strictfp void setTheta(double theta) {
      if (theta < -360.0 || theta > 360.0) {
         theta %= 360.0;
      }

      if (theta < 0.0) {
         theta += 360.0;
      }

      double oldTheta = this.getTheta();
      if (theta < -360.0 || theta > 360.0) {
         oldTheta %= 360.0;
      }

      if (theta < 0.0) {
         oldTheta += 360.0;
      }

      float len = this.length();
      this.x = len * (float)FastTrig.cos(StrictMath.toRadians(theta));
      this.y = len * (float)FastTrig.sin(StrictMath.toRadians(theta));
   }

   public strictfp Vector2f add(double theta) {
      this.setTheta(this.getTheta() + theta);
      return this;
   }

   public strictfp Vector2f sub(double theta) {
      this.setTheta(this.getTheta() - theta);
      return this;
   }

   public strictfp double getTheta() {
      double theta = StrictMath.toDegrees(StrictMath.atan2((double)this.y, (double)this.x));
      if (theta < -360.0 || theta > 360.0) {
         theta %= 360.0;
      }

      if (theta < 0.0) {
         theta += 360.0;
      }

      return theta;
   }

   public strictfp float getX() {
      return this.x;
   }

   public strictfp float getY() {
      return this.y;
   }

   public strictfp Vector2f(Vector2f other) {
      this(other.getX(), other.getY());
   }

   public strictfp Vector2f(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public strictfp void set(Vector2f other) {
      this.set(other.getX(), other.getY());
   }

   public strictfp float dot(Vector2f other) {
      return this.x * other.getX() + this.y * other.getY();
   }

   public strictfp Vector2f set(float x, float y) {
      this.x = x;
      this.y = y;
      return this;
   }

   public strictfp Vector2f getPerpendicular() {
      return new Vector2f(-this.y, this.x);
   }

   public strictfp Vector2f set(float[] pt) {
      return this.set(pt[0], pt[1]);
   }

   public strictfp Vector2f negate() {
      return new Vector2f(-this.x, -this.y);
   }

   public strictfp Vector2f negateLocal() {
      this.x = -this.x;
      this.y = -this.y;
      return this;
   }

   public strictfp Vector2f add(Vector2f v) {
      this.x += v.getX();
      this.y += v.getY();
      return this;
   }

   public strictfp Vector2f sub(Vector2f v) {
      this.x -= v.getX();
      this.y -= v.getY();
      return this;
   }

   public strictfp Vector2f scale(float a) {
      this.x *= a;
      this.y *= a;
      return this;
   }

   public strictfp Vector2f normalise() {
      float l = this.length();
      if (l == 0.0F) {
         return this;
      } else {
         this.x /= l;
         this.y /= l;
         return this;
      }
   }

   public strictfp Vector2f getNormal() {
      Vector2f cp = this.copy();
      cp.normalise();
      return cp;
   }

   public strictfp float lengthSquared() {
      return this.x * this.x + this.y * this.y;
   }

   public strictfp float length() {
      return (float)Math.sqrt((double)this.lengthSquared());
   }

   public strictfp void projectOntoUnit(Vector2f b, Vector2f result) {
      float dp = b.dot(this);
      result.x = dp * b.getX();
      result.y = dp * b.getY();
   }

   public strictfp Vector2f copy() {
      return new Vector2f(this.x, this.y);
   }

   public strictfp String toString() {
      return "[Vector2f " + this.x + "," + this.y + " (" + this.length() + ")]";
   }

   public strictfp float distance(Vector2f other) {
      return (float)Math.sqrt((double)this.distanceSquared(other));
   }

   public strictfp float distanceSquared(Vector2f other) {
      float dx = other.getX() - this.getX();
      float dy = other.getY() - this.getY();
      return dx * dx + dy * dy;
   }

   public strictfp int hashCode() {
      return 997 * (int)this.x ^ 991 * (int)this.y;
   }

   public strictfp boolean equals(Object other) {
      if (!(other instanceof Vector2f)) {
         return false;
      } else {
         Vector2f o = (Vector2f)other;
         return o.x == this.x && o.y == this.y;
      }
   }
}

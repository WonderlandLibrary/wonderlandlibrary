package org.newdawn.slick.geom;

public class Curve extends Shape {
   private Vector2f p1;
   private Vector2f c1;
   private Vector2f c2;
   private Vector2f p2;
   private int segments;

   public Curve(Vector2f var1, Vector2f var2, Vector2f var3, Vector2f var4) {
      this(var1, var2, var3, var4, 20);
   }

   public Curve(Vector2f var1, Vector2f var2, Vector2f var3, Vector2f var4, int var5) {
      this.p1 = new Vector2f(var1);
      this.c1 = new Vector2f(var2);
      this.c2 = new Vector2f(var3);
      this.p2 = new Vector2f(var4);
      this.segments = var5;
      this.pointsDirty = true;
   }

   public Vector2f pointAt(float var1) {
      float var2 = 1.0F - var1;
      float var4 = var2 * var2 * var2;
      float var5 = 3.0F * var2 * var2 * var1;
      float var6 = 3.0F * var2 * var1 * var1;
      float var7 = var1 * var1 * var1;
      float var8 = this.p1.x * var4 + this.c1.x * var5 + this.c2.x * var6 + this.p2.x * var7;
      float var9 = this.p1.y * var4 + this.c1.y * var5 + this.c2.y * var6 + this.p2.y * var7;
      return new Vector2f(var8, var9);
   }

   protected void createPoints() {
      float var1 = 1.0F / (float)this.segments;
      this.points = new float[(this.segments + 1) * 2];

      for(int var2 = 0; var2 < this.segments + 1; ++var2) {
         float var3 = (float)var2 * var1;
         Vector2f var4 = this.pointAt(var3);
         this.points[var2 * 2] = var4.x;
         this.points[var2 * 2 + 1] = var4.y;
      }

   }

   public Shape transform(Transform var1) {
      float[] var2 = new float[8];
      float[] var3 = new float[8];
      var2[0] = this.p1.x;
      var2[1] = this.p1.y;
      var2[2] = this.c1.x;
      var2[3] = this.c1.y;
      var2[4] = this.c2.x;
      var2[5] = this.c2.y;
      var2[6] = this.p2.x;
      var2[7] = this.p2.y;
      var1.transform(var2, 0, var3, 0, 4);
      return new Curve(new Vector2f(var3[0], var3[1]), new Vector2f(var3[2], var3[3]), new Vector2f(var3[4], var3[5]), new Vector2f(var3[6], var3[7]));
   }

   public boolean closed() {
      return false;
   }
}

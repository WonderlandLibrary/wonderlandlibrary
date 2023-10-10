package org.newdawn.slick.geom;

public class Line extends Shape {
   private Vector2f start;
   private Vector2f end;
   private Vector2f vec;
   private float lenSquared;
   private Vector2f loc;
   private Vector2f v;
   private Vector2f v2;
   private Vector2f proj;
   private Vector2f closest;
   private Vector2f other;
   private boolean outerEdge;
   private boolean innerEdge;

   public Line(float var1, float var2, boolean var3, boolean var4) {
      this(0.0F, 0.0F, var1, var2);
   }

   public Line(float var1, float var2) {
      this(var1, var2, true, true);
   }

   public Line(float var1, float var2, float var3, float var4) {
      this(new Vector2f(var1, var2), new Vector2f(var3, var4));
   }

   public Line(float var1, float var2, float var3, float var4, boolean var5) {
      this(new Vector2f(var1, var2), new Vector2f(var1 + var3, var2 + var4));
   }

   public Line(Vector2f var1, Vector2f var2) {
      this.loc = new Vector2f(0.0F, 0.0F);
      this.v = new Vector2f(0.0F, 0.0F);
      this.v2 = new Vector2f(0.0F, 0.0F);
      this.proj = new Vector2f(0.0F, 0.0F);
      this.closest = new Vector2f(0.0F, 0.0F);
      this.other = new Vector2f(0.0F, 0.0F);
      this.outerEdge = true;
      this.innerEdge = true;
      this.set(var1, var2);
   }

   public Vector2f getStart() {
      return this.start;
   }

   public Vector2f getEnd() {
      return this.end;
   }

   public float length() {
      return this.vec.length();
   }

   public float lengthSquared() {
      return this.vec.lengthSquared();
   }

   public void set(Vector2f var1, Vector2f var2) {
      super.pointsDirty = true;
      if (this.start == null) {
         this.start = new Vector2f();
      }

      this.start.set(var1);
      if (this.end == null) {
         this.end = new Vector2f();
      }

      this.end.set(var2);
      this.vec = new Vector2f(var2);
      this.vec.sub(var1);
      this.lenSquared = this.vec.lengthSquared();
   }

   public void set(float var1, float var2, float var3, float var4) {
      super.pointsDirty = true;
      this.start.set(var1, var2);
      this.end.set(var3, var4);
      float var5 = var3 - var1;
      float var6 = var4 - var2;
      this.lenSquared = var5 * var5 + var6 * var6;
   }

   public float getDX() {
      return this.end.getX() - this.start.getX();
   }

   public float getDY() {
      return this.end.getY() - this.start.getY();
   }

   public float getX() {
      return this.getX1();
   }

   public float getY() {
      return this.getY1();
   }

   public float getX1() {
      return this.start.getX();
   }

   public float getY1() {
      return this.start.getY();
   }

   public float getX2() {
      return this.end.getX();
   }

   public float getY2() {
      return this.end.getY();
   }

   public float distance(Vector2f var1) {
      return (float)Math.sqrt((double)this.distanceSquared(var1));
   }

   public boolean on(Vector2f var1) {
      this.getClosestPoint(var1, this.closest);
      return var1.equals(this.closest);
   }

   public float distanceSquared(Vector2f var1) {
      this.getClosestPoint(var1, this.closest);
      this.closest.sub(var1);
      float var2 = this.closest.lengthSquared();
      return var2;
   }

   public void getClosestPoint(Vector2f var1, Vector2f var2) {
      this.loc.set(var1);
      this.loc.sub(this.start);
      float var3 = this.vec.dot(this.loc);
      var3 /= this.vec.lengthSquared();
      if (var3 < 0.0F) {
         var2.set(this.start);
      } else if (var3 > 1.0F) {
         var2.set(this.end);
      } else {
         var2.x = this.start.getX() + var3 * this.vec.getX();
         var2.y = this.start.getY() + var3 * this.vec.getY();
      }
   }

   public String toString() {
      return "[Line " + this.start + "," + this.end + "]";
   }

   public Vector2f intersect(Line var1) {
      return this.intersect(var1, false);
   }

   public Vector2f intersect(Line var1, boolean var2) {
      Vector2f var3 = new Vector2f();
      return var3 == false ? null : var3;
   }

   protected void createPoints() {
      this.points = new float[4];
      this.points[0] = this.getX1();
      this.points[1] = this.getY1();
      this.points[2] = this.getX2();
      this.points[3] = this.getY2();
   }

   public Shape transform(Transform var1) {
      float[] var2 = new float[4];
      this.createPoints();
      var1.transform(this.points, 0, var2, 0, 2);
      return new Line(var2[0], var2[1], var2[2], var2[3]);
   }

   public boolean closed() {
      return false;
   }
}

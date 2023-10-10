package org.newdawn.slick.geom;

public class Rectangle extends Shape {
   protected float width;
   protected float height;

   public Rectangle(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
      this.maxX = var1 + var3;
      this.maxY = var2 + var4;
      this.checkPoints();
   }

   public boolean contains(float var1, float var2) {
      if (var1 <= this.getX()) {
         return false;
      } else if (var2 <= this.getY()) {
         return false;
      } else if (var1 >= this.maxX + 1.0F) {
         return false;
      } else {
         return !(var2 >= this.maxY + 1.0F);
      }
   }

   public void setBounds(Rectangle var1) {
      this.setBounds(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
   }

   public void setBounds(float var1, float var2, float var3, float var4) {
      this.setX(var1);
      this.setY(var2);
      this.setSize(var3, var4);
   }

   public void setSize(float var1, float var2) {
      this.setWidth(var1);
      this.setHeight(var2);
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public void grow(float var1, float var2) {
      this.setX(this.getX() - var1);
      this.setY(this.getY() - var2);
      this.setWidth(this.getWidth() + var1 * 2.0F);
      this.setHeight(this.getHeight() + var2 * 2.0F);
   }

   public void scaleGrow(float var1, float var2) {
      this.grow(this.getWidth() * (var1 - 1.0F), this.getHeight() * (var2 - 1.0F));
   }

   public void setWidth(float var1) {
      if (var1 != this.width) {
         this.pointsDirty = true;
         this.width = var1;
         this.maxX = this.x + var1;
      }

   }

   public void setHeight(float var1) {
      if (var1 != this.height) {
         this.pointsDirty = true;
         this.height = var1;
         this.maxY = this.y + var1;
      }

   }

   public boolean intersects(Shape var1) {
      if (var1 instanceof Rectangle) {
         Rectangle var2 = (Rectangle)var1;
         if (!(this.x > var2.x + var2.width) && !(this.x + this.width < var2.x)) {
            return !(this.y > var2.y + var2.height) && !(this.y + this.height < var2.y);
         } else {
            return false;
         }
      } else {
         return var1 instanceof Circle ? this.intersects((Circle)var1) : super.intersects(var1);
      }
   }

   protected void createPoints() {
      float var1 = this.width - 1.0F;
      float var2 = this.height - 1.0F;
      this.points = new float[8];
      this.points[0] = this.x;
      this.points[1] = this.y;
      this.points[2] = this.x + var1;
      this.points[3] = this.y;
      this.points[4] = this.x + var1;
      this.points[5] = this.y + var2;
      this.points[6] = this.x;
      this.points[7] = this.y + var2;
      this.maxX = this.points[2];
      this.maxY = this.points[5];
      this.minX = this.points[0];
      this.minY = this.points[1];
      this.findCenter();
      this.calculateRadius();
   }

   private boolean intersects(Circle var1) {
      return var1.intersects((Shape)this);
   }

   public String toString() {
      return "[Rectangle " + this.width + "x" + this.height + "]";
   }

   public static boolean contains(float var0, float var1, float var2, float var3, float var4, float var5) {
      return var0 >= var2 && var1 >= var3 && var0 <= var2 + var4 && var1 <= var3 + var5;
   }

   public Shape transform(Transform var1) {
      this.checkPoints();
      Polygon var2 = new Polygon();
      float[] var3 = new float[this.points.length];
      var1.transform(this.points, 0, var3, 0, this.points.length / 2);
      var2.points = var3;
      var2.findCenter();
      var2.checkPoints();
      return var2;
   }
}

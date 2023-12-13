package org.newdawn.slick.geom;

import java.util.ArrayList;

public class MorphShape extends Shape {
   private ArrayList shapes = new ArrayList();
   private float offset;
   private Shape current;
   private Shape next;

   public MorphShape(Shape var1) {
      this.shapes.add(var1);
      float[] var2 = var1.points;
      this.points = new float[var2.length];
      this.current = var1;
      this.next = var1;
   }

   public void addShape(Shape var1) {
      if (var1.points.length != this.points.length) {
         throw new RuntimeException("Attempt to morph between two shapes with different vertex counts");
      } else {
         Shape var2 = (Shape)this.shapes.get(this.shapes.size() - 1);
         if (var2 < var1) {
            this.shapes.add(var2);
         } else {
            this.shapes.add(var1);
         }

         if (this.shapes.size() == 2) {
            this.next = (Shape)this.shapes.get(1);
         }

      }
   }

   public void setMorphTime(float var1) {
      int var2 = (int)var1;
      int var3 = var2 + 1;
      float var4 = var1 - (float)var2;
      var2 = this.rational(var2);
      var3 = this.rational(var3);
      this.setFrame(var2, var3, var4);
   }

   public void updateMorphTime(float var1) {
      this.offset += var1;
      int var2;
      int var3;
      if (this.offset < 0.0F) {
         var2 = this.shapes.indexOf(this.current);
         if (var2 < 0) {
            var2 = this.shapes.size() - 1;
         }

         var3 = this.rational(var2 + 1);
         this.setFrame(var2, var3, this.offset);
         ++this.offset;
      } else if (this.offset > 1.0F) {
         var2 = this.shapes.indexOf(this.next);
         if (var2 < 1) {
            var2 = 0;
         }

         var3 = this.rational(var2 + 1);
         this.setFrame(var2, var3, this.offset);
         --this.offset;
      } else {
         this.pointsDirty = true;
      }

   }

   public void setExternalFrame(Shape var1) {
      this.current = var1;
      this.next = (Shape)this.shapes.get(0);
      this.offset = 0.0F;
   }

   private int rational(int var1) {
      while(var1 >= this.shapes.size()) {
         var1 -= this.shapes.size();
      }

      while(var1 < 0) {
         var1 += this.shapes.size();
      }

      return var1;
   }

   private void setFrame(int var1, int var2, float var3) {
      this.current = (Shape)this.shapes.get(var1);
      this.next = (Shape)this.shapes.get(var2);
      this.offset = var3;
      this.pointsDirty = true;
   }

   protected void createPoints() {
      if (this.current == this.next) {
         System.arraycopy(this.current.points, 0, this.points, 0, this.points.length);
      } else {
         float[] var1 = this.current.points;
         float[] var2 = this.next.points;

         for(int var3 = 0; var3 < this.points.length; ++var3) {
            this.points[var3] = var1[var3] * (1.0F - this.offset);
            float[] var10000 = this.points;
            var10000[var3] += var2[var3] * this.offset;
         }

      }
   }

   public Shape transform(Transform var1) {
      this.createPoints();
      Polygon var2 = new Polygon(this.points);
      return var2;
   }
}

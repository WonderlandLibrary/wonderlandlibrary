package me.theresa.fontRenderer.font.geom;

import java.util.ArrayList;

public class MorphShape extends Shape {
   private final ArrayList shapes = new ArrayList();
   private float offset;
   private Shape current;
   private Shape next;

   public MorphShape(Shape base) {
      this.shapes.add(base);
      float[] copy = base.points;
      this.points = new float[copy.length];
      this.current = base;
      this.next = base;
   }

   public void addShape(Shape shape) {
      if (shape.points.length != this.points.length) {
         throw new RuntimeException("Attempt to morph between two shapes with different vertex counts");
      } else {
         Shape prev = (Shape)this.shapes.get(this.shapes.size() - 1);
         if (this.equalShapes(prev, shape)) {
            this.shapes.add(prev);
         } else {
            this.shapes.add(shape);
         }

         if (this.shapes.size() == 2) {
            this.next = (Shape)this.shapes.get(1);
         }

      }
   }

   private boolean equalShapes(Shape a, Shape b) {
      a.checkPoints();
      b.checkPoints();

      for(int i = 0; i < a.points.length; ++i) {
         if (a.points[i] != b.points[i]) {
            return false;
         }
      }

      return true;
   }

   public void setMorphTime(float time) {
      int p = (int)time;
      int n = p + 1;
      float offset = time - (float)p;
      p = this.rational(p);
      n = this.rational(n);
      this.setFrame(p, n, offset);
   }

   public void updateMorphTime(float delta) {
      this.offset += delta;
      int index;
      int nframe;
      if (this.offset < 0.0F) {
         index = this.shapes.indexOf(this.current);
         if (index < 0) {
            index = this.shapes.size() - 1;
         }

         nframe = this.rational(index + 1);
         this.setFrame(index, nframe, this.offset);
         ++this.offset;
      } else if (this.offset > 1.0F) {
         index = this.shapes.indexOf(this.next);
         if (index < 1) {
            index = 0;
         }

         nframe = this.rational(index + 1);
         this.setFrame(index, nframe, this.offset);
         --this.offset;
      } else {
         this.pointsDirty = true;
      }

   }

   public void setExternalFrame(Shape current) {
      this.current = current;
      this.next = (Shape)this.shapes.get(0);
      this.offset = 0.0F;
   }

   private int rational(int n) {
      while(n >= this.shapes.size()) {
         n -= this.shapes.size();
      }

      while(n < 0) {
         n += this.shapes.size();
      }

      return n;
   }

   private void setFrame(int a, int b, float offset) {
      this.current = (Shape)this.shapes.get(a);
      this.next = (Shape)this.shapes.get(b);
      this.offset = offset;
      this.pointsDirty = true;
   }

   protected void createPoints() {
      if (this.current == this.next) {
         System.arraycopy(this.current.points, 0, this.points, 0, this.points.length);
      } else {
         float[] apoints = this.current.points;
         float[] bpoints = this.next.points;

         for(int i = 0; i < this.points.length; ++i) {
            this.points[i] = apoints[i] * (1.0F - this.offset);
            float[] var10000 = this.points;
            var10000[i] += bpoints[i] * this.offset;
         }

      }
   }

   public Shape transform(Transform transform) {
      this.createPoints();
      return new Polygon(this.points);
   }
}

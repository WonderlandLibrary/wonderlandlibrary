package me.theresa.fontRenderer.font.geom;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MannTriangulator implements Triangulator {
   private static final double EPSILON = 1.0E-5;
   protected PointBag contour = this.getPointBag();
   protected PointBag holes;
   private PointBag nextFreePointBag;
   private Point nextFreePoint;
   private final List triangles = new ArrayList();

   public void addPolyPoint(float x, float y) {
      this.addPoint(new Vector2f(x, y));
   }

   public void reset() {
      while(this.holes != null) {
         this.holes = this.freePointBag(this.holes);
      }

      this.contour.clear();
      this.holes = null;
   }

   public void startHole() {
      PointBag newHole = this.getPointBag();
      newHole.next = this.holes;
      this.holes = newHole;
   }

   private void addPoint(Vector2f pt) {
      Point p;
      if (this.holes == null) {
         p = this.getPoint(pt);
         this.contour.add(p);
      } else {
         p = this.getPoint(pt);
         this.holes.add(p);
      }

   }

   private Vector2f[] triangulate(Vector2f[] result) {
      this.contour.computeAngles();

      for(PointBag hole = this.holes; hole != null; hole = hole.next) {
         hole.computeAngles();
      }

      Point newPtContour;
      Point newPtHole;
      label88:
      for(; this.holes != null; this.holes = this.freePointBag(this.holes)) {
         Point pHole = this.holes.first;

         do {
            if (pHole.angle <= 0.0) {
               Point pContour = this.contour.first;

               do {
                  if (pHole.isInfront(pContour) && pContour.isInfront(pHole) && !this.contour.doesIntersectSegment(pHole.pt, pContour.pt)) {
                     PointBag hole = this.holes;

                     while(!hole.doesIntersectSegment(pHole.pt, pContour.pt)) {
                        if ((hole = hole.next) == null) {
                           newPtContour = this.getPoint(pContour.pt);
                           pContour.insertAfter(newPtContour);
                           newPtHole = this.getPoint(pHole.pt);
                           pHole.insertBefore(newPtHole);
                           pContour.next = pHole;
                           pHole.prev = pContour;
                           newPtHole.next = newPtContour;
                           newPtContour.prev = newPtHole;
                           pContour.computeAngle();
                           pHole.computeAngle();
                           newPtContour.computeAngle();
                           newPtHole.computeAngle();
                           this.holes.first = null;
                           continue label88;
                        }
                     }
                  }
               } while((pContour = pContour.next) != this.contour.first);
            }
         } while((pHole = pHole.next) != this.holes.first);
      }

      int numTriangles = this.contour.countPoints() - 2;
      int neededSpace = numTriangles * 3 + 1;
      if (result.length < neededSpace) {
         result = (Vector2f[])((Vector2f[])Array.newInstance(result.getClass().getComponentType(), neededSpace));
      }

      int idx = 0;

      while(true) {
         newPtContour = this.contour.first;
         if (newPtContour == null || newPtContour.next == newPtContour.prev) {
            result[idx] = null;
            this.contour.clear();
            return result;
         }

         Point next;
         do {
            if (newPtContour.angle > 0.0) {
               newPtHole = newPtContour.prev;
               next = newPtContour.next;
               if ((next.next == newPtHole || newPtHole.isInfront(next) && next.isInfront(newPtHole)) && !this.contour.doesIntersectSegment(newPtHole.pt, next.pt)) {
                  result[idx++] = newPtContour.pt;
                  result[idx++] = next.pt;
                  result[idx++] = newPtHole.pt;
                  break;
               }
            }
         } while((newPtContour = newPtContour.next) != this.contour.first);

         newPtHole = newPtContour.prev;
         next = newPtContour.next;
         this.contour.first = newPtHole;
         newPtContour.unlink();
         this.freePoint(newPtContour);
         next.computeAngle();
         newPtHole.computeAngle();
      }
   }

   private PointBag getPointBag() {
      PointBag pb = this.nextFreePointBag;
      if (pb != null) {
         this.nextFreePointBag = pb.next;
         pb.next = null;
         return pb;
      } else {
         return new PointBag();
      }
   }

   private PointBag freePointBag(PointBag pb) {
      PointBag next = pb.next;
      pb.clear();
      pb.next = this.nextFreePointBag;
      this.nextFreePointBag = pb;
      return next;
   }

   private Point getPoint(Vector2f pt) {
      Point p = this.nextFreePoint;
      if (p != null) {
         this.nextFreePoint = p.next;
         p.next = null;
         p.prev = null;
         p.pt = pt;
         return p;
      } else {
         return new Point(pt);
      }
   }

   private void freePoint(Point p) {
      p.next = this.nextFreePoint;
      this.nextFreePoint = p;
   }

   private void freePoints(Point head) {
      head.prev.next = this.nextFreePoint;
      head.prev = null;
      this.nextFreePoint = head;
   }

   public boolean triangulate() {
      Vector2f[] temp = this.triangulate(new Vector2f[0]);
      Vector2f[] var2 = temp;
      int var3 = temp.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Vector2f vector2f = var2[var4];
         if (vector2f == null) {
            break;
         }

         this.triangles.add(vector2f);
      }

      return true;
   }

   public int getTriangleCount() {
      return this.triangles.size() / 3;
   }

   public float[] getTrianglePoint(int tri, int i) {
      Vector2f pt = (Vector2f)this.triangles.get(tri * 3 + i);
      return new float[]{pt.x, pt.y};
   }

   protected class PointBag implements Serializable {
      protected Point first;
      protected PointBag next;

      public void clear() {
         if (this.first != null) {
            MannTriangulator.this.freePoints(this.first);
            this.first = null;
         }

      }

      public void add(Point p) {
         if (this.first != null) {
            this.first.insertBefore(p);
         } else {
            this.first = p;
            p.next = p;
            p.prev = p;
         }

      }

      public void computeAngles() {
         if (this.first != null) {
            Point p = this.first;

            do {
               p.computeAngle();
            } while((p = p.next) != this.first);

         }
      }

      public boolean doesIntersectSegment(Vector2f v1, Vector2f v2) {
         double dxA = (double)(v2.x - v1.x);
         double dyA = (double)(v2.y - v1.y);
         Point p = this.first;

         while(true) {
            Point n = p.next;
            if (p.pt != v1 && n.pt != v1 && p.pt != v2 && n.pt != v2) {
               double dxB = (double)(n.pt.x - p.pt.x);
               double dyB = (double)(n.pt.y - p.pt.y);
               double d = dxA * dyB - dyA * dxB;
               if (Math.abs(d) > 1.0E-5) {
                  double tmp1 = (double)(p.pt.x - v1.x);
                  double tmp2 = (double)(p.pt.y - v1.y);
                  double tA = (dyB * tmp1 - dxB * tmp2) / d;
                  double tB = (dyA * tmp1 - dxA * tmp2) / d;
                  if (tA >= 0.0 && tA <= 1.0 && tB >= 0.0 && tB <= 1.0) {
                     return true;
                  }
               }
            }

            if (n == this.first) {
               return false;
            }

            p = n;
         }
      }

      public int countPoints() {
         if (this.first == null) {
            return 0;
         } else {
            int count = 0;
            Point p = this.first;

            do {
               ++count;
            } while((p = p.next) != this.first);

            return count;
         }
      }

      public boolean contains(Vector2f point) {
         if (this.first == null) {
            return false;
         } else {
            return this.first.prev.pt.equals(point) ? true : this.first.pt.equals(point);
         }
      }
   }

   private static class Point implements Serializable {
      protected Vector2f pt;
      protected Point prev;
      protected Point next;
      protected double nx;
      protected double ny;
      protected double angle;
      protected double dist;

      public Point(Vector2f pt) {
         this.pt = pt;
      }

      public void unlink() {
         this.prev.next = this.next;
         this.next.prev = this.prev;
         this.next = null;
         this.prev = null;
      }

      public void insertBefore(Point p) {
         this.prev.next = p;
         p.prev = this.prev;
         p.next = this;
         this.prev = p;
      }

      public void insertAfter(Point p) {
         this.next.prev = p;
         p.prev = this;
         p.next = this.next;
         this.next = p;
      }

      private double hypot(double x, double y) {
         return Math.sqrt(x * x + y * y);
      }

      public void computeAngle() {
         Vector2f var10000;
         if (this.prev.pt.equals(this.pt)) {
            var10000 = this.pt;
            var10000.x += 0.01F;
         }

         double dx1 = (double)(this.pt.x - this.prev.pt.x);
         double dy1 = (double)(this.pt.y - this.prev.pt.y);
         double len1 = this.hypot(dx1, dy1);
         dx1 /= len1;
         dy1 /= len1;
         if (this.next.pt.equals(this.pt)) {
            var10000 = this.pt;
            var10000.y += 0.01F;
         }

         double dx2 = (double)(this.next.pt.x - this.pt.x);
         double dy2 = (double)(this.next.pt.y - this.pt.y);
         double len2 = this.hypot(dx2, dy2);
         dx2 /= len2;
         dy2 /= len2;
         double nx1 = -dy1;
         this.nx = (nx1 - dy2) * 0.5;
         this.ny = (dx1 + dx2) * 0.5;
         if (this.nx * this.nx + this.ny * this.ny < 1.0E-5) {
            this.nx = dx1;
            this.ny = dy2;
            this.angle = 1.0;
            if (dx1 * dx2 + dy1 * dy2 > 0.0) {
               this.nx = -dx1;
               this.ny = -dy1;
            }
         } else {
            this.angle = this.nx * dx2 + this.ny * dy2;
         }

      }

      public double getAngle(Point p) {
         double dx = (double)(p.pt.x - this.pt.x);
         double dy = (double)(p.pt.y - this.pt.y);
         double dlen = this.hypot(dx, dy);
         return (this.nx * dx + this.ny * dy) / dlen;
      }

      public boolean isConcave() {
         return this.angle < 0.0;
      }

      public boolean isInfront(double dx, double dy) {
         boolean sidePrev = (double)(this.prev.pt.y - this.pt.y) * dx + (double)(this.pt.x - this.prev.pt.x) * dy >= 0.0;
         boolean sideNext = (double)(this.pt.y - this.next.pt.y) * dx + (double)(this.next.pt.x - this.pt.x) * dy >= 0.0;
         return this.angle < 0.0 ? sidePrev | sideNext : sidePrev & sideNext;
      }

      public boolean isInfront(Point p) {
         return this.isInfront((double)(p.pt.x - this.pt.x), (double)(p.pt.y - this.pt.y));
      }
   }
}

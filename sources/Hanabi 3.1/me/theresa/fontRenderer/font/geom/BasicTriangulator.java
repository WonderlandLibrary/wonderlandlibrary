package me.theresa.fontRenderer.font.geom;

import java.util.ArrayList;

public class BasicTriangulator implements Triangulator {
   private static final float EPSILON = 1.0E-10F;
   private final PointList poly = new PointList();
   private final PointList tris = new PointList();
   private boolean tried;

   public void addPolyPoint(float x, float y) {
      Point p = new Point(x, y);
      if (!this.poly.contains(p)) {
         this.poly.add(p);
      }

   }

   public int getPolyPointCount() {
      return this.poly.size();
   }

   public float[] getPolyPoint(int index) {
      return new float[]{this.poly.get(index).x, this.poly.get(index).y};
   }

   public boolean triangulate() {
      this.tried = true;
      return this.process(this.poly, this.tris);
   }

   public int getTriangleCount() {
      if (!this.tried) {
         throw new RuntimeException("Call triangulate() before accessing triangles");
      } else {
         return this.tris.size() / 3;
      }
   }

   public float[] getTrianglePoint(int tri, int i) {
      if (!this.tried) {
         throw new RuntimeException("Call triangulate() before accessing triangles");
      } else {
         return this.tris.get(tri * 3 + i).toArray();
      }
   }

   private float area(PointList contour) {
      int n = contour.size();
      float A = 0.0F;
      int p = n - 1;

      for(int q = 0; q < n; p = q++) {
         Point contourP = contour.get(p);
         Point contourQ = contour.get(q);
         A += contourP.getX() * contourQ.getY() - contourQ.getX() * contourP.getY();
      }

      return A * 0.5F;
   }

   private boolean insideTriangle(float Ax, float Ay, float Bx, float By, float Cx, float Cy, float Px, float Py) {
      float ax = Cx - Bx;
      float ay = Cy - By;
      float bx = Ax - Cx;
      float by = Ay - Cy;
      float cx = Bx - Ax;
      float cy = By - Ay;
      float apx = Px - Ax;
      float apy = Py - Ay;
      float bpx = Px - Bx;
      float bpy = Py - By;
      float cpx = Px - Cx;
      float cpy = Py - Cy;
      float aCROSSbp = ax * bpy - ay * bpx;
      float cCROSSap = cx * apy - cy * apx;
      float bCROSScp = bx * cpy - by * cpx;
      return aCROSSbp >= 0.0F && bCROSScp >= 0.0F && cCROSSap >= 0.0F;
   }

   private boolean snip(PointList contour, int u, int v, int w, int n, int[] V) {
      float Ax = contour.get(V[u]).getX();
      float Ay = contour.get(V[u]).getY();
      float Bx = contour.get(V[v]).getX();
      float By = contour.get(V[v]).getY();
      float Cx = contour.get(V[w]).getX();
      float Cy = contour.get(V[w]).getY();
      if (1.0E-10F > (Bx - Ax) * (Cy - Ay) - (By - Ay) * (Cx - Ax)) {
         return false;
      } else {
         for(int p = 0; p < n; ++p) {
            if (p != u && p != v && p != w) {
               float Px = contour.get(V[p]).getX();
               float Py = contour.get(V[p]).getY();
               if (this.insideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px, Py)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean process(PointList contour, PointList result) {
      result.clear();
      int n = contour.size();
      if (n < 3) {
         return false;
      } else {
         int[] V = new int[n];
         int nv;
         if (0.0F < this.area(contour)) {
            for(nv = 0; nv < n; V[nv] = nv++) {
            }
         } else {
            for(nv = 0; nv < n; ++nv) {
               V[nv] = n - 1 - nv;
            }
         }

         nv = n;
         int count = 2 * n;
         int m = 0;
         int v = n - 1;

         while(true) {
            int u;
            int w;
            do {
               if (nv <= 2) {
                  return true;
               }

               if (0 >= count--) {
                  return false;
               }

               u = v;
               if (nv <= v) {
                  u = 0;
               }

               v = u + 1;
               if (nv <= v) {
                  v = 0;
               }

               w = v + 1;
               if (nv <= w) {
                  w = 0;
               }
            } while(!this.snip(contour, u, v, w, nv, V));

            int a = V[u];
            int b = V[v];
            int c = V[w];
            result.add(contour.get(a));
            result.add(contour.get(b));
            result.add(contour.get(c));
            ++m;
            int s = v;

            for(int t = v + 1; t < nv; ++t) {
               V[s] = V[t];
               ++s;
            }

            --nv;
            count = 2 * nv;
         }
      }
   }

   public void startHole() {
   }

   private class PointList {
      private final ArrayList points = new ArrayList();

      public PointList() {
      }

      public boolean contains(Point p) {
         return this.points.contains(p);
      }

      public void add(Point point) {
         this.points.add(point);
      }

      public void remove(Point point) {
         this.points.remove(point);
      }

      public int size() {
         return this.points.size();
      }

      public Point get(int i) {
         return (Point)this.points.get(i);
      }

      public void clear() {
         this.points.clear();
      }
   }

   private static class Point {
      private final float x;
      private final float y;
      private final float[] array;

      public Point(float x, float y) {
         this.x = x;
         this.y = y;
         this.array = new float[]{x, y};
      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public float[] toArray() {
         return this.array;
      }

      public int hashCode() {
         return (int)(this.x * this.y * 31.0F);
      }

      public boolean equals(Object other) {
         if (!(other instanceof Point)) {
            return false;
         } else {
            Point p = (Point)other;
            return p.x == this.x && p.y == this.y;
         }
      }
   }
}

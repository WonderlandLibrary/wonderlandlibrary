package me.theresa.fontRenderer.font.geom;

import java.util.ArrayList;

public class GeomUtil {
   public float EPSILON = 1.0E-4F;
   public float EDGE_SCALE = 1.0F;
   public int MAX_POINTS = 10000;
   public GeomUtilListener listener;

   public Shape[] subtract(Shape target, Shape missing) {
      target = target.transform(new Transform());
      missing = missing.transform(new Transform());
      int count = 0;

      int found;
      for(found = 0; found < target.getPointCount(); ++found) {
         if (missing.contains(target.getPoint(found)[0], target.getPoint(found)[1])) {
            ++count;
         }
      }

      if (count == target.getPointCount()) {
         return new Shape[0];
      } else if (!target.intersects(missing)) {
         return new Shape[]{target};
      } else {
         found = 0;

         int i;
         for(i = 0; i < missing.getPointCount(); ++i) {
            if (target.contains(missing.getPoint(i)[0], missing.getPoint(i)[1]) && this.onPath(target, missing.getPoint(i)[0], missing.getPoint(i)[1])) {
               ++found;
            }
         }

         for(i = 0; i < target.getPointCount(); ++i) {
            if (missing.contains(target.getPoint(i)[0], target.getPoint(i)[1]) && this.onPath(missing, target.getPoint(i)[0], target.getPoint(i)[1])) {
               ++found;
            }
         }

         if (found < 1) {
            return new Shape[]{target};
         } else {
            return this.combine(target, missing, true);
         }
      }
   }

   private boolean onPath(Shape path, float x, float y) {
      for(int i = 0; i < path.getPointCount() + 1; ++i) {
         int n = rationalPoint(path, i + 1);
         Line line = this.getLine(path, rationalPoint(path, i), n);
         if (line.distance(new Vector2f(x, y)) < this.EPSILON * 100.0F) {
            return false;
         }
      }

      return true;
   }

   public void setListener(GeomUtilListener listener) {
      this.listener = listener;
   }

   public Shape[] union(Shape target, Shape other) {
      target = target.transform(new Transform());
      other = other.transform(new Transform());
      if (!target.intersects(other)) {
         return new Shape[]{target, other};
      } else {
         boolean touches = false;
         int buttCount = 0;

         int i;
         for(i = 0; i < target.getPointCount(); ++i) {
            if (other.contains(target.getPoint(i)[0], target.getPoint(i)[1]) && !other.hasVertex(target.getPoint(i)[0], target.getPoint(i)[1])) {
               touches = true;
               break;
            }

            if (other.hasVertex(target.getPoint(i)[0], target.getPoint(i)[1])) {
               ++buttCount;
            }
         }

         for(i = 0; i < other.getPointCount(); ++i) {
            if (target.contains(other.getPoint(i)[0], other.getPoint(i)[1]) && !target.hasVertex(other.getPoint(i)[0], other.getPoint(i)[1])) {
               touches = true;
               break;
            }
         }

         return !touches && buttCount < 2 ? new Shape[]{target, other} : this.combine(target, other, false);
      }
   }

   private Shape[] combine(Shape target, Shape other, boolean subtract) {
      if (!subtract) {
         for(int i = 0; i < target.getPointCount(); ++i) {
            if (!other.contains(target.getPoint(i)[0], target.getPoint(i)[1]) && !other.hasVertex(target.getPoint(i)[0], target.getPoint(i)[1])) {
               Shape shape = this.combineSingle(target, other, false, i);
               return new Shape[]{shape};
            }
         }

         return new Shape[]{other};
      } else {
         ArrayList shapes = new ArrayList();
         ArrayList used = new ArrayList();

         int i;
         float[] point;
         for(i = 0; i < target.getPointCount(); ++i) {
            point = target.getPoint(i);
            if (other.contains(point[0], point[1])) {
               used.add(new Vector2f(point[0], point[1]));
               if (this.listener != null) {
                  this.listener.pointExcluded(point[0], point[1]);
               }
            }
         }

         for(i = 0; i < target.getPointCount(); ++i) {
            point = target.getPoint(i);
            Vector2f pt = new Vector2f(point[0], point[1]);
            if (!used.contains(pt)) {
               Shape result = this.combineSingle(target, other, true, i);
               shapes.add(result);

               for(int j = 0; j < result.getPointCount(); ++j) {
                  float[] kpoint = result.getPoint(j);
                  Vector2f kpt = new Vector2f(kpoint[0], kpoint[1]);
                  used.add(kpt);
               }
            }
         }

         return (Shape[])((Shape[])shapes.toArray(new Shape[0]));
      }
   }

   private Shape combineSingle(Shape target, Shape missing, boolean subtract, int start) {
      Shape current = target;
      Shape other = missing;
      int point = start;
      int dir = 1;
      Polygon poly = new Polygon();
      boolean first = true;
      int loop = 0;
      float px = target.getPoint(start)[0];
      float py = target.getPoint(start)[1];

      while(!poly.hasVertex(px, py) || first || current != target) {
         first = false;
         ++loop;
         if (loop > this.MAX_POINTS) {
            break;
         }

         poly.addPoint(px, py);
         if (this.listener != null) {
            this.listener.pointUsed(px, py);
         }

         Line line = this.getLine(current, px, py, rationalPoint(current, point + dir));
         HitResult hit = this.intersect(other, line);
         if (hit != null) {
            Line hitLine = hit.line;
            Vector2f pt = hit.pt;
            px = pt.x;
            py = pt.y;
            if (this.listener != null) {
               this.listener.pointIntersected(px, py);
            }

            if (other.hasVertex(px, py)) {
               point = other.indexOf(pt.x, pt.y);
               dir = 1;
               px = pt.x;
               py = pt.y;
               Shape temp = current;
               current = other;
               other = temp;
            } else {
               float dx = hitLine.getDX() / hitLine.length();
               float dy = hitLine.getDY() / hitLine.length();
               dx *= this.EDGE_SCALE;
               dy *= this.EDGE_SCALE;
               Shape temp;
               if (current.contains(pt.x + dx, pt.y + dy)) {
                  if (subtract) {
                     if (current == missing) {
                        point = hit.p2;
                        dir = -1;
                     } else {
                        point = hit.p1;
                        dir = 1;
                     }
                  } else if (current == target) {
                     point = hit.p2;
                     dir = -1;
                  } else {
                     point = hit.p2;
                     dir = -1;
                  }

                  temp = current;
                  current = other;
                  other = temp;
               } else if (current.contains(pt.x - dx, pt.y - dy)) {
                  if (subtract) {
                     if (current == target) {
                        point = hit.p2;
                        dir = -1;
                     } else {
                        point = hit.p1;
                        dir = 1;
                     }
                  } else if (current == missing) {
                     point = hit.p1;
                     dir = 1;
                  } else {
                     point = hit.p1;
                     dir = 1;
                  }

                  temp = current;
                  current = other;
                  other = temp;
               } else {
                  if (subtract) {
                     break;
                  }

                  point = hit.p1;
                  dir = 1;
                  temp = current;
                  current = other;
                  other = temp;
                  point = rationalPoint(current, point + dir);
                  px = current.getPoint(point)[0];
                  py = current.getPoint(point)[1];
               }
            }
         } else {
            point = rationalPoint(current, point + dir);
            px = current.getPoint(point)[0];
            py = current.getPoint(point)[1];
         }
      }

      poly.addPoint(px, py);
      if (this.listener != null) {
         this.listener.pointUsed(px, py);
      }

      return poly;
   }

   public HitResult intersect(Shape shape, Line line) {
      float distance = Float.MAX_VALUE;
      HitResult hit = null;

      for(int i = 0; i < shape.getPointCount(); ++i) {
         int next = rationalPoint(shape, i + 1);
         Line local = this.getLine(shape, i, next);
         Vector2f pt = line.intersect(local, true);
         if (pt != null) {
            float newDis = pt.distance(line.getStart());
            if (newDis < distance && newDis > this.EPSILON) {
               hit = new HitResult();
               hit.pt = pt;
               hit.line = local;
               hit.p1 = i;
               hit.p2 = next;
               distance = newDis;
            }
         }
      }

      return hit;
   }

   public static int rationalPoint(Shape shape, int p) {
      while(p < 0) {
         p += shape.getPointCount();
      }

      while(p >= shape.getPointCount()) {
         p -= shape.getPointCount();
      }

      return p;
   }

   public Line getLine(Shape shape, int s, int e) {
      float[] start = shape.getPoint(s);
      float[] end = shape.getPoint(e);
      return new Line(start[0], start[1], end[0], end[1]);
   }

   public Line getLine(Shape shape, float sx, float sy, int e) {
      float[] end = shape.getPoint(e);
      return new Line(sx, sy, end[0], end[1]);
   }

   public static class HitResult {
      public Line line;
      public int p1;
      public int p2;
      public Vector2f pt;
   }
}

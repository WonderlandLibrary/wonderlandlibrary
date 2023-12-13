package me.theresa.fontRenderer.font.geom;

public class Circle extends Ellipse {
   public float radius;

   public strictfp Circle(float centerPointX, float centerPointY, float radius) {
      this(centerPointX, centerPointY, radius, 50);
   }

   public strictfp Circle(float centerPointX, float centerPointY, float radius, int segmentCount) {
      super(centerPointX, centerPointY, radius, radius, segmentCount);
      this.x = centerPointX - radius;
      this.y = centerPointY - radius;
      this.radius = radius;
      this.boundingCircleRadius = radius;
   }

   public strictfp float getCenterX() {
      return this.getX() + this.radius;
   }

   public strictfp float getCenterY() {
      return this.getY() + this.radius;
   }

   public strictfp void setRadius(float radius) {
      if (radius != this.radius) {
         this.pointsDirty = true;
         this.radius = radius;
         this.setRadii(radius, radius);
      }

   }

   public strictfp float getRadius() {
      return this.radius;
   }

   public strictfp boolean intersects(Shape shape) {
      if (shape instanceof Circle) {
         Circle other = (Circle)shape;
         float totalRad2 = this.getRadius() + other.getRadius();
         if (Math.abs(other.getCenterX() - this.getCenterX()) > totalRad2) {
            return false;
         } else if (Math.abs(other.getCenterY() - this.getCenterY()) > totalRad2) {
            return false;
         } else {
            totalRad2 *= totalRad2;
            float dx = Math.abs(other.getCenterX() - this.getCenterX());
            float dy = Math.abs(other.getCenterY() - this.getCenterY());
            return totalRad2 >= dx * dx + dy * dy;
         }
      } else {
         return shape instanceof Rectangle ? this.intersects((Rectangle)shape) : super.intersects(shape);
      }
   }

   public strictfp boolean contains(float x, float y) {
      return (x - this.getX()) * (x - this.getX()) + (y - this.getY()) * (y - this.getY()) < this.getRadius() * this.getRadius();
   }

   private strictfp boolean contains(Line line) {
      return this.contains(line.getX1(), line.getY1()) && this.contains(line.getX2(), line.getY2());
   }

   protected strictfp void findCenter() {
      this.center = new float[2];
      this.center[0] = this.x + this.radius;
      this.center[1] = this.y + this.radius;
   }

   protected strictfp void calculateRadius() {
      this.boundingCircleRadius = this.radius;
   }

   private strictfp boolean intersects(Rectangle other) {
      if (other.contains(this.x + this.radius, this.y + this.radius)) {
         return true;
      } else {
         float x1 = other.getX();
         float y1 = other.getY();
         float x2 = other.getX() + other.getWidth();
         float y2 = other.getY() + other.getHeight();
         Line[] lines = new Line[]{new Line(x1, y1, x2, y1), new Line(x2, y1, x2, y2), new Line(x2, y2, x1, y2), new Line(x1, y2, x1, y1)};
         float r2 = this.getRadius() * this.getRadius();
         Vector2f pos = new Vector2f(this.getCenterX(), this.getCenterY());

         for(int i = 0; i < 4; ++i) {
            float dis = lines[i].distanceSquared(pos);
            if (dis < r2) {
               return true;
            }
         }

         return false;
      }
   }

   private strictfp boolean intersects(Line other) {
      Vector2f lineSegmentStart = new Vector2f(other.getX1(), other.getY1());
      Vector2f lineSegmentEnd = new Vector2f(other.getX2(), other.getY2());
      Vector2f circleCenter = new Vector2f(this.getCenterX(), this.getCenterY());
      Vector2f segv = lineSegmentEnd.copy().sub(lineSegmentStart);
      Vector2f ptv = circleCenter.copy().sub(lineSegmentStart);
      float segvLength = segv.length();
      float projvl = ptv.dot(segv) / segvLength;
      Vector2f closest;
      if (projvl < 0.0F) {
         closest = lineSegmentStart;
      } else if (projvl > segvLength) {
         closest = lineSegmentEnd;
      } else {
         Vector2f projv = segv.copy().scale(projvl / segvLength);
         closest = lineSegmentStart.copy().add(projv);
      }

      return circleCenter.copy().sub(closest).lengthSquared() <= this.getRadius() * this.getRadius();
   }
}

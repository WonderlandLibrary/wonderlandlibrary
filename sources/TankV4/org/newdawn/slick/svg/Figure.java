package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class Figure {
   public static final int ELLIPSE = 1;
   public static final int LINE = 2;
   public static final int RECTANGLE = 3;
   public static final int PATH = 4;
   public static final int POLYGON = 5;
   private int type;
   private Shape shape;
   private NonGeometricData data;
   private Transform transform;

   public Figure(int var1, Shape var2, NonGeometricData var3, Transform var4) {
      this.shape = var2;
      this.data = var3;
      this.type = var1;
      this.transform = var4;
   }

   public Transform getTransform() {
      return this.transform;
   }

   public int getType() {
      return this.type;
   }

   public Shape getShape() {
      return this.shape;
   }

   public NonGeometricData getData() {
      return this.data;
   }
}

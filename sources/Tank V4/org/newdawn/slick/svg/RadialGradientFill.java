package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class RadialGradientFill implements TexCoordGenerator {
   private Vector2f centre;
   private float radius;
   private Gradient gradient;
   private Shape shape;

   public RadialGradientFill(Shape var1, Transform var2, Gradient var3) {
      this.gradient = var3;
      this.radius = var3.getR();
      float var4 = var3.getX1();
      float var5 = var3.getY1();
      float[] var6 = new float[]{var4, var5};
      var3.getTransform().transform(var6, 0, var6, 0, 1);
      var2.transform(var6, 0, var6, 0, 1);
      float[] var7 = new float[]{var4, var5 - this.radius};
      var3.getTransform().transform(var7, 0, var7, 0, 1);
      var2.transform(var7, 0, var7, 0, 1);
      this.centre = new Vector2f(var6[0], var6[1]);
      Vector2f var8 = new Vector2f(var7[0], var7[1]);
      this.radius = var8.distance(this.centre);
   }

   public Vector2f getCoordFor(float var1, float var2) {
      float var3 = this.centre.distance(new Vector2f(var1, var2));
      var3 /= this.radius;
      if (var3 > 0.99F) {
         var3 = 0.99F;
      }

      return new Vector2f(var3, 0.0F);
   }
}

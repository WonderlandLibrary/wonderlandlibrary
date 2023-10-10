package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.TexCoordGenerator;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class LinearGradientFill implements TexCoordGenerator {
   private Vector2f start;
   private Vector2f end;
   private Gradient gradient;
   private Line line;
   private Shape shape;

   public LinearGradientFill(Shape var1, Transform var2, Gradient var3) {
      this.gradient = var3;
      float var4 = var3.getX1();
      float var5 = var3.getY1();
      float var6 = var3.getX2();
      float var7 = var3.getY2();
      float var8 = var7 - var5;
      float var9 = var6 - var4;
      float[] var10 = new float[]{var4, var5 + var8 / 2.0F};
      var3.getTransform().transform(var10, 0, var10, 0, 1);
      var2.transform(var10, 0, var10, 0, 1);
      float[] var11 = new float[]{var4 + var9, var5 + var8 / 2.0F};
      var3.getTransform().transform(var11, 0, var11, 0, 1);
      var2.transform(var11, 0, var11, 0, 1);
      this.start = new Vector2f(var10[0], var10[1]);
      this.end = new Vector2f(var11[0], var11[1]);
      this.line = new Line(this.start, this.end);
   }

   public Vector2f getCoordFor(float var1, float var2) {
      Vector2f var3 = new Vector2f();
      this.line.getClosestPoint(new Vector2f(var1, var2), var3);
      float var4 = var3.distance(this.start);
      var4 /= this.line.length();
      return new Vector2f(var4, 0.0F);
   }
}

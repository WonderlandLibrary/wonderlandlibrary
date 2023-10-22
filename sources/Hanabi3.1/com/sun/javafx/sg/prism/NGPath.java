package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.scene.shape.FillRule;

public class NGPath extends NGShape {
   private Path2D p = new Path2D();

   public void reset() {
      this.p.reset();
   }

   public void update() {
      this.geometryChanged();
   }

   private int toWindingRule(FillRule var1) {
      return var1 == FillRule.NON_ZERO ? 1 : 0;
   }

   public void setFillRule(FillRule var1) {
      this.p.setWindingRule(this.toWindingRule(var1));
   }

   public float getCurrentX() {
      return this.p.getCurrentPoint().x;
   }

   public float getCurrentY() {
      return this.p.getCurrentPoint().y;
   }

   public void addClosePath() {
      this.p.closePath();
   }

   public void addMoveTo(float var1, float var2) {
      this.p.moveTo(var1, var2);
   }

   public void addLineTo(float var1, float var2) {
      this.p.lineTo(var1, var2);
   }

   public void addQuadTo(float var1, float var2, float var3, float var4) {
      this.p.quadTo(var1, var2, var3, var4);
   }

   public void addCubicTo(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.p.curveTo(var1, var2, var3, var4, var5, var6);
   }

   public void addArcTo(float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      Arc2D var8 = new Arc2D(var1, var2, var3, var4, var5, var6, 0);
      BaseTransform var9 = (double)var7 == 0.0 ? null : BaseTransform.getRotateInstance((double)var7, (double)var8.getCenterX(), (double)var8.getCenterY());
      PathIterator var10 = var8.getPathIterator(var9);
      var10.next();
      this.p.append(var10, true);
   }

   public Path2D getGeometry() {
      return this.p;
   }

   public Shape getShape() {
      return this.p;
   }

   public boolean acceptsPath2dOnUpdate() {
      return true;
   }

   public void updateWithPath2d(Path2D var1) {
      this.p.setTo(var1);
      this.geometryChanged();
   }
}

package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCPathIterator;
import com.sun.webkit.graphics.WCRectangle;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

final class WCPathImpl extends WCPath {
   private final Path2D path;
   private boolean hasCP = false;
   private static final Logger log = Logger.getLogger(WCPathImpl.class.getName());

   WCPathImpl() {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "Create empty WCPathImpl({0})", this.getID());
      }

      this.path = new Path2D();
   }

   WCPathImpl(WCPathImpl var1) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "Create WCPathImpl({0}) from WCPathImpl({1})", new Object[]{this.getID(), var1.getID()});
      }

      this.path = new Path2D(var1.path);
      this.hasCP = var1.hasCP;
   }

   public void addRect(double var1, double var3, double var5, double var7) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).addRect({1},{2},{3},{4})", new Object[]{this.getID(), var1, var3, var5, var7});
      }

      this.hasCP = true;
      this.path.append((Shape)(new RoundRectangle2D((float)var1, (float)var3, (float)var5, (float)((int)var7), 0.0F, 0.0F)), false);
   }

   public void addEllipse(double var1, double var3, double var5, double var7) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).addEllipse({1},{2},{3},{4})", new Object[]{this.getID(), var1, var3, var5, var7});
      }

      this.hasCP = true;
      this.path.append((Shape)(new Ellipse2D((float)var1, (float)var3, (float)var5, (float)var7)), false);
   }

   public void addArcTo(double var1, double var3, double var5, double var7, double var9) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).addArcTo({1},{2},{3},{4})", new Object[]{this.getID(), var1, var3, var5, var7});
      }

      Arc2D var11 = new Arc2D();
      var11.setArcByTangent(this.path.getCurrentPoint(), new Point2D((float)var1, (float)var3), new Point2D((float)var5, (float)var7), (float)var9);
      this.hasCP = true;
      this.path.append((Shape)var11, true);
   }

   public void addArc(double var1, double var3, double var5, double var7, double var9, boolean var11) {
      float var13 = (float)var7;
      float var14 = (float)var9;
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).addArc(x={1},y={2},r={3},sa=|{4}|,ea=|{5}|,aclock={6})", new Object[]{this.getID(), var1, var3, var5, var13, var14, var11});
      }

      this.hasCP = true;
      float var15 = var14;
      if (!var11 && var13 > var14) {
         var15 = var13 + (6.2831855F - (var13 - var14) % 6.2831855F);
      } else if (var11 && var13 < var14) {
         var15 = var13 - (6.2831855F - (var14 - var13) % 6.2831855F);
      }

      this.path.append((Shape)(new Arc2D((float)(var1 - var5), (float)(var3 - var5), (float)(2.0 * var5), (float)(2.0 * var5), (float)Math.toDegrees((double)(-var13)), (float)Math.toDegrees((double)(var13 - var15)), 0)), true);
   }

   public boolean contains(int var1, double var2, double var4) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).contains({1},{2},{3})", new Object[]{this.getID(), var1, var2, var4});
      }

      int var6 = this.path.getWindingRule();
      this.path.setWindingRule(var1);
      boolean var7 = this.path.contains((float)var2, (float)var4);
      this.path.setWindingRule(var6);
      return var7;
   }

   public WCRectangle getBounds() {
      RectBounds var1 = this.path.getBounds();
      return new WCRectangle(var1.getMinX(), var1.getMinY(), var1.getWidth(), var1.getHeight());
   }

   public void clear() {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).clear()", this.getID());
      }

      this.hasCP = false;
      this.path.reset();
   }

   public void moveTo(double var1, double var3) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).moveTo({1},{2})", new Object[]{this.getID(), var1, var3});
      }

      this.hasCP = true;
      this.path.moveTo((float)var1, (float)var3);
   }

   public void addLineTo(double var1, double var3) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).addLineTo({1},{2})", new Object[]{this.getID(), var1, var3});
      }

      this.hasCP = true;
      this.path.lineTo((float)var1, (float)var3);
   }

   public void addQuadCurveTo(double var1, double var3, double var5, double var7) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).addQuadCurveTo({1},{2},{3},{4})", new Object[]{this.getID(), var1, var3, var5, var7});
      }

      this.hasCP = true;
      this.path.quadTo((float)var1, (float)var3, (float)var5, (float)var7);
   }

   public void addBezierCurveTo(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).addBezierCurveTo({1},{2},{3},{4},{5},{6})", new Object[]{this.getID(), var1, var3, var5, var7, var9, var11});
      }

      this.hasCP = true;
      this.path.curveTo((float)var1, (float)var3, (float)var5, (float)var7, (float)var9, (float)var11);
   }

   public void addPath(WCPath var1) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).addPath({1})", new Object[]{this.getID(), var1.getID()});
      }

      this.hasCP = this.hasCP || ((WCPathImpl)var1).hasCP;
      this.path.append((Shape)((WCPathImpl)var1).path, false);
   }

   public void closeSubpath() {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).closeSubpath()", this.getID());
      }

      this.path.closePath();
   }

   public boolean isEmpty() {
      return !this.hasCP;
   }

   public int getWindingRule() {
      return 1 - this.path.getWindingRule();
   }

   public void setWindingRule(int var1) {
      this.path.setWindingRule(1 - var1);
   }

   public Path2D getPlatformPath() {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).getPath() BEGIN=====", this.getID());
         PathIterator var1 = this.path.getPathIterator((BaseTransform)null);

         for(float[] var2 = new float[6]; !var1.isDone(); var1.next()) {
            switch (var1.currentSegment(var2)) {
               case 0:
                  log.log(Level.FINE, "SEG_MOVETO ({0},{1})", new Object[]{var2[0], var2[1]});
                  break;
               case 1:
                  log.log(Level.FINE, "SEG_LINETO ({0},{1})", new Object[]{var2[0], var2[1]});
                  break;
               case 2:
                  log.log(Level.FINE, "SEG_QUADTO ({0},{1},{2},{3})", new Object[]{var2[0], var2[1], var2[2], var2[3]});
                  break;
               case 3:
                  log.log(Level.FINE, "SEG_CUBICTO ({0},{1},{2},{3},{4},{5})", new Object[]{var2[0], var2[1], var2[2], var2[3], var2[4], var2[5]});
                  break;
               case 4:
                  log.fine("SEG_CLOSE");
            }
         }

         log.fine("========getPath() END=====");
      }

      return this.path;
   }

   public WCPathIterator getPathIterator() {
      final PathIterator var1 = this.path.getPathIterator((BaseTransform)null);
      return new WCPathIterator() {
         public int getWindingRule() {
            return var1.getWindingRule();
         }

         public boolean isDone() {
            return var1.isDone();
         }

         public void next() {
            var1.next();
         }

         public int currentSegment(double[] var1x) {
            float[] var2 = new float[6];
            int var3 = var1.currentSegment(var2);

            for(int var4 = 0; var4 < var1x.length; ++var4) {
               var1x[var4] = (double)var2[var4];
            }

            return var3;
         }
      };
   }

   public void translate(double var1, double var3) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).translate({1}, {2})", new Object[]{this.getID(), var1, var3});
      }

      this.path.transform(BaseTransform.getTranslateInstance(var1, var3));
   }

   public void transform(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).transform({1},{2},{3},{4},{5},{6})", new Object[]{this.getID(), var1, var3, var5, var7, var9, var11});
      }

      this.path.transform(BaseTransform.getInstance(var1, var3, var5, var7, var9, var11));
   }

   public boolean strokeContains(double var1, double var3, double var5, double var7, int var9, int var10, double var11, double[] var13) {
      BasicStroke var14 = new BasicStroke((float)var5, var9, var10, (float)var7);
      if (var13.length > 0) {
         var14.set(var13, (float)var11);
      }

      boolean var15 = var14.createCenteredStrokedShape(this.path).contains((float)var1, (float)var3);
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "WCPathImpl({0}).strokeContains({1},{2},{3},{4},{5},{6},{7},{8}) = {9}", new Object[]{this.getID(), var1, var3, var5, var7, var9, var10, var11, Arrays.toString(var13), var15});
      }

      return var15;
   }
}

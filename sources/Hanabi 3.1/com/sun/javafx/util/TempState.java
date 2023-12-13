package com.sun.javafx.util;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;

public final class TempState {
   public BaseBounds bounds;
   public final BaseTransform pickTx;
   public final Affine3D leafTx;
   public final Point2D point;
   public final Vec3d vec3d;
   public final GeneralTransform3D projViewTx;
   public final Affine3D tempTx;
   private static final ThreadLocal tempStateRef = new ThreadLocal() {
      protected TempState initialValue() {
         return new TempState();
      }
   };

   private TempState() {
      this.bounds = new RectBounds(0.0F, 0.0F, -1.0F, -1.0F);
      this.pickTx = new Affine3D();
      this.leafTx = new Affine3D();
      this.point = new Point2D(0.0F, 0.0F);
      this.vec3d = new Vec3d(0.0, 0.0, 0.0);
      this.projViewTx = new GeneralTransform3D();
      this.tempTx = new Affine3D();
   }

   public static TempState getInstance() {
      return (TempState)tempStateRef.get();
   }

   // $FF: synthetic method
   TempState(Object var1) {
      this();
   }
}

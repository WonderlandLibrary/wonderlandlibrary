package javafx.scene;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sun.util.logging.PlatformLogger;

public class PerspectiveCamera extends Camera {
   private boolean fixedEyeAtCameraZero;
   private static final Affine3D LOOK_AT_TX = new Affine3D();
   private static final Affine3D LOOK_AT_TX_FIXED_EYE = new Affine3D();
   private DoubleProperty fieldOfView;
   private BooleanProperty verticalFieldOfView;

   public final void setFieldOfView(double var1) {
      this.fieldOfViewProperty().set(var1);
   }

   public final double getFieldOfView() {
      return this.fieldOfView == null ? 30.0 : this.fieldOfView.get();
   }

   public final DoubleProperty fieldOfViewProperty() {
      if (this.fieldOfView == null) {
         this.fieldOfView = new SimpleDoubleProperty(this, "fieldOfView", 30.0) {
            protected void invalidated() {
               PerspectiveCamera.this.impl_markDirty(DirtyBits.NODE_CAMERA);
            }
         };
      }

      return this.fieldOfView;
   }

   public final void setVerticalFieldOfView(boolean var1) {
      this.verticalFieldOfViewProperty().set(var1);
   }

   public final boolean isVerticalFieldOfView() {
      return this.verticalFieldOfView == null ? true : this.verticalFieldOfView.get();
   }

   public final BooleanProperty verticalFieldOfViewProperty() {
      if (this.verticalFieldOfView == null) {
         this.verticalFieldOfView = new SimpleBooleanProperty(this, "verticalFieldOfView", true) {
            protected void invalidated() {
               PerspectiveCamera.this.impl_markDirty(DirtyBits.NODE_CAMERA);
            }
         };
      }

      return this.verticalFieldOfView;
   }

   public PerspectiveCamera() {
      this(false);
   }

   public PerspectiveCamera(boolean var1) {
      this.fixedEyeAtCameraZero = false;
      if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
         String var2 = PerspectiveCamera.class.getName();
         PlatformLogger.getLogger(var2).warning("System can't support ConditionalFeature.SCENE3D");
      }

      this.fixedEyeAtCameraZero = var1;
   }

   public final boolean isFixedEyeAtCameraZero() {
      return this.fixedEyeAtCameraZero;
   }

   final PickRay computePickRay(double var1, double var3, PickRay var5) {
      return PickRay.computePerspectivePickRay(var1, var3, this.fixedEyeAtCameraZero, this.getViewWidth(), this.getViewHeight(), Math.toRadians(this.getFieldOfView()), this.isVerticalFieldOfView(), this.getCameraTransform(), this.getNearClip(), this.getFarClip(), var5);
   }

   Camera copy() {
      PerspectiveCamera var1 = new PerspectiveCamera(this.fixedEyeAtCameraZero);
      var1.setNearClip(this.getNearClip());
      var1.setFarClip(this.getFarClip());
      var1.setFieldOfView(this.getFieldOfView());
      return var1;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      NGPerspectiveCamera var1 = new NGPerspectiveCamera(this.fixedEyeAtCameraZero);
      var1.setNearClip((float)this.getNearClip());
      var1.setFarClip((float)this.getFarClip());
      var1.setFieldOfView((float)this.getFieldOfView());
      return var1;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      NGPerspectiveCamera var1 = (NGPerspectiveCamera)this.impl_getPeer();
      if (this.impl_isDirty(DirtyBits.NODE_CAMERA)) {
         var1.setVerticalFieldOfView(this.isVerticalFieldOfView());
         var1.setFieldOfView((float)this.getFieldOfView());
      }

   }

   void computeProjectionTransform(GeneralTransform3D var1) {
      var1.perspective(this.isVerticalFieldOfView(), Math.toRadians(this.getFieldOfView()), this.getViewWidth() / this.getViewHeight(), this.getNearClip(), this.getFarClip());
   }

   void computeViewTransform(Affine3D var1) {
      if (this.isFixedEyeAtCameraZero()) {
         var1.setTransform(LOOK_AT_TX_FIXED_EYE);
      } else {
         double var2 = this.getViewWidth();
         double var4 = this.getViewHeight();
         boolean var6 = this.isVerticalFieldOfView();
         double var7 = var2 / var4;
         double var9 = Math.tan(Math.toRadians(this.getFieldOfView()) / 2.0);
         double var11 = -var9 * (var6 ? var7 : 1.0);
         double var13 = var9 * (var6 ? 1.0 : 1.0 / var7);
         double var15 = 2.0 * var9 / (var6 ? var4 : var2);
         var1.setToTranslation(var11, var13, 0.0);
         var1.concatenate(LOOK_AT_TX);
         var1.scale(var15, var15, var15);
      }

   }

   Vec3d computePosition(Vec3d var1) {
      if (var1 == null) {
         var1 = new Vec3d();
      }

      if (this.fixedEyeAtCameraZero) {
         var1.set(0.0, 0.0, 0.0);
      } else {
         double var2 = this.getViewWidth() / 2.0;
         double var4 = this.getViewHeight() / 2.0;
         double var6 = this.isVerticalFieldOfView() ? var4 : var2;
         double var8 = var6 / Math.tan(Math.toRadians(this.getFieldOfView() / 2.0));
         var1.set(var2, var4, -var8);
      }

      return var1;
   }

   static {
      LOOK_AT_TX.setToTranslation(0.0, 0.0, -1.0);
      LOOK_AT_TX.rotate(Math.PI, 1.0, 0.0, 0.0);
      LOOK_AT_TX_FIXED_EYE.rotate(Math.PI, 1.0, 0.0, 0.0);
   }
}

package javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGCamera;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Transform;
import sun.util.logging.PlatformLogger;

public abstract class Camera extends Node {
   private Affine3D localToSceneTx = new Affine3D();
   private double farClipInScene;
   private double nearClipInScene;
   private Scene ownerScene = null;
   private SubScene ownerSubScene = null;
   private GeneralTransform3D projViewTx = new GeneralTransform3D();
   private GeneralTransform3D projTx = new GeneralTransform3D();
   private Affine3D viewTx = new Affine3D();
   private double viewWidth = 1.0;
   private double viewHeight = 1.0;
   private Vec3d position = new Vec3d();
   private boolean clipInSceneValid = false;
   private boolean projViewTxValid = false;
   private boolean localToSceneValid = false;
   private boolean sceneToLocalValid = false;
   private Affine3D sceneToLocalTx = new Affine3D();
   private DoubleProperty nearClip;
   private DoubleProperty farClip;

   protected Camera() {
      InvalidationListener var1 = (var1x) -> {
         this.impl_markDirty(DirtyBits.NODE_CAMERA_TRANSFORM);
      };
      this.localToSceneTransformProperty().addListener(var1);
      this.sceneProperty().addListener(var1);
   }

   double getFarClipInScene() {
      this.updateClipPlane();
      return this.farClipInScene;
   }

   double getNearClipInScene() {
      this.updateClipPlane();
      return this.nearClipInScene;
   }

   private void updateClipPlane() {
      if (!this.clipInSceneValid) {
         Transform var1 = this.getLocalToSceneTransform();
         this.nearClipInScene = var1.transform(0.0, 0.0, this.getNearClip()).getZ();
         this.farClipInScene = var1.transform(0.0, 0.0, this.getFarClip()).getZ();
         this.clipInSceneValid = true;
      }

   }

   Affine3D getSceneToLocalTransform() {
      if (!this.sceneToLocalValid) {
         this.sceneToLocalTx.setTransform(this.getCameraTransform());

         try {
            this.sceneToLocalTx.invert();
         } catch (NoninvertibleTransformException var3) {
            String var2 = Camera.class.getName();
            PlatformLogger.getLogger(var2).severe("getSceneToLocalTransform", var3);
            this.sceneToLocalTx.setToIdentity();
         }

         this.sceneToLocalValid = true;
      }

      return this.sceneToLocalTx;
   }

   public final void setNearClip(double var1) {
      this.nearClipProperty().set(var1);
   }

   public final double getNearClip() {
      return this.nearClip == null ? 0.1 : this.nearClip.get();
   }

   public final DoubleProperty nearClipProperty() {
      if (this.nearClip == null) {
         this.nearClip = new SimpleDoubleProperty(this, "nearClip", 0.1) {
            protected void invalidated() {
               Camera.this.clipInSceneValid = false;
               Camera.this.impl_markDirty(DirtyBits.NODE_CAMERA);
            }
         };
      }

      return this.nearClip;
   }

   public final void setFarClip(double var1) {
      this.farClipProperty().set(var1);
   }

   public final double getFarClip() {
      return this.farClip == null ? 100.0 : this.farClip.get();
   }

   public final DoubleProperty farClipProperty() {
      if (this.farClip == null) {
         this.farClip = new SimpleDoubleProperty(this, "farClip", 100.0) {
            protected void invalidated() {
               Camera.this.clipInSceneValid = false;
               Camera.this.impl_markDirty(DirtyBits.NODE_CAMERA);
            }
         };
      }

      return this.farClip;
   }

   Camera copy() {
      return this;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      NGCamera var1 = (NGCamera)this.impl_getPeer();
      if (!this.impl_isDirtyEmpty()) {
         if (this.impl_isDirty(DirtyBits.NODE_CAMERA)) {
            var1.setNearClip((float)this.getNearClip());
            var1.setFarClip((float)this.getFarClip());
            var1.setViewWidth(this.getViewWidth());
            var1.setViewHeight(this.getViewHeight());
         }

         if (this.impl_isDirty(DirtyBits.NODE_CAMERA_TRANSFORM)) {
            var1.setWorldTransform(this.getCameraTransform());
         }

         var1.setProjViewTransform(this.getProjViewTransform());
         this.position = this.computePosition(this.position);
         this.getCameraTransform().transform(this.position, this.position);
         var1.setPosition(this.position);
      }

   }

   void setViewWidth(double var1) {
      this.viewWidth = var1;
      this.impl_markDirty(DirtyBits.NODE_CAMERA);
   }

   double getViewWidth() {
      return this.viewWidth;
   }

   void setViewHeight(double var1) {
      this.viewHeight = var1;
      this.impl_markDirty(DirtyBits.NODE_CAMERA);
   }

   double getViewHeight() {
      return this.viewHeight;
   }

   void setOwnerScene(Scene var1) {
      if (var1 == null) {
         this.ownerScene = null;
      } else if (var1 != this.ownerScene) {
         if (this.ownerScene != null || this.ownerSubScene != null) {
            throw new IllegalArgumentException(this + "is already set as camera in other scene or subscene");
         }

         this.ownerScene = var1;
         this.markOwnerDirty();
      }

   }

   void setOwnerSubScene(SubScene var1) {
      if (var1 == null) {
         this.ownerSubScene = null;
      } else if (var1 != this.ownerSubScene) {
         if (this.ownerScene != null || this.ownerSubScene != null) {
            throw new IllegalArgumentException(this + "is already set as camera in other scene or subscene");
         }

         this.ownerSubScene = var1;
         this.markOwnerDirty();
      }

   }

   /** @deprecated */
   @Deprecated
   protected void impl_markDirty(DirtyBits var1) {
      super.impl_markDirty(var1);
      if (var1 == DirtyBits.NODE_CAMERA_TRANSFORM) {
         this.localToSceneValid = false;
         this.sceneToLocalValid = false;
         this.clipInSceneValid = false;
         this.projViewTxValid = false;
      } else if (var1 == DirtyBits.NODE_CAMERA) {
         this.projViewTxValid = false;
      }

      this.markOwnerDirty();
   }

   private void markOwnerDirty() {
      if (this.ownerScene != null) {
         this.ownerScene.markCameraDirty();
      }

      if (this.ownerSubScene != null) {
         this.ownerSubScene.markContentDirty();
      }

   }

   Affine3D getCameraTransform() {
      if (!this.localToSceneValid) {
         this.localToSceneTx.setToIdentity();
         this.getLocalToSceneTransform().impl_apply(this.localToSceneTx);
         this.localToSceneValid = true;
      }

      return this.localToSceneTx;
   }

   abstract void computeProjectionTransform(GeneralTransform3D var1);

   abstract void computeViewTransform(Affine3D var1);

   GeneralTransform3D getProjViewTransform() {
      if (!this.projViewTxValid) {
         this.computeProjectionTransform(this.projTx);
         this.computeViewTransform(this.viewTx);
         this.projViewTx.set(this.projTx);
         this.projViewTx.mul((BaseTransform)this.viewTx);
         this.projViewTx.mul((BaseTransform)this.getSceneToLocalTransform());
         this.projViewTxValid = true;
      }

      return this.projViewTx;
   }

   private Point2D project(Point3D var1) {
      Vec3d var2 = this.getProjViewTransform().transform(new Vec3d(var1.getX(), var1.getY(), var1.getZ()));
      double var3 = this.getViewWidth() / 2.0;
      double var5 = this.getViewHeight() / 2.0;
      return new Point2D(var3 * (1.0 + var2.x), var5 * (1.0 - var2.y));
   }

   private Point2D pickNodeXYPlane(Node var1, double var2, double var4) {
      PickRay var6 = this.computePickRay(var2, var4, (PickRay)null);
      Affine3D var7 = new Affine3D();
      var1.getLocalToSceneTransform().impl_apply(var7);
      Vec3d var8 = var6.getOriginNoClone();
      Vec3d var9 = var6.getDirectionNoClone();

      try {
         var7.inverseTransform(var8, var8);
         var7.inverseDeltaTransform(var9, var9);
      } catch (NoninvertibleTransformException var12) {
         return null;
      }

      if (almostZero(var9.z)) {
         return null;
      } else {
         double var10 = -var8.z / var9.z;
         return new Point2D(var8.x + var9.x * var10, var8.y + var9.y * var10);
      }
   }

   Point3D pickProjectPlane(double var1, double var3) {
      PickRay var5 = this.computePickRay(var1, var3, (PickRay)null);
      Vec3d var6 = new Vec3d();
      var6.add(var5.getOriginNoClone(), var5.getDirectionNoClone());
      return new Point3D(var6.x, var6.y, var6.z);
   }

   abstract PickRay computePickRay(double var1, double var3, PickRay var5);

   abstract Vec3d computePosition(Vec3d var1);

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      return new BoxBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      return false;
   }

   /** @deprecated */
   @Deprecated
   public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   static {
      CameraHelper.setCameraAccessor(new CameraHelper.CameraAccessor() {
         public Point2D project(Camera var1, Point3D var2) {
            return var1.project(var2);
         }

         public Point2D pickNodeXYPlane(Camera var1, Node var2, double var3, double var5) {
            return var1.pickNodeXYPlane(var2, var3, var5);
         }

         public Point3D pickProjectPlane(Camera var1, double var2, double var4) {
            return var1.pickProjectPlane(var2, var4);
         }
      });
   }
}

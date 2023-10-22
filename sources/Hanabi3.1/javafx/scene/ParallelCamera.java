package javafx.scene;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGParallelCamera;

public class ParallelCamera extends Camera {
   Camera copy() {
      ParallelCamera var1 = new ParallelCamera();
      var1.setNearClip(this.getNearClip());
      var1.setFarClip(this.getFarClip());
      return var1;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      NGParallelCamera var1 = new NGParallelCamera();
      var1.setNearClip((float)this.getNearClip());
      var1.setFarClip((float)this.getFarClip());
      return var1;
   }

   final PickRay computePickRay(double var1, double var3, PickRay var5) {
      return PickRay.computeParallelPickRay(var1, var3, this.getViewHeight(), this.getCameraTransform(), this.getNearClip(), this.getFarClip(), var5);
   }

   void computeProjectionTransform(GeneralTransform3D var1) {
      double var2 = this.getViewWidth();
      double var4 = this.getViewHeight();
      double var6 = var2 > var4 ? var2 / 2.0 : var4 / 2.0;
      var1.ortho(0.0, var2, var4, 0.0, -var6, var6);
   }

   void computeViewTransform(Affine3D var1) {
      var1.setToIdentity();
   }

   Vec3d computePosition(Vec3d var1) {
      if (var1 == null) {
         var1 = new Vec3d();
      }

      double var2 = this.getViewWidth() / 2.0;
      double var4 = this.getViewHeight() / 2.0;
      double var6 = var4 / Math.tan(Math.toRadians(15.0));
      var1.set(var2, var4, -var6);
      return var1;
   }
}

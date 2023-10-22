package javafx.scene;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import sun.util.logging.PlatformLogger;

public class SnapshotParameters {
   private boolean depthBuffer;
   private Camera camera;
   private Transform transform;
   private Paint fill;
   private Rectangle2D viewport;
   Camera defaultCamera;

   public boolean isDepthBuffer() {
      return this.depthBuffer;
   }

   boolean isDepthBufferInternal() {
      return !Platform.isSupported(ConditionalFeature.SCENE3D) ? false : this.depthBuffer;
   }

   public void setDepthBuffer(boolean var1) {
      if (var1 && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
         String var2 = SnapshotParameters.class.getName();
         PlatformLogger.getLogger(var2).warning("System can't support ConditionalFeature.SCENE3D");
      }

      this.depthBuffer = var1;
   }

   public Camera getCamera() {
      return this.camera;
   }

   Camera getEffectiveCamera() {
      if (this.camera instanceof PerspectiveCamera && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
         if (this.defaultCamera == null) {
            this.defaultCamera = new ParallelCamera();
         }

         return this.defaultCamera;
      } else {
         return this.camera;
      }
   }

   public void setCamera(Camera var1) {
      if (var1 instanceof PerspectiveCamera && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
         String var2 = SnapshotParameters.class.getName();
         PlatformLogger.getLogger(var2).warning("System can't support ConditionalFeature.SCENE3D");
      }

      this.camera = var1;
   }

   public Transform getTransform() {
      return this.transform;
   }

   public void setTransform(Transform var1) {
      this.transform = var1;
   }

   public Paint getFill() {
      return this.fill;
   }

   public void setFill(Paint var1) {
      this.fill = var1;
   }

   public Rectangle2D getViewport() {
      return this.viewport;
   }

   public void setViewport(Rectangle2D var1) {
      this.viewport = var1;
   }

   SnapshotParameters copy() {
      SnapshotParameters var1 = new SnapshotParameters();
      var1.camera = this.camera == null ? null : this.camera.copy();
      var1.depthBuffer = this.depthBuffer;
      var1.fill = this.fill;
      var1.viewport = this.viewport;
      var1.transform = this.transform == null ? null : this.transform.clone();
      return var1;
   }
}

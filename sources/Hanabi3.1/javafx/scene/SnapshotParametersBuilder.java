package javafx.scene;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class SnapshotParametersBuilder implements Builder {
   private int __set;
   private Camera camera;
   private boolean depthBuffer;
   private Paint fill;
   private Transform transform;
   private Rectangle2D viewport;

   protected SnapshotParametersBuilder() {
   }

   public static SnapshotParametersBuilder create() {
      return new SnapshotParametersBuilder();
   }

   public void applyTo(SnapshotParameters var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setCamera(this.camera);
      }

      if ((var2 & 2) != 0) {
         var1.setDepthBuffer(this.depthBuffer);
      }

      if ((var2 & 4) != 0) {
         var1.setFill(this.fill);
      }

      if ((var2 & 8) != 0) {
         var1.setTransform(this.transform);
      }

      if ((var2 & 16) != 0) {
         var1.setViewport(this.viewport);
      }

   }

   public SnapshotParametersBuilder camera(Camera var1) {
      this.camera = var1;
      this.__set |= 1;
      return this;
   }

   public SnapshotParametersBuilder depthBuffer(boolean var1) {
      this.depthBuffer = var1;
      this.__set |= 2;
      return this;
   }

   public SnapshotParametersBuilder fill(Paint var1) {
      this.fill = var1;
      this.__set |= 4;
      return this;
   }

   public SnapshotParametersBuilder transform(Transform var1) {
      this.transform = var1;
      this.__set |= 8;
      return this;
   }

   public SnapshotParametersBuilder viewport(Rectangle2D var1) {
      this.viewport = var1;
      this.__set |= 16;
      return this;
   }

   public SnapshotParameters build() {
      SnapshotParameters var1 = new SnapshotParameters();
      this.applyTo(var1);
      return var1;
   }
}

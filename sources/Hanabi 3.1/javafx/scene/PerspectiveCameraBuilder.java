package javafx.scene;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class PerspectiveCameraBuilder implements Builder {
   private boolean __set;
   private double fieldOfView;

   protected PerspectiveCameraBuilder() {
   }

   public static PerspectiveCameraBuilder create() {
      return new PerspectiveCameraBuilder();
   }

   public void applyTo(PerspectiveCamera var1) {
      if (this.__set) {
         var1.setFieldOfView(this.fieldOfView);
      }

   }

   public PerspectiveCameraBuilder fieldOfView(double var1) {
      this.fieldOfView = var1;
      this.__set = true;
      return this;
   }

   public PerspectiveCamera build() {
      PerspectiveCamera var1 = new PerspectiveCamera();
      this.applyTo(var1);
      return var1;
   }
}

package javafx.scene.layout;

import javafx.geometry.Pos;

/** @deprecated */
@Deprecated
public class VBoxBuilder extends PaneBuilder {
   private int __set;
   private Pos alignment;
   private boolean fillWidth;
   private double spacing;

   protected VBoxBuilder() {
   }

   public static VBoxBuilder create() {
      return new VBoxBuilder();
   }

   public void applyTo(VBox var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAlignment(this.alignment);
      }

      if ((var2 & 2) != 0) {
         var1.setFillWidth(this.fillWidth);
      }

      if ((var2 & 4) != 0) {
         var1.setSpacing(this.spacing);
      }

   }

   public VBoxBuilder alignment(Pos var1) {
      this.alignment = var1;
      this.__set |= 1;
      return this;
   }

   public VBoxBuilder fillWidth(boolean var1) {
      this.fillWidth = var1;
      this.__set |= 2;
      return this;
   }

   public VBoxBuilder spacing(double var1) {
      this.spacing = var1;
      this.__set |= 4;
      return this;
   }

   public VBox build() {
      VBox var1 = new VBox();
      this.applyTo(var1);
      return var1;
   }
}

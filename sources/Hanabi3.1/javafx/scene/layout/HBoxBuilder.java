package javafx.scene.layout;

import javafx.geometry.Pos;

/** @deprecated */
@Deprecated
public class HBoxBuilder extends PaneBuilder {
   private int __set;
   private Pos alignment;
   private boolean fillHeight;
   private double spacing;

   protected HBoxBuilder() {
   }

   public static HBoxBuilder create() {
      return new HBoxBuilder();
   }

   public void applyTo(HBox var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAlignment(this.alignment);
      }

      if ((var2 & 2) != 0) {
         var1.setFillHeight(this.fillHeight);
      }

      if ((var2 & 4) != 0) {
         var1.setSpacing(this.spacing);
      }

   }

   public HBoxBuilder alignment(Pos var1) {
      this.alignment = var1;
      this.__set |= 1;
      return this;
   }

   public HBoxBuilder fillHeight(boolean var1) {
      this.fillHeight = var1;
      this.__set |= 2;
      return this;
   }

   public HBoxBuilder spacing(double var1) {
      this.spacing = var1;
      this.__set |= 4;
      return this;
   }

   public HBox build() {
      HBox var1 = new HBox();
      this.applyTo(var1);
      return var1;
   }
}

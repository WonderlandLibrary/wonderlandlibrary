package javafx.scene.control;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class SeparatorBuilder extends ControlBuilder implements Builder {
   private int __set;
   private HPos halignment;
   private Orientation orientation;
   private VPos valignment;

   protected SeparatorBuilder() {
   }

   public static SeparatorBuilder create() {
      return new SeparatorBuilder();
   }

   public void applyTo(Separator var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setHalignment(this.halignment);
      }

      if ((var2 & 2) != 0) {
         var1.setOrientation(this.orientation);
      }

      if ((var2 & 4) != 0) {
         var1.setValignment(this.valignment);
      }

   }

   public SeparatorBuilder halignment(HPos var1) {
      this.halignment = var1;
      this.__set |= 1;
      return this;
   }

   public SeparatorBuilder orientation(Orientation var1) {
      this.orientation = var1;
      this.__set |= 2;
      return this;
   }

   public SeparatorBuilder valignment(VPos var1) {
      this.valignment = var1;
      this.__set |= 4;
      return this;
   }

   public Separator build() {
      Separator var1 = new Separator();
      this.applyTo(var1);
      return var1;
   }
}

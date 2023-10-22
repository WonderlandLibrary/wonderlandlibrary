package javafx.scene.layout;

import javafx.geometry.HPos;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ColumnConstraintsBuilder implements Builder {
   private int __set;
   private boolean fillWidth;
   private HPos halignment;
   private Priority hgrow;
   private double maxWidth;
   private double minWidth;
   private double percentWidth;
   private double prefWidth;

   protected ColumnConstraintsBuilder() {
   }

   public static ColumnConstraintsBuilder create() {
      return new ColumnConstraintsBuilder();
   }

   public void applyTo(ColumnConstraints var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setFillWidth(this.fillWidth);
      }

      if ((var2 & 2) != 0) {
         var1.setHalignment(this.halignment);
      }

      if ((var2 & 4) != 0) {
         var1.setHgrow(this.hgrow);
      }

      if ((var2 & 8) != 0) {
         var1.setMaxWidth(this.maxWidth);
      }

      if ((var2 & 16) != 0) {
         var1.setMinWidth(this.minWidth);
      }

      if ((var2 & 32) != 0) {
         var1.setPercentWidth(this.percentWidth);
      }

      if ((var2 & 64) != 0) {
         var1.setPrefWidth(this.prefWidth);
      }

   }

   public ColumnConstraintsBuilder fillWidth(boolean var1) {
      this.fillWidth = var1;
      this.__set |= 1;
      return this;
   }

   public ColumnConstraintsBuilder halignment(HPos var1) {
      this.halignment = var1;
      this.__set |= 2;
      return this;
   }

   public ColumnConstraintsBuilder hgrow(Priority var1) {
      this.hgrow = var1;
      this.__set |= 4;
      return this;
   }

   public ColumnConstraintsBuilder maxWidth(double var1) {
      this.maxWidth = var1;
      this.__set |= 8;
      return this;
   }

   public ColumnConstraintsBuilder minWidth(double var1) {
      this.minWidth = var1;
      this.__set |= 16;
      return this;
   }

   public ColumnConstraintsBuilder percentWidth(double var1) {
      this.percentWidth = var1;
      this.__set |= 32;
      return this;
   }

   public ColumnConstraintsBuilder prefWidth(double var1) {
      this.prefWidth = var1;
      this.__set |= 64;
      return this;
   }

   public ColumnConstraints build() {
      ColumnConstraints var1 = new ColumnConstraints();
      this.applyTo(var1);
      return var1;
   }
}

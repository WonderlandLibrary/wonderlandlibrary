package javafx.scene.layout;

import javafx.geometry.VPos;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class RowConstraintsBuilder implements Builder {
   private int __set;
   private boolean fillHeight;
   private double maxHeight;
   private double minHeight;
   private double percentHeight;
   private double prefHeight;
   private VPos valignment;
   private Priority vgrow;

   protected RowConstraintsBuilder() {
   }

   public static RowConstraintsBuilder create() {
      return new RowConstraintsBuilder();
   }

   public void applyTo(RowConstraints var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setFillHeight(this.fillHeight);
      }

      if ((var2 & 2) != 0) {
         var1.setMaxHeight(this.maxHeight);
      }

      if ((var2 & 4) != 0) {
         var1.setMinHeight(this.minHeight);
      }

      if ((var2 & 8) != 0) {
         var1.setPercentHeight(this.percentHeight);
      }

      if ((var2 & 16) != 0) {
         var1.setPrefHeight(this.prefHeight);
      }

      if ((var2 & 32) != 0) {
         var1.setValignment(this.valignment);
      }

      if ((var2 & 64) != 0) {
         var1.setVgrow(this.vgrow);
      }

   }

   public RowConstraintsBuilder fillHeight(boolean var1) {
      this.fillHeight = var1;
      this.__set |= 1;
      return this;
   }

   public RowConstraintsBuilder maxHeight(double var1) {
      this.maxHeight = var1;
      this.__set |= 2;
      return this;
   }

   public RowConstraintsBuilder minHeight(double var1) {
      this.minHeight = var1;
      this.__set |= 4;
      return this;
   }

   public RowConstraintsBuilder percentHeight(double var1) {
      this.percentHeight = var1;
      this.__set |= 8;
      return this;
   }

   public RowConstraintsBuilder prefHeight(double var1) {
      this.prefHeight = var1;
      this.__set |= 16;
      return this;
   }

   public RowConstraintsBuilder valignment(VPos var1) {
      this.valignment = var1;
      this.__set |= 32;
      return this;
   }

   public RowConstraintsBuilder vgrow(Priority var1) {
      this.vgrow = var1;
      this.__set |= 64;
      return this;
   }

   public RowConstraints build() {
      RowConstraints var1 = new RowConstraints();
      this.applyTo(var1);
      return var1;
   }
}

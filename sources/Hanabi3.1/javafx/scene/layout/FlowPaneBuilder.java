package javafx.scene.layout;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

/** @deprecated */
@Deprecated
public class FlowPaneBuilder extends PaneBuilder {
   private int __set;
   private Pos alignment;
   private HPos columnHalignment;
   private double hgap;
   private Orientation orientation;
   private double prefWrapLength;
   private VPos rowValignment;
   private double vgap;

   protected FlowPaneBuilder() {
   }

   public static FlowPaneBuilder create() {
      return new FlowPaneBuilder();
   }

   public void applyTo(FlowPane var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAlignment(this.alignment);
      }

      if ((var2 & 2) != 0) {
         var1.setColumnHalignment(this.columnHalignment);
      }

      if ((var2 & 4) != 0) {
         var1.setHgap(this.hgap);
      }

      if ((var2 & 8) != 0) {
         var1.setOrientation(this.orientation);
      }

      if ((var2 & 16) != 0) {
         var1.setPrefWrapLength(this.prefWrapLength);
      }

      if ((var2 & 32) != 0) {
         var1.setRowValignment(this.rowValignment);
      }

      if ((var2 & 64) != 0) {
         var1.setVgap(this.vgap);
      }

   }

   public FlowPaneBuilder alignment(Pos var1) {
      this.alignment = var1;
      this.__set |= 1;
      return this;
   }

   public FlowPaneBuilder columnHalignment(HPos var1) {
      this.columnHalignment = var1;
      this.__set |= 2;
      return this;
   }

   public FlowPaneBuilder hgap(double var1) {
      this.hgap = var1;
      this.__set |= 4;
      return this;
   }

   public FlowPaneBuilder orientation(Orientation var1) {
      this.orientation = var1;
      this.__set |= 8;
      return this;
   }

   public FlowPaneBuilder prefWrapLength(double var1) {
      this.prefWrapLength = var1;
      this.__set |= 16;
      return this;
   }

   public FlowPaneBuilder rowValignment(VPos var1) {
      this.rowValignment = var1;
      this.__set |= 32;
      return this;
   }

   public FlowPaneBuilder vgap(double var1) {
      this.vgap = var1;
      this.__set |= 64;
      return this;
   }

   public FlowPane build() {
      FlowPane var1 = new FlowPane();
      this.applyTo(var1);
      return var1;
   }
}

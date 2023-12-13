package javafx.scene.layout;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Pos;

/** @deprecated */
@Deprecated
public class GridPaneBuilder extends PaneBuilder {
   private int __set;
   private Pos alignment;
   private Collection columnConstraints;
   private boolean gridLinesVisible;
   private double hgap;
   private Collection rowConstraints;
   private double vgap;

   protected GridPaneBuilder() {
   }

   public static GridPaneBuilder create() {
      return new GridPaneBuilder();
   }

   public void applyTo(GridPane var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAlignment(this.alignment);
      }

      if ((var2 & 2) != 0) {
         var1.getColumnConstraints().addAll(this.columnConstraints);
      }

      if ((var2 & 4) != 0) {
         var1.setGridLinesVisible(this.gridLinesVisible);
      }

      if ((var2 & 8) != 0) {
         var1.setHgap(this.hgap);
      }

      if ((var2 & 16) != 0) {
         var1.getRowConstraints().addAll(this.rowConstraints);
      }

      if ((var2 & 32) != 0) {
         var1.setVgap(this.vgap);
      }

   }

   public GridPaneBuilder alignment(Pos var1) {
      this.alignment = var1;
      this.__set |= 1;
      return this;
   }

   public GridPaneBuilder columnConstraints(Collection var1) {
      this.columnConstraints = var1;
      this.__set |= 2;
      return this;
   }

   public GridPaneBuilder columnConstraints(ColumnConstraints... var1) {
      return this.columnConstraints((Collection)Arrays.asList(var1));
   }

   public GridPaneBuilder gridLinesVisible(boolean var1) {
      this.gridLinesVisible = var1;
      this.__set |= 4;
      return this;
   }

   public GridPaneBuilder hgap(double var1) {
      this.hgap = var1;
      this.__set |= 8;
      return this;
   }

   public GridPaneBuilder rowConstraints(Collection var1) {
      this.rowConstraints = var1;
      this.__set |= 16;
      return this;
   }

   public GridPaneBuilder rowConstraints(RowConstraints... var1) {
      return this.rowConstraints((Collection)Arrays.asList(var1));
   }

   public GridPaneBuilder vgap(double var1) {
      this.vgap = var1;
      this.__set |= 32;
      return this;
   }

   public GridPane build() {
      GridPane var1 = new GridPane();
      this.applyTo(var1);
      return var1;
   }
}

package javafx.scene.layout;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;

/** @deprecated */
@Deprecated
public class TilePaneBuilder extends PaneBuilder {
   private int __set;
   private Pos alignment;
   private double hgap;
   private Orientation orientation;
   private int prefColumns;
   private int prefRows;
   private double prefTileHeight;
   private double prefTileWidth;
   private Pos tileAlignment;
   private double vgap;

   protected TilePaneBuilder() {
   }

   public static TilePaneBuilder create() {
      return new TilePaneBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(TilePane var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setAlignment(this.alignment);
               break;
            case 1:
               var1.setHgap(this.hgap);
               break;
            case 2:
               var1.setOrientation(this.orientation);
               break;
            case 3:
               var1.setPrefColumns(this.prefColumns);
               break;
            case 4:
               var1.setPrefRows(this.prefRows);
               break;
            case 5:
               var1.setPrefTileHeight(this.prefTileHeight);
               break;
            case 6:
               var1.setPrefTileWidth(this.prefTileWidth);
               break;
            case 7:
               var1.setTileAlignment(this.tileAlignment);
               break;
            case 8:
               var1.setVgap(this.vgap);
         }
      }

   }

   public TilePaneBuilder alignment(Pos var1) {
      this.alignment = var1;
      this.__set(0);
      return this;
   }

   public TilePaneBuilder hgap(double var1) {
      this.hgap = var1;
      this.__set(1);
      return this;
   }

   public TilePaneBuilder orientation(Orientation var1) {
      this.orientation = var1;
      this.__set(2);
      return this;
   }

   public TilePaneBuilder prefColumns(int var1) {
      this.prefColumns = var1;
      this.__set(3);
      return this;
   }

   public TilePaneBuilder prefRows(int var1) {
      this.prefRows = var1;
      this.__set(4);
      return this;
   }

   public TilePaneBuilder prefTileHeight(double var1) {
      this.prefTileHeight = var1;
      this.__set(5);
      return this;
   }

   public TilePaneBuilder prefTileWidth(double var1) {
      this.prefTileWidth = var1;
      this.__set(6);
      return this;
   }

   public TilePaneBuilder tileAlignment(Pos var1) {
      this.tileAlignment = var1;
      this.__set(7);
      return this;
   }

   public TilePaneBuilder vgap(double var1) {
      this.vgap = var1;
      this.__set(8);
      return this;
   }

   public TilePane build() {
      TilePane var1 = new TilePane();
      this.applyTo(var1);
      return var1;
   }
}

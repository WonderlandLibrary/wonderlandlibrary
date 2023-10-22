package javafx.scene.layout;

import javafx.geometry.Insets;
import javafx.scene.ParentBuilder;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class RegionBuilder extends ParentBuilder implements Builder {
   private int __set;
   private double maxHeight;
   private double maxWidth;
   private double minHeight;
   private double minWidth;
   private Insets padding;
   private double prefHeight;
   private double prefWidth;
   private boolean snapToPixel;

   protected RegionBuilder() {
   }

   public static RegionBuilder create() {
      return new RegionBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Region var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setMaxHeight(this.maxHeight);
               break;
            case 1:
               var1.setMaxWidth(this.maxWidth);
               break;
            case 2:
               var1.setMinHeight(this.minHeight);
               break;
            case 3:
               var1.setMinWidth(this.minWidth);
               break;
            case 4:
               var1.setPadding(this.padding);
               break;
            case 5:
               var1.setPrefHeight(this.prefHeight);
               break;
            case 6:
               var1.setPrefWidth(this.prefWidth);
               break;
            case 7:
               var1.setSnapToPixel(this.snapToPixel);
         }
      }

   }

   public RegionBuilder maxHeight(double var1) {
      this.maxHeight = var1;
      this.__set(0);
      return this;
   }

   public RegionBuilder maxWidth(double var1) {
      this.maxWidth = var1;
      this.__set(1);
      return this;
   }

   public RegionBuilder minHeight(double var1) {
      this.minHeight = var1;
      this.__set(2);
      return this;
   }

   public RegionBuilder minWidth(double var1) {
      this.minWidth = var1;
      this.__set(3);
      return this;
   }

   public RegionBuilder padding(Insets var1) {
      this.padding = var1;
      this.__set(4);
      return this;
   }

   public RegionBuilder prefHeight(double var1) {
      this.prefHeight = var1;
      this.__set(5);
      return this;
   }

   public RegionBuilder prefWidth(double var1) {
      this.prefWidth = var1;
      this.__set(6);
      return this;
   }

   public RegionBuilder snapToPixel(boolean var1) {
      this.snapToPixel = var1;
      this.__set(7);
      return this;
   }

   public Region build() {
      Region var1 = new Region();
      this.applyTo(var1);
      return var1;
   }
}

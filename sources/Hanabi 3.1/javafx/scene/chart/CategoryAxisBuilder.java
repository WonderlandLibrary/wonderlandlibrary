package javafx.scene.chart;

import javafx.collections.ObservableList;

/** @deprecated */
@Deprecated
public final class CategoryAxisBuilder extends AxisBuilder {
   private int __set;
   private ObservableList categories;
   private double endMargin;
   private boolean gapStartAndEnd;
   private double startMargin;

   protected CategoryAxisBuilder() {
   }

   public static CategoryAxisBuilder create() {
      return new CategoryAxisBuilder();
   }

   public void applyTo(CategoryAxis var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setCategories(this.categories);
      }

      if ((var2 & 2) != 0) {
         var1.setEndMargin(this.endMargin);
      }

      if ((var2 & 4) != 0) {
         var1.setGapStartAndEnd(this.gapStartAndEnd);
      }

      if ((var2 & 8) != 0) {
         var1.setStartMargin(this.startMargin);
      }

   }

   public CategoryAxisBuilder categories(ObservableList var1) {
      this.categories = var1;
      this.__set |= 1;
      return this;
   }

   public CategoryAxisBuilder endMargin(double var1) {
      this.endMargin = var1;
      this.__set |= 2;
      return this;
   }

   public CategoryAxisBuilder gapStartAndEnd(boolean var1) {
      this.gapStartAndEnd = var1;
      this.__set |= 4;
      return this;
   }

   public CategoryAxisBuilder startMargin(double var1) {
      this.startMargin = var1;
      this.__set |= 8;
      return this;
   }

   public CategoryAxis build() {
      CategoryAxis var1 = new CategoryAxis();
      this.applyTo(var1);
      return var1;
   }
}

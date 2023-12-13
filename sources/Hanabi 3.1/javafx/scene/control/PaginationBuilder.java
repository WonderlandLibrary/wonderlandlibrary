package javafx.scene.control;

import javafx.util.Builder;
import javafx.util.Callback;

/** @deprecated */
@Deprecated
public class PaginationBuilder extends ControlBuilder implements Builder {
   private int __set;
   private int currentPageIndex;
   private int maxPageIndicatorCount;
   private int pageCount;
   private Callback pageFactory;

   protected PaginationBuilder() {
   }

   public static PaginationBuilder create() {
      return new PaginationBuilder();
   }

   public void applyTo(Pagination var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setCurrentPageIndex(this.currentPageIndex);
      }

      if ((var2 & 2) != 0) {
         var1.setMaxPageIndicatorCount(this.maxPageIndicatorCount);
      }

      if ((var2 & 4) != 0) {
         var1.setPageCount(this.pageCount);
      }

      if ((var2 & 8) != 0) {
         var1.setPageFactory(this.pageFactory);
      }

   }

   public PaginationBuilder currentPageIndex(int var1) {
      this.currentPageIndex = var1;
      this.__set |= 1;
      return this;
   }

   public PaginationBuilder maxPageIndicatorCount(int var1) {
      this.maxPageIndicatorCount = var1;
      this.__set |= 2;
      return this;
   }

   public PaginationBuilder pageCount(int var1) {
      this.pageCount = var1;
      this.__set |= 4;
      return this;
   }

   public PaginationBuilder pageFactory(Callback var1) {
      this.pageFactory = var1;
      this.__set |= 8;
      return this;
   }

   public Pagination build() {
      Pagination var1 = new Pagination();
      this.applyTo(var1);
      return var1;
   }
}

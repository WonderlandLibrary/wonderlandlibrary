package javafx.print;

import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

public final class PageRange {
   private ReadOnlyIntegerWrapper startPage;
   private ReadOnlyIntegerWrapper endPage;

   public PageRange(@NamedArg("startPage") int var1, @NamedArg("endPage") int var2) {
      if (var1 > 0 && var1 <= var2) {
         this.startPageImplProperty().set(var1);
         this.endPageImplProperty().set(var2);
      } else {
         throw new IllegalArgumentException("Invalid range : " + var1 + " -> " + var2);
      }
   }

   private ReadOnlyIntegerWrapper startPageImplProperty() {
      if (this.startPage == null) {
         this.startPage = new ReadOnlyIntegerWrapper(this, "startPage", 1) {
            public void set(int var1) {
               if (var1 > 0 && (PageRange.this.endPage == null || var1 >= PageRange.this.endPage.get())) {
                  super.set(var1);
               }
            }
         };
      }

      return this.startPage;
   }

   public ReadOnlyIntegerProperty startPageProperty() {
      return this.startPageImplProperty().getReadOnlyProperty();
   }

   public int getStartPage() {
      return this.startPageProperty().get();
   }

   private ReadOnlyIntegerWrapper endPageImplProperty() {
      if (this.endPage == null) {
         this.endPage = new ReadOnlyIntegerWrapper(this, "endPage", 9999) {
            public void set(int var1) {
               if (var1 > 0 && (PageRange.this.startPage == null || var1 >= PageRange.this.startPage.get())) {
                  super.set(var1);
               }
            }
         };
      }

      return this.endPage;
   }

   public ReadOnlyIntegerProperty endPageProperty() {
      return this.endPageImplProperty().getReadOnlyProperty();
   }

   public int getEndPage() {
      return this.endPageProperty().get();
   }
}

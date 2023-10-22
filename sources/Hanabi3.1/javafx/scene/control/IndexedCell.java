package javafx.scene.control;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.css.PseudoClass;

public class IndexedCell extends Cell {
   private ReadOnlyIntegerWrapper index = new ReadOnlyIntegerWrapper(this, "index", -1) {
      protected void invalidated() {
         boolean var1 = this.get() % 2 == 0;
         IndexedCell.this.pseudoClassStateChanged(IndexedCell.PSEUDO_CLASS_EVEN, var1);
         IndexedCell.this.pseudoClassStateChanged(IndexedCell.PSEUDO_CLASS_ODD, !var1);
      }
   };
   private static final String DEFAULT_STYLE_CLASS = "indexed-cell";
   private static final PseudoClass PSEUDO_CLASS_ODD = PseudoClass.getPseudoClass("odd");
   private static final PseudoClass PSEUDO_CLASS_EVEN = PseudoClass.getPseudoClass("even");

   public IndexedCell() {
      this.getStyleClass().addAll("indexed-cell");
   }

   public final int getIndex() {
      return this.index.get();
   }

   public final ReadOnlyIntegerProperty indexProperty() {
      return this.index.getReadOnlyProperty();
   }

   public void updateIndex(int var1) {
      int var2 = this.index.get();
      this.index.set(var1);
      this.indexChanged(var2, var1);
   }

   void indexChanged(int var1, int var2) {
   }
}

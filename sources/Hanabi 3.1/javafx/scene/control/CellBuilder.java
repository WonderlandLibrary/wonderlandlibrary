package javafx.scene.control;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class CellBuilder extends LabeledBuilder implements Builder {
   private int __set;
   private boolean editable;
   private Object item;

   protected CellBuilder() {
   }

   public static CellBuilder create() {
      return new CellBuilder();
   }

   public void applyTo(Cell var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setEditable(this.editable);
      }

      if ((var2 & 2) != 0) {
         var1.setItem(this.item);
      }

   }

   public CellBuilder editable(boolean var1) {
      this.editable = var1;
      this.__set |= 1;
      return this;
   }

   public CellBuilder item(Object var1) {
      this.item = var1;
      this.__set |= 2;
      return this;
   }

   public Cell build() {
      Cell var1 = new Cell();
      this.applyTo(var1);
      return var1;
   }
}

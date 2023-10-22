package javafx.scene.control;

import java.lang.ref.WeakReference;

public abstract class TablePositionBase {
   private final int row;
   private final WeakReference tableColumnRef;

   protected TablePositionBase(int var1, TableColumnBase var2) {
      this.row = var1;
      this.tableColumnRef = new WeakReference(var2);
   }

   public int getRow() {
      return this.row;
   }

   public abstract int getColumn();

   public TableColumnBase getTableColumn() {
      return (TableColumnBase)this.tableColumnRef.get();
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         TablePositionBase var2 = (TablePositionBase)var1;
         if (this.row != var2.row) {
            return false;
         } else {
            TableColumnBase var3 = this.getTableColumn();
            TableColumnBase var4 = var2.getTableColumn();
            return var3 == var4 || var3 != null && var3.equals(var4);
         }
      }
   }

   public int hashCode() {
      int var1 = 5;
      var1 = 79 * var1 + this.row;
      TableColumnBase var2 = this.getTableColumn();
      var1 = 79 * var1 + (var2 != null ? var2.hashCode() : 0);
      return var1;
   }
}

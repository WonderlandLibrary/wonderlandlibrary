package javafx.scene.control;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.collections.ObservableList;

public class TablePosition extends TablePositionBase {
   private final WeakReference controlRef;
   private final WeakReference itemRef;
   int fixedColumnIndex = -1;

   public TablePosition(@NamedArg("tableView") TableView var1, @NamedArg("row") int var2, @NamedArg("tableColumn") TableColumn var3) {
      super(var2, var3);
      this.controlRef = new WeakReference(var1);
      ObservableList var4 = var1.getItems();
      this.itemRef = new WeakReference(var4 != null && var2 >= 0 && var2 < var4.size() ? var4.get(var2) : null);
   }

   public int getColumn() {
      if (this.fixedColumnIndex > -1) {
         return this.fixedColumnIndex;
      } else {
         TableView var1 = this.getTableView();
         TableColumn var2 = this.getTableColumn();
         return var1 != null && var2 != null ? var1.getVisibleLeafIndex(var2) : -1;
      }
   }

   public final TableView getTableView() {
      return (TableView)this.controlRef.get();
   }

   public final TableColumn getTableColumn() {
      return (TableColumn)super.getTableColumn();
   }

   final Object getItem() {
      return this.itemRef == null ? null : this.itemRef.get();
   }

   public String toString() {
      return "TablePosition [ row: " + this.getRow() + ", column: " + this.getTableColumn() + ", tableView: " + this.getTableView() + " ]";
   }
}

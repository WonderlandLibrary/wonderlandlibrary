package javafx.scene.control;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;

public class TreeTablePosition extends TablePositionBase {
   private final WeakReference controlRef;
   private final WeakReference treeItemRef;
   int fixedColumnIndex = -1;

   public TreeTablePosition(@NamedArg("treeTableView") TreeTableView var1, @NamedArg("row") int var2, @NamedArg("tableColumn") TreeTableColumn var3) {
      super(var2, var3);
      this.controlRef = new WeakReference(var1);
      this.treeItemRef = new WeakReference(var1.getTreeItem(var2));
   }

   TreeTablePosition(@NamedArg("treeTableView") TreeTablePosition var1, @NamedArg("row") int var2) {
      super(var2, var1.getTableColumn());
      this.controlRef = new WeakReference(var1.getTreeTableView());
      this.treeItemRef = new WeakReference(var1.getTreeItem());
   }

   public int getColumn() {
      if (this.fixedColumnIndex > -1) {
         return this.fixedColumnIndex;
      } else {
         TreeTableView var1 = this.getTreeTableView();
         TreeTableColumn var2 = this.getTableColumn();
         return var1 != null && var2 != null ? var1.getVisibleLeafIndex(var2) : -1;
      }
   }

   public final TreeTableView getTreeTableView() {
      return (TreeTableView)this.controlRef.get();
   }

   public final TreeTableColumn getTableColumn() {
      return (TreeTableColumn)super.getTableColumn();
   }

   public final TreeItem getTreeItem() {
      return (TreeItem)this.treeItemRef.get();
   }
}

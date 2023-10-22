package javafx.scene.control.cell;

import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;

class DefaultTreeCell extends TreeCell {
   private HBox hbox;
   private WeakReference treeItemRef;
   private InvalidationListener treeItemGraphicListener = (var1) -> {
      this.updateDisplay(this.getItem(), this.isEmpty());
   };
   private InvalidationListener treeItemListener = new InvalidationListener() {
      public void invalidated(Observable var1) {
         TreeItem var2 = DefaultTreeCell.this.treeItemRef == null ? null : (TreeItem)DefaultTreeCell.this.treeItemRef.get();
         if (var2 != null) {
            var2.graphicProperty().removeListener(DefaultTreeCell.this.weakTreeItemGraphicListener);
         }

         TreeItem var3 = DefaultTreeCell.this.getTreeItem();
         if (var3 != null) {
            var3.graphicProperty().addListener(DefaultTreeCell.this.weakTreeItemGraphicListener);
            DefaultTreeCell.this.treeItemRef = new WeakReference(var3);
         }

      }
   };
   private WeakInvalidationListener weakTreeItemGraphicListener;
   private WeakInvalidationListener weakTreeItemListener;

   public DefaultTreeCell() {
      this.weakTreeItemGraphicListener = new WeakInvalidationListener(this.treeItemGraphicListener);
      this.weakTreeItemListener = new WeakInvalidationListener(this.treeItemListener);
      this.treeItemProperty().addListener(this.weakTreeItemListener);
      if (this.getTreeItem() != null) {
         this.getTreeItem().graphicProperty().addListener(this.weakTreeItemGraphicListener);
      }

   }

   void updateDisplay(Object var1, boolean var2) {
      if (var1 != null && !var2) {
         TreeItem var3 = this.getTreeItem();
         if (var3 != null && var3.getGraphic() != null) {
            if (var1 instanceof Node) {
               this.setText((String)null);
               if (this.hbox == null) {
                  this.hbox = new HBox(3.0);
               }

               this.hbox.getChildren().setAll((Object[])(var3.getGraphic(), (Node)var1));
               this.setGraphic(this.hbox);
            } else {
               this.hbox = null;
               this.setText(var1.toString());
               this.setGraphic(var3.getGraphic());
            }
         } else {
            this.hbox = null;
            if (var1 instanceof Node) {
               this.setText((String)null);
               this.setGraphic((Node)var1);
            } else {
               this.setText(var1.toString());
               this.setGraphic((Node)null);
            }
         }
      } else {
         this.hbox = null;
         this.setText((String)null);
         this.setGraphic((Node)null);
      }

   }

   public void updateItem(Object var1, boolean var2) {
      super.updateItem(var1, var2);
      this.updateDisplay(var1, var2);
   }
}

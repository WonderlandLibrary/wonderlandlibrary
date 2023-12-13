package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TreeTableRowBehavior;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;

public class TreeTableRowSkin extends TableRowSkinBase {
   private SimpleObjectProperty itemsProperty;
   private TreeItem treeItem;
   private boolean disclosureNodeDirty = true;
   private Node graphic;
   private TreeTableViewSkin treeTableViewSkin;
   private boolean childrenDirty = false;
   private MultiplePropertyChangeListenerHandler treeItemListener = new MultiplePropertyChangeListenerHandler((var1x) -> {
      if ("GRAPHIC".equals(var1x)) {
         this.disclosureNodeDirty = true;
         ((TreeTableRow)this.getSkinnable()).requestLayout();
      }

      return null;
   });
   private DoubleProperty indent = null;

   public TreeTableRowSkin(TreeTableRow var1) {
      super(var1, new TreeTableRowBehavior(var1));
      super.init(var1);
      this.updateTreeItem();
      this.updateTableViewSkin();
      this.registerChangeListener(var1.treeTableViewProperty(), "TREE_TABLE_VIEW");
      this.registerChangeListener(var1.indexProperty(), "INDEX");
      this.registerChangeListener(var1.treeItemProperty(), "TREE_ITEM");
      this.registerChangeListener(var1.getTreeTableView().treeColumnProperty(), "TREE_COLUMN");
   }

   public final void setIndent(double var1) {
      this.indentProperty().set(var1);
   }

   public final double getIndent() {
      return this.indent == null ? 10.0 : this.indent.get();
   }

   public final DoubleProperty indentProperty() {
      if (this.indent == null) {
         this.indent = new StyleableDoubleProperty(10.0) {
            public Object getBean() {
               return TreeTableRowSkin.this;
            }

            public String getName() {
               return "indent";
            }

            public CssMetaData getCssMetaData() {
               return TreeTableRowSkin.StyleableProperties.INDENT;
            }
         };
      }

      return this.indent;
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("TREE_ABLE_VIEW".equals(var1)) {
         this.updateTableViewSkin();
      } else if ("INDEX".equals(var1)) {
         this.updateCells = true;
      } else if ("TREE_ITEM".equals(var1)) {
         this.updateTreeItem();
         this.isDirty = true;
      } else if ("TREE_COLUMN".equals(var1)) {
         this.isDirty = true;
         ((TreeTableRow)this.getSkinnable()).requestLayout();
      }

   }

   protected void updateChildren() {
      super.updateChildren();
      this.updateDisclosureNodeAndGraphic();
      if (this.childrenDirty) {
         this.childrenDirty = false;
         if (this.cells.isEmpty()) {
            this.getChildren().clear();
         } else {
            this.getChildren().addAll(this.cells);
         }
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      if (this.disclosureNodeDirty) {
         this.updateDisclosureNodeAndGraphic();
         this.disclosureNodeDirty = false;
      }

      Node var9 = this.getDisclosureNode();
      if (var9 != null && var9.getScene() == null) {
         this.updateDisclosureNodeAndGraphic();
      }

      super.layoutChildren(var1, var3, var5, var7);
   }

   protected TreeTableCell getCell(TableColumnBase var1) {
      TreeTableColumn var2 = (TreeTableColumn)var1;
      TreeTableCell var3 = (TreeTableCell)var2.getCellFactory().call(var2);
      var3.updateTreeTableColumn(var2);
      var3.updateTreeTableView(var2.getTreeTableView());
      return var3;
   }

   protected void updateCells(boolean var1) {
      super.updateCells(var1);
      if (var1) {
         this.childrenDirty = true;
         this.updateChildren();
      }

   }

   protected boolean isIndentationRequired() {
      return true;
   }

   protected TableColumnBase getTreeColumn() {
      return ((TreeTableRow)this.getSkinnable()).getTreeTableView().getTreeColumn();
   }

   protected int getIndentationLevel(TreeTableRow var1) {
      return var1.getTreeTableView().getTreeItemLevel(var1.getTreeItem());
   }

   protected double getIndentationPerLevel() {
      return this.getIndent();
   }

   protected Node getDisclosureNode() {
      return ((TreeTableRow)this.getSkinnable()).getDisclosureNode();
   }

   protected boolean isDisclosureNodeVisible() {
      return this.getDisclosureNode() != null && this.treeItem != null && !this.treeItem.isLeaf();
   }

   protected boolean isShowRoot() {
      return ((TreeTableRow)this.getSkinnable()).getTreeTableView().isShowRoot();
   }

   protected ObservableList getVisibleLeafColumns() {
      return ((TreeTableRow)this.getSkinnable()).getTreeTableView().getVisibleLeafColumns();
   }

   protected void updateCell(TreeTableCell var1, TreeTableRow var2) {
      var1.updateTreeTableRow(var2);
   }

   protected boolean isColumnPartiallyOrFullyVisible(TableColumnBase var1) {
      return this.treeTableViewSkin == null ? false : this.treeTableViewSkin.isColumnPartiallyOrFullyVisible(var1);
   }

   protected TreeTableColumn getTableColumnBase(TreeTableCell var1) {
      return var1.getTableColumn();
   }

   protected ObjectProperty graphicProperty() {
      TreeTableRow var1 = (TreeTableRow)this.getSkinnable();
      if (var1 == null) {
         return null;
      } else {
         return this.treeItem == null ? null : this.treeItem.graphicProperty();
      }
   }

   protected Control getVirtualFlowOwner() {
      return ((TreeTableRow)this.getSkinnable()).getTreeTableView();
   }

   protected DoubleProperty fixedCellSizeProperty() {
      return ((TreeTableRow)this.getSkinnable()).getTreeTableView().fixedCellSizeProperty();
   }

   private void updateTreeItem() {
      if (this.treeItem != null) {
         this.treeItemListener.unregisterChangeListener(this.treeItem.expandedProperty());
         this.treeItemListener.unregisterChangeListener(this.treeItem.graphicProperty());
      }

      this.treeItem = ((TreeTableRow)this.getSkinnable()).getTreeItem();
      if (this.treeItem != null) {
         this.treeItemListener.registerChangeListener(this.treeItem.graphicProperty(), "GRAPHIC");
      }

   }

   private void updateDisclosureNodeAndGraphic() {
      if (!((TreeTableRow)this.getSkinnable()).isEmpty()) {
         ObjectProperty var1 = this.graphicProperty();
         Node var2 = var1 == null ? null : (Node)var1.get();
         if (var2 != null) {
            if (var2 != this.graphic) {
               this.getChildren().remove(this.graphic);
            }

            if (!this.getChildren().contains(var2)) {
               this.getChildren().add(var2);
               this.graphic = var2;
            }
         }

         Node var3 = ((TreeTableRow)this.getSkinnable()).getDisclosureNode();
         if (var3 != null) {
            boolean var4 = this.treeItem != null && !this.treeItem.isLeaf();
            var3.setVisible(var4);
            if (!var4) {
               this.getChildren().remove(var3);
            } else if (var3.getParent() == null) {
               this.getChildren().add(var3);
               var3.toFront();
            } else {
               var3.toBack();
            }

            if (var3.getScene() != null) {
               var3.applyCss();
            }
         }

      }
   }

   private void updateTableViewSkin() {
      TreeTableView var1 = ((TreeTableRow)this.getSkinnable()).getTreeTableView();
      if (var1.getSkin() instanceof TreeTableViewSkin) {
         this.treeTableViewSkin = (TreeTableViewSkin)var1.getSkin();
      }

   }

   public static List getClassCssMetaData() {
      return TreeTableRowSkin.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      TreeTableView var3 = ((TreeTableRow)this.getSkinnable()).getTreeTableView();
      switch (var1) {
         case SELECTED_ITEMS:
            ArrayList var10 = new ArrayList();
            int var12 = ((TreeTableRow)this.getSkinnable()).getIndex();
            Iterator var14 = var3.getSelectionModel().getSelectedCells().iterator();
            if (var14.hasNext()) {
               TreeTablePosition var7 = (TreeTablePosition)var14.next();
               if (var7.getRow() == var12) {
                  TreeTableColumn var8 = var7.getTableColumn();
                  if (var8 == null) {
                     var8 = var3.getVisibleLeafColumn(0);
                  }

                  TreeTableCell var9 = (TreeTableCell)((Reference)this.cellsMap.get(var8)).get();
                  if (var9 != null) {
                     var10.add(var9);
                  }
               }

               return FXCollections.observableArrayList((Collection)var10);
            }
         case CELL_AT_ROW_COLUMN:
            int var11 = (Integer)var2[1];
            TreeTableColumn var13 = var3.getVisibleLeafColumn(var11);
            if (this.cellsMap.containsKey(var13)) {
               return ((Reference)this.cellsMap.get(var13)).get();
            }

            return null;
         case FOCUS_ITEM:
            TreeTableView.TreeTableViewFocusModel var4 = var3.getFocusModel();
            TreeTablePosition var5 = var4.getFocusedCell();
            TreeTableColumn var6 = var5.getTableColumn();
            if (var6 == null) {
               var6 = var3.getVisibleLeafColumn(0);
            }

            if (this.cellsMap.containsKey(var6)) {
               return ((Reference)this.cellsMap.get(var6)).get();
            }

            return null;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData INDENT = new CssMetaData("-fx-indent", SizeConverter.getInstance(), 10.0) {
         public boolean isSettable(TreeTableRow var1) {
            DoubleProperty var2 = ((TreeTableRowSkin)var1.getSkin()).indentProperty();
            return var2 == null || !var2.isBound();
         }

         public StyleableProperty getStyleableProperty(TreeTableRow var1) {
            TreeTableRowSkin var2 = (TreeTableRowSkin)var1.getSkin();
            return (StyleableProperty)var2.indentProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(CellSkinBase.getClassCssMetaData());
         var0.add(INDENT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}

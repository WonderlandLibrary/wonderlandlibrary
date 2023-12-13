package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TreeCellBehavior;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.beans.property.DoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TreeCellSkin extends CellSkinBase {
   private static final Map maxDisclosureWidthMap = new WeakHashMap();
   private DoubleProperty indent = null;
   private boolean disclosureNodeDirty = true;
   private TreeItem treeItem;
   private double fixedCellSize;
   private boolean fixedCellSizeEnabled;
   private MultiplePropertyChangeListenerHandler treeItemListener = new MultiplePropertyChangeListenerHandler((var1x) -> {
      if ("EXPANDED".equals(var1x)) {
         this.updateDisclosureNodeRotation(true);
      }

      return null;
   });

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
               return TreeCellSkin.this;
            }

            public String getName() {
               return "indent";
            }

            public CssMetaData getCssMetaData() {
               return TreeCellSkin.StyleableProperties.INDENT;
            }
         };
      }

      return this.indent;
   }

   public TreeCellSkin(TreeCell var1) {
      super(var1, new TreeCellBehavior(var1));
      this.fixedCellSize = var1.getTreeView().getFixedCellSize();
      this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
      this.updateTreeItem();
      this.updateDisclosureNodeRotation(false);
      this.registerChangeListener(var1.treeItemProperty(), "TREE_ITEM");
      this.registerChangeListener(var1.textProperty(), "TEXT");
      this.registerChangeListener(var1.getTreeView().fixedCellSizeProperty(), "FIXED_CELL_SIZE");
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("TREE_ITEM".equals(var1)) {
         this.updateTreeItem();
         this.disclosureNodeDirty = true;
         ((TreeCell)this.getSkinnable()).requestLayout();
      } else if ("TEXT".equals(var1)) {
         ((TreeCell)this.getSkinnable()).requestLayout();
      } else if ("FIXED_CELL_SIZE".equals(var1)) {
         this.fixedCellSize = ((TreeCell)this.getSkinnable()).getTreeView().getFixedCellSize();
         this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
      }

   }

   private void updateDisclosureNodeRotation(boolean var1) {
   }

   private void updateTreeItem() {
      if (this.treeItem != null) {
         this.treeItemListener.unregisterChangeListener(this.treeItem.expandedProperty());
      }

      this.treeItem = ((TreeCell)this.getSkinnable()).getTreeItem();
      if (this.treeItem != null) {
         this.treeItemListener.registerChangeListener(this.treeItem.expandedProperty(), "EXPANDED");
      }

      this.updateDisclosureNodeRotation(false);
   }

   private void updateDisclosureNode() {
      if (!((TreeCell)this.getSkinnable()).isEmpty()) {
         Node var1 = ((TreeCell)this.getSkinnable()).getDisclosureNode();
         if (var1 != null) {
            boolean var2 = this.treeItem != null && !this.treeItem.isLeaf();
            var1.setVisible(var2);
            if (!var2) {
               this.getChildren().remove(var1);
            } else if (var1.getParent() == null) {
               this.getChildren().add(var1);
               var1.toFront();
            } else {
               var1.toBack();
            }

            if (var1.getScene() != null) {
               var1.applyCss();
            }

         }
      }
   }

   protected void updateChildren() {
      super.updateChildren();
      this.updateDisclosureNode();
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      TreeView var9 = ((TreeCell)this.getSkinnable()).getTreeView();
      if (var9 != null) {
         if (this.disclosureNodeDirty) {
            this.updateDisclosureNode();
            this.disclosureNodeDirty = false;
         }

         Node var10 = ((TreeCell)this.getSkinnable()).getDisclosureNode();
         int var11 = var9.getTreeItemLevel(this.treeItem);
         if (!var9.isShowRoot()) {
            --var11;
         }

         double var12 = this.getIndent() * (double)var11;
         var1 += var12;
         boolean var14 = var10 != null && this.treeItem != null && !this.treeItem.isLeaf();
         double var15 = maxDisclosureWidthMap.containsKey(var9) ? (Double)maxDisclosureWidthMap.get(var9) : 18.0;
         double var17 = var15;
         if (var14) {
            if (var10 == null || var10.getScene() == null) {
               this.updateChildren();
            }

            if (var10 != null) {
               var17 = var10.prefWidth(var7);
               if (var17 > var15) {
                  maxDisclosureWidthMap.put(var9, var17);
               }

               double var19 = var10.prefHeight(var17);
               var10.resize(var17, var19);
               this.positionInArea(var10, var1, var3, var17, var19, 0.0, HPos.CENTER, VPos.CENTER);
            }
         }

         int var21 = this.treeItem != null && this.treeItem.getGraphic() == null ? 0 : 3;
         var1 += var17 + (double)var21;
         var5 -= var12 + var17 + (double)var21;
         Node var20 = ((TreeCell)this.getSkinnable()).getGraphic();
         if (var20 != null && !this.getChildren().contains(var20)) {
            this.getChildren().add(var20);
         }

         this.layoutLabelInArea(var1, var3, var5, var7);
      }
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      if (this.fixedCellSizeEnabled) {
         return this.fixedCellSize;
      } else {
         double var11 = super.computeMinHeight(var1, var3, var5, var7, var9);
         Node var13 = ((TreeCell)this.getSkinnable()).getDisclosureNode();
         return var13 == null ? var11 : Math.max(var13.minHeight(-1.0), var11);
      }
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      if (this.fixedCellSizeEnabled) {
         return this.fixedCellSize;
      } else {
         TreeCell var11 = (TreeCell)this.getSkinnable();
         double var12 = super.computePrefHeight(var1, var3, var5, var7, var9);
         Node var14 = var11.getDisclosureNode();
         double var15 = var14 == null ? var12 : Math.max(var14.prefHeight(-1.0), var12);
         return this.snapSize(Math.max(var11.getMinHeight(), var15));
      }
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return this.fixedCellSizeEnabled ? this.fixedCellSize : super.computeMaxHeight(var1, var3, var5, var7, var9);
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = super.computePrefWidth(var1, var3, var5, var7, var9);
      double var13 = this.snappedLeftInset() + this.snappedRightInset();
      TreeView var15 = ((TreeCell)this.getSkinnable()).getTreeView();
      if (var15 == null) {
         return var13;
      } else if (this.treeItem == null) {
         return var13;
      } else {
         int var16 = var15.getTreeItemLevel(this.treeItem);
         if (!var15.isShowRoot()) {
            --var16;
         }

         var13 = var11 + this.getIndent() * (double)var16;
         Node var17 = ((TreeCell)this.getSkinnable()).getDisclosureNode();
         double var18 = var17 == null ? 0.0 : var17.prefWidth(-1.0);
         double var20 = maxDisclosureWidthMap.containsKey(var15) ? (Double)maxDisclosureWidthMap.get(var15) : 0.0;
         var13 += Math.max(var20, var18);
         return var13;
      }
   }

   public static List getClassCssMetaData() {
      return TreeCellSkin.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData INDENT = new CssMetaData("-fx-indent", SizeConverter.getInstance(), 10.0) {
         public boolean isSettable(TreeCell var1) {
            DoubleProperty var2 = ((TreeCellSkin)var1.getSkin()).indentProperty();
            return var2 == null || !var2.isBound();
         }

         public StyleableProperty getStyleableProperty(TreeCell var1) {
            TreeCellSkin var2 = (TreeCellSkin)var1.getSkin();
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

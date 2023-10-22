package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumnBase;
import javafx.util.Duration;

public abstract class TableRowSkinBase extends CellSkinBase {
   private static boolean IS_STUB_TOOLKIT = Toolkit.getToolkit().toString().contains("StubToolkit");
   private static boolean DO_ANIMATIONS;
   private static final Duration FADE_DURATION;
   static final Map maxDisclosureWidthMap;
   private static final int DEFAULT_FULL_REFRESH_COUNTER = 100;
   protected WeakHashMap cellsMap;
   protected final List cells = new ArrayList();
   private int fullRefreshCounter = 100;
   protected boolean isDirty = false;
   protected boolean updateCells = false;
   private double fixedCellSize;
   private boolean fixedCellSizeEnabled;
   private ListChangeListener visibleLeafColumnsListener = (var1x) -> {
      this.isDirty = true;
      ((IndexedCell)this.getSkinnable()).requestLayout();
   };
   private WeakListChangeListener weakVisibleLeafColumnsListener;

   public TableRowSkinBase(IndexedCell var1, CellBehaviorBase var2) {
      super(var1, var2);
      this.weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
   }

   protected void init(IndexedCell var1) {
      ((IndexedCell)this.getSkinnable()).setPickOnBounds(false);
      this.recreateCells();
      this.updateCells(true);
      this.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
      var1.itemProperty().addListener((var1x) -> {
         this.requestCellUpdate();
      });
      this.registerChangeListener(var1.indexProperty(), "INDEX");
      if (this.fixedCellSizeProperty() != null) {
         this.registerChangeListener(this.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
         this.fixedCellSize = this.fixedCellSizeProperty().get();
         this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
      }

   }

   protected abstract ObjectProperty graphicProperty();

   protected abstract Control getVirtualFlowOwner();

   protected abstract ObservableList getVisibleLeafColumns();

   protected abstract void updateCell(IndexedCell var1, IndexedCell var2);

   protected abstract DoubleProperty fixedCellSizeProperty();

   protected abstract boolean isColumnPartiallyOrFullyVisible(TableColumnBase var1);

   protected abstract IndexedCell getCell(TableColumnBase var1);

   protected abstract TableColumnBase getTableColumnBase(IndexedCell var1);

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("INDEX".equals(var1)) {
         if (((IndexedCell)this.getSkinnable()).isEmpty()) {
            this.requestCellUpdate();
         }
      } else if ("FIXED_CELL_SIZE".equals(var1)) {
         this.fixedCellSize = this.fixedCellSizeProperty().get();
         this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      this.checkState();
      if (!this.cellsMap.isEmpty()) {
         ObservableList var9 = this.getVisibleLeafColumns();
         if (var9.isEmpty()) {
            super.layoutChildren(var1, var3, var5, var7);
         } else {
            IndexedCell var10 = (IndexedCell)this.getSkinnable();
            double var11 = 0.0;
            double var13 = 0.0;
            double var15 = 0.0;
            boolean var17 = this.isIndentationRequired();
            boolean var18 = this.isDisclosureNodeVisible();
            int var19 = 0;
            Node var20 = null;
            double var23;
            if (var17) {
               TableColumnBase var21 = this.getTreeColumn();
               var19 = var21 == null ? 0 : var9.indexOf(var21);
               var19 = var19 < 0 ? 0 : var19;
               int var22 = this.getIndentationLevel(var10);
               if (!this.isShowRoot()) {
                  --var22;
               }

               var23 = this.getIndentationPerLevel();
               var11 = (double)var22 * var23;
               Control var25 = this.getVirtualFlowOwner();
               double var26 = maxDisclosureWidthMap.containsKey(var25) ? (Double)maxDisclosureWidthMap.get(var25) : 0.0;
               var13 = var26;
               var20 = this.getDisclosureNode();
               if (var20 != null) {
                  var20.setVisible(var18);
                  if (var18) {
                     var13 = var20.prefWidth(var7);
                     if (var13 > var26) {
                        maxDisclosureWidthMap.put(var25, var13);
                        VirtualFlow var28 = this.getVirtualFlow();
                        int var29 = ((IndexedCell)this.getSkinnable()).getIndex();

                        for(int var30 = 0; var30 < var28.cells.size(); ++var30) {
                           IndexedCell var31 = (IndexedCell)var28.cells.get(var30);
                           if (var31 != null && !var31.isEmpty()) {
                              var31.requestLayout();
                              var31.layout();
                           }
                        }
                     }
                  }
               }
            }

            double var44 = this.snappedTopInset() + this.snappedBottomInset();
            double var27 = this.snappedLeftInset() + this.snappedRightInset();
            double var45 = var10.getHeight();
            int var46 = var10.getIndex();
            if (var46 >= 0) {
               int var32 = 0;

               for(int var33 = this.cells.size(); var32 < var33; ++var32) {
                  IndexedCell var34 = (IndexedCell)this.cells.get(var32);
                  TableColumnBase var35 = this.getTableColumnBase(var34);
                  boolean var36 = true;
                  if (this.fixedCellSizeEnabled) {
                     var36 = this.isColumnPartiallyOrFullyVisible(var35);
                     var23 = this.fixedCellSize;
                  } else {
                     var23 = Math.max(var45, var34.prefHeight(-1.0));
                     var23 = this.snapSize(var23) - this.snapSize(var44);
                  }

                  double var43;
                  if (var36) {
                     if (this.fixedCellSizeEnabled && var34.getParent() == null) {
                        this.getChildren().add(var34);
                     }

                     var43 = this.snapSize(var34.prefWidth(-1.0)) - this.snapSize(var27);
                     boolean var37 = var7 <= 24.0;
                     StyleOrigin var38 = ((StyleableObjectProperty)var34.alignmentProperty()).getStyleOrigin();
                     if (!var37 && var38 == null) {
                        var34.setAlignment(Pos.TOP_LEFT);
                     }

                     if (var17 && var32 == var19) {
                        if (var18) {
                           double var39 = var20.prefHeight(var13);
                           if (var43 > 0.0 && var43 < var13 + var11) {
                              this.fadeOut(var20);
                           } else {
                              this.fadeIn(var20);
                              var20.resize(var13, var39);
                              var20.relocate(var1 + var11, var37 ? var7 / 2.0 - var39 / 2.0 : var3 + var34.getPadding().getTop());
                              var20.toFront();
                           }
                        }

                        ObjectProperty var47 = this.graphicProperty();
                        Node var40 = var47 == null ? null : (Node)var47.get();
                        if (var40 != null) {
                           var15 = var40.prefWidth(-1.0) + 3.0;
                           double var41 = var40.prefHeight(var15);
                           if (var43 > 0.0 && var43 < var13 + var11 + var15) {
                              this.fadeOut(var40);
                           } else {
                              this.fadeIn(var40);
                              var40.relocate(var1 + var11 + var13, var37 ? var7 / 2.0 - var41 / 2.0 : var3 + var34.getPadding().getTop());
                              var40.toFront();
                           }
                        }
                     }

                     var34.resize(var43, var23);
                     var34.relocate(var1, this.snappedTopInset());
                     var34.requestLayout();
                  } else {
                     if (this.fixedCellSizeEnabled) {
                        this.getChildren().remove(var34);
                     }

                     var43 = this.snapSize(var34.prefWidth(-1.0)) - this.snapSize(var27);
                  }

                  var1 += var43;
               }

            }
         }
      }
   }

   protected int getIndentationLevel(IndexedCell var1) {
      return 0;
   }

   protected double getIndentationPerLevel() {
      return 0.0;
   }

   protected boolean isIndentationRequired() {
      return false;
   }

   protected TableColumnBase getTreeColumn() {
      return null;
   }

   protected Node getDisclosureNode() {
      return null;
   }

   protected boolean isDisclosureNodeVisible() {
      return false;
   }

   protected boolean isShowRoot() {
      return true;
   }

   protected TableColumnBase getVisibleLeafColumn(int var1) {
      ObservableList var2 = this.getVisibleLeafColumns();
      return var1 >= 0 && var1 < var2.size() ? (TableColumnBase)var2.get(var1) : null;
   }

   protected void updateCells(boolean var1) {
      if (var1) {
         if (this.fullRefreshCounter == 0) {
            this.recreateCells();
         }

         --this.fullRefreshCounter;
      }

      boolean var2 = this.cells.isEmpty();
      this.cells.clear();
      IndexedCell var3 = (IndexedCell)this.getSkinnable();
      int var4 = var3.getIndex();
      ObservableList var5 = this.getVisibleLeafColumns();
      int var6 = 0;

      for(int var7 = var5.size(); var6 < var7; ++var6) {
         TableColumnBase var8 = (TableColumnBase)var5.get(var6);
         IndexedCell var9 = null;
         if (this.cellsMap.containsKey(var8)) {
            var9 = (IndexedCell)((Reference)this.cellsMap.get(var8)).get();
            if (var9 == null) {
               this.cellsMap.remove(var8);
            }
         }

         if (var9 == null) {
            var9 = this.createCell(var8);
         }

         this.updateCell(var9, var3);
         var9.updateIndex(var4);
         this.cells.add(var9);
      }

      if (!this.fixedCellSizeEnabled && (var1 || var2)) {
         this.getChildren().setAll((Collection)this.cells);
      }

   }

   private VirtualFlow getVirtualFlow() {
      for(Object var1 = this.getSkinnable(); var1 != null; var1 = ((Parent)var1).getParent()) {
         if (var1 instanceof VirtualFlow) {
            return (VirtualFlow)var1;
         }
      }

      return null;
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      ObservableList var13 = this.getVisibleLeafColumns();
      int var14 = 0;

      for(int var15 = var13.size(); var14 < var15; ++var14) {
         var11 += ((TableColumnBase)var13.get(var14)).getWidth();
      }

      return var11;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      if (this.fixedCellSizeEnabled) {
         return this.fixedCellSize;
      } else {
         this.checkState();
         if (this.getCellSize() < 24.0) {
            return this.getCellSize();
         } else {
            double var11 = 0.0;
            int var13 = this.cells.size();

            for(int var14 = 0; var14 < var13; ++var14) {
               IndexedCell var15 = (IndexedCell)this.cells.get(var14);
               var11 = Math.max(var11, var15.prefHeight(-1.0));
            }

            double var16 = Math.max(var11, Math.max(this.getCellSize(), ((IndexedCell)this.getSkinnable()).minHeight(-1.0)));
            return var16;
         }
      }
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      if (this.fixedCellSizeEnabled) {
         return this.fixedCellSize;
      } else {
         this.checkState();
         if (this.getCellSize() < 24.0) {
            return this.getCellSize();
         } else {
            double var11 = 0.0;
            int var13 = this.cells.size();

            for(int var14 = 0; var14 < var13; ++var14) {
               IndexedCell var15 = (IndexedCell)this.cells.get(var14);
               var11 = Math.max(var11, var15.minHeight(-1.0));
            }

            return var11;
         }
      }
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return this.fixedCellSizeEnabled ? this.fixedCellSize : super.computeMaxHeight(var1, var3, var5, var7, var9);
   }

   protected final void checkState() {
      if (this.isDirty) {
         this.updateCells(true);
         this.isDirty = false;
      } else if (this.updateCells) {
         this.updateCells(false);
         this.updateCells = false;
      }

   }

   private void requestCellUpdate() {
      this.updateCells = true;
      ((IndexedCell)this.getSkinnable()).requestLayout();
      int var1 = ((IndexedCell)this.getSkinnable()).getIndex();
      int var2 = 0;

      for(int var3 = this.cells.size(); var2 < var3; ++var2) {
         ((IndexedCell)this.cells.get(var2)).updateIndex(var1);
      }

   }

   private void recreateCells() {
      Iterator var2;
      if (this.cellsMap != null) {
         Collection var1 = this.cellsMap.values();
         var2 = var1.iterator();

         while(var2.hasNext()) {
            Reference var3 = (Reference)var2.next();
            IndexedCell var4 = (IndexedCell)var3.get();
            if (var4 != null) {
               var4.updateIndex(-1);
               var4.getSkin().dispose();
               var4.setSkin((Skin)null);
            }
         }

         this.cellsMap.clear();
      }

      ObservableList var5 = this.getVisibleLeafColumns();
      this.cellsMap = new WeakHashMap(var5.size());
      this.fullRefreshCounter = 100;
      this.getChildren().clear();
      var2 = var5.iterator();

      while(var2.hasNext()) {
         TableColumnBase var6 = (TableColumnBase)var2.next();
         if (!this.cellsMap.containsKey(var6)) {
            this.createCell(var6);
         }
      }

   }

   private IndexedCell createCell(TableColumnBase var1) {
      IndexedCell var2 = this.getCell(var1);
      this.cellsMap.put(var1, new WeakReference(var2));
      return var2;
   }

   private void fadeOut(Node var1) {
      if (!(var1.getOpacity() < 1.0)) {
         if (!DO_ANIMATIONS) {
            var1.setOpacity(0.0);
         } else {
            FadeTransition var2 = new FadeTransition(FADE_DURATION, var1);
            var2.setToValue(0.0);
            var2.play();
         }
      }
   }

   private void fadeIn(Node var1) {
      if (!(var1.getOpacity() > 0.0)) {
         if (!DO_ANIMATIONS) {
            var1.setOpacity(1.0);
         } else {
            FadeTransition var2 = new FadeTransition(FADE_DURATION, var1);
            var2.setToValue(1.0);
            var2.play();
         }
      }
   }

   static {
      DO_ANIMATIONS = !IS_STUB_TOOLKIT && !PlatformUtil.isEmbedded();
      FADE_DURATION = Duration.millis(200.0);
      maxDisclosureWidthMap = new WeakHashMap();
   }
}

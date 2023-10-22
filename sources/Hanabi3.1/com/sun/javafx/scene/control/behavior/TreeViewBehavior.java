package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class TreeViewBehavior extends BehaviorBase {
   protected static final List TREE_VIEW_BINDINGS = new ArrayList();
   private boolean isShiftDown = false;
   private boolean isShortcutDown = false;
   private Callback onScrollPageUp;
   private Callback onScrollPageDown;
   private Runnable onSelectPreviousRow;
   private Runnable onSelectNextRow;
   private Runnable onMoveToFirstCell;
   private Runnable onMoveToLastCell;
   private Runnable onFocusPreviousRow;
   private Runnable onFocusNextRow;
   private boolean selectionChanging = false;
   private final ListChangeListener selectedIndicesListener = (var1x) -> {
      while(var1x.next()) {
         if (var1x.wasReplaced() && TreeCellBehavior.hasDefaultAnchor(this.getControl())) {
            TreeCellBehavior.removeAnchor(this.getControl());
         }

         int var2 = var1x.wasPermutated() ? var1x.getTo() - var1x.getFrom() : 0;
         MultipleSelectionModel var3 = ((TreeView)this.getControl()).getSelectionModel();
         if (!this.selectionChanging) {
            if (var3.isEmpty()) {
               this.setAnchor(-1);
            } else if (this.hasAnchor() && !var3.isSelected(this.getAnchor() + var2)) {
               this.setAnchor(-1);
            }
         }

         int var4 = var1x.getAddedSize();
         if (var4 > 0 && !this.hasAnchor()) {
            List var5 = var1x.getAddedSubList();
            int var6 = (Integer)var5.get(var4 - 1);
            this.setAnchor(var6);
         }
      }

   };
   private final ChangeListener selectionModelListener = new ChangeListener() {
      public void changed(ObservableValue var1, MultipleSelectionModel var2, MultipleSelectionModel var3) {
         if (var2 != null) {
            var2.getSelectedIndices().removeListener(TreeViewBehavior.this.weakSelectedIndicesListener);
         }

         if (var3 != null) {
            var3.getSelectedIndices().addListener(TreeViewBehavior.this.weakSelectedIndicesListener);
         }

      }
   };
   private final WeakListChangeListener weakSelectedIndicesListener;
   private final WeakChangeListener weakSelectionModelListener;

   protected String matchActionForEvent(KeyEvent var1) {
      String var2 = super.matchActionForEvent(var1);
      if (((TreeView)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
         if ("CollapseRow".equals(var2) && (var1.getCode() == KeyCode.LEFT || var1.getCode() == KeyCode.KP_LEFT)) {
            var2 = "ExpandRow";
         } else if ("ExpandRow".equals(var2) && (var1.getCode() == KeyCode.RIGHT || var1.getCode() == KeyCode.KP_RIGHT)) {
            var2 = "CollapseRow";
         }
      }

      return var2;
   }

   protected void callAction(String var1) {
      if ("SelectPreviousRow".equals(var1)) {
         this.selectPreviousRow();
      } else if ("SelectNextRow".equals(var1)) {
         this.selectNextRow();
      } else if ("SelectFirstRow".equals(var1)) {
         this.selectFirstRow();
      } else if ("SelectLastRow".equals(var1)) {
         this.selectLastRow();
      } else if ("SelectAllPageUp".equals(var1)) {
         this.selectAllPageUp();
      } else if ("SelectAllPageDown".equals(var1)) {
         this.selectAllPageDown();
      } else if ("SelectAllToFirstRow".equals(var1)) {
         this.selectAllToFirstRow();
      } else if ("SelectAllToLastRow".equals(var1)) {
         this.selectAllToLastRow();
      } else if ("AlsoSelectNextRow".equals(var1)) {
         this.alsoSelectNextRow();
      } else if ("AlsoSelectPreviousRow".equals(var1)) {
         this.alsoSelectPreviousRow();
      } else if ("ClearSelection".equals(var1)) {
         this.clearSelection();
      } else if ("SelectAll".equals(var1)) {
         this.selectAll();
      } else if ("ScrollUp".equals(var1)) {
         this.scrollUp();
      } else if ("ScrollDown".equals(var1)) {
         this.scrollDown();
      } else if ("ExpandRow".equals(var1)) {
         this.expandRow();
      } else if ("CollapseRow".equals(var1)) {
         this.collapseRow();
      } else if ("ExpandAll".equals(var1)) {
         this.expandAll();
      } else if ("Edit".equals(var1)) {
         this.edit();
      } else if ("CancelEdit".equals(var1)) {
         this.cancelEdit();
      } else if ("FocusFirstRow".equals(var1)) {
         this.focusFirstRow();
      } else if ("FocusLastRow".equals(var1)) {
         this.focusLastRow();
      } else if ("toggleFocusOwnerSelection".equals(var1)) {
         this.toggleFocusOwnerSelection();
      } else if ("SelectAllToFocus".equals(var1)) {
         this.selectAllToFocus(false);
      } else if ("SelectAllToFocusAndSetAnchor".equals(var1)) {
         this.selectAllToFocus(true);
      } else if ("FocusPageUp".equals(var1)) {
         this.focusPageUp();
      } else if ("FocusPageDown".equals(var1)) {
         this.focusPageDown();
      } else if ("FocusPreviousRow".equals(var1)) {
         this.focusPreviousRow();
      } else if ("FocusNextRow".equals(var1)) {
         this.focusNextRow();
      } else if ("DiscontinuousSelectNextRow".equals(var1)) {
         this.discontinuousSelectNextRow();
      } else if ("DiscontinuousSelectPreviousRow".equals(var1)) {
         this.discontinuousSelectPreviousRow();
      } else if ("DiscontinuousSelectPageUp".equals(var1)) {
         this.discontinuousSelectPageUp();
      } else if ("DiscontinuousSelectPageDown".equals(var1)) {
         this.discontinuousSelectPageDown();
      } else if ("DiscontinuousSelectAllToLastRow".equals(var1)) {
         this.discontinuousSelectAllToLastRow();
      } else if ("DiscontinuousSelectAllToFirstRow".equals(var1)) {
         this.discontinuousSelectAllToFirstRow();
      } else {
         super.callAction(var1);
      }

   }

   protected void callActionForEvent(KeyEvent var1) {
      this.isShiftDown = var1.getEventType() == KeyEvent.KEY_PRESSED && var1.isShiftDown();
      this.isShortcutDown = var1.getEventType() == KeyEvent.KEY_PRESSED && var1.isShortcutDown();
      super.callActionForEvent(var1);
   }

   public void setOnScrollPageUp(Callback var1) {
      this.onScrollPageUp = var1;
   }

   public void setOnScrollPageDown(Callback var1) {
      this.onScrollPageDown = var1;
   }

   public void setOnSelectPreviousRow(Runnable var1) {
      this.onSelectPreviousRow = var1;
   }

   public void setOnSelectNextRow(Runnable var1) {
      this.onSelectNextRow = var1;
   }

   public void setOnMoveToFirstCell(Runnable var1) {
      this.onMoveToFirstCell = var1;
   }

   public void setOnMoveToLastCell(Runnable var1) {
      this.onMoveToLastCell = var1;
   }

   public void setOnFocusPreviousRow(Runnable var1) {
      this.onFocusPreviousRow = var1;
   }

   public void setOnFocusNextRow(Runnable var1) {
      this.onFocusNextRow = var1;
   }

   public TreeViewBehavior(TreeView var1) {
      super(var1, TREE_VIEW_BINDINGS);
      this.weakSelectedIndicesListener = new WeakListChangeListener(this.selectedIndicesListener);
      this.weakSelectionModelListener = new WeakChangeListener(this.selectionModelListener);
      ((TreeView)this.getControl()).selectionModelProperty().addListener(this.weakSelectionModelListener);
      if (var1.getSelectionModel() != null) {
         var1.getSelectionModel().getSelectedIndices().addListener(this.weakSelectedIndicesListener);
      }

   }

   public void dispose() {
      TreeCellBehavior.removeAnchor(this.getControl());
      super.dispose();
   }

   private void setAnchor(int var1) {
      TreeCellBehavior.setAnchor(this.getControl(), var1 < 0 ? null : var1, false);
   }

   private int getAnchor() {
      return (Integer)TreeCellBehavior.getAnchor(this.getControl(), ((TreeView)this.getControl()).getFocusModel().getFocusedIndex());
   }

   private boolean hasAnchor() {
      return TreeCellBehavior.hasNonDefaultAnchor(this.getControl());
   }

   public void mousePressed(MouseEvent var1) {
      super.mousePressed(var1);
      if (!var1.isShiftDown()) {
         int var2 = ((TreeView)this.getControl()).getSelectionModel().getSelectedIndex();
         this.setAnchor(var2);
      }

      if (!((TreeView)this.getControl()).isFocused() && ((TreeView)this.getControl()).isFocusTraversable()) {
         ((TreeView)this.getControl()).requestFocus();
      }

   }

   private void clearSelection() {
      ((TreeView)this.getControl()).getSelectionModel().clearSelection();
   }

   private void scrollUp() {
      int var1 = -1;
      if (this.onScrollPageUp != null) {
         var1 = (Integer)this.onScrollPageUp.call(false);
      }

      if (var1 != -1) {
         MultipleSelectionModel var2 = ((TreeView)this.getControl()).getSelectionModel();
         if (var2 != null) {
            var2.clearAndSelect(var1);
         }
      }
   }

   private void scrollDown() {
      int var1 = -1;
      if (this.onScrollPageDown != null) {
         var1 = (Integer)this.onScrollPageDown.call(false);
      }

      if (var1 != -1) {
         MultipleSelectionModel var2 = ((TreeView)this.getControl()).getSelectionModel();
         if (var2 != null) {
            var2.clearAndSelect(var1);
         }
      }
   }

   private void focusFirstRow() {
      FocusModel var1 = ((TreeView)this.getControl()).getFocusModel();
      if (var1 != null) {
         var1.focus(0);
         if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
         }

      }
   }

   private void focusLastRow() {
      FocusModel var1 = ((TreeView)this.getControl()).getFocusModel();
      if (var1 != null) {
         var1.focus(((TreeView)this.getControl()).getExpandedItemCount() - 1);
         if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
         }

      }
   }

   private void focusPreviousRow() {
      FocusModel var1 = ((TreeView)this.getControl()).getFocusModel();
      if (var1 != null) {
         MultipleSelectionModel var2 = ((TreeView)this.getControl()).getSelectionModel();
         if (var2 != null) {
            var1.focusPrevious();
            if (!this.isShortcutDown || this.getAnchor() == -1) {
               this.setAnchor(var1.getFocusedIndex());
            }

            if (this.onFocusPreviousRow != null) {
               this.onFocusPreviousRow.run();
            }

         }
      }
   }

   private void focusNextRow() {
      FocusModel var1 = ((TreeView)this.getControl()).getFocusModel();
      if (var1 != null) {
         MultipleSelectionModel var2 = ((TreeView)this.getControl()).getSelectionModel();
         if (var2 != null) {
            var1.focusNext();
            if (!this.isShortcutDown || this.getAnchor() == -1) {
               this.setAnchor(var1.getFocusedIndex());
            }

            if (this.onFocusNextRow != null) {
               this.onFocusNextRow.run();
            }

         }
      }
   }

   private void focusPageUp() {
      int var1 = (Integer)this.onScrollPageUp.call(true);
      FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
      if (var2 != null) {
         var2.focus(var1);
      }
   }

   private void focusPageDown() {
      int var1 = (Integer)this.onScrollPageDown.call(true);
      FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
      if (var2 != null) {
         var2.focus(var1);
      }
   }

   private void alsoSelectPreviousRow() {
      FocusModel var1 = ((TreeView)this.getControl()).getFocusModel();
      if (var1 != null) {
         MultipleSelectionModel var2 = ((TreeView)this.getControl()).getSelectionModel();
         if (var2 != null) {
            if (this.isShiftDown && this.getAnchor() != -1) {
               int var3 = var1.getFocusedIndex() - 1;
               if (var3 < 0) {
                  return;
               }

               int var4 = this.getAnchor();
               if (!this.hasAnchor()) {
                  this.setAnchor(var1.getFocusedIndex());
               }

               if (var2.getSelectedIndices().size() > 1) {
                  this.clearSelectionOutsideRange(var4, var3);
               }

               if (var4 > var3) {
                  var2.selectRange(var4, var3 - 1);
               } else {
                  var2.selectRange(var4, var3 + 1);
               }
            } else {
               var2.selectPrevious();
            }

            this.onSelectPreviousRow.run();
         }
      }
   }

   private void alsoSelectNextRow() {
      FocusModel var1 = ((TreeView)this.getControl()).getFocusModel();
      if (var1 != null) {
         MultipleSelectionModel var2 = ((TreeView)this.getControl()).getSelectionModel();
         if (var2 != null) {
            if (this.isShiftDown && this.getAnchor() != -1) {
               int var3 = var1.getFocusedIndex() + 1;
               int var4 = this.getAnchor();
               if (!this.hasAnchor()) {
                  this.setAnchor(var1.getFocusedIndex());
               }

               if (var2.getSelectedIndices().size() > 1) {
                  this.clearSelectionOutsideRange(var4, var3);
               }

               if (var4 > var3) {
                  var2.selectRange(var4, var3 - 1);
               } else {
                  var2.selectRange(var4, var3 + 1);
               }
            } else {
               var2.selectNext();
            }

            this.onSelectNextRow.run();
         }
      }
   }

   private void clearSelectionOutsideRange(int var1, int var2) {
      MultipleSelectionModel var3 = ((TreeView)this.getControl()).getSelectionModel();
      if (var3 != null) {
         int var4 = Math.min(var1, var2);
         int var5 = Math.max(var1, var2);
         ArrayList var6 = new ArrayList(var3.getSelectedIndices());
         this.selectionChanging = true;

         for(int var7 = 0; var7 < var6.size(); ++var7) {
            int var8 = (Integer)var6.get(var7);
            if (var8 < var4 || var8 > var5) {
               var3.clearSelection(var8);
            }
         }

         this.selectionChanging = false;
      }
   }

   private void selectPreviousRow() {
      FocusModel var1 = ((TreeView)this.getControl()).getFocusModel();
      if (var1 != null) {
         int var2 = var1.getFocusedIndex();
         if (var2 > 0) {
            this.setAnchor(var2 - 1);
            ((TreeView)this.getControl()).getSelectionModel().clearAndSelect(var2 - 1);
            this.onSelectPreviousRow.run();
         }
      }
   }

   private void selectNextRow() {
      FocusModel var1 = ((TreeView)this.getControl()).getFocusModel();
      if (var1 != null) {
         int var2 = var1.getFocusedIndex();
         if (var2 != ((TreeView)this.getControl()).getExpandedItemCount() - 1) {
            this.setAnchor(var2 + 1);
            ((TreeView)this.getControl()).getSelectionModel().clearAndSelect(var2 + 1);
            this.onSelectNextRow.run();
         }
      }
   }

   private void selectFirstRow() {
      if (((TreeView)this.getControl()).getExpandedItemCount() > 0) {
         ((TreeView)this.getControl()).getSelectionModel().clearAndSelect(0);
         if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
         }
      }

   }

   private void selectLastRow() {
      ((TreeView)this.getControl()).getSelectionModel().clearAndSelect(((TreeView)this.getControl()).getExpandedItemCount() - 1);
      this.onMoveToLastCell.run();
   }

   private void selectAllToFirstRow() {
      MultipleSelectionModel var1 = ((TreeView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex();
            if (this.isShiftDown) {
               var3 = this.hasAnchor() ? this.getAnchor() : var3;
            }

            var1.clearSelection();
            var1.selectRange(var3, -1);
            var2.focus(0);
            if (this.isShiftDown) {
               this.setAnchor(var3);
            }

            if (this.onMoveToFirstCell != null) {
               this.onMoveToFirstCell.run();
            }

         }
      }
   }

   private void selectAllToLastRow() {
      MultipleSelectionModel var1 = ((TreeView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex();
            if (this.isShiftDown) {
               var3 = this.hasAnchor() ? this.getAnchor() : var3;
            }

            var1.clearSelection();
            var1.selectRange(var3, ((TreeView)this.getControl()).getExpandedItemCount());
            if (this.isShiftDown) {
               this.setAnchor(var3);
            }

            if (this.onMoveToLastCell != null) {
               this.onMoveToLastCell.run();
            }

         }
      }
   }

   private void selectAll() {
      ((TreeView)this.getControl()).getSelectionModel().selectAll();
   }

   private void selectAllPageUp() {
      FocusModel var1 = ((TreeView)this.getControl()).getFocusModel();
      if (var1 != null) {
         int var2 = var1.getFocusedIndex();
         if (this.isShiftDown) {
            var2 = this.getAnchor() == -1 ? var2 : this.getAnchor();
            this.setAnchor(var2);
         }

         int var3 = (Integer)this.onScrollPageUp.call(false);
         int var4 = var2 < var3 ? 1 : -1;
         MultipleSelectionModel var5 = ((TreeView)this.getControl()).getSelectionModel();
         if (var5 != null) {
            this.selectionChanging = true;
            if (var5.getSelectionMode() == SelectionMode.SINGLE) {
               var5.select(var3);
            } else {
               var5.clearSelection();
               var5.selectRange(var2, var3 + var4);
            }

            this.selectionChanging = false;
         }
      }
   }

   private void selectAllPageDown() {
      FocusModel var1 = ((TreeView)this.getControl()).getFocusModel();
      if (var1 != null) {
         int var2 = var1.getFocusedIndex();
         if (this.isShiftDown) {
            var2 = this.getAnchor() == -1 ? var2 : this.getAnchor();
            this.setAnchor(var2);
         }

         int var3 = (Integer)this.onScrollPageDown.call(false);
         int var4 = var2 < var3 ? 1 : -1;
         MultipleSelectionModel var5 = ((TreeView)this.getControl()).getSelectionModel();
         if (var5 != null) {
            this.selectionChanging = true;
            if (var5.getSelectionMode() == SelectionMode.SINGLE) {
               var5.select(var3);
            } else {
               var5.clearSelection();
               var5.selectRange(var2, var3 + var4);
            }

            this.selectionChanging = false;
         }
      }
   }

   private void selectAllToFocus(boolean var1) {
      TreeView var2 = (TreeView)this.getControl();
      if (var2.getEditingItem() == null) {
         MultipleSelectionModel var3 = var2.getSelectionModel();
         if (var3 != null) {
            FocusModel var4 = var2.getFocusModel();
            if (var4 != null) {
               int var5 = var4.getFocusedIndex();
               int var6 = this.getAnchor();
               var3.clearSelection();
               int var8 = var6 > var5 ? var5 - 1 : var5 + 1;
               var3.selectRange(var6, var8);
               this.setAnchor(var1 ? var5 : var6);
            }
         }
      }
   }

   private void expandRow() {
      Callback var1 = (var1x) -> {
         return ((TreeView)this.getControl()).getRow(var1x);
      };
      expandRow(((TreeView)this.getControl()).getSelectionModel(), var1);
   }

   private void expandAll() {
      expandAll(((TreeView)this.getControl()).getRoot());
   }

   private void collapseRow() {
      TreeView var1 = (TreeView)this.getControl();
      collapseRow(var1.getSelectionModel(), var1.getRoot(), var1.isShowRoot());
   }

   static void expandRow(MultipleSelectionModel var0, Callback var1) {
      if (var0 != null) {
         TreeItem var2 = (TreeItem)var0.getSelectedItem();
         if (var2 != null && !var2.isLeaf()) {
            if (var2.isExpanded()) {
               ObservableList var3 = var2.getChildren();
               if (!var3.isEmpty()) {
                  var0.clearAndSelect((Integer)var1.call(var3.get(0)));
               }
            } else {
               var2.setExpanded(true);
            }

         }
      }
   }

   static void expandAll(TreeItem var0) {
      if (var0 != null) {
         var0.setExpanded(true);
         expandChildren(var0);
      }
   }

   private static void expandChildren(TreeItem var0) {
      if (var0 != null) {
         ObservableList var1 = var0.getChildren();
         if (var1 != null) {
            for(int var2 = 0; var2 < var1.size(); ++var2) {
               TreeItem var3 = (TreeItem)var1.get(var2);
               if (var3 != null && !var3.isLeaf()) {
                  var3.setExpanded(true);
                  expandChildren(var3);
               }
            }

         }
      }
   }

   static void collapseRow(MultipleSelectionModel var0, TreeItem var1, boolean var2) {
      if (var0 != null) {
         TreeItem var3 = (TreeItem)var0.getSelectedItem();
         if (var3 != null) {
            if (var1 != null) {
               if (var2 || var3.isExpanded() || !var1.equals(var3.getParent())) {
                  if (!var1.equals(var3) || var1.isExpanded() && !var1.getChildren().isEmpty()) {
                     if (!var3.isLeaf() && var3.isExpanded()) {
                        var3.setExpanded(false);
                     } else {
                        var0.clearSelection();
                        var0.select(var3.getParent());
                     }

                  }
               }
            }
         }
      }
   }

   private void cancelEdit() {
      ((TreeView)this.getControl()).edit((TreeItem)null);
   }

   private void edit() {
      TreeItem var1 = (TreeItem)((TreeView)this.getControl()).getSelectionModel().getSelectedItem();
      if (var1 != null) {
         ((TreeView)this.getControl()).edit(var1);
      }
   }

   private void toggleFocusOwnerSelection() {
      MultipleSelectionModel var1 = ((TreeView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex();
            if (var1.isSelected(var3)) {
               var1.clearSelection(var3);
               var2.focus(var3);
            } else {
               var1.select(var3);
            }

            this.setAnchor(var3);
         }
      }
   }

   private void discontinuousSelectPreviousRow() {
      MultipleSelectionModel var1 = ((TreeView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         if (var1.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectPreviousRow();
         } else {
            FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
            if (var2 != null) {
               int var3 = var2.getFocusedIndex();
               int var4 = var3 - 1;
               if (var4 >= 0) {
                  int var5 = var3;
                  if (this.isShiftDown) {
                     var5 = this.getAnchor() == -1 ? var3 : this.getAnchor();
                  }

                  var1.selectRange(var4, var5 + 1);
                  var2.focus(var4);
                  if (this.onFocusPreviousRow != null) {
                     this.onFocusPreviousRow.run();
                  }

               }
            }
         }
      }
   }

   private void discontinuousSelectNextRow() {
      MultipleSelectionModel var1 = ((TreeView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         if (var1.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectNextRow();
         } else {
            FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
            if (var2 != null) {
               int var3 = var2.getFocusedIndex();
               int var4 = var3 + 1;
               if (var4 < ((TreeView)this.getControl()).getExpandedItemCount()) {
                  int var5 = var3;
                  if (this.isShiftDown) {
                     var5 = this.getAnchor() == -1 ? var3 : this.getAnchor();
                  }

                  var1.selectRange(var5, var4 + 1);
                  var2.focus(var4);
                  if (this.onFocusNextRow != null) {
                     this.onFocusNextRow.run();
                  }

               }
            }
         }
      }
   }

   private void discontinuousSelectPageUp() {
      MultipleSelectionModel var1 = ((TreeView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = this.getAnchor();
            int var4 = (Integer)this.onScrollPageUp.call(false);
            var1.selectRange(var3, var4 - 1);
         }
      }
   }

   private void discontinuousSelectPageDown() {
      MultipleSelectionModel var1 = ((TreeView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = this.getAnchor();
            int var4 = (Integer)this.onScrollPageDown.call(false);
            var1.selectRange(var3, var4 + 1);
         }
      }
   }

   private void discontinuousSelectAllToFirstRow() {
      MultipleSelectionModel var1 = ((TreeView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex();
            var1.selectRange(0, var3);
            var2.focus(0);
            if (this.onMoveToFirstCell != null) {
               this.onMoveToFirstCell.run();
            }

         }
      }
   }

   private void discontinuousSelectAllToLastRow() {
      MultipleSelectionModel var1 = ((TreeView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((TreeView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex() + 1;
            var1.selectRange(var3, ((TreeView)this.getControl()).getExpandedItemCount());
            if (this.onMoveToLastCell != null) {
               this.onMoveToLastCell.run();
            }

         }
      }
   }

   static {
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectFirstRow"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectLastRow"));
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.HOME, "SelectAllToFirstRow")).shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.END, "SelectAllToLastRow")).shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, "SelectAllPageUp")).shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, "SelectAllPageDown")).shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "SelectAllToFocus")).shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "SelectAllToFocusAndSetAnchor")).shortcut().shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.HOME, "FocusFirstRow")).shortcut());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.END, "FocusLastRow")).shortcut());
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "ScrollUp"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "ScrollDown"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection"));
      if (PlatformUtil.isMac()) {
         TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection")).ctrl().shortcut());
      } else {
         TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection")).ctrl());
      }

      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.A, "SelectAll")).shortcut());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, "FocusPageUp")).shortcut());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, "FocusPageDown")).shortcut());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.UP, "FocusPreviousRow")).shortcut());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.DOWN, "FocusNextRow")).shortcut());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.UP, "DiscontinuousSelectPreviousRow")).shortcut().shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.DOWN, "DiscontinuousSelectNextRow")).shortcut().shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, "DiscontinuousSelectPageUp")).shortcut().shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, "DiscontinuousSelectPageDown")).shortcut().shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.HOME, "DiscontinuousSelectAllToFirstRow")).shortcut().shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.END, "DiscontinuousSelectAllToLastRow")).shortcut().shift());
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "CollapseRow"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "CollapseRow"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "ExpandRow"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "ExpandRow"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.MULTIPLY, "ExpandAll"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ADD, "ExpandRow"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SUBTRACT, "CollapseRow"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "SelectPreviousRow"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "SelectPreviousRow"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "SelectNextRow"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "SelectNextRow"));
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.UP, "AlsoSelectPreviousRow")).shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.KP_UP, "AlsoSelectPreviousRow")).shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.DOWN, "AlsoSelectNextRow")).shift());
      TREE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.KP_DOWN, "AlsoSelectNextRow")).shift());
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ENTER, "Edit"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.F2, "Edit"));
      TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "CancelEdit"));
   }
}

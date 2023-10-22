package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ListViewBehavior extends BehaviorBase {
   protected static final List LIST_VIEW_BINDINGS = new ArrayList();
   private boolean isShiftDown = false;
   private boolean isShortcutDown = false;
   private Callback onScrollPageUp;
   private Callback onScrollPageDown;
   private Runnable onFocusPreviousRow;
   private Runnable onFocusNextRow;
   private Runnable onSelectPreviousRow;
   private Runnable onSelectNextRow;
   private Runnable onMoveToFirstCell;
   private Runnable onMoveToLastCell;
   private boolean selectionChanging = false;
   private final ListChangeListener selectedIndicesListener = (var1x) -> {
      while(var1x.next()) {
         if (var1x.wasReplaced() && ListCellBehavior.hasDefaultAnchor(this.getControl())) {
            ListCellBehavior.removeAnchor(this.getControl());
         }

         int var2 = var1x.wasPermutated() ? var1x.getTo() - var1x.getFrom() : 0;
         MultipleSelectionModel var3 = ((ListView)this.getControl()).getSelectionModel();
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
   private final ListChangeListener itemsListListener = (var1x) -> {
      while(var1x.next()) {
         if (var1x.wasAdded() && var1x.getFrom() <= this.getAnchor()) {
            this.setAnchor(this.getAnchor() + var1x.getAddedSize());
         } else if (var1x.wasRemoved() && var1x.getFrom() <= this.getAnchor()) {
            this.setAnchor(this.getAnchor() - var1x.getRemovedSize());
         }
      }

   };
   private final ChangeListener itemsListener = new ChangeListener() {
      public void changed(ObservableValue var1, ObservableList var2, ObservableList var3) {
         if (var2 != null) {
            var2.removeListener(ListViewBehavior.this.weakItemsListListener);
         }

         if (var3 != null) {
            var3.addListener(ListViewBehavior.this.weakItemsListListener);
         }

      }
   };
   private final ChangeListener selectionModelListener = new ChangeListener() {
      public void changed(ObservableValue var1, MultipleSelectionModel var2, MultipleSelectionModel var3) {
         if (var2 != null) {
            var2.getSelectedIndices().removeListener(ListViewBehavior.this.weakSelectedIndicesListener);
         }

         if (var3 != null) {
            var3.getSelectedIndices().addListener(ListViewBehavior.this.weakSelectedIndicesListener);
         }

      }
   };
   private final WeakChangeListener weakItemsListener;
   private final WeakListChangeListener weakSelectedIndicesListener;
   private final WeakListChangeListener weakItemsListListener;
   private final WeakChangeListener weakSelectionModelListener;
   private TwoLevelFocusListBehavior tlFocus;

   protected String matchActionForEvent(KeyEvent var1) {
      String var2 = super.matchActionForEvent(var1);
      if (var2 != null) {
         if (var1.getCode() != KeyCode.LEFT && var1.getCode() != KeyCode.KP_LEFT) {
            if ((var1.getCode() == KeyCode.RIGHT || var1.getCode() == KeyCode.KP_RIGHT) && ((ListView)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
               if (var1.isShiftDown()) {
                  var2 = "AlsoSelectPreviousRow";
               } else if (var1.isShortcutDown()) {
                  var2 = "FocusPreviousRow";
               } else {
                  var2 = ((ListView)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "SelectPreviousRow" : "TraverseLeft";
               }
            }
         } else if (((ListView)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            if (var1.isShiftDown()) {
               var2 = "AlsoSelectNextRow";
            } else if (var1.isShortcutDown()) {
               var2 = "FocusNextRow";
            } else {
               var2 = ((ListView)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "SelectNextRow" : "TraverseRight";
            }
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
      } else if ("SelectAllToFirstRow".equals(var1)) {
         this.selectAllToFirstRow();
      } else if ("SelectAllToLastRow".equals(var1)) {
         this.selectAllToLastRow();
      } else if ("SelectAllPageUp".equals(var1)) {
         this.selectAllPageUp();
      } else if ("SelectAllPageDown".equals(var1)) {
         this.selectAllPageDown();
      } else if ("AlsoSelectNextRow".equals(var1)) {
         this.alsoSelectNextRow();
      } else if ("AlsoSelectPreviousRow".equals(var1)) {
         this.alsoSelectPreviousRow();
      } else if ("ClearSelection".equals(var1)) {
         this.clearSelection();
      } else if ("SelectAll".equals(var1)) {
         this.selectAll();
      } else if ("ScrollUp".equals(var1)) {
         this.scrollPageUp();
      } else if ("ScrollDown".equals(var1)) {
         this.scrollPageDown();
      } else if ("FocusPreviousRow".equals(var1)) {
         this.focusPreviousRow();
      } else if ("FocusNextRow".equals(var1)) {
         this.focusNextRow();
      } else if ("FocusPageUp".equals(var1)) {
         this.focusPageUp();
      } else if ("FocusPageDown".equals(var1)) {
         this.focusPageDown();
      } else if ("Activate".equals(var1)) {
         this.activate();
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

   public void setOnFocusPreviousRow(Runnable var1) {
      this.onFocusPreviousRow = var1;
   }

   public void setOnFocusNextRow(Runnable var1) {
      this.onFocusNextRow = var1;
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

   public ListViewBehavior(ListView var1) {
      super(var1, LIST_VIEW_BINDINGS);
      this.weakItemsListener = new WeakChangeListener(this.itemsListener);
      this.weakSelectedIndicesListener = new WeakListChangeListener(this.selectedIndicesListener);
      this.weakItemsListListener = new WeakListChangeListener(this.itemsListListener);
      this.weakSelectionModelListener = new WeakChangeListener(this.selectionModelListener);
      var1.itemsProperty().addListener(this.weakItemsListener);
      if (var1.getItems() != null) {
         var1.getItems().addListener(this.weakItemsListListener);
      }

      ((ListView)this.getControl()).selectionModelProperty().addListener(this.weakSelectionModelListener);
      if (var1.getSelectionModel() != null) {
         var1.getSelectionModel().getSelectedIndices().addListener(this.weakSelectedIndicesListener);
      }

      if (Utils.isTwoLevelFocus()) {
         this.tlFocus = new TwoLevelFocusListBehavior(var1);
      }

   }

   public void dispose() {
      ListCellBehavior.removeAnchor(this.getControl());
      if (this.tlFocus != null) {
         this.tlFocus.dispose();
      }

      super.dispose();
   }

   private void setAnchor(int var1) {
      ListCellBehavior.setAnchor(this.getControl(), var1 < 0 ? null : var1, false);
   }

   private int getAnchor() {
      return (Integer)ListCellBehavior.getAnchor(this.getControl(), ((ListView)this.getControl()).getFocusModel().getFocusedIndex());
   }

   private boolean hasAnchor() {
      return ListCellBehavior.hasNonDefaultAnchor(this.getControl());
   }

   public void mousePressed(MouseEvent var1) {
      super.mousePressed(var1);
      if (!var1.isShiftDown() && !var1.isSynthesized()) {
         int var2 = ((ListView)this.getControl()).getSelectionModel().getSelectedIndex();
         this.setAnchor(var2);
      }

      if (!((ListView)this.getControl()).isFocused() && ((ListView)this.getControl()).isFocusTraversable()) {
         ((ListView)this.getControl()).requestFocus();
      }

   }

   private int getRowCount() {
      return ((ListView)this.getControl()).getItems() == null ? 0 : ((ListView)this.getControl()).getItems().size();
   }

   private void clearSelection() {
      ((ListView)this.getControl()).getSelectionModel().clearSelection();
   }

   private void scrollPageUp() {
      int var1 = -1;
      if (this.onScrollPageUp != null) {
         var1 = (Integer)this.onScrollPageUp.call(false);
      }

      if (var1 != -1) {
         MultipleSelectionModel var2 = ((ListView)this.getControl()).getSelectionModel();
         if (var2 != null) {
            var2.clearAndSelect(var1);
         }
      }
   }

   private void scrollPageDown() {
      int var1 = -1;
      if (this.onScrollPageDown != null) {
         var1 = (Integer)this.onScrollPageDown.call(false);
      }

      if (var1 != -1) {
         MultipleSelectionModel var2 = ((ListView)this.getControl()).getSelectionModel();
         if (var2 != null) {
            var2.clearAndSelect(var1);
         }
      }
   }

   private void focusFirstRow() {
      FocusModel var1 = ((ListView)this.getControl()).getFocusModel();
      if (var1 != null) {
         var1.focus(0);
         if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
         }

      }
   }

   private void focusLastRow() {
      FocusModel var1 = ((ListView)this.getControl()).getFocusModel();
      if (var1 != null) {
         var1.focus(this.getRowCount() - 1);
         if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
         }

      }
   }

   private void focusPreviousRow() {
      FocusModel var1 = ((ListView)this.getControl()).getFocusModel();
      if (var1 != null) {
         MultipleSelectionModel var2 = ((ListView)this.getControl()).getSelectionModel();
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
      FocusModel var1 = ((ListView)this.getControl()).getFocusModel();
      if (var1 != null) {
         MultipleSelectionModel var2 = ((ListView)this.getControl()).getSelectionModel();
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
      FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
      if (var2 != null) {
         var2.focus(var1);
      }
   }

   private void focusPageDown() {
      int var1 = (Integer)this.onScrollPageDown.call(true);
      FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
      if (var2 != null) {
         var2.focus(var1);
      }
   }

   private void alsoSelectPreviousRow() {
      FocusModel var1 = ((ListView)this.getControl()).getFocusModel();
      if (var1 != null) {
         MultipleSelectionModel var2 = ((ListView)this.getControl()).getSelectionModel();
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
      FocusModel var1 = ((ListView)this.getControl()).getFocusModel();
      if (var1 != null) {
         MultipleSelectionModel var2 = ((ListView)this.getControl()).getSelectionModel();
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
      MultipleSelectionModel var3 = ((ListView)this.getControl()).getSelectionModel();
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
      FocusModel var1 = ((ListView)this.getControl()).getFocusModel();
      if (var1 != null) {
         int var2 = var1.getFocusedIndex();
         if (var2 > 0) {
            this.setAnchor(var2 - 1);
            ((ListView)this.getControl()).getSelectionModel().clearAndSelect(var2 - 1);
            this.onSelectPreviousRow.run();
         }
      }
   }

   private void selectNextRow() {
      ListView var1 = (ListView)this.getControl();
      FocusModel var2 = var1.getFocusModel();
      if (var2 != null) {
         int var3 = var2.getFocusedIndex();
         if (var3 != this.getRowCount() - 1) {
            MultipleSelectionModel var4 = var1.getSelectionModel();
            if (var4 != null) {
               this.setAnchor(var3 + 1);
               var4.clearAndSelect(var3 + 1);
               if (this.onSelectNextRow != null) {
                  this.onSelectNextRow.run();
               }

            }
         }
      }
   }

   private void selectFirstRow() {
      if (this.getRowCount() > 0) {
         ((ListView)this.getControl()).getSelectionModel().clearAndSelect(0);
         if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
         }
      }

   }

   private void selectLastRow() {
      ((ListView)this.getControl()).getSelectionModel().clearAndSelect(this.getRowCount() - 1);
      if (this.onMoveToLastCell != null) {
         this.onMoveToLastCell.run();
      }

   }

   private void selectAllPageUp() {
      FocusModel var1 = ((ListView)this.getControl()).getFocusModel();
      if (var1 != null) {
         int var2 = var1.getFocusedIndex();
         if (this.isShiftDown) {
            var2 = this.getAnchor() == -1 ? var2 : this.getAnchor();
            this.setAnchor(var2);
         }

         int var3 = (Integer)this.onScrollPageUp.call(false);
         int var4 = var2 < var3 ? 1 : -1;
         MultipleSelectionModel var5 = ((ListView)this.getControl()).getSelectionModel();
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
      FocusModel var1 = ((ListView)this.getControl()).getFocusModel();
      if (var1 != null) {
         int var2 = var1.getFocusedIndex();
         if (this.isShiftDown) {
            var2 = this.getAnchor() == -1 ? var2 : this.getAnchor();
            this.setAnchor(var2);
         }

         int var3 = (Integer)this.onScrollPageDown.call(false);
         int var4 = var2 < var3 ? 1 : -1;
         MultipleSelectionModel var5 = ((ListView)this.getControl()).getSelectionModel();
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

   private void selectAllToFirstRow() {
      MultipleSelectionModel var1 = ((ListView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
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
      MultipleSelectionModel var1 = ((ListView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex();
            if (this.isShiftDown) {
               var3 = this.hasAnchor() ? this.getAnchor() : var3;
            }

            var1.clearSelection();
            var1.selectRange(var3, this.getRowCount());
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
      MultipleSelectionModel var1 = ((ListView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         var1.selectAll();
      }
   }

   private void selectAllToFocus(boolean var1) {
      ListView var2 = (ListView)this.getControl();
      if (var2.getEditingIndex() < 0) {
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

   private void cancelEdit() {
      ((ListView)this.getControl()).edit(-1);
   }

   private void activate() {
      int var1 = ((ListView)this.getControl()).getFocusModel().getFocusedIndex();
      ((ListView)this.getControl()).getSelectionModel().select(var1);
      this.setAnchor(var1);
      if (var1 >= 0) {
         ((ListView)this.getControl()).edit(var1);
      }

   }

   private void toggleFocusOwnerSelection() {
      MultipleSelectionModel var1 = ((ListView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
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
      MultipleSelectionModel var1 = ((ListView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         if (var1.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectPreviousRow();
         } else {
            FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
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
      MultipleSelectionModel var1 = ((ListView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         if (var1.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectNextRow();
         } else {
            FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
            if (var2 != null) {
               int var3 = var2.getFocusedIndex();
               int var4 = var3 + 1;
               if (var4 < this.getRowCount()) {
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
      MultipleSelectionModel var1 = ((ListView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = this.getAnchor();
            int var4 = (Integer)this.onScrollPageUp.call(false);
            var1.selectRange(var3, var4 - 1);
         }
      }
   }

   private void discontinuousSelectPageDown() {
      MultipleSelectionModel var1 = ((ListView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = this.getAnchor();
            int var4 = (Integer)this.onScrollPageDown.call(false);
            var1.selectRange(var3, var4 + 1);
         }
      }
   }

   private void discontinuousSelectAllToFirstRow() {
      MultipleSelectionModel var1 = ((ListView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
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
      MultipleSelectionModel var1 = ((ListView)this.getControl()).getSelectionModel();
      if (var1 != null) {
         FocusModel var2 = ((ListView)this.getControl()).getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex() + 1;
            var1.selectRange(var3, this.getRowCount());
            if (this.onMoveToLastCell != null) {
               this.onMoveToLastCell.run();
            }

         }
      }
   }

   static {
      LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectFirstRow"));
      LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectLastRow"));
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.HOME, "SelectAllToFirstRow")).shift());
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.END, "SelectAllToLastRow")).shift());
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, "SelectAllPageUp")).shift());
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, "SelectAllPageDown")).shift());
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "SelectAllToFocus")).shift());
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "SelectAllToFocusAndSetAnchor")).shortcut().shift());
      LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "ScrollUp"));
      LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "ScrollDown"));
      LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ENTER, "Activate"));
      LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "Activate"));
      LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.F2, "Activate"));
      LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "CancelEdit"));
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.A, "SelectAll")).shortcut());
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.HOME, "FocusFirstRow")).shortcut());
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.END, "FocusLastRow")).shortcut());
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, "FocusPageUp")).shortcut());
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, "FocusPageDown")).shortcut());
      if (PlatformUtil.isMac()) {
         LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection")).ctrl().shortcut());
      } else {
         LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection")).ctrl());
      }

      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.UP, "SelectPreviousRow")).vertical());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.KP_UP, "SelectPreviousRow")).vertical());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.DOWN, "SelectNextRow")).vertical());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.KP_DOWN, "SelectNextRow")).vertical());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.UP, "AlsoSelectPreviousRow")).vertical().shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.KP_UP, "AlsoSelectPreviousRow")).vertical().shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.DOWN, "AlsoSelectNextRow")).vertical().shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.KP_DOWN, "AlsoSelectNextRow")).vertical().shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.UP, "FocusPreviousRow")).vertical().shortcut());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.DOWN, "FocusNextRow")).vertical().shortcut());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.UP, "DiscontinuousSelectPreviousRow")).vertical().shortcut().shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.DOWN, "DiscontinuousSelectNextRow")).vertical().shortcut().shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.PAGE_UP, "DiscontinuousSelectPageUp")).vertical().shortcut().shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.PAGE_DOWN, "DiscontinuousSelectPageDown")).vertical().shortcut().shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.HOME, "DiscontinuousSelectAllToFirstRow")).vertical().shortcut().shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.END, "DiscontinuousSelectAllToLastRow")).vertical().shortcut().shift());
      LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.LEFT, "SelectPreviousRow"));
      LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_LEFT, "SelectPreviousRow"));
      LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.RIGHT, "SelectNextRow"));
      LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_RIGHT, "SelectNextRow"));
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.LEFT, "AlsoSelectPreviousRow")).shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.KP_LEFT, "AlsoSelectPreviousRow")).shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.RIGHT, "AlsoSelectNextRow")).shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.KP_RIGHT, "AlsoSelectNextRow")).shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.LEFT, "FocusPreviousRow")).shortcut());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.RIGHT, "FocusNextRow")).shortcut());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.LEFT, "DiscontinuousSelectPreviousRow")).shortcut().shift());
      LIST_VIEW_BINDINGS.add((new ListViewKeyBinding(KeyCode.RIGHT, "DiscontinuousSelectNextRow")).shortcut().shift());
      LIST_VIEW_BINDINGS.add((new KeyBinding(KeyCode.BACK_SLASH, "ClearSelection")).shortcut());
   }

   private static class ListViewKeyBinding extends OrientedKeyBinding {
      public ListViewKeyBinding(KeyCode var1, String var2) {
         super(var1, var2);
      }

      public ListViewKeyBinding(KeyCode var1, EventType var2, String var3) {
         super(var1, var2, var3);
      }

      public boolean getVertical(Control var1) {
         return ((ListView)var1).getOrientation() == Orientation.VERTICAL;
      }
   }
}

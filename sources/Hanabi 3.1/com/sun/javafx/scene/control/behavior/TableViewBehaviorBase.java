package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.SizeLimitedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Control;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public abstract class TableViewBehaviorBase extends BehaviorBase {
   protected static final List TABLE_VIEW_BINDINGS = new ArrayList();
   protected boolean isShortcutDown;
   protected boolean isShiftDown;
   private boolean selectionPathDeviated;
   protected boolean selectionChanging;
   private final SizeLimitedList selectionHistory;
   protected final ListChangeListener selectedCellsListener;
   protected final WeakListChangeListener weakSelectedCellsListener;
   private Callback onScrollPageUp;
   private Callback onScrollPageDown;
   private Runnable onFocusPreviousRow;
   private Runnable onFocusNextRow;
   private Runnable onSelectPreviousRow;
   private Runnable onSelectNextRow;
   private Runnable onMoveToFirstCell;
   private Runnable onMoveToLastCell;
   private Runnable onSelectRightCell;
   private Runnable onSelectLeftCell;

   protected void callAction(String var1) {
      boolean var2 = this.getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
      if ("SelectPreviousRow".equals(var1)) {
         this.selectPreviousRow();
      } else if ("SelectNextRow".equals(var1)) {
         this.selectNextRow();
      } else if ("SelectLeftCell".equals(var1)) {
         if (var2) {
            this.selectRightCell();
         } else {
            this.selectLeftCell();
         }
      } else if ("SelectRightCell".equals(var1)) {
         if (var2) {
            this.selectLeftCell();
         } else {
            this.selectRightCell();
         }
      } else if ("SelectFirstRow".equals(var1)) {
         this.selectFirstRow();
      } else if ("SelectLastRow".equals(var1)) {
         this.selectLastRow();
      } else if ("SelectAll".equals(var1)) {
         this.selectAll();
      } else if ("SelectAllPageUp".equals(var1)) {
         this.selectAllPageUp();
      } else if ("SelectAllPageDown".equals(var1)) {
         this.selectAllPageDown();
      } else if ("SelectAllToFirstRow".equals(var1)) {
         this.selectAllToFirstRow();
      } else if ("SelectAllToLastRow".equals(var1)) {
         this.selectAllToLastRow();
      } else if ("AlsoSelectNext".equals(var1)) {
         this.alsoSelectNext();
      } else if ("AlsoSelectPrevious".equals(var1)) {
         this.alsoSelectPrevious();
      } else if ("AlsoSelectLeftCell".equals(var1)) {
         if (var2) {
            this.alsoSelectRightCell();
         } else {
            this.alsoSelectLeftCell();
         }
      } else if ("AlsoSelectRightCell".equals(var1)) {
         if (var2) {
            this.alsoSelectLeftCell();
         } else {
            this.alsoSelectRightCell();
         }
      } else if ("ClearSelection".equals(var1)) {
         this.clearSelection();
      } else if ("ScrollUp".equals(var1)) {
         this.scrollUp();
      } else if ("ScrollDown".equals(var1)) {
         this.scrollDown();
      } else if ("FocusPreviousRow".equals(var1)) {
         this.focusPreviousRow();
      } else if ("FocusNextRow".equals(var1)) {
         this.focusNextRow();
      } else if ("FocusLeftCell".equals(var1)) {
         if (var2) {
            this.focusRightCell();
         } else {
            this.focusLeftCell();
         }
      } else if ("FocusRightCell".equals(var1)) {
         if (var2) {
            this.focusLeftCell();
         } else {
            this.focusRightCell();
         }
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
      } else if ("FocusPageUp".equals(var1)) {
         this.focusPageUp();
      } else if ("FocusPageDown".equals(var1)) {
         this.focusPageDown();
      } else if ("DiscontinuousSelectNextRow".equals(var1)) {
         this.discontinuousSelectNextRow();
      } else if ("DiscontinuousSelectPreviousRow".equals(var1)) {
         this.discontinuousSelectPreviousRow();
      } else if ("DiscontinuousSelectNextColumn".equals(var1)) {
         if (var2) {
            this.discontinuousSelectPreviousColumn();
         } else {
            this.discontinuousSelectNextColumn();
         }
      } else if ("DiscontinuousSelectPreviousColumn".equals(var1)) {
         if (var2) {
            this.discontinuousSelectNextColumn();
         } else {
            this.discontinuousSelectPreviousColumn();
         }
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

   public TableViewBehaviorBase(Control var1) {
      this(var1, (List)null);
   }

   public TableViewBehaviorBase(Control var1, List var2) {
      super(var1, var2 == null ? TABLE_VIEW_BINDINGS : var2);
      this.isShortcutDown = false;
      this.isShiftDown = false;
      this.selectionPathDeviated = false;
      this.selectionChanging = false;
      this.selectionHistory = new SizeLimitedList(10);
      this.selectedCellsListener = (var1x) -> {
         label63:
         while(true) {
            if (var1x.next()) {
               if (var1x.wasReplaced() && TreeTableCellBehavior.hasDefaultAnchor(this.getControl())) {
                  TreeTableCellBehavior.removeAnchor(this.getControl());
               }

               if (!var1x.wasAdded()) {
                  continue;
               }

               TableSelectionModel var2 = this.getSelectionModel();
               if (var2 == null) {
                  return;
               }

               TablePositionBase var3 = this.getAnchor();
               boolean var4 = var2.isCellSelectionEnabled();
               int var5 = var1x.getAddedSize();
               List var6 = var1x.getAddedSubList();
               Iterator var7 = var6.iterator();

               TablePositionBase var8;
               while(var7.hasNext()) {
                  var8 = (TablePositionBase)var7.next();
                  if (!this.selectionHistory.contains(var8)) {
                     this.selectionHistory.add(var8);
                  }
               }

               if (var5 > 0 && !this.hasAnchor()) {
                  TablePositionBase var9 = (TablePositionBase)var6.get(var5 - 1);
                  this.setAnchor(var9);
               }

               if (var3 == null || !var4 || this.selectionPathDeviated) {
                  continue;
               }

               int var10 = 0;

               while(true) {
                  if (var10 >= var5) {
                     continue label63;
                  }

                  var8 = (TablePositionBase)var6.get(var10);
                  if (var3.getRow() != -1 && var8.getRow() != var3.getRow() && var8.getColumn() != var3.getColumn()) {
                     this.setSelectionPathDeviated(true);
                     continue label63;
                  }

                  ++var10;
               }
            }

            return;
         }
      };
      this.weakSelectedCellsListener = new WeakListChangeListener(this.selectedCellsListener);
   }

   protected void setAnchor(TablePositionBase var1) {
      TableCellBehaviorBase.setAnchor(this.getControl(), var1, false);
      this.setSelectionPathDeviated(false);
   }

   protected TablePositionBase getAnchor() {
      return (TablePositionBase)TableCellBehaviorBase.getAnchor(this.getControl(), this.getFocusedCell());
   }

   protected boolean hasAnchor() {
      return TableCellBehaviorBase.hasNonDefaultAnchor(this.getControl());
   }

   protected abstract int getItemCount();

   protected abstract TableFocusModel getFocusModel();

   protected abstract TableSelectionModel getSelectionModel();

   protected abstract ObservableList getSelectedCells();

   protected abstract TablePositionBase getFocusedCell();

   protected abstract int getVisibleLeafIndex(TableColumnBase var1);

   protected abstract TableColumnBase getVisibleLeafColumn(int var1);

   protected abstract void editCell(int var1, TableColumnBase var2);

   protected abstract ObservableList getVisibleLeafColumns();

   protected abstract TablePositionBase getTablePosition(int var1, TableColumnBase var2);

   protected void setAnchor(int var1, TableColumnBase var2) {
      this.setAnchor(var1 == -1 && var2 == null ? null : this.getTablePosition(var1, var2));
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

   public void setOnSelectRightCell(Runnable var1) {
      this.onSelectRightCell = var1;
   }

   public void setOnSelectLeftCell(Runnable var1) {
      this.onSelectLeftCell = var1;
   }

   public void mousePressed(MouseEvent var1) {
      super.mousePressed(var1);
      if (!this.getControl().isFocused() && this.getControl().isFocusTraversable()) {
         this.getControl().requestFocus();
      }

   }

   protected boolean isRTL() {
      return this.getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
   }

   private void setSelectionPathDeviated(boolean var1) {
      this.selectionPathDeviated = var1;
   }

   protected void scrollUp() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null && !this.getSelectedCells().isEmpty()) {
         TablePositionBase var2 = (TablePositionBase)this.getSelectedCells().get(0);
         int var3 = -1;
         if (this.onScrollPageUp != null) {
            var3 = (Integer)this.onScrollPageUp.call(false);
         }

         if (var3 != -1) {
            var1.clearAndSelect(var3, var2.getTableColumn());
         }
      }
   }

   protected void scrollDown() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null && !this.getSelectedCells().isEmpty()) {
         TablePositionBase var2 = (TablePositionBase)this.getSelectedCells().get(0);
         int var3 = -1;
         if (this.onScrollPageDown != null) {
            var3 = (Integer)this.onScrollPageDown.call(false);
         }

         if (var3 != -1) {
            var1.clearAndSelect(var3, var2.getTableColumn());
         }
      }
   }

   protected void focusFirstRow() {
      TableFocusModel var1 = this.getFocusModel();
      if (var1 != null) {
         TableColumnBase var2 = this.getFocusedCell() == null ? null : this.getFocusedCell().getTableColumn();
         var1.focus(0, var2);
         if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
         }

      }
   }

   protected void focusLastRow() {
      TableFocusModel var1 = this.getFocusModel();
      if (var1 != null) {
         TableColumnBase var2 = this.getFocusedCell() == null ? null : this.getFocusedCell().getTableColumn();
         var1.focus(this.getItemCount() - 1, var2);
         if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
         }

      }
   }

   protected void focusPreviousRow() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            if (var1.isCellSelectionEnabled()) {
               var2.focusAboveCell();
            } else {
               var2.focusPrevious();
            }

            if (!this.isShortcutDown || this.getAnchor() == null) {
               this.setAnchor(var2.getFocusedIndex(), (TableColumnBase)null);
            }

            if (this.onFocusPreviousRow != null) {
               this.onFocusPreviousRow.run();
            }

         }
      }
   }

   protected void focusNextRow() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            if (var1.isCellSelectionEnabled()) {
               var2.focusBelowCell();
            } else {
               var2.focusNext();
            }

            if (!this.isShortcutDown || this.getAnchor() == null) {
               this.setAnchor(var2.getFocusedIndex(), (TableColumnBase)null);
            }

            if (this.onFocusNextRow != null) {
               this.onFocusNextRow.run();
            }

         }
      }
   }

   protected void focusLeftCell() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            var2.focusLeftCell();
            if (this.onFocusPreviousRow != null) {
               this.onFocusPreviousRow.run();
            }

         }
      }
   }

   protected void focusRightCell() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            var2.focusRightCell();
            if (this.onFocusNextRow != null) {
               this.onFocusNextRow.run();
            }

         }
      }
   }

   protected void focusPageUp() {
      int var1 = (Integer)this.onScrollPageUp.call(true);
      TableFocusModel var2 = this.getFocusModel();
      if (var2 != null) {
         TableColumnBase var3 = this.getFocusedCell() == null ? null : this.getFocusedCell().getTableColumn();
         var2.focus(var1, var3);
      }
   }

   protected void focusPageDown() {
      int var1 = (Integer)this.onScrollPageDown.call(true);
      TableFocusModel var2 = this.getFocusModel();
      if (var2 != null) {
         TableColumnBase var3 = this.getFocusedCell() == null ? null : this.getFocusedCell().getTableColumn();
         var2.focus(var1, var3);
      }
   }

   protected void clearSelection() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         var1.clearSelection();
      }
   }

   protected void clearSelectionOutsideRange(int var1, int var2, TableColumnBase var3) {
      TableSelectionModel var4 = this.getSelectionModel();
      if (var4 != null) {
         int var5 = Math.min(var1, var2);
         int var6 = Math.max(var1, var2);
         ArrayList var7 = new ArrayList(var4.getSelectedIndices());
         this.selectionChanging = true;

         for(int var8 = 0; var8 < var7.size(); ++var8) {
            int var9 = (Integer)var7.get(var8);
            if (var9 < var5 || var9 > var6) {
               var4.clearSelection(var9, var3);
            }
         }

         this.selectionChanging = false;
      }
   }

   protected void alsoSelectPrevious() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         if (var1.getSelectionMode() == SelectionMode.SINGLE) {
            this.selectPreviousRow();
         } else {
            TableFocusModel var2 = this.getFocusModel();
            if (var2 != null) {
               if (var1.isCellSelectionEnabled()) {
                  this.updateCellVerticalSelection(-1, () -> {
                     this.getSelectionModel().selectAboveCell();
                  });
               } else if (this.isShiftDown && this.hasAnchor()) {
                  this.updateRowSelection(-1);
               } else {
                  var1.selectPrevious();
               }

               this.onSelectPreviousRow.run();
            }
         }
      }
   }

   protected void alsoSelectNext() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         if (var1.getSelectionMode() == SelectionMode.SINGLE) {
            this.selectNextRow();
         } else {
            TableFocusModel var2 = this.getFocusModel();
            if (var2 != null) {
               if (var1.isCellSelectionEnabled()) {
                  this.updateCellVerticalSelection(1, () -> {
                     this.getSelectionModel().selectBelowCell();
                  });
               } else if (this.isShiftDown && this.hasAnchor()) {
                  this.updateRowSelection(1);
               } else {
                  var1.selectNext();
               }

               this.onSelectNextRow.run();
            }
         }
      }
   }

   protected void alsoSelectLeftCell() {
      this.updateCellHorizontalSelection(-1, () -> {
         this.getSelectionModel().selectLeftCell();
      });
      this.onSelectLeftCell.run();
   }

   protected void alsoSelectRightCell() {
      this.updateCellHorizontalSelection(1, () -> {
         this.getSelectionModel().selectRightCell();
      });
      this.onSelectRightCell.run();
   }

   protected void updateRowSelection(int var1) {
      TableSelectionModel var2 = this.getSelectionModel();
      if (var2 != null && var2.getSelectionMode() != SelectionMode.SINGLE) {
         TableFocusModel var3 = this.getFocusModel();
         if (var3 != null) {
            int var4 = var3.getFocusedIndex() + var1;
            TablePositionBase var5 = this.getAnchor();
            if (!this.hasAnchor()) {
               this.setAnchor(this.getFocusedCell());
            }

            if (var2.getSelectedIndices().size() > 1) {
               this.clearSelectionOutsideRange(var5.getRow(), var4, (TableColumnBase)null);
            }

            if (var5.getRow() > var4) {
               var2.selectRange(var5.getRow(), var4 - 1);
            } else {
               var2.selectRange(var5.getRow(), var4 + 1);
            }

         }
      }
   }

   protected void updateCellVerticalSelection(int var1, Runnable var2) {
      TableSelectionModel var3 = this.getSelectionModel();
      if (var3 != null && var3.getSelectionMode() != SelectionMode.SINGLE) {
         TableFocusModel var4 = this.getFocusModel();
         if (var4 != null) {
            TablePositionBase var5 = this.getFocusedCell();
            int var6 = var5.getRow();
            int var7;
            int var9;
            if (this.isShiftDown && var3.isSelected(var6 + var1, var5.getTableColumn())) {
               var7 = var6 + var1;
               boolean var11 = false;
               if (this.selectionHistory.size() >= 2) {
                  TablePositionBase var12 = (TablePositionBase)this.selectionHistory.get(1);
                  var11 = var12.getRow() == var7 && var12.getColumn() == var5.getColumn();
               }

               var9 = this.selectionPathDeviated ? (var11 ? var6 : var7) : var6;
               var3.clearSelection(var9, var5.getTableColumn());
               var4.focus(var7, var5.getTableColumn());
            } else if (this.isShiftDown && this.getAnchor() != null && !this.selectionPathDeviated) {
               var7 = var4.getFocusedIndex() + var1;
               var7 = Math.max(Math.min(this.getItemCount() - 1, var7), 0);
               int var8 = Math.min(this.getAnchor().getRow(), var7);
               var9 = Math.max(this.getAnchor().getRow(), var7);
               if (var3.getSelectedIndices().size() > 1) {
                  this.clearSelectionOutsideRange(var8, var9, var5.getTableColumn());
               }

               for(int var10 = var8; var10 <= var9; ++var10) {
                  if (!var3.isSelected(var10, var5.getTableColumn())) {
                     var3.select(var10, var5.getTableColumn());
                  }
               }

               var4.focus(var7, var5.getTableColumn());
            } else {
               var7 = var4.getFocusedIndex();
               if (!var3.isSelected(var7, var5.getTableColumn())) {
                  var3.select(var7, var5.getTableColumn());
               }

               var2.run();
            }

         }
      }
   }

   protected void updateCellHorizontalSelection(int var1, Runnable var2) {
      TableSelectionModel var3 = this.getSelectionModel();
      if (var3 != null && var3.getSelectionMode() != SelectionMode.SINGLE) {
         TableFocusModel var4 = this.getFocusModel();
         if (var4 != null) {
            TablePositionBase var5 = this.getFocusedCell();
            if (var5 != null && var5.getTableColumn() != null) {
               boolean var6 = false;
               TableColumnBase var7 = this.getColumn(var5.getTableColumn(), var1);
               if (var7 == null) {
                  var7 = var5.getTableColumn();
                  var6 = true;
               }

               int var8 = var5.getRow();
               if (this.isShiftDown && var3.isSelected(var8, var7)) {
                  if (var6) {
                     return;
                  }

                  boolean var14 = false;
                  ObservableList var15 = this.getSelectedCells();
                  if (var15.size() >= 2) {
                     TablePositionBase var16 = (TablePositionBase)var15.get(var15.size() - 2);
                     var14 = var16.getRow() == var8 && var16.getTableColumn().equals(var7);
                  }

                  TableColumnBase var17 = this.selectionPathDeviated ? (var14 ? var5.getTableColumn() : var7) : var5.getTableColumn();
                  var3.clearSelection(var8, var17);
                  var4.focus(var8, var7);
               } else if (this.isShiftDown && this.getAnchor() != null && !this.selectionPathDeviated) {
                  int var9 = this.getAnchor().getColumn();
                  int var10 = this.getVisibleLeafIndex(var5.getTableColumn()) + var1;
                  var10 = Math.max(Math.min(this.getVisibleLeafColumns().size() - 1, var10), 0);
                  int var11 = Math.min(var9, var10);
                  int var12 = Math.max(var9, var10);

                  for(int var13 = var11; var13 <= var12; ++var13) {
                     var3.select(var5.getRow(), this.getColumn(var13));
                  }

                  var4.focus(var5.getRow(), this.getColumn(var10));
               } else {
                  var2.run();
               }

            }
         }
      }
   }

   protected TableColumnBase getColumn(int var1) {
      return this.getVisibleLeafColumn(var1);
   }

   protected TableColumnBase getColumn(TableColumnBase var1, int var2) {
      return this.getVisibleLeafColumn(this.getVisibleLeafIndex(var1) + var2);
   }

   protected void selectFirstRow() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         ObservableList var2 = this.getSelectedCells();
         TableColumnBase var3 = var2.size() == 0 ? null : ((TablePositionBase)var2.get(0)).getTableColumn();
         var1.clearAndSelect(0, var3);
         if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
         }

      }
   }

   protected void selectLastRow() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         ObservableList var2 = this.getSelectedCells();
         TableColumnBase var3 = var2.size() == 0 ? null : ((TablePositionBase)var2.get(0)).getTableColumn();
         var1.clearAndSelect(this.getItemCount() - 1, var3);
         if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
         }

      }
   }

   protected void selectPreviousRow() {
      this.selectCell(-1, 0);
      if (this.onSelectPreviousRow != null) {
         this.onSelectPreviousRow.run();
      }

   }

   protected void selectNextRow() {
      this.selectCell(1, 0);
      if (this.onSelectNextRow != null) {
         this.onSelectNextRow.run();
      }

   }

   protected void selectLeftCell() {
      this.selectCell(0, -1);
      if (this.onSelectLeftCell != null) {
         this.onSelectLeftCell.run();
      }

   }

   protected void selectRightCell() {
      this.selectCell(0, 1);
      if (this.onSelectRightCell != null) {
         this.onSelectRightCell.run();
      }

   }

   protected void selectCell(int var1, int var2) {
      TableSelectionModel var3 = this.getSelectionModel();
      if (var3 != null) {
         TableFocusModel var4 = this.getFocusModel();
         if (var4 != null) {
            TablePositionBase var5 = this.getFocusedCell();
            int var6 = var5.getRow();
            int var7 = this.getVisibleLeafIndex(var5.getTableColumn());
            if (var1 >= 0 || var6 > 0) {
               if (var1 <= 0 || var6 < this.getItemCount() - 1) {
                  if (var2 >= 0 || var7 > 0) {
                     if (var2 <= 0 || var7 < this.getVisibleLeafColumns().size() - 1) {
                        if (var2 <= 0 || var7 != -1) {
                           TableColumnBase var8 = var5.getTableColumn();
                           var8 = this.getColumn(var8, var2);
                           int var9 = var5.getRow() + var1;
                           var3.clearAndSelect(var9, var8);
                           this.setAnchor(var9, var8);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected void cancelEdit() {
      this.editCell(-1, (TableColumnBase)null);
   }

   protected void activate() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            TablePositionBase var3 = this.getFocusedCell();
            var1.select(var3.getRow(), var3.getTableColumn());
            this.setAnchor(var3);
            if (var3.getRow() >= 0) {
               this.editCell(var3.getRow(), var3.getTableColumn());
            }

         }
      }
   }

   protected void selectAllToFocus(boolean var1) {
      TableSelectionModel var2 = this.getSelectionModel();
      if (var2 != null) {
         TableFocusModel var3 = this.getFocusModel();
         if (var3 != null) {
            TablePositionBase var4 = this.getFocusedCell();
            int var5 = var4.getRow();
            TablePositionBase var6 = this.getAnchor();
            int var7 = var6.getRow();
            var2.clearSelection();
            if (!var2.isCellSelectionEnabled()) {
               int var9 = var7 > var5 ? var5 - 1 : var5 + 1;
               var2.selectRange(var7, var9);
            } else {
               var2.selectRange(var6.getRow(), var6.getTableColumn(), var4.getRow(), var4.getTableColumn());
            }

            this.setAnchor(var1 ? var4 : var6);
         }
      }
   }

   protected void selectAll() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         var1.selectAll();
      }
   }

   protected void selectAllToFirstRow() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            boolean var3 = var1.getSelectionMode() == SelectionMode.SINGLE;
            TablePositionBase var4 = this.getFocusedCell();
            TableColumnBase var5 = this.getFocusedCell().getTableColumn();
            int var6 = var4.getRow();
            if (this.isShiftDown) {
               var6 = this.getAnchor() == null ? var6 : this.getAnchor().getRow();
            }

            var1.clearSelection();
            if (!var1.isCellSelectionEnabled()) {
               if (var3) {
                  var1.select(0);
               } else {
                  var1.selectRange(var6, -1);
               }

               var2.focus(0);
            } else {
               if (var3) {
                  var1.select(0, var5);
               } else {
                  var1.selectRange(var6, var5, -1, var5);
               }

               var2.focus(0, var5);
            }

            if (this.isShiftDown) {
               this.setAnchor(var6, var5);
            }

            if (this.onMoveToFirstCell != null) {
               this.onMoveToFirstCell.run();
            }

         }
      }
   }

   protected void selectAllToLastRow() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            int var3 = this.getItemCount();
            TablePositionBase var4 = this.getFocusedCell();
            TableColumnBase var5 = this.getFocusedCell().getTableColumn();
            int var6 = var4.getRow();
            if (this.isShiftDown) {
               var6 = this.getAnchor() == null ? var6 : this.getAnchor().getRow();
            }

            var1.clearSelection();
            if (!var1.isCellSelectionEnabled()) {
               var1.selectRange(var6, var3);
            } else {
               var1.selectRange(var6, var5, var3 - 1, var5);
            }

            if (this.isShiftDown) {
               this.setAnchor(var6, var5);
            }

            if (this.onMoveToLastCell != null) {
               this.onMoveToLastCell.run();
            }

         }
      }
   }

   protected void selectAllPageUp() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex();
            TableColumnBase var4 = var1.isCellSelectionEnabled() ? this.getFocusedCell().getTableColumn() : null;
            if (this.isShiftDown) {
               var3 = this.getAnchor() == null ? var3 : this.getAnchor().getRow();
               this.setAnchor(var3, var4);
            }

            int var5 = (Integer)this.onScrollPageUp.call(false);
            this.selectionChanging = true;
            if (var1.getSelectionMode() != null && var1.getSelectionMode() != SelectionMode.SINGLE) {
               var1.clearSelection();
               if (var1.isCellSelectionEnabled()) {
                  var1.selectRange(var3, var4, var5, var4);
               } else {
                  int var6 = var3 < var5 ? 1 : -1;
                  var1.selectRange(var3, var5 + var6);
               }
            } else if (var1.isCellSelectionEnabled()) {
               var1.select(var5, var4);
            } else {
               var1.select(var5);
            }

            this.selectionChanging = false;
         }
      }
   }

   protected void selectAllPageDown() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex();
            TableColumnBase var4 = var1.isCellSelectionEnabled() ? this.getFocusedCell().getTableColumn() : null;
            if (this.isShiftDown) {
               var3 = this.getAnchor() == null ? var3 : this.getAnchor().getRow();
               this.setAnchor(var3, var4);
            }

            int var5 = (Integer)this.onScrollPageDown.call(false);
            this.selectionChanging = true;
            if (var1.getSelectionMode() != null && var1.getSelectionMode() != SelectionMode.SINGLE) {
               var1.clearSelection();
               if (var1.isCellSelectionEnabled()) {
                  var1.selectRange(var3, var4, var5, var4);
               } else {
                  int var6 = var3 < var5 ? 1 : -1;
                  var1.selectRange(var3, var5 + var6);
               }
            } else if (var1.isCellSelectionEnabled()) {
               var1.select(var5, var4);
            } else {
               var1.select(var5);
            }

            this.selectionChanging = false;
         }
      }
   }

   protected void toggleFocusOwnerSelection() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            TablePositionBase var3 = this.getFocusedCell();
            if (var1.isSelected(var3.getRow(), var3.getTableColumn())) {
               var1.clearSelection(var3.getRow(), var3.getTableColumn());
               var2.focus(var3.getRow(), var3.getTableColumn());
            } else {
               var1.select(var3.getRow(), var3.getTableColumn());
            }

            this.setAnchor(var3.getRow(), var3.getTableColumn());
         }
      }
   }

   protected void discontinuousSelectPreviousRow() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         if (var1.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectPreviousRow();
         } else {
            TableFocusModel var2 = this.getFocusModel();
            if (var2 != null) {
               int var3 = var2.getFocusedIndex();
               int var4 = var3 - 1;
               if (var4 >= 0) {
                  int var5 = var3;
                  TableColumnBase var6 = var1.isCellSelectionEnabled() ? this.getFocusedCell().getTableColumn() : null;
                  if (this.isShiftDown) {
                     var5 = this.getAnchor() == null ? var3 : this.getAnchor().getRow();
                  }

                  if (!var1.isCellSelectionEnabled()) {
                     var1.selectRange(var4, var5 + 1);
                     var2.focus(var4);
                  } else {
                     for(int var7 = var4; var7 < var5 + 1; ++var7) {
                        var1.select(var7, var6);
                     }

                     var2.focus(var4, var6);
                  }

                  if (this.onFocusPreviousRow != null) {
                     this.onFocusPreviousRow.run();
                  }

               }
            }
         }
      }
   }

   protected void discontinuousSelectNextRow() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         if (var1.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectNextRow();
         } else {
            TableFocusModel var2 = this.getFocusModel();
            if (var2 != null) {
               int var3 = var2.getFocusedIndex();
               int var4 = var3 + 1;
               if (var4 < this.getItemCount()) {
                  int var5 = var3;
                  TableColumnBase var6 = var1.isCellSelectionEnabled() ? this.getFocusedCell().getTableColumn() : null;
                  if (this.isShiftDown) {
                     var5 = this.getAnchor() == null ? var3 : this.getAnchor().getRow();
                  }

                  if (!var1.isCellSelectionEnabled()) {
                     var1.selectRange(var5, var4 + 1);
                     var2.focus(var4);
                  } else {
                     for(int var7 = var5; var7 < var4 + 1; ++var7) {
                        var1.select(var7, var6);
                     }

                     var2.focus(var4, var6);
                  }

                  if (this.onFocusNextRow != null) {
                     this.onFocusNextRow.run();
                  }

               }
            }
         }
      }
   }

   protected void discontinuousSelectPreviousColumn() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null && var1.isCellSelectionEnabled()) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            TableColumnBase var3 = this.getColumn(this.getFocusedCell().getTableColumn(), -1);
            var1.select(var2.getFocusedIndex(), var3);
         }
      }
   }

   protected void discontinuousSelectNextColumn() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null && var1.isCellSelectionEnabled()) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            TableColumnBase var3 = this.getColumn(this.getFocusedCell().getTableColumn(), 1);
            var1.select(var2.getFocusedIndex(), var3);
         }
      }
   }

   protected void discontinuousSelectPageUp() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            int var3 = this.hasAnchor() ? this.getAnchor().getRow() : var2.getFocusedIndex();
            int var4 = (Integer)this.onScrollPageUp.call(false);
            if (!var1.isCellSelectionEnabled()) {
               var1.selectRange(var3, var4 - 1);
            }

         }
      }
   }

   protected void discontinuousSelectPageDown() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            int var3 = this.hasAnchor() ? this.getAnchor().getRow() : var2.getFocusedIndex();
            int var4 = (Integer)this.onScrollPageDown.call(false);
            if (!var1.isCellSelectionEnabled()) {
               var1.selectRange(var3, var4 + 1);
            }

         }
      }
   }

   protected void discontinuousSelectAllToFirstRow() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex();
            if (!var1.isCellSelectionEnabled()) {
               var1.selectRange(0, var3);
               var2.focus(0);
            } else {
               for(int var4 = 0; var4 < var3; ++var4) {
                  var1.select(var4, this.getFocusedCell().getTableColumn());
               }

               var2.focus(0, this.getFocusedCell().getTableColumn());
            }

            if (this.onMoveToFirstCell != null) {
               this.onMoveToFirstCell.run();
            }

         }
      }
   }

   protected void discontinuousSelectAllToLastRow() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         TableFocusModel var2 = this.getFocusModel();
         if (var2 != null) {
            int var3 = var2.getFocusedIndex() + 1;
            if (!var1.isCellSelectionEnabled()) {
               var1.selectRange(var3, this.getItemCount());
            } else {
               for(int var4 = var3; var4 < this.getItemCount(); ++var4) {
                  var1.select(var4, this.getFocusedCell().getTableColumn());
               }
            }

            if (this.onMoveToLastCell != null) {
               this.onMoveToLastCell.run();
            }

         }
      }
   }

   static {
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraverseNext"));
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.TAB, "TraversePrevious")).shift());
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectFirstRow"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectLastRow"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "ScrollUp"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "ScrollDown"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "SelectLeftCell"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "SelectLeftCell"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "SelectRightCell"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "SelectRightCell"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "SelectPreviousRow"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "SelectPreviousRow"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "SelectNextRow"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "SelectNextRow"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "TraverseLeft"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "SelectNextRow"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "SelectNextRow"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "TraverseUp"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "TraverseDown"));
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.HOME, "SelectAllToFirstRow")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.END, "SelectAllToLastRow")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, "SelectAllPageUp")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, "SelectAllPageDown")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.UP, "AlsoSelectPrevious")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.KP_UP, "AlsoSelectPrevious")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.DOWN, "AlsoSelectNext")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.KP_DOWN, "AlsoSelectNext")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "SelectAllToFocus")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "SelectAllToFocusAndSetAnchor")).shortcut().shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.LEFT, "AlsoSelectLeftCell")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.KP_LEFT, "AlsoSelectLeftCell")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.RIGHT, "AlsoSelectRightCell")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.KP_RIGHT, "AlsoSelectRightCell")).shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.UP, "FocusPreviousRow")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.DOWN, "FocusNextRow")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.RIGHT, "FocusRightCell")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.KP_RIGHT, "FocusRightCell")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.LEFT, "FocusLeftCell")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.KP_LEFT, "FocusLeftCell")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.A, "SelectAll")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.HOME, "FocusFirstRow")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.END, "FocusLastRow")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, "FocusPageUp")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, "FocusPageDown")).shortcut());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.UP, "DiscontinuousSelectPreviousRow")).shortcut().shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.DOWN, "DiscontinuousSelectNextRow")).shortcut().shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.LEFT, "DiscontinuousSelectPreviousColumn")).shortcut().shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.RIGHT, "DiscontinuousSelectNextColumn")).shortcut().shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, "DiscontinuousSelectPageUp")).shortcut().shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, "DiscontinuousSelectPageDown")).shortcut().shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.HOME, "DiscontinuousSelectAllToFirstRow")).shortcut().shift());
      TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.END, "DiscontinuousSelectAllToLastRow")).shortcut().shift());
      if (PlatformUtil.isMac()) {
         TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection")).ctrl().shortcut());
      } else {
         TABLE_VIEW_BINDINGS.add((new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection")).ctrl());
      }

      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ENTER, "Activate"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "Activate"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.F2, "Activate"));
      TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "CancelEdit"));
   }
}

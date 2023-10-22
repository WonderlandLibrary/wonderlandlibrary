package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public abstract class CellBehaviorBase extends BehaviorBase {
   private static final String ANCHOR_PROPERTY_KEY = "anchor";
   private static final String IS_DEFAULT_ANCHOR_KEY = "isDefaultAnchor";
   private boolean latePress = false;

   public static Object getAnchor(Control var0, Object var1) {
      return hasNonDefaultAnchor(var0) ? var0.getProperties().get("anchor") : var1;
   }

   public static void setAnchor(Control var0, Object var1, boolean var2) {
      if (var0 != null && var1 == null) {
         removeAnchor(var0);
      } else {
         var0.getProperties().put("anchor", var1);
         var0.getProperties().put("isDefaultAnchor", var2);
      }

   }

   public static boolean hasNonDefaultAnchor(Control var0) {
      Boolean var1 = (Boolean)var0.getProperties().remove("isDefaultAnchor");
      return (var1 == null || !var1) && hasAnchor(var0);
   }

   public static boolean hasDefaultAnchor(Control var0) {
      Boolean var1 = (Boolean)var0.getProperties().remove("isDefaultAnchor");
      return var1 != null && var1 && hasAnchor(var0);
   }

   private static boolean hasAnchor(Control var0) {
      return var0.getProperties().get("anchor") != null;
   }

   public static void removeAnchor(Control var0) {
      var0.getProperties().remove("anchor");
      var0.getProperties().remove("isDefaultAnchor");
   }

   public CellBehaviorBase(Cell var1, List var2) {
      super(var1, var2);
   }

   protected abstract Control getCellContainer();

   protected abstract MultipleSelectionModel getSelectionModel();

   protected abstract FocusModel getFocusModel();

   protected abstract void edit(Cell var1);

   protected boolean handleDisclosureNode(double var1, double var3) {
      return false;
   }

   protected boolean isClickPositionValid(double var1, double var3) {
      return true;
   }

   protected int getIndex() {
      return this.getControl() instanceof IndexedCell ? ((IndexedCell)this.getControl()).getIndex() : -1;
   }

   public void mousePressed(MouseEvent var1) {
      if (var1.isSynthesized()) {
         this.latePress = true;
      } else {
         this.latePress = this.isSelected();
         if (!this.latePress) {
            this.doSelect(var1.getX(), var1.getY(), var1.getButton(), var1.getClickCount(), var1.isShiftDown(), var1.isShortcutDown());
         }
      }

   }

   public void mouseReleased(MouseEvent var1) {
      if (this.latePress) {
         this.latePress = false;
         this.doSelect(var1.getX(), var1.getY(), var1.getButton(), var1.getClickCount(), var1.isShiftDown(), var1.isShortcutDown());
      }

   }

   public void mouseDragged(MouseEvent var1) {
      this.latePress = false;
   }

   protected void doSelect(double var1, double var3, MouseButton var5, int var6, boolean var7, boolean var8) {
      Cell var9 = (Cell)this.getControl();
      Control var10 = this.getCellContainer();
      if (!var9.isEmpty() && var9.contains(var1, var3)) {
         int var11 = this.getIndex();
         boolean var12 = var9.isSelected();
         MultipleSelectionModel var13 = this.getSelectionModel();
         if (var13 != null) {
            FocusModel var14 = this.getFocusModel();
            if (var14 != null) {
               if (!this.handleDisclosureNode(var1, var3)) {
                  if (this.isClickPositionValid(var1, var3)) {
                     if (var7) {
                        if (!hasNonDefaultAnchor(var10)) {
                           setAnchor(var10, var14.getFocusedIndex(), false);
                        }
                     } else {
                        removeAnchor(var10);
                     }

                     if (var5 == MouseButton.PRIMARY || var5 == MouseButton.SECONDARY && !var12) {
                        if (var13.getSelectionMode() == SelectionMode.SINGLE) {
                           this.simpleSelect(var5, var6, var8);
                        } else if (var8) {
                           if (var12) {
                              var13.clearSelection(var11);
                              var14.focus(var11);
                           } else {
                              var13.select(var11);
                           }
                        } else if (var7 && var6 == 1) {
                           int var15 = (Integer)getAnchor(var10, var14.getFocusedIndex());
                           this.selectRows(var15, var11);
                           var14.focus(var11);
                        } else {
                           this.simpleSelect(var5, var6, var8);
                        }
                     }

                  }
               }
            }
         }
      }
   }

   protected void simpleSelect(MouseButton var1, int var2, boolean var3) {
      int var4 = this.getIndex();
      MultipleSelectionModel var5 = this.getSelectionModel();
      boolean var6 = var5.isSelected(var4);
      if (var6 && var3) {
         var5.clearSelection(var4);
         this.getFocusModel().focus(var4);
         var6 = false;
      } else {
         var5.clearAndSelect(var4);
      }

      this.handleClicks(var1, var2, var6);
   }

   protected void handleClicks(MouseButton var1, int var2, boolean var3) {
      if (var1 == MouseButton.PRIMARY) {
         if (var2 == 1 && var3) {
            this.edit((Cell)this.getControl());
         } else if (var2 == 1) {
            this.edit((Cell)null);
         } else if (var2 == 2 && ((Cell)this.getControl()).isEditable()) {
            this.edit((Cell)this.getControl());
         }
      }

   }

   void selectRows(int var1, int var2) {
      boolean var3 = var1 < var2;
      int var4 = Math.min(var1, var2);
      int var5 = Math.max(var1, var2);
      ArrayList var6 = new ArrayList(this.getSelectionModel().getSelectedIndices());
      int var7 = 0;

      for(int var8 = var6.size(); var7 < var8; ++var7) {
         int var9 = (Integer)var6.get(var7);
         if (var9 < var4 || var9 > var5) {
            this.getSelectionModel().clearSelection(var9);
         }
      }

      if (var4 == var5) {
         this.getSelectionModel().select(var4);
      } else if (var3) {
         this.getSelectionModel().selectRange(var4, var5 + 1);
      } else {
         this.getSelectionModel().selectRange(var5, var4 - 1);
      }

   }

   protected boolean isSelected() {
      return ((Cell)this.getControl()).isSelected();
   }
}

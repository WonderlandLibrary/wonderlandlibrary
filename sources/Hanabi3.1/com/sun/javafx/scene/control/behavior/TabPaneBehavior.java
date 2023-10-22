package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class TabPaneBehavior extends BehaviorBase {
   private static final String HOME = "Home";
   private static final String END = "End";
   private static final String CTRL_PAGE_UP = "Ctrl_Page_Up";
   private static final String CTRL_PAGE_DOWN = "Ctrl_Page_Down";
   private static final String CTRL_TAB = "Ctrl_Tab";
   private static final String CTRL_SHIFT_TAB = "Ctrl_Shift_Tab";
   protected static final List TAB_PANE_BINDINGS = new ArrayList();

   protected void callAction(String var1) {
      boolean var2 = ((TabPane)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
      if ((!"TraverseLeft".equals(var1) || var2) && (!"TraverseRight".equals(var1) || !var2) && !"TraverseUp".equals(var1)) {
         if ((!"TraverseRight".equals(var1) || var2) && (!"TraverseLeft".equals(var1) || !var2) && !"TraverseDown".equals(var1)) {
            if (!"Ctrl_Tab".equals(var1) && !"Ctrl_Page_Down".equals(var1)) {
               if (!"Ctrl_Shift_Tab".equals(var1) && !"Ctrl_Page_Up".equals(var1)) {
                  if ("Home".equals(var1)) {
                     if (((TabPane)this.getControl()).isFocused()) {
                        this.moveSelection(0, 1);
                     }
                  } else if ("End".equals(var1)) {
                     if (((TabPane)this.getControl()).isFocused()) {
                        this.moveSelection(((TabPane)this.getControl()).getTabs().size() - 1, -1);
                     }
                  } else {
                     super.callAction(var1);
                  }
               } else {
                  this.selectPreviousTab();
               }
            } else {
               this.selectNextTab();
            }
         } else if (((TabPane)this.getControl()).isFocused()) {
            this.selectNextTab();
         }
      } else if (((TabPane)this.getControl()).isFocused()) {
         this.selectPreviousTab();
      }

   }

   public void mousePressed(MouseEvent var1) {
      super.mousePressed(var1);
      TabPane var2 = (TabPane)this.getControl();
      var2.requestFocus();
   }

   public TabPaneBehavior(TabPane var1) {
      super(var1, TAB_PANE_BINDINGS);
   }

   public void selectTab(Tab var1) {
      ((TabPane)this.getControl()).getSelectionModel().select(var1);
   }

   public boolean canCloseTab(Tab var1) {
      Event var2 = new Event(var1, var1, Tab.TAB_CLOSE_REQUEST_EVENT);
      Event.fireEvent(var1, var2);
      return !var2.isConsumed();
   }

   public void closeTab(Tab var1) {
      TabPane var2 = (TabPane)this.getControl();
      int var3 = var2.getTabs().indexOf(var1);
      if (var3 != -1) {
         var2.getTabs().remove(var3);
      }

      if (var1.getOnClosed() != null) {
         Event.fireEvent(var1, new Event(Tab.CLOSED_EVENT));
      }

   }

   public void selectNextTab() {
      this.moveSelection(1);
   }

   public void selectPreviousTab() {
      this.moveSelection(-1);
   }

   private void moveSelection(int var1) {
      this.moveSelection(((TabPane)this.getControl()).getSelectionModel().getSelectedIndex(), var1);
   }

   private void moveSelection(int var1, int var2) {
      TabPane var3 = (TabPane)this.getControl();
      int var4 = this.findValidTab(var1, var2);
      if (var4 > -1) {
         SingleSelectionModel var5 = var3.getSelectionModel();
         var5.select(var4);
      }

      var3.requestFocus();
   }

   private int findValidTab(int var1, int var2) {
      TabPane var3 = (TabPane)this.getControl();
      ObservableList var4 = var3.getTabs();
      int var5 = var4.size();
      int var6 = var1;

      do {
         var6 = this.nextIndex(var6 + var2, var5);
         Tab var7 = (Tab)var4.get(var6);
         if (var7 != null && !var7.isDisable()) {
            return var6;
         }
      } while(var6 != var1);

      return -1;
   }

   private int nextIndex(int var1, int var2) {
      int var4 = var1 % var2;
      if (var4 > 0 && var2 < 0) {
         var4 = var4 + var2 - 0;
      } else if (var4 < 0 && var2 > 0) {
         var4 = var4 + var2 - 0;
      }

      return var4;
   }

   static {
      TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
      TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
      TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
      TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
      TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.HOME, "Home"));
      TAB_PANE_BINDINGS.add(new KeyBinding(KeyCode.END, "End"));
      TAB_PANE_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, "Ctrl_Page_Up")).ctrl());
      TAB_PANE_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, "Ctrl_Page_Down")).ctrl());
      TAB_PANE_BINDINGS.add((new KeyBinding(KeyCode.TAB, "Ctrl_Tab")).ctrl());
      TAB_PANE_BINDINGS.add((new KeyBinding(KeyCode.TAB, "Ctrl_Shift_Tab")).shift().ctrl());
   }
}

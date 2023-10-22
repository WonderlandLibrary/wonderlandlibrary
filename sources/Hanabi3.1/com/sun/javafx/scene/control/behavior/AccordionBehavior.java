package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Accordion;
import javafx.scene.control.FocusModel;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class AccordionBehavior extends BehaviorBase {
   private AccordionFocusModel focusModel;
   private static final String HOME = "Home";
   private static final String END = "End";
   private static final String PAGE_UP = "Page_Up";
   private static final String PAGE_DOWN = "Page_Down";
   private static final String CTRL_PAGE_UP = "Ctrl_Page_Up";
   private static final String CTRL_PAGE_DOWN = "Ctrl_Page_Down";
   private static final String CTRL_TAB = "Ctrl_Tab";
   private static final String CTRL_SHIFT_TAB = "Ctrl_Shift_Tab";
   protected static final List ACCORDION_BINDINGS = new ArrayList();

   public AccordionBehavior(Accordion var1) {
      super(var1, ACCORDION_BINDINGS);
      this.focusModel = new AccordionFocusModel(var1);
   }

   public void dispose() {
      this.focusModel.dispose();
      super.dispose();
   }

   protected void callAction(String var1) {
      Accordion var2 = (Accordion)this.getControl();
      boolean var3 = var2.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
      int var4;
      if ((!"TraverseLeft".equals(var1) || var3) && (!"TraverseRight".equals(var1) || !var3) && !"TraverseUp".equals(var1) && !"Page_Up".equals(var1)) {
         if ((!"TraverseRight".equals(var1) || var3) && (!"TraverseLeft".equals(var1) || !var3) && !"TraverseDown".equals(var1) && !"Page_Down".equals(var1)) {
            if (!"Ctrl_Tab".equals(var1) && !"Ctrl_Page_Down".equals(var1)) {
               if (!"Ctrl_Shift_Tab".equals(var1) && !"Ctrl_Page_Up".equals(var1)) {
                  TitledPane var5;
                  if ("Home".equals(var1)) {
                     if (this.focusModel.getFocusedIndex() != -1 && ((TitledPane)var2.getPanes().get(this.focusModel.getFocusedIndex())).isFocused()) {
                        var5 = (TitledPane)var2.getPanes().get(0);
                        var5.requestFocus();
                        var5.setExpanded(!var5.isExpanded());
                     }
                  } else if ("End".equals(var1)) {
                     if (this.focusModel.getFocusedIndex() != -1 && ((TitledPane)var2.getPanes().get(this.focusModel.getFocusedIndex())).isFocused()) {
                        var5 = (TitledPane)var2.getPanes().get(var2.getPanes().size() - 1);
                        var5.requestFocus();
                        var5.setExpanded(!var5.isExpanded());
                     }
                  } else {
                     super.callAction(var1);
                  }
               } else {
                  this.focusModel.focusPrevious();
                  if (this.focusModel.getFocusedIndex() != -1) {
                     var4 = this.focusModel.getFocusedIndex();
                     ((TitledPane)var2.getPanes().get(var4)).requestFocus();
                     ((TitledPane)var2.getPanes().get(var4)).setExpanded(true);
                  }
               }
            } else {
               this.focusModel.focusNext();
               if (this.focusModel.getFocusedIndex() != -1) {
                  var4 = this.focusModel.getFocusedIndex();
                  ((TitledPane)var2.getPanes().get(var4)).requestFocus();
                  ((TitledPane)var2.getPanes().get(var4)).setExpanded(true);
               }
            }
         } else if (this.focusModel.getFocusedIndex() != -1 && ((TitledPane)var2.getPanes().get(this.focusModel.getFocusedIndex())).isFocused()) {
            this.focusModel.focusNext();
            var4 = this.focusModel.getFocusedIndex();
            ((TitledPane)var2.getPanes().get(var4)).requestFocus();
            if ("Page_Down".equals(var1)) {
               ((TitledPane)var2.getPanes().get(var4)).setExpanded(true);
            }
         }
      } else if (this.focusModel.getFocusedIndex() != -1 && ((TitledPane)var2.getPanes().get(this.focusModel.getFocusedIndex())).isFocused()) {
         this.focusModel.focusPrevious();
         var4 = this.focusModel.getFocusedIndex();
         ((TitledPane)var2.getPanes().get(var4)).requestFocus();
         if ("Page_Up".equals(var1)) {
            ((TitledPane)var2.getPanes().get(var4)).setExpanded(true);
         }
      }

   }

   public void mousePressed(MouseEvent var1) {
      Accordion var2 = (Accordion)this.getControl();
      if (var2.getPanes().size() > 0) {
         TitledPane var3 = (TitledPane)var2.getPanes().get(var2.getPanes().size() - 1);
         var3.requestFocus();
      } else {
         var2.requestFocus();
      }

   }

   static {
      ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
      ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
      ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
      ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
      ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.HOME, "Home"));
      ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.END, "End"));
      ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "Page_Up"));
      ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "Page_Down"));
      ACCORDION_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, "Ctrl_Page_Up")).ctrl());
      ACCORDION_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, "Ctrl_Page_Down")).ctrl());
      ACCORDION_BINDINGS.add((new KeyBinding(KeyCode.TAB, "Ctrl_Tab")).ctrl());
      ACCORDION_BINDINGS.add((new KeyBinding(KeyCode.TAB, "Ctrl_Shift_Tab")).shift().ctrl());
   }

   static class AccordionFocusModel extends FocusModel {
      private final Accordion accordion;
      private final ChangeListener focusListener = new ChangeListener() {
         public void changed(ObservableValue var1, Boolean var2, Boolean var3) {
            if (var3) {
               if (AccordionFocusModel.this.accordion.getExpandedPane() != null) {
                  AccordionFocusModel.this.accordion.getExpandedPane().requestFocus();
               } else if (!AccordionFocusModel.this.accordion.getPanes().isEmpty()) {
                  ((TitledPane)AccordionFocusModel.this.accordion.getPanes().get(0)).requestFocus();
               }
            }

         }
      };
      private final ChangeListener paneFocusListener = new ChangeListener() {
         public void changed(ObservableValue var1, Boolean var2, Boolean var3) {
            if (var3) {
               ReadOnlyBooleanProperty var4 = (ReadOnlyBooleanProperty)var1;
               TitledPane var5 = (TitledPane)var4.getBean();
               AccordionFocusModel.this.focus(AccordionFocusModel.this.accordion.getPanes().indexOf(var5));
            }

         }
      };
      private final ListChangeListener panesListener = (var1x) -> {
         label30:
         while(true) {
            if (var1x.next()) {
               Iterator var2;
               TitledPane var3;
               if (var1x.wasAdded()) {
                  var2 = var1x.getAddedSubList().iterator();

                  while(true) {
                     if (!var2.hasNext()) {
                        continue label30;
                     }

                     var3 = (TitledPane)var2.next();
                     var3.focusedProperty().addListener(this.paneFocusListener);
                  }
               }

               if (!var1x.wasRemoved()) {
                  continue;
               }

               var2 = var1x.getAddedSubList().iterator();

               while(true) {
                  if (!var2.hasNext()) {
                     continue label30;
                  }

                  var3 = (TitledPane)var2.next();
                  var3.focusedProperty().removeListener(this.paneFocusListener);
               }
            }

            return;
         }
      };

      public AccordionFocusModel(Accordion var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("Accordion can not be null");
         } else {
            this.accordion = var1;
            this.accordion.focusedProperty().addListener(this.focusListener);
            this.accordion.getPanes().addListener(this.panesListener);
            Iterator var2 = this.accordion.getPanes().iterator();

            while(var2.hasNext()) {
               TitledPane var3 = (TitledPane)var2.next();
               var3.focusedProperty().addListener(this.paneFocusListener);
            }

         }
      }

      void dispose() {
         this.accordion.focusedProperty().removeListener(this.focusListener);
         this.accordion.getPanes().removeListener(this.panesListener);
         Iterator var1 = this.accordion.getPanes().iterator();

         while(var1.hasNext()) {
            TitledPane var2 = (TitledPane)var1.next();
            var2.focusedProperty().removeListener(this.paneFocusListener);
         }

      }

      protected int getItemCount() {
         ObservableList var1 = this.accordion.getPanes();
         return var1 == null ? 0 : var1.size();
      }

      protected TitledPane getModelItem(int var1) {
         ObservableList var2 = this.accordion.getPanes();
         if (var2 == null) {
            return null;
         } else {
            return var1 < 0 ? null : (TitledPane)var2.get(var1 % var2.size());
         }
      }

      public void focusPrevious() {
         if (this.getFocusedIndex() <= 0) {
            this.focus(this.accordion.getPanes().size() - 1);
         } else {
            this.focus((this.getFocusedIndex() - 1) % this.accordion.getPanes().size());
         }

      }

      public void focusNext() {
         if (this.getFocusedIndex() == -1) {
            this.focus(0);
         } else {
            this.focus((this.getFocusedIndex() + 1) % this.accordion.getPanes().size());
         }

      }
   }
}

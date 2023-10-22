package javafx.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.Node;

public abstract class ButtonBase extends Labeled {
   private ReadOnlyBooleanWrapper armed = new ReadOnlyBooleanWrapper() {
      protected void invalidated() {
         ButtonBase.this.pseudoClassStateChanged(ButtonBase.ARMED_PSEUDOCLASS_STATE, this.get());
      }

      public Object getBean() {
         return ButtonBase.this;
      }

      public String getName() {
         return "armed";
      }
   };
   private ObjectProperty onAction = new ObjectPropertyBase() {
      protected void invalidated() {
         ButtonBase.this.setEventHandler(ActionEvent.ACTION, (EventHandler)this.get());
      }

      public Object getBean() {
         return ButtonBase.this;
      }

      public String getName() {
         return "onAction";
      }
   };
   private static final PseudoClass ARMED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("armed");

   public ButtonBase() {
   }

   public ButtonBase(String var1) {
      super(var1);
   }

   public ButtonBase(String var1, Node var2) {
      super(var1, var2);
   }

   public final ReadOnlyBooleanProperty armedProperty() {
      return this.armed.getReadOnlyProperty();
   }

   private void setArmed(boolean var1) {
      this.armed.set(var1);
   }

   public final boolean isArmed() {
      return this.armedProperty().get();
   }

   public final ObjectProperty onActionProperty() {
      return this.onAction;
   }

   public final void setOnAction(EventHandler var1) {
      this.onActionProperty().set(var1);
   }

   public final EventHandler getOnAction() {
      return (EventHandler)this.onActionProperty().get();
   }

   public void arm() {
      this.setArmed(true);
   }

   public void disarm() {
      this.setArmed(false);
   }

   public abstract void fire();

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case FIRE:
            this.fire();
            break;
         default:
            super.executeAccessibleAction(var1, new Object[0]);
      }

   }
}

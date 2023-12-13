package com.sun.javafx.scene.control.skin;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;

abstract class InputField extends Control {
   public static final int DEFAULT_PREF_COLUMN_COUNT = 12;
   private BooleanProperty editable = new SimpleBooleanProperty(this, "editable", true);
   private StringProperty promptText = new StringPropertyBase("") {
      protected void invalidated() {
         String var1 = this.get();
         if (var1 != null && var1.contains("\n")) {
            var1 = var1.replace("\n", "");
            this.set(var1);
         }

      }

      public Object getBean() {
         return InputField.this;
      }

      public String getName() {
         return "promptText";
      }
   };
   private IntegerProperty prefColumnCount = new IntegerPropertyBase(12) {
      private int oldValue = this.get();

      protected void invalidated() {
         int var1 = this.get();
         if (var1 < 0) {
            if (this.isBound()) {
               this.unbind();
            }

            this.set(this.oldValue);
            throw new IllegalArgumentException("value cannot be negative.");
         } else {
            this.oldValue = var1;
         }
      }

      public Object getBean() {
         return InputField.this;
      }

      public String getName() {
         return "prefColumnCount";
      }
   };
   private ObjectProperty onAction = new ObjectPropertyBase() {
      protected void invalidated() {
         InputField.this.setEventHandler(ActionEvent.ACTION, (EventHandler)this.get());
      }

      public Object getBean() {
         return InputField.this;
      }

      public String getName() {
         return "onAction";
      }
   };

   public final boolean isEditable() {
      return this.editable.getValue();
   }

   public final void setEditable(boolean var1) {
      this.editable.setValue(var1);
   }

   public final BooleanProperty editableProperty() {
      return this.editable;
   }

   public final StringProperty promptTextProperty() {
      return this.promptText;
   }

   public final String getPromptText() {
      return (String)this.promptText.get();
   }

   public final void setPromptText(String var1) {
      this.promptText.set(var1);
   }

   public final IntegerProperty prefColumnCountProperty() {
      return this.prefColumnCount;
   }

   public final int getPrefColumnCount() {
      return this.prefColumnCount.getValue();
   }

   public final void setPrefColumnCount(int var1) {
      this.prefColumnCount.setValue((Number)var1);
   }

   public final ObjectProperty onActionProperty() {
      return this.onAction;
   }

   public final EventHandler getOnAction() {
      return (EventHandler)this.onActionProperty().get();
   }

   public final void setOnAction(EventHandler var1) {
      this.onActionProperty().set(var1);
   }

   public InputField() {
      this.getStyleClass().setAll((Object[])("input-field"));
   }
}

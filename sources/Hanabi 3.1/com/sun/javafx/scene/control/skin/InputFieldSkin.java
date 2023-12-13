package com.sun.javafx.scene.control.skin;

import com.sun.javafx.event.EventDispatchChainImpl;
import java.util.Collection;
import javafx.beans.InvalidationListener;
import javafx.event.EventDispatchChain;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

abstract class InputFieldSkin implements Skin {
   protected InputField control;
   private InnerTextField textField;
   private InvalidationListener InputFieldFocusListener;
   private InvalidationListener InputFieldStyleClassListener;

   public InputFieldSkin(InputField var1) {
      this.control = var1;
      this.textField = new InnerTextField() {
         public void replaceText(int var1, int var2, String var3) {
            String var4 = InputFieldSkin.this.textField.getText() == null ? "" : InputFieldSkin.this.textField.getText();
            var4 = var4.substring(0, var1) + var3 + var4.substring(var2);
            if (InputFieldSkin.this.accept(var4)) {
               super.replaceText(var1, var2, var3);
            }

         }

         public void replaceSelection(String var1) {
            String var2 = InputFieldSkin.this.textField.getText() == null ? "" : InputFieldSkin.this.textField.getText();
            int var3 = Math.min(InputFieldSkin.this.textField.getAnchor(), InputFieldSkin.this.textField.getCaretPosition());
            int var4 = Math.max(InputFieldSkin.this.textField.getAnchor(), InputFieldSkin.this.textField.getCaretPosition());
            var2 = var2.substring(0, var3) + var1 + var2.substring(var4);
            if (InputFieldSkin.this.accept(var2)) {
               super.replaceSelection(var1);
            }

         }
      };
      this.textField.setId("input-text-field");
      this.textField.setFocusTraversable(false);
      var1.getStyleClass().addAll(this.textField.getStyleClass());
      this.textField.getStyleClass().setAll((Collection)var1.getStyleClass());
      var1.getStyleClass().addListener(this.InputFieldStyleClassListener = (var2) -> {
         this.textField.getStyleClass().setAll((Collection)var1.getStyleClass());
      });
      this.textField.promptTextProperty().bind(var1.promptTextProperty());
      this.textField.prefColumnCountProperty().bind(var1.prefColumnCountProperty());
      this.textField.textProperty().addListener((var1x) -> {
         this.updateValue();
      });
      var1.focusedProperty().addListener(this.InputFieldFocusListener = (var2) -> {
         this.textField.handleFocus(var1.isFocused());
      });
      this.updateText();
   }

   public InputField getSkinnable() {
      return this.control;
   }

   public Node getNode() {
      return this.textField;
   }

   public void dispose() {
      this.control.getStyleClass().removeListener(this.InputFieldStyleClassListener);
      this.control.focusedProperty().removeListener(this.InputFieldFocusListener);
      this.textField = null;
   }

   protected abstract boolean accept(String var1);

   protected abstract void updateText();

   protected abstract void updateValue();

   protected TextField getTextField() {
      return this.textField;
   }

   private class InnerTextField extends TextField {
      private InnerTextField() {
      }

      public void handleFocus(boolean var1) {
         this.setFocused(var1);
      }

      public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
         EventDispatchChainImpl var2 = new EventDispatchChainImpl();
         var2.append(InputFieldSkin.this.textField.getEventDispatcher());
         return var2;
      }

      // $FF: synthetic method
      InnerTextField(Object var2) {
         this();
      }
   }
}

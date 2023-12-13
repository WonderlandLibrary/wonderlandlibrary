package javafx.scene.control;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class TextFieldBuilder extends TextInputControlBuilder implements Builder {
   private int __set;
   private Pos alignment;
   private EventHandler onAction;
   private int prefColumnCount;
   private String promptText;

   protected TextFieldBuilder() {
   }

   public static TextFieldBuilder create() {
      return new TextFieldBuilder();
   }

   public void applyTo(TextField var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAlignment(this.alignment);
      }

      if ((var2 & 2) != 0) {
         var1.setOnAction(this.onAction);
      }

      if ((var2 & 4) != 0) {
         var1.setPrefColumnCount(this.prefColumnCount);
      }

      if ((var2 & 8) != 0) {
         var1.setPromptText(this.promptText);
      }

   }

   public TextFieldBuilder alignment(Pos var1) {
      this.alignment = var1;
      this.__set |= 1;
      return this;
   }

   public TextFieldBuilder onAction(EventHandler var1) {
      this.onAction = var1;
      this.__set |= 2;
      return this;
   }

   public TextFieldBuilder prefColumnCount(int var1) {
      this.prefColumnCount = var1;
      this.__set |= 4;
      return this;
   }

   public TextFieldBuilder promptText(String var1) {
      this.promptText = var1;
      this.__set |= 8;
      return this;
   }

   public TextField build() {
      TextField var1 = new TextField();
      this.applyTo(var1);
      return var1;
   }
}

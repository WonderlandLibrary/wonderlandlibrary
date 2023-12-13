package javafx.scene.control;

/** @deprecated */
@Deprecated
public abstract class TextInputControlBuilder extends ControlBuilder {
   private int __set;
   private boolean editable;
   private String promptText;
   private String text;

   protected TextInputControlBuilder() {
   }

   public void applyTo(TextInputControl var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setEditable(this.editable);
      }

      if ((var2 & 2) != 0) {
         var1.setPromptText(this.promptText);
      }

      if ((var2 & 4) != 0) {
         var1.setText(this.text);
      }

   }

   public TextInputControlBuilder editable(boolean var1) {
      this.editable = var1;
      this.__set |= 1;
      return this;
   }

   public TextInputControlBuilder promptText(String var1) {
      this.promptText = var1;
      this.__set |= 2;
      return this;
   }

   public TextInputControlBuilder text(String var1) {
      this.text = var1;
      this.__set |= 4;
      return this;
   }
}

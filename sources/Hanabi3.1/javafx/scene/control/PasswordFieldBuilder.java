package javafx.scene.control;

/** @deprecated */
@Deprecated
public class PasswordFieldBuilder extends TextFieldBuilder {
   private boolean __set;
   private String promptText;

   protected PasswordFieldBuilder() {
   }

   public static PasswordFieldBuilder create() {
      return new PasswordFieldBuilder();
   }

   public void applyTo(PasswordField var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setPromptText(this.promptText);
      }

   }

   public PasswordFieldBuilder promptText(String var1) {
      this.promptText = var1;
      this.__set = true;
      return this;
   }

   public PasswordField build() {
      PasswordField var1 = new PasswordField();
      this.applyTo(var1);
      return var1;
   }
}

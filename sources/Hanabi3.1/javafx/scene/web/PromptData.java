package javafx.scene.web;

import javafx.beans.NamedArg;

public final class PromptData {
   private final String message;
   private final String defaultValue;

   public PromptData(@NamedArg("message") String var1, @NamedArg("defaultValue") String var2) {
      this.message = var1;
      this.defaultValue = var2;
   }

   public final String getMessage() {
      return this.message;
   }

   public final String getDefaultValue() {
      return this.defaultValue;
   }
}

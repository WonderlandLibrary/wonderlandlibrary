package javafx.scene.input;

import com.sun.javafx.tk.Toolkit;
import javafx.beans.NamedArg;

public final class KeyCharacterCombination extends KeyCombination {
   private String character = "";

   public final String getCharacter() {
      return this.character;
   }

   public KeyCharacterCombination(@NamedArg("character") String var1, @NamedArg("shift") KeyCombination.ModifierValue var2, @NamedArg("control") KeyCombination.ModifierValue var3, @NamedArg("alt") KeyCombination.ModifierValue var4, @NamedArg("meta") KeyCombination.ModifierValue var5, @NamedArg("shortcut") KeyCombination.ModifierValue var6) {
      super(var2, var3, var4, var5, var6);
      validateKeyCharacter(var1);
      this.character = var1;
   }

   public KeyCharacterCombination(@NamedArg("character") String var1, @NamedArg("modifiers") KeyCombination.Modifier... var2) {
      super(var2);
      validateKeyCharacter(var1);
      this.character = var1;
   }

   public boolean match(KeyEvent var1) {
      if (var1.getCode() == KeyCode.UNDEFINED) {
         return false;
      } else {
         return var1.getCode().impl_getCode() == Toolkit.getToolkit().getKeyCodeForChar(this.getCharacter()) && super.match(var1);
      }
   }

   public String getName() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.getName());
      if (var1.length() > 0) {
         var1.append("+");
      }

      return var1.append('\'').append(this.character.replace("'", "\\'")).append('\'').toString();
   }

   public String getDisplayText() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.getDisplayText());
      var1.append(this.getCharacter());
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof KeyCharacterCombination)) {
         return false;
      } else {
         return this.character.equals(((KeyCharacterCombination)var1).getCharacter()) && super.equals(var1);
      }
   }

   public int hashCode() {
      return 23 * super.hashCode() + this.character.hashCode();
   }

   private static void validateKeyCharacter(String var0) {
      if (var0 == null) {
         throw new NullPointerException("Key character must not be null!");
      }
   }
}

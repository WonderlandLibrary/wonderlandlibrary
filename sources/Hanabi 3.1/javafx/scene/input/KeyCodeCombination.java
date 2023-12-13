package javafx.scene.input;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.util.Utils;
import javafx.beans.NamedArg;

public final class KeyCodeCombination extends KeyCombination {
   private KeyCode code;

   public final KeyCode getCode() {
      return this.code;
   }

   public KeyCodeCombination(@NamedArg("code") KeyCode var1, @NamedArg("shift") KeyCombination.ModifierValue var2, @NamedArg("control") KeyCombination.ModifierValue var3, @NamedArg("alt") KeyCombination.ModifierValue var4, @NamedArg("meta") KeyCombination.ModifierValue var5, @NamedArg("shortcut") KeyCombination.ModifierValue var6) {
      super(var2, var3, var4, var5, var6);
      validateKeyCode(var1);
      this.code = var1;
   }

   public KeyCodeCombination(@NamedArg("code") KeyCode var1, @NamedArg("modifiers") KeyCombination.Modifier... var2) {
      super(var2);
      validateKeyCode(var1);
      this.code = var1;
   }

   public boolean match(KeyEvent var1) {
      return var1.getCode() == this.getCode() && super.match(var1);
   }

   public String getName() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.getName());
      if (var1.length() > 0) {
         var1.append("+");
      }

      return var1.append(this.code.getName()).toString();
   }

   public String getDisplayText() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.getDisplayText());
      int var2 = var1.length();
      char var3 = getSingleChar(this.code);
      if (var3 != 0) {
         var1.append(var3);
         return var1.toString();
      } else {
         String var4 = this.code.toString();
         String[] var5 = Utils.split(var4, "_");
         String[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            if (var1.length() > var2) {
               var1.append(' ');
            }

            var1.append(var9.charAt(0));
            var1.append(var9.substring(1).toLowerCase());
         }

         return var1.toString();
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof KeyCodeCombination)) {
         return false;
      } else {
         return this.getCode() == ((KeyCodeCombination)var1).getCode() && super.equals(var1);
      }
   }

   public int hashCode() {
      return 23 * super.hashCode() + this.code.hashCode();
   }

   private static void validateKeyCode(KeyCode var0) {
      if (var0 == null) {
         throw new NullPointerException("Key code must not be null!");
      } else if (getModifier(var0.getName()) != null) {
         throw new IllegalArgumentException("Key code must not match modifier key!");
      } else if (var0 == KeyCode.UNDEFINED) {
         throw new IllegalArgumentException("Key code must differ from undefined value!");
      }
   }

   private static char getSingleChar(KeyCode var0) {
      switch (var0) {
         case ENTER:
            return '↵';
         case LEFT:
            return '←';
         case UP:
            return '↑';
         case RIGHT:
            return '→';
         case DOWN:
            return '↓';
         case COMMA:
            return ',';
         case MINUS:
            return '-';
         case PERIOD:
            return '.';
         case SLASH:
            return '/';
         case SEMICOLON:
            return ';';
         case EQUALS:
            return '=';
         case OPEN_BRACKET:
            return '[';
         case BACK_SLASH:
            return '\\';
         case CLOSE_BRACKET:
            return ']';
         case MULTIPLY:
            return '*';
         case ADD:
            return '+';
         case SUBTRACT:
            return '-';
         case DECIMAL:
            return '.';
         case DIVIDE:
            return '/';
         case BACK_QUOTE:
            return '`';
         case QUOTE:
            return '"';
         case AMPERSAND:
            return '&';
         case ASTERISK:
            return '*';
         case LESS:
            return '<';
         case GREATER:
            return '>';
         case BRACELEFT:
            return '{';
         case BRACERIGHT:
            return '}';
         case AT:
            return '@';
         case COLON:
            return ':';
         case CIRCUMFLEX:
            return '^';
         case DOLLAR:
            return '$';
         case EURO_SIGN:
            return '€';
         case EXCLAMATION_MARK:
            return '!';
         case LEFT_PARENTHESIS:
            return '(';
         case NUMBER_SIGN:
            return '#';
         case PLUS:
            return '+';
         case RIGHT_PARENTHESIS:
            return ')';
         case UNDERSCORE:
            return '_';
         case DIGIT0:
            return '0';
         case DIGIT1:
            return '1';
         case DIGIT2:
            return '2';
         case DIGIT3:
            return '3';
         case DIGIT4:
            return '4';
         case DIGIT5:
            return '5';
         case DIGIT6:
            return '6';
         case DIGIT7:
            return '7';
         case DIGIT8:
            return '8';
         case DIGIT9:
            return '9';
         default:
            if (PlatformUtil.isMac()) {
               switch (var0) {
                  case BACK_SPACE:
                     return '⌫';
                  case ESCAPE:
                     return '⎋';
                  case DELETE:
                     return '⌦';
               }
            }

            return '\u0000';
      }
   }
}

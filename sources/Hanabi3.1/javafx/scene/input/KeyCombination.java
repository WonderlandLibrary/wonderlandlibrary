package javafx.scene.input;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.Locale;

public abstract class KeyCombination {
   public static final Modifier SHIFT_DOWN;
   public static final Modifier SHIFT_ANY;
   public static final Modifier CONTROL_DOWN;
   public static final Modifier CONTROL_ANY;
   public static final Modifier ALT_DOWN;
   public static final Modifier ALT_ANY;
   public static final Modifier META_DOWN;
   public static final Modifier META_ANY;
   public static final Modifier SHORTCUT_DOWN;
   public static final Modifier SHORTCUT_ANY;
   private static final Modifier[] POSSIBLE_MODIFIERS;
   public static final KeyCombination NO_MATCH;
   private final ModifierValue shift;
   private final ModifierValue control;
   private final ModifierValue alt;
   private final ModifierValue meta;
   private final ModifierValue shortcut;

   public final ModifierValue getShift() {
      return this.shift;
   }

   public final ModifierValue getControl() {
      return this.control;
   }

   public final ModifierValue getAlt() {
      return this.alt;
   }

   public final ModifierValue getMeta() {
      return this.meta;
   }

   public final ModifierValue getShortcut() {
      return this.shortcut;
   }

   protected KeyCombination(ModifierValue var1, ModifierValue var2, ModifierValue var3, ModifierValue var4, ModifierValue var5) {
      if (var1 != null && var2 != null && var3 != null && var4 != null && var5 != null) {
         this.shift = var1;
         this.control = var2;
         this.alt = var3;
         this.meta = var4;
         this.shortcut = var5;
      } else {
         throw new NullPointerException("Modifier value must not be null!");
      }
   }

   protected KeyCombination(Modifier... var1) {
      this(getModifierValue(var1, KeyCode.SHIFT), getModifierValue(var1, KeyCode.CONTROL), getModifierValue(var1, KeyCode.ALT), getModifierValue(var1, KeyCode.META), getModifierValue(var1, KeyCode.SHORTCUT));
   }

   public boolean match(KeyEvent var1) {
      KeyCode var2 = Toolkit.getToolkit().getPlatformShortcutKey();
      return test(KeyCode.SHIFT, this.shift, var2, this.shortcut, var1.isShiftDown()) && test(KeyCode.CONTROL, this.control, var2, this.shortcut, var1.isControlDown()) && test(KeyCode.ALT, this.alt, var2, this.shortcut, var1.isAltDown()) && test(KeyCode.META, this.meta, var2, this.shortcut, var1.isMetaDown());
   }

   public String getName() {
      StringBuilder var1 = new StringBuilder();
      this.addModifiersIntoString(var1);
      return var1.toString();
   }

   public String getDisplayText() {
      StringBuilder var1 = new StringBuilder();
      if (PlatformUtil.isMac()) {
         if (this.getControl() == KeyCombination.ModifierValue.DOWN) {
            var1.append("⌃");
         }

         if (this.getAlt() == KeyCombination.ModifierValue.DOWN) {
            var1.append("⌥");
         }

         if (this.getShift() == KeyCombination.ModifierValue.DOWN) {
            var1.append("⇧");
         }

         if (this.getMeta() == KeyCombination.ModifierValue.DOWN || this.getShortcut() == KeyCombination.ModifierValue.DOWN) {
            var1.append("⌘");
         }
      } else {
         if (this.getControl() == KeyCombination.ModifierValue.DOWN || this.getShortcut() == KeyCombination.ModifierValue.DOWN) {
            var1.append("Ctrl+");
         }

         if (this.getAlt() == KeyCombination.ModifierValue.DOWN) {
            var1.append("Alt+");
         }

         if (this.getShift() == KeyCombination.ModifierValue.DOWN) {
            var1.append("Shift+");
         }

         if (this.getMeta() == KeyCombination.ModifierValue.DOWN) {
            var1.append("Meta+");
         }
      }

      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof KeyCombination)) {
         return false;
      } else {
         KeyCombination var2 = (KeyCombination)var1;
         return this.shift == var2.shift && this.control == var2.control && this.alt == var2.alt && this.meta == var2.meta && this.shortcut == var2.shortcut;
      }
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 23 * var1 + this.shift.hashCode();
      var1 = 23 * var1 + this.control.hashCode();
      var1 = 23 * var1 + this.alt.hashCode();
      var1 = 23 * var1 + this.meta.hashCode();
      var1 = 23 * var1 + this.shortcut.hashCode();
      return var1;
   }

   public String toString() {
      return this.getName();
   }

   public static KeyCombination valueOf(String var0) {
      ArrayList var1 = new ArrayList(4);
      String[] var2 = splitName(var0);
      KeyCode var3 = null;
      String var4 = null;
      String[] var5 = var2;
      int var6 = var2.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         if (var8.length() > 2 && var8.charAt(0) == '\'' && var8.charAt(var8.length() - 1) == '\'') {
            if (var3 != null || var4 != null) {
               throw new IllegalArgumentException("Cannot parse key binding " + var0);
            }

            var4 = var8.substring(1, var8.length() - 1).replace("\\'", "'");
         } else {
            String var9 = normalizeToken(var8);
            Modifier var10 = getModifier(var9);
            if (var10 != null) {
               var1.add(var10);
            } else {
               if (var3 != null || var4 != null) {
                  throw new IllegalArgumentException("Cannot parse key binding " + var0);
               }

               var3 = KeyCode.getKeyCode(var9);
               if (var3 == null) {
                  var4 = var8;
               }
            }
         }
      }

      if (var3 == null && var4 == null) {
         throw new IllegalArgumentException("Cannot parse key binding " + var0);
      } else {
         Modifier[] var11 = (Modifier[])var1.toArray(new Modifier[var1.size()]);
         return (KeyCombination)(var3 != null ? new KeyCodeCombination(var3, var11) : new KeyCharacterCombination(var4, var11));
      }
   }

   public static KeyCombination keyCombination(String var0) {
      return valueOf(var0);
   }

   private void addModifiersIntoString(StringBuilder var1) {
      addModifierIntoString(var1, KeyCode.SHIFT, this.shift);
      addModifierIntoString(var1, KeyCode.CONTROL, this.control);
      addModifierIntoString(var1, KeyCode.ALT, this.alt);
      addModifierIntoString(var1, KeyCode.META, this.meta);
      addModifierIntoString(var1, KeyCode.SHORTCUT, this.shortcut);
   }

   private static void addModifierIntoString(StringBuilder var0, KeyCode var1, ModifierValue var2) {
      if (var2 != KeyCombination.ModifierValue.UP) {
         if (var0.length() > 0) {
            var0.append("+");
         }

         if (var2 == KeyCombination.ModifierValue.ANY) {
            var0.append("Ignore ");
         }

         var0.append(var1.getName());
      }
   }

   private static boolean test(KeyCode var0, ModifierValue var1, KeyCode var2, ModifierValue var3, boolean var4) {
      ModifierValue var5 = var0 == var2 ? resolveModifierValue(var1, var3) : var1;
      return test(var5, var4);
   }

   private static boolean test(ModifierValue var0, boolean var1) {
      switch (var0) {
         case DOWN:
            return var1;
         case UP:
            return !var1;
         case ANY:
         default:
            return true;
      }
   }

   private static ModifierValue resolveModifierValue(ModifierValue var0, ModifierValue var1) {
      if (var0 != KeyCombination.ModifierValue.DOWN && var1 != KeyCombination.ModifierValue.DOWN) {
         return var0 != KeyCombination.ModifierValue.ANY && var1 != KeyCombination.ModifierValue.ANY ? KeyCombination.ModifierValue.UP : KeyCombination.ModifierValue.ANY;
      } else {
         return KeyCombination.ModifierValue.DOWN;
      }
   }

   static Modifier getModifier(String var0) {
      Modifier[] var1 = POSSIBLE_MODIFIERS;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Modifier var4 = var1[var3];
         if (var4.toString().equals(var0)) {
            return var4;
         }
      }

      return null;
   }

   private static ModifierValue getModifierValue(Modifier[] var0, KeyCode var1) {
      ModifierValue var2 = KeyCombination.ModifierValue.UP;
      Modifier[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Modifier var6 = var3[var5];
         if (var6 == null) {
            throw new NullPointerException("Modifier must not be null!");
         }

         if (var6.getKey() == var1) {
            if (var2 != KeyCombination.ModifierValue.UP) {
               throw new IllegalArgumentException(var6.getValue() != var2 ? "Conflicting modifiers specified!" : "Duplicate modifiers specified!");
            }

            var2 = var6.getValue();
         }
      }

      return var2;
   }

   private static String normalizeToken(String var0) {
      String[] var1 = var0.split("\\s+");
      StringBuilder var2 = new StringBuilder();
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (var2.length() > 0) {
            var2.append(' ');
         }

         var2.append(var6.substring(0, 1).toUpperCase(Locale.ROOT));
         var2.append(var6.substring(1).toLowerCase(Locale.ROOT));
      }

      return var2.toString();
   }

   private static String[] splitName(String var0) {
      ArrayList var1 = new ArrayList();
      char[] var2 = var0.trim().toCharArray();
      int var7 = 0;
      int var8 = 0;
      int var9 = -1;

      for(int var10 = 0; var10 < var2.length; ++var10) {
         char var11 = var2[var10];
         switch (var7) {
            case 0:
               switch (var11) {
                  case '\t':
                  case '\n':
                  case '\u000b':
                  case '\f':
                  case '\r':
                  case ' ':
                     var9 = var10;
                     var7 = 1;
                     continue;
                  case '\'':
                     if (var10 == 0 || var2[var10 - 1] != '\\') {
                        var7 = 3;
                     }
                     continue;
                  case '+':
                     var9 = var10;
                     var7 = 2;
                  default:
                     continue;
               }
            case 1:
               switch (var11) {
                  case '\t':
                  case '\n':
                  case '\u000b':
                  case '\f':
                  case '\r':
                  case ' ':
                     continue;
                  case '\'':
                     var7 = 3;
                     var9 = -1;
                     continue;
                  case '+':
                     var7 = 2;
                     continue;
                  default:
                     var7 = 0;
                     var9 = -1;
                     continue;
               }
            case 2:
               switch (var11) {
                  case '\t':
                  case '\n':
                  case '\u000b':
                  case '\f':
                  case '\r':
                  case ' ':
                     continue;
                  case '+':
                     throw new IllegalArgumentException("Cannot parse key binding " + var0);
                  default:
                     if (var9 <= var8) {
                        throw new IllegalArgumentException("Cannot parse key binding " + var0);
                     }

                     var1.add(new String(var2, var8, var9 - var8));
                     var8 = var10;
                     var9 = -1;
                     var7 = var11 == '\'' ? 3 : 0;
                     continue;
               }
            case 3:
               if (var11 == '\'' && var2[var10 - 1] != '\\') {
                  var7 = 0;
               }
         }
      }

      switch (var7) {
         case 0:
         case 1:
            var1.add(new String(var2, var8, var2.length - var8));
         default:
            return (String[])var1.toArray(new String[var1.size()]);
         case 2:
         case 3:
            throw new IllegalArgumentException("Cannot parse key binding " + var0);
      }
   }

   static {
      SHIFT_DOWN = new Modifier(KeyCode.SHIFT, KeyCombination.ModifierValue.DOWN);
      SHIFT_ANY = new Modifier(KeyCode.SHIFT, KeyCombination.ModifierValue.ANY);
      CONTROL_DOWN = new Modifier(KeyCode.CONTROL, KeyCombination.ModifierValue.DOWN);
      CONTROL_ANY = new Modifier(KeyCode.CONTROL, KeyCombination.ModifierValue.ANY);
      ALT_DOWN = new Modifier(KeyCode.ALT, KeyCombination.ModifierValue.DOWN);
      ALT_ANY = new Modifier(KeyCode.ALT, KeyCombination.ModifierValue.ANY);
      META_DOWN = new Modifier(KeyCode.META, KeyCombination.ModifierValue.DOWN);
      META_ANY = new Modifier(KeyCode.META, KeyCombination.ModifierValue.ANY);
      SHORTCUT_DOWN = new Modifier(KeyCode.SHORTCUT, KeyCombination.ModifierValue.DOWN);
      SHORTCUT_ANY = new Modifier(KeyCode.SHORTCUT, KeyCombination.ModifierValue.ANY);
      POSSIBLE_MODIFIERS = new Modifier[]{SHIFT_DOWN, SHIFT_ANY, CONTROL_DOWN, CONTROL_ANY, ALT_DOWN, ALT_ANY, META_DOWN, META_ANY, SHORTCUT_DOWN, SHORTCUT_ANY};
      NO_MATCH = new KeyCombination(new Modifier[0]) {
         public boolean match(KeyEvent var1) {
            return false;
         }
      };
   }

   public static enum ModifierValue {
      DOWN,
      UP,
      ANY;
   }

   public static final class Modifier {
      private final KeyCode key;
      private final ModifierValue value;

      private Modifier(KeyCode var1, ModifierValue var2) {
         this.key = var1;
         this.value = var2;
      }

      public KeyCode getKey() {
         return this.key;
      }

      public ModifierValue getValue() {
         return this.value;
      }

      public String toString() {
         return (this.value == KeyCombination.ModifierValue.ANY ? "Ignore " : "") + this.key.getName();
      }

      // $FF: synthetic method
      Modifier(KeyCode var1, ModifierValue var2, Object var3) {
         this(var1, var2);
      }
   }
}

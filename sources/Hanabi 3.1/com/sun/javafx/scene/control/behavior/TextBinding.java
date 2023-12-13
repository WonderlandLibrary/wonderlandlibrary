package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class TextBinding {
   private String MNEMONIC_SYMBOL = "_";
   private String text = null;
   private String mnemonic = null;
   private KeyCombination mnemonicKeyCombination = null;
   private int mnemonicIndex = -1;
   private String extendedMnemonicText = null;

   public String getText() {
      return this.text;
   }

   public String getMnemonic() {
      return this.mnemonic;
   }

   public KeyCombination getMnemonicKeyCombination() {
      if (this.mnemonic != null && this.mnemonicKeyCombination == null) {
         this.mnemonicKeyCombination = new MnemonicKeyCombination(this.mnemonic);
      }

      return this.mnemonicKeyCombination;
   }

   public int getMnemonicIndex() {
      return this.mnemonicIndex;
   }

   public String getExtendedMnemonicText() {
      return this.extendedMnemonicText;
   }

   public TextBinding(String var1) {
      this.parseAndSplit(var1);
   }

   private void parseAndSplit(String var1) {
      if (var1 != null && var1.length() != 0) {
         StringBuffer var2 = new StringBuffer(var1);

         for(int var3 = var2.indexOf(this.MNEMONIC_SYMBOL); var3 >= 0 && var3 < var2.length() - 1; var3 = var2.indexOf(this.MNEMONIC_SYMBOL, var3 + 1)) {
            if (this.MNEMONIC_SYMBOL.equals(var2.substring(var3 + 1, var3 + 2))) {
               var2.delete(var3, var3 + 1);
            } else {
               if (var2.charAt(var3 + 1) != '(' || var3 == var2.length() - 2) {
                  this.mnemonic = var2.substring(var3 + 1, var3 + 2);
                  if (this.mnemonic != null) {
                     this.mnemonicIndex = var3;
                  }

                  var2.delete(var3, var3 + 1);
                  break;
               }

               int var4 = var2.indexOf(")", var3 + 3);
               if (var4 == -1) {
                  this.mnemonic = var2.substring(var3 + 1, var3 + 2);
                  if (this.mnemonic != null) {
                     this.mnemonicIndex = var3;
                  }

                  var2.delete(var3, var3 + 1);
                  break;
               }

               if (var4 == var3 + 3) {
                  this.mnemonic = var2.substring(var3 + 2, var3 + 3);
                  this.extendedMnemonicText = var2.substring(var3 + 1, var3 + 4);
                  var2.delete(var3, var4 + 3);
                  break;
               }
            }
         }

         this.text = var2.toString();
      } else {
         this.text = var1;
      }
   }

   public static class MnemonicKeyCombination extends KeyCombination {
      private String character = "";

      public MnemonicKeyCombination(String var1) {
         super(PlatformUtil.isMac() ? KeyCombination.META_DOWN : KeyCombination.ALT_DOWN);
         this.character = var1;
      }

      public final String getCharacter() {
         return this.character;
      }

      public boolean match(KeyEvent var1) {
         String var2 = var1.getText();
         return var2 != null && !var2.isEmpty() && var2.equalsIgnoreCase(this.getCharacter()) && super.match(var1);
      }

      public String getName() {
         StringBuilder var1 = new StringBuilder();
         var1.append(super.getName());
         if (var1.length() > 0) {
            var1.append("+");
         }

         return var1.append('\'').append(this.character.replace("'", "\\'")).append('\'').toString();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof MnemonicKeyCombination)) {
            return false;
         } else {
            return this.character.equals(((MnemonicKeyCombination)var1).getCharacter()) && super.equals(var1);
         }
      }

      public int hashCode() {
         return 23 * super.hashCode() + this.character.hashCode();
      }
   }
}

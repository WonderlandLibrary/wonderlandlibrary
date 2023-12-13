package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import java.text.CharacterIterator;
import java.util.Stack;

final class UnhandledBreakEngine implements LanguageBreakEngine {
   private final UnicodeSet[] fHandled = new UnicodeSet[5];

   public UnhandledBreakEngine() {
      for(int var1 = 0; var1 < this.fHandled.length; ++var1) {
         this.fHandled[var1] = new UnicodeSet();
      }

   }

   public boolean handles(int var1, int var2) {
      return var2 >= 0 && var2 < this.fHandled.length && this.fHandled[var2].contains(var1);
   }

   public int findBreaks(CharacterIterator var1, int var2, int var3, boolean var4, int var5, Stack var6) {
      var1.setIndex(var3);
      return 0;
   }

   public synchronized void handleChar(int var1, int var2) {
      if (var2 >= 0 && var2 < this.fHandled.length && var1 != Integer.MAX_VALUE && !this.fHandled[var2].contains(var1)) {
         int var3 = UCharacter.getIntPropertyValue(var1, 4106);
         this.fHandled[var2].applyIntPropertyValue(4106, var3);
      }

   }
}

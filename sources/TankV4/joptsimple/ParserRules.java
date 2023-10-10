package joptsimple;

import java.util.Collection;
import java.util.Iterator;

final class ParserRules {
   static final char HYPHEN_CHAR = '-';
   static final String HYPHEN = String.valueOf('-');
   static final String DOUBLE_HYPHEN = "--";
   static final String OPTION_TERMINATOR = "--";
   static final String RESERVED_FOR_EXTENSIONS = "W";

   private ParserRules() {
      throw new UnsupportedOperationException();
   }

   static boolean isShortOptionToken(String var0) {
      return var0.startsWith(HYPHEN) && !HYPHEN.equals(var0) && var0 != false;
   }

   static boolean isOptionTerminator(String var0) {
      return "--".equals(var0);
   }

   static void ensureLegalOption(String var0) {
      if (var0.startsWith(HYPHEN)) {
         throw new IllegalOptionSpecificationException(String.valueOf(var0));
      } else {
         for(int var1 = 0; var1 < var0.length(); ++var1) {
            ensureLegalOptionCharacter(var0.charAt(var1));
         }

      }
   }

   static void ensureLegalOptions(Collection var0) {
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         ensureLegalOption(var2);
      }

   }

   private static void ensureLegalOptionCharacter(char param0) {
      // $FF: Couldn't be decompiled
   }
}

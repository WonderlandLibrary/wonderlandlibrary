package com.mojang.realmsclient.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public enum ChatFormatting {
   BLACK('0'),
   DARK_BLUE('1'),
   DARK_GREEN('2'),
   DARK_AQUA('3'),
   DARK_RED('4'),
   DARK_PURPLE('5'),
   GOLD('6'),
   GRAY('7'),
   DARK_GRAY('8'),
   BLUE('9'),
   GREEN('a'),
   AQUA('b'),
   RED('c'),
   LIGHT_PURPLE('d'),
   YELLOW('e'),
   WHITE('f'),
   OBFUSCATED('k', true),
   BOLD('l', true),
   STRIKETHROUGH('m', true),
   UNDERLINE('n', true),
   ITALIC('o', true),
   RESET('r');

   public static final char PREFIX_CODE = '§';
   private static final Map FORMATTING_BY_CHAR = new HashMap();
   private static final Map FORMATTING_BY_NAME = new HashMap();
   private static final Pattern STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
   private final char code;
   private final boolean isFormat;
   private final String toString;
   private static final ChatFormatting[] $VALUES = new ChatFormatting[]{BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE, OBFUSCATED, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC, RESET};

   private ChatFormatting(char var3) {
      this(var3, false);
   }

   private ChatFormatting(char var3, boolean var4) {
      this.code = var3;
      this.isFormat = var4;
      this.toString = "§" + var3;
   }

   public char getChar() {
      return this.code;
   }

   public boolean isFormat() {
      return this.isFormat;
   }

   public String getName() {
      return this.name().toLowerCase();
   }

   public String toString() {
      return this.toString;
   }

   public static String stripFormatting(String var0) {
      return var0 == null ? null : STRIP_FORMATTING_PATTERN.matcher(var0).replaceAll("");
   }

   public static ChatFormatting getByChar(char var0) {
      return (ChatFormatting)FORMATTING_BY_CHAR.get(var0);
   }

   public static ChatFormatting getByName(String var0) {
      return var0 == null ? null : (ChatFormatting)FORMATTING_BY_NAME.get(var0.toLowerCase());
   }

   public static Collection getNames(boolean var0, boolean var1) {
      ArrayList var2 = new ArrayList();
      ChatFormatting[] var3 = values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ChatFormatting var6 = var3[var5];
         if ((var6 != false || var0) && (!var6.isFormat() || var1)) {
            var2.add(var6.getName());
         }
      }

      return var2;
   }

   static {
      ChatFormatting[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         ChatFormatting var3 = var0[var2];
         FORMATTING_BY_CHAR.put(var3.getChar(), var3);
         FORMATTING_BY_NAME.put(var3.getName(), var3);
      }

   }
}

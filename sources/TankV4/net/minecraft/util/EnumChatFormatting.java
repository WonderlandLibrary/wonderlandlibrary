package net.minecraft.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public enum EnumChatFormatting {
   RED("RED", 'c', 12),
   DARK_RED("DARK_RED", '4', 4);

   private static final EnumChatFormatting[] ENUM$VALUES = new EnumChatFormatting[]{BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE, OBFUSCATED, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC, RESET};
   YELLOW("YELLOW", 'e', 14),
   DARK_GREEN("DARK_GREEN", '2', 2);

   private static final Map nameMapping = Maps.newHashMap();
   GOLD("GOLD", '6', 6),
   ITALIC("ITALIC", 'o', true),
   GREEN("GREEN", 'a', 10),
   RESET("RESET", 'r', -1),
   UNDERLINE("UNDERLINE", 'n', true);

   private final char formattingCode;
   STRIKETHROUGH("STRIKETHROUGH", 'm', true),
   BLUE("BLUE", '9', 9);

   private final int colorIndex;
   AQUA("AQUA", 'b', 11),
   GRAY("GRAY", '7', 7),
   BOLD("BOLD", 'l', true);

   private final boolean fancyStyling;
   BLACK("BLACK", '0', 0);

   private final String name;
   private final String controlString;
   LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13),
   DARK_AQUA("DARK_AQUA", '3', 3),
   OBFUSCATED("OBFUSCATED", 'k', true),
   DARK_PURPLE("DARK_PURPLE", '5', 5),
   DARK_BLUE("DARK_BLUE", '1', 1);

   private static final Pattern formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
   DARK_GRAY("DARK_GRAY", '8', 8),
   WHITE("WHITE", 'f', 15);

   static {
      EnumChatFormatting[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         EnumChatFormatting var0 = var3[var1];
         nameMapping.put(func_175745_c(var0.name), var0);
      }

   }

   private EnumChatFormatting(String var3, char var4, int var5) {
      this(var3, var4, false, var5);
   }

   private static String func_175745_c(String var0) {
      return var0.toLowerCase().replaceAll("[^a-z]", "");
   }

   public static Collection getValidValues(boolean var0, boolean var1) {
      ArrayList var2 = Lists.newArrayList();
      EnumChatFormatting[] var6;
      int var5 = (var6 = values()).length;

      for(int var4 = 0; var4 < var5; ++var4) {
         EnumChatFormatting var3 = var6[var4];
         if ((var3 == false || var0) && (!var3.isFancyStyling() || var1)) {
            var2.add(var3.getFriendlyName());
         }
      }

      return var2;
   }

   public boolean isFancyStyling() {
      return this.fancyStyling;
   }

   public static EnumChatFormatting func_175744_a(int var0) {
      if (var0 < 0) {
         return RESET;
      } else {
         EnumChatFormatting[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            EnumChatFormatting var1 = var4[var2];
            if (var1.getColorIndex() == var0) {
               return var1;
            }
         }

         return null;
      }
   }

   public static EnumChatFormatting getValueByName(String var0) {
      return var0 == null ? null : (EnumChatFormatting)nameMapping.get(func_175745_c(var0));
   }

   public static String getTextWithoutFormattingCodes(String var0) {
      return var0 == null ? null : formattingCodePattern.matcher(var0).replaceAll("");
   }

   private EnumChatFormatting(String var3, char var4, boolean var5, int var6) {
      this.name = var3;
      this.formattingCode = var4;
      this.fancyStyling = var5;
      this.colorIndex = var6;
      this.controlString = "§" + var4;
   }

   private EnumChatFormatting(String var3, char var4, boolean var5) {
      this(var3, var4, var5, -1);
   }

   public String getFriendlyName() {
      return this.name().toLowerCase();
   }

   public String toString() {
      return this.controlString;
   }

   public int getColorIndex() {
      return this.colorIndex;
   }
}

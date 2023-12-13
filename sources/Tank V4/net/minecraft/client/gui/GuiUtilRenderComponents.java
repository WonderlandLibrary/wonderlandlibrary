package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class GuiUtilRenderComponents {
   public static List func_178908_a(IChatComponent var0, int var1, FontRenderer var2, boolean var3, boolean var4) {
      int var5 = 0;
      ChatComponentText var6 = new ChatComponentText("");
      ArrayList var7 = Lists.newArrayList();
      ArrayList var8 = Lists.newArrayList((Iterable)var0);

      for(int var9 = 0; var9 < var8.size(); ++var9) {
         IChatComponent var10 = (IChatComponent)var8.get(var9);
         String var11 = var10.getUnformattedTextForChat();
         boolean var12 = false;
         String var14;
         if (var11.contains("\n")) {
            int var13 = var11.indexOf(10);
            var14 = var11.substring(var13 + 1);
            var11 = var11.substring(0, var13 + 1);
            ChatComponentText var15 = new ChatComponentText(var14);
            var15.setChatStyle(var10.getChatStyle().createShallowCopy());
            var8.add(var9 + 1, var15);
            var12 = true;
         }

         String var21 = func_178909_a(var10.getChatStyle().getFormattingCode() + var11, var4);
         var14 = var21.endsWith("\n") ? var21.substring(0, var21.length() - 1) : var21;
         int var22 = var2.getStringWidth(var14);
         ChatComponentText var16 = new ChatComponentText(var14);
         var16.setChatStyle(var10.getChatStyle().createShallowCopy());
         if (var5 + var22 > var1) {
            String var17 = var2.trimStringToWidth(var21, var1 - var5, false);
            String var18 = var17.length() < var21.length() ? var21.substring(var17.length()) : null;
            if (var18 != null && var18.length() > 0) {
               int var19 = var17.lastIndexOf(" ");
               if (var19 >= 0 && var2.getStringWidth(var21.substring(0, var19)) > 0) {
                  var17 = var21.substring(0, var19);
                  if (var3) {
                     ++var19;
                  }

                  var18 = var21.substring(var19);
               } else if (var5 > 0 && !var21.contains(" ")) {
                  var17 = "";
                  var18 = var21;
               }

               ChatComponentText var20 = new ChatComponentText(var18);
               var20.setChatStyle(var10.getChatStyle().createShallowCopy());
               var8.add(var9 + 1, var20);
            }

            var22 = var2.getStringWidth(var17);
            var16 = new ChatComponentText(var17);
            var16.setChatStyle(var10.getChatStyle().createShallowCopy());
            var12 = true;
         }

         if (var5 + var22 <= var1) {
            var5 += var22;
            var6.appendSibling(var16);
         } else {
            var12 = true;
         }

         if (var12) {
            var7.add(var6);
            var5 = 0;
            var6 = new ChatComponentText("");
         }
      }

      var7.add(var6);
      return var7;
   }

   public static String func_178909_a(String var0, boolean var1) {
      return !var1 && !Minecraft.getMinecraft().gameSettings.chatColours ? EnumChatFormatting.getTextWithoutFormattingCodes(var0) : var0;
   }
}

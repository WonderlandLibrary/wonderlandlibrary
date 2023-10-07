// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import java.util.ArrayList;
import com.google.common.collect.Lists;
import net.minecraft.util.ChatComponentText;
import java.util.List;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.Minecraft;

public class GuiUtilRenderComponents
{
    private static final String __OBFID = "CL_00001957";
    
    public static String func_178909_a(final String p_178909_0_, final boolean p_178909_1_) {
        return (!p_178909_1_ && !Minecraft.getMinecraft().gameSettings.chatColours) ? EnumChatFormatting.getTextWithoutFormattingCodes(p_178909_0_) : p_178909_0_;
    }
    
    public static List func_178908_a(final IChatComponent p_178908_0_, final int p_178908_1_, final FontRenderer p_178908_2_, final boolean p_178908_3_, final boolean p_178908_4_) {
        int var5 = 0;
        ChatComponentText var6 = new ChatComponentText("");
        final ArrayList var7 = Lists.newArrayList();
        final ArrayList var8 = Lists.newArrayList((Iterable)p_178908_0_);
        for (int var9 = 0; var9 < var8.size(); ++var9) {
            final IChatComponent var10 = var8.get(var9);
            String var11 = var10.getUnformattedTextForChat();
            boolean var12 = false;
            if (var11.contains("\n")) {
                final int var13 = var11.indexOf(10);
                final String var14 = var11.substring(var13 + 1);
                var11 = var11.substring(0, var13 + 1);
                final ChatComponentText var15 = new ChatComponentText(var14);
                var15.setChatStyle(var10.getChatStyle().createShallowCopy());
                var8.add(var9 + 1, var15);
                var12 = true;
            }
            final String var16 = func_178909_a(var10.getChatStyle().getFormattingCode() + var11, p_178908_4_);
            final String var14 = var16.endsWith("\n") ? var16.substring(0, var16.length() - 1) : var16;
            int var17 = p_178908_2_.getStringWidth(var14);
            ChatComponentText var18 = new ChatComponentText(var14);
            var18.setChatStyle(var10.getChatStyle().createShallowCopy());
            if (var5 + var17 > p_178908_1_) {
                String var19 = p_178908_2_.trimStringToWidth(var16, p_178908_1_ - var5, false);
                String var20 = (var19.length() < var16.length()) ? var16.substring(var19.length()) : null;
                if (var20 != null && var20.length() > 0) {
                    int var21 = var19.lastIndexOf(" ");
                    if (var21 >= 0 && p_178908_2_.getStringWidth(var16.substring(0, var21)) > 0) {
                        var19 = var16.substring(0, var21);
                        if (p_178908_3_) {
                            ++var21;
                        }
                        var20 = var16.substring(var21);
                    }
                    else if (var5 > 0 && !var16.contains(" ")) {
                        var19 = "";
                        var20 = var16;
                    }
                    final ChatComponentText var22 = new ChatComponentText(var20);
                    var22.setChatStyle(var10.getChatStyle().createShallowCopy());
                    var8.add(var9 + 1, var22);
                }
                var17 = p_178908_2_.getStringWidth(var19);
                var18 = new ChatComponentText(var19);
                var18.setChatStyle(var10.getChatStyle().createShallowCopy());
                var12 = true;
            }
            if (var5 + var17 <= p_178908_1_) {
                var5 += var17;
                var6.appendSibling(var18);
            }
            else {
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
}

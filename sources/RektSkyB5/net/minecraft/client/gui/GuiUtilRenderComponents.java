/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class GuiUtilRenderComponents {
    public static String func_178909_a(String p_178909_0_, boolean p_178909_1_) {
        return !p_178909_1_ && !Minecraft.getMinecraft().gameSettings.chatColours ? EnumChatFormatting.getTextWithoutFormattingCodes(p_178909_0_) : p_178909_0_;
    }

    public static List<IChatComponent> func_178908_a(IChatComponent p_178908_0_, int p_178908_1_, FontRenderer p_178908_2_, boolean p_178908_3_, boolean p_178908_4_) {
        int i2 = 0;
        ChatComponentText ichatcomponent = new ChatComponentText("");
        ArrayList<IChatComponent> list = Lists.newArrayList();
        ArrayList<IChatComponent> list1 = Lists.newArrayList(p_178908_0_);
        for (int j2 = 0; j2 < list1.size(); ++j2) {
            String s4;
            IChatComponent ichatcomponent1 = (IChatComponent)list1.get(j2);
            String s2 = ichatcomponent1.getUnformattedTextForChat();
            boolean flag = false;
            if (s2.contains("\n")) {
                int k2 = s2.indexOf(10);
                String s1 = s2.substring(k2 + 1);
                s2 = s2.substring(0, k2 + 1);
                ChatComponentText chatcomponenttext = new ChatComponentText(s1);
                chatcomponenttext.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                list1.add(j2 + 1, chatcomponenttext);
                flag = true;
            }
            String s5 = (s4 = GuiUtilRenderComponents.func_178909_a(ichatcomponent1.getChatStyle().getFormattingCode() + s2, p_178908_4_)).endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
            int i1 = p_178908_2_.getStringWidth(s5);
            ChatComponentText chatcomponenttext1 = new ChatComponentText(s5);
            chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
            if (i2 + i1 > p_178908_1_) {
                String s3;
                String s22 = p_178908_2_.trimStringToWidth(s4, p_178908_1_ - i2, false);
                String string = s3 = s22.length() < s4.length() ? s4.substring(s22.length()) : null;
                if (s3 != null && s3.length() > 0) {
                    int l2 = s22.lastIndexOf(" ");
                    if (l2 >= 0 && p_178908_2_.getStringWidth(s4.substring(0, l2)) > 0) {
                        s22 = s4.substring(0, l2);
                        if (p_178908_3_) {
                            ++l2;
                        }
                        s3 = s4.substring(l2);
                    } else if (i2 > 0 && !s4.contains(" ")) {
                        s22 = "";
                        s3 = s4;
                    }
                    ChatComponentText chatcomponenttext2 = new ChatComponentText(s3);
                    chatcomponenttext2.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                    list1.add(j2 + 1, chatcomponenttext2);
                }
                i1 = p_178908_2_.getStringWidth(s22);
                chatcomponenttext1 = new ChatComponentText(s22);
                chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                flag = true;
            }
            if (i2 + i1 <= p_178908_1_) {
                i2 += i1;
                ichatcomponent.appendSibling(chatcomponenttext1);
            } else {
                flag = true;
            }
            if (!flag) continue;
            list.add(ichatcomponent);
            i2 = 0;
            ichatcomponent = new ChatComponentText("");
        }
        list.add(ichatcomponent);
        return list;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
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
    public static List<IChatComponent> func_178908_a(IChatComponent iChatComponent, int n, FontRenderer fontRenderer, boolean bl, boolean bl2) {
        int n2 = 0;
        ChatComponentText chatComponentText = new ChatComponentText("");
        ArrayList arrayList = Lists.newArrayList();
        ArrayList arrayList2 = Lists.newArrayList((Iterable)iChatComponent);
        int n3 = 0;
        while (n3 < arrayList2.size()) {
            String string;
            String string2;
            IChatComponent iChatComponent2 = (IChatComponent)arrayList2.get(n3);
            String string3 = iChatComponent2.getUnformattedTextForChat();
            boolean bl3 = false;
            if (string3.contains("\n")) {
                int n4 = string3.indexOf(10);
                string2 = string3.substring(n4 + 1);
                string3 = string3.substring(0, n4 + 1);
                ChatComponentText chatComponentText2 = new ChatComponentText(string2);
                chatComponentText2.setChatStyle(iChatComponent2.getChatStyle().createShallowCopy());
                arrayList2.add(n3 + 1, chatComponentText2);
                bl3 = true;
            }
            string2 = (string = GuiUtilRenderComponents.func_178909_a(String.valueOf(iChatComponent2.getChatStyle().getFormattingCode()) + string3, bl2)).endsWith("\n") ? string.substring(0, string.length() - 1) : string;
            int n5 = fontRenderer.getStringWidth(string2);
            ChatComponentText chatComponentText3 = new ChatComponentText(string2);
            chatComponentText3.setChatStyle(iChatComponent2.getChatStyle().createShallowCopy());
            if (n2 + n5 > n) {
                String string4;
                String string5 = fontRenderer.trimStringToWidth(string, n - n2, false);
                String string6 = string4 = string5.length() < string.length() ? string.substring(string5.length()) : null;
                if (string4 != null && string4.length() > 0) {
                    int n6 = string5.lastIndexOf(" ");
                    if (n6 >= 0 && fontRenderer.getStringWidth(string.substring(0, n6)) > 0) {
                        string5 = string.substring(0, n6);
                        if (bl) {
                            ++n6;
                        }
                        string4 = string.substring(n6);
                    } else if (n2 > 0 && !string.contains(" ")) {
                        string5 = "";
                        string4 = string;
                    }
                    ChatComponentText chatComponentText4 = new ChatComponentText(string4);
                    chatComponentText4.setChatStyle(iChatComponent2.getChatStyle().createShallowCopy());
                    arrayList2.add(n3 + 1, chatComponentText4);
                }
                n5 = fontRenderer.getStringWidth(string5);
                chatComponentText3 = new ChatComponentText(string5);
                chatComponentText3.setChatStyle(iChatComponent2.getChatStyle().createShallowCopy());
                bl3 = true;
            }
            if (n2 + n5 <= n) {
                n2 += n5;
                chatComponentText.appendSibling(chatComponentText3);
            } else {
                bl3 = true;
            }
            if (bl3) {
                arrayList.add(chatComponentText);
                n2 = 0;
                chatComponentText = new ChatComponentText("");
            }
            ++n3;
        }
        arrayList.add(chatComponentText);
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String func_178909_a(String string, boolean bl) {
        String string2;
        if (!bl) {
            Minecraft.getMinecraft();
            if (!Minecraft.gameSettings.chatColours) {
                string2 = EnumChatFormatting.getTextWithoutFormattingCodes(string);
                return string2;
            }
        }
        string2 = string;
        return string2;
    }
}


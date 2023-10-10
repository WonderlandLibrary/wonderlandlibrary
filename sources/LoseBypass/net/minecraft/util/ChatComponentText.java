/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.Iterator;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.IChatComponent;

public class ChatComponentText
extends ChatComponentStyle {
    private final String text;

    public ChatComponentText(String msg) {
        this.text = msg;
    }

    public String getChatComponentText_TextValue() {
        return this.text;
    }

    @Override
    public String getUnformattedTextForChat() {
        return this.text;
    }

    @Override
    public ChatComponentText createCopy() {
        ChatComponentText chatcomponenttext = new ChatComponentText(this.text);
        chatcomponenttext.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator<IChatComponent> iterator = this.getSiblings().iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = iterator.next();
            chatcomponenttext.appendSibling(ichatcomponent.createCopy());
        }
        return chatcomponenttext;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentText)) {
            return false;
        }
        ChatComponentText chatcomponenttext = (ChatComponentText)p_equals_1_;
        if (!this.text.equals(chatcomponenttext.getChatComponentText_TextValue())) return false;
        if (!super.equals(p_equals_1_)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}


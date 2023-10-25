package net.minecraft.util;

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

    public String getUnformattedTextForChat() {
        return this.text;
    }

    public ChatComponentText createCopy() {
        ChatComponentText chatcomponenttext = new ChatComponentText(this.text);
        chatcomponenttext.setChatStyle(this.getChatStyle().createShallowCopy());
        for (IChatComponent ichatcomponent : this.getSiblings()) {
            chatcomponenttext.appendSibling(ichatcomponent.createCopy());
        }
        return chatcomponenttext;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentText)) {
            return false;
        }
        ChatComponentText chatcomponenttext = (ChatComponentText)((Object)p_equals_1_);
        return this.text.equals((Object)chatcomponenttext.getChatComponentText_TextValue()) && super.equals(p_equals_1_);
    }

    public String toString() {
        return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}

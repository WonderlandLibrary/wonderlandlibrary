/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.IChatComponent;

public class ChatComponentText
extends ChatComponentStyle {
    private final String text;
    private static final String __OBFID = "CL_00001269";

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
        ChatComponentText var1 = new ChatComponentText(this.text);
        var1.setChatStyle(this.getChatStyle().createShallowCopy());
        for (IChatComponent var3 : this.getSiblings()) {
            var1.appendSibling(var3.createCopy());
        }
        return var1;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentText)) {
            return false;
        }
        ChatComponentText var2 = (ChatComponentText)p_equals_1_;
        return this.text.equals(var2.getChatComponentText_TextValue()) && super.equals(p_equals_1_);
    }

    @Override
    public String toString() {
        return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}


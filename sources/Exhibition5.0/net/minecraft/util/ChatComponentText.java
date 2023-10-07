// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentText extends ChatComponentStyle
{
    private final String text;
    private static final String __OBFID = "CL_00001269";
    
    public ChatComponentText(final String msg) {
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
        final ChatComponentText var1 = new ChatComponentText(this.text);
        var1.setChatStyle(this.getChatStyle().createShallowCopy());
        for (final IChatComponent var3 : this.getSiblings()) {
            var1.appendSibling(var3.createCopy());
        }
        return var1;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentText)) {
            return false;
        }
        final ChatComponentText var2 = (ChatComponentText)p_equals_1_;
        return this.text.equals(var2.getChatComponentText_TextValue()) && super.equals(p_equals_1_);
    }
    
    @Override
    public String toString() {
        return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}

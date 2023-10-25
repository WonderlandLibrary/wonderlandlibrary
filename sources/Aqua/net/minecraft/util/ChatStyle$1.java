package net.minecraft.util;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

static final class ChatStyle.1
extends ChatStyle {
    ChatStyle.1() {
    }

    public EnumChatFormatting getColor() {
        return null;
    }

    public boolean getBold() {
        return false;
    }

    public boolean getItalic() {
        return false;
    }

    public boolean getStrikethrough() {
        return false;
    }

    public boolean getUnderlined() {
        return false;
    }

    public boolean getObfuscated() {
        return false;
    }

    public ClickEvent getChatClickEvent() {
        return null;
    }

    public HoverEvent getChatHoverEvent() {
        return null;
    }

    public String getInsertion() {
        return null;
    }

    public ChatStyle setColor(EnumChatFormatting color) {
        throw new UnsupportedOperationException();
    }

    public ChatStyle setBold(Boolean boldIn) {
        throw new UnsupportedOperationException();
    }

    public ChatStyle setItalic(Boolean italic) {
        throw new UnsupportedOperationException();
    }

    public ChatStyle setStrikethrough(Boolean strikethrough) {
        throw new UnsupportedOperationException();
    }

    public ChatStyle setUnderlined(Boolean underlined) {
        throw new UnsupportedOperationException();
    }

    public ChatStyle setObfuscated(Boolean obfuscated) {
        throw new UnsupportedOperationException();
    }

    public ChatStyle setChatClickEvent(ClickEvent event) {
        throw new UnsupportedOperationException();
    }

    public ChatStyle setChatHoverEvent(HoverEvent event) {
        throw new UnsupportedOperationException();
    }

    public ChatStyle setParentStyle(ChatStyle parent) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return "Style.ROOT";
    }

    public ChatStyle createShallowCopy() {
        return this;
    }

    public ChatStyle createDeepCopy() {
        return this;
    }

    public String getFormattingCode() {
        return "";
    }
}

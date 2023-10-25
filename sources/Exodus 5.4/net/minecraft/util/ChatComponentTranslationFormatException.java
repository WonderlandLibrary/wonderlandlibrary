/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.util.ChatComponentTranslation;

public class ChatComponentTranslationFormatException
extends IllegalArgumentException {
    public ChatComponentTranslationFormatException(ChatComponentTranslation chatComponentTranslation, Throwable throwable) {
        super(String.format("Error while parsing: %s", chatComponentTranslation), throwable);
    }

    public ChatComponentTranslationFormatException(ChatComponentTranslation chatComponentTranslation, int n) {
        super(String.format("Invalid index %d requested for %s", n, chatComponentTranslation));
    }

    public ChatComponentTranslationFormatException(ChatComponentTranslation chatComponentTranslation, String string) {
        super(String.format("Error parsing: %s: %s", chatComponentTranslation, string));
    }
}


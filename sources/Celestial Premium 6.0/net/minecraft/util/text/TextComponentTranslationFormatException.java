/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.text;

import net.minecraft.util.text.TextComponentTranslation;

public class TextComponentTranslationFormatException
extends IllegalArgumentException {
    public TextComponentTranslationFormatException(TextComponentTranslation component, String message) {
        super(String.format("Error parsing: %s: %s", component, message));
    }

    public TextComponentTranslationFormatException(TextComponentTranslation component, int index) {
        super(String.format("Invalid index %d requested for %s", index, component));
    }

    public TextComponentTranslationFormatException(TextComponentTranslation component, Throwable cause) {
        super(String.format("Error while parsing: %s", component), cause);
    }
}


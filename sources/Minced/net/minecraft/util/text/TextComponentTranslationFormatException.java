// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text;

public class TextComponentTranslationFormatException extends IllegalArgumentException
{
    public TextComponentTranslationFormatException(final TextComponentTranslation component, final String message) {
        super(String.format("Error parsing: %s: %s", component, message));
    }
    
    public TextComponentTranslationFormatException(final TextComponentTranslation component, final int index) {
        super(String.format("Invalid index %d requested for %s", index, component));
    }
    
    public TextComponentTranslationFormatException(final TextComponentTranslation component, final Throwable cause) {
        super(String.format("Error while parsing: %s", component), cause);
    }
}

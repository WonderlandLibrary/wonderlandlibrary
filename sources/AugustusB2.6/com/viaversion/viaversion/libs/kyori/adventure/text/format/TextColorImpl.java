// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "asHexString()")
final class TextColorImpl implements TextColor
{
    private final int value;
    
    TextColorImpl(final int value) {
        this.value = value;
    }
    
    @Override
    public int value() {
        return this.value;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TextColorImpl)) {
            return false;
        }
        final TextColorImpl that = (TextColorImpl)other;
        return this.value == that.value;
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return this.asHexString();
    }
}

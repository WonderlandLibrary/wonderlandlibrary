// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

final class TextDecorationAndStateImpl implements TextDecorationAndState
{
    private final TextDecoration decoration;
    private final TextDecoration.State state;
    
    TextDecorationAndStateImpl(final TextDecoration decoration, final TextDecoration.State state) {
        this.decoration = decoration;
        this.state = state;
    }
    
    @NotNull
    @Override
    public TextDecoration decoration() {
        return this.decoration;
    }
    
    @Override
    public TextDecoration.State state() {
        return this.state;
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final TextDecorationAndStateImpl that = (TextDecorationAndStateImpl)other;
        return this.decoration == that.decoration && this.state == that.state;
    }
    
    @Override
    public int hashCode() {
        int result = this.decoration.hashCode();
        result = 31 * result + this.state.hashCode();
        return result;
    }
}

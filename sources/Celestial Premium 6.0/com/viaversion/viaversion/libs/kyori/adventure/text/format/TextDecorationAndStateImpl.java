/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecorationAndState;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class TextDecorationAndStateImpl
implements TextDecorationAndState {
    private final TextDecoration decoration;
    private final TextDecoration.State state;

    TextDecorationAndStateImpl(TextDecoration decoration, TextDecoration.State state) {
        this.decoration = decoration;
        this.state = state;
    }

    @Override
    public @NonNull TextDecoration decoration() {
        return this.decoration;
    }

    @Override
    public @NonNull TextDecoration.State state() {
        return this.state;
    }

    public String toString() {
        return this.examine(StringExaminer.simpleEscaping());
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        TextDecorationAndStateImpl that = (TextDecorationAndStateImpl)other;
        return this.decoration == that.decoration && this.state == that.state;
    }

    public int hashCode() {
        int result = this.decoration.hashCode();
        result = 31 * result + this.state.hashCode();
        return result;
    }
}


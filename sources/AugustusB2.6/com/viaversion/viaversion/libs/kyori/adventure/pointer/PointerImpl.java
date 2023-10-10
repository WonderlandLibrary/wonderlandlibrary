// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.pointer;

import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;

final class PointerImpl<T> implements Pointer<T>
{
    private final Class<T> type;
    private final Key key;
    
    PointerImpl(final Class<T> type, final Key key) {
        this.type = type;
        this.key = key;
    }
    
    @NotNull
    @Override
    public Class<T> type() {
        return this.type;
    }
    
    @NotNull
    @Override
    public Key key() {
        return this.key;
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
        final PointerImpl<?> that = (PointerImpl<?>)other;
        return this.type.equals(that.type) && this.key.equals(that.key);
    }
    
    @Override
    public int hashCode() {
        int result = this.type.hashCode();
        result = 31 * result + this.key.hashCode();
        return result;
    }
}

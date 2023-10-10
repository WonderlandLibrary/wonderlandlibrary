// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.key;

import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

final class KeyedValueImpl<T> implements Examinable, KeyedValue<T>
{
    private final Key key;
    private final T value;
    
    KeyedValueImpl(final Key key, final T value) {
        this.key = key;
        this.value = value;
    }
    
    @NotNull
    @Override
    public Key key() {
        return this.key;
    }
    
    @NotNull
    @Override
    public T value() {
        return this.value;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final KeyedValueImpl<?> that = (KeyedValueImpl<?>)other;
        return this.key.equals(that.key) && this.value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        int result = this.key.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("key", this.key), ExaminableProperty.of("value", this.value) });
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
}

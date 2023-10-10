// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "\"\\\"\" + this.value + \"\\\"\"", hasChildren = "false")
final class StringBinaryTagImpl extends AbstractBinaryTag implements StringBinaryTag
{
    private final String value;
    
    StringBinaryTagImpl(final String value) {
        this.value = value;
    }
    
    @NotNull
    @Override
    public String value() {
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
        final StringBinaryTagImpl that = (StringBinaryTagImpl)other;
        return this.value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "String.valueOf(this.value) + \"l\"", hasChildren = "false")
final class LongBinaryTagImpl extends AbstractBinaryTag implements LongBinaryTag
{
    private final long value;
    
    LongBinaryTagImpl(final long value) {
        this.value = value;
    }
    
    @Override
    public long value() {
        return this.value;
    }
    
    @Override
    public byte byteValue() {
        return (byte)(this.value & 0xFFL);
    }
    
    @Override
    public double doubleValue() {
        return (double)this.value;
    }
    
    @Override
    public float floatValue() {
        return (float)this.value;
    }
    
    @Override
    public int intValue() {
        return (int)this.value;
    }
    
    @Override
    public long longValue() {
        return this.value;
    }
    
    @Override
    public short shortValue() {
        return (short)(this.value & 0xFFFFL);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final LongBinaryTagImpl that = (LongBinaryTagImpl)other;
        return this.value == that.value;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

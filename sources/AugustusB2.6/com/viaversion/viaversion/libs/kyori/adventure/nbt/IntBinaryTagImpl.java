// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "String.valueOf(this.value) + \"i\"", hasChildren = "false")
final class IntBinaryTagImpl extends AbstractBinaryTag implements IntBinaryTag
{
    private final int value;
    
    IntBinaryTagImpl(final int value) {
        this.value = value;
    }
    
    @Override
    public int value() {
        return this.value;
    }
    
    @Override
    public byte byteValue() {
        return (byte)(this.value & 0xFF);
    }
    
    @Override
    public double doubleValue() {
        return this.value;
    }
    
    @Override
    public float floatValue() {
        return (float)this.value;
    }
    
    @Override
    public int intValue() {
        return this.value;
    }
    
    @Override
    public long longValue() {
        return this.value;
    }
    
    @Override
    public short shortValue() {
        return (short)(this.value & 0xFFFF);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final IntBinaryTagImpl that = (IntBinaryTagImpl)other;
        return this.value == that.value;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(this.value);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

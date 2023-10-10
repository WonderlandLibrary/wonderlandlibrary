// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "String.valueOf(this.value) + \"d\"", hasChildren = "false")
final class DoubleBinaryTagImpl extends AbstractBinaryTag implements DoubleBinaryTag
{
    private final double value;
    
    DoubleBinaryTagImpl(final double value) {
        this.value = value;
    }
    
    @Override
    public double value() {
        return this.value;
    }
    
    @Override
    public byte byteValue() {
        return (byte)(ShadyPines.floor(this.value) & 0xFF);
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
        return ShadyPines.floor(this.value);
    }
    
    @Override
    public long longValue() {
        return (long)Math.floor(this.value);
    }
    
    @Override
    public short shortValue() {
        return (short)(ShadyPines.floor(this.value) & 0xFFFF);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final DoubleBinaryTagImpl that = (DoubleBinaryTagImpl)other;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(that.value);
    }
    
    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

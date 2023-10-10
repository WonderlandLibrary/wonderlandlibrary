// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "\"byte[\" + this.value.length + \"]\"", childrenArray = "this.value", hasChildren = "this.value.length > 0")
final class ByteArrayBinaryTagImpl extends ArrayBinaryTagImpl implements ByteArrayBinaryTag
{
    final byte[] value;
    
    ByteArrayBinaryTagImpl(final byte[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }
    
    @Override
    public byte[] value() {
        return Arrays.copyOf(this.value, this.value.length);
    }
    
    @Override
    public int size() {
        return this.value.length;
    }
    
    @Override
    public byte get(final int index) {
        ArrayBinaryTagImpl.checkIndex(index, this.value.length);
        return this.value[index];
    }
    
    static byte[] value(final ByteArrayBinaryTag tag) {
        return (tag instanceof ByteArrayBinaryTagImpl) ? ((ByteArrayBinaryTagImpl)tag).value : tag.value();
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final ByteArrayBinaryTagImpl that = (ByteArrayBinaryTagImpl)other;
        return Arrays.equals(this.value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
    
    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            private int index;
            
            @Override
            public boolean hasNext() {
                return this.index < ByteArrayBinaryTagImpl.this.value.length - 1;
            }
            
            @Override
            public Byte next() {
                return ByteArrayBinaryTagImpl.this.value[this.index++];
            }
        };
    }
}

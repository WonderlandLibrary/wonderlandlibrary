// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.Iterator;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.function.LongConsumer;
import org.jetbrains.annotations.NotNull;
import java.util.stream.LongStream;
import java.util.Spliterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Arrays;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "\"long[\" + this.value.length + \"]\"", childrenArray = "this.value", hasChildren = "this.value.length > 0")
final class LongArrayBinaryTagImpl extends ArrayBinaryTagImpl implements LongArrayBinaryTag
{
    final long[] value;
    
    LongArrayBinaryTagImpl(final long[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }
    
    @Override
    public long[] value() {
        return Arrays.copyOf(this.value, this.value.length);
    }
    
    @Override
    public int size() {
        return this.value.length;
    }
    
    @Override
    public long get(final int index) {
        ArrayBinaryTagImpl.checkIndex(index, this.value.length);
        return this.value[index];
    }
    
    @Override
    public PrimitiveIterator.OfLong iterator() {
        return new PrimitiveIterator.OfLong() {
            private int index;
            
            @Override
            public boolean hasNext() {
                return this.index < LongArrayBinaryTagImpl.this.value.length - 1;
            }
            
            @Override
            public long nextLong() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return LongArrayBinaryTagImpl.this.value[this.index++];
            }
        };
    }
    
    @Override
    public Spliterator.OfLong spliterator() {
        return Arrays.spliterator(this.value);
    }
    
    @NotNull
    @Override
    public LongStream stream() {
        return Arrays.stream(this.value);
    }
    
    @Override
    public void forEachLong(@NotNull final LongConsumer action) {
        for (int i = 0, length = this.value.length; i < length; ++i) {
            action.accept(this.value[i]);
        }
    }
    
    static long[] value(final LongArrayBinaryTag tag) {
        return (tag instanceof LongArrayBinaryTagImpl) ? ((LongArrayBinaryTagImpl)tag).value : tag.value();
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final LongArrayBinaryTagImpl that = (LongArrayBinaryTagImpl)other;
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
}

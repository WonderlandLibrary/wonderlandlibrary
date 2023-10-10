// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.util.Iterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.Spliterator;
import java.util.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public interface IntArrayBinaryTag extends ArrayBinaryTag, Iterable<Integer>
{
    @NotNull
    default IntArrayBinaryTag of(final int... value) {
        return new IntArrayBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<IntArrayBinaryTag> type() {
        return BinaryTagTypes.INT_ARRAY;
    }
    
    int[] value();
    
    int size();
    
    int get(final int index);
    
    PrimitiveIterator.OfInt iterator();
    
    Spliterator.OfInt spliterator();
    
    @NotNull
    IntStream stream();
    
    void forEachInt(@NotNull final IntConsumer action);
}

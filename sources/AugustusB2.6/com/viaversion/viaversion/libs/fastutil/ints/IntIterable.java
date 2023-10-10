// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.IntConsumer;

public interface IntIterable extends Iterable<Integer>
{
    IntIterator iterator();
    
    default IntIterator intIterator() {
        return this.iterator();
    }
    
    default IntSpliterator spliterator() {
        return IntSpliterators.asSpliteratorUnknownSize(this.iterator(), 0);
    }
    
    default IntSpliterator intSpliterator() {
        return this.spliterator();
    }
    
    default void forEach(final IntConsumer action) {
        Objects.requireNonNull(action);
        this.iterator().forEachRemaining(action);
    }
    
    default void forEach(final com.viaversion.viaversion.libs.fastutil.ints.IntConsumer action) {
        this.forEach((IntConsumer)action);
    }
    
    @Deprecated
    default void forEach(final Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        IntConsumer action2;
        if (action instanceof IntConsumer) {
            action2 = (IntConsumer)action;
        }
        else {
            Objects.requireNonNull(action);
            action2 = action::accept;
        }
        this.forEach(action2);
    }
}

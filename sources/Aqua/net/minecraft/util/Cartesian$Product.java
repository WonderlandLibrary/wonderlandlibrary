package net.minecraft.util;

import java.util.Collections;
import java.util.Iterator;
import net.minecraft.util.Cartesian;

/*
 * Exception performing whole class analysis ignored.
 */
static class Cartesian.Product<T>
implements Iterable<T[]> {
    private final Class<T> clazz;
    private final Iterable<? extends T>[] iterables;

    private Cartesian.Product(Class<T> clazz, Iterable<? extends T>[] iterables) {
        this.clazz = clazz;
        this.iterables = iterables;
    }

    public Iterator<T[]> iterator() {
        return this.iterables.length <= 0 ? Collections.singletonList((Object)Cartesian.access$200(this.clazz, (int)0)).iterator() : new ProductIterator(this.clazz, this.iterables, null);
    }
}

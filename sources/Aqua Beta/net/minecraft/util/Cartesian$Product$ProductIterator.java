package net.minecraft.util;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.minecraft.util.Cartesian;

/*
 * Exception performing whole class analysis ignored.
 */
static class Cartesian.Product.ProductIterator<T>
extends UnmodifiableIterator<T[]> {
    private int index = -2;
    private final Iterable<? extends T>[] iterables;
    private final Iterator<? extends T>[] iterators;
    private final T[] results;

    private Cartesian.Product.ProductIterator(Class<T> clazz, Iterable<? extends T>[] iterables) {
        this.iterables = iterables;
        this.iterators = (Iterator[])Cartesian.access$200(Iterator.class, (int)this.iterables.length);
        for (int i = 0; i < this.iterables.length; ++i) {
            this.iterators[i] = iterables[i].iterator();
        }
        this.results = Cartesian.access$200(clazz, (int)this.iterators.length);
    }

    private void endOfData() {
        this.index = -1;
        Arrays.fill((Object[])this.iterators, null);
        Arrays.fill((Object[])this.results, null);
    }

    public boolean hasNext() {
        if (this.index == -2) {
            this.index = 0;
            for (Iterator<? extends T> iterator1 : this.iterators) {
                if (iterator1.hasNext()) continue;
                this.endOfData();
                break;
            }
            return true;
        }
        if (this.index >= this.iterators.length) {
            Iterator iterator;
            this.index = this.iterators.length - 1;
            while (this.index >= 0 && !(iterator = this.iterators[this.index]).hasNext()) {
                if (this.index == 0) {
                    this.endOfData();
                    break;
                }
                this.iterators[this.index] = iterator = this.iterables[this.index].iterator();
                if (!iterator.hasNext()) {
                    this.endOfData();
                    break;
                }
                --this.index;
            }
        }
        return this.index >= 0;
    }

    public T[] next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        while (this.index < this.iterators.length) {
            this.results[this.index] = this.iterators[this.index].next();
            ++this.index;
        }
        return (Object[])this.results.clone();
    }
}

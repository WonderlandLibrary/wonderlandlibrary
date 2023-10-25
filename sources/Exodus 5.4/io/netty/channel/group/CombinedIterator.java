/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.group;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class CombinedIterator<E>
implements Iterator<E> {
    private final Iterator<E> i1;
    private Iterator<E> currentIterator;
    private final Iterator<E> i2;

    CombinedIterator(Iterator<E> iterator, Iterator<E> iterator2) {
        if (iterator == null) {
            throw new NullPointerException("i1");
        }
        if (iterator2 == null) {
            throw new NullPointerException("i2");
        }
        this.i1 = iterator;
        this.i2 = iterator2;
        this.currentIterator = iterator;
    }

    @Override
    public boolean hasNext() {
        while (true) {
            if (this.currentIterator.hasNext()) {
                return true;
            }
            if (this.currentIterator != this.i1) break;
            this.currentIterator = this.i2;
        }
        return false;
    }

    @Override
    public void remove() {
        this.currentIterator.remove();
    }

    @Override
    public E next() {
        while (true) {
            try {
                return this.currentIterator.next();
            }
            catch (NoSuchElementException noSuchElementException) {
                if (this.currentIterator == this.i1) {
                    this.currentIterator = this.i2;
                    continue;
                }
                throw noSuchElementException;
            }
            break;
        }
    }
}


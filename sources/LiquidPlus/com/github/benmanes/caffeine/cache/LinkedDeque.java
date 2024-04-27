/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.checkerframework.checker.nullness.qual.Nullable;

interface LinkedDeque<E>
extends Deque<E> {
    public boolean isFirst(E var1);

    public boolean isLast(E var1);

    public void moveToFront(E var1);

    public void moveToBack(E var1);

    public @Nullable E getPrevious(E var1);

    public void setPrevious(E var1, @Nullable E var2);

    public @Nullable E getNext(E var1);

    public void setNext(E var1, @Nullable E var2);

    @Override
    public PeekingIterator<E> iterator();

    @Override
    public PeekingIterator<E> descendingIterator();

    public static interface PeekingIterator<E>
    extends Iterator<E> {
        public @Nullable E peek();

        public static <E> PeekingIterator<E> concat(final PeekingIterator<E> first, final PeekingIterator<E> second) {
            return new PeekingIterator<E>(){

                @Override
                public boolean hasNext() {
                    return first.hasNext() || second.hasNext();
                }

                @Override
                public E next() {
                    if (first.hasNext()) {
                        return first.next();
                    }
                    if (second.hasNext()) {
                        return second.next();
                    }
                    throw new NoSuchElementException();
                }

                @Override
                public @Nullable E peek() {
                    return first.hasNext() ? first.peek() : second.peek();
                }
            };
        }

        public static <E> PeekingIterator<E> comparing(final PeekingIterator<E> first, final PeekingIterator<E> second, final Comparator<E> comparator) {
            return new PeekingIterator<E>(){

                @Override
                public boolean hasNext() {
                    return first.hasNext() || second.hasNext();
                }

                @Override
                public E next() {
                    Object o2;
                    if (!first.hasNext()) {
                        return second.next();
                    }
                    if (!second.hasNext()) {
                        return first.next();
                    }
                    Object o1 = first.peek();
                    boolean greaterOrEqual = comparator.compare(o1, o2 = second.peek()) >= 0;
                    return greaterOrEqual ? first.next() : second.next();
                }

                @Override
                public @Nullable E peek() {
                    Object o2;
                    if (!first.hasNext()) {
                        return second.peek();
                    }
                    if (!second.hasNext()) {
                        return first.peek();
                    }
                    Object o1 = first.peek();
                    boolean greaterOrEqual = comparator.compare(o1, o2 = second.peek()) >= 0;
                    return greaterOrEqual ? first.peek() : second.peek();
                }
            };
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.LinkedDeque;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.NoSuchElementException;
import org.checkerframework.checker.nullness.qual.Nullable;

abstract class AbstractLinkedDeque<E>
extends AbstractCollection<E>
implements LinkedDeque<E> {
    @Nullable E first;
    @Nullable E last;

    AbstractLinkedDeque() {
    }

    void linkFirst(E e) {
        E f = this.first;
        this.first = e;
        if (f == null) {
            this.last = e;
        } else {
            this.setPrevious(f, e);
            this.setNext(e, f);
        }
    }

    void linkLast(E e) {
        E l = this.last;
        this.last = e;
        if (l == null) {
            this.first = e;
        } else {
            this.setNext(l, e);
            this.setPrevious(e, l);
        }
    }

    E unlinkFirst() {
        E f = this.first;
        E next = this.getNext(f);
        this.setNext(f, null);
        this.first = next;
        if (next == null) {
            this.last = null;
        } else {
            this.setPrevious(next, null);
        }
        return f;
    }

    E unlinkLast() {
        E l = this.last;
        E prev = this.getPrevious(l);
        this.setPrevious(l, null);
        this.last = prev;
        if (prev == null) {
            this.first = null;
        } else {
            this.setNext(prev, null);
        }
        return l;
    }

    void unlink(E e) {
        E prev = this.getPrevious(e);
        E next = this.getNext(e);
        if (prev == null) {
            this.first = next;
        } else {
            this.setNext(prev, next);
            this.setPrevious(e, null);
        }
        if (next == null) {
            this.last = prev;
        } else {
            this.setPrevious(next, prev);
            this.setNext(e, null);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.first == null;
    }

    void checkNotEmpty() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int size() {
        int size = 0;
        E e = this.first;
        while (e != null) {
            ++size;
            e = this.getNext(e);
        }
        return size;
    }

    @Override
    public void clear() {
        E e = this.first;
        while (e != null) {
            E next = this.getNext(e);
            this.setPrevious(e, null);
            this.setNext(e, null);
            e = next;
        }
        this.last = null;
        this.first = null;
    }

    @Override
    public abstract boolean contains(Object var1);

    @Override
    public boolean isFirst(E e) {
        return e != null && e == this.first;
    }

    @Override
    public boolean isLast(E e) {
        return e != null && e == this.last;
    }

    @Override
    public void moveToFront(E e) {
        if (e != this.first) {
            this.unlink(e);
            this.linkFirst(e);
        }
    }

    @Override
    public void moveToBack(E e) {
        if (e != this.last) {
            this.unlink(e);
            this.linkLast(e);
        }
    }

    @Override
    public @Nullable E peek() {
        return this.peekFirst();
    }

    @Override
    public @Nullable E peekFirst() {
        return this.first;
    }

    @Override
    public @Nullable E peekLast() {
        return this.last;
    }

    @Override
    public E getFirst() {
        this.checkNotEmpty();
        return this.peekFirst();
    }

    @Override
    public E getLast() {
        this.checkNotEmpty();
        return this.peekLast();
    }

    @Override
    public E element() {
        return this.getFirst();
    }

    @Override
    public boolean offer(E e) {
        return this.offerLast(e);
    }

    @Override
    public boolean offerFirst(E e) {
        if (this.contains(e)) {
            return false;
        }
        this.linkFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        if (this.contains(e)) {
            return false;
        }
        this.linkLast(e);
        return true;
    }

    @Override
    public boolean add(E e) {
        return this.offerLast(e);
    }

    @Override
    public void addFirst(E e) {
        if (!this.offerFirst(e)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void addLast(E e) {
        if (!this.offerLast(e)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public @Nullable E poll() {
        return this.pollFirst();
    }

    @Override
    public @Nullable E pollFirst() {
        return this.isEmpty() ? null : (E)this.unlinkFirst();
    }

    @Override
    public @Nullable E pollLast() {
        return this.isEmpty() ? null : (E)this.unlinkLast();
    }

    @Override
    public E remove() {
        return this.removeFirst();
    }

    @Override
    public E removeFirst() {
        this.checkNotEmpty();
        return this.pollFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return this.remove(o);
    }

    @Override
    public E removeLast() {
        this.checkNotEmpty();
        return this.pollLast();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return this.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            modified |= this.remove(o);
        }
        return modified;
    }

    @Override
    public void push(E e) {
        this.addFirst(e);
    }

    @Override
    public E pop() {
        return this.removeFirst();
    }

    @Override
    public LinkedDeque.PeekingIterator<E> iterator() {
        return new AbstractLinkedIterator((Object)this.first){

            @Override
            @Nullable E computeNext() {
                return AbstractLinkedDeque.this.getNext(this.cursor);
            }
        };
    }

    @Override
    public LinkedDeque.PeekingIterator<E> descendingIterator() {
        return new AbstractLinkedIterator((Object)this.last){

            @Override
            @Nullable E computeNext() {
                return AbstractLinkedDeque.this.getPrevious(this.cursor);
            }
        };
    }

    abstract class AbstractLinkedIterator
    implements LinkedDeque.PeekingIterator<E> {
        @Nullable E previous;
        @Nullable E cursor;

        AbstractLinkedIterator(E start) {
            this.cursor = start;
        }

        @Override
        public boolean hasNext() {
            return this.cursor != null;
        }

        @Override
        public @Nullable E peek() {
            return this.cursor;
        }

        @Override
        public E next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.previous = this.cursor;
            this.cursor = this.computeNext();
            return this.previous;
        }

        abstract @Nullable E computeNext();

        @Override
        public void remove() {
            if (this.previous == null) {
                throw new IllegalStateException();
            }
            AbstractLinkedDeque.this.remove(this.previous);
            this.previous = null;
        }
    }
}


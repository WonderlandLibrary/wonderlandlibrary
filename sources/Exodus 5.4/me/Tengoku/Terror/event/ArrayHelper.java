/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event;

import java.util.Iterator;

public class ArrayHelper<T>
implements Iterable<T> {
    private T[] elements;

    public ArrayHelper(T[] TArray) {
        this.elements = TArray;
    }

    public void clear() {
        this.elements = new Object[0];
    }

    public boolean contains(T t) {
        T[] TArray = this.array();
        int n = TArray.length;
        int n2 = 0;
        while (n2 < n) {
            T t2 = TArray[n2];
            if (t2.equals(t)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public void add(T t) {
        if (t != null) {
            Object[] objectArray = new Object[this.size() + 1];
            int n = 0;
            while (n < objectArray.length) {
                objectArray[n] = n < this.size() ? this.get(n) : t;
                ++n;
            }
            this.set(objectArray);
        }
    }

    public void set(T[] TArray) {
        this.elements = TArray;
    }

    public int size() {
        return this.array().length;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public void remove(T t) {
        if (this.contains(t)) {
            Object[] objectArray = new Object[this.size() - 1];
            boolean bl = true;
            int n = 0;
            while (n < this.size()) {
                if (bl && this.get(n).equals(t)) {
                    bl = false;
                } else {
                    objectArray[bl ? n : n - 1] = this.get(n);
                }
                ++n;
            }
            this.set(objectArray);
        }
    }

    public T get(int n) {
        return this.array()[n];
    }

    public T[] array() {
        return this.elements;
    }

    public ArrayHelper() {
        this.elements = new Object[0];
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(){
            private int index = 0;

            @Override
            public T next() {
                return ArrayHelper.this.get(this.index++);
            }

            @Override
            public boolean hasNext() {
                return this.index < ArrayHelper.this.size() && ArrayHelper.this.get(this.index) != null;
            }

            @Override
            public void remove() {
                ArrayHelper.this.remove(ArrayHelper.this.get(this.index));
            }
        };
    }
}


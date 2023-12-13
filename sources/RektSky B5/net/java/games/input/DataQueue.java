/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

final class DataQueue<T> {
    private final T[] elements;
    private int position;
    private int limit;

    public DataQueue(int size, Class<T> element_type) {
        this.elements = (Object[])Array.newInstance(element_type, size);
        for (int i2 = 0; i2 < this.elements.length; ++i2) {
            try {
                this.elements[i2] = element_type.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                continue;
            }
            catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e2) {
                throw new RuntimeException(e2);
            }
        }
        this.clear();
    }

    public final void clear() {
        this.position = 0;
        this.limit = this.elements.length;
    }

    public final int position() {
        return this.position;
    }

    public final int limit() {
        return this.limit;
    }

    public final T get(int index) {
        assert (index < this.limit);
        return this.elements[index];
    }

    public final T get() {
        if (!this.hasRemaining()) {
            return null;
        }
        return this.get(this.position++);
    }

    public final void compact() {
        int index = 0;
        while (this.hasRemaining()) {
            this.swap(this.position, index);
            ++this.position;
            ++index;
        }
        this.position = index;
        this.limit = this.elements.length;
    }

    private final void swap(int index1, int index2) {
        T temp = this.elements[index1];
        this.elements[index1] = this.elements[index2];
        this.elements[index2] = temp;
    }

    public final void flip() {
        this.limit = this.position;
        this.position = 0;
    }

    public final boolean hasRemaining() {
        return this.remaining() > 0;
    }

    public final int remaining() {
        return this.limit - this.position;
    }

    public final void position(int position) {
        this.position = position;
    }

    public final T[] getElements() {
        return this.elements;
    }
}


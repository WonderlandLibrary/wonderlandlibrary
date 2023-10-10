// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.utils;

public final class IntArray
{
    public int[] items;
    public int size;
    private boolean ordered;
    
    public IntArray() {
        this(true, 16);
    }
    
    private IntArray(final boolean ordered, final int capacity) {
        this.ordered = true;
        this.items = new int[16];
    }
    
    public final void add(final int value) {
        int[] items = this.items;
        if (this.size == items.length) {
            items = this.resize(Math.max(8, (int)(this.size * 1.75f)));
        }
        items[this.size++] = value;
    }
    
    public final boolean contains(final int value) {
        int i = this.size - 1;
        final int[] items = this.items;
        while (i >= 0) {
            if (items[i--] == value) {
                return true;
            }
        }
        return false;
    }
    
    public final int[] shrink() {
        if (this.items.length != this.size) {
            this.resize(this.size);
        }
        return this.items;
    }
    
    private int[] resize(final int newSize) {
        final int[] newItems = new int[newSize];
        System.arraycopy(this.items, 0, newItems, 0, Math.min(this.size, newItems.length));
        return this.items = newItems;
    }
    
    @Override
    public final int hashCode() {
        if (!this.ordered) {
            return super.hashCode();
        }
        final int[] items = this.items;
        int h = 1;
        for (int i = 0, n = this.size; i < n; ++i) {
            h = h * 31 + items[i];
        }
        return h;
    }
    
    @Override
    public final boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!this.ordered) {
            return false;
        }
        if (!(object instanceof IntArray)) {
            return false;
        }
        final IntArray array;
        if (!(array = (IntArray)object).ordered) {
            return false;
        }
        final int n;
        if ((n = this.size) != array.size) {
            return false;
        }
        final int[] items1 = this.items;
        final int[] items2 = array.items;
        for (int i = 0; i < n; ++i) {
            if (items1[i] != items2[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public final String toString() {
        if (this.size == 0) {
            return "[]";
        }
        final int[] items = this.items;
        final StringBuilder buffer;
        (buffer = new StringBuilder(32)).append('[');
        buffer.append(items[0], 0);
        for (int i = 1; i < this.size; ++i) {
            buffer.append(", ");
            buffer.append(items[i], 0);
        }
        buffer.append(']');
        return buffer.toString();
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.utils;

public final class FloatArray
{
    private float[] items;
    public int size;
    private boolean ordered;
    
    public FloatArray() {
        this(true, 16);
    }
    
    public FloatArray(final int capacity) {
        this(true, 8);
    }
    
    private FloatArray(final boolean ordered, final int capacity) {
        this.ordered = true;
        this.items = new float[capacity];
    }
    
    public final void add(final float value) {
        float[] items = this.items;
        if (this.size == items.length) {
            final int max = Math.max(8, (int)(this.size * 1.75f));
            items = (float[])(Object)this;
            final float[] items2 = new float[max];
            System.arraycopy(((FloatArray)(Object)items).items, 0, items2, 0, Math.min(((FloatArray)(Object)items).size, items2.length));
            ((FloatArray)(Object)items).items = items2;
            items = items2;
        }
        items[this.size++] = value;
    }
    
    public final float get(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size);
        }
        return this.items[index];
    }
    
    public final float pop() {
        final float[] items = this.items;
        final int size = this.size - 1;
        this.size = size;
        return items[size];
    }
    
    @Override
    public final int hashCode() {
        if (!this.ordered) {
            return super.hashCode();
        }
        final float[] items = this.items;
        int h = 1;
        for (int i = 0, n = this.size; i < n; ++i) {
            h = h * 31 + Float.floatToIntBits(items[i]);
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
        if (!(object instanceof FloatArray)) {
            return false;
        }
        final FloatArray array;
        if (!(array = (FloatArray)object).ordered) {
            return false;
        }
        final int n;
        if ((n = this.size) != array.size) {
            return false;
        }
        final float[] items1 = this.items;
        final float[] items2 = array.items;
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
        final float[] items = this.items;
        final com.badlogic.gdx.utils.StringBuilder buffer;
        (buffer = new com.badlogic.gdx.utils.StringBuilder(32)).append('[');
        buffer.append(items[0]);
        for (int i = 1; i < this.size; ++i) {
            buffer.append(", ");
            buffer.append(items[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }
}

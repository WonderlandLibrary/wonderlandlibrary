/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

public final class UVector32 {
    private int[] buffer = new int[32];
    private int length = 0;

    public boolean isEmpty() {
        return this.length == 0;
    }

    public int size() {
        return this.length;
    }

    public int elementAti(int i2) {
        return this.buffer[i2];
    }

    public int[] getBuffer() {
        return this.buffer;
    }

    public void addElement(int e2) {
        this.ensureAppendCapacity();
        this.buffer[this.length++] = e2;
    }

    public void setElementAt(int elem, int index) {
        this.buffer[index] = elem;
    }

    public void insertElementAt(int elem, int index) {
        this.ensureAppendCapacity();
        System.arraycopy(this.buffer, index, this.buffer, index + 1, this.length - index);
        this.buffer[index] = elem;
        ++this.length;
    }

    public void removeAllElements() {
        this.length = 0;
    }

    private void ensureAppendCapacity() {
        if (this.length >= this.buffer.length) {
            int newCapacity = this.buffer.length <= 65535 ? 4 * this.buffer.length : 2 * this.buffer.length;
            int[] newBuffer = new int[newCapacity];
            System.arraycopy(this.buffer, 0, newBuffer, 0, this.length);
            this.buffer = newBuffer;
        }
    }
}


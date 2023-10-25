/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

final class DataQueue {
    private final Object[] elements;
    private int position;
    private int limit;
    static final /* synthetic */ boolean $assertionsDisabled;

    public DataQueue(int size, Class element_type) {
        this.elements = new Object[size];
        for (int i = 0; i < this.elements.length; ++i) {
            try {
                this.elements[i] = element_type.newInstance();
                continue;
            }
            catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
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

    public final Object get(int index) {
        if (!$assertionsDisabled && index >= this.limit) {
            throw new AssertionError();
        }
        return this.elements[index];
    }

    public final Object get() {
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
        Object temp = this.elements[index1];
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

    public final Object[] getElements() {
        return this.elements;
    }

    static {
        $assertionsDisabled = !DataQueue.class.desiredAssertionStatus();
    }
}


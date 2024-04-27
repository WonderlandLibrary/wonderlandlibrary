/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal;

public class IntDeque {
    private int[] deque = new int[16];
    private int nextFree = 0;

    public void push(int value) {
        if (this.nextFree == this.deque.length) {
            int[] newDeque = new int[this.nextFree * 2];
            System.arraycopy(this.deque, 0, newDeque, 0, this.nextFree);
            this.deque = newDeque;
        }
        this.deque[this.nextFree++] = value;
    }

    public int pop() {
        return this.deque[--this.nextFree];
    }

    public int peek() {
        return this.deque[this.nextFree - 1];
    }

    public int getAndIncrement() {
        int n = this.nextFree - 1;
        int n2 = this.deque[n];
        this.deque[n] = n2 + 1;
        return n2;
    }

    public int decrementAndGet() {
        int n = this.nextFree - 1;
        int n2 = this.deque[n] - 1;
        this.deque[n] = n2;
        return n2;
    }

    public boolean isEmpty() {
        return this.nextFree == 0;
    }
}


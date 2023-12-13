/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util;

import java.util.IdentityHashMap;
import java.util.Map;

public abstract class Recycler<T> {
    private final ThreadLocal<Stack<T>> threadLocal = new ThreadLocal<Stack<T>>(){

        @Override
        protected Stack<T> initialValue() {
            return new Stack(Recycler.this, Thread.currentThread());
        }
    };

    public final T get() {
        Stack<T> stack = this.threadLocal.get();
        T o = stack.pop();
        if (o == null) {
            o = this.newObject(stack);
        }
        return o;
    }

    public final boolean recycle(T o, Handle handle) {
        Stack stack = (Stack)handle;
        if (stack.parent != this) {
            return false;
        }
        if (Thread.currentThread() != stack.thread) {
            return false;
        }
        stack.push(o);
        return true;
    }

    protected abstract T newObject(Handle var1);

    static final class Stack<T>
    implements Handle {
        private static final int INITIAL_CAPACITY = 256;
        final Recycler<T> parent;
        final Thread thread;
        private T[] elements;
        private int size;
        private final Map<T, Boolean> map = new IdentityHashMap<T, Boolean>(256);

        Stack(Recycler<T> parent, Thread thread) {
            this.parent = parent;
            this.thread = thread;
            this.elements = Stack.newArray(256);
        }

        T pop() {
            int size = this.size;
            if (size == 0) {
                return null;
            }
            T ret = this.elements[--size];
            this.elements[size] = null;
            this.map.remove(ret);
            this.size = size;
            return ret;
        }

        void push(T o) {
            if (this.map.put(o, Boolean.TRUE) != null) {
                throw new IllegalStateException("recycled already");
            }
            int size = this.size;
            if (size == this.elements.length) {
                T[] newElements = Stack.newArray(size << 1);
                System.arraycopy(this.elements, 0, newElements, 0, size);
                this.elements = newElements;
            }
            this.elements[size] = o;
            this.size = size + 1;
        }

        private static <T> T[] newArray(int length) {
            return new Object[length];
        }
    }

    public static interface Handle {
    }
}


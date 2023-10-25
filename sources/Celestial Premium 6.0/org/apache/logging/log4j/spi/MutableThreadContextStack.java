/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.spi.ThreadContextStack;

public class MutableThreadContextStack
implements ThreadContextStack {
    private static final long serialVersionUID = 50505011L;
    private final List<String> list;

    public MutableThreadContextStack(List<String> list) {
        this.list = new ArrayList<String>(list);
    }

    private MutableThreadContextStack(MutableThreadContextStack stack) {
        this.list = new ArrayList<String>(stack.list);
    }

    @Override
    public String pop() {
        if (this.list.isEmpty()) {
            return null;
        }
        int last = this.list.size() - 1;
        String result = this.list.remove(last);
        return result;
    }

    @Override
    public String peek() {
        if (this.list.isEmpty()) {
            return null;
        }
        int last = this.list.size() - 1;
        return this.list.get(last);
    }

    @Override
    public void push(String message) {
        this.list.add(message);
    }

    @Override
    public int getDepth() {
        return this.list.size();
    }

    @Override
    public List<String> asList() {
        return this.list;
    }

    @Override
    public void trim(int depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        }
        if (this.list == null) {
            return;
        }
        ArrayList<String> copy = new ArrayList<String>(this.list.size());
        int count = Math.min(depth, this.list.size());
        for (int i = 0; i < count; ++i) {
            copy.add(this.list.get(i));
        }
        this.list.clear();
        this.list.addAll(copy);
    }

    @Override
    public ThreadContextStack copy() {
        return new MutableThreadContextStack(this);
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    @Override
    public Iterator<String> iterator() {
        return this.list.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return this.list.toArray(ts);
    }

    @Override
    public boolean add(String s) {
        return this.list.add(s);
    }

    @Override
    public boolean remove(Object o) {
        return this.list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return this.list.containsAll(objects);
    }

    @Override
    public boolean addAll(Collection<? extends String> strings) {
        return this.list.addAll(strings);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return this.list.removeAll(objects);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return this.list.retainAll(objects);
    }

    public String toString() {
        return String.valueOf(this.list);
    }
}


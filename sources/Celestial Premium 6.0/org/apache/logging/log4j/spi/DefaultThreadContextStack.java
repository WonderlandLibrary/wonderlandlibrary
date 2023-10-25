/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.spi.MutableThreadContextStack;
import org.apache.logging.log4j.spi.ThreadContextStack;

public class DefaultThreadContextStack
implements ThreadContextStack {
    private static final long serialVersionUID = 5050501L;
    private static ThreadLocal<List<String>> stack = new ThreadLocal();
    private final boolean useStack;

    public DefaultThreadContextStack(boolean useStack) {
        this.useStack = useStack;
    }

    @Override
    public String pop() {
        if (!this.useStack) {
            return "";
        }
        List<String> list = stack.get();
        if (list == null || list.size() == 0) {
            throw new NoSuchElementException("The ThreadContext stack is empty");
        }
        ArrayList<String> copy = new ArrayList<String>(list);
        int last = copy.size() - 1;
        String result = (String)copy.remove(last);
        stack.set(Collections.unmodifiableList(copy));
        return result;
    }

    @Override
    public String peek() {
        List<String> list = stack.get();
        if (list == null || list.size() == 0) {
            return null;
        }
        int last = list.size() - 1;
        return list.get(last);
    }

    @Override
    public void push(String message) {
        if (!this.useStack) {
            return;
        }
        this.add(message);
    }

    @Override
    public int getDepth() {
        List<String> list = stack.get();
        return list == null ? 0 : list.size();
    }

    @Override
    public List<String> asList() {
        List<String> list = stack.get();
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public void trim(int depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        }
        List<String> list = stack.get();
        if (list == null) {
            return;
        }
        ArrayList<String> copy = new ArrayList<String>();
        int count = Math.min(depth, list.size());
        for (int i = 0; i < count; ++i) {
            copy.add(list.get(i));
        }
        stack.set(copy);
    }

    @Override
    public ThreadContextStack copy() {
        List<String> result = null;
        if (!this.useStack || (result = stack.get()) == null) {
            return new MutableThreadContextStack(new ArrayList<String>());
        }
        return new MutableThreadContextStack(result);
    }

    @Override
    public void clear() {
        stack.remove();
    }

    @Override
    public int size() {
        List<String> result = stack.get();
        return result == null ? 0 : result.size();
    }

    @Override
    public boolean isEmpty() {
        List<String> result = stack.get();
        return result == null || result.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        List<String> result = stack.get();
        return result != null && result.contains(o);
    }

    @Override
    public Iterator<String> iterator() {
        List<String> immutable = stack.get();
        if (immutable == null) {
            List empty = Collections.emptyList();
            return empty.iterator();
        }
        return immutable.iterator();
    }

    @Override
    public Object[] toArray() {
        List<String> result = stack.get();
        if (result == null) {
            return new String[0];
        }
        return result.toArray(new Object[result.size()]);
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        List<String> result = stack.get();
        if (result == null) {
            if (ts.length > 0) {
                ts[0] = null;
            }
            return ts;
        }
        return result.toArray(ts);
    }

    @Override
    public boolean add(String s) {
        if (!this.useStack) {
            return false;
        }
        List<String> list = stack.get();
        ArrayList<String> copy = list == null ? new ArrayList<String>() : new ArrayList<String>(list);
        copy.add(s);
        stack.set(Collections.unmodifiableList(copy));
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!this.useStack) {
            return false;
        }
        List<String> list = stack.get();
        if (list == null || list.size() == 0) {
            return false;
        }
        ArrayList<String> copy = new ArrayList<String>(list);
        boolean result = copy.remove(o);
        stack.set(Collections.unmodifiableList(copy));
        return result;
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        if (objects.isEmpty()) {
            return true;
        }
        List<String> list = stack.get();
        return list != null && list.containsAll(objects);
    }

    @Override
    public boolean addAll(Collection<? extends String> strings) {
        if (!this.useStack || strings.isEmpty()) {
            return false;
        }
        List<String> list = stack.get();
        ArrayList<? extends String> copy = list == null ? new ArrayList<String>() : new ArrayList<String>(list);
        copy.addAll(strings);
        stack.set(Collections.unmodifiableList(copy));
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        if (!this.useStack || objects.isEmpty()) {
            return false;
        }
        List<String> list = stack.get();
        if (list == null || list.isEmpty()) {
            return false;
        }
        ArrayList<String> copy = new ArrayList<String>(list);
        boolean result = copy.removeAll(objects);
        stack.set(Collections.unmodifiableList(copy));
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        if (!this.useStack || objects.isEmpty()) {
            return false;
        }
        List<String> list = stack.get();
        if (list == null || list.isEmpty()) {
            return false;
        }
        ArrayList<String> copy = new ArrayList<String>(list);
        boolean result = copy.retainAll(objects);
        stack.set(Collections.unmodifiableList(copy));
        return result;
    }

    public String toString() {
        List<String> list = stack.get();
        return list == null ? "[]" : list.toString();
    }
}


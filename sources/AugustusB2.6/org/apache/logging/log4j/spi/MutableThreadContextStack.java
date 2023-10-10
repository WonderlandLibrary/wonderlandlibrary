// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.ThreadContext;
import java.util.Objects;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class MutableThreadContextStack implements ThreadContextStack, StringBuilderFormattable
{
    private static final long serialVersionUID = 50505011L;
    private final List<String> list;
    private boolean frozen;
    
    public MutableThreadContextStack() {
        this(new ArrayList<String>());
    }
    
    public MutableThreadContextStack(final List<String> list) {
        this.list = new ArrayList<String>(list);
    }
    
    private MutableThreadContextStack(final MutableThreadContextStack stack) {
        this.list = new ArrayList<String>(stack.list);
    }
    
    private void checkInvariants() {
        if (this.frozen) {
            throw new UnsupportedOperationException("context stack has been frozen");
        }
    }
    
    @Override
    public String pop() {
        this.checkInvariants();
        if (this.list.isEmpty()) {
            return null;
        }
        final int last = this.list.size() - 1;
        final String result = this.list.remove(last);
        return result;
    }
    
    @Override
    public String peek() {
        if (this.list.isEmpty()) {
            return null;
        }
        final int last = this.list.size() - 1;
        return this.list.get(last);
    }
    
    @Override
    public void push(final String message) {
        this.checkInvariants();
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
    public void trim(final int depth) {
        this.checkInvariants();
        if (depth < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        }
        if (this.list == null) {
            return;
        }
        final List<String> copy = new ArrayList<String>(this.list.size());
        for (int count = Math.min(depth, this.list.size()), i = 0; i < count; ++i) {
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
        this.checkInvariants();
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
    public boolean contains(final Object o) {
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
    public <T> T[] toArray(final T[] ts) {
        return this.list.toArray(ts);
    }
    
    @Override
    public boolean add(final String s) {
        this.checkInvariants();
        return this.list.add(s);
    }
    
    @Override
    public boolean remove(final Object o) {
        this.checkInvariants();
        return this.list.remove(o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> objects) {
        return this.list.containsAll(objects);
    }
    
    @Override
    public boolean addAll(final Collection<? extends String> strings) {
        this.checkInvariants();
        return this.list.addAll(strings);
    }
    
    @Override
    public boolean removeAll(final Collection<?> objects) {
        this.checkInvariants();
        return this.list.removeAll(objects);
    }
    
    @Override
    public boolean retainAll(final Collection<?> objects) {
        this.checkInvariants();
        return this.list.retainAll(objects);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.list);
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        buffer.append('[');
        for (int i = 0; i < this.list.size(); ++i) {
            if (i > 0) {
                buffer.append(',').append(' ');
            }
            buffer.append(this.list.get(i));
        }
        buffer.append(']');
    }
    
    @Override
    public int hashCode() {
        return 31 + Objects.hashCode(this.list);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ThreadContextStack)) {
            return false;
        }
        final ThreadContextStack other = (ThreadContextStack)obj;
        final List<String> otherAsList = other.asList();
        return Objects.equals(this.list, otherAsList);
    }
    
    @Override
    public ThreadContext.ContextStack getImmutableStackOrNull() {
        return this.copy();
    }
    
    public void freeze() {
        this.frozen = true;
    }
    
    public boolean isFrozen() {
        return this.frozen;
    }
}

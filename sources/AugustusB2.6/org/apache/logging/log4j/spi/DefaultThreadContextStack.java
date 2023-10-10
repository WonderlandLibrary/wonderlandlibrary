// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.Strings;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class DefaultThreadContextStack implements ThreadContextStack, StringBuilderFormattable
{
    private static final long serialVersionUID = 5050501L;
    private static final ThreadLocal<MutableThreadContextStack> STACK;
    private final boolean useStack;
    
    public DefaultThreadContextStack(final boolean useStack) {
        this.useStack = useStack;
    }
    
    private MutableThreadContextStack getNonNullStackCopy() {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        return (MutableThreadContextStack)((values == null) ? new MutableThreadContextStack() : values.copy());
    }
    
    @Override
    public boolean add(final String s) {
        if (!this.useStack) {
            return false;
        }
        final MutableThreadContextStack copy = this.getNonNullStackCopy();
        copy.add(s);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return true;
    }
    
    @Override
    public boolean addAll(final Collection<? extends String> strings) {
        if (!this.useStack || strings.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack copy = this.getNonNullStackCopy();
        copy.addAll(strings);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return true;
    }
    
    @Override
    public List<String> asList() {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        if (values == null) {
            return Collections.emptyList();
        }
        return values.asList();
    }
    
    @Override
    public void clear() {
        DefaultThreadContextStack.STACK.remove();
    }
    
    @Override
    public boolean contains(final Object o) {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        return values != null && values.contains(o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> objects) {
        if (objects.isEmpty()) {
            return true;
        }
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        return values != null && values.containsAll(objects);
    }
    
    @Override
    public ThreadContextStack copy() {
        MutableThreadContextStack values = null;
        if (!this.useStack || (values = DefaultThreadContextStack.STACK.get()) == null) {
            return new MutableThreadContextStack();
        }
        return values.copy();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof DefaultThreadContextStack) {
            final DefaultThreadContextStack other = (DefaultThreadContextStack)obj;
            if (this.useStack != other.useStack) {
                return false;
            }
        }
        if (!(obj instanceof ThreadContextStack)) {
            return false;
        }
        final ThreadContextStack other2 = (ThreadContextStack)obj;
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        return values != null && values.equals(other2);
    }
    
    @Override
    public int getDepth() {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        return (values == null) ? 0 : values.getDepth();
    }
    
    @Override
    public int hashCode() {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((values == null) ? 0 : values.hashCode());
        return result;
    }
    
    @Override
    public boolean isEmpty() {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        return values == null || values.isEmpty();
    }
    
    @Override
    public Iterator<String> iterator() {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        if (values == null) {
            final List<String> empty = Collections.emptyList();
            return empty.iterator();
        }
        return values.iterator();
    }
    
    @Override
    public String peek() {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        if (values == null || values.isEmpty()) {
            return "";
        }
        return values.peek();
    }
    
    @Override
    public String pop() {
        if (!this.useStack) {
            return "";
        }
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        if (values == null || values.isEmpty()) {
            return "";
        }
        final MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
        final String result = copy.pop();
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return result;
    }
    
    @Override
    public void push(final String message) {
        if (!this.useStack) {
            return;
        }
        this.add(message);
    }
    
    @Override
    public boolean remove(final Object o) {
        if (!this.useStack) {
            return false;
        }
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        if (values == null || values.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
        final boolean result = copy.remove(o);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return result;
    }
    
    @Override
    public boolean removeAll(final Collection<?> objects) {
        if (!this.useStack || objects.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        if (values == null || values.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
        final boolean result = copy.removeAll(objects);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return result;
    }
    
    @Override
    public boolean retainAll(final Collection<?> objects) {
        if (!this.useStack || objects.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        if (values == null || values.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
        final boolean result = copy.retainAll(objects);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return result;
    }
    
    @Override
    public int size() {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        return (values == null) ? 0 : values.size();
    }
    
    @Override
    public Object[] toArray() {
        final MutableThreadContextStack result = DefaultThreadContextStack.STACK.get();
        if (result == null) {
            return Strings.EMPTY_ARRAY;
        }
        return result.toArray(new Object[result.size()]);
    }
    
    @Override
    public <T> T[] toArray(final T[] ts) {
        final MutableThreadContextStack result = DefaultThreadContextStack.STACK.get();
        if (result == null) {
            if (ts.length > 0) {
                ts[0] = null;
            }
            return ts;
        }
        return result.toArray(ts);
    }
    
    @Override
    public String toString() {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        return (values == null) ? "[]" : values.toString();
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        if (values == null) {
            buffer.append("[]");
        }
        else {
            StringBuilders.appendValue(buffer, values);
        }
    }
    
    @Override
    public void trim(final int depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        }
        final MutableThreadContextStack values = DefaultThreadContextStack.STACK.get();
        if (values == null) {
            return;
        }
        final MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
        copy.trim(depth);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
    }
    
    @Override
    public ThreadContext.ContextStack getImmutableStackOrNull() {
        return DefaultThreadContextStack.STACK.get();
    }
    
    static {
        STACK = new ThreadLocal<MutableThreadContextStack>();
    }
}

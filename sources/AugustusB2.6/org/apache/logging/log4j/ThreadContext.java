// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.AbstractCollection;
import java.util.Collections;
import org.apache.logging.log4j.message.ParameterizedMessage;
import java.util.Collection;
import org.apache.logging.log4j.spi.CleanableThreadContextMap;
import java.util.Iterator;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap2;
import org.apache.logging.log4j.spi.NoOpThreadContextMap;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.spi.ThreadContextMapFactory;
import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextStack;
import java.util.Map;

public final class ThreadContext
{
    public static final Map<String, String> EMPTY_MAP;
    public static final ThreadContextStack EMPTY_STACK;
    private static final String DISABLE_MAP = "disableThreadContextMap";
    private static final String DISABLE_STACK = "disableThreadContextStack";
    private static final String DISABLE_ALL = "disableThreadContext";
    private static boolean useStack;
    private static ThreadContextMap contextMap;
    private static ThreadContextStack contextStack;
    private static ReadOnlyThreadContextMap readOnlyContextMap;
    
    private ThreadContext() {
    }
    
    static void init() {
        ThreadContextMapFactory.init();
        ThreadContext.contextMap = null;
        final PropertiesUtil managerProps = PropertiesUtil.getProperties();
        final boolean disableAll = managerProps.getBooleanProperty("disableThreadContext");
        ThreadContext.useStack = (!managerProps.getBooleanProperty("disableThreadContextStack") && !disableAll);
        final boolean useMap = !managerProps.getBooleanProperty("disableThreadContextMap") && !disableAll;
        ThreadContext.contextStack = new DefaultThreadContextStack(ThreadContext.useStack);
        if (!useMap) {
            ThreadContext.contextMap = new NoOpThreadContextMap();
        }
        else {
            ThreadContext.contextMap = ThreadContextMapFactory.createThreadContextMap();
        }
        if (ThreadContext.contextMap instanceof ReadOnlyThreadContextMap) {
            ThreadContext.readOnlyContextMap = (ReadOnlyThreadContextMap)ThreadContext.contextMap;
        }
        else {
            ThreadContext.readOnlyContextMap = null;
        }
    }
    
    public static void put(final String key, final String value) {
        ThreadContext.contextMap.put(key, value);
    }
    
    public static void putIfNull(final String key, final String value) {
        if (!ThreadContext.contextMap.containsKey(key)) {
            ThreadContext.contextMap.put(key, value);
        }
    }
    
    public static void putAll(final Map<String, String> m) {
        if (ThreadContext.contextMap instanceof ThreadContextMap2) {
            ((ThreadContextMap2)ThreadContext.contextMap).putAll(m);
        }
        else if (ThreadContext.contextMap instanceof DefaultThreadContextMap) {
            ((DefaultThreadContextMap)ThreadContext.contextMap).putAll(m);
        }
        else {
            for (final Map.Entry<String, String> entry : m.entrySet()) {
                ThreadContext.contextMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    public static String get(final String key) {
        return ThreadContext.contextMap.get(key);
    }
    
    public static void remove(final String key) {
        ThreadContext.contextMap.remove(key);
    }
    
    public static void removeAll(final Iterable<String> keys) {
        if (ThreadContext.contextMap instanceof CleanableThreadContextMap) {
            ((CleanableThreadContextMap)ThreadContext.contextMap).removeAll(keys);
        }
        else if (ThreadContext.contextMap instanceof DefaultThreadContextMap) {
            ((DefaultThreadContextMap)ThreadContext.contextMap).removeAll(keys);
        }
        else {
            for (final String key : keys) {
                ThreadContext.contextMap.remove(key);
            }
        }
    }
    
    public static void clearMap() {
        ThreadContext.contextMap.clear();
    }
    
    public static void clearAll() {
        clearMap();
        clearStack();
    }
    
    public static boolean containsKey(final String key) {
        return ThreadContext.contextMap.containsKey(key);
    }
    
    public static Map<String, String> getContext() {
        return ThreadContext.contextMap.getCopy();
    }
    
    public static Map<String, String> getImmutableContext() {
        final Map<String, String> map = ThreadContext.contextMap.getImmutableMapOrNull();
        return (map == null) ? ThreadContext.EMPTY_MAP : map;
    }
    
    public static ReadOnlyThreadContextMap getThreadContextMap() {
        return ThreadContext.readOnlyContextMap;
    }
    
    public static boolean isEmpty() {
        return ThreadContext.contextMap.isEmpty();
    }
    
    public static void clearStack() {
        ThreadContext.contextStack.clear();
    }
    
    public static ContextStack cloneStack() {
        return ThreadContext.contextStack.copy();
    }
    
    public static ContextStack getImmutableStack() {
        final ContextStack result = ThreadContext.contextStack.getImmutableStackOrNull();
        return (result == null) ? ThreadContext.EMPTY_STACK : result;
    }
    
    public static void setStack(final Collection<String> stack) {
        if (stack.isEmpty() || !ThreadContext.useStack) {
            return;
        }
        ThreadContext.contextStack.clear();
        ThreadContext.contextStack.addAll(stack);
    }
    
    public static int getDepth() {
        return ThreadContext.contextStack.getDepth();
    }
    
    public static String pop() {
        return ThreadContext.contextStack.pop();
    }
    
    public static String peek() {
        return ThreadContext.contextStack.peek();
    }
    
    public static void push(final String message) {
        ThreadContext.contextStack.push(message);
    }
    
    public static void push(final String message, final Object... args) {
        ThreadContext.contextStack.push(ParameterizedMessage.format(message, args));
    }
    
    public static void removeStack() {
        ThreadContext.contextStack.clear();
    }
    
    public static void trim(final int depth) {
        ThreadContext.contextStack.trim(depth);
    }
    
    static {
        EMPTY_MAP = Collections.emptyMap();
        EMPTY_STACK = new EmptyThreadContextStack();
        init();
    }
    
    private static class EmptyThreadContextStack extends AbstractCollection<String> implements ThreadContextStack
    {
        private static final long serialVersionUID = 1L;
        private static final Iterator<String> EMPTY_ITERATOR;
        
        @Override
        public String pop() {
            return null;
        }
        
        @Override
        public String peek() {
            return null;
        }
        
        @Override
        public void push(final String message) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int getDepth() {
            return 0;
        }
        
        @Override
        public List<String> asList() {
            return Collections.emptyList();
        }
        
        @Override
        public void trim(final int depth) {
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Collection && ((Collection)o).isEmpty();
        }
        
        @Override
        public int hashCode() {
            return 1;
        }
        
        @Override
        public ContextStack copy() {
            return this;
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final String e) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return false;
        }
        
        @Override
        public boolean addAll(final Collection<? extends String> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Iterator<String> iterator() {
            return EmptyThreadContextStack.EMPTY_ITERATOR;
        }
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public ContextStack getImmutableStackOrNull() {
            return this;
        }
        
        static {
            EMPTY_ITERATOR = new EmptyIterator<String>();
        }
    }
    
    private static class EmptyIterator<E> implements Iterator<E>
    {
        @Override
        public boolean hasNext() {
            return false;
        }
        
        @Override
        public E next() {
            throw new NoSuchElementException("This is an empty iterator!");
        }
        
        @Override
        public void remove() {
        }
    }
    
    public interface ContextStack extends Serializable, Collection<String>
    {
        String pop();
        
        String peek();
        
        void push(final String message);
        
        int getDepth();
        
        List<String> asList();
        
        void trim(final int depth);
        
        ContextStack copy();
        
        ContextStack getImmutableStackOrNull();
    }
}

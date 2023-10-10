// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CloseableThreadContext
{
    private CloseableThreadContext() {
    }
    
    public static Instance push(final String message) {
        return new Instance().push(message);
    }
    
    public static Instance push(final String message, final Object... args) {
        return new Instance().push(message, args);
    }
    
    public static Instance put(final String key, final String value) {
        return new Instance().put(key, value);
    }
    
    public static Instance pushAll(final List<String> messages) {
        return new Instance().pushAll(messages);
    }
    
    public static Instance putAll(final Map<String, String> values) {
        return new Instance().putAll(values);
    }
    
    public static class Instance implements AutoCloseable
    {
        private int pushCount;
        private final Map<String, String> originalValues;
        
        private Instance() {
            this.pushCount = 0;
            this.originalValues = new HashMap<String, String>();
        }
        
        public Instance push(final String message) {
            ThreadContext.push(message);
            ++this.pushCount;
            return this;
        }
        
        public Instance push(final String message, final Object[] args) {
            ThreadContext.push(message, args);
            ++this.pushCount;
            return this;
        }
        
        public Instance put(final String key, final String value) {
            if (!this.originalValues.containsKey(key)) {
                this.originalValues.put(key, ThreadContext.get(key));
            }
            ThreadContext.put(key, value);
            return this;
        }
        
        public Instance putAll(final Map<String, String> values) {
            final Map<String, String> currentValues = ThreadContext.getContext();
            ThreadContext.putAll(values);
            for (final String key : values.keySet()) {
                if (!this.originalValues.containsKey(key)) {
                    this.originalValues.put(key, currentValues.get(key));
                }
            }
            return this;
        }
        
        public Instance pushAll(final List<String> messages) {
            for (final String message : messages) {
                this.push(message);
            }
            return this;
        }
        
        @Override
        public void close() {
            this.closeStack();
            this.closeMap();
        }
        
        private void closeMap() {
            final Iterator<Map.Entry<String, String>> it = this.originalValues.entrySet().iterator();
            while (it.hasNext()) {
                final Map.Entry<String, String> entry = it.next();
                final String key = entry.getKey();
                final String originalValue = entry.getValue();
                if (null == originalValue) {
                    ThreadContext.remove(key);
                }
                else {
                    ThreadContext.put(key, originalValue);
                }
                it.remove();
            }
        }
        
        private void closeStack() {
            for (int i = 0; i < this.pushCount; ++i) {
                ThreadContext.pop();
            }
            this.pushCount = 0;
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.util.HashMap;
import java.util.Map;

public class NoOpThreadContextMap implements ThreadContextMap
{
    @Override
    public void clear() {
    }
    
    @Override
    public boolean containsKey(final String key) {
        return false;
    }
    
    @Override
    public String get(final String key) {
        return null;
    }
    
    @Override
    public Map<String, String> getCopy() {
        return new HashMap<String, String>();
    }
    
    @Override
    public Map<String, String> getImmutableMapOrNull() {
        return null;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public void put(final String key, final String value) {
    }
    
    @Override
    public void remove(final String key) {
    }
}

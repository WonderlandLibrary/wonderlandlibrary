// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.ThreadContext;
import java.util.Map;
import org.apache.logging.log4j.core.util.ContextDataProvider;

public class ThreadContextDataProvider implements ContextDataProvider
{
    @Override
    public Map<String, String> supplyContextData() {
        return ThreadContext.getImmutableContext();
    }
    
    @Override
    public StringMap supplyStringMap() {
        return ThreadContext.getThreadContextMap().getReadOnlyContextData();
    }
}

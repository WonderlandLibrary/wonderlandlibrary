// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout.internal;

import java.util.List;

public class IncludeChecker implements ListChecker
{
    private final List<String> list;
    
    public IncludeChecker(final List<String> list) {
        this.list = list;
    }
    
    @Override
    public boolean check(final String key) {
        return this.list.contains(key);
    }
    
    @Override
    public String toString() {
        return "ThreadContextIncludes=" + this.list.toString();
    }
}

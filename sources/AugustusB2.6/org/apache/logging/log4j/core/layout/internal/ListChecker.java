// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout.internal;

public interface ListChecker
{
    public static final NoopChecker NOOP_CHECKER = new NoopChecker();
    
    boolean check(final String key);
    
    public static class NoopChecker implements ListChecker
    {
        @Override
        public boolean check(final String key) {
            return true;
        }
        
        @Override
        public String toString() {
            return "";
        }
    }
}

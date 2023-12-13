// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

public interface LookupResult
{
    String value();
    
    default boolean isLookupEvaluationAllowedInValue() {
        return false;
    }
}

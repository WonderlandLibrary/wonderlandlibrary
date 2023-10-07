// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import java.util.Objects;

final class DefaultLookupResult implements LookupResult
{
    private final String value;
    
    DefaultLookupResult(final String value) {
        this.value = Objects.requireNonNull(value, "value is required");
    }
    
    @Override
    public String value() {
        return this.value;
    }
    
    @Override
    public boolean isLookupEvaluationAllowedInValue() {
        return false;
    }
    
    @Override
    public String toString() {
        return "DefaultLookupResult{value='" + this.value + "'}";
    }
}

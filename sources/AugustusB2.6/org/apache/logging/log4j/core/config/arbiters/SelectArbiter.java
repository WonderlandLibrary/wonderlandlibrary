// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.arbiters;

import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import java.util.Iterator;
import java.util.Optional;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Select", category = "Core", elementType = "Arbiter", deferChildren = true, printObject = true)
public class SelectArbiter
{
    public Arbiter evaluateConditions(final List<Arbiter> conditions) {
        final IllegalStateException ex;
        final Optional<Arbiter> opt = conditions.stream().filter(c -> c instanceof DefaultArbiter).reduce((a, b) -> {
            new IllegalStateException("Multiple elements: " + a + ", " + b);
            throw ex;
        });
        for (final Arbiter condition : conditions) {
            if (condition instanceof DefaultArbiter) {
                continue;
            }
            if (condition.isCondition()) {
                return condition;
            }
        }
        return opt.orElse(null);
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<SelectArbiter>
    {
        public Builder asBuilder() {
            return this;
        }
        
        @Override
        public SelectArbiter build() {
            return new SelectArbiter();
        }
    }
}

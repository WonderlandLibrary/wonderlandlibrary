// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.arbiters;

import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DefaultArbiter", category = "Core", elementType = "Arbiter", deferChildren = true, printObject = true)
public class DefaultArbiter implements Arbiter
{
    @Override
    public boolean isCondition() {
        return true;
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<DefaultArbiter>
    {
        public Builder asBuilder() {
            return this;
        }
        
        @Override
        public DefaultArbiter build() {
            return new DefaultArbiter();
        }
    }
}

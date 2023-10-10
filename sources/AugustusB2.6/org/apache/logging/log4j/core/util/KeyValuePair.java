// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "KeyValuePair", category = "Core", printObject = true)
public final class KeyValuePair
{
    public static final KeyValuePair[] EMPTY_ARRAY;
    private final String key;
    private final String value;
    
    public KeyValuePair(final String key, final String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return this.key + '=' + this.value;
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final KeyValuePair other = (KeyValuePair)obj;
        return Objects.equals(this.key, other.key) && Objects.equals(this.value, other.value);
    }
    
    static {
        EMPTY_ARRAY = new KeyValuePair[0];
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<KeyValuePair>
    {
        @PluginBuilderAttribute
        private String key;
        @PluginBuilderAttribute
        private String value;
        
        public Builder setKey(final String aKey) {
            this.key = aKey;
            return this;
        }
        
        public Builder setValue(final String aValue) {
            this.value = aValue;
            return this;
        }
        
        @Override
        public KeyValuePair build() {
            return new KeyValuePair(this.key, this.value);
        }
    }
}

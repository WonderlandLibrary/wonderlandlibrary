/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;

@Plugin(name="KeyValuePair", category="Core", printObject=true)
public final class KeyValuePair {
    private final String key;
    private final String value;

    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return this.key + '=' + this.value;
    }

    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        KeyValuePair other = (KeyValuePair)obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return Objects.equals(this.value, other.value);
    }

    public static class Builder
    implements org.apache.logging.log4j.core.util.Builder<KeyValuePair> {
        @PluginBuilderAttribute
        private String key;
        @PluginBuilderAttribute
        private String value;

        public Builder setKey(String aKey) {
            this.key = aKey;
            return this;
        }

        public Builder setValue(String aValue) {
            this.value = aValue;
            return this;
        }

        @Override
        public KeyValuePair build() {
            return new KeyValuePair(this.key, this.value);
        }
    }
}


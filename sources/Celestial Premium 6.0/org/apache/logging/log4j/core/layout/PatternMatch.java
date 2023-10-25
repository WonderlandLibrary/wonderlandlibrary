/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;

@Plugin(name="PatternMatch", category="Core", printObject=true)
public final class PatternMatch {
    private final String key;
    private final String pattern;

    public PatternMatch(String key, String pattern) {
        this.key = key;
        this.pattern = pattern;
    }

    public String getKey() {
        return this.key;
    }

    public String getPattern() {
        return this.pattern;
    }

    public String toString() {
        return this.key + '=' + this.pattern;
    }

    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Objects.hash(this.key, this.pattern);
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
        PatternMatch other = (PatternMatch)obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return Objects.equals(this.pattern, other.pattern);
    }

    public static class Builder
    implements org.apache.logging.log4j.core.util.Builder<PatternMatch>,
    Serializable {
        private static final long serialVersionUID = 1L;
        @PluginBuilderAttribute
        private String key;
        @PluginBuilderAttribute
        private String pattern;

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        @Override
        public PatternMatch build() {
            return new PatternMatch(this.key, this.pattern);
        }

        protected Object readResolve() throws ObjectStreamException {
            return new PatternMatch(this.key, this.pattern);
        }
    }
}


/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.arbiters;

import org.apache.logging.log4j.core.config.arbiters.Arbiter;
import org.apache.logging.log4j.core.config.arbiters.SystemPropertyArbiter;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.util.LoaderUtil;

@Plugin(name="ClassArbiter", category="Core", elementType="Arbiter", printObject=true, deferChildren=true)
public class ClassArbiter
implements Arbiter {
    private final String className;

    private ClassArbiter(String className) {
        this.className = className;
    }

    @Override
    public boolean isCondition() {
        return LoaderUtil.isClassAvailable(this.className);
    }

    @PluginBuilderFactory
    public static SystemPropertyArbiter.Builder newBuilder() {
        return new SystemPropertyArbiter.Builder();
    }

    public static class Builder
    implements org.apache.logging.log4j.core.util.Builder<ClassArbiter> {
        public static final String ATTR_CLASS_NAME = "className";
        @PluginBuilderAttribute(value="className")
        private String className;

        public Builder setClassName(String className) {
            this.className = className;
            return this.asBuilder();
        }

        public Builder asBuilder() {
            return this;
        }

        @Override
        public ClassArbiter build() {
            return new ClassArbiter(this.className);
        }
    }
}


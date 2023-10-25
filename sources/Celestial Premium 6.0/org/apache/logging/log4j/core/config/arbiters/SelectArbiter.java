/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.arbiters;

import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.core.config.arbiters.Arbiter;
import org.apache.logging.log4j.core.config.arbiters.DefaultArbiter;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;

@Plugin(name="Select", category="Core", elementType="Arbiter", deferChildren=true, printObject=true)
public class SelectArbiter {
    public Arbiter evaluateConditions(List<Arbiter> conditions) {
        Optional<Arbiter> opt = conditions.stream().filter(c -> c instanceof DefaultArbiter).reduce((a, b) -> {
            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
        });
        for (Arbiter condition : conditions) {
            if (condition instanceof DefaultArbiter || !condition.isCondition()) continue;
            return condition;
        }
        return opt.orElse(null);
    }

    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder
    implements org.apache.logging.log4j.core.util.Builder<SelectArbiter> {
        public Builder asBuilder() {
            return this;
        }

        @Override
        public SelectArbiter build() {
            return new SelectArbiter();
        }
    }
}


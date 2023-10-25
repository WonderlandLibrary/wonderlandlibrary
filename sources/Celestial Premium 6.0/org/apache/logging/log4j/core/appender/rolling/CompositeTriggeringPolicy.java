/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name="Policies", category="Core", printObject=true)
public final class CompositeTriggeringPolicy
implements TriggeringPolicy {
    private final TriggeringPolicy[] policies;

    private CompositeTriggeringPolicy(TriggeringPolicy ... policies) {
        this.policies = policies;
    }

    @Override
    public void initialize(RollingFileManager manager) {
        for (TriggeringPolicy policy : this.policies) {
            policy.initialize(manager);
        }
    }

    @Override
    public boolean isTriggeringEvent(LogEvent event) {
        for (TriggeringPolicy policy : this.policies) {
            if (!policy.isTriggeringEvent(event)) continue;
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CompositeTriggeringPolicy{");
        boolean first = true;
        for (TriggeringPolicy policy : this.policies) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(policy.toString());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    @PluginFactory
    public static CompositeTriggeringPolicy createPolicy(TriggeringPolicy ... policies) {
        return new CompositeTriggeringPolicy(policies);
    }
}


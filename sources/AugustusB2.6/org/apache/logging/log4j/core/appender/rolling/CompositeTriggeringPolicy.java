// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import java.util.Arrays;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LifeCycle2;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Policies", category = "Core", printObject = true)
public final class CompositeTriggeringPolicy extends AbstractTriggeringPolicy
{
    private final TriggeringPolicy[] triggeringPolicies;
    
    private CompositeTriggeringPolicy(final TriggeringPolicy... triggeringPolicies) {
        this.triggeringPolicies = triggeringPolicies;
    }
    
    public TriggeringPolicy[] getTriggeringPolicies() {
        return this.triggeringPolicies;
    }
    
    @Override
    public void initialize(final RollingFileManager manager) {
        for (final TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
            CompositeTriggeringPolicy.LOGGER.debug("Initializing triggering policy {}", triggeringPolicy.toString());
            triggeringPolicy.initialize(manager);
        }
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent event) {
        for (final TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
            if (triggeringPolicy.isTriggeringEvent(event)) {
                return true;
            }
        }
        return false;
    }
    
    @PluginFactory
    public static CompositeTriggeringPolicy createPolicy(@PluginElement("Policies") final TriggeringPolicy... triggeringPolicy) {
        return new CompositeTriggeringPolicy(triggeringPolicy);
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = true;
        for (final TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
            if (triggeringPolicy instanceof LifeCycle2) {
                stopped &= ((LifeCycle2)triggeringPolicy).stop(timeout, timeUnit);
            }
            else if (triggeringPolicy instanceof LifeCycle) {
                ((LifeCycle)triggeringPolicy).stop();
                stopped &= true;
            }
        }
        this.setStopped();
        return stopped;
    }
    
    @Override
    public String toString() {
        return "CompositeTriggeringPolicy(policies=" + Arrays.toString(this.triggeringPolicies) + ")";
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.util.Arrays;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "IfAny", category = "Core", printObject = true)
public final class IfAny implements PathCondition
{
    private final PathCondition[] components;
    
    private IfAny(final PathCondition... filters) {
        this.components = Objects.requireNonNull(filters, "filters");
    }
    
    public PathCondition[] getDeleteFilters() {
        return this.components;
    }
    
    @Override
    public boolean accept(final Path baseDir, final Path relativePath, final BasicFileAttributes attrs) {
        for (final PathCondition component : this.components) {
            if (component.accept(baseDir, relativePath, attrs)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void beforeFileTreeWalk() {
        for (final PathCondition condition : this.components) {
            condition.beforeFileTreeWalk();
        }
    }
    
    @PluginFactory
    public static IfAny createOrCondition(@PluginElement("PathConditions") final PathCondition... components) {
        return new IfAny(components);
    }
    
    @Override
    public String toString() {
        return "IfAny" + Arrays.toString(this.components);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "IfNot", category = "Core", printObject = true)
public final class IfNot implements PathCondition
{
    private final PathCondition negate;
    
    private IfNot(final PathCondition negate) {
        this.negate = Objects.requireNonNull(negate, "filter");
    }
    
    public PathCondition getWrappedFilter() {
        return this.negate;
    }
    
    @Override
    public boolean accept(final Path baseDir, final Path relativePath, final BasicFileAttributes attrs) {
        return !this.negate.accept(baseDir, relativePath, attrs);
    }
    
    @Override
    public void beforeFileTreeWalk() {
        this.negate.beforeFileTreeWalk();
    }
    
    @PluginFactory
    public static IfNot createNotCondition(@PluginElement("PathConditions") final PathCondition condition) {
        return new IfNot(condition);
    }
    
    @Override
    public String toString() {
        return "IfNot(" + this.negate + ")";
    }
}

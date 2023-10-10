// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "IfAccumulatedFileCount", category = "Core", printObject = true)
public final class IfAccumulatedFileCount implements PathCondition
{
    private static final Logger LOGGER;
    private final int threshold;
    private int count;
    private final PathCondition[] nestedConditions;
    
    private IfAccumulatedFileCount(final int thresholdParam, final PathCondition[] nestedConditions) {
        if (thresholdParam <= 0) {
            throw new IllegalArgumentException("Count must be a positive integer but was " + thresholdParam);
        }
        this.threshold = thresholdParam;
        this.nestedConditions = PathCondition.copy(nestedConditions);
    }
    
    public int getThresholdCount() {
        return this.threshold;
    }
    
    public List<PathCondition> getNestedConditions() {
        return Collections.unmodifiableList((List<? extends PathCondition>)Arrays.asList((T[])this.nestedConditions));
    }
    
    @Override
    public boolean accept(final Path basePath, final Path relativePath, final BasicFileAttributes attrs) {
        final boolean result = ++this.count > this.threshold;
        final String match = result ? ">" : "<=";
        final String accept = result ? "ACCEPTED" : "REJECTED";
        IfAccumulatedFileCount.LOGGER.trace("IfAccumulatedFileCount {}: {} count '{}' {} threshold '{}'", accept, relativePath, this.count, match, this.threshold);
        if (result) {
            return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs);
        }
        return result;
    }
    
    @Override
    public void beforeFileTreeWalk() {
        this.count = 0;
        IfAll.beforeFileTreeWalk(this.nestedConditions);
    }
    
    @PluginFactory
    public static IfAccumulatedFileCount createFileCountCondition(@PluginAttribute(value = "exceeds", defaultInt = Integer.MAX_VALUE) final int threshold, @PluginElement("PathConditions") final PathCondition... nestedConditions) {
        if (threshold == Integer.MAX_VALUE) {
            IfAccumulatedFileCount.LOGGER.error("IfAccumulatedFileCount invalid or missing threshold value.");
        }
        return new IfAccumulatedFileCount(threshold, nestedConditions);
    }
    
    @Override
    public String toString() {
        final String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString(this.nestedConditions));
        return "IfAccumulatedFileCount(exceeds=" + this.threshold + nested + ")";
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}

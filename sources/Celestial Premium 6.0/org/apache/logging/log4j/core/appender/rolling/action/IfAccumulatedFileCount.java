/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="IfAccumulatedFileCount", category="Core", printObject=true)
public final class IfAccumulatedFileCount
implements PathCondition {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final int threshold;
    private int count;
    private final PathCondition[] nestedConditions;

    private IfAccumulatedFileCount(int thresholdParam, PathCondition[] nestedConditions) {
        if (thresholdParam <= 0) {
            throw new IllegalArgumentException("Count must be a positive integer but was " + thresholdParam);
        }
        this.threshold = thresholdParam;
        this.nestedConditions = nestedConditions == null ? new PathCondition[0] : Arrays.copyOf(nestedConditions, nestedConditions.length);
    }

    public int getThresholdCount() {
        return this.threshold;
    }

    public List<PathCondition> getNestedConditions() {
        return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
    }

    @Override
    public boolean accept(Path basePath, Path relativePath, BasicFileAttributes attrs) {
        boolean result = ++this.count > this.threshold;
        String match = result ? ">" : "<=";
        String accept = result ? "ACCEPTED" : "REJECTED";
        LOGGER.trace("IfAccumulatedFileCount {}: {} count '{}' {} threshold '{}'", accept, relativePath, this.count, match, this.threshold);
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
    public static IfAccumulatedFileCount createFileCountCondition(@PluginAttribute(value="exceeds", defaultInt=0x7FFFFFFF) int threshold, PathCondition ... nestedConditions) {
        if (threshold == Integer.MAX_VALUE) {
            LOGGER.error("IfAccumulatedFileCount invalid or missing threshold value.");
        }
        return new IfAccumulatedFileCount(threshold, nestedConditions);
    }

    public String toString() {
        String nested = this.nestedConditions.length == 0 ? "" : " AND " + Arrays.toString(this.nestedConditions);
        return "IfAccumulatedFileCount(exceeds=" + this.threshold + nested + ")";
    }
}


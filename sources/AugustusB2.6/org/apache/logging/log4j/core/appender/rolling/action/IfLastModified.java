// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "IfLastModified", category = "Core", printObject = true)
public final class IfLastModified implements PathCondition
{
    private static final Logger LOGGER;
    private static final Clock CLOCK;
    private final Duration age;
    private final PathCondition[] nestedConditions;
    
    private IfLastModified(final Duration age, final PathCondition[] nestedConditions) {
        this.age = Objects.requireNonNull(age, "age");
        this.nestedConditions = PathCondition.copy(nestedConditions);
    }
    
    public Duration getAge() {
        return this.age;
    }
    
    public List<PathCondition> getNestedConditions() {
        return Collections.unmodifiableList((List<? extends PathCondition>)Arrays.asList((T[])this.nestedConditions));
    }
    
    @Override
    public boolean accept(final Path basePath, final Path relativePath, final BasicFileAttributes attrs) {
        final FileTime fileTime = attrs.lastModifiedTime();
        final long millis = fileTime.toMillis();
        final long ageMillis = IfLastModified.CLOCK.currentTimeMillis() - millis;
        final boolean result = ageMillis >= this.age.toMillis();
        final String match = result ? ">=" : "<";
        final String accept = result ? "ACCEPTED" : "REJECTED";
        IfLastModified.LOGGER.trace("IfLastModified {}: {} ageMillis '{}' {} '{}'", accept, relativePath, ageMillis, match, this.age);
        if (result) {
            return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs);
        }
        return result;
    }
    
    @Override
    public void beforeFileTreeWalk() {
        IfAll.beforeFileTreeWalk(this.nestedConditions);
    }
    
    @PluginFactory
    public static IfLastModified createAgeCondition(@PluginAttribute("age") final Duration age, @PluginElement("PathConditions") final PathCondition... nestedConditions) {
        return new IfLastModified(age, nestedConditions);
    }
    
    @Override
    public String toString() {
        final String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString(this.nestedConditions));
        return "IfLastModified(age=" + this.age + nested + ")";
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        CLOCK = ClockFactory.getClock();
    }
}

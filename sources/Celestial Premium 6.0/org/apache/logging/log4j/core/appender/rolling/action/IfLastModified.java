/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.action.Duration;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="IfLastModified", category="Core", printObject=true)
public final class IfLastModified
implements PathCondition {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final Clock CLOCK = ClockFactory.getClock();
    private final Duration age;
    private final PathCondition[] nestedConditions;

    private IfLastModified(Duration age, PathCondition[] nestedConditions) {
        this.age = Objects.requireNonNull(age, "age");
        this.nestedConditions = nestedConditions == null ? new PathCondition[0] : Arrays.copyOf(nestedConditions, nestedConditions.length);
    }

    public Duration getAge() {
        return this.age;
    }

    public List<PathCondition> getNestedConditions() {
        return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
    }

    @Override
    public boolean accept(Path basePath, Path relativePath, BasicFileAttributes attrs) {
        FileTime fileTime = attrs.lastModifiedTime();
        long millis = fileTime.toMillis();
        long ageMillis = CLOCK.currentTimeMillis() - millis;
        boolean result = ageMillis >= this.age.toMillis();
        String match = result ? ">=" : "<";
        String accept = result ? "ACCEPTED" : "REJECTED";
        LOGGER.trace("IfLastModified {}: {} ageMillis '{}' {} '{}'", accept, relativePath, ageMillis, match, this.age);
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
    public static IfLastModified createAgeCondition(@PluginAttribute(value="age") Duration age, PathCondition ... nestedConditions) {
        return new IfLastModified(age, nestedConditions);
    }

    public String toString() {
        String nested = this.nestedConditions.length == 0 ? "" : " AND " + Arrays.toString(this.nestedConditions);
        return "IfLastModified(age=" + this.age + nested + ")";
    }
}


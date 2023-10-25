/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name="IfAny", category="Core", printObject=true)
public final class IfAny
implements PathCondition {
    private final PathCondition[] components;

    private IfAny(PathCondition ... filters) {
        this.components = Objects.requireNonNull(filters, "filters");
    }

    public PathCondition[] getDeleteFilters() {
        return this.components;
    }

    @Override
    public boolean accept(Path baseDir, Path relativePath, BasicFileAttributes attrs) {
        for (PathCondition component : this.components) {
            if (!component.accept(baseDir, relativePath, attrs)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void beforeFileTreeWalk() {
        for (PathCondition condition : this.components) {
            condition.beforeFileTreeWalk();
        }
    }

    @PluginFactory
    public static IfAny createOrCondition(PathCondition ... components) {
        return new IfAny(components);
    }

    public String toString() {
        return "IfAny" + Arrays.toString(this.components);
    }
}


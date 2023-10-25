/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
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

@Plugin(name="IfFileName", category="Core", printObject=true)
public final class IfFileName
implements PathCondition {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final PathMatcher pathMatcher;
    private final String syntaxAndPattern;
    private final PathCondition[] nestedConditions;

    private IfFileName(String glob, String regex, PathCondition[] nestedConditions) {
        if (regex == null && glob == null) {
            throw new IllegalArgumentException("Specify either a path glob or a regular expression. Both cannot be null.");
        }
        this.syntaxAndPattern = IfFileName.createSyntaxAndPatternString(glob, regex);
        this.pathMatcher = FileSystems.getDefault().getPathMatcher(this.syntaxAndPattern);
        this.nestedConditions = nestedConditions == null ? new PathCondition[0] : Arrays.copyOf(nestedConditions, nestedConditions.length);
    }

    static String createSyntaxAndPatternString(String glob, String regex) {
        if (glob != null) {
            return glob.startsWith("glob:") ? glob : "glob:" + glob;
        }
        return regex.startsWith("regex:") ? regex : "regex:" + regex;
    }

    public String getSyntaxAndPattern() {
        return this.syntaxAndPattern;
    }

    public List<PathCondition> getNestedConditions() {
        return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
    }

    @Override
    public boolean accept(Path basePath, Path relativePath, BasicFileAttributes attrs) {
        boolean result = this.pathMatcher.matches(relativePath);
        String match = result ? "matches" : "does not match";
        String accept = result ? "ACCEPTED" : "REJECTED";
        LOGGER.trace("IfFileName {}: '{}' {} relative path '{}'", accept, this.syntaxAndPattern, match, relativePath);
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
    public static IfFileName createNameCondition(@PluginAttribute(value="glob") String glob, @PluginAttribute(value="regex") String regex, PathCondition ... nestedConditions) {
        return new IfFileName(glob, regex, nestedConditions);
    }

    public String toString() {
        String nested = this.nestedConditions.length == 0 ? "" : " AND " + Arrays.toString(this.nestedConditions);
        return "IfFileName(" + this.syntaxAndPattern + nested + ")";
    }
}


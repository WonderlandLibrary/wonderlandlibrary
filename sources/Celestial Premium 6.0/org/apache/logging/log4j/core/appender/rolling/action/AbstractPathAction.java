/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

public abstract class AbstractPathAction
extends AbstractAction {
    private final String basePathString;
    private final Set<FileVisitOption> options;
    private final int maxDepth;
    private final List<PathCondition> pathConditions;
    private final StrSubstitutor subst;

    protected AbstractPathAction(String basePath, boolean followSymbolicLinks, int maxDepth, PathCondition[] pathFilters, StrSubstitutor subst) {
        this.basePathString = basePath;
        this.options = followSymbolicLinks ? EnumSet.of(FileVisitOption.FOLLOW_LINKS) : Collections.emptySet();
        this.maxDepth = maxDepth;
        this.pathConditions = Arrays.asList(Arrays.copyOf(pathFilters, pathFilters.length));
        this.subst = subst;
    }

    @Override
    public boolean execute() throws IOException {
        return this.execute(this.createFileVisitor(this.getBasePath(), this.pathConditions));
    }

    public boolean execute(FileVisitor<Path> visitor) throws IOException {
        long start = System.nanoTime();
        LOGGER.debug("Starting {}", this);
        Files.walkFileTree(this.getBasePath(), this.options, this.maxDepth, visitor);
        double duration = System.nanoTime() - start;
        LOGGER.debug("{} complete in {} seconds", this.getClass().getSimpleName(), duration / (double)TimeUnit.SECONDS.toNanos(1L));
        return true;
    }

    protected abstract FileVisitor<Path> createFileVisitor(Path var1, List<PathCondition> var2);

    public Path getBasePath() {
        return Paths.get(this.subst.replace(this.getBasePathString()), new String[0]);
    }

    public String getBasePathString() {
        return this.basePathString;
    }

    public StrSubstitutor getStrSubstitutor() {
        return this.subst;
    }

    public Set<FileVisitOption> getOptions() {
        return Collections.unmodifiableSet(this.options);
    }

    public boolean isFollowSymbolicLinks() {
        return this.options.contains((Object)FileVisitOption.FOLLOW_LINKS);
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }

    public List<PathCondition> getPathConditions() {
        return Collections.unmodifiableList(this.pathConditions);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "[basePath=" + this.getBasePath() + ", options=" + this.options + ", maxDepth=" + this.maxDepth + ", conditions=" + this.pathConditions + "]";
    }
}


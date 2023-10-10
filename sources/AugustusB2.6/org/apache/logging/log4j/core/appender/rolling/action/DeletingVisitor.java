// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import org.apache.logging.log4j.status.StatusLogger;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Objects;
import java.util.List;
import org.apache.logging.log4j.Logger;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

public class DeletingVisitor extends SimpleFileVisitor<Path>
{
    private static final Logger LOGGER;
    private final Path basePath;
    private final boolean testMode;
    private final List<? extends PathCondition> pathConditions;
    
    public DeletingVisitor(final Path basePath, final List<? extends PathCondition> pathConditions, final boolean testMode) {
        this.testMode = testMode;
        this.basePath = Objects.requireNonNull(basePath, "basePath");
        this.pathConditions = Objects.requireNonNull(pathConditions, "pathConditions");
        for (final PathCondition condition : pathConditions) {
            condition.beforeFileTreeWalk();
        }
    }
    
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        for (final PathCondition pathFilter : this.pathConditions) {
            final Path relative = this.basePath.relativize(file);
            if (!pathFilter.accept(this.basePath, relative, attrs)) {
                DeletingVisitor.LOGGER.trace("Not deleting base={}, relative={}", this.basePath, relative);
                return FileVisitResult.CONTINUE;
            }
        }
        if (this.isTestMode()) {
            DeletingVisitor.LOGGER.info("Deleting {} (TEST MODE: file not actually deleted)", file);
        }
        else {
            this.delete(file);
        }
        return FileVisitResult.CONTINUE;
    }
    
    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException ioException) throws IOException {
        if (ioException instanceof NoSuchFileException) {
            DeletingVisitor.LOGGER.info("File {} could not be accessed, it has likely already been deleted", file, ioException);
            return FileVisitResult.CONTINUE;
        }
        return super.visitFileFailed(file, ioException);
    }
    
    protected void delete(final Path file) throws IOException {
        DeletingVisitor.LOGGER.trace("Deleting {}", file);
        Files.deleteIfExists(file);
    }
    
    public boolean isTestMode() {
        return this.testMode;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}

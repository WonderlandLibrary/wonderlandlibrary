// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.Comparator;
import java.util.Collections;
import java.nio.file.NoSuchFileException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

public class SortingVisitor extends SimpleFileVisitor<Path>
{
    private static final Logger LOGGER;
    private final PathSorter sorter;
    private final List<PathWithAttributes> collected;
    
    public SortingVisitor(final PathSorter sorter) {
        this.collected = new ArrayList<PathWithAttributes>();
        this.sorter = Objects.requireNonNull(sorter, "sorter");
    }
    
    @Override
    public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException {
        this.collected.add(new PathWithAttributes(path, attrs));
        return FileVisitResult.CONTINUE;
    }
    
    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException ioException) throws IOException {
        if (ioException instanceof NoSuchFileException) {
            SortingVisitor.LOGGER.info("File {} could not be accessed, it has likely already been deleted", file, ioException);
            return FileVisitResult.CONTINUE;
        }
        return super.visitFileFailed(file, ioException);
    }
    
    public List<PathWithAttributes> getSortedPaths() {
        Collections.sort(this.collected, this.sorter);
        return this.collected;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}

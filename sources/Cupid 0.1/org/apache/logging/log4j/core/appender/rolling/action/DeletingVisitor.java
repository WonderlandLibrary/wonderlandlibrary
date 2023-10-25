package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class DeletingVisitor extends SimpleFileVisitor<Path> {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final Path basePath;
  
  private final boolean testMode;
  
  private final List<? extends PathCondition> pathConditions;
  
  public DeletingVisitor(Path basePath, List<? extends PathCondition> pathConditions, boolean testMode) {
    this.testMode = testMode;
    this.basePath = Objects.<Path>requireNonNull(basePath, "basePath");
    this.pathConditions = Objects.<List<? extends PathCondition>>requireNonNull(pathConditions, "pathConditions");
    for (PathCondition condition : pathConditions)
      condition.beforeFileTreeWalk(); 
  }
  
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    for (PathCondition pathFilter : this.pathConditions) {
      Path relative = this.basePath.relativize(file);
      if (!pathFilter.accept(this.basePath, relative, attrs)) {
        LOGGER.trace("Not deleting base={}, relative={}", this.basePath, relative);
        return FileVisitResult.CONTINUE;
      } 
    } 
    if (isTestMode()) {
      LOGGER.info("Deleting {} (TEST MODE: file not actually deleted)", file);
    } else {
      delete(file);
    } 
    return FileVisitResult.CONTINUE;
  }
  
  public FileVisitResult visitFileFailed(Path file, IOException ioException) throws IOException {
    if (ioException instanceof java.nio.file.NoSuchFileException) {
      LOGGER.info("File {} could not be accessed, it has likely already been deleted", file, ioException);
      return FileVisitResult.CONTINUE;
    } 
    return super.visitFileFailed(file, ioException);
  }
  
  protected void delete(Path file) throws IOException {
    LOGGER.trace("Deleting {}", file);
    Files.deleteIfExists(file);
  }
  
  public boolean isTestMode() {
    return this.testMode;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\DeletingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
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
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

public abstract class AbstractPathAction extends AbstractAction {
  private final String basePathString;
  
  private final Set<FileVisitOption> options;
  
  private final int maxDepth;
  
  private final List<PathCondition> pathConditions;
  
  private final StrSubstitutor subst;
  
  protected AbstractPathAction(String basePath, boolean followSymbolicLinks, int maxDepth, PathCondition[] pathFilters, StrSubstitutor subst) {
    this.basePathString = basePath;
    this
      .options = followSymbolicLinks ? EnumSet.<FileVisitOption>of(FileVisitOption.FOLLOW_LINKS) : Collections.<FileVisitOption>emptySet();
    this.maxDepth = maxDepth;
    this.pathConditions = Arrays.asList(Arrays.copyOf(pathFilters, pathFilters.length));
    this.subst = subst;
  }
  
  public boolean execute() throws IOException {
    return execute(createFileVisitor(getBasePath(), this.pathConditions));
  }
  
  public boolean execute(FileVisitor<Path> visitor) throws IOException {
    long start = System.nanoTime();
    LOGGER.debug("Starting {}", this);
    Files.walkFileTree(getBasePath(), this.options, this.maxDepth, visitor);
    double duration = (System.nanoTime() - start);
    LOGGER.debug("{} complete in {} seconds", getClass().getSimpleName(), Double.valueOf(duration / TimeUnit.SECONDS.toNanos(1L)));
    return true;
  }
  
  protected abstract FileVisitor<Path> createFileVisitor(Path paramPath, List<PathCondition> paramList);
  
  public Path getBasePath() {
    return Paths.get(this.subst.replace(getBasePathString()), new String[0]);
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
    return this.options.contains(FileVisitOption.FOLLOW_LINKS);
  }
  
  public int getMaxDepth() {
    return this.maxDepth;
  }
  
  public List<PathCondition> getPathConditions() {
    return Collections.unmodifiableList(this.pathConditions);
  }
  
  public String toString() {
    return getClass().getSimpleName() + "[basePath=" + getBasePath() + ", options=" + this.options + ", maxDepth=" + this.maxDepth + ", conditions=" + this.pathConditions + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\AbstractPathAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

@Plugin(name = "Delete", category = "Core", printObject = true)
public class DeleteAction extends AbstractPathAction {
  private final PathSorter pathSorter;
  
  private final boolean testMode;
  
  private final ScriptCondition scriptCondition;
  
  DeleteAction(String basePath, boolean followSymbolicLinks, int maxDepth, boolean testMode, PathSorter sorter, PathCondition[] pathConditions, ScriptCondition scriptCondition, StrSubstitutor subst) {
    super(basePath, followSymbolicLinks, maxDepth, pathConditions, subst);
    this.testMode = testMode;
    this.pathSorter = Objects.<PathSorter>requireNonNull(sorter, "sorter");
    this.scriptCondition = scriptCondition;
    if (scriptCondition == null && (pathConditions == null || pathConditions.length == 0)) {
      LOGGER.error("Missing Delete conditions: unconditional Delete not supported");
      throw new IllegalArgumentException("Unconditional Delete not supported");
    } 
  }
  
  public boolean execute() throws IOException {
    return (this.scriptCondition != null) ? executeScript() : super.execute();
  }
  
  private boolean executeScript() throws IOException {
    List<PathWithAttributes> selectedForDeletion = callScript();
    if (selectedForDeletion == null) {
      LOGGER.trace("Script returned null list (no files to delete)");
      return true;
    } 
    deleteSelectedFiles(selectedForDeletion);
    return true;
  }
  
  private List<PathWithAttributes> callScript() throws IOException {
    List<PathWithAttributes> sortedPaths = getSortedPaths();
    trace("Sorted paths:", sortedPaths);
    List<PathWithAttributes> result = this.scriptCondition.selectFilesToDelete(getBasePath(), sortedPaths);
    return result;
  }
  
  private void deleteSelectedFiles(List<PathWithAttributes> selectedForDeletion) throws IOException {
    trace("Paths the script selected for deletion:", selectedForDeletion);
    for (PathWithAttributes pathWithAttributes : selectedForDeletion) {
      Path path = (pathWithAttributes == null) ? null : pathWithAttributes.getPath();
      if (isTestMode()) {
        LOGGER.info("Deleting {} (TEST MODE: file not actually deleted)", path);
        continue;
      } 
      delete(path);
    } 
  }
  
  protected void delete(Path path) throws IOException {
    LOGGER.trace("Deleting {}", path);
    Files.deleteIfExists(path);
  }
  
  public boolean execute(FileVisitor<Path> visitor) throws IOException {
    List<PathWithAttributes> sortedPaths = getSortedPaths();
    trace("Sorted paths:", sortedPaths);
    for (PathWithAttributes element : sortedPaths) {
      try {
        visitor.visitFile(element.getPath(), element.getAttributes());
      } catch (IOException ioex) {
        LOGGER.error("Error in post-rollover Delete when visiting {}", element.getPath(), ioex);
        visitor.visitFileFailed(element.getPath(), ioex);
      } 
    } 
    return true;
  }
  
  private void trace(String label, List<PathWithAttributes> sortedPaths) {
    LOGGER.trace(label);
    for (PathWithAttributes pathWithAttributes : sortedPaths)
      LOGGER.trace(pathWithAttributes); 
  }
  
  List<PathWithAttributes> getSortedPaths() throws IOException {
    SortingVisitor sort = new SortingVisitor(this.pathSorter);
    super.execute(sort);
    List<PathWithAttributes> sortedPaths = sort.getSortedPaths();
    return sortedPaths;
  }
  
  public boolean isTestMode() {
    return this.testMode;
  }
  
  protected FileVisitor<Path> createFileVisitor(Path visitorBaseDir, List<PathCondition> conditions) {
    return new DeletingVisitor(visitorBaseDir, conditions, this.testMode);
  }
  
  @PluginFactory
  public static DeleteAction createDeleteAction(@PluginAttribute("basePath") String basePath, @PluginAttribute("followLinks") boolean followLinks, @PluginAttribute(value = "maxDepth", defaultInt = 1) int maxDepth, @PluginAttribute("testMode") boolean testMode, @PluginElement("PathSorter") PathSorter sorterParameter, @PluginElement("PathConditions") PathCondition[] pathConditions, @PluginElement("ScriptCondition") ScriptCondition scriptCondition, @PluginConfiguration Configuration config) {
    PathSorter sorter = (sorterParameter == null) ? new PathSortByModificationTime(true) : sorterParameter;
    return new DeleteAction(basePath, followLinks, maxDepth, testMode, sorter, pathConditions, scriptCondition, config
        .getStrSubstitutor());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\DeleteAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
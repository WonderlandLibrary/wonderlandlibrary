// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.util.Objects;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Delete", category = "Core", printObject = true)
public class DeleteAction extends AbstractPathAction
{
    private final PathSorter pathSorter;
    private final boolean testMode;
    private final ScriptCondition scriptCondition;
    
    DeleteAction(final String basePath, final boolean followSymbolicLinks, final int maxDepth, final boolean testMode, final PathSorter sorter, final PathCondition[] pathConditions, final ScriptCondition scriptCondition, final StrSubstitutor subst) {
        super(basePath, followSymbolicLinks, maxDepth, pathConditions, subst);
        this.testMode = testMode;
        this.pathSorter = Objects.requireNonNull(sorter, "sorter");
        this.scriptCondition = scriptCondition;
        if (scriptCondition == null && (pathConditions == null || pathConditions.length == 0)) {
            DeleteAction.LOGGER.error("Missing Delete conditions: unconditional Delete not supported");
            throw new IllegalArgumentException("Unconditional Delete not supported");
        }
    }
    
    @Override
    public boolean execute() throws IOException {
        return (this.scriptCondition != null) ? this.executeScript() : super.execute();
    }
    
    private boolean executeScript() throws IOException {
        final List<PathWithAttributes> selectedForDeletion = this.callScript();
        if (selectedForDeletion == null) {
            DeleteAction.LOGGER.trace("Script returned null list (no files to delete)");
            return true;
        }
        this.deleteSelectedFiles(selectedForDeletion);
        return true;
    }
    
    private List<PathWithAttributes> callScript() throws IOException {
        final List<PathWithAttributes> sortedPaths = this.getSortedPaths();
        this.trace("Sorted paths:", sortedPaths);
        final List<PathWithAttributes> result = this.scriptCondition.selectFilesToDelete(this.getBasePath(), sortedPaths);
        return result;
    }
    
    private void deleteSelectedFiles(final List<PathWithAttributes> selectedForDeletion) throws IOException {
        this.trace("Paths the script selected for deletion:", selectedForDeletion);
        for (final PathWithAttributes pathWithAttributes : selectedForDeletion) {
            final Path path = (pathWithAttributes == null) ? null : pathWithAttributes.getPath();
            if (this.isTestMode()) {
                DeleteAction.LOGGER.info("Deleting {} (TEST MODE: file not actually deleted)", path);
            }
            else {
                this.delete(path);
            }
        }
    }
    
    protected void delete(final Path path) throws IOException {
        DeleteAction.LOGGER.trace("Deleting {}", path);
        Files.deleteIfExists(path);
    }
    
    @Override
    public boolean execute(final FileVisitor<Path> visitor) throws IOException {
        final List<PathWithAttributes> sortedPaths = this.getSortedPaths();
        this.trace("Sorted paths:", sortedPaths);
        for (final PathWithAttributes element : sortedPaths) {
            try {
                visitor.visitFile(element.getPath(), element.getAttributes());
            }
            catch (IOException ioex) {
                DeleteAction.LOGGER.error("Error in post-rollover Delete when visiting {}", element.getPath(), ioex);
                visitor.visitFileFailed(element.getPath(), ioex);
            }
        }
        return true;
    }
    
    private void trace(final String label, final List<PathWithAttributes> sortedPaths) {
        DeleteAction.LOGGER.trace(label);
        for (final PathWithAttributes pathWithAttributes : sortedPaths) {
            DeleteAction.LOGGER.trace(pathWithAttributes);
        }
    }
    
    List<PathWithAttributes> getSortedPaths() throws IOException {
        final SortingVisitor sort = new SortingVisitor(this.pathSorter);
        super.execute(sort);
        final List<PathWithAttributes> sortedPaths = sort.getSortedPaths();
        return sortedPaths;
    }
    
    public boolean isTestMode() {
        return this.testMode;
    }
    
    @Override
    protected FileVisitor<Path> createFileVisitor(final Path visitorBaseDir, final List<PathCondition> conditions) {
        return new DeletingVisitor(visitorBaseDir, conditions, this.testMode);
    }
    
    @PluginFactory
    public static DeleteAction createDeleteAction(@PluginAttribute("basePath") final String basePath, @PluginAttribute("followLinks") final boolean followLinks, @PluginAttribute(value = "maxDepth", defaultInt = 1) final int maxDepth, @PluginAttribute("testMode") final boolean testMode, @PluginElement("PathSorter") final PathSorter sorterParameter, @PluginElement("PathConditions") final PathCondition[] pathConditions, @PluginElement("ScriptCondition") final ScriptCondition scriptCondition, @PluginConfiguration final Configuration config) {
        final PathSorter sorter = (sorterParameter == null) ? new PathSortByModificationTime(true) : sorterParameter;
        return new DeleteAction(basePath, followLinks, maxDepth, testMode, sorter, pathConditions, scriptCondition, config.getStrSubstitutor());
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.apache.logging.log4j.core.appender.rolling.action.PosixViewAttributeAction;
import org.apache.logging.log4j.core.appender.rolling.action.CompositeAction;
import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.SortedMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DirectWriteRolloverStrategy", category = "Core", printObject = true)
public class DirectWriteRolloverStrategy extends AbstractRolloverStrategy implements DirectFileRolloverStrategy
{
    private static final int DEFAULT_MAX_FILES = 7;
    private final int maxFiles;
    private final int compressionLevel;
    private final List<Action> customActions;
    private final boolean stopCustomActionsOnError;
    private volatile String currentFileName;
    private int nextIndex;
    private final PatternProcessor tempCompressedFilePattern;
    private volatile boolean usePrevTime;
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    @Deprecated
    @PluginFactory
    public static DirectWriteRolloverStrategy createStrategy(@PluginAttribute("maxFiles") final String maxFiles, @PluginAttribute("compressionLevel") final String compressionLevelStr, @PluginElement("Actions") final Action[] customActions, @PluginAttribute(value = "stopCustomActionsOnError", defaultBoolean = true) final boolean stopCustomActionsOnError, @PluginConfiguration final Configuration config) {
        return newBuilder().withMaxFiles(maxFiles).withCompressionLevelStr(compressionLevelStr).withCustomActions(customActions).withStopCustomActionsOnError(stopCustomActionsOnError).withConfig(config).build();
    }
    
    @Deprecated
    protected DirectWriteRolloverStrategy(final int maxFiles, final int compressionLevel, final StrSubstitutor strSubstitutor, final Action[] customActions, final boolean stopCustomActionsOnError) {
        this(maxFiles, compressionLevel, strSubstitutor, customActions, stopCustomActionsOnError, null);
    }
    
    protected DirectWriteRolloverStrategy(final int maxFiles, final int compressionLevel, final StrSubstitutor strSubstitutor, final Action[] customActions, final boolean stopCustomActionsOnError, final String tempCompressedFilePatternString) {
        super(strSubstitutor);
        this.nextIndex = -1;
        this.usePrevTime = false;
        this.maxFiles = maxFiles;
        this.compressionLevel = compressionLevel;
        this.stopCustomActionsOnError = stopCustomActionsOnError;
        this.customActions = ((customActions == null) ? Collections.emptyList() : Arrays.asList(customActions));
        this.tempCompressedFilePattern = ((tempCompressedFilePatternString != null) ? new PatternProcessor(tempCompressedFilePatternString) : null);
    }
    
    public int getCompressionLevel() {
        return this.compressionLevel;
    }
    
    public List<Action> getCustomActions() {
        return this.customActions;
    }
    
    public int getMaxFiles() {
        return this.maxFiles;
    }
    
    public boolean isStopCustomActionsOnError() {
        return this.stopCustomActionsOnError;
    }
    
    public PatternProcessor getTempCompressedFilePattern() {
        return this.tempCompressedFilePattern;
    }
    
    private int purge(final RollingFileManager manager) {
        final SortedMap<Integer, Path> eligibleFiles = this.getEligibleFiles(manager);
        DirectWriteRolloverStrategy.LOGGER.debug("Found {} eligible files, max is  {}", (Object)eligibleFiles.size(), this.maxFiles);
        while (eligibleFiles.size() >= this.maxFiles) {
            try {
                final Integer key = eligibleFiles.firstKey();
                Files.delete(eligibleFiles.get(key));
                eligibleFiles.remove(key);
                continue;
            }
            catch (IOException ioe) {
                DirectWriteRolloverStrategy.LOGGER.error("Unable to delete {}", eligibleFiles.firstKey(), ioe);
            }
            break;
        }
        return (eligibleFiles.size() > 0) ? eligibleFiles.lastKey() : 1;
    }
    
    @Override
    public String getCurrentFileName(final RollingFileManager manager) {
        if (this.currentFileName == null) {
            final SortedMap<Integer, Path> eligibleFiles = this.getEligibleFiles(manager);
            final int fileIndex = (eligibleFiles.size() > 0) ? ((this.nextIndex > 0) ? this.nextIndex : eligibleFiles.size()) : 1;
            final StringBuilder buf = new StringBuilder(255);
            manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, true, fileIndex);
            final int suffixLength = this.suffixLength(buf.toString());
            final String name = (suffixLength > 0) ? buf.substring(0, buf.length() - suffixLength) : buf.toString();
            this.currentFileName = name;
        }
        return this.currentFileName;
    }
    
    @Override
    public void clearCurrentFileName() {
        this.currentFileName = null;
    }
    
    @Override
    public RolloverDescription rollover(final RollingFileManager manager) throws SecurityException {
        DirectWriteRolloverStrategy.LOGGER.debug("Rolling " + this.currentFileName);
        if (this.maxFiles < 0) {
            return null;
        }
        final long startNanos = System.nanoTime();
        final int fileIndex = this.purge(manager);
        if (DirectWriteRolloverStrategy.LOGGER.isTraceEnabled()) {
            final double durationMillis = (double)TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
            DirectWriteRolloverStrategy.LOGGER.trace("DirectWriteRolloverStrategy.purge() took {} milliseconds", (Object)durationMillis);
        }
        Action compressAction = null;
        String compressedName;
        final String sourceName = compressedName = this.getCurrentFileName(manager);
        this.currentFileName = null;
        this.nextIndex = fileIndex + 1;
        final FileExtension fileExtension = manager.getFileExtension();
        if (fileExtension != null) {
            compressedName += fileExtension.getExtension();
            if (this.tempCompressedFilePattern != null) {
                final StringBuilder buf = new StringBuilder();
                this.tempCompressedFilePattern.formatFileName(this.strSubstitutor, buf, fileIndex);
                final String tmpCompressedName = buf.toString();
                final File tmpCompressedNameFile = new File(tmpCompressedName);
                final File parentFile = tmpCompressedNameFile.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                compressAction = new CompositeAction(Arrays.asList(fileExtension.createCompressAction(sourceName, tmpCompressedName, true, this.compressionLevel), new FileRenameAction(tmpCompressedNameFile, new File(compressedName), true)), true);
            }
            else {
                compressAction = fileExtension.createCompressAction(sourceName, compressedName, true, this.compressionLevel);
            }
        }
        if (compressAction != null && manager.isAttributeViewEnabled()) {
            final Action posixAttributeViewAction = PosixViewAttributeAction.newBuilder().withBasePath(compressedName).withFollowLinks(false).withMaxDepth(1).withPathConditions(PathCondition.EMPTY_ARRAY).withSubst(this.getStrSubstitutor()).withFilePermissions(manager.getFilePermissions()).withFileOwner(manager.getFileOwner()).withFileGroup(manager.getFileGroup()).build();
            compressAction = new CompositeAction(Arrays.asList(compressAction, posixAttributeViewAction), false);
        }
        final Action asyncAction = this.merge(compressAction, this.customActions, this.stopCustomActionsOnError);
        return new RolloverDescriptionImpl(sourceName, false, null, asyncAction);
    }
    
    @Override
    public String toString() {
        return "DirectWriteRolloverStrategy(maxFiles=" + this.maxFiles + ')';
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<DirectWriteRolloverStrategy>
    {
        @PluginBuilderAttribute("maxFiles")
        private String maxFiles;
        @PluginBuilderAttribute("compressionLevel")
        private String compressionLevelStr;
        @PluginElement("Actions")
        private Action[] customActions;
        @PluginBuilderAttribute("stopCustomActionsOnError")
        private boolean stopCustomActionsOnError;
        @PluginBuilderAttribute("tempCompressedFilePattern")
        private String tempCompressedFilePattern;
        @PluginConfiguration
        private Configuration config;
        
        public Builder() {
            this.stopCustomActionsOnError = true;
        }
        
        @Override
        public DirectWriteRolloverStrategy build() {
            int maxIndex = Integer.MAX_VALUE;
            if (this.maxFiles != null) {
                maxIndex = Integer.parseInt(this.maxFiles);
                if (maxIndex < 0) {
                    maxIndex = Integer.MAX_VALUE;
                }
                else if (maxIndex < 2) {
                    AbstractRolloverStrategy.LOGGER.error("Maximum files too small. Limited to 7");
                    maxIndex = 7;
                }
            }
            final int compressionLevel = Integers.parseInt(this.compressionLevelStr, -1);
            return new DirectWriteRolloverStrategy(maxIndex, compressionLevel, this.config.getStrSubstitutor(), this.customActions, this.stopCustomActionsOnError, this.tempCompressedFilePattern);
        }
        
        public String getMaxFiles() {
            return this.maxFiles;
        }
        
        public Builder withMaxFiles(final String maxFiles) {
            this.maxFiles = maxFiles;
            return this;
        }
        
        public String getCompressionLevelStr() {
            return this.compressionLevelStr;
        }
        
        public Builder withCompressionLevelStr(final String compressionLevelStr) {
            this.compressionLevelStr = compressionLevelStr;
            return this;
        }
        
        public Action[] getCustomActions() {
            return this.customActions;
        }
        
        public Builder withCustomActions(final Action[] customActions) {
            this.customActions = customActions;
            return this;
        }
        
        public boolean isStopCustomActionsOnError() {
            return this.stopCustomActionsOnError;
        }
        
        public Builder withStopCustomActionsOnError(final boolean stopCustomActionsOnError) {
            this.stopCustomActionsOnError = stopCustomActionsOnError;
            return this;
        }
        
        public String getTempCompressedFilePattern() {
            return this.tempCompressedFilePattern;
        }
        
        public Builder withTempCompressedFilePattern(final String tempCompressedFilePattern) {
            this.tempCompressedFilePattern = tempCompressedFilePattern;
            return this;
        }
        
        public Configuration getConfig() {
            return this.config;
        }
        
        public Builder withConfig(final Configuration config) {
            this.config = config;
            return this;
        }
    }
}

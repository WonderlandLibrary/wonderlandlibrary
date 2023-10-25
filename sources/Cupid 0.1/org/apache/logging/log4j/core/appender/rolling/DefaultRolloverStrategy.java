package org.apache.logging.log4j.core.appender.rolling;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.appender.rolling.action.CompositeAction;
import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.apache.logging.log4j.core.appender.rolling.action.PosixViewAttributeAction;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.util.Integers;

@Plugin(name = "DefaultRolloverStrategy", category = "Core", printObject = true)
public class DefaultRolloverStrategy extends AbstractRolloverStrategy {
  private static final int MIN_WINDOW_SIZE = 1;
  
  private static final int DEFAULT_WINDOW_SIZE = 7;
  
  private final int maxIndex;
  
  private final int minIndex;
  
  private final boolean useMax;
  
  private final int compressionLevel;
  
  private final List<Action> customActions;
  
  private final boolean stopCustomActionsOnError;
  
  private final PatternProcessor tempCompressedFilePattern;
  
  public static class Builder implements org.apache.logging.log4j.core.util.Builder<DefaultRolloverStrategy> {
    @PluginBuilderAttribute("max")
    private String max;
    
    @PluginBuilderAttribute("min")
    private String min;
    
    @PluginBuilderAttribute("fileIndex")
    private String fileIndex;
    
    @PluginBuilderAttribute("compressionLevel")
    private String compressionLevelStr;
    
    @PluginElement("Actions")
    private Action[] customActions;
    
    @PluginBuilderAttribute("stopCustomActionsOnError")
    private boolean stopCustomActionsOnError = true;
    
    @PluginBuilderAttribute("tempCompressedFilePattern")
    private String tempCompressedFilePattern;
    
    @PluginConfiguration
    private Configuration config;
    
    public DefaultRolloverStrategy build() {
      int minIndex, maxIndex;
      boolean useMax;
      if (this.fileIndex != null && this.fileIndex.equalsIgnoreCase("nomax")) {
        minIndex = Integer.MIN_VALUE;
        maxIndex = Integer.MAX_VALUE;
        useMax = false;
      } else {
        useMax = (this.fileIndex == null) ? true : this.fileIndex.equalsIgnoreCase("max");
        minIndex = 1;
        if (this.min != null) {
          minIndex = Integer.parseInt(this.min);
          if (minIndex < 1) {
            AbstractRolloverStrategy.LOGGER.error("Minimum window size too small. Limited to 1");
            minIndex = 1;
          } 
        } 
        maxIndex = 7;
        if (this.max != null) {
          maxIndex = Integer.parseInt(this.max);
          if (maxIndex < minIndex) {
            maxIndex = (minIndex < 7) ? 7 : minIndex;
            AbstractRolloverStrategy.LOGGER.error("Maximum window size must be greater than the minimum windows size. Set to " + maxIndex);
          } 
        } 
      } 
      int compressionLevel = Integers.parseInt(this.compressionLevelStr, -1);
      StrSubstitutor nonNullStrSubstitutor = (this.config != null) ? this.config.getStrSubstitutor() : new StrSubstitutor();
      return new DefaultRolloverStrategy(minIndex, maxIndex, useMax, compressionLevel, nonNullStrSubstitutor, this.customActions, this.stopCustomActionsOnError, this.tempCompressedFilePattern);
    }
    
    public String getMax() {
      return this.max;
    }
    
    public Builder withMax(String max) {
      this.max = max;
      return this;
    }
    
    public String getMin() {
      return this.min;
    }
    
    public Builder withMin(String min) {
      this.min = min;
      return this;
    }
    
    public String getFileIndex() {
      return this.fileIndex;
    }
    
    public Builder withFileIndex(String fileIndex) {
      this.fileIndex = fileIndex;
      return this;
    }
    
    public String getCompressionLevelStr() {
      return this.compressionLevelStr;
    }
    
    public Builder withCompressionLevelStr(String compressionLevelStr) {
      this.compressionLevelStr = compressionLevelStr;
      return this;
    }
    
    public Action[] getCustomActions() {
      return this.customActions;
    }
    
    public Builder withCustomActions(Action[] customActions) {
      this.customActions = customActions;
      return this;
    }
    
    public boolean isStopCustomActionsOnError() {
      return this.stopCustomActionsOnError;
    }
    
    public Builder withStopCustomActionsOnError(boolean stopCustomActionsOnError) {
      this.stopCustomActionsOnError = stopCustomActionsOnError;
      return this;
    }
    
    public String getTempCompressedFilePattern() {
      return this.tempCompressedFilePattern;
    }
    
    public Builder withTempCompressedFilePattern(String tempCompressedFilePattern) {
      this.tempCompressedFilePattern = tempCompressedFilePattern;
      return this;
    }
    
    public Configuration getConfig() {
      return this.config;
    }
    
    public Builder withConfig(Configuration config) {
      this.config = config;
      return this;
    }
  }
  
  @PluginBuilderFactory
  public static Builder newBuilder() {
    return new Builder();
  }
  
  @PluginFactory
  @Deprecated
  public static DefaultRolloverStrategy createStrategy(@PluginAttribute("max") String max, @PluginAttribute("min") String min, @PluginAttribute("fileIndex") String fileIndex, @PluginAttribute("compressionLevel") String compressionLevelStr, @PluginElement("Actions") Action[] customActions, @PluginAttribute(value = "stopCustomActionsOnError", defaultBoolean = true) boolean stopCustomActionsOnError, @PluginConfiguration Configuration config) {
    return newBuilder()
      .withMin(min)
      .withMax(max)
      .withFileIndex(fileIndex)
      .withCompressionLevelStr(compressionLevelStr)
      .withCustomActions(customActions)
      .withStopCustomActionsOnError(stopCustomActionsOnError)
      .withConfig(config)
      .build();
  }
  
  @Deprecated
  protected DefaultRolloverStrategy(int minIndex, int maxIndex, boolean useMax, int compressionLevel, StrSubstitutor strSubstitutor, Action[] customActions, boolean stopCustomActionsOnError) {
    this(minIndex, maxIndex, useMax, compressionLevel, strSubstitutor, customActions, stopCustomActionsOnError, (String)null);
  }
  
  protected DefaultRolloverStrategy(int minIndex, int maxIndex, boolean useMax, int compressionLevel, StrSubstitutor strSubstitutor, Action[] customActions, boolean stopCustomActionsOnError, String tempCompressedFilePatternString) {
    super(strSubstitutor);
    this.minIndex = minIndex;
    this.maxIndex = maxIndex;
    this.useMax = useMax;
    this.compressionLevel = compressionLevel;
    this.stopCustomActionsOnError = stopCustomActionsOnError;
    this.customActions = (customActions == null) ? Collections.<Action>emptyList() : Arrays.<Action>asList(customActions);
    this.tempCompressedFilePattern = (tempCompressedFilePatternString != null) ? new PatternProcessor(tempCompressedFilePatternString) : null;
  }
  
  public int getCompressionLevel() {
    return this.compressionLevel;
  }
  
  public List<Action> getCustomActions() {
    return this.customActions;
  }
  
  public int getMaxIndex() {
    return this.maxIndex;
  }
  
  public int getMinIndex() {
    return this.minIndex;
  }
  
  public boolean isStopCustomActionsOnError() {
    return this.stopCustomActionsOnError;
  }
  
  public boolean isUseMax() {
    return this.useMax;
  }
  
  public PatternProcessor getTempCompressedFilePattern() {
    return this.tempCompressedFilePattern;
  }
  
  private int purge(int lowIndex, int highIndex, RollingFileManager manager) {
    return this.useMax ? purgeAscending(lowIndex, highIndex, manager) : purgeDescending(lowIndex, highIndex, manager);
  }
  
  private int purgeAscending(int lowIndex, int highIndex, RollingFileManager manager) {
    SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager);
    int maxFiles = highIndex - lowIndex + 1;
    boolean renameFiles = (!eligibleFiles.isEmpty() && ((Integer)eligibleFiles.lastKey()).intValue() >= this.maxIndex);
    while (eligibleFiles.size() >= maxFiles) {
      try {
        LOGGER.debug("Eligible files: {}", eligibleFiles);
        Integer key = eligibleFiles.firstKey();
        LOGGER.debug("Deleting {}", ((Path)eligibleFiles.get(key)).toFile().getAbsolutePath());
        Files.delete(eligibleFiles.get(key));
        eligibleFiles.remove(key);
        renameFiles = true;
      } catch (IOException ioe) {
        LOGGER.error("Unable to delete {}, {}", eligibleFiles.firstKey(), ioe.getMessage(), ioe);
        break;
      } 
    } 
    StringBuilder buf = new StringBuilder();
    if (renameFiles)
      for (Map.Entry<Integer, Path> entry : eligibleFiles.entrySet()) {
        buf.setLength(0);
        manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(((Integer)entry.getKey()).intValue() - 1));
        String currentName = ((Path)entry.getValue()).toFile().getName();
        String renameTo = buf.toString();
        int suffixLength = suffixLength(renameTo);
        if (suffixLength > 0 && suffixLength(currentName) == 0)
          renameTo = renameTo.substring(0, renameTo.length() - suffixLength); 
        FileRenameAction fileRenameAction = new FileRenameAction(((Path)entry.getValue()).toFile(), new File(renameTo), true);
        try {
          LOGGER.debug("DefaultRolloverStrategy.purgeAscending executing {}", fileRenameAction);
          if (!fileRenameAction.execute())
            return -1; 
        } catch (Exception ex) {
          LOGGER.warn("Exception during purge in RollingFileAppender", ex);
          return -1;
        } 
      }  
    return (eligibleFiles.size() > 0) ? (
      (((Integer)eligibleFiles.lastKey()).intValue() < highIndex) ? (((Integer)eligibleFiles.lastKey()).intValue() + 1) : highIndex) : lowIndex;
  }
  
  private int purgeDescending(int lowIndex, int highIndex, RollingFileManager manager) {
    SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager, false);
    int maxFiles = highIndex - lowIndex + 1;
    LOGGER.debug("Eligible files: {}", eligibleFiles);
    while (eligibleFiles.size() >= maxFiles) {
      try {
        Integer key = eligibleFiles.firstKey();
        Files.delete(eligibleFiles.get(key));
        eligibleFiles.remove(key);
      } catch (IOException ioe) {
        LOGGER.error("Unable to delete {}, {}", eligibleFiles.firstKey(), ioe.getMessage(), ioe);
        break;
      } 
    } 
    StringBuilder buf = new StringBuilder();
    for (Map.Entry<Integer, Path> entry : eligibleFiles.entrySet()) {
      buf.setLength(0);
      manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(((Integer)entry.getKey()).intValue() + 1));
      String currentName = ((Path)entry.getValue()).toFile().getName();
      String renameTo = buf.toString();
      int suffixLength = suffixLength(renameTo);
      if (suffixLength > 0 && suffixLength(currentName) == 0)
        renameTo = renameTo.substring(0, renameTo.length() - suffixLength); 
      FileRenameAction fileRenameAction = new FileRenameAction(((Path)entry.getValue()).toFile(), new File(renameTo), true);
      try {
        LOGGER.debug("DefaultRolloverStrategy.purgeDescending executing {}", fileRenameAction);
        if (!fileRenameAction.execute())
          return -1; 
      } catch (Exception ex) {
        LOGGER.warn("Exception during purge in RollingFileAppender", ex);
        return -1;
      } 
    } 
    return lowIndex;
  }
  
  public RolloverDescription rollover(RollingFileManager manager) throws SecurityException {
    int fileIndex;
    CompositeAction compositeAction;
    StringBuilder buf = new StringBuilder(255);
    if (this.minIndex == Integer.MIN_VALUE) {
      SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager);
      fileIndex = (eligibleFiles.size() > 0) ? (((Integer)eligibleFiles.lastKey()).intValue() + 1) : 1;
      manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(fileIndex));
    } else {
      if (this.maxIndex < 0)
        return null; 
      long startNanos = System.nanoTime();
      fileIndex = purge(this.minIndex, this.maxIndex, manager);
      if (fileIndex < 0)
        return null; 
      manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(fileIndex));
      if (LOGGER.isTraceEnabled()) {
        double durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
        LOGGER.trace("DefaultRolloverStrategy.purge() took {} milliseconds", Double.valueOf(durationMillis));
      } 
    } 
    String currentFileName = manager.getFileName();
    String renameTo = buf.toString();
    String compressedName = renameTo;
    Action compressAction = null;
    FileExtension fileExtension = manager.getFileExtension();
    if (fileExtension != null) {
      File renameToFile = new File(renameTo);
      renameTo = renameTo.substring(0, renameTo.length() - fileExtension.length());
      if (this.tempCompressedFilePattern != null) {
        buf.delete(0, buf.length());
        this.tempCompressedFilePattern.formatFileName(this.strSubstitutor, buf, Integer.valueOf(fileIndex));
        String tmpCompressedName = buf.toString();
        File tmpCompressedNameFile = new File(tmpCompressedName);
        File parentFile = tmpCompressedNameFile.getParentFile();
        if (parentFile != null)
          parentFile.mkdirs(); 
        compositeAction = new CompositeAction(Arrays.asList(new Action[] { fileExtension.createCompressAction(renameTo, tmpCompressedName, true, this.compressionLevel), (Action)new FileRenameAction(tmpCompressedNameFile, renameToFile, true) }), true);
      } else {
        compressAction = fileExtension.createCompressAction(renameTo, compressedName, true, this.compressionLevel);
      } 
    } 
    if (currentFileName.equals(renameTo)) {
      LOGGER.warn("Attempt to rename file {} to itself will be ignored", currentFileName);
      return new RolloverDescriptionImpl(currentFileName, false, null, null);
    } 
    if (compressAction != null && manager.isAttributeViewEnabled()) {
      PosixViewAttributeAction posixViewAttributeAction = PosixViewAttributeAction.newBuilder().withBasePath(compressedName).withFollowLinks(false).withMaxDepth(1).withPathConditions(PathCondition.EMPTY_ARRAY).withSubst(getStrSubstitutor()).withFilePermissions(manager.getFilePermissions()).withFileOwner(manager.getFileOwner()).withFileGroup(manager.getFileGroup()).build();
      compositeAction = new CompositeAction(Arrays.asList(new Action[] { compressAction, (Action)posixViewAttributeAction }, ), false);
    } 
    FileRenameAction renameAction = new FileRenameAction(new File(currentFileName), new File(renameTo), manager.isRenameEmptyFiles());
    Action asyncAction = merge((Action)compositeAction, this.customActions, this.stopCustomActionsOnError);
    return new RolloverDescriptionImpl(currentFileName, false, (Action)renameAction, asyncAction);
  }
  
  public String toString() {
    return "DefaultRolloverStrategy(min=" + this.minIndex + ", max=" + this.maxIndex + ", useMax=" + this.useMax + ")";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\DefaultRolloverStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
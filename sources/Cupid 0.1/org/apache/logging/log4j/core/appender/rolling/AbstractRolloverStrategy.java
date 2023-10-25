package org.apache.logging.log4j.core.appender.rolling;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.appender.rolling.action.CompositeAction;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.pattern.NotANumber;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractRolloverStrategy implements RolloverStrategy {
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  public static final Pattern PATTERN_COUNTER = Pattern.compile(".*%((?<ZEROPAD>0)?(?<PADDING>\\d+))?i.*");
  
  protected final StrSubstitutor strSubstitutor;
  
  protected AbstractRolloverStrategy(StrSubstitutor strSubstitutor) {
    this.strSubstitutor = strSubstitutor;
  }
  
  public StrSubstitutor getStrSubstitutor() {
    return this.strSubstitutor;
  }
  
  protected Action merge(Action compressAction, List<Action> custom, boolean stopOnError) {
    if (custom.isEmpty())
      return compressAction; 
    if (compressAction == null)
      return (Action)new CompositeAction(custom, stopOnError); 
    List<Action> all = new ArrayList<>();
    all.add(compressAction);
    all.addAll(custom);
    return (Action)new CompositeAction(all, stopOnError);
  }
  
  protected int suffixLength(String lowFilename) {
    for (FileExtension extension : FileExtension.values()) {
      if (extension.isExtensionFor(lowFilename))
        return extension.length(); 
    } 
    return 0;
  }
  
  protected SortedMap<Integer, Path> getEligibleFiles(RollingFileManager manager) {
    return getEligibleFiles(manager, true);
  }
  
  protected SortedMap<Integer, Path> getEligibleFiles(RollingFileManager manager, boolean isAscending) {
    StringBuilder buf = new StringBuilder();
    String pattern = manager.getPatternProcessor().getPattern();
    manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, NotANumber.NAN);
    String fileName = manager.isDirectWrite() ? "" : manager.getFileName();
    return getEligibleFiles(fileName, buf.toString(), pattern, isAscending);
  }
  
  protected SortedMap<Integer, Path> getEligibleFiles(String path, String pattern) {
    return getEligibleFiles("", path, pattern, true);
  }
  
  @Deprecated
  protected SortedMap<Integer, Path> getEligibleFiles(String path, String logfilePattern, boolean isAscending) {
    return getEligibleFiles("", path, logfilePattern, isAscending);
  }
  
  protected SortedMap<Integer, Path> getEligibleFiles(String currentFile, String path, String logfilePattern, boolean isAscending) {
    TreeMap<Integer, Path> eligibleFiles = new TreeMap<>();
    File file = new File(path);
    File parent = file.getParentFile();
    if (parent == null) {
      parent = new File(".");
    } else {
      parent.mkdirs();
    } 
    if (!PATTERN_COUNTER.matcher(logfilePattern).matches())
      return eligibleFiles; 
    Path dir = parent.toPath();
    String fileName = file.getName();
    int suffixLength = suffixLength(fileName);
    if (suffixLength > 0) {
      fileName = Pattern.quote(fileName.substring(0, fileName.length() - suffixLength)) + ".*";
    } else {
      fileName = Pattern.quote(fileName);
    } 
    String filePattern = fileName.replaceFirst("0?\\u0000", "\\\\E(0?\\\\d+)\\\\Q");
    Pattern pattern = Pattern.compile(filePattern);
    Path current = (currentFile.length() > 0) ? (new File(currentFile)).toPath() : null;
    LOGGER.debug("Current file: {}", currentFile);
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for (Path entry : stream) {
        Matcher matcher = pattern.matcher(entry.toFile().getName());
        if (matcher.matches() && !entry.equals(current))
          try {
            Integer index = Integer.valueOf(Integer.parseInt(matcher.group(1)));
            eligibleFiles.put(index, entry);
          } catch (NumberFormatException ex) {
            LOGGER.debug("Ignoring file {} which matches pattern but the index is invalid.", entry
                .toFile().getName());
          }  
      } 
    } catch (IOException ioe) {
      throw new LoggingException("Error reading folder " + dir + " " + ioe.getMessage(), ioe);
    } 
    return isAscending ? eligibleFiles : eligibleFiles.descendingMap();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\AbstractRolloverStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
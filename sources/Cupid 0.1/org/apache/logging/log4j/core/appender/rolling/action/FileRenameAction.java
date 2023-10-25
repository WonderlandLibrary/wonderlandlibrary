package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileRenameAction extends AbstractAction {
  private final File source;
  
  private final File destination;
  
  private final boolean renameEmptyFiles;
  
  public FileRenameAction(File src, File dst, boolean renameEmptyFiles) {
    this.source = src;
    this.destination = dst;
    this.renameEmptyFiles = renameEmptyFiles;
  }
  
  public boolean execute() {
    return execute(this.source, this.destination, this.renameEmptyFiles);
  }
  
  public File getDestination() {
    return this.destination;
  }
  
  public File getSource() {
    return this.source;
  }
  
  public boolean isRenameEmptyFiles() {
    return this.renameEmptyFiles;
  }
  
  public static boolean execute(File source, File destination, boolean renameEmptyFiles) {
    if (renameEmptyFiles || source.length() > 0L) {
      File parent = destination.getParentFile();
      if (parent != null && !parent.exists()) {
        parent.mkdirs();
        if (!parent.exists()) {
          LOGGER.error("Unable to create directory {}", parent.getAbsolutePath());
          return false;
        } 
      } 
      try {
        return moveFile(Paths.get(source.getAbsolutePath(), new String[0]), Paths.get(destination.getAbsolutePath(), new String[0]));
      } catch (IOException exMove) {
        LOGGER.debug("Unable to move file {} to {}: {} {} - will try to copy and delete", source
            .getAbsolutePath(), destination.getAbsolutePath(), exMove.getClass().getName(), exMove
            .getMessage());
        boolean result = source.renameTo(destination);
        if (!result) {
          try {
            Files.copy(Paths.get(source.getAbsolutePath(), new String[0]), Paths.get(destination.getAbsolutePath(), new String[0]), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
            try {
              Files.delete(Paths.get(source.getAbsolutePath(), new String[0]));
              result = true;
              LOGGER.trace("Renamed file {} to {} using copy and delete", source
                  .getAbsolutePath(), destination.getAbsolutePath());
            } catch (IOException exDelete) {
              LOGGER.error("Unable to delete file {}: {} {}", source.getAbsolutePath(), exDelete
                  .getClass().getName(), exDelete.getMessage());
              try {
                result = true;
                (new PrintWriter(source.getAbsolutePath())).close();
                LOGGER.trace("Renamed file {} to {} with copy and truncation", source
                    .getAbsolutePath(), destination.getAbsolutePath());
              } catch (IOException exOwerwrite) {
                LOGGER.error("Unable to overwrite file {}: {} {}", source
                    .getAbsolutePath(), exOwerwrite.getClass().getName(), exOwerwrite
                    .getMessage());
              } 
            } 
          } catch (IOException exCopy) {
            LOGGER.error("Unable to copy file {} to {}: {} {}", source.getAbsolutePath(), destination
                .getAbsolutePath(), exCopy.getClass().getName(), exCopy.getMessage());
          } 
        } else {
          LOGGER.trace("Renamed file {} to {} with source.renameTo", source
              .getAbsolutePath(), destination.getAbsolutePath());
        } 
        return result;
      } catch (RuntimeException ex) {
        LOGGER.error("Unable to rename file {} to {}: {} {}", source.getAbsolutePath(), destination
            .getAbsolutePath(), ex.getClass().getName(), ex.getMessage());
      } 
    } else {
      try {
        source.delete();
      } catch (Exception exDelete) {
        LOGGER.error("Unable to delete empty file {}: {} {}", source.getAbsolutePath(), exDelete
            .getClass().getName(), exDelete.getMessage());
      } 
    } 
    return false;
  }
  
  private static boolean moveFile(Path source, Path target) throws IOException {
    try {
      Files.move(source, target, new CopyOption[] { StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING });
      LOGGER.trace("Renamed file {} to {} with Files.move", source.toFile().getAbsolutePath(), target
          .toFile().getAbsolutePath());
      return true;
    } catch (AtomicMoveNotSupportedException ex) {
      Files.move(source, target, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
      LOGGER.trace("Renamed file {} to {} with Files.move", source.toFile().getAbsolutePath(), target
          .toFile().getAbsolutePath());
      return true;
    } 
  }
  
  public String toString() {
    return FileRenameAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", renameEmptyFiles=" + this.renameEmptyFiles + ']';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\FileRenameAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
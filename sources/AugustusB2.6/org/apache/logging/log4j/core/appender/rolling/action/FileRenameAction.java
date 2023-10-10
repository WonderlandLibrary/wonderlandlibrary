// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Path;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.Paths;
import java.io.File;

public class FileRenameAction extends AbstractAction
{
    private final File source;
    private final File destination;
    private final boolean renameEmptyFiles;
    
    public FileRenameAction(final File src, final File dst, final boolean renameEmptyFiles) {
        this.source = src;
        this.destination = dst;
        this.renameEmptyFiles = renameEmptyFiles;
    }
    
    @Override
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
    
    public static boolean execute(final File source, final File destination, final boolean renameEmptyFiles) {
        if (renameEmptyFiles || source.length() > 0L) {
            final File parent = destination.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
                if (!parent.exists()) {
                    FileRenameAction.LOGGER.error("Unable to create directory {}", parent.getAbsolutePath());
                    return false;
                }
            }
            try {
                try {
                    return moveFile(Paths.get(source.getAbsolutePath(), new String[0]), Paths.get(destination.getAbsolutePath(), new String[0]));
                }
                catch (IOException exMove) {
                    FileRenameAction.LOGGER.debug("Unable to move file {} to {}: {} {} - will try to copy and delete", source.getAbsolutePath(), destination.getAbsolutePath(), exMove.getClass().getName(), exMove.getMessage());
                    boolean result = source.renameTo(destination);
                    if (!result) {
                        try {
                            Files.copy(Paths.get(source.getAbsolutePath(), new String[0]), Paths.get(destination.getAbsolutePath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                            try {
                                Files.delete(Paths.get(source.getAbsolutePath(), new String[0]));
                                result = true;
                                FileRenameAction.LOGGER.trace("Renamed file {} to {} using copy and delete", source.getAbsolutePath(), destination.getAbsolutePath());
                            }
                            catch (IOException exDelete) {
                                FileRenameAction.LOGGER.error("Unable to delete file {}: {} {}", source.getAbsolutePath(), exDelete.getClass().getName(), exDelete.getMessage());
                                try {
                                    result = true;
                                    new PrintWriter(source.getAbsolutePath()).close();
                                    FileRenameAction.LOGGER.trace("Renamed file {} to {} with copy and truncation", source.getAbsolutePath(), destination.getAbsolutePath());
                                }
                                catch (IOException exOwerwrite) {
                                    FileRenameAction.LOGGER.error("Unable to overwrite file {}: {} {}", source.getAbsolutePath(), exOwerwrite.getClass().getName(), exOwerwrite.getMessage());
                                }
                            }
                        }
                        catch (IOException exCopy) {
                            FileRenameAction.LOGGER.error("Unable to copy file {} to {}: {} {}", source.getAbsolutePath(), destination.getAbsolutePath(), exCopy.getClass().getName(), exCopy.getMessage());
                        }
                    }
                    else {
                        FileRenameAction.LOGGER.trace("Renamed file {} to {} with source.renameTo", source.getAbsolutePath(), destination.getAbsolutePath());
                    }
                    return result;
                }
            }
            catch (RuntimeException ex) {
                FileRenameAction.LOGGER.error("Unable to rename file {} to {}: {} {}", source.getAbsolutePath(), destination.getAbsolutePath(), ex.getClass().getName(), ex.getMessage());
                return false;
            }
        }
        try {
            source.delete();
        }
        catch (Exception exDelete2) {
            FileRenameAction.LOGGER.error("Unable to delete empty file {}: {} {}", source.getAbsolutePath(), exDelete2.getClass().getName(), exDelete2.getMessage());
        }
        return false;
    }
    
    private static boolean moveFile(final Path source, final Path target) throws IOException {
        try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            FileRenameAction.LOGGER.trace("Renamed file {} to {} with Files.move", source.toFile().getAbsolutePath(), target.toFile().getAbsolutePath());
            return true;
        }
        catch (AtomicMoveNotSupportedException ex) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            FileRenameAction.LOGGER.trace("Renamed file {} to {} with Files.move", source.toFile().getAbsolutePath(), target.toFile().getAbsolutePath());
            return true;
        }
    }
    
    @Override
    public String toString() {
        return FileRenameAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", renameEmptyFiles=" + this.renameEmptyFiles + ']';
    }
}

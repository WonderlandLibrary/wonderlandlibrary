/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import org.apache.logging.log4j.core.appender.rolling.helper.AbstractAction;

public class FileRenameAction
extends AbstractAction {
    private final File source;
    private final File destination;
    private final boolean renameEmptyFiles;

    public FileRenameAction(File src, File dst, boolean renameEmptyFiles) {
        this.source = src;
        this.destination = dst;
        this.renameEmptyFiles = renameEmptyFiles;
    }

    @Override
    public boolean execute() {
        return FileRenameAction.execute(this.source, this.destination, this.renameEmptyFiles);
    }

    public static boolean execute(File source, File destination, boolean renameEmptyFiles) {
        if (renameEmptyFiles || source.length() > 0L) {
            File parent = destination.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                LOGGER.error("Unable to create directory {}", parent.getAbsolutePath());
                return false;
            }
            try {
                if (!source.renameTo(destination)) {
                    try {
                        FileRenameAction.copyFile(source, destination);
                        return source.delete();
                    }
                    catch (IOException iex) {
                        LOGGER.error("Unable to rename file {} to {} - {}", source.getAbsolutePath(), destination.getAbsolutePath(), iex.getMessage());
                    }
                }
                return true;
            }
            catch (Exception ex) {
                try {
                    FileRenameAction.copyFile(source, destination);
                    return source.delete();
                }
                catch (IOException iex) {
                    LOGGER.error("Unable to rename file {} to {} - {}", source.getAbsolutePath(), destination.getAbsolutePath(), iex.getMessage());
                }
            }
        } else {
            try {
                source.delete();
            }
            catch (Exception ex) {
                LOGGER.error("Unable to delete empty file " + source.getAbsolutePath());
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void copyFile(File source, File destination) throws IOException {
        if (!destination.exists()) {
            destination.createNewFile();
        }
        AbstractInterruptibleChannel srcChannel = null;
        AbstractInterruptibleChannel destChannel = null;
        FileInputStream srcStream = null;
        FileOutputStream destStream = null;
        try {
            srcStream = new FileInputStream(source);
            destStream = new FileOutputStream(destination);
            srcChannel = srcStream.getChannel();
            destChannel = destStream.getChannel();
            ((FileChannel)destChannel).transferFrom((ReadableByteChannel)((Object)srcChannel), 0L, ((FileChannel)srcChannel).size());
        }
        finally {
            if (srcChannel != null) {
                srcChannel.close();
            }
            if (srcStream != null) {
                srcStream.close();
            }
            if (destChannel != null) {
                destChannel.close();
            }
            if (destStream != null) {
                destStream.close();
            }
        }
    }
}


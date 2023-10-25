/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;
import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;

public final class GzCompressAction
extends AbstractAction {
    private static final int BUF_SIZE = 8192;
    private final File source;
    private final File destination;
    private final boolean deleteSource;
    private final int compressionLevel;

    public GzCompressAction(File source, File destination, boolean deleteSource, int compressionLevel) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        this.source = source;
        this.destination = destination;
        this.deleteSource = deleteSource;
        this.compressionLevel = compressionLevel;
    }

    @Deprecated
    public GzCompressAction(File source, File destination, boolean deleteSource) {
        this(source, destination, deleteSource, -1);
    }

    @Override
    public boolean execute() throws IOException {
        return GzCompressAction.execute(this.source, this.destination, this.deleteSource, this.compressionLevel);
    }

    @Deprecated
    public static boolean execute(File source, File destination, boolean deleteSource) throws IOException {
        return GzCompressAction.execute(source, destination, deleteSource, -1);
    }

    public static boolean execute(File source, File destination, boolean deleteSource, int compressionLevel) throws IOException {
        if (source.exists()) {
            try (FileInputStream fis = new FileInputStream(source);
                 FileOutputStream fos = new FileOutputStream(destination);
                 ConfigurableLevelGZIPOutputStream gzipOut = new ConfigurableLevelGZIPOutputStream((OutputStream)fos, 8192, compressionLevel);
                 BufferedOutputStream os = new BufferedOutputStream(gzipOut, 8192);){
                int n;
                byte[] inbuf = new byte[8192];
                while ((n = fis.read(inbuf)) != -1) {
                    ((OutputStream)os).write(inbuf, 0, n);
                }
            }
            if (deleteSource && !source.delete()) {
                LOGGER.warn("Unable to delete {}.", source);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void reportException(Exception ex) {
        LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", (Throwable)ex);
    }

    public String toString() {
        return GzCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", deleteSource=" + this.deleteSource + ']';
    }

    public File getSource() {
        return this.source;
    }

    public File getDestination() {
        return this.destination;
    }

    public boolean isDeleteSource() {
        return this.deleteSource;
    }

    private static final class ConfigurableLevelGZIPOutputStream
    extends GZIPOutputStream {
        ConfigurableLevelGZIPOutputStream(OutputStream out, int bufSize, int level) throws IOException {
            super(out, bufSize);
            this.def.setLevel(level);
        }
    }
}


// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.util.zip.GZIPOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.io.File;

public final class GzCompressAction extends AbstractAction
{
    private static final int BUF_SIZE = 8192;
    private final File source;
    private final File destination;
    private final boolean deleteSource;
    private final int compressionLevel;
    
    public GzCompressAction(final File source, final File destination, final boolean deleteSource, final int compressionLevel) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        this.source = source;
        this.destination = destination;
        this.deleteSource = deleteSource;
        this.compressionLevel = compressionLevel;
    }
    
    @Deprecated
    public GzCompressAction(final File source, final File destination, final boolean deleteSource) {
        this(source, destination, deleteSource, -1);
    }
    
    @Override
    public boolean execute() throws IOException {
        return execute(this.source, this.destination, this.deleteSource, this.compressionLevel);
    }
    
    @Deprecated
    public static boolean execute(final File source, final File destination, final boolean deleteSource) throws IOException {
        return execute(source, destination, deleteSource, -1);
    }
    
    public static boolean execute(final File source, final File destination, final boolean deleteSource, final int compressionLevel) throws IOException {
        if (source.exists()) {
            try (final FileInputStream fis = new FileInputStream(source);
                 final OutputStream fos = new FileOutputStream(destination);
                 final OutputStream gzipOut = new ConfigurableLevelGZIPOutputStream(fos, 8192, compressionLevel);
                 final OutputStream os = new BufferedOutputStream(gzipOut, 8192)) {
                final byte[] inbuf = new byte[8192];
                int n;
                while ((n = fis.read(inbuf)) != -1) {
                    os.write(inbuf, 0, n);
                }
            }
            if (deleteSource && !source.delete()) {
                GzCompressAction.LOGGER.warn("Unable to delete {}.", source);
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected void reportException(final Exception ex) {
        GzCompressAction.LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
    }
    
    @Override
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
    
    private static final class ConfigurableLevelGZIPOutputStream extends GZIPOutputStream
    {
        ConfigurableLevelGZIPOutputStream(final OutputStream out, final int bufSize, final int level) throws IOException {
            super(out, bufSize);
            this.def.setLevel(level);
        }
    }
}

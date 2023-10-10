// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.util.zip.ZipEntry;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.io.File;

public final class ZipCompressAction extends AbstractAction
{
    private static final int BUF_SIZE = 8192;
    private final File source;
    private final File destination;
    private final boolean deleteSource;
    private final int level;
    
    public ZipCompressAction(final File source, final File destination, final boolean deleteSource, final int level) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        this.source = source;
        this.destination = destination;
        this.deleteSource = deleteSource;
        this.level = level;
    }
    
    @Override
    public boolean execute() throws IOException {
        return execute(this.source, this.destination, this.deleteSource, this.level);
    }
    
    public static boolean execute(final File source, final File destination, final boolean deleteSource, final int level) throws IOException {
        if (source.exists()) {
            try (final FileInputStream fis = new FileInputStream(source);
                 final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destination))) {
                zos.setLevel(level);
                final ZipEntry zipEntry = new ZipEntry(source.getName());
                zos.putNextEntry(zipEntry);
                final byte[] inbuf = new byte[8192];
                int n;
                while ((n = fis.read(inbuf)) != -1) {
                    zos.write(inbuf, 0, n);
                }
            }
            if (deleteSource && !source.delete()) {
                ZipCompressAction.LOGGER.warn("Unable to delete " + source.toString() + '.');
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected void reportException(final Exception ex) {
        ZipCompressAction.LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
    }
    
    @Override
    public String toString() {
        return ZipCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", level=" + this.level + ", deleteSource=" + this.deleteSource + ']';
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
    
    public int getLevel() {
        return this.level;
    }
}

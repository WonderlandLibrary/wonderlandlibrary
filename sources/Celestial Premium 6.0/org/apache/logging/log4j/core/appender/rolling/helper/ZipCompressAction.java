/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.core.appender.rolling.helper.AbstractAction;

public final class ZipCompressAction
extends AbstractAction {
    private static final int BUF_SIZE = 8102;
    private final File source;
    private final File destination;
    private final boolean deleteSource;
    private final int level;

    public ZipCompressAction(File source, File destination, boolean deleteSource, int level) {
        if (source == null) {
            throw new NullPointerException("source");
        }
        if (destination == null) {
            throw new NullPointerException("destination");
        }
        this.source = source;
        this.destination = destination;
        this.deleteSource = deleteSource;
        this.level = level;
    }

    @Override
    public boolean execute() throws IOException {
        return ZipCompressAction.execute(this.source, this.destination, this.deleteSource, this.level);
    }

    public static boolean execute(File source, File destination, boolean deleteSource, int level) throws IOException {
        if (source.exists()) {
            int n;
            FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(destination);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.setLevel(level);
            ZipEntry zipEntry = new ZipEntry(source.getName());
            zos.putNextEntry(zipEntry);
            byte[] inbuf = new byte[8102];
            while ((n = fis.read(inbuf)) != -1) {
                zos.write(inbuf, 0, n);
            }
            zos.close();
            fis.close();
            if (deleteSource && !source.delete()) {
                LOGGER.warn("Unable to delete " + source.toString() + '.');
            }
            return true;
        }
        return false;
    }

    @Override
    protected void reportException(Exception ex) {
        LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", (Throwable)ex);
    }
}


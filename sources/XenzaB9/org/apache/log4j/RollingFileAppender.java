// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.log4j.spi.LoggingEvent;
import java.io.Writer;
import org.apache.log4j.helpers.OptionConverter;
import java.io.InterruptedIOException;
import java.io.File;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.CountingQuietWriter;
import java.io.IOException;

public class RollingFileAppender extends FileAppender
{
    protected long maxFileSize;
    protected int maxBackupIndex;
    private long nextRollover;
    
    public RollingFileAppender() {
        this.maxFileSize = 10485760L;
        this.maxBackupIndex = 1;
        this.nextRollover = 0L;
    }
    
    public RollingFileAppender(final Layout layout, final String filename, final boolean append) throws IOException {
        super(layout, filename, append);
        this.maxFileSize = 10485760L;
        this.maxBackupIndex = 1;
        this.nextRollover = 0L;
    }
    
    public RollingFileAppender(final Layout layout, final String filename) throws IOException {
        super(layout, filename);
        this.maxFileSize = 10485760L;
        this.maxBackupIndex = 1;
        this.nextRollover = 0L;
    }
    
    public int getMaxBackupIndex() {
        return this.maxBackupIndex;
    }
    
    public long getMaximumFileSize() {
        return this.maxFileSize;
    }
    
    public void rollOver() {
        if (this.qw != null) {
            final long size = ((CountingQuietWriter)this.qw).getCount();
            LogLog.debug("rolling over count=" + size);
            this.nextRollover = size + this.maxFileSize;
        }
        LogLog.debug("maxBackupIndex=" + this.maxBackupIndex);
        boolean renameSucceeded = true;
        if (this.maxBackupIndex > 0) {
            File file = new File(this.fileName + '.' + this.maxBackupIndex);
            if (file.exists()) {
                renameSucceeded = file.delete();
            }
            for (int i = this.maxBackupIndex - 1; i >= 1 && renameSucceeded; --i) {
                file = new File(this.fileName + "." + i);
                if (file.exists()) {
                    final File target = new File(this.fileName + '.' + (i + 1));
                    LogLog.debug("Renaming file " + file + " to " + target);
                    renameSucceeded = file.renameTo(target);
                }
            }
            if (renameSucceeded) {
                final File target = new File(this.fileName + "." + 1);
                this.closeFile();
                file = new File(this.fileName);
                LogLog.debug("Renaming file " + file + " to " + target);
                renameSucceeded = file.renameTo(target);
                if (!renameSucceeded) {
                    try {
                        this.setFile(this.fileName, true, this.bufferedIO, this.bufferSize);
                    }
                    catch (final IOException e) {
                        if (e instanceof InterruptedIOException) {
                            Thread.currentThread().interrupt();
                        }
                        LogLog.error("setFile(" + this.fileName + ", true) call failed.", e);
                    }
                }
            }
        }
        if (renameSucceeded) {
            try {
                this.setFile(this.fileName, false, this.bufferedIO, this.bufferSize);
                this.nextRollover = 0L;
            }
            catch (final IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("setFile(" + this.fileName + ", false) call failed.", e);
            }
        }
    }
    
    @Override
    public synchronized void setFile(final String fileName, final boolean append, final boolean bufferedIO, final int bufferSize) throws IOException {
        super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
        if (append) {
            final File f = new File(fileName);
            ((CountingQuietWriter)this.qw).setCount(f.length());
        }
    }
    
    public void setMaxBackupIndex(final int maxBackups) {
        this.maxBackupIndex = maxBackups;
    }
    
    public void setMaximumFileSize(final long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
    
    public void setMaxFileSize(final String value) {
        this.maxFileSize = OptionConverter.toFileSize(value, this.maxFileSize + 1L);
    }
    
    @Override
    protected void setQWForFiles(final Writer writer) {
        this.qw = new CountingQuietWriter(writer, this.errorHandler);
    }
    
    @Override
    protected void subAppend(final LoggingEvent event) {
        super.subAppend(event);
        if (this.fileName != null && this.qw != null) {
            final long size = ((CountingQuietWriter)this.qw).getCount();
            if (size >= this.maxFileSize && size >= this.nextRollover) {
                this.rollOver();
            }
        }
    }
}

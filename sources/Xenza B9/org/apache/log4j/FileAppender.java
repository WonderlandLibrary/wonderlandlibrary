// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.log4j.helpers.QuietWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InterruptedIOException;
import org.apache.log4j.helpers.LogLog;
import java.io.IOException;

public class FileAppender extends WriterAppender
{
    protected boolean fileAppend;
    protected String fileName;
    protected boolean bufferedIO;
    protected int bufferSize;
    
    public FileAppender() {
        this.fileAppend = true;
        this.fileName = null;
        this.bufferedIO = false;
        this.bufferSize = 8192;
    }
    
    public FileAppender(final Layout layout, final String filename) throws IOException {
        this(layout, filename, true);
    }
    
    public FileAppender(final Layout layout, final String filename, final boolean append) throws IOException {
        this.fileAppend = true;
        this.fileName = null;
        this.bufferedIO = false;
        this.bufferSize = 8192;
        this.layout = layout;
        this.setFile(filename, append, false, this.bufferSize);
    }
    
    public FileAppender(final Layout layout, final String filename, final boolean append, final boolean bufferedIO, final int bufferSize) throws IOException {
        this.fileAppend = true;
        this.fileName = null;
        this.bufferedIO = false;
        this.bufferSize = 8192;
        this.layout = layout;
        this.setFile(filename, append, bufferedIO, bufferSize);
    }
    
    @Override
    public void activateOptions() {
        if (this.fileName != null) {
            try {
                this.setFile(this.fileName, this.fileAppend, this.bufferedIO, this.bufferSize);
            }
            catch (final IOException e) {
                this.errorHandler.error("setFile(" + this.fileName + "," + this.fileAppend + ") call failed.", e, 4);
            }
        }
        else {
            LogLog.warn("File option not set for appender [" + this.name + "].");
            LogLog.warn("Are you using FileAppender instead of ConsoleAppender?");
        }
    }
    
    protected void closeFile() {
        if (this.qw != null) {
            try {
                this.qw.close();
            }
            catch (final IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("Could not close " + this.qw, e);
            }
        }
    }
    
    public boolean getAppend() {
        return this.fileAppend;
    }
    
    public boolean getBufferedIO() {
        return this.bufferedIO;
    }
    
    public int getBufferSize() {
        return this.bufferSize;
    }
    
    public String getFile() {
        return this.fileName;
    }
    
    @Override
    protected void reset() {
        this.closeFile();
        this.fileName = null;
        super.reset();
    }
    
    public void setAppend(final boolean flag) {
        this.fileAppend = flag;
    }
    
    public void setBufferedIO(final boolean bufferedIO) {
        this.bufferedIO = bufferedIO;
        if (bufferedIO) {
            this.immediateFlush = false;
        }
    }
    
    public void setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
    }
    
    public void setFile(final String file) {
        final String val = file.trim();
        this.fileName = val;
    }
    
    public synchronized void setFile(final String fileName, final boolean append, final boolean bufferedIO, final int bufferSize) throws IOException {
        LogLog.debug("setFile called: " + fileName + ", " + append);
        if (bufferedIO) {
            this.setImmediateFlush(false);
        }
        this.reset();
        FileOutputStream ostream = null;
        try {
            ostream = new FileOutputStream(fileName, append);
        }
        catch (final FileNotFoundException ex) {
            final String parentName = new File(fileName).getParent();
            if (parentName == null) {
                throw ex;
            }
            final File parentDir = new File(parentName);
            if (parentDir.exists() || !parentDir.mkdirs()) {
                throw ex;
            }
            ostream = new FileOutputStream(fileName, append);
        }
        Writer fw = this.createWriter(ostream);
        if (bufferedIO) {
            fw = new BufferedWriter(fw, bufferSize);
        }
        this.setQWForFiles(fw);
        this.fileName = fileName;
        this.fileAppend = append;
        this.bufferedIO = bufferedIO;
        this.bufferSize = bufferSize;
        this.writeHeader();
        LogLog.debug("setFile ended");
    }
    
    protected void setQWForFiles(final Writer writer) {
        this.qw = new QuietWriter(writer, this.errorHandler);
    }
}

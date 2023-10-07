// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.log4j.spi.ErrorHandler;
import java.io.IOException;
import java.io.InterruptedIOException;
import org.apache.log4j.spi.LoggingEvent;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import org.apache.log4j.helpers.QuietWriter;
import org.apache.logging.log4j.Logger;

public class WriterAppender extends AppenderSkeleton
{
    private static final Logger LOGGER;
    protected boolean immediateFlush;
    protected String encoding;
    protected QuietWriter qw;
    
    public WriterAppender() {
        this.immediateFlush = true;
    }
    
    public WriterAppender(final Layout layout, final OutputStream os) {
        this(layout, new OutputStreamWriter(os));
    }
    
    public WriterAppender(final Layout layout, final Writer writer) {
        this.immediateFlush = true;
        this.layout = layout;
        this.setWriter(writer);
    }
    
    public boolean getImmediateFlush() {
        return this.immediateFlush;
    }
    
    public void setImmediateFlush(final boolean value) {
        this.immediateFlush = value;
    }
    
    @Override
    public void activateOptions() {
    }
    
    public void append(final LoggingEvent event) {
        if (!this.checkEntryConditions()) {
            return;
        }
        this.subAppend(event);
    }
    
    protected boolean checkEntryConditions() {
        if (this.closed) {
            WriterAppender.LOGGER.warn("Not allowed to write to a closed appender.");
            return false;
        }
        if (this.qw == null) {
            this.errorHandler.error("No output stream or file set for the appender named [" + this.name + "].");
            return false;
        }
        if (this.layout == null) {
            this.errorHandler.error("No layout set for the appender named [" + this.name + "].");
            return false;
        }
        return true;
    }
    
    @Override
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.writeFooter();
        this.reset();
    }
    
    protected void closeWriter() {
        if (this.qw != null) {
            try {
                this.qw.close();
            }
            catch (final IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                WriterAppender.LOGGER.error("Could not close " + this.qw, e);
            }
        }
    }
    
    protected OutputStreamWriter createWriter(final OutputStream os) {
        OutputStreamWriter retval = null;
        final String enc = this.getEncoding();
        if (enc != null) {
            try {
                retval = new OutputStreamWriter(os, enc);
            }
            catch (final IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                WriterAppender.LOGGER.warn("Error initializing output writer.");
                WriterAppender.LOGGER.warn("Unsupported encoding?");
            }
        }
        if (retval == null) {
            retval = new OutputStreamWriter(os);
        }
        return retval;
    }
    
    public String getEncoding() {
        return this.encoding;
    }
    
    public void setEncoding(final String value) {
        this.encoding = value;
    }
    
    @Override
    public synchronized void setErrorHandler(final ErrorHandler eh) {
        if (eh == null) {
            WriterAppender.LOGGER.warn("You have tried to set a null error-handler.");
        }
        else {
            this.errorHandler = eh;
            if (this.qw != null) {
                this.qw.setErrorHandler(eh);
            }
        }
    }
    
    public synchronized void setWriter(final Writer writer) {
        this.reset();
        this.qw = new QuietWriter(writer, this.errorHandler);
        this.writeHeader();
    }
    
    protected void subAppend(final LoggingEvent event) {
        this.qw.write(this.layout.format(event));
        if (this.layout.ignoresThrowable()) {
            final String[] s = event.getThrowableStrRep();
            if (s != null) {
                for (int len = s.length, i = 0; i < len; ++i) {
                    this.qw.write(s[i]);
                    this.qw.write(Layout.LINE_SEP);
                }
            }
        }
        if (this.shouldFlush(event)) {
            this.qw.flush();
        }
    }
    
    @Override
    public boolean requiresLayout() {
        return true;
    }
    
    protected void reset() {
        this.closeWriter();
        this.qw = null;
    }
    
    protected void writeFooter() {
        if (this.layout != null) {
            final String f = this.layout.getFooter();
            if (f != null && this.qw != null) {
                this.qw.write(f);
                this.qw.flush();
            }
        }
    }
    
    protected void writeHeader() {
        if (this.layout != null) {
            final String h = this.layout.getHeader();
            if (h != null && this.qw != null) {
                this.qw.write(h);
            }
        }
    }
    
    protected boolean shouldFlush(final LoggingEvent event) {
        return this.immediateFlush;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}

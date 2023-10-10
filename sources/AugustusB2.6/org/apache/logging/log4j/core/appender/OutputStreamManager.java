// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.layout.ByteBufferDestinationHelper;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.util.Constants;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.layout.ByteBufferDestination;

public class OutputStreamManager extends AbstractManager implements ByteBufferDestination
{
    protected final Layout<?> layout;
    protected ByteBuffer byteBuffer;
    private volatile OutputStream outputStream;
    private boolean skipFooter;
    
    protected OutputStreamManager(final OutputStream os, final String streamName, final Layout<?> layout, final boolean writeHeader) {
        this(os, streamName, layout, writeHeader, Constants.ENCODER_BYTE_BUFFER_SIZE);
    }
    
    protected OutputStreamManager(final OutputStream os, final String streamName, final Layout<?> layout, final boolean writeHeader, final int bufferSize) {
        this(os, streamName, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
    }
    
    @Deprecated
    protected OutputStreamManager(final OutputStream os, final String streamName, final Layout<?> layout, final boolean writeHeader, final ByteBuffer byteBuffer) {
        super(null, streamName);
        this.outputStream = os;
        this.layout = layout;
        if (writeHeader) {
            this.writeHeader(os);
        }
        this.byteBuffer = Objects.requireNonNull(byteBuffer, "byteBuffer");
    }
    
    protected OutputStreamManager(final LoggerContext loggerContext, final OutputStream os, final String streamName, final boolean createOnDemand, final Layout<? extends Serializable> layout, final boolean writeHeader, final ByteBuffer byteBuffer) {
        super(loggerContext, streamName);
        if (createOnDemand && os != null) {
            OutputStreamManager.LOGGER.error("Invalid OutputStreamManager configuration for '{}': You cannot both set the OutputStream and request on-demand.", streamName);
        }
        this.layout = layout;
        this.byteBuffer = Objects.requireNonNull(byteBuffer, "byteBuffer");
        this.outputStream = os;
        if (writeHeader) {
            this.writeHeader(os);
        }
    }
    
    public static <T> OutputStreamManager getManager(final String name, final T data, final ManagerFactory<? extends OutputStreamManager, T> factory) {
        return AbstractManager.getManager(name, factory, data);
    }
    
    protected OutputStream createOutputStream() throws IOException {
        throw new IllegalStateException(this.getClass().getCanonicalName() + " must implement createOutputStream()");
    }
    
    public void skipFooter(final boolean skipFooter) {
        this.skipFooter = skipFooter;
    }
    
    public boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        this.writeFooter();
        return this.closeOutputStream();
    }
    
    protected void writeHeader(final OutputStream os) {
        if (this.layout != null && os != null) {
            final byte[] header = this.layout.getHeader();
            if (header != null) {
                try {
                    os.write(header, 0, header.length);
                }
                catch (IOException e) {
                    this.logError("Unable to write header", e);
                }
            }
        }
    }
    
    protected void writeFooter() {
        if (this.layout == null || this.skipFooter) {
            return;
        }
        final byte[] footer = this.layout.getFooter();
        if (footer != null) {
            this.write(footer);
        }
    }
    
    public boolean isOpen() {
        return this.getCount() > 0;
    }
    
    public boolean hasOutputStream() {
        return this.outputStream != null;
    }
    
    protected OutputStream getOutputStream() throws IOException {
        if (this.outputStream == null) {
            this.outputStream = this.createOutputStream();
        }
        return this.outputStream;
    }
    
    protected void setOutputStream(final OutputStream os) {
        this.outputStream = os;
    }
    
    protected void write(final byte[] bytes) {
        this.write(bytes, 0, bytes.length, false);
    }
    
    protected void write(final byte[] bytes, final boolean immediateFlush) {
        this.write(bytes, 0, bytes.length, immediateFlush);
    }
    
    @Override
    public void writeBytes(final byte[] data, final int offset, final int length) {
        this.write(data, offset, length, false);
    }
    
    protected void write(final byte[] bytes, final int offset, final int length) {
        this.writeBytes(bytes, offset, length);
    }
    
    protected synchronized void write(final byte[] bytes, final int offset, final int length, final boolean immediateFlush) {
        if (immediateFlush && this.byteBuffer.position() == 0) {
            this.writeToDestination(bytes, offset, length);
            this.flushDestination();
            return;
        }
        if (length >= this.byteBuffer.capacity()) {
            this.flush();
            this.writeToDestination(bytes, offset, length);
        }
        else {
            if (length > this.byteBuffer.remaining()) {
                this.flush();
            }
            this.byteBuffer.put(bytes, offset, length);
        }
        if (immediateFlush) {
            this.flush();
        }
    }
    
    protected synchronized void writeToDestination(final byte[] bytes, final int offset, final int length) {
        try {
            this.getOutputStream().write(bytes, offset, length);
        }
        catch (IOException ex) {
            throw new AppenderLoggingException("Error writing to stream " + this.getName(), ex);
        }
    }
    
    protected synchronized void flushDestination() {
        final OutputStream stream = this.outputStream;
        if (stream != null) {
            try {
                stream.flush();
            }
            catch (IOException ex) {
                throw new AppenderLoggingException("Error flushing stream " + this.getName(), ex);
            }
        }
    }
    
    protected synchronized void flushBuffer(final ByteBuffer buf) {
        buf.flip();
        try {
            if (buf.remaining() > 0) {
                this.writeToDestination(buf.array(), buf.arrayOffset() + buf.position(), buf.remaining());
            }
        }
        finally {
            buf.clear();
        }
    }
    
    public synchronized void flush() {
        this.flushBuffer(this.byteBuffer);
        this.flushDestination();
    }
    
    protected synchronized boolean closeOutputStream() {
        this.flush();
        final OutputStream stream = this.outputStream;
        if (stream == null || stream == System.out || stream == System.err) {
            return true;
        }
        try {
            stream.close();
            OutputStreamManager.LOGGER.debug("OutputStream closed");
        }
        catch (IOException ex) {
            this.logError("Unable to close stream", ex);
            return false;
        }
        return true;
    }
    
    @Override
    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }
    
    @Override
    public ByteBuffer drain(final ByteBuffer buf) {
        this.flushBuffer(buf);
        return buf;
    }
    
    @Override
    public void writeBytes(final ByteBuffer data) {
        if (data.remaining() == 0) {
            return;
        }
        synchronized (this) {
            ByteBufferDestinationHelper.writeToUnsynchronized(data, this);
        }
    }
}

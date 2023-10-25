/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ThresholdingOutputStream
extends OutputStream {
    private final int threshold;
    private long written;
    private boolean thresholdExceeded;

    public ThresholdingOutputStream(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void write(int b) throws IOException {
        this.checkThreshold(1);
        this.getStream().write(b);
        ++this.written;
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.checkThreshold(b.length);
        this.getStream().write(b);
        this.written += (long)b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.checkThreshold(len);
        this.getStream().write(b, off, len);
        this.written += (long)len;
    }

    @Override
    public void flush() throws IOException {
        this.getStream().flush();
    }

    @Override
    public void close() throws IOException {
        try {
            this.flush();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        this.getStream().close();
    }

    public int getThreshold() {
        return this.threshold;
    }

    public long getByteCount() {
        return this.written;
    }

    public boolean isThresholdExceeded() {
        return this.written > (long)this.threshold;
    }

    protected void checkThreshold(int count) throws IOException {
        if (!this.thresholdExceeded && this.written + (long)count > (long)this.threshold) {
            this.thresholdExceeded = true;
            this.thresholdReached();
        }
    }

    protected void resetByteCount() {
        this.thresholdExceeded = false;
        this.written = 0L;
    }

    protected void setByteCount(long count) {
        this.written = count;
    }

    protected abstract OutputStream getStream() throws IOException;

    protected abstract void thresholdReached() throws IOException;
}


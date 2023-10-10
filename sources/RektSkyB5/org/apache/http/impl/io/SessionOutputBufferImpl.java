/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

public class SessionOutputBufferImpl
implements SessionOutputBuffer,
BufferInfo {
    private static final byte[] CRLF = new byte[]{13, 10};
    private final HttpTransportMetricsImpl metrics;
    private final ByteArrayBuffer buffer;
    private final int fragementSizeHint;
    private final CharsetEncoder encoder;
    private OutputStream outstream;
    private ByteBuffer bbuf;

    public SessionOutputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize, int fragementSizeHint, CharsetEncoder charencoder) {
        Args.positive(buffersize, "Buffer size");
        Args.notNull(metrics, "HTTP transport metrcis");
        this.metrics = metrics;
        this.buffer = new ByteArrayBuffer(buffersize);
        this.fragementSizeHint = fragementSizeHint >= 0 ? fragementSizeHint : 0;
        this.encoder = charencoder;
    }

    public SessionOutputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize) {
        this(metrics, buffersize, buffersize, null);
    }

    public void bind(OutputStream outstream) {
        this.outstream = outstream;
    }

    public boolean isBound() {
        return this.outstream != null;
    }

    @Override
    public int capacity() {
        return this.buffer.capacity();
    }

    @Override
    public int length() {
        return this.buffer.length();
    }

    @Override
    public int available() {
        return this.capacity() - this.length();
    }

    private void streamWrite(byte[] b2, int off, int len) throws IOException {
        Asserts.notNull(this.outstream, "Output stream");
        this.outstream.write(b2, off, len);
    }

    private void flushStream() throws IOException {
        if (this.outstream != null) {
            this.outstream.flush();
        }
    }

    private void flushBuffer() throws IOException {
        int len = this.buffer.length();
        if (len > 0) {
            this.streamWrite(this.buffer.buffer(), 0, len);
            this.buffer.clear();
            this.metrics.incrementBytesTransferred(len);
        }
    }

    @Override
    public void flush() throws IOException {
        this.flushBuffer();
        this.flushStream();
    }

    @Override
    public void write(byte[] b2, int off, int len) throws IOException {
        if (b2 == null) {
            return;
        }
        if (len > this.fragementSizeHint || len > this.buffer.capacity()) {
            this.flushBuffer();
            this.streamWrite(b2, off, len);
            this.metrics.incrementBytesTransferred(len);
        } else {
            int freecapacity = this.buffer.capacity() - this.buffer.length();
            if (len > freecapacity) {
                this.flushBuffer();
            }
            this.buffer.append(b2, off, len);
        }
    }

    @Override
    public void write(byte[] b2) throws IOException {
        if (b2 == null) {
            return;
        }
        this.write(b2, 0, b2.length);
    }

    @Override
    public void write(int b2) throws IOException {
        if (this.fragementSizeHint > 0) {
            if (this.buffer.isFull()) {
                this.flushBuffer();
            }
            this.buffer.append(b2);
        } else {
            this.flushBuffer();
            this.outstream.write(b2);
        }
    }

    @Override
    public void writeLine(String s2) throws IOException {
        if (s2 == null) {
            return;
        }
        if (s2.length() > 0) {
            if (this.encoder == null) {
                for (int i2 = 0; i2 < s2.length(); ++i2) {
                    this.write(s2.charAt(i2));
                }
            } else {
                CharBuffer cbuf = CharBuffer.wrap(s2);
                this.writeEncoded(cbuf);
            }
        }
        this.write(CRLF);
    }

    @Override
    public void writeLine(CharArrayBuffer charbuffer) throws IOException {
        if (charbuffer == null) {
            return;
        }
        if (this.encoder == null) {
            int chunk;
            int off = 0;
            for (int remaining = charbuffer.length(); remaining > 0; remaining -= chunk) {
                chunk = this.buffer.capacity() - this.buffer.length();
                if ((chunk = Math.min(chunk, remaining)) > 0) {
                    this.buffer.append(charbuffer, off, chunk);
                }
                if (this.buffer.isFull()) {
                    this.flushBuffer();
                }
                off += chunk;
            }
        } else {
            CharBuffer cbuf = CharBuffer.wrap(charbuffer.buffer(), 0, charbuffer.length());
            this.writeEncoded(cbuf);
        }
        this.write(CRLF);
    }

    private void writeEncoded(CharBuffer cbuf) throws IOException {
        CoderResult result;
        if (!cbuf.hasRemaining()) {
            return;
        }
        if (this.bbuf == null) {
            this.bbuf = ByteBuffer.allocate(1024);
        }
        this.encoder.reset();
        while (cbuf.hasRemaining()) {
            result = this.encoder.encode(cbuf, this.bbuf, true);
            this.handleEncodingResult(result);
        }
        result = this.encoder.flush(this.bbuf);
        this.handleEncodingResult(result);
        this.bbuf.clear();
    }

    private void handleEncodingResult(CoderResult result) throws IOException {
        if (result.isError()) {
            result.throwException();
        }
        this.bbuf.flip();
        while (this.bbuf.hasRemaining()) {
            this.write(this.bbuf.get());
        }
        this.bbuf.compact();
    }

    @Override
    public HttpTransportMetrics getMetrics() {
        return this.metrics;
    }
}


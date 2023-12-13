/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import org.apache.http.MessageConstraintException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

public class SessionInputBufferImpl
implements SessionInputBuffer,
BufferInfo {
    private final HttpTransportMetricsImpl metrics;
    private final byte[] buffer;
    private final ByteArrayBuffer linebuffer;
    private final int minChunkLimit;
    private final MessageConstraints constraints;
    private final CharsetDecoder decoder;
    private InputStream instream;
    private int bufferpos;
    private int bufferlen;
    private CharBuffer cbuf;

    public SessionInputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize, int minChunkLimit, MessageConstraints constraints, CharsetDecoder chardecoder) {
        Args.notNull(metrics, "HTTP transport metrcis");
        Args.positive(buffersize, "Buffer size");
        this.metrics = metrics;
        this.buffer = new byte[buffersize];
        this.bufferpos = 0;
        this.bufferlen = 0;
        this.minChunkLimit = minChunkLimit >= 0 ? minChunkLimit : 512;
        this.constraints = constraints != null ? constraints : MessageConstraints.DEFAULT;
        this.linebuffer = new ByteArrayBuffer(buffersize);
        this.decoder = chardecoder;
    }

    public SessionInputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize) {
        this(metrics, buffersize, buffersize, null, null);
    }

    public void bind(InputStream instream) {
        this.instream = instream;
    }

    public boolean isBound() {
        return this.instream != null;
    }

    @Override
    public int capacity() {
        return this.buffer.length;
    }

    @Override
    public int length() {
        return this.bufferlen - this.bufferpos;
    }

    @Override
    public int available() {
        return this.capacity() - this.length();
    }

    private int streamRead(byte[] b2, int off, int len) throws IOException {
        Asserts.notNull(this.instream, "Input stream");
        return this.instream.read(b2, off, len);
    }

    public int fillBuffer() throws IOException {
        int len;
        int off;
        int l2;
        if (this.bufferpos > 0) {
            int len2 = this.bufferlen - this.bufferpos;
            if (len2 > 0) {
                System.arraycopy(this.buffer, this.bufferpos, this.buffer, 0, len2);
            }
            this.bufferpos = 0;
            this.bufferlen = len2;
        }
        if ((l2 = this.streamRead(this.buffer, off = this.bufferlen, len = this.buffer.length - off)) == -1) {
            return -1;
        }
        this.bufferlen = off + l2;
        this.metrics.incrementBytesTransferred(l2);
        return l2;
    }

    public boolean hasBufferedData() {
        return this.bufferpos < this.bufferlen;
    }

    public void clear() {
        this.bufferpos = 0;
        this.bufferlen = 0;
    }

    @Override
    public int read() throws IOException {
        while (!this.hasBufferedData()) {
            int noRead = this.fillBuffer();
            if (noRead != -1) continue;
            return -1;
        }
        return this.buffer[this.bufferpos++] & 0xFF;
    }

    @Override
    public int read(byte[] b2, int off, int len) throws IOException {
        if (b2 == null) {
            return 0;
        }
        if (this.hasBufferedData()) {
            int chunk = Math.min(len, this.bufferlen - this.bufferpos);
            System.arraycopy(this.buffer, this.bufferpos, b2, off, chunk);
            this.bufferpos += chunk;
            return chunk;
        }
        if (len > this.minChunkLimit) {
            int read = this.streamRead(b2, off, len);
            if (read > 0) {
                this.metrics.incrementBytesTransferred(read);
            }
            return read;
        }
        while (!this.hasBufferedData()) {
            int noRead = this.fillBuffer();
            if (noRead != -1) continue;
            return -1;
        }
        int chunk = Math.min(len, this.bufferlen - this.bufferpos);
        System.arraycopy(this.buffer, this.bufferpos, b2, off, chunk);
        this.bufferpos += chunk;
        return chunk;
    }

    @Override
    public int read(byte[] b2) throws IOException {
        if (b2 == null) {
            return 0;
        }
        return this.read(b2, 0, b2.length);
    }

    @Override
    public int readLine(CharArrayBuffer charbuffer) throws IOException {
        Args.notNull(charbuffer, "Char array buffer");
        int maxLineLen = this.constraints.getMaxLineLength();
        int noRead = 0;
        boolean retry = true;
        while (retry) {
            int len;
            int currentLen;
            int pos = -1;
            for (int i2 = this.bufferpos; i2 < this.bufferlen; ++i2) {
                if (this.buffer[i2] != 10) continue;
                pos = i2;
                break;
            }
            if (maxLineLen > 0 && (currentLen = this.linebuffer.length() + (pos >= 0 ? pos : this.bufferlen) - this.bufferpos) >= maxLineLen) {
                throw new MessageConstraintException("Maximum line length limit exceeded");
            }
            if (pos != -1) {
                if (this.linebuffer.isEmpty()) {
                    return this.lineFromReadBuffer(charbuffer, pos);
                }
                retry = false;
                len = pos + 1 - this.bufferpos;
                this.linebuffer.append(this.buffer, this.bufferpos, len);
                this.bufferpos = pos + 1;
                continue;
            }
            if (this.hasBufferedData()) {
                len = this.bufferlen - this.bufferpos;
                this.linebuffer.append(this.buffer, this.bufferpos, len);
                this.bufferpos = this.bufferlen;
            }
            if ((noRead = this.fillBuffer()) != -1) continue;
            retry = false;
        }
        if (noRead == -1 && this.linebuffer.isEmpty()) {
            return -1;
        }
        return this.lineFromLineBuffer(charbuffer);
    }

    private int lineFromLineBuffer(CharArrayBuffer charbuffer) throws IOException {
        int len = this.linebuffer.length();
        if (len > 0) {
            if (this.linebuffer.byteAt(len - 1) == 10) {
                --len;
            }
            if (len > 0 && this.linebuffer.byteAt(len - 1) == 13) {
                --len;
            }
        }
        if (this.decoder == null) {
            charbuffer.append(this.linebuffer, 0, len);
        } else {
            ByteBuffer bbuf = ByteBuffer.wrap(this.linebuffer.buffer(), 0, len);
            len = this.appendDecoded(charbuffer, bbuf);
        }
        this.linebuffer.clear();
        return len;
    }

    private int lineFromReadBuffer(CharArrayBuffer charbuffer, int position) throws IOException {
        int pos = position;
        int off = this.bufferpos;
        this.bufferpos = pos + 1;
        if (pos > off && this.buffer[pos - 1] == 13) {
            --pos;
        }
        int len = pos - off;
        if (this.decoder == null) {
            charbuffer.append(this.buffer, off, len);
        } else {
            ByteBuffer bbuf = ByteBuffer.wrap(this.buffer, off, len);
            len = this.appendDecoded(charbuffer, bbuf);
        }
        return len;
    }

    private int appendDecoded(CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
        CoderResult result;
        if (!bbuf.hasRemaining()) {
            return 0;
        }
        if (this.cbuf == null) {
            this.cbuf = CharBuffer.allocate(1024);
        }
        this.decoder.reset();
        int len = 0;
        while (bbuf.hasRemaining()) {
            result = this.decoder.decode(bbuf, this.cbuf, true);
            len += this.handleDecodingResult(result, charbuffer, bbuf);
        }
        result = this.decoder.flush(this.cbuf);
        this.cbuf.clear();
        return len += this.handleDecodingResult(result, charbuffer, bbuf);
    }

    private int handleDecodingResult(CoderResult result, CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
        if (result.isError()) {
            result.throwException();
        }
        this.cbuf.flip();
        int len = this.cbuf.remaining();
        while (this.cbuf.hasRemaining()) {
            charbuffer.append(this.cbuf.get());
        }
        this.cbuf.compact();
        return len;
    }

    @Override
    public String readLine() throws IOException {
        CharArrayBuffer charbuffer = new CharArrayBuffer(64);
        int l2 = this.readLine(charbuffer);
        if (l2 != -1) {
            return charbuffer.toString();
        }
        return null;
    }

    @Override
    public boolean isDataAvailable(int timeout) throws IOException {
        return this.hasBufferedData();
    }

    @Override
    public HttpTransportMetrics getMetrics() {
        return this.metrics;
    }
}


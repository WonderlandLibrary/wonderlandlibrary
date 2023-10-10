/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.MalformedChunkCodingException;
import org.apache.http.TruncatedChunkException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.io.AbstractMessageParser;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class ChunkedInputStream
extends InputStream {
    private static final int CHUNK_LEN = 1;
    private static final int CHUNK_DATA = 2;
    private static final int CHUNK_CRLF = 3;
    private static final int CHUNK_INVALID = Integer.MAX_VALUE;
    private static final int BUFFER_SIZE = 2048;
    private final SessionInputBuffer in;
    private final CharArrayBuffer buffer;
    private final MessageConstraints constraints;
    private int state;
    private long chunkSize;
    private long pos;
    private boolean eof = false;
    private boolean closed = false;
    private Header[] footers = new Header[0];

    public ChunkedInputStream(SessionInputBuffer in, MessageConstraints constraints) {
        this.in = Args.notNull(in, "Session input buffer");
        this.pos = 0L;
        this.buffer = new CharArrayBuffer(16);
        this.constraints = constraints != null ? constraints : MessageConstraints.DEFAULT;
        this.state = 1;
    }

    public ChunkedInputStream(SessionInputBuffer in) {
        this(in, null);
    }

    @Override
    public int available() throws IOException {
        if (this.in instanceof BufferInfo) {
            int len = ((BufferInfo)((Object)this.in)).length();
            return (int)Math.min((long)len, this.chunkSize - this.pos);
        }
        return 0;
    }

    @Override
    public int read() throws IOException {
        int b2;
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        }
        if (this.eof) {
            return -1;
        }
        if (this.state != 2) {
            this.nextChunk();
            if (this.eof) {
                return -1;
            }
        }
        if ((b2 = this.in.read()) != -1) {
            ++this.pos;
            if (this.pos >= this.chunkSize) {
                this.state = 3;
            }
        }
        return b2;
    }

    @Override
    public int read(byte[] b2, int off, int len) throws IOException {
        int bytesRead;
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        }
        if (this.eof) {
            return -1;
        }
        if (this.state != 2) {
            this.nextChunk();
            if (this.eof) {
                return -1;
            }
        }
        if ((bytesRead = this.in.read(b2, off, (int)Math.min((long)len, this.chunkSize - this.pos))) != -1) {
            this.pos += (long)bytesRead;
            if (this.pos >= this.chunkSize) {
                this.state = 3;
            }
            return bytesRead;
        }
        this.eof = true;
        throw new TruncatedChunkException("Truncated chunk ( expected size: " + this.chunkSize + "; actual size: " + this.pos + ")");
    }

    @Override
    public int read(byte[] b2) throws IOException {
        return this.read(b2, 0, b2.length);
    }

    private void nextChunk() throws IOException {
        if (this.state == Integer.MAX_VALUE) {
            throw new MalformedChunkCodingException("Corrupt data stream");
        }
        try {
            this.chunkSize = this.getChunkSize();
            if (this.chunkSize < 0L) {
                throw new MalformedChunkCodingException("Negative chunk size");
            }
            this.state = 2;
            this.pos = 0L;
            if (this.chunkSize == 0L) {
                this.eof = true;
                this.parseTrailerHeaders();
            }
        }
        catch (MalformedChunkCodingException ex) {
            this.state = Integer.MAX_VALUE;
            throw ex;
        }
    }

    private long getChunkSize() throws IOException {
        int st = this.state;
        switch (st) {
            case 3: {
                this.buffer.clear();
                int bytesRead1 = this.in.readLine(this.buffer);
                if (bytesRead1 == -1) {
                    throw new MalformedChunkCodingException("CRLF expected at end of chunk");
                }
                if (!this.buffer.isEmpty()) {
                    throw new MalformedChunkCodingException("Unexpected content at the end of chunk");
                }
                this.state = 1;
            }
            case 1: {
                this.buffer.clear();
                int bytesRead2 = this.in.readLine(this.buffer);
                if (bytesRead2 == -1) {
                    throw new ConnectionClosedException("Premature end of chunk coded message body: closing chunk expected");
                }
                int separator = this.buffer.indexOf(59);
                if (separator < 0) {
                    separator = this.buffer.length();
                }
                String s2 = this.buffer.substringTrimmed(0, separator);
                try {
                    return Long.parseLong(s2, 16);
                }
                catch (NumberFormatException e2) {
                    throw new MalformedChunkCodingException("Bad chunk header: " + s2);
                }
            }
        }
        throw new IllegalStateException("Inconsistent codec state");
    }

    private void parseTrailerHeaders() throws IOException {
        try {
            this.footers = AbstractMessageParser.parseHeaders(this.in, this.constraints.getMaxHeaderCount(), this.constraints.getMaxLineLength(), null);
        }
        catch (HttpException ex) {
            MalformedChunkCodingException ioe = new MalformedChunkCodingException("Invalid footer: " + ex.getMessage());
            ioe.initCause(ex);
            throw ioe;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            try {
                if (!this.eof && this.state != Integer.MAX_VALUE) {
                    byte[] buff = new byte[2048];
                    while (this.read(buff) >= 0) {
                    }
                }
            }
            finally {
                this.eof = true;
                this.closed = true;
            }
        }
    }

    public Header[] getFooters() {
        return (Header[])this.footers.clone();
    }
}


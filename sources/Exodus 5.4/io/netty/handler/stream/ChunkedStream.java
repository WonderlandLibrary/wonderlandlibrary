/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class ChunkedStream
implements ChunkedInput<ByteBuf> {
    private final int chunkSize;
    private final PushbackInputStream in;
    private long offset;
    static final int DEFAULT_CHUNK_SIZE = 8192;

    @Override
    public ByteBuf readChunk(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.isEndOfInput()) {
            return null;
        }
        int n = this.in.available();
        int n2 = n <= 0 ? this.chunkSize : Math.min(this.chunkSize, this.in.available());
        boolean bl = true;
        ByteBuf byteBuf = channelHandlerContext.alloc().buffer(n2);
        this.offset += (long)byteBuf.writeBytes(this.in, n2);
        bl = false;
        ByteBuf byteBuf2 = byteBuf;
        if (bl) {
            byteBuf.release();
        }
        return byteBuf2;
    }

    public ChunkedStream(InputStream inputStream) {
        this(inputStream, 8192);
    }

    @Override
    public boolean isEndOfInput() throws Exception {
        int n = this.in.read();
        if (n < 0) {
            return true;
        }
        this.in.unread(n);
        return false;
    }

    @Override
    public void close() throws Exception {
        this.in.close();
    }

    public ChunkedStream(InputStream inputStream, int n) {
        if (inputStream == null) {
            throw new NullPointerException("in");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("chunkSize: " + n + " (expected: a positive integer)");
        }
        this.in = inputStream instanceof PushbackInputStream ? (PushbackInputStream)inputStream : new PushbackInputStream(inputStream);
        this.chunkSize = n;
    }

    public long transferredBytes() {
        return this.offset;
    }
}


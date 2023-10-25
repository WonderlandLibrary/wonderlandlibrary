/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class ChunkedNioStream
implements ChunkedInput<ByteBuf> {
    private final ByteBuffer byteBuffer;
    private long offset;
    private final ReadableByteChannel in;
    private final int chunkSize;

    @Override
    public ByteBuf readChunk(ChannelHandlerContext channelHandlerContext) throws Exception {
        int n;
        if (this.isEndOfInput()) {
            return null;
        }
        int n2 = this.byteBuffer.position();
        while ((n = this.in.read(this.byteBuffer)) >= 0) {
            this.offset += (long)n;
            if ((n2 += n) != this.chunkSize) continue;
            break;
        }
        this.byteBuffer.flip();
        n = 1;
        ByteBuf byteBuf = channelHandlerContext.alloc().buffer(this.byteBuffer.remaining());
        byteBuf.writeBytes(this.byteBuffer);
        this.byteBuffer.clear();
        n = 0;
        ByteBuf byteBuf2 = byteBuf;
        if (n != 0) {
            byteBuf.release();
        }
        return byteBuf2;
    }

    public ChunkedNioStream(ReadableByteChannel readableByteChannel) {
        this(readableByteChannel, 8192);
    }

    @Override
    public void close() throws Exception {
        this.in.close();
    }

    public ChunkedNioStream(ReadableByteChannel readableByteChannel, int n) {
        if (readableByteChannel == null) {
            throw new NullPointerException("in");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("chunkSize: " + n + " (expected: a positive integer)");
        }
        this.in = readableByteChannel;
        this.offset = 0L;
        this.chunkSize = n;
        this.byteBuffer = ByteBuffer.allocate(n);
    }

    public long transferredBytes() {
        return this.offset;
    }

    @Override
    public boolean isEndOfInput() throws Exception {
        if (this.byteBuffer.position() > 0) {
            return false;
        }
        if (this.in.isOpen()) {
            int n = this.in.read(this.byteBuffer);
            if (n < 0) {
                return true;
            }
            this.offset += (long)n;
            return false;
        }
        return true;
    }
}


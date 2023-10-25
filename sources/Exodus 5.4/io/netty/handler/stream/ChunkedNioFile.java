/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ChunkedNioFile
implements ChunkedInput<ByteBuf> {
    private long offset;
    private final long endOffset;
    private final int chunkSize;
    private final FileChannel in;
    private final long startOffset;

    @Override
    public void close() throws Exception {
        this.in.close();
    }

    @Override
    public ByteBuf readChunk(ChannelHandlerContext channelHandlerContext) throws Exception {
        int n;
        long l = this.offset;
        if (l >= this.endOffset) {
            return null;
        }
        int n2 = (int)Math.min((long)this.chunkSize, this.endOffset - l);
        ByteBuf byteBuf = channelHandlerContext.alloc().buffer(n2);
        boolean bl = true;
        int n3 = 0;
        while ((n = byteBuf.writeBytes(this.in, n2 - n3)) >= 0 && (n3 += n) != n2) {
        }
        this.offset += (long)n3;
        bl = false;
        ByteBuf byteBuf2 = byteBuf;
        if (bl) {
            byteBuf.release();
        }
        return byteBuf2;
    }

    public long startOffset() {
        return this.startOffset;
    }

    public ChunkedNioFile(FileChannel fileChannel, int n) throws IOException {
        this(fileChannel, 0L, fileChannel.size(), n);
    }

    public long endOffset() {
        return this.endOffset;
    }

    public long currentOffset() {
        return this.offset;
    }

    public ChunkedNioFile(FileChannel fileChannel, long l, long l2, int n) throws IOException {
        if (fileChannel == null) {
            throw new NullPointerException("in");
        }
        if (l < 0L) {
            throw new IllegalArgumentException("offset: " + l + " (expected: 0 or greater)");
        }
        if (l2 < 0L) {
            throw new IllegalArgumentException("length: " + l2 + " (expected: 0 or greater)");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("chunkSize: " + n + " (expected: a positive integer)");
        }
        if (l != 0L) {
            fileChannel.position(l);
        }
        this.in = fileChannel;
        this.chunkSize = n;
        this.offset = this.startOffset = l;
        this.endOffset = l + l2;
    }

    @Override
    public boolean isEndOfInput() throws Exception {
        return this.offset >= this.endOffset || !this.in.isOpen();
    }

    public ChunkedNioFile(File file) throws IOException {
        this(new FileInputStream(file).getChannel());
    }

    public ChunkedNioFile(File file, int n) throws IOException {
        this(new FileInputStream(file).getChannel(), n);
    }

    public ChunkedNioFile(FileChannel fileChannel) throws IOException {
        this(fileChannel, 8192);
    }
}


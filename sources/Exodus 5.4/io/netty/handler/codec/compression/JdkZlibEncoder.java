/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelPromiseNotifier;
import io.netty.handler.codec.compression.ZlibEncoder;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

public class JdkZlibEncoder
extends ZlibEncoder {
    private static final byte[] gzipHeader = new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0};
    private final CRC32 crc = new CRC32();
    private boolean writeHeader = true;
    private final ZlibWrapper wrapper;
    private volatile ChannelHandlerContext ctx;
    private final Deflater deflater;
    private volatile boolean finished;

    private ChannelFuture finishEncode(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) {
        if (this.finished) {
            channelPromise.setSuccess();
            return channelPromise;
        }
        this.finished = true;
        ByteBuf byteBuf = channelHandlerContext.alloc().heapBuffer();
        if (this.writeHeader && this.wrapper == ZlibWrapper.GZIP) {
            this.writeHeader = false;
            byteBuf.writeBytes(gzipHeader);
        }
        this.deflater.finish();
        while (!this.deflater.finished()) {
            this.deflate(byteBuf);
            if (byteBuf.isWritable()) continue;
            channelHandlerContext.write(byteBuf);
            byteBuf = channelHandlerContext.alloc().heapBuffer();
        }
        if (this.wrapper == ZlibWrapper.GZIP) {
            int n = (int)this.crc.getValue();
            int n2 = this.deflater.getTotalIn();
            byteBuf.writeByte(n);
            byteBuf.writeByte(n >>> 8);
            byteBuf.writeByte(n >>> 16);
            byteBuf.writeByte(n >>> 24);
            byteBuf.writeByte(n2);
            byteBuf.writeByte(n2 >>> 8);
            byteBuf.writeByte(n2 >>> 16);
            byteBuf.writeByte(n2 >>> 24);
        }
        this.deflater.end();
        return channelHandlerContext.writeAndFlush(byteBuf, channelPromise);
    }

    public JdkZlibEncoder(ZlibWrapper zlibWrapper, int n) {
        if (n < 0 || n > 9) {
            throw new IllegalArgumentException("compressionLevel: " + n + " (expected: 0-9)");
        }
        if (zlibWrapper == null) {
            throw new NullPointerException("wrapper");
        }
        if (zlibWrapper == ZlibWrapper.ZLIB_OR_NONE) {
            throw new IllegalArgumentException("wrapper '" + (Object)((Object)ZlibWrapper.ZLIB_OR_NONE) + "' is not " + "allowed for compression.");
        }
        this.wrapper = zlibWrapper;
        this.deflater = new Deflater(n, zlibWrapper != ZlibWrapper.ZLIB);
    }

    public JdkZlibEncoder(ZlibWrapper zlibWrapper) {
        this(zlibWrapper, 6);
    }

    @Override
    public boolean isClosed() {
        return this.finished;
    }

    public JdkZlibEncoder(int n) {
        this(ZlibWrapper.ZLIB, n);
    }

    @Override
    public void close(final ChannelHandlerContext channelHandlerContext, final ChannelPromise channelPromise) throws Exception {
        ChannelFuture channelFuture = this.finishEncode(channelHandlerContext, channelHandlerContext.newPromise());
        channelFuture.addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                channelHandlerContext.close(channelPromise);
            }
        });
        if (!channelFuture.isDone()) {
            channelHandlerContext.executor().schedule(new Runnable(){

                @Override
                public void run() {
                    channelHandlerContext.close(channelPromise);
                }
            }, 10L, TimeUnit.SECONDS);
        }
    }

    @Override
    public ChannelFuture close(final ChannelPromise channelPromise) {
        ChannelHandlerContext channelHandlerContext = this.ctx();
        EventExecutor eventExecutor = channelHandlerContext.executor();
        if (eventExecutor.inEventLoop()) {
            return this.finishEncode(channelHandlerContext, channelPromise);
        }
        final ChannelPromise channelPromise2 = channelHandlerContext.newPromise();
        eventExecutor.execute(new Runnable(){

            @Override
            public void run() {
                ChannelFuture channelFuture = JdkZlibEncoder.this.finishEncode(JdkZlibEncoder.this.ctx(), channelPromise2);
                channelFuture.addListener(new ChannelPromiseNotifier(channelPromise));
            }
        });
        return channelPromise2;
    }

    @Override
    public ChannelFuture close() {
        return this.close(this.ctx().newPromise());
    }

    public JdkZlibEncoder(int n, byte[] byArray) {
        if (n < 0 || n > 9) {
            throw new IllegalArgumentException("compressionLevel: " + n + " (expected: 0-9)");
        }
        if (byArray == null) {
            throw new NullPointerException("dictionary");
        }
        this.wrapper = ZlibWrapper.ZLIB;
        this.deflater = new Deflater(n);
        this.deflater.setDictionary(byArray);
    }

    private ChannelHandlerContext ctx() {
        ChannelHandlerContext channelHandlerContext = this.ctx;
        if (channelHandlerContext == null) {
            throw new IllegalStateException("not added to a pipeline");
        }
        return channelHandlerContext;
    }

    private void deflate(ByteBuf byteBuf) {
        int n;
        do {
            int n2 = byteBuf.writerIndex();
            n = this.deflater.deflate(byteBuf.array(), byteBuf.arrayOffset() + n2, byteBuf.writableBytes(), 2);
            byteBuf.writerIndex(n2 + n);
        } while (n > 0);
    }

    @Override
    protected final ByteBuf allocateBuffer(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, boolean bl) throws Exception {
        int n = (int)Math.ceil((double)byteBuf.readableBytes() * 1.001) + 12;
        if (this.writeHeader) {
            switch (this.wrapper) {
                case GZIP: {
                    n += gzipHeader.length;
                    break;
                }
                case ZLIB: {
                    n += 2;
                }
            }
        }
        return channelHandlerContext.alloc().heapBuffer(n);
    }

    public JdkZlibEncoder(byte[] byArray) {
        this(6, byArray);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.ctx = channelHandlerContext;
    }

    public JdkZlibEncoder() {
        this(6);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) throws Exception {
        int n;
        byte[] byArray;
        if (this.finished) {
            byteBuf2.writeBytes(byteBuf);
            return;
        }
        int n2 = byteBuf.readableBytes();
        if (byteBuf.hasArray()) {
            byArray = byteBuf.array();
            n = byteBuf.arrayOffset() + byteBuf.readerIndex();
            byteBuf.skipBytes(n2);
        } else {
            byArray = new byte[n2];
            byteBuf.readBytes(byArray);
            n = 0;
        }
        if (this.writeHeader) {
            this.writeHeader = false;
            if (this.wrapper == ZlibWrapper.GZIP) {
                byteBuf2.writeBytes(gzipHeader);
            }
        }
        if (this.wrapper == ZlibWrapper.GZIP) {
            this.crc.update(byArray, n, n2);
        }
        this.deflater.setInput(byArray, n, n2);
        while (!this.deflater.needsInput()) {
            this.deflate(byteBuf2);
        }
    }
}


/*
 * Decompiled with CFR 0.150.
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
    private final ZlibWrapper wrapper;
    private final byte[] encodeBuf = new byte[8192];
    private final Deflater deflater;
    private volatile boolean finished;
    private volatile ChannelHandlerContext ctx;
    private final CRC32 crc = new CRC32();
    private static final byte[] gzipHeader = new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0};
    private boolean writeHeader = true;

    public JdkZlibEncoder() {
        this(6);
    }

    public JdkZlibEncoder(int compressionLevel) {
        this(ZlibWrapper.ZLIB, compressionLevel);
    }

    public JdkZlibEncoder(ZlibWrapper wrapper) {
        this(wrapper, 6);
    }

    public JdkZlibEncoder(ZlibWrapper wrapper, int compressionLevel) {
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        if (wrapper == null) {
            throw new NullPointerException("wrapper");
        }
        if (wrapper == ZlibWrapper.ZLIB_OR_NONE) {
            throw new IllegalArgumentException("wrapper '" + (Object)((Object)ZlibWrapper.ZLIB_OR_NONE) + "' is not " + "allowed for compression.");
        }
        this.wrapper = wrapper;
        this.deflater = new Deflater(compressionLevel, wrapper != ZlibWrapper.ZLIB);
    }

    public JdkZlibEncoder(byte[] dictionary) {
        this(6, dictionary);
    }

    public JdkZlibEncoder(int compressionLevel, byte[] dictionary) {
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        if (dictionary == null) {
            throw new NullPointerException("dictionary");
        }
        this.wrapper = ZlibWrapper.ZLIB;
        this.deflater = new Deflater(compressionLevel);
        this.deflater.setDictionary(dictionary);
    }

    @Override
    public ChannelFuture close() {
        return this.close(this.ctx().newPromise());
    }

    @Override
    public ChannelFuture close(final ChannelPromise promise) {
        ChannelHandlerContext ctx = this.ctx();
        EventExecutor executor = ctx.executor();
        if (executor.inEventLoop()) {
            return this.finishEncode(ctx, promise);
        }
        final ChannelPromise p = ctx.newPromise();
        executor.execute(new Runnable(){

            @Override
            public void run() {
                ChannelFuture f = JdkZlibEncoder.this.finishEncode(JdkZlibEncoder.this.ctx(), p);
                f.addListener(new ChannelPromiseNotifier(promise));
            }
        });
        return p;
    }

    private ChannelHandlerContext ctx() {
        ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException("not added to a pipeline");
        }
        return ctx;
    }

    @Override
    public boolean isClosed() {
        return this.finished;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf uncompressed, ByteBuf out) throws Exception {
        if (this.finished) {
            out.writeBytes(uncompressed);
            return;
        }
        byte[] inAry = new byte[uncompressed.readableBytes()];
        uncompressed.readBytes(inAry);
        int sizeEstimate = (int)Math.ceil((double)inAry.length * 1.001) + 12;
        if (this.writeHeader) {
            this.writeHeader = false;
            switch (this.wrapper) {
                case GZIP: {
                    out.ensureWritable(sizeEstimate + gzipHeader.length);
                    out.writeBytes(gzipHeader);
                    break;
                }
                case ZLIB: {
                    out.ensureWritable(sizeEstimate + 2);
                    break;
                }
                default: {
                    out.ensureWritable(sizeEstimate);
                    break;
                }
            }
        } else {
            out.ensureWritable(sizeEstimate);
        }
        if (this.wrapper == ZlibWrapper.GZIP) {
            this.crc.update(inAry);
        }
        this.deflater.setInput(inAry);
        while (!this.deflater.needsInput()) {
            this.deflate(out);
        }
    }

    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ChannelFuture f = this.finishEncode(ctx, ctx.newPromise());
        f.addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                ctx.close(promise);
            }
        });
        if (!f.isDone()) {
            ctx.executor().schedule(new Runnable(){

                @Override
                public void run() {
                    ctx.close(promise);
                }
            }, 10L, TimeUnit.SECONDS);
        }
    }

    private ChannelFuture finishEncode(ChannelHandlerContext ctx, ChannelPromise promise) {
        if (this.finished) {
            promise.setSuccess();
            return promise;
        }
        this.finished = true;
        ByteBuf footer = ctx.alloc().buffer();
        if (this.writeHeader && this.wrapper == ZlibWrapper.GZIP) {
            this.writeHeader = false;
            footer.writeBytes(gzipHeader);
        }
        this.deflater.finish();
        while (!this.deflater.finished()) {
            this.deflate(footer);
        }
        if (this.wrapper == ZlibWrapper.GZIP) {
            int crcValue = (int)this.crc.getValue();
            int uncBytes = this.deflater.getTotalIn();
            footer.writeByte(crcValue);
            footer.writeByte(crcValue >>> 8);
            footer.writeByte(crcValue >>> 16);
            footer.writeByte(crcValue >>> 24);
            footer.writeByte(uncBytes);
            footer.writeByte(uncBytes >>> 8);
            footer.writeByte(uncBytes >>> 16);
            footer.writeByte(uncBytes >>> 24);
        }
        this.deflater.end();
        return ctx.writeAndFlush(footer, promise);
    }

    private void deflate(ByteBuf out) {
        int numBytes;
        do {
            numBytes = this.deflater.deflate(this.encodeBuf, 0, this.encodeBuf.length, 2);
            out.writeBytes(this.encodeBuf, 0, numBytes);
        } while (numBytes > 0);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
}


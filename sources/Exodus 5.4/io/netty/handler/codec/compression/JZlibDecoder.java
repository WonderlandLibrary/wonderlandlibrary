/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.jcraft.jzlib.Inflater
 *  com.jcraft.jzlib.JZlib
 */
package io.netty.handler.codec.compression;

import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.compression.ZlibDecoder;
import io.netty.handler.codec.compression.ZlibUtil;
import io.netty.handler.codec.compression.ZlibWrapper;
import java.util.List;

public class JZlibDecoder
extends ZlibDecoder {
    private final Inflater z = new Inflater();
    private volatile boolean finished;
    private byte[] dictionary;

    public JZlibDecoder(ZlibWrapper zlibWrapper) {
        if (zlibWrapper == null) {
            throw new NullPointerException("wrapper");
        }
        int n = this.z.init(ZlibUtil.convertWrapperType(zlibWrapper));
        if (n != 0) {
            ZlibUtil.fail(this.z, "initialization failure", n);
        }
    }

    public JZlibDecoder(byte[] byArray) {
        if (byArray == null) {
            throw new NullPointerException("dictionary");
        }
        this.dictionary = byArray;
        int n = this.z.inflateInit(JZlib.W_ZLIB);
        if (n != 0) {
            ZlibUtil.fail(this.z, "initialization failure", n);
        }
    }

    @Override
    public boolean isClosed() {
        return this.finished;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int n;
        if (this.finished) {
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }
        if (!byteBuf.isReadable()) {
            return;
        }
        this.z.avail_in = n = byteBuf.readableBytes();
        if (byteBuf.hasArray()) {
            this.z.next_in = byteBuf.array();
            this.z.next_in_index = byteBuf.arrayOffset() + byteBuf.readerIndex();
        } else {
            byte[] byArray = new byte[n];
            byteBuf.getBytes(byteBuf.readerIndex(), byArray);
            this.z.next_in = byArray;
            this.z.next_in_index = 0;
        }
        int n2 = this.z.next_in_index;
        int n3 = n << 1;
        ByteBuf byteBuf2 = channelHandlerContext.alloc().heapBuffer(n3);
        block6: while (true) {
            this.z.avail_out = n3;
            byteBuf2.ensureWritable(n3);
            this.z.next_out = byteBuf2.array();
            int n4 = this.z.next_out_index = byteBuf2.arrayOffset() + byteBuf2.writerIndex();
            int n5 = this.z.inflate(2);
            int n6 = this.z.next_out_index - n4;
            if (n6 > 0) {
                byteBuf2.writerIndex(byteBuf2.writerIndex() + n6);
            }
            switch (n5) {
                case 2: {
                    if (this.dictionary == null) {
                        ZlibUtil.fail(this.z, "decompression failure", n5);
                        continue block6;
                    }
                    n5 = this.z.inflateSetDictionary(this.dictionary, this.dictionary.length);
                    if (n5 == 0) continue block6;
                    ZlibUtil.fail(this.z, "failed to set the dictionary", n5);
                    continue block6;
                }
                case 1: {
                    this.finished = true;
                    this.z.inflateEnd();
                    break block6;
                }
                case 0: {
                    continue block6;
                }
                case -5: {
                    if (this.z.avail_in > 0) continue block6;
                    break block6;
                }
                default: {
                    ZlibUtil.fail(this.z, "decompression failure", n5);
                    continue block6;
                }
            }
            break;
        }
        byteBuf.skipBytes(this.z.next_in_index - n2);
        if (byteBuf2.isReadable()) {
            list.add(byteBuf2);
        } else {
            byteBuf2.release();
        }
        this.z.next_in = null;
        this.z.next_out = null;
    }

    public JZlibDecoder() {
        this(ZlibWrapper.ZLIB);
    }
}


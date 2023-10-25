/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoderBuffer;
import io.netty.util.Signal;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import java.util.List;

public abstract class ReplayingDecoder<S>
extends ByteToMessageDecoder {
    private int checkpoint = -1;
    private final ReplayingDecoderBuffer replayable = new ReplayingDecoderBuffer();
    private S state;
    static final Signal REPLAY = Signal.valueOf(ReplayingDecoder.class.getName() + ".REPLAY");

    protected ReplayingDecoder(S s) {
        this.state = s;
    }

    protected S state(S s) {
        S s2 = this.state;
        this.state = s;
        return s2;
    }

    protected S state() {
        return this.state;
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        RecyclableArrayList recyclableArrayList;
        block9: {
            recyclableArrayList = RecyclableArrayList.newInstance();
            try {
                this.replayable.terminate();
                this.callDecode(channelHandlerContext, this.internalBuffer(), recyclableArrayList);
                this.decodeLast(channelHandlerContext, this.replayable, recyclableArrayList);
                if (this.cumulation == null) break block9;
            }
            catch (Signal signal) {
                signal.expect(REPLAY);
                if (this.cumulation != null) {
                    this.cumulation.release();
                    this.cumulation = null;
                }
                int n = recyclableArrayList.size();
                for (int i = 0; i < n; ++i) {
                    channelHandlerContext.fireChannelRead(recyclableArrayList.get(i));
                }
                if (n > 0) {
                    channelHandlerContext.fireChannelReadComplete();
                }
                channelHandlerContext.fireChannelInactive();
                recyclableArrayList.recycle();
            }
            catch (DecoderException decoderException) {
                throw decoderException;
            }
            catch (Exception exception) {
                throw new DecoderException(exception);
            }
            this.cumulation.release();
            this.cumulation = null;
        }
        int n = recyclableArrayList.size();
        for (int i = 0; i < n; ++i) {
            channelHandlerContext.fireChannelRead(recyclableArrayList.get(i));
        }
        if (n > 0) {
            channelHandlerContext.fireChannelReadComplete();
        }
        channelHandlerContext.fireChannelInactive();
        recyclableArrayList.recycle();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void callDecode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        this.replayable.setCumulation(byteBuf);
        try {
            while (byteBuf.isReadable()) {
                int n = this.checkpoint = byteBuf.readerIndex();
                int n2 = list.size();
                S s = this.state;
                int n3 = byteBuf.readableBytes();
                try {
                    this.decode(channelHandlerContext, this.replayable, list);
                    if (channelHandlerContext.isRemoved()) {
                        return;
                    }
                    if (n2 == list.size()) {
                        if (n3 != byteBuf.readableBytes() || s != this.state) continue;
                        throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() must consume the inbound " + "data or change its state if it did not decode anything.");
                    }
                }
                catch (Signal signal) {
                    signal.expect(REPLAY);
                    if (channelHandlerContext.isRemoved()) {
                        return;
                    }
                    int n4 = this.checkpoint;
                    if (n4 < 0) return;
                    byteBuf.readerIndex(n4);
                    return;
                }
                if (n == byteBuf.readerIndex() && s == this.state) {
                    throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() method must consume the inbound data " + "or change its state if it decoded something.");
                }
                if (this.isSingleDecode()) return;
            }
            return;
        }
        catch (DecoderException decoderException) {
            throw decoderException;
        }
        catch (Throwable throwable) {
            throw new DecoderException(throwable);
        }
    }

    protected void checkpoint(S s) {
        this.checkpoint();
        this.state(s);
    }

    protected void checkpoint() {
        this.checkpoint = this.internalBuffer().readerIndex();
    }

    protected ReplayingDecoder() {
        this(null);
    }
}


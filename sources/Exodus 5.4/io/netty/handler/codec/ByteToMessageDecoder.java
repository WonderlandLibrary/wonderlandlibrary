/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import java.util.List;

public abstract class ByteToMessageDecoder
extends ChannelInboundHandlerAdapter {
    ByteBuf cumulation;
    private boolean singleDecode;
    private boolean decodeWasNull;
    private boolean first;

    protected abstract void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception;

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        block9: {
            block8: {
                int n;
                if (!(object instanceof ByteBuf)) break block8;
                RecyclableArrayList recyclableArrayList = RecyclableArrayList.newInstance();
                try {
                    ByteBuf byteBuf = (ByteBuf)object;
                    boolean bl = this.first = this.cumulation == null;
                    if (this.first) {
                        this.cumulation = byteBuf;
                    } else {
                        if (this.cumulation.writerIndex() > this.cumulation.maxCapacity() - byteBuf.readableBytes() || this.cumulation.refCnt() > 1) {
                            this.expandCumulation(channelHandlerContext, byteBuf.readableBytes());
                        }
                        this.cumulation.writeBytes(byteBuf);
                        byteBuf.release();
                    }
                    this.callDecode(channelHandlerContext, this.cumulation, recyclableArrayList);
                }
                catch (DecoderException decoderException) {
                    throw decoderException;
                }
                catch (Throwable throwable) {
                    throw new DecoderException(throwable);
                }
                if (this.cumulation != null && !this.cumulation.isReadable()) {
                    this.cumulation.release();
                    this.cumulation = null;
                }
                this.decodeWasNull = (n = recyclableArrayList.size()) == 0;
                for (int i = 0; i < n; ++i) {
                    channelHandlerContext.fireChannelRead(recyclableArrayList.get(i));
                }
                recyclableArrayList.recycle();
                break block9;
            }
            channelHandlerContext.fireChannelRead(object);
        }
    }

    protected void decodeLast(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        this.decode(channelHandlerContext, byteBuf, list);
    }

    @Override
    public final void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        ByteBuf byteBuf = this.internalBuffer();
        int n = byteBuf.readableBytes();
        if (byteBuf.isReadable()) {
            ByteBuf byteBuf2 = byteBuf.readBytes(n);
            byteBuf.release();
            channelHandlerContext.fireChannelRead(byteBuf2);
        } else {
            byteBuf.release();
        }
        this.cumulation = null;
        channelHandlerContext.fireChannelReadComplete();
        this.handlerRemoved0(channelHandlerContext);
    }

    protected void handlerRemoved0(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    protected int actualReadableBytes() {
        return this.internalBuffer().readableBytes();
    }

    private void expandCumulation(ChannelHandlerContext channelHandlerContext, int n) {
        ByteBuf byteBuf = this.cumulation;
        this.cumulation = channelHandlerContext.alloc().buffer(byteBuf.readableBytes() + n);
        this.cumulation.writeBytes(byteBuf);
        byteBuf.release();
    }

    public boolean isSingleDecode() {
        return this.singleDecode;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.cumulation != null && !this.first && this.cumulation.refCnt() == 1) {
            this.cumulation.discardSomeReadBytes();
        }
        if (this.decodeWasNull) {
            this.decodeWasNull = false;
            if (!channelHandlerContext.channel().config().isAutoRead()) {
                channelHandlerContext.read();
            }
        }
        channelHandlerContext.fireChannelReadComplete();
    }

    public void setSingleDecode(boolean bl) {
        this.singleDecode = bl;
    }

    protected ByteBuf internalBuffer() {
        if (this.cumulation != null) {
            return this.cumulation;
        }
        return Unpooled.EMPTY_BUFFER;
    }

    protected void callDecode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        try {
            while (byteBuf.isReadable()) {
                int n = list.size();
                int n2 = byteBuf.readableBytes();
                this.decode(channelHandlerContext, byteBuf, list);
                if (!channelHandlerContext.isRemoved()) {
                    if (n == list.size()) {
                        if (n2 != byteBuf.readableBytes()) continue;
                    } else {
                        if (n2 == byteBuf.readableBytes()) {
                            throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() did not read anything but decoded a message.");
                        }
                        if (!this.isSingleDecode()) continue;
                    }
                }
                break;
            }
        }
        catch (DecoderException decoderException) {
            throw decoderException;
        }
        catch (Throwable throwable) {
            throw new DecoderException(throwable);
        }
    }

    protected ByteToMessageDecoder() {
        if (this.isSharable()) {
            throw new IllegalStateException("@Sharable annotation is not allowed");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        RecyclableArrayList recyclableArrayList;
        block7: {
            recyclableArrayList = RecyclableArrayList.newInstance();
            try {
                if (this.cumulation != null) {
                    this.callDecode(channelHandlerContext, this.cumulation, recyclableArrayList);
                    this.decodeLast(channelHandlerContext, this.cumulation, recyclableArrayList);
                } else {
                    this.decodeLast(channelHandlerContext, Unpooled.EMPTY_BUFFER, recyclableArrayList);
                }
                if (this.cumulation == null) break block7;
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
}


/*
 * Decompiled with CFR 0.150.
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
    static final Signal REPLAY = Signal.valueOf(ReplayingDecoder.class.getName() + ".REPLAY");
    private final ReplayingDecoderBuffer replayable = new ReplayingDecoderBuffer();
    private S state;
    private int checkpoint = -1;

    protected ReplayingDecoder() {
        this(null);
    }

    protected ReplayingDecoder(S initialState) {
        this.state = initialState;
    }

    protected void checkpoint() {
        this.checkpoint = this.internalBuffer().readerIndex();
    }

    protected void checkpoint(S state) {
        this.checkpoint();
        this.state(state);
    }

    protected S state() {
        return this.state;
    }

    protected S state(S newState) {
        S oldState = this.state;
        this.state = newState;
        return oldState;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        RecyclableArrayList out = RecyclableArrayList.newInstance();
        try {
            this.replayable.terminate();
            this.callDecode(ctx, this.internalBuffer(), out);
            this.decodeLast(ctx, this.replayable, out);
        }
        catch (Signal replay) {
            replay.expect(REPLAY);
        }
        catch (DecoderException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DecoderException(e);
        }
        finally {
            if (this.cumulation != null) {
                this.cumulation.release();
                this.cumulation = null;
            }
            int size = out.size();
            for (int i = 0; i < size; ++i) {
                ctx.fireChannelRead(out.get(i));
            }
            ctx.fireChannelInactive();
            out.recycle();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        this.replayable.setCumulation(in);
        try {
            do lbl-1000:
            // 3 sources

            {
                block8: {
                    if (in.isReadable() == false) return;
                    oldReaderIndex = this.checkpoint = in.readerIndex();
                    outSize = out.size();
                    oldState = this.state;
                    oldInputLength = in.readableBytes();
                    try {
                        this.decode(ctx, this.replayable, out);
                        if (ctx.isRemoved()) {
                            return;
                        }
                        if (outSize != out.size()) break block8;
                        if (oldInputLength != in.readableBytes() || oldState != this.state) ** GOTO lbl-1000
                        throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() must consume the inbound " + "data or change its state if it did not decode anything.");
                    }
                    catch (Signal replay) {
                        replay.expect(ReplayingDecoder.REPLAY);
                        if (ctx.isRemoved()) {
                            return;
                        }
                        checkpoint = this.checkpoint;
                        if (checkpoint < 0) return;
                        in.readerIndex(checkpoint);
                        return;
                    }
                }
                if (oldReaderIndex != in.readerIndex() || oldState != this.state) continue;
                throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() method must consume the inbound data " + "or change its state if it decoded something.");
            } while (!this.isSingleDecode());
            return;
        }
        catch (DecoderException e) {
            throw e;
        }
        catch (Throwable cause) {
            throw new DecoderException(cause);
        }
    }
}


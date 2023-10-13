/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

public abstract class MessageToMessageEncoder<I>
extends ChannelOutboundHandlerAdapter {
    private final TypeParameterMatcher matcher;

    protected MessageToMessageEncoder() {
        this.matcher = TypeParameterMatcher.find(this, MessageToMessageEncoder.class, "I");
    }

    protected MessageToMessageEncoder(Class<? extends I> outboundMessageType) {
        this.matcher = TypeParameterMatcher.get(outboundMessageType);
    }

    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return this.matcher.match(msg);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        block15: {
            RecyclableArrayList out = null;
            try {
                if (this.acceptOutboundMessage(msg)) {
                    out = RecyclableArrayList.newInstance();
                    Object cast = msg;
                    try {
                        this.encode(ctx, cast, out);
                    }
                    finally {
                        ReferenceCountUtil.release(cast);
                    }
                    if (out.isEmpty()) {
                        out.recycle();
                        out = null;
                        throw new EncoderException(StringUtil.simpleClassName(this) + " must produce at least one message.");
                    }
                    break block15;
                }
                ctx.write(msg, promise);
            }
            catch (EncoderException e) {
                throw e;
            }
            catch (Throwable t) {
                throw new EncoderException(t);
            }
            finally {
                if (out != null) {
                    int sizeMinusOne = out.size() - 1;
                    if (sizeMinusOne >= 0) {
                        for (int i = 0; i < sizeMinusOne; ++i) {
                            ctx.write(out.get(i));
                        }
                        ctx.write(out.get(sizeMinusOne), promise);
                    }
                    out.recycle();
                }
            }
        }
    }

    protected abstract void encode(ChannelHandlerContext var1, I var2, List<Object> var3) throws Exception;
}


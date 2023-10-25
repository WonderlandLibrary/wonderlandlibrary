/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

public abstract class MessageToMessageDecoder<I>
extends ChannelInboundHandlerAdapter {
    private final TypeParameterMatcher matcher;

    protected MessageToMessageDecoder() {
        this.matcher = TypeParameterMatcher.find(this, MessageToMessageDecoder.class, "I");
    }

    protected abstract void decode(ChannelHandlerContext var1, I var2, List<Object> var3) throws Exception;

    protected MessageToMessageDecoder(Class<? extends I> clazz) {
        this.matcher = TypeParameterMatcher.get(clazz);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        RecyclableArrayList recyclableArrayList;
        block5: {
            recyclableArrayList = RecyclableArrayList.newInstance();
            try {
                if (this.acceptInboundMessage(object)) {
                    Object object2 = object;
                    this.decode(channelHandlerContext, object2, recyclableArrayList);
                    ReferenceCountUtil.release(object2);
                    break block5;
                }
                recyclableArrayList.add(object);
            }
            catch (DecoderException decoderException) {
                throw decoderException;
            }
            catch (Exception exception) {
                throw new DecoderException(exception);
            }
        }
        int n = recyclableArrayList.size();
        for (int i = 0; i < n; ++i) {
            channelHandlerContext.fireChannelRead(recyclableArrayList.get(i));
        }
        recyclableArrayList.recycle();
    }

    public boolean acceptInboundMessage(Object object) throws Exception {
        return this.matcher.match(object);
    }
}


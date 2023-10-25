/*
 * Decompiled with CFR 0.152.
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        RecyclableArrayList recyclableArrayList = null;
        try {
            if (this.acceptOutboundMessage(object)) {
                recyclableArrayList = RecyclableArrayList.newInstance();
                Object object2 = object;
                this.encode(channelHandlerContext, object2, recyclableArrayList);
                ReferenceCountUtil.release(object2);
                if (recyclableArrayList.isEmpty()) {
                    recyclableArrayList.recycle();
                    recyclableArrayList = null;
                    throw new EncoderException(StringUtil.simpleClassName(this) + " must produce at least one message.");
                }
            } else {
                channelHandlerContext.write(object, channelPromise);
            }
            if (recyclableArrayList == null) return;
        }
        catch (EncoderException encoderException) {
            throw encoderException;
        }
        catch (Throwable throwable) {
            throw new EncoderException(throwable);
        }
        int n = recyclableArrayList.size() - 1;
        if (n == 0) {
            channelHandlerContext.write(recyclableArrayList.get(0), channelPromise);
        } else if (n > 0) {
            ChannelPromise channelPromise2 = channelHandlerContext.voidPromise();
            boolean bl = channelPromise == channelPromise2;
            for (int i = 0; i < n; ++i) {
                ChannelPromise channelPromise3 = bl ? channelPromise2 : channelHandlerContext.newPromise();
                channelHandlerContext.write(recyclableArrayList.get(i), channelPromise3);
            }
            channelHandlerContext.write(recyclableArrayList.get(n), channelPromise);
        }
        recyclableArrayList.recycle();
        return;
    }

    public boolean acceptOutboundMessage(Object object) throws Exception {
        return this.matcher.match(object);
    }

    protected MessageToMessageEncoder() {
        this.matcher = TypeParameterMatcher.find(this, MessageToMessageEncoder.class, "I");
    }

    protected abstract void encode(ChannelHandlerContext var1, I var2, List<Object> var3) throws Exception;

    protected MessageToMessageEncoder(Class<? extends I> clazz) {
        this.matcher = TypeParameterMatcher.get(clazz);
    }
}


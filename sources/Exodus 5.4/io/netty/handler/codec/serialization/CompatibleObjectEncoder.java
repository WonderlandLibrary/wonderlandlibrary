/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class CompatibleObjectEncoder
extends MessageToByteEncoder<Serializable> {
    private static final AttributeKey<ObjectOutputStream> OOS = AttributeKey.valueOf(CompatibleObjectEncoder.class.getName() + ".OOS");
    private int writtenObjects;
    private final int resetInterval;

    public CompatibleObjectEncoder(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("resetInterval: " + n);
        }
        this.resetInterval = n;
    }

    protected ObjectOutputStream newObjectOutputStream(OutputStream outputStream) throws Exception {
        return new ObjectOutputStream(outputStream);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Serializable serializable, ByteBuf byteBuf) throws Exception {
        ObjectOutputStream objectOutputStream;
        Attribute<ObjectOutputStream> attribute = channelHandlerContext.attr(OOS);
        ObjectOutputStream objectOutputStream2 = attribute.get();
        if (objectOutputStream2 == null && (objectOutputStream = attribute.setIfAbsent(objectOutputStream2 = this.newObjectOutputStream(new ByteBufOutputStream(byteBuf)))) != null) {
            objectOutputStream2 = objectOutputStream;
        }
        objectOutputStream = objectOutputStream2;
        synchronized (objectOutputStream) {
            if (this.resetInterval != 0) {
                ++this.writtenObjects;
                if (this.writtenObjects % this.resetInterval == 0) {
                    objectOutputStream2.reset();
                }
            }
            objectOutputStream2.writeObject(serializable);
            objectOutputStream2.flush();
        }
    }

    public CompatibleObjectEncoder() {
        this(16);
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.ByteToMessageDecoder
 *  io.netty.handler.codec.MessageToByteEncoder
 */
package com.viaversion.viaversion.classgenerator.generated;

import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

public interface HandlerConstructor {
    public MessageToByteEncoder newEncodeHandler(UserConnection var1, MessageToByteEncoder var2);

    public ByteToMessageDecoder newDecodeHandler(UserConnection var1, ByteToMessageDecoder var2);
}


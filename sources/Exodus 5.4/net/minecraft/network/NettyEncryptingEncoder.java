/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;
import net.minecraft.network.NettyEncryptionTranslator;

public class NettyEncryptingEncoder
extends MessageToByteEncoder<ByteBuf> {
    private final NettyEncryptionTranslator encryptionCodec;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) throws ShortBufferException, Exception {
        this.encryptionCodec.cipher(byteBuf, byteBuf2);
    }

    public NettyEncryptingEncoder(Cipher cipher) {
        this.encryptionCodec = new NettyEncryptionTranslator(cipher);
    }
}


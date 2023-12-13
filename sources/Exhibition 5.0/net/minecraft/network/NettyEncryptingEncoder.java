// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network;

import javax.crypto.ShortBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncryptingEncoder extends MessageToByteEncoder
{
    private final NettyEncryptionTranslator encryptionCodec;
    private static final String __OBFID = "CL_00001239";
    
    public NettyEncryptingEncoder(final Cipher cipher) {
        this.encryptionCodec = new NettyEncryptionTranslator(cipher);
    }
    
    protected void encode(final ChannelHandlerContext p_encode_1_, final ByteBuf p_encode_2_, final ByteBuf p_encode_3_) throws ShortBufferException {
        this.encryptionCodec.cipher(p_encode_2_, p_encode_3_);
    }
    
    protected void encode(final ChannelHandlerContext p_encode_1_, final Object p_encode_2_, final ByteBuf p_encode_3_) throws ShortBufferException {
        this.encode(p_encode_1_, (ByteBuf)p_encode_2_, p_encode_3_);
    }
}

// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network;

import javax.crypto.ShortBufferException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import io.netty.handler.codec.MessageToMessageDecoder;

public class NettyEncryptingDecoder extends MessageToMessageDecoder
{
    private final NettyEncryptionTranslator decryptionCodec;
    private static final String __OBFID = "CL_00001238";
    
    public NettyEncryptingDecoder(final Cipher cipher) {
        this.decryptionCodec = new NettyEncryptionTranslator(cipher);
    }
    
    protected void decode(final ChannelHandlerContext p_decode_1_, final ByteBuf p_decode_2_, final List p_decode_3_) throws ShortBufferException {
        p_decode_3_.add(this.decryptionCodec.decipher(p_decode_1_, p_decode_2_));
    }
    
    protected void decode(final ChannelHandlerContext p_decode_1_, final Object p_decode_2_, final List p_decode_3_) throws ShortBufferException {
        this.decode(p_decode_1_, (ByteBuf)p_decode_2_, p_decode_3_);
    }
}

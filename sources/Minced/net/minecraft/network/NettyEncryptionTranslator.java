// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import javax.crypto.ShortBufferException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import javax.crypto.Cipher;

public class NettyEncryptionTranslator
{
    private final Cipher cipher;
    private byte[] inputBuffer;
    private byte[] outputBuffer;
    
    protected NettyEncryptionTranslator(final Cipher cipherIn) {
        this.inputBuffer = new byte[0];
        this.outputBuffer = new byte[0];
        this.cipher = cipherIn;
    }
    
    private byte[] bufToBytes(final ByteBuf buf) {
        final int i = buf.readableBytes();
        if (this.inputBuffer.length < i) {
            this.inputBuffer = new byte[i];
        }
        buf.readBytes(this.inputBuffer, 0, i);
        return this.inputBuffer;
    }
    
    protected ByteBuf decipher(final ChannelHandlerContext ctx, final ByteBuf buffer) throws ShortBufferException {
        final int i = buffer.readableBytes();
        final byte[] abyte = this.bufToBytes(buffer);
        final ByteBuf bytebuf = ctx.alloc().heapBuffer(this.cipher.getOutputSize(i));
        bytebuf.writerIndex(this.cipher.update(abyte, 0, i, bytebuf.array(), bytebuf.arrayOffset()));
        return bytebuf;
    }
    
    protected void cipher(final ByteBuf in, final ByteBuf out) throws ShortBufferException {
        final int i = in.readableBytes();
        final byte[] abyte = this.bufToBytes(in);
        final int j = this.cipher.getOutputSize(i);
        if (this.outputBuffer.length < j) {
            this.outputBuffer = new byte[j];
        }
        out.writeBytes(this.outputBuffer, 0, this.cipher.update(abyte, 0, i, this.outputBuffer));
    }
}

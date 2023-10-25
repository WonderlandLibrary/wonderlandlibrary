/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class NettyEncryptionTranslator {
    private byte[] field_150505_b = new byte[0];
    private byte[] field_150506_c = new byte[0];
    private final Cipher cipher;

    protected void cipher(ByteBuf byteBuf, ByteBuf byteBuf2) throws ShortBufferException {
        int n = byteBuf.readableBytes();
        byte[] byArray = this.func_150502_a(byteBuf);
        int n2 = this.cipher.getOutputSize(n);
        if (this.field_150506_c.length < n2) {
            this.field_150506_c = new byte[n2];
        }
        byteBuf2.writeBytes(this.field_150506_c, 0, this.cipher.update(byArray, 0, n, this.field_150506_c));
    }

    protected NettyEncryptionTranslator(Cipher cipher) {
        this.cipher = cipher;
    }

    protected ByteBuf decipher(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws ShortBufferException {
        int n = byteBuf.readableBytes();
        byte[] byArray = this.func_150502_a(byteBuf);
        ByteBuf byteBuf2 = channelHandlerContext.alloc().heapBuffer(this.cipher.getOutputSize(n));
        byteBuf2.writerIndex(this.cipher.update(byArray, 0, n, byteBuf2.array(), byteBuf2.arrayOffset()));
        return byteBuf2;
    }

    private byte[] func_150502_a(ByteBuf byteBuf) {
        int n = byteBuf.readableBytes();
        if (this.field_150505_b.length < n) {
            this.field_150505_b = new byte[n];
        }
        byteBuf.readBytes(this.field_150505_b, 0, n);
        return this.field_150505_b;
    }
}


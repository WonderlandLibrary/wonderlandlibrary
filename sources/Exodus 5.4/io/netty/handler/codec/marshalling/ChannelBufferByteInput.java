/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jboss.marshalling.ByteInput
 */
package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import org.jboss.marshalling.ByteInput;

class ChannelBufferByteInput
implements ByteInput {
    private final ByteBuf buffer;

    public long skip(long l) throws IOException {
        int n = this.buffer.readableBytes();
        if ((long)n < l) {
            l = n;
        }
        this.buffer.readerIndex((int)((long)this.buffer.readerIndex() + l));
        return l;
    }

    public int read(byte[] byArray, int n, int n2) throws IOException {
        int n3 = this.available();
        if (n3 == 0) {
            return -1;
        }
        n2 = Math.min(n3, n2);
        this.buffer.readBytes(byArray, n, n2);
        return n2;
    }

    ChannelBufferByteInput(ByteBuf byteBuf) {
        this.buffer = byteBuf;
    }

    public void close() throws IOException {
    }

    public int available() throws IOException {
        return this.buffer.readableBytes();
    }

    public int read() throws IOException {
        if (this.buffer.isReadable()) {
            return this.buffer.readByte() & 0xFF;
        }
        return -1;
    }

    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }
}


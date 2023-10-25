/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jboss.marshalling.ByteOutput
 */
package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import org.jboss.marshalling.ByteOutput;

class ChannelBufferByteOutput
implements ByteOutput {
    private final ByteBuf buffer;

    public void close() throws IOException {
    }

    ChannelBufferByteOutput(ByteBuf byteBuf) {
        this.buffer = byteBuf;
    }

    public void write(byte[] byArray, int n, int n2) throws IOException {
        this.buffer.writeBytes(byArray, n, n2);
    }

    public void flush() throws IOException {
    }

    ByteBuf getBuffer() {
        return this.buffer;
    }

    public void write(int n) throws IOException {
        this.buffer.writeByte(n);
    }

    public void write(byte[] byArray) throws IOException {
        this.buffer.writeBytes(byArray);
    }
}


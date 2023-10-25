/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.FileRegion;
import io.netty.channel.oio.AbstractOioByteChannel;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.WritableByteChannel;

public abstract class OioByteStreamChannel
extends AbstractOioByteChannel {
    private static final InputStream CLOSED_IN = new InputStream(){

        @Override
        public int read() {
            return -1;
        }
    };
    private static final OutputStream CLOSED_OUT = new OutputStream(){

        @Override
        public void write(int n) throws IOException {
            throw new ClosedChannelException();
        }
    };
    private InputStream is;
    private OutputStream os;
    private WritableByteChannel outChannel;

    @Override
    protected void doWriteFileRegion(FileRegion fileRegion) throws Exception {
        long l;
        OutputStream outputStream = this.os;
        if (outputStream == null) {
            throw new NotYetConnectedException();
        }
        if (this.outChannel == null) {
            this.outChannel = Channels.newChannel(outputStream);
        }
        long l2 = 0L;
        do {
            if ((l = fileRegion.transferTo(this.outChannel, l2)) != -1L) continue;
            OioByteStreamChannel.checkEOF(fileRegion);
            return;
        } while ((l2 += l) < fileRegion.count());
    }

    @Override
    protected int doReadBytes(ByteBuf byteBuf) throws Exception {
        int n = Math.max(1, Math.min(this.available(), byteBuf.maxWritableBytes()));
        return byteBuf.writeBytes(this.is, n);
    }

    protected final void activate(InputStream inputStream, OutputStream outputStream) {
        if (this.is != null) {
            throw new IllegalStateException("input was set already");
        }
        if (this.os != null) {
            throw new IllegalStateException("output was set already");
        }
        if (inputStream == null) {
            throw new NullPointerException("is");
        }
        if (outputStream == null) {
            throw new NullPointerException("os");
        }
        this.is = inputStream;
        this.os = outputStream;
    }

    protected OioByteStreamChannel(Channel channel) {
        super(channel);
    }

    @Override
    public boolean isActive() {
        InputStream inputStream = this.is;
        if (inputStream == null || inputStream == CLOSED_IN) {
            return false;
        }
        OutputStream outputStream = this.os;
        return outputStream != null && outputStream != CLOSED_OUT;
    }

    @Override
    protected void doWriteBytes(ByteBuf byteBuf) throws Exception {
        OutputStream outputStream = this.os;
        if (outputStream == null) {
            throw new NotYetConnectedException();
        }
        byteBuf.readBytes(outputStream, byteBuf.readableBytes());
    }

    @Override
    protected int available() {
        try {
            return this.is.available();
        }
        catch (IOException iOException) {
            return 0;
        }
    }

    @Override
    protected void doClose() throws Exception {
        block1: {
            InputStream inputStream = this.is;
            OutputStream outputStream = this.os;
            this.is = CLOSED_IN;
            this.os = CLOSED_OUT;
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream == null) break block1;
            outputStream.close();
        }
    }

    private static void checkEOF(FileRegion fileRegion) throws IOException {
        if (fileRegion.transfered() < fileRegion.count()) {
            throw new EOFException("Expected to be able to write " + fileRegion.count() + " bytes, " + "but only wrote " + fileRegion.transfered());
        }
    }
}


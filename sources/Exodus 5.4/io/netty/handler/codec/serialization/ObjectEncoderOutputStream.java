/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.serialization.CompactObjectOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;

public class ObjectEncoderOutputStream
extends OutputStream
implements ObjectOutput {
    private final DataOutputStream out;
    private final int estimatedLength;

    @Override
    public final void writeFloat(float f) throws IOException {
        this.out.writeFloat(f);
    }

    @Override
    public void write(int n) throws IOException {
        this.out.write(n);
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public void writeObject(Object object) throws IOException {
        ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(Unpooled.buffer(this.estimatedLength));
        CompactObjectOutputStream compactObjectOutputStream = new CompactObjectOutputStream(byteBufOutputStream);
        compactObjectOutputStream.writeObject(object);
        compactObjectOutputStream.flush();
        compactObjectOutputStream.close();
        ByteBuf byteBuf = byteBufOutputStream.buffer();
        int n = byteBuf.readableBytes();
        this.writeInt(n);
        byteBuf.getBytes(0, this, n);
    }

    @Override
    public void write(byte[] byArray, int n, int n2) throws IOException {
        this.out.write(byArray, n, n2);
    }

    @Override
    public final void writeUTF(String string) throws IOException {
        this.out.writeUTF(string);
    }

    @Override
    public void write(byte[] byArray) throws IOException {
        this.out.write(byArray);
    }

    public ObjectEncoderOutputStream(OutputStream outputStream, int n) {
        if (outputStream == null) {
            throw new NullPointerException("out");
        }
        if (n < 0) {
            throw new IllegalArgumentException("estimatedLength: " + n);
        }
        this.out = outputStream instanceof DataOutputStream ? (DataOutputStream)outputStream : new DataOutputStream(outputStream);
        this.estimatedLength = n;
    }

    @Override
    public final void writeBytes(String string) throws IOException {
        this.out.writeBytes(string);
    }

    @Override
    public final void writeChars(String string) throws IOException {
        this.out.writeChars(string);
    }

    public ObjectEncoderOutputStream(OutputStream outputStream) {
        this(outputStream, 512);
    }

    @Override
    public final void writeShort(int n) throws IOException {
        this.out.writeShort(n);
    }

    public final int size() {
        return this.out.size();
    }

    @Override
    public final void writeLong(long l) throws IOException {
        this.out.writeLong(l);
    }

    @Override
    public final void writeDouble(double d) throws IOException {
        this.out.writeDouble(d);
    }

    @Override
    public final void writeBoolean(boolean bl) throws IOException {
        this.out.writeBoolean(bl);
    }

    @Override
    public final void writeByte(int n) throws IOException {
        this.out.writeByte(n);
    }

    @Override
    public final void writeChar(int n) throws IOException {
        this.out.writeChar(n);
    }

    @Override
    public final void writeInt(int n) throws IOException {
        this.out.writeInt(n);
    }

    @Override
    public void close() throws IOException {
        this.out.close();
    }
}


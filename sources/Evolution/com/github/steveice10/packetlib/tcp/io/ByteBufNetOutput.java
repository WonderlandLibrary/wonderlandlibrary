/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package com.github.steveice10.packetlib.tcp.io;

import com.github.steveice10.packetlib.io.NetOutput;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.UUID;

public class ByteBufNetOutput
implements NetOutput {
    private ByteBuf buf;

    public ByteBufNetOutput(ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        this.buf.writeBoolean(b);
    }

    @Override
    public void writeByte(int b) throws IOException {
        this.buf.writeByte(b);
    }

    @Override
    public void writeShort(int s) throws IOException {
        this.buf.writeShort(s);
    }

    @Override
    public void writeChar(int c2) throws IOException {
        this.buf.writeChar(c2);
    }

    @Override
    public void writeInt(int i) throws IOException {
        this.buf.writeInt(i);
    }

    @Override
    public void writeVarInt(int i) throws IOException {
        while ((i & 0xFFFFFF80) != 0) {
            this.writeByte(i & 0x7F | 0x80);
            i >>>= 7;
        }
        this.writeByte(i);
    }

    @Override
    public void writeLong(long l) throws IOException {
        this.buf.writeLong(l);
    }

    @Override
    public void writeVarLong(long l) throws IOException {
        while ((l & 0xFFFFFFFFFFFFFF80L) != 0L) {
            this.writeByte((int)(l & 0x7FL) | 0x80);
            l >>>= 7;
        }
        this.writeByte((int)l);
    }

    @Override
    public void writeFloat(float f2) throws IOException {
        this.buf.writeFloat(f2);
    }

    @Override
    public void writeDouble(double d) throws IOException {
        this.buf.writeDouble(d);
    }

    @Override
    public void writeBytes(byte[] b) throws IOException {
        this.buf.writeBytes(b);
    }

    @Override
    public void writeBytes(byte[] b, int length) throws IOException {
        this.buf.writeBytes(b, 0, length);
    }

    @Override
    public void writeShorts(short[] s) throws IOException {
        this.writeShorts(s, s.length);
    }

    @Override
    public void writeShorts(short[] s, int length) throws IOException {
        int index = 0;
        while (index < length) {
            this.writeShort(s[index]);
            ++index;
        }
    }

    @Override
    public void writeInts(int[] i) throws IOException {
        this.writeInts(i, i.length);
    }

    @Override
    public void writeInts(int[] i, int length) throws IOException {
        int index = 0;
        while (index < length) {
            this.writeInt(i[index]);
            ++index;
        }
    }

    @Override
    public void writeLongs(long[] l) throws IOException {
        this.writeLongs(l, l.length);
    }

    @Override
    public void writeLongs(long[] l, int length) throws IOException {
        int index = 0;
        while (index < length) {
            this.writeLong(l[index]);
            ++index;
        }
    }

    @Override
    public void writeString(String s) throws IOException {
        if (s == null) {
            throw new IllegalArgumentException("String cannot be null!");
        }
        byte[] bytes = s.getBytes("UTF-8");
        if (bytes.length > Short.MAX_VALUE) {
            throw new IOException("String too big (was " + s.length() + " bytes encoded, max " + Short.MAX_VALUE + ")");
        }
        this.writeVarInt(bytes.length);
        this.writeBytes(bytes);
    }

    @Override
    public void writeUUID(UUID uuid) throws IOException {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }

    @Override
    public void flush() throws IOException {
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.io;

import java.io.IOException;
import java.util.UUID;

public interface NetOutput {
    public void writeBoolean(boolean var1) throws IOException;

    public void writeByte(int var1) throws IOException;

    public void writeShort(int var1) throws IOException;

    public void writeChar(int var1) throws IOException;

    public void writeInt(int var1) throws IOException;

    public void writeVarInt(int var1) throws IOException;

    public void writeLong(long var1) throws IOException;

    public void writeVarLong(long var1) throws IOException;

    public void writeFloat(float var1) throws IOException;

    public void writeDouble(double var1) throws IOException;

    public void writeBytes(byte[] var1) throws IOException;

    public void writeBytes(byte[] var1, int var2) throws IOException;

    public void writeShorts(short[] var1) throws IOException;

    public void writeShorts(short[] var1, int var2) throws IOException;

    public void writeInts(int[] var1) throws IOException;

    public void writeInts(int[] var1, int var2) throws IOException;

    public void writeLongs(long[] var1) throws IOException;

    public void writeLongs(long[] var1, int var2) throws IOException;

    public void writeString(String var1) throws IOException;

    public void writeUUID(UUID var1) throws IOException;

    public void flush() throws IOException;
}


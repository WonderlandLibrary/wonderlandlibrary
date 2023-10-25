/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.io;

import java.io.IOException;
import java.util.UUID;

public interface NetInput {
    public boolean readBoolean() throws IOException;

    public byte readByte() throws IOException;

    public int readUnsignedByte() throws IOException;

    public short readShort() throws IOException;

    public int readUnsignedShort() throws IOException;

    public char readChar() throws IOException;

    public int readInt() throws IOException;

    public int readVarInt() throws IOException;

    public long readLong() throws IOException;

    public long readVarLong() throws IOException;

    public float readFloat() throws IOException;

    public double readDouble() throws IOException;

    public byte[] readBytes(int var1) throws IOException;

    public int readBytes(byte[] var1) throws IOException;

    public int readBytes(byte[] var1, int var2, int var3) throws IOException;

    public short[] readShorts(int var1) throws IOException;

    public int readShorts(short[] var1) throws IOException;

    public int readShorts(short[] var1, int var2, int var3) throws IOException;

    public int[] readInts(int var1) throws IOException;

    public int readInts(int[] var1) throws IOException;

    public int readInts(int[] var1, int var2, int var3) throws IOException;

    public long[] readLongs(int var1) throws IOException;

    public int readLongs(long[] var1) throws IOException;

    public int readLongs(long[] var1, int var2, int var3) throws IOException;

    public String readString() throws IOException;

    public UUID readUUID() throws IOException;

    public int available() throws IOException;
}


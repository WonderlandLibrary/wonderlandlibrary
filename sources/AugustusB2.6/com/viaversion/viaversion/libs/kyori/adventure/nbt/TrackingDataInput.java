// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.DataInput;

final class TrackingDataInput implements DataInput, BinaryTagScope
{
    private static final int MAX_DEPTH = 512;
    private final DataInput input;
    private final long maxLength;
    private long counter;
    private int depth;
    
    TrackingDataInput(final DataInput input, final long maxLength) {
        this.input = input;
        this.maxLength = maxLength;
    }
    
    public static BinaryTagScope enter(final DataInput input) throws IOException {
        if (input instanceof TrackingDataInput) {
            return ((TrackingDataInput)input).enter();
        }
        return NoOp.INSTANCE;
    }
    
    public static BinaryTagScope enter(final DataInput input, final long expectedSize) throws IOException {
        if (input instanceof TrackingDataInput) {
            return ((TrackingDataInput)input).enter(expectedSize);
        }
        return NoOp.INSTANCE;
    }
    
    public DataInput input() {
        return this.input;
    }
    
    public TrackingDataInput enter(final long expectedSize) throws IOException {
        if (this.depth++ > 512) {
            throw new IOException("NBT read exceeded maximum depth of 512");
        }
        this.ensureMaxLength(expectedSize);
        return this;
    }
    
    public TrackingDataInput enter() throws IOException {
        if (this.depth++ > 512) {
            throw new IOException("NBT read exceeded maximum depth of 512");
        }
        this.ensureMaxLength(0L);
        return this;
    }
    
    public void exit() throws IOException {
        --this.depth;
        this.ensureMaxLength(0L);
    }
    
    private void ensureMaxLength(final long expected) throws IOException {
        if (this.maxLength > 0L && this.counter + expected > this.maxLength) {
            throw new IOException("The read NBT was longer than the maximum allowed size of " + this.maxLength + " bytes!");
        }
    }
    
    @Override
    public void readFully(final byte[] array) throws IOException {
        this.counter += array.length;
        this.input.readFully(array);
    }
    
    @Override
    public void readFully(final byte[] array, final int off, final int len) throws IOException {
        this.counter += len;
        this.input.readFully(array, off, len);
    }
    
    @Override
    public int skipBytes(final int n) throws IOException {
        return this.input.skipBytes(n);
    }
    
    @Override
    public boolean readBoolean() throws IOException {
        ++this.counter;
        return this.input.readBoolean();
    }
    
    @Override
    public byte readByte() throws IOException {
        ++this.counter;
        return this.input.readByte();
    }
    
    @Override
    public int readUnsignedByte() throws IOException {
        ++this.counter;
        return this.input.readUnsignedByte();
    }
    
    @Override
    public short readShort() throws IOException {
        this.counter += 2L;
        return this.input.readShort();
    }
    
    @Override
    public int readUnsignedShort() throws IOException {
        this.counter += 2L;
        return this.input.readUnsignedShort();
    }
    
    @Override
    public char readChar() throws IOException {
        this.counter += 2L;
        return this.input.readChar();
    }
    
    @Override
    public int readInt() throws IOException {
        this.counter += 4L;
        return this.input.readInt();
    }
    
    @Override
    public long readLong() throws IOException {
        this.counter += 8L;
        return this.input.readLong();
    }
    
    @Override
    public float readFloat() throws IOException {
        this.counter += 4L;
        return this.input.readFloat();
    }
    
    @Override
    public double readDouble() throws IOException {
        this.counter += 8L;
        return this.input.readDouble();
    }
    
    @Nullable
    @Override
    public String readLine() throws IOException {
        final String result = this.input.readLine();
        if (result != null) {
            this.counter += result.length() + 1;
        }
        return result;
    }
    
    @NotNull
    @Override
    public String readUTF() throws IOException {
        final String result = this.input.readUTF();
        this.counter += result.length() * 2L + 2L;
        return result;
    }
    
    @Override
    public void close() throws IOException {
        this.exit();
    }
}

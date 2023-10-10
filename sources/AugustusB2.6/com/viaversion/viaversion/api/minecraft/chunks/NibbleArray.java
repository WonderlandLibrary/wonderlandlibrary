// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.chunks;

import java.util.Arrays;

public class NibbleArray
{
    private final byte[] handle;
    
    public NibbleArray(final int length) {
        if (length == 0 || length % 2 != 0) {
            throw new IllegalArgumentException("Length of nibble array must be a positive number dividable by 2!");
        }
        this.handle = new byte[length / 2];
    }
    
    public NibbleArray(final byte[] handle) {
        if (handle.length == 0 || handle.length % 2 != 0) {
            throw new IllegalArgumentException("Length of nibble array must be a positive number dividable by 2!");
        }
        this.handle = handle;
    }
    
    public byte get(final int x, final int y, final int z) {
        return this.get(ChunkSection.index(x, y, z));
    }
    
    public byte get(final int index) {
        final byte value = this.handle[index / 2];
        if (index % 2 == 0) {
            return (byte)(value & 0xF);
        }
        return (byte)(value >> 4 & 0xF);
    }
    
    public void set(final int x, final int y, final int z, final int value) {
        this.set(ChunkSection.index(x, y, z), value);
    }
    
    public void set(int index, final int value) {
        if (index % 2 == 0) {
            index /= 2;
            this.handle[index] = (byte)((this.handle[index] & 0xF0) | (value & 0xF));
        }
        else {
            index /= 2;
            this.handle[index] = (byte)((this.handle[index] & 0xF) | (value & 0xF) << 4);
        }
    }
    
    public int size() {
        return this.handle.length * 2;
    }
    
    public int actualSize() {
        return this.handle.length;
    }
    
    public void fill(byte value) {
        value &= 0xF;
        Arrays.fill(this.handle, (byte)(value << 4 | value));
    }
    
    public byte[] getHandle() {
        return this.handle;
    }
    
    public void setHandle(final byte[] handle) {
        if (handle.length != this.handle.length) {
            throw new IllegalArgumentException("Length of handle must equal to size of nibble array!");
        }
        System.arraycopy(handle, 0, this.handle, 0, handle.length);
    }
}

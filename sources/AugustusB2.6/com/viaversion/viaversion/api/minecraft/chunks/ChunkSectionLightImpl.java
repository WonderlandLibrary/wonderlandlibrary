// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.chunks;

import io.netty.buffer.ByteBuf;

public class ChunkSectionLightImpl implements ChunkSectionLight
{
    private NibbleArray blockLight;
    private NibbleArray skyLight;
    
    public ChunkSectionLightImpl() {
        this.blockLight = new NibbleArray(4096);
    }
    
    @Override
    public void setBlockLight(final byte[] data) {
        if (data.length != 2048) {
            throw new IllegalArgumentException("Data length != 2048");
        }
        if (this.blockLight == null) {
            this.blockLight = new NibbleArray(data);
        }
        else {
            this.blockLight.setHandle(data);
        }
    }
    
    @Override
    public void setSkyLight(final byte[] data) {
        if (data.length != 2048) {
            throw new IllegalArgumentException("Data length != 2048");
        }
        if (this.skyLight == null) {
            this.skyLight = new NibbleArray(data);
        }
        else {
            this.skyLight.setHandle(data);
        }
    }
    
    @Override
    public byte[] getBlockLight() {
        return (byte[])((this.blockLight == null) ? null : this.blockLight.getHandle());
    }
    
    @Override
    public NibbleArray getBlockLightNibbleArray() {
        return this.blockLight;
    }
    
    @Override
    public byte[] getSkyLight() {
        return (byte[])((this.skyLight == null) ? null : this.skyLight.getHandle());
    }
    
    @Override
    public NibbleArray getSkyLightNibbleArray() {
        return this.skyLight;
    }
    
    @Override
    public void readBlockLight(final ByteBuf input) {
        if (this.blockLight == null) {
            this.blockLight = new NibbleArray(4096);
        }
        input.readBytes(this.blockLight.getHandle());
    }
    
    @Override
    public void readSkyLight(final ByteBuf input) {
        if (this.skyLight == null) {
            this.skyLight = new NibbleArray(4096);
        }
        input.readBytes(this.skyLight.getHandle());
    }
    
    @Override
    public void writeBlockLight(final ByteBuf output) {
        output.writeBytes(this.blockLight.getHandle());
    }
    
    @Override
    public void writeSkyLight(final ByteBuf output) {
        output.writeBytes(this.skyLight.getHandle());
    }
    
    @Override
    public boolean hasSkyLight() {
        return this.skyLight != null;
    }
    
    @Override
    public boolean hasBlockLight() {
        return this.blockLight != null;
    }
}

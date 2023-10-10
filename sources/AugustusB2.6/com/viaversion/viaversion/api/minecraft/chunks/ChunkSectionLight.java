// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.chunks;

import io.netty.buffer.ByteBuf;

public interface ChunkSectionLight
{
    public static final int LIGHT_LENGTH = 2048;
    
    boolean hasSkyLight();
    
    boolean hasBlockLight();
    
    byte[] getSkyLight();
    
    byte[] getBlockLight();
    
    void setSkyLight(final byte[] p0);
    
    void setBlockLight(final byte[] p0);
    
    NibbleArray getSkyLightNibbleArray();
    
    NibbleArray getBlockLightNibbleArray();
    
    void readSkyLight(final ByteBuf p0);
    
    void readBlockLight(final ByteBuf p0);
    
    void writeSkyLight(final ByteBuf p0);
    
    void writeBlockLight(final ByteBuf p0);
}

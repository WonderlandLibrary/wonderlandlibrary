// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk;

import net.minecraft.network.PacketBuffer;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;

public interface IBlockStatePalette
{
    int idFor(final IBlockState p0);
    
    @Nullable
    IBlockState getBlockState(final int p0);
    
    void read(final PacketBuffer p0);
    
    void write(final PacketBuffer p0);
    
    int getSerializedSize();
}

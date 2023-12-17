// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk;

import net.minecraft.network.PacketBuffer;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockStatePaletteRegistry implements IBlockStatePalette
{
    @Override
    public int idFor(final IBlockState state) {
        final int i = Block.BLOCK_STATE_IDS.get(state);
        return (i == -1) ? 0 : i;
    }
    
    @Override
    public IBlockState getBlockState(final int indexKey) {
        final IBlockState iblockstate = Block.BLOCK_STATE_IDS.getByValue(indexKey);
        return (iblockstate == null) ? Blocks.AIR.getDefaultState() : iblockstate;
    }
    
    @Override
    public void read(final PacketBuffer buf) {
        buf.readVarInt();
    }
    
    @Override
    public void write(final PacketBuffer buf) {
        buf.writeVarInt(0);
    }
    
    @Override
    public int getSerializedSize() {
        return PacketBuffer.getVarIntSize(0);
    }
}

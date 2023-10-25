/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.IBlockStatePalette;

public class BlockStatePaletteRegistry
implements IBlockStatePalette {
    @Override
    public int idFor(IBlockState state) {
        int i = Block.BLOCK_STATE_IDS.get(state);
        return i == -1 ? 0 : i;
    }

    @Override
    public IBlockState getBlockState(int indexKey) {
        IBlockState iblockstate = Block.BLOCK_STATE_IDS.getByValue(indexKey);
        return iblockstate == null ? Blocks.AIR.getDefaultState() : iblockstate;
    }

    @Override
    public void read(PacketBuffer buf) {
        buf.readVarIntFromBuffer();
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeVarIntToBuffer(0);
    }

    @Override
    public int getSerializedState() {
        return PacketBuffer.getVarIntSize(0);
    }
}


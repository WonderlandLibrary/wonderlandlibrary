/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.IBlockStatePalette;
import net.minecraft.world.chunk.IBlockStatePaletteResizer;

public class BlockStatePaletteLinear
implements IBlockStatePalette {
    private final IBlockState[] states;
    private final IBlockStatePaletteResizer resizeHandler;
    private final int bits;
    private int arraySize;

    public BlockStatePaletteLinear(int p_i47088_1_, IBlockStatePaletteResizer p_i47088_2_) {
        this.states = new IBlockState[1 << p_i47088_1_];
        this.bits = p_i47088_1_;
        this.resizeHandler = p_i47088_2_;
    }

    @Override
    public int idFor(IBlockState state) {
        int j;
        for (int i = 0; i < this.arraySize; ++i) {
            if (this.states[i] != state) continue;
            return i;
        }
        if ((j = this.arraySize++) < this.states.length) {
            this.states[j] = state;
            return j;
        }
        return this.resizeHandler.onResize(this.bits + 1, state);
    }

    @Override
    @Nullable
    public IBlockState getBlockState(int indexKey) {
        return indexKey >= 0 && indexKey < this.arraySize ? this.states[indexKey] : null;
    }

    @Override
    public void read(PacketBuffer buf) {
        this.arraySize = buf.readVarIntFromBuffer();
        for (int i = 0; i < this.arraySize; ++i) {
            this.states[i] = Block.BLOCK_STATE_IDS.getByValue(buf.readVarIntFromBuffer());
        }
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeVarIntToBuffer(this.arraySize);
        for (int i = 0; i < this.arraySize; ++i) {
            buf.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(this.states[i]));
        }
    }

    @Override
    public int getSerializedState() {
        int i = PacketBuffer.getVarIntSize(this.arraySize);
        for (int j = 0; j < this.arraySize; ++j) {
            i += PacketBuffer.getVarIntSize(Block.BLOCK_STATE_IDS.get(this.states[j]));
        }
        return i;
    }
}


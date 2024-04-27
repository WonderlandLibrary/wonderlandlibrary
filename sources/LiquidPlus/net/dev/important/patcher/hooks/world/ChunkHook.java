/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.BlockPos
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraft.world.chunk.storage.ExtendedBlockStorage
 */
package net.dev.important.patcher.hooks.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ChunkHook {
    public static IBlockState getBlockState(Chunk chunk, BlockPos pos) {
        ExtendedBlockStorage storage;
        int y = pos.func_177956_o();
        if (y >= 0 && y >> 4 < chunk.func_76587_i().length && (storage = chunk.func_76587_i()[y >> 4]) != null) {
            return storage.func_177485_a(pos.func_177958_n() & 0xF, y & 0xF, pos.func_177952_p() & 0xF);
        }
        return Blocks.field_150350_a.func_176223_P();
    }
}


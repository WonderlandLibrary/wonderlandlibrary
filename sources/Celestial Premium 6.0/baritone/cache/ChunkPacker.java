/*
 * Decompiled with CFR 0.150.
 */
package baritone.cache;

import baritone.api.utils.BlockUtils;
import baritone.cache.CachedChunk;
import baritone.pathing.movement.MovementHelper;
import baritone.utils.pathing.PathingBlockType;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public final class ChunkPacker {
    private ChunkPacker() {
    }

    public static CachedChunk pack(Chunk chunk) {
        HashMap<String, List<BlockPos>> specialBlocks = new HashMap<String, List<BlockPos>>();
        BitSet bitSet = new BitSet(131072);
        try {
            ExtendedBlockStorage[] chunkInternalStorageArray = chunk.getBlockStorageArray();
            for (int y0 = 0; y0 < 16; ++y0) {
                ExtendedBlockStorage extendedblockstorage = chunkInternalStorageArray[y0];
                if (extendedblockstorage == null) continue;
                BlockStateContainer bsc = extendedblockstorage.getData();
                int yReal = y0 << 4;
                for (int y1 = 0; y1 < 16; ++y1) {
                    int y = y1 | yReal;
                    for (int z = 0; z < 16; ++z) {
                        for (int x = 0; x < 16; ++x) {
                            int index = CachedChunk.getPositionIndex(x, y, z);
                            IBlockState state = bsc.get(x, y1, z);
                            boolean[] bits = ChunkPacker.getPathingBlockType(state, chunk, x, y, z).getBits();
                            bitSet.set(index, bits[0]);
                            bitSet.set(index + 1, bits[1]);
                            Block block = state.getBlock();
                            if (!CachedChunk.BLOCKS_TO_KEEP_TRACK_OF.contains(block)) continue;
                            String name = BlockUtils.blockToString(block);
                            specialBlocks.computeIfAbsent(name, b -> new ArrayList()).add(new BlockPos(x, y, z));
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        IBlockState[] blocks = new IBlockState[256];
        for (int z = 0; z < 16; ++z) {
            block7: for (int x = 0; x < 16; ++x) {
                for (int y = 255; y >= 0; --y) {
                    int index = CachedChunk.getPositionIndex(x, y, z);
                    if (!bitSet.get(index) && !bitSet.get(index + 1)) continue;
                    blocks[z << 4 | x] = chunk.getBlockState(x, y, z);
                    continue block7;
                }
                blocks[z << 4 | x] = Blocks.AIR.getDefaultState();
            }
        }
        return new CachedChunk(chunk.x, chunk.z, bitSet, blocks, specialBlocks, System.currentTimeMillis());
    }

    private static PathingBlockType getPathingBlockType(IBlockState state, Chunk chunk, int x, int y, int z) {
        Block block = state.getBlock();
        if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
            if (MovementHelper.possiblyFlowing(state)) {
                return PathingBlockType.AVOID;
            }
            if (x != 15 && MovementHelper.possiblyFlowing(chunk.getBlockState(x + 1, y, z)) || x != 0 && MovementHelper.possiblyFlowing(chunk.getBlockState(x - 1, y, z)) || z != 15 && MovementHelper.possiblyFlowing(chunk.getBlockState(x, y, z + 1)) || z != 0 && MovementHelper.possiblyFlowing(chunk.getBlockState(x, y, z - 1))) {
                return PathingBlockType.AVOID;
            }
            if (x == 0 || x == 15 || z == 0 || z == 15) {
                if (BlockLiquid.getSlopeAngle(chunk.getWorld(), new BlockPos(x + (chunk.x << 4), y, z + (chunk.z << 4)), state.getMaterial(), state) == -1000.0f) {
                    return PathingBlockType.WATER;
                }
                return PathingBlockType.AVOID;
            }
            return PathingBlockType.WATER;
        }
        if (MovementHelper.avoidWalkingInto(block) || MovementHelper.isBottomSlab(state)) {
            return PathingBlockType.AVOID;
        }
        if (block == Blocks.AIR || block instanceof BlockTallGrass || block instanceof BlockDoublePlant || block instanceof BlockFlower) {
            return PathingBlockType.AIR;
        }
        return PathingBlockType.SOLID;
    }

    public static IBlockState pathingTypeToBlock(PathingBlockType type, int dimension) {
        switch (type) {
            case AIR: {
                return Blocks.AIR.getDefaultState();
            }
            case WATER: {
                return Blocks.WATER.getDefaultState();
            }
            case AVOID: {
                return Blocks.LAVA.getDefaultState();
            }
            case SOLID: {
                switch (dimension) {
                    case -1: {
                        return Blocks.NETHERRACK.getDefaultState();
                    }
                    default: {
                        return Blocks.STONE.getDefaultState();
                    }
                    case 1: 
                }
                return Blocks.END_STONE.getDefaultState();
            }
        }
        return null;
    }
}


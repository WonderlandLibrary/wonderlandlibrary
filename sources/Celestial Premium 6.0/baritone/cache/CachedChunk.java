/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 */
package baritone.cache;

import baritone.api.utils.BlockUtils;
import baritone.cache.ChunkPacker;
import baritone.utils.pathing.PathingBlockType;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public final class CachedChunk {
    public static final ImmutableSet<Block> BLOCKS_TO_KEEP_TRACK_OF = ImmutableSet.of(Blocks.DIAMOND_BLOCK, Blocks.COAL_BLOCK, Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK, Blocks.EMERALD_ORE, Blocks.EMERALD_BLOCK, new Block[]{Blocks.ENDER_CHEST, Blocks.FURNACE, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.END_PORTAL, Blocks.END_PORTAL_FRAME, Blocks.MOB_SPAWNER, Blocks.BARRIER, Blocks.OBSERVER, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.PORTAL, Blocks.HOPPER, Blocks.BEACON, Blocks.BREWING_STAND, Blocks.SKULL, Blocks.ENCHANTING_TABLE, Blocks.ANVIL, Blocks.LIT_FURNACE, Blocks.BED, Blocks.DRAGON_EGG, Blocks.JUKEBOX, Blocks.END_GATEWAY, Blocks.WEB, Blocks.NETHER_WART, Blocks.LADDER, Blocks.VINE});
    public static final int SIZE = 131072;
    public static final int SIZE_IN_BYTES = 16384;
    public final int x;
    public final int z;
    private final BitSet data;
    private final Int2ObjectOpenHashMap<String> special;
    private final IBlockState[] overview;
    private final int[] heightMap;
    private final Map<String, List<BlockPos>> specialBlockLocations;
    public final long cacheTimestamp;

    CachedChunk(int x, int z, BitSet data, IBlockState[] overview, Map<String, List<BlockPos>> specialBlockLocations, long cacheTimestamp) {
        CachedChunk.validateSize(data);
        this.x = x;
        this.z = z;
        this.data = data;
        this.overview = overview;
        this.heightMap = new int[256];
        this.specialBlockLocations = specialBlockLocations;
        this.cacheTimestamp = cacheTimestamp;
        if (specialBlockLocations.isEmpty()) {
            this.special = null;
        } else {
            this.special = new Int2ObjectOpenHashMap();
            this.setSpecial();
        }
        this.calculateHeightMap();
    }

    private final void setSpecial() {
        for (Map.Entry<String, List<BlockPos>> entry : this.specialBlockLocations.entrySet()) {
            for (BlockPos pos : entry.getValue()) {
                this.special.put(CachedChunk.getPositionIndex(pos.getX(), pos.getY(), pos.getZ()), (Object)entry.getKey());
            }
        }
    }

    public final IBlockState getBlock(int x, int y, int z, int dimension) {
        String str;
        int index = CachedChunk.getPositionIndex(x, y, z);
        PathingBlockType type = this.getType(index);
        int internalPos = z << 4 | x;
        if (this.heightMap[internalPos] == y && type != PathingBlockType.AVOID) {
            return this.overview[internalPos];
        }
        if (this.special != null && (str = (String)this.special.get(index)) != null) {
            return BlockUtils.stringToBlockRequired(str).getDefaultState();
        }
        if (type == PathingBlockType.SOLID) {
            if (y == 127 && dimension == -1) {
                return Blocks.BEDROCK.getDefaultState();
            }
            if (y < 5 && dimension == 0) {
                return Blocks.OBSIDIAN.getDefaultState();
            }
        }
        return ChunkPacker.pathingTypeToBlock(type, dimension);
    }

    private PathingBlockType getType(int index) {
        return PathingBlockType.fromBits(this.data.get(index), this.data.get(index + 1));
    }

    private void calculateHeightMap() {
        for (int z = 0; z < 16; ++z) {
            block1: for (int x = 0; x < 16; ++x) {
                int index = z << 4 | x;
                this.heightMap[index] = 0;
                for (int y = 256; y >= 0; --y) {
                    int i = CachedChunk.getPositionIndex(x, y, z);
                    if (!this.data.get(i) && !this.data.get(i + 1)) continue;
                    this.heightMap[index] = y;
                    continue block1;
                }
            }
        }
    }

    public final IBlockState[] getOverview() {
        return this.overview;
    }

    public final Map<String, List<BlockPos>> getRelativeBlocks() {
        return this.specialBlockLocations;
    }

    public final ArrayList<BlockPos> getAbsoluteBlocks(String blockType) {
        if (this.specialBlockLocations.get(blockType) == null) {
            return null;
        }
        ArrayList<BlockPos> res = new ArrayList<BlockPos>();
        for (BlockPos pos : this.specialBlockLocations.get(blockType)) {
            res.add(new BlockPos(pos.getX() + this.x * 16, pos.getY(), pos.getZ() + this.z * 16));
        }
        return res;
    }

    public final byte[] toByteArray() {
        return this.data.toByteArray();
    }

    public static int getPositionIndex(int x, int y, int z) {
        return x << 1 | z << 5 | y << 9;
    }

    private static void validateSize(BitSet data) {
        if (data.size() > 131072) {
            throw new IllegalArgumentException("BitSet of invalid length provided");
        }
    }
}


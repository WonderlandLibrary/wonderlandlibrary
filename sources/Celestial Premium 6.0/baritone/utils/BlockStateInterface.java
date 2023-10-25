/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 */
package baritone.utils;

import baritone.Baritone;
import baritone.api.utils.IPlayerContext;
import baritone.cache.CachedRegion;
import baritone.cache.WorldData;
import baritone.utils.BlockStateInterfaceAccessWrapper;
import baritone.utils.accessor.IChunkProviderClient;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockStateInterface {
    private final Long2ObjectMap<Chunk> loadedChunks;
    private final WorldData worldData;
    protected final IBlockAccess world;
    public final BlockPos.MutableBlockPos isPassableBlockPos;
    public final IBlockAccess access;
    private Chunk prev = null;
    private CachedRegion prevCached = null;
    private final boolean useTheRealWorld;
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();

    public BlockStateInterface(IPlayerContext ctx) {
        this(ctx, false);
    }

    public BlockStateInterface(IPlayerContext ctx, boolean copyLoadedChunks) {
        this(ctx.world(), (WorldData)ctx.worldData(), copyLoadedChunks);
    }

    public BlockStateInterface(World world, WorldData worldData, boolean copyLoadedChunks) {
        this.world = world;
        this.worldData = worldData;
        Long2ObjectMap<Chunk> worldLoaded = ((IChunkProviderClient)((Object)world.getChunkProvider())).loadedChunks();
        this.loadedChunks = copyLoadedChunks ? new Long2ObjectOpenHashMap(worldLoaded) : worldLoaded;
        boolean bl = this.useTheRealWorld = (Boolean)Baritone.settings().pathThroughCachedOnly.value == false;
        if (!Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
            throw new IllegalStateException();
        }
        this.isPassableBlockPos = new BlockPos.MutableBlockPos();
        this.access = new BlockStateInterfaceAccessWrapper(this);
    }

    public boolean worldContainsLoadedChunk(int blockX, int blockZ) {
        return this.loadedChunks.containsKey(ChunkPos.asLong(blockX >> 4, blockZ >> 4));
    }

    public static Block getBlock(IPlayerContext ctx, BlockPos pos) {
        return BlockStateInterface.get(ctx, pos).getBlock();
    }

    public static IBlockState get(IPlayerContext ctx, BlockPos pos) {
        return new BlockStateInterface(ctx).get0(pos.getX(), pos.getY(), pos.getZ());
    }

    public IBlockState get0(BlockPos pos) {
        return this.get0(pos.getX(), pos.getY(), pos.getZ());
    }

    public IBlockState get0(int x, int y, int z) {
        IBlockState type;
        Object cached;
        if (y < 0 || y >= 256) {
            return AIR;
        }
        if (this.useTheRealWorld) {
            cached = this.prev;
            if (cached != null && ((Chunk)cached).x == x >> 4 && ((Chunk)cached).z == z >> 4) {
                return ((Chunk)cached).getBlockState(x, y, z);
            }
            Chunk chunk = (Chunk)this.loadedChunks.get(ChunkPos.asLong(x >> 4, z >> 4));
            if (chunk != null && chunk.isLoaded()) {
                this.prev = chunk;
                return chunk.getBlockState(x, y, z);
            }
        }
        if ((cached = this.prevCached) == null || ((CachedRegion)cached).getX() != x >> 9 || ((CachedRegion)cached).getZ() != z >> 9) {
            if (this.worldData == null) {
                return AIR;
            }
            CachedRegion region = this.worldData.cache.getRegion(x >> 9, z >> 9);
            if (region == null) {
                return AIR;
            }
            this.prevCached = region;
            cached = region;
        }
        if ((type = ((CachedRegion)cached).getBlock(x & 0x1FF, y, z & 0x1FF)) == null) {
            return AIR;
        }
        return type;
    }

    public boolean isLoaded(int x, int z) {
        Chunk prevChunk = this.prev;
        if (prevChunk != null && prevChunk.x == x >> 4 && prevChunk.z == z >> 4) {
            return true;
        }
        prevChunk = (Chunk)this.loadedChunks.get(ChunkPos.asLong(x >> 4, z >> 4));
        if (prevChunk != null && prevChunk.isLoaded()) {
            this.prev = prevChunk;
            return true;
        }
        CachedRegion prevRegion = this.prevCached;
        if (prevRegion != null && prevRegion.getX() == x >> 9 && prevRegion.getZ() == z >> 9) {
            return prevRegion.isCached(x & 0x1FF, z & 0x1FF);
        }
        if (this.worldData == null) {
            return false;
        }
        prevRegion = this.worldData.cache.getRegion(x >> 9, z >> 9);
        if (prevRegion == null) {
            return false;
        }
        this.prevCached = prevRegion;
        return prevRegion.isCached(x & 0x1FF, z & 0x1FF);
    }
}


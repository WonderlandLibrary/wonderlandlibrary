// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import optifine.DynamicLights;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.tileentity.TileEntity;
import java.util.Arrays;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import optifine.Config;
import net.minecraft.init.Blocks;
import java.util.ArrayDeque;
import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.ChunkCache;

public class RegionRenderCache extends ChunkCache
{
    private static final IBlockState field_175632_f;
    private final BlockPos field_175633_g;
    private int[] field_175634_h;
    private IBlockState[] field_175635_i;
    private static final String __OBFID = "CL_00002565";
    private static ArrayDeque<int[]> cacheLights;
    private static ArrayDeque<IBlockState[]> cacheStates;
    private static int maxCacheSize;
    
    static {
        field_175632_f = Blocks.air.getDefaultState();
        RegionRenderCache.cacheLights = new ArrayDeque<int[]>();
        RegionRenderCache.cacheStates = new ArrayDeque<IBlockState[]>();
        RegionRenderCache.maxCacheSize = Config.limit(Runtime.getRuntime().availableProcessors(), 1, 32);
    }
    
    public RegionRenderCache(final World worldIn, final BlockPos posFromIn, final BlockPos posToIn, final int subIn) {
        super(worldIn, posFromIn, posToIn, subIn);
        this.field_175633_g = posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
        final boolean var5 = true;
        Arrays.fill(this.field_175634_h = allocateLights(8000), -1);
        this.field_175635_i = allocateStates(8000);
    }
    
    @Override
    public TileEntity getTileEntity(final BlockPos pos) {
        final int var2 = (pos.getX() >> 4) - this.chunkX;
        final int var3 = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[var2][var3].func_177424_a(pos, Chunk.EnumCreateEntityType.QUEUED);
    }
    
    @Override
    public int getCombinedLight(final BlockPos p_175626_1_, final int p_175626_2_) {
        final int var3 = this.func_175630_e(p_175626_1_);
        int var4 = this.field_175634_h[var3];
        if (var4 == -1) {
            var4 = super.getCombinedLight(p_175626_1_, p_175626_2_);
            if (Config.isDynamicLights() && !this.getBlockState(p_175626_1_).getBlock().isOpaqueCube()) {
                var4 = DynamicLights.getCombinedLight(p_175626_1_, var4);
            }
            this.field_175634_h[var3] = var4;
        }
        return var4;
    }
    
    @Override
    public IBlockState getBlockState(final BlockPos pos) {
        final int var2 = this.func_175630_e(pos);
        IBlockState var3 = this.field_175635_i[var2];
        if (var3 == null) {
            var3 = this.func_175631_c(pos);
            this.field_175635_i[var2] = var3;
        }
        return var3;
    }
    
    private IBlockState func_175631_c(final BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final int var2 = (pos.getX() >> 4) - this.chunkX;
            final int var3 = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[var2][var3].getBlockState(pos);
        }
        return RegionRenderCache.field_175632_f;
    }
    
    private int func_175630_e(final BlockPos p_175630_1_) {
        final int var2 = p_175630_1_.getX() - this.field_175633_g.getX();
        final int var3 = p_175630_1_.getY() - this.field_175633_g.getY();
        final int var4 = p_175630_1_.getZ() - this.field_175633_g.getZ();
        return var2 * 400 + var4 * 20 + var3;
    }
    
    public void freeBuffers() {
        freeLights(this.field_175634_h);
        freeStates(this.field_175635_i);
    }
    
    private static int[] allocateLights(final int size) {
        final ArrayDeque var1 = RegionRenderCache.cacheLights;
        synchronized (RegionRenderCache.cacheLights) {
            int[] ints = RegionRenderCache.cacheLights.pollLast();
            if (ints == null || ints.length < size) {
                ints = new int[size];
            }
            // monitorexit(RegionRenderCache.cacheLights)
            return ints;
        }
    }
    
    public static void freeLights(final int[] ints) {
        final ArrayDeque var1 = RegionRenderCache.cacheLights;
        synchronized (RegionRenderCache.cacheLights) {
            if (RegionRenderCache.cacheLights.size() < RegionRenderCache.maxCacheSize) {
                RegionRenderCache.cacheLights.add(ints);
            }
        }
        // monitorexit(RegionRenderCache.cacheLights)
    }
    
    private static IBlockState[] allocateStates(final int size) {
        final ArrayDeque var1 = RegionRenderCache.cacheStates;
        synchronized (RegionRenderCache.cacheStates) {
            IBlockState[] states = RegionRenderCache.cacheStates.pollLast();
            if (states != null && states.length >= size) {
                Arrays.fill(states, null);
            }
            else {
                states = new IBlockState[size];
            }
            // monitorexit(RegionRenderCache.cacheStates)
            return states;
        }
    }
    
    public static void freeStates(final IBlockState[] states) {
        final ArrayDeque var1 = RegionRenderCache.cacheStates;
        synchronized (RegionRenderCache.cacheStates) {
            if (RegionRenderCache.cacheStates.size() < RegionRenderCache.maxCacheSize) {
                RegionRenderCache.cacheStates.add(states);
            }
        }
        // monitorexit(RegionRenderCache.cacheStates)
    }
}

/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import java.util.ArrayDeque;
import java.util.Arrays;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import optifine.Config;
import optifine.DynamicLights;

public class RegionRenderCache
extends ChunkCache {
    private static final IBlockState field_175632_f = Blocks.air.getDefaultState();
    private final BlockPos field_175633_g;
    private int[] field_175634_h;
    private IBlockState[] field_175635_i;
    private static final String __OBFID = "CL_00002565";
    private static ArrayDeque<int[]> cacheLights = new ArrayDeque();
    private static ArrayDeque<IBlockState[]> cacheStates = new ArrayDeque();
    private static int maxCacheSize = Config.limit(Runtime.getRuntime().availableProcessors(), 1, 32);

    public RegionRenderCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn) {
        super(worldIn, posFromIn, posToIn, subIn);
        this.field_175633_g = posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
        boolean var5 = true;
        this.field_175634_h = RegionRenderCache.allocateLights(8000);
        Arrays.fill(this.field_175634_h, -1);
        this.field_175635_i = RegionRenderCache.allocateStates(8000);
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        int var2 = (pos.getX() >> 4) - this.chunkX;
        int var3 = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[var2][var3].func_177424_a(pos, Chunk.EnumCreateEntityType.QUEUED);
    }

    @Override
    public int getCombinedLight(BlockPos p_175626_1_, int p_175626_2_) {
        int var3 = this.func_175630_e(p_175626_1_);
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
    public IBlockState getBlockState(BlockPos pos) {
        int var2 = this.func_175630_e(pos);
        IBlockState var3 = this.field_175635_i[var2];
        if (var3 == null) {
            this.field_175635_i[var2] = var3 = this.func_175631_c(pos);
        }
        return var3;
    }

    private IBlockState func_175631_c(BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            int var2 = (pos.getX() >> 4) - this.chunkX;
            int var3 = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[var2][var3].getBlockState(pos);
        }
        return field_175632_f;
    }

    private int func_175630_e(BlockPos p_175630_1_) {
        int var2 = p_175630_1_.getX() - this.field_175633_g.getX();
        int var3 = p_175630_1_.getY() - this.field_175633_g.getY();
        int var4 = p_175630_1_.getZ() - this.field_175633_g.getZ();
        return var2 * 400 + var4 * 20 + var3;
    }

    public void freeBuffers() {
        RegionRenderCache.freeLights(this.field_175634_h);
        RegionRenderCache.freeStates(this.field_175635_i);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int[] allocateLights(int size) {
        ArrayDeque<int[]> var1 = cacheLights;
        ArrayDeque<int[]> arrayDeque = cacheLights;
        synchronized (arrayDeque) {
            int[] ints = cacheLights.pollLast();
            if (ints == null || ints.length < size) {
                ints = new int[size];
            }
            return ints;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void freeLights(int[] ints) {
        ArrayDeque<int[]> var1 = cacheLights;
        ArrayDeque<int[]> arrayDeque = cacheLights;
        synchronized (arrayDeque) {
            if (cacheLights.size() < maxCacheSize) {
                cacheLights.add(ints);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static IBlockState[] allocateStates(int size) {
        ArrayDeque<IBlockState[]> var1 = cacheStates;
        ArrayDeque<IBlockState[]> arrayDeque = cacheStates;
        synchronized (arrayDeque) {
            Object[] states = cacheStates.pollLast();
            if (states != null && states.length >= size) {
                Arrays.fill(states, null);
            } else {
                states = new IBlockState[size];
            }
            return states;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void freeStates(IBlockState[] states) {
        ArrayDeque<IBlockState[]> var1 = cacheStates;
        ArrayDeque<IBlockState[]> arrayDeque = cacheStates;
        synchronized (arrayDeque) {
            if (cacheStates.size() < maxCacheSize) {
                cacheStates.add(states);
            }
        }
    }
}


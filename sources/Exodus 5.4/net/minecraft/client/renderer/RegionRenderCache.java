/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import java.util.Arrays;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RegionRenderCache
extends ChunkCache {
    private final BlockPos position;
    private int[] combinedLights;
    private static final IBlockState DEFAULT_STATE = Blocks.air.getDefaultState();
    private IBlockState[] blockStates;

    private IBlockState getBlockStateRaw(BlockPos blockPos) {
        if (blockPos.getY() >= 0 && blockPos.getY() < 256) {
            int n = (blockPos.getX() >> 4) - this.chunkX;
            int n2 = (blockPos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[n][n2].getBlockState(blockPos);
        }
        return DEFAULT_STATE;
    }

    public RegionRenderCache(World world, BlockPos blockPos, BlockPos blockPos2, int n) {
        super(world, blockPos, blockPos2, n);
        this.position = blockPos.subtract(new Vec3i(n, n, n));
        int n2 = 8000;
        this.combinedLights = new int[8000];
        Arrays.fill(this.combinedLights, -1);
        this.blockStates = new IBlockState[8000];
    }

    @Override
    public int getCombinedLight(BlockPos blockPos, int n) {
        int n2 = this.getPositionIndex(blockPos);
        int n3 = this.combinedLights[n2];
        if (n3 == -1) {
            this.combinedLights[n2] = n3 = super.getCombinedLight(blockPos, n);
        }
        return n3;
    }

    private int getPositionIndex(BlockPos blockPos) {
        int n = blockPos.getX() - this.position.getX();
        int n2 = blockPos.getY() - this.position.getY();
        int n3 = blockPos.getZ() - this.position.getZ();
        return n * 400 + n3 * 20 + n2;
    }

    @Override
    public IBlockState getBlockState(BlockPos blockPos) {
        int n = this.getPositionIndex(blockPos);
        IBlockState iBlockState = this.blockStates[n];
        if (iBlockState == null) {
            this.blockStates[n] = iBlockState = this.getBlockStateRaw(blockPos);
        }
        return iBlockState;
    }

    @Override
    public TileEntity getTileEntity(BlockPos blockPos) {
        int n = (blockPos.getX() >> 4) - this.chunkX;
        int n2 = (blockPos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[n][n2].getTileEntity(blockPos, Chunk.EnumCreateEntityType.QUEUED);
    }
}


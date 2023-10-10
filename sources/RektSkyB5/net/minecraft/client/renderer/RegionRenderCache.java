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
    private static final IBlockState DEFAULT_STATE = Blocks.air.getDefaultState();
    private final BlockPos position;
    private int[] combinedLights;
    private IBlockState[] blockStates;

    public RegionRenderCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn) {
        super(worldIn, posFromIn, posToIn, subIn);
        this.position = posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
        int i2 = 8000;
        this.combinedLights = new int[8000];
        Arrays.fill(this.combinedLights, -1);
        this.blockStates = new IBlockState[8000];
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        int i2 = (pos.getX() >> 4) - this.chunkX;
        int j2 = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[i2][j2].getTileEntity(pos, Chunk.EnumCreateEntityType.QUEUED);
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        int i2 = this.getPositionIndex(pos);
        int j2 = this.combinedLights[i2];
        if (j2 == -1) {
            this.combinedLights[i2] = j2 = super.getCombinedLight(pos, lightValue);
        }
        return j2;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        int i2 = this.getPositionIndex(pos);
        IBlockState iblockstate = this.blockStates[i2];
        if (iblockstate == null) {
            this.blockStates[i2] = iblockstate = this.getBlockStateRaw(pos);
        }
        return iblockstate;
    }

    private IBlockState getBlockStateRaw(BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            int i2 = (pos.getX() >> 4) - this.chunkX;
            int j2 = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[i2][j2].getBlockState(pos);
        }
        return DEFAULT_STATE;
    }

    private int getPositionIndex(BlockPos p_175630_1_) {
        int i2 = p_175630_1_.getX() - this.position.getX();
        int j2 = p_175630_1_.getY() - this.position.getY();
        int k2 = p_175630_1_.getZ() - this.position.getZ();
        return i2 * 400 + k2 * 20 + j2;
    }
}


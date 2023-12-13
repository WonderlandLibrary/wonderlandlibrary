/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class EmptyChunk
extends Chunk {
    private static final String __OBFID = "CL_00000372";

    public EmptyChunk(World worldIn, int x, int z) {
        super(worldIn, x, z);
    }

    @Override
    public boolean isAtLocation(int x, int z) {
        return x == this.xPosition && z == this.zPosition;
    }

    @Override
    public int getHeight(int x, int z) {
        return 0;
    }

    @Override
    public void generateHeightMap() {
    }

    @Override
    public void generateSkylightMap() {
    }

    @Override
    public Block getBlock(BlockPos pos) {
        return Blocks.air;
    }

    @Override
    public int getBlockLightOpacity(BlockPos pos) {
        return 255;
    }

    @Override
    public int getBlockMetadata(BlockPos pos) {
        return 0;
    }

    @Override
    public int getLightFor(EnumSkyBlock p_177413_1_, BlockPos p_177413_2_) {
        return p_177413_1_.defaultLightValue;
    }

    @Override
    public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos p_177431_2_, int p_177431_3_) {
    }

    @Override
    public int setLight(BlockPos p_177443_1_, int p_177443_2_) {
        return 0;
    }

    @Override
    public void addEntity(Entity entityIn) {
    }

    @Override
    public void removeEntity(Entity p_76622_1_) {
    }

    @Override
    public void removeEntityAtIndex(Entity p_76608_1_, int p_76608_2_) {
    }

    @Override
    public boolean canSeeSky(BlockPos pos) {
        return false;
    }

    @Override
    public TileEntity func_177424_a(BlockPos p_177424_1_, Chunk.EnumCreateEntityType p_177424_2_) {
        return null;
    }

    @Override
    public void addTileEntity(TileEntity tileEntityIn) {
    }

    @Override
    public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {
    }

    @Override
    public void removeTileEntity(BlockPos pos) {
    }

    @Override
    public void onChunkLoad() {
    }

    @Override
    public void onChunkUnload() {
    }

    @Override
    public void setChunkModified() {
    }

    @Override
    public void func_177414_a(Entity p_177414_1_, AxisAlignedBB p_177414_2_, List p_177414_3_, Predicate p_177414_4_) {
    }

    @Override
    public void func_177430_a(Class p_177430_1_, AxisAlignedBB p_177430_2_, List p_177430_3_, Predicate p_177430_4_) {
    }

    @Override
    public boolean needsSaving(boolean p_76601_1_) {
        return false;
    }

    @Override
    public Random getRandomWithSeed(long seed) {
        return new Random(this.getWorld().getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ seed);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean getAreLevelsEmpty(int p_76606_1_, int p_76606_2_) {
        return true;
    }
}


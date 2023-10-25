/*
 * Decompiled with CFR 0.152.
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
    @Override
    public void onChunkLoad() {
    }

    @Override
    public void generateHeightMap() {
    }

    @Override
    public int getLightFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        return enumSkyBlock.defaultLightValue;
    }

    public EmptyChunk(World world, int n, int n2) {
        super(world, n, n2);
    }

    @Override
    public void addEntity(Entity entity) {
    }

    @Override
    public Block getBlock(BlockPos blockPos) {
        return Blocks.air;
    }

    @Override
    public boolean needsSaving(boolean bl) {
        return false;
    }

    @Override
    public void addTileEntity(TileEntity tileEntity) {
    }

    @Override
    public void addTileEntity(BlockPos blockPos, TileEntity tileEntity) {
    }

    @Override
    public int getLightSubtracted(BlockPos blockPos, int n) {
        return 0;
    }

    @Override
    public TileEntity getTileEntity(BlockPos blockPos, Chunk.EnumCreateEntityType enumCreateEntityType) {
        return null;
    }

    @Override
    public void onChunkUnload() {
    }

    @Override
    public int getHeightValue(int n, int n2) {
        return 0;
    }

    @Override
    public int getBlockLightOpacity(BlockPos blockPos) {
        return 255;
    }

    @Override
    public Random getRandomWithSeed(long l) {
        return new Random(this.getWorld().getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ l);
    }

    @Override
    public void removeTileEntity(BlockPos blockPos) {
    }

    @Override
    public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> clazz, AxisAlignedBB axisAlignedBB, List<T> list, Predicate<? super T> predicate) {
    }

    @Override
    public boolean getAreLevelsEmpty(int n, int n2) {
        return true;
    }

    @Override
    public void generateSkylightMap() {
    }

    @Override
    public void removeEntityAtIndex(Entity entity, int n) {
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void removeEntity(Entity entity) {
    }

    @Override
    public void getEntitiesWithinAABBForEntity(Entity entity, AxisAlignedBB axisAlignedBB, List<Entity> list, Predicate<? super Entity> predicate) {
    }

    @Override
    public boolean canSeeSky(BlockPos blockPos) {
        return false;
    }

    @Override
    public boolean isAtLocation(int n, int n2) {
        return n == this.xPosition && n2 == this.zPosition;
    }

    @Override
    public int getBlockMetadata(BlockPos blockPos) {
        return 0;
    }

    @Override
    public void setLightFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos, int n) {
    }

    @Override
    public void setChunkModified() {
    }
}


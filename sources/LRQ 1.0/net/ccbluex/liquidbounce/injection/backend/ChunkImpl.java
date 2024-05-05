/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.chunk.Chunk
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.List;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.world.IChunk;
import net.ccbluex.liquidbounce.api.util.WrappedMutableList;
import net.ccbluex.liquidbounce.injection.backend.AxisAlignedBBImpl;
import net.ccbluex.liquidbounce.injection.backend.ChunkImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerSPImpl;
import net.ccbluex.liquidbounce.injection.backend.IBlockStateImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

public final class ChunkImpl
implements IChunk {
    private final Chunk wrapped;

    @Override
    public int getX() {
        return this.wrapped.field_76635_g;
    }

    @Override
    public int getZ() {
        return this.wrapped.field_76647_h;
    }

    @Override
    public void getEntitiesWithinAABBForEntity(IEntityPlayerSP thePlayer, IAxisAlignedBB arrowBox, List<IEntity> collidedEntities, @Nullable Void nothing) {
        IAxisAlignedBB $this$unwrap$iv;
        IEntityPlayerSP iEntityPlayerSP = thePlayer;
        Chunk chunk = this.wrapped;
        boolean $i$f$unwrap = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)((Object)$this$unwrap$iv)).getWrapped();
        $this$unwrap$iv = arrowBox;
        entityPlayerSP = (Entity)entityPlayerSP;
        $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        chunk.func_177414_a((Entity)entityPlayerSP, axisAlignedBB, (List)new WrappedMutableList(collidedEntities, getEntitiesWithinAABBForEntity.1.INSTANCE, getEntitiesWithinAABBForEntity.2.INSTANCE), null);
    }

    @Override
    public int getHeightValue(int x, int z) {
        return this.wrapped.func_76611_b(x, z);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public IIBlockState getBlockState(WBlockPos blockPos) {
        void $this$unwrap$iv;
        WBlockPos wBlockPos = blockPos;
        Chunk chunk = this.wrapped;
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        IBlockState $this$wrap$iv = chunk.func_177435_g(blockPos2);
        boolean $i$f$wrap = false;
        return new IBlockStateImpl($this$wrap$iv);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ChunkImpl && ((ChunkImpl)other).wrapped.equals(this.wrapped);
    }

    public final Chunk getWrapped() {
        return this.wrapped;
    }

    public ChunkImpl(Chunk wrapped) {
        this.wrapped = wrapped;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
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
import kotlin.jvm.functions.Function1;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
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
    public IIBlockState getBlockState(WBlockPos wBlockPos) {
        WBlockPos wBlockPos2 = wBlockPos;
        Chunk chunk = this.wrapped;
        boolean bl = false;
        BlockPos blockPos = new BlockPos(wBlockPos2.getX(), wBlockPos2.getY(), wBlockPos2.getZ());
        wBlockPos2 = chunk.func_177435_g(blockPos);
        bl = false;
        return new IBlockStateImpl((IBlockState)wBlockPos2);
    }

    @Override
    public int getZ() {
        return this.wrapped.field_76647_h;
    }

    @Override
    public int getHeightValue(int n, int n2) {
        return this.wrapped.func_76611_b(n, n2);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof ChunkImpl && ((ChunkImpl)object).wrapped.equals(this.wrapped);
    }

    public ChunkImpl(Chunk chunk) {
        this.wrapped = chunk;
    }

    @Override
    public int getX() {
        return this.wrapped.field_76635_g;
    }

    public final Chunk getWrapped() {
        return this.wrapped;
    }

    @Override
    public void getEntitiesWithinAABBForEntity(IEntityPlayerSP iEntityPlayerSP, IAxisAlignedBB iAxisAlignedBB, List list, @Nullable Void void_) {
        Object object = iEntityPlayerSP;
        Chunk chunk = this.wrapped;
        boolean bl = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)object).getWrapped();
        object = iAxisAlignedBB;
        entityPlayerSP = (Entity)entityPlayerSP;
        bl = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)object).getWrapped();
        chunk.func_177414_a((Entity)entityPlayerSP, axisAlignedBB, (List)new WrappedMutableList(list, (Function1)getEntitiesWithinAABBForEntity.1.INSTANCE, (Function1)getEntitiesWithinAABBForEntity.2.INSTANCE), null);
    }
}


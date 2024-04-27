/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.chunk.Chunk
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0001\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J0\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\u0018\u0010 \u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\bH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\n\u00a8\u0006!"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ChunkImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IChunk;", "wrapped", "Lnet/minecraft/world/chunk/Chunk;", "(Lnet/minecraft/world/chunk/Chunk;)V", "getWrapped", "()Lnet/minecraft/world/chunk/Chunk;", "x", "", "getX", "()I", "z", "getZ", "equals", "", "other", "", "getBlockState", "Lnet/ccbluex/liquidbounce/api/minecraft/block/state/IIBlockState;", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getEntitiesWithinAABBForEntity", "", "thePlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "arrowBox", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "collidedEntities", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "nothing", "", "getHeightValue", "LiKingSense"})
public final class ChunkImpl
implements IChunk {
    @NotNull
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
    public void getEntitiesWithinAABBForEntity(@NotNull IEntityPlayerSP thePlayer, @NotNull IAxisAlignedBB arrowBox, @NotNull List<IEntity> collidedEntities, @Nullable Void nothing) {
        IAxisAlignedBB $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)thePlayer, (String)"thePlayer");
        Intrinsics.checkParameterIsNotNull((Object)arrowBox, (String)"arrowBox");
        Intrinsics.checkParameterIsNotNull(collidedEntities, (String)"collidedEntities");
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
    @NotNull
    public IIBlockState getBlockState(@NotNull WBlockPos blockPos) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
        WBlockPos wBlockPos = blockPos;
        Chunk chunk = this.wrapped;
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        IBlockState iBlockState = chunk.func_177435_g(blockPos2);
        Intrinsics.checkExpressionValueIsNotNull((Object)iBlockState, (String)"wrapped.getBlockState(blockPos.unwrap())");
        IBlockState $this$wrap$iv = iBlockState;
        boolean $i$f$wrap = false;
        return new IBlockStateImpl($this$wrap$iv);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ChunkImpl && Intrinsics.areEqual((Object)((ChunkImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Chunk getWrapped() {
        return this.wrapped;
    }

    public ChunkImpl(@NotNull Chunk wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}


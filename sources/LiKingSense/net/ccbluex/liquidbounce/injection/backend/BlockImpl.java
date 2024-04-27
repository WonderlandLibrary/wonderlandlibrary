/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.NotImplementedError
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.NotImplementedError;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.injection.backend.AxisAlignedBBImpl;
import net.ccbluex.liquidbounce.injection.backend.Backend;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerSPImpl;
import net.ccbluex.liquidbounce.injection.backend.IBlockStateImpl;
import net.ccbluex.liquidbounce.injection.backend.MaterialImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldClientImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldImpl;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0001\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u001b\u001a\u00020\u0019H\u0016J\u0013\u0010\u001c\u001a\u00020\u00192\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0096\u0002J\"\u0010\u001f\u001a\u0004\u0018\u00010 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010\u001a\u001a\u00020\u0006H\u0016J\u0010\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u0001H\u0016J \u0010(\u001a\u00020&2\u0006\u0010)\u001a\u00020\u00062\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020$H\u0016J\u0012\u0010-\u001a\u0004\u0018\u00010.2\u0006\u0010\u001a\u001a\u00020\u0006H\u0016J \u0010/\u001a\u00020\u00102\u0006\u00100\u001a\u0002012\u0006\u0010*\u001a\u00020\"2\u0006\u00102\u001a\u00020$H\u0016J \u00103\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010)\u001a\u00020\u00062\u0006\u00102\u001a\u00020$H\u0016J\u0010\u00104\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0006H\u0016J\u0010\u00105\u001a\u00020\u00192\u0006\u0010)\u001a\u00020\u0006H\u0016J\u0018\u00106\u001a\u0002072\u0006\u0010!\u001a\u00020\"2\u0006\u00102\u001a\u00020$H\u0016R\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\fR$\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00108V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u00068"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/BlockImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "wrapped", "Lnet/minecraft/block/Block;", "(Lnet/minecraft/block/Block;)V", "defaultState", "Lnet/ccbluex/liquidbounce/api/minecraft/block/state/IIBlockState;", "getDefaultState", "()Lnet/ccbluex/liquidbounce/api/minecraft/block/state/IIBlockState;", "localizedName", "", "getLocalizedName", "()Ljava/lang/String;", "registryName", "getRegistryName", "value", "", "slipperiness", "getSlipperiness", "()F", "setSlipperiness", "(F)V", "getWrapped", "()Lnet/minecraft/block/Block;", "canCollideCheck", "", "state", "hitIfLiquid", "equals", "other", "", "getCollisionBoundingBox", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "world", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorld;", "pos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getIdFromBlock", "", "block", "getMapColor", "blockState", "theWorld", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "bp", "getMaterial", "Lnet/ccbluex/liquidbounce/api/minecraft/block/material/IMaterial;", "getPlayerRelativeBlockHardness", "thePlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "blockPos", "getSelectedBoundingBox", "isFullCube", "isTranslucent", "setBlockBoundsBasedOnState", "", "LiKingSense"})
public final class BlockImpl
implements IBlock {
    @NotNull
    private final Block wrapped;

    @Override
    @NotNull
    public String getRegistryName() {
        String string = this.wrapped.func_149739_a();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.unlocalizedName");
        return string;
    }

    @Override
    public float getSlipperiness() {
        return this.wrapped.field_149765_K;
    }

    @Override
    public void setSlipperiness(float value) {
        this.wrapped.field_149765_K = value;
    }

    @Override
    @Nullable
    public IIBlockState getDefaultState() {
        IBlockState iBlockState = this.wrapped.func_176223_P();
        Intrinsics.checkExpressionValueIsNotNull((Object)iBlockState, (String)"wrapped.defaultState");
        return new IBlockStateImpl(iBlockState);
    }

    @Override
    @NotNull
    public String getLocalizedName() {
        String string = this.wrapped.func_149732_F();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.localizedName");
        return string;
    }

    @Override
    @NotNull
    public IAxisAlignedBB getSelectedBoundingBox(@NotNull IWorld world, @NotNull IIBlockState blockState, @NotNull WBlockPos blockPos) {
        Object $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)world, (String)"world");
        Intrinsics.checkParameterIsNotNull((Object)blockState, (String)"blockState");
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
        IIBlockState iIBlockState = blockState;
        Block block = this.wrapped;
        boolean $i$f$unwrap = false;
        IBlockState iBlockState = ((IBlockStateImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = world;
        $i$f$unwrap = false;
        Object object = ((WorldImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = blockPos;
        object = (IBlockAccess)object;
        $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos(((WVec3i)$this$unwrap$iv).getX(), ((WVec3i)$this$unwrap$iv).getY(), ((WVec3i)$this$unwrap$iv).getZ());
        $this$unwrap$iv = blockPos;
        block = block.func_185496_a(iBlockState, object, blockPos2);
        $i$f$unwrap = false;
        iBlockState = new BlockPos(((WVec3i)$this$unwrap$iv).getX(), ((WVec3i)$this$unwrap$iv).getY(), ((WVec3i)$this$unwrap$iv).getZ());
        AxisAlignedBB axisAlignedBB = block.func_186670_a((BlockPos)iBlockState);
        Intrinsics.checkExpressionValueIsNotNull((Object)axisAlignedBB, (String)"wrapped.getBoundingBox(b\u2026offset(blockPos.unwrap())");
        AxisAlignedBB axisAlignedBB2 = axisAlignedBB;
        return new AxisAlignedBBImpl(axisAlignedBB2);
    }

    @Override
    @Nullable
    public IAxisAlignedBB getCollisionBoundingBox(@NotNull IWorld world, @NotNull WBlockPos pos, @NotNull IIBlockState state) {
        IAxisAlignedBB iAxisAlignedBB;
        Object $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)world, (String)"world");
        Intrinsics.checkParameterIsNotNull((Object)pos, (String)"pos");
        Intrinsics.checkParameterIsNotNull((Object)state, (String)"state");
        IIBlockState iIBlockState = state;
        Block block = this.wrapped;
        boolean $i$f$unwrap = false;
        IBlockState iBlockState = ((IBlockStateImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = world;
        $i$f$unwrap = false;
        Object object = ((WorldImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = pos;
        object = (IBlockAccess)object;
        $i$f$unwrap = false;
        BlockPos blockPos = new BlockPos(((WVec3i)$this$unwrap$iv).getX(), ((WVec3i)$this$unwrap$iv).getY(), ((WVec3i)$this$unwrap$iv).getZ());
        AxisAlignedBB axisAlignedBB = block.func_180646_a(iBlockState, object, blockPos);
        if (axisAlignedBB != null) {
            AxisAlignedBB $this$wrap$iv = axisAlignedBB;
            boolean $i$f$wrap = false;
            iAxisAlignedBB = new AxisAlignedBBImpl($this$wrap$iv);
        } else {
            iAxisAlignedBB = null;
        }
        return iAxisAlignedBB;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean canCollideCheck(@Nullable IIBlockState state, boolean hitIfLiquid) {
        IBlockState iBlockState;
        Block block = this.wrapped;
        IIBlockState iIBlockState = state;
        if (iIBlockState != null) {
            void $this$unwrap$iv;
            IIBlockState iIBlockState2 = iIBlockState;
            Block block2 = block;
            boolean $i$f$unwrap = false;
            void v2 = $this$unwrap$iv;
            if (v2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.IBlockStateImpl");
            }
            IBlockState iBlockState2 = ((IBlockStateImpl)v2).getWrapped();
            block = block2;
            iBlockState = iBlockState2;
        } else {
            iBlockState = null;
        }
        return block.func_176209_a(iBlockState, hitIfLiquid);
    }

    @NotNull
    public Void setBlockBoundsBasedOnState(@NotNull IWorld world, @NotNull WBlockPos blockPos) {
        Intrinsics.checkParameterIsNotNull((Object)world, (String)"world");
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
        Backend this_$iv = Backend.INSTANCE;
        boolean $i$f$BACKEND_UNSUPPORTED = false;
        throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
    }

    @Override
    public float getPlayerRelativeBlockHardness(@NotNull IEntityPlayerSP thePlayer, @NotNull IWorld theWorld, @NotNull WBlockPos blockPos) {
        Object $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)thePlayer, (String)"thePlayer");
        Intrinsics.checkParameterIsNotNull((Object)theWorld, (String)"theWorld");
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
        IIBlockState iIBlockState = theWorld.getBlockState(blockPos);
        Block block = this.wrapped;
        boolean $i$f$unwrap = false;
        void v0 = $this$unwrap$iv;
        if (v0 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.IBlockStateImpl");
        }
        IBlockState iBlockState = ((IBlockStateImpl)v0).getWrapped();
        $this$unwrap$iv = thePlayer;
        $i$f$unwrap = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = theWorld;
        entityPlayerSP = (EntityPlayer)entityPlayerSP;
        $i$f$unwrap = false;
        Object t2 = ((WorldImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = blockPos;
        $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos(((WVec3i)$this$unwrap$iv).getX(), ((WVec3i)$this$unwrap$iv).getY(), ((WVec3i)$this$unwrap$iv).getZ());
        return block.func_180647_a(iBlockState, (EntityPlayer)entityPlayerSP, t2, blockPos2);
    }

    @Override
    public int getIdFromBlock(@NotNull IBlock block) {
        Intrinsics.checkParameterIsNotNull((Object)block, (String)"block");
        IBlock $this$unwrap$iv = block;
        boolean $i$f$unwrap = false;
        return Block.func_149682_b((Block)((BlockImpl)$this$unwrap$iv).getWrapped());
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isTranslucent(@NotNull IIBlockState blockState) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)blockState, (String)"blockState");
        IIBlockState iIBlockState = blockState;
        Block block = this.wrapped;
        boolean $i$f$unwrap = false;
        IBlockState iBlockState = ((IBlockStateImpl)$this$unwrap$iv).getWrapped();
        return block.func_149751_l(iBlockState);
    }

    @Override
    public int getMapColor(@NotNull IIBlockState blockState, @NotNull IWorldClient theWorld, @NotNull WBlockPos bp) {
        Object $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)blockState, (String)"blockState");
        Intrinsics.checkParameterIsNotNull((Object)theWorld, (String)"theWorld");
        Intrinsics.checkParameterIsNotNull((Object)bp, (String)"bp");
        IIBlockState iIBlockState = blockState;
        Block block = this.wrapped;
        boolean $i$f$unwrap = false;
        IBlockState iBlockState = ((IBlockStateImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = theWorld;
        $i$f$unwrap = false;
        WorldClient worldClient = (WorldClient)((WorldClientImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = bp;
        worldClient = (IBlockAccess)worldClient;
        $i$f$unwrap = false;
        BlockPos blockPos = new BlockPos(((WVec3i)$this$unwrap$iv).getX(), ((WVec3i)$this$unwrap$iv).getY(), ((WVec3i)$this$unwrap$iv).getZ());
        return block.func_180659_g((IBlockState)iBlockState, (IBlockAccess)worldClient, (BlockPos)blockPos).field_76291_p;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public IMaterial getMaterial(@NotNull IIBlockState state) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)state, (String)"state");
        IIBlockState iIBlockState = state;
        Block block = this.wrapped;
        boolean $i$f$unwrap = false;
        IBlockState iBlockState = ((IBlockStateImpl)$this$unwrap$iv).getWrapped();
        Material material = block.func_149688_o(iBlockState);
        Intrinsics.checkExpressionValueIsNotNull((Object)material, (String)"wrapped.getMaterial(state.unwrap())");
        Material $this$wrap$iv = material;
        boolean $i$f$wrap = false;
        return new MaterialImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isFullCube(@NotNull IIBlockState state) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)state, (String)"state");
        IIBlockState iIBlockState = state;
        Block block = this.wrapped;
        boolean $i$f$unwrap = false;
        IBlockState iBlockState = ((IBlockStateImpl)$this$unwrap$iv).getWrapped();
        return block.func_149686_d(iBlockState);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof BlockImpl && Intrinsics.areEqual((Object)((BlockImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Block getWrapped() {
        return this.wrapped;
    }

    public BlockImpl(@NotNull Block wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}


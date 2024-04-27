/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NotImplementedError
 *  kotlin.TypeCastException
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.NotImplementedError;
import kotlin.TypeCastException;
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
import org.jetbrains.annotations.Nullable;

public final class BlockImpl
implements IBlock {
    private final Block wrapped;

    @Override
    public String getRegistryName() {
        return this.wrapped.func_149739_a();
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
    public IIBlockState getDefaultState() {
        return new IBlockStateImpl(this.wrapped.func_176223_P());
    }

    @Override
    public String getLocalizedName() {
        return this.wrapped.func_149732_F();
    }

    @Override
    public IAxisAlignedBB getSelectedBoundingBox(IWorld world, IIBlockState blockState, WBlockPos blockPos) {
        Object $this$unwrap$iv;
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
        return new AxisAlignedBBImpl(axisAlignedBB);
    }

    @Override
    public IAxisAlignedBB getCollisionBoundingBox(IWorld world, WBlockPos pos, IIBlockState state) {
        IAxisAlignedBB iAxisAlignedBB;
        Object $this$unwrap$iv;
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

    public Void setBlockBoundsBasedOnState(IWorld world, WBlockPos blockPos) {
        Backend this_$iv = Backend.INSTANCE;
        boolean $i$f$BACKEND_UNSUPPORTED = false;
        throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
    }

    @Override
    public float getPlayerRelativeBlockHardness(IEntityPlayerSP thePlayer, IWorld theWorld, WBlockPos blockPos) {
        Object $this$unwrap$iv;
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
        Object t = ((WorldImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = blockPos;
        $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos(((WVec3i)$this$unwrap$iv).getX(), ((WVec3i)$this$unwrap$iv).getY(), ((WVec3i)$this$unwrap$iv).getZ());
        return block.func_180647_a(iBlockState, (EntityPlayer)entityPlayerSP, t, blockPos2);
    }

    @Override
    public int getIdFromBlock(IBlock block) {
        IBlock $this$unwrap$iv = block;
        boolean $i$f$unwrap = false;
        return Block.func_149682_b((Block)((BlockImpl)$this$unwrap$iv).getWrapped());
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isTranslucent(IIBlockState blockState) {
        void $this$unwrap$iv;
        IIBlockState iIBlockState = blockState;
        Block block = this.wrapped;
        boolean $i$f$unwrap = false;
        IBlockState iBlockState = ((IBlockStateImpl)$this$unwrap$iv).getWrapped();
        return block.func_149751_l(iBlockState);
    }

    @Override
    public int getMapColor(IIBlockState blockState, IWorldClient theWorld, WBlockPos bp) {
        Object $this$unwrap$iv;
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
    public IMaterial getMaterial(IIBlockState state) {
        void $this$unwrap$iv;
        IIBlockState iIBlockState = state;
        Block block = this.wrapped;
        boolean $i$f$unwrap = false;
        IBlockState iBlockState = ((IBlockStateImpl)$this$unwrap$iv).getWrapped();
        Material $this$wrap$iv = block.func_149688_o(iBlockState);
        boolean $i$f$wrap = false;
        return new MaterialImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isFullCube(IIBlockState state) {
        void $this$unwrap$iv;
        IIBlockState iIBlockState = state;
        Block block = this.wrapped;
        boolean $i$f$unwrap = false;
        IBlockState iBlockState = ((IBlockStateImpl)$this$unwrap$iv).getWrapped();
        return block.func_149686_d(iBlockState);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof BlockImpl && ((BlockImpl)other).wrapped.equals(this.wrapped);
    }

    public final Block getWrapped() {
        return this.wrapped;
    }

    public BlockImpl(Block wrapped) {
        this.wrapped = wrapped;
    }
}


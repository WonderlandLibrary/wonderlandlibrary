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
 *  net.minecraft.world.World
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public final class BlockImpl
implements IBlock {
    private final Block wrapped;

    @Override
    public IAxisAlignedBB getSelectedBoundingBox(IWorld iWorld, IIBlockState iIBlockState, WBlockPos wBlockPos) {
        Object object = iIBlockState;
        Block block = this.wrapped;
        boolean bl = false;
        IBlockState iBlockState = ((IBlockStateImpl)object).getWrapped();
        object = iWorld;
        bl = false;
        World world = ((WorldImpl)object).getWrapped();
        object = wBlockPos;
        world = (IBlockAccess)world;
        bl = false;
        BlockPos blockPos = new BlockPos(((WVec3i)object).getX(), ((WVec3i)object).getY(), ((WVec3i)object).getZ());
        object = wBlockPos;
        block = block.func_185496_a(iBlockState, (IBlockAccess)world, blockPos);
        bl = false;
        iBlockState = new BlockPos(((WVec3i)object).getX(), ((WVec3i)object).getY(), ((WVec3i)object).getZ());
        AxisAlignedBB axisAlignedBB = block.func_186670_a((BlockPos)iBlockState);
        return new AxisAlignedBBImpl(axisAlignedBB);
    }

    @Override
    public boolean isTranslucent(IIBlockState iIBlockState) {
        IIBlockState iIBlockState2 = iIBlockState;
        Block block = this.wrapped;
        boolean bl = false;
        IBlockState iBlockState = ((IBlockStateImpl)iIBlockState2).getWrapped();
        return block.func_149751_l(iBlockState);
    }

    @Override
    public boolean isFullCube(IIBlockState iIBlockState) {
        IIBlockState iIBlockState2 = iIBlockState;
        Block block = this.wrapped;
        boolean bl = false;
        IBlockState iBlockState = ((IBlockStateImpl)iIBlockState2).getWrapped();
        return block.func_149686_d(iBlockState);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof BlockImpl && ((BlockImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public float getSlipperiness() {
        return this.wrapped.field_149765_K;
    }

    @Override
    public String getLocalizedName() {
        return this.wrapped.func_149732_F();
    }

    @Override
    public int getIdFromBlock(IBlock iBlock) {
        IBlock iBlock2 = iBlock;
        boolean bl = false;
        return Block.func_149682_b((Block)((BlockImpl)iBlock2).getWrapped());
    }

    @Override
    public boolean canCollideCheck(@Nullable IIBlockState iIBlockState, boolean bl) {
        IBlockState iBlockState;
        Block block = this.wrapped;
        IIBlockState iIBlockState2 = iIBlockState;
        if (iIBlockState2 != null) {
            IIBlockState iIBlockState3 = iIBlockState2;
            Block block2 = block;
            boolean bl2 = false;
            IIBlockState iIBlockState4 = iIBlockState3;
            if (iIBlockState4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.IBlockStateImpl");
            }
            IBlockState iBlockState2 = ((IBlockStateImpl)iIBlockState4).getWrapped();
            block = block2;
            iBlockState = iBlockState2;
        } else {
            iBlockState = null;
        }
        return block.func_176209_a(iBlockState, bl);
    }

    @Override
    public IMaterial getMaterial(IIBlockState iIBlockState) {
        IIBlockState iIBlockState2 = iIBlockState;
        Block block = this.wrapped;
        boolean bl = false;
        IBlockState iBlockState = ((IBlockStateImpl)iIBlockState2).getWrapped();
        iIBlockState2 = block.func_149688_o(iBlockState);
        bl = false;
        return new MaterialImpl((Material)iIBlockState2);
    }

    @Override
    public String getRegistryName() {
        return this.wrapped.func_149739_a();
    }

    @Override
    public float getPlayerRelativeBlockHardness(IEntityPlayerSP iEntityPlayerSP, IWorld iWorld, WBlockPos wBlockPos) {
        Object object = iWorld.getBlockState(wBlockPos);
        Block block = this.wrapped;
        boolean bl = false;
        IIBlockState iIBlockState = object;
        if (iIBlockState == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.IBlockStateImpl");
        }
        IBlockState iBlockState = ((IBlockStateImpl)iIBlockState).getWrapped();
        object = iEntityPlayerSP;
        bl = false;
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP)((EntityPlayerSPImpl)object).getWrapped();
        object = iWorld;
        entityPlayerSP = (EntityPlayer)entityPlayerSP;
        bl = false;
        World world = ((WorldImpl)object).getWrapped();
        object = wBlockPos;
        bl = false;
        BlockPos blockPos = new BlockPos(((WVec3i)object).getX(), ((WVec3i)object).getY(), ((WVec3i)object).getZ());
        return block.func_180647_a(iBlockState, (EntityPlayer)entityPlayerSP, world, blockPos);
    }

    @Override
    public IIBlockState getDefaultState() {
        return new IBlockStateImpl(this.wrapped.func_176223_P());
    }

    public final Block getWrapped() {
        return this.wrapped;
    }

    @Override
    public void setBlockBoundsBasedOnState(IWorld iWorld, WBlockPos wBlockPos) {
        this.setBlockBoundsBasedOnState(iWorld, wBlockPos);
    }

    public Void setBlockBoundsBasedOnState(IWorld iWorld, WBlockPos wBlockPos) {
        Backend backend = Backend.INSTANCE;
        boolean bl = false;
        throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
    }

    @Override
    public void setSlipperiness(float f) {
        this.wrapped.field_149765_K = f;
    }

    public BlockImpl(Block block) {
        this.wrapped = block;
    }

    @Override
    public int getMapColor(IIBlockState iIBlockState, IWorldClient iWorldClient, WBlockPos wBlockPos) {
        Object object = iIBlockState;
        Block block = this.wrapped;
        boolean bl = false;
        IBlockState iBlockState = ((IBlockStateImpl)object).getWrapped();
        object = iWorldClient;
        bl = false;
        WorldClient worldClient = (WorldClient)((WorldClientImpl)object).getWrapped();
        object = wBlockPos;
        worldClient = (IBlockAccess)worldClient;
        bl = false;
        BlockPos blockPos = new BlockPos(((WVec3i)object).getX(), ((WVec3i)object).getY(), ((WVec3i)object).getZ());
        return block.func_180659_g((IBlockState)iBlockState, (IBlockAccess)worldClient, (BlockPos)blockPos).field_76291_p;
    }

    @Override
    public IAxisAlignedBB getCollisionBoundingBox(IWorld iWorld, WBlockPos wBlockPos, IIBlockState iIBlockState) {
        IAxisAlignedBB iAxisAlignedBB;
        Object object = iIBlockState;
        Block block = this.wrapped;
        boolean bl = false;
        IBlockState iBlockState = ((IBlockStateImpl)object).getWrapped();
        object = iWorld;
        bl = false;
        World world = ((WorldImpl)object).getWrapped();
        object = wBlockPos;
        world = (IBlockAccess)world;
        bl = false;
        BlockPos blockPos = new BlockPos(((WVec3i)object).getX(), ((WVec3i)object).getY(), ((WVec3i)object).getZ());
        AxisAlignedBB axisAlignedBB = block.func_180646_a(iBlockState, (IBlockAccess)world, blockPos);
        if (axisAlignedBB != null) {
            object = axisAlignedBB;
            bl = false;
            iAxisAlignedBB = new AxisAlignedBBImpl((AxisAlignedBB)object);
        } else {
            iAxisAlignedBB = null;
        }
        return iAxisAlignedBB;
    }
}


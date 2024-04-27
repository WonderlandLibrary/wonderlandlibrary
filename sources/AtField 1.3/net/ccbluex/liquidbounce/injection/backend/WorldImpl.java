/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  kotlin.TypeCastException
 *  kotlin.jvm.functions.Function1
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.scoreboard.Scoreboard
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraft.world.border.WorldBorder
 *  net.minecraft.world.chunk.Chunk
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.google.common.base.Predicate;
import java.util.Collection;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreboard;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.world.IChunk;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.api.minecraft.world.border.IWorldBorder;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import net.ccbluex.liquidbounce.injection.backend.AxisAlignedBBImpl;
import net.ccbluex.liquidbounce.injection.backend.ChunkImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.ccbluex.liquidbounce.injection.backend.IBlockStateImpl;
import net.ccbluex.liquidbounce.injection.backend.MovingObjectPositionImpl;
import net.ccbluex.liquidbounce.injection.backend.ScoreboardImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldBorderImpl;
import net.ccbluex.liquidbounce.injection.backend.WorldImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

public class WorldImpl
implements IWorld {
    private final World wrapped;

    @Override
    public IScoreboard getScoreboard() {
        Scoreboard scoreboard = this.wrapped.func_96441_U();
        boolean bl = false;
        return new ScoreboardImpl(scoreboard);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof WorldImpl && ((WorldImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public Collection getEntitiesInAABBexcluding(@Nullable IEntity iEntity, IAxisAlignedBB iAxisAlignedBB, Function1 function1) {
        Entity entity;
        Entity entity2;
        boolean bl;
        World world;
        Object object;
        World world2 = this.wrapped;
        IEntity iEntity2 = iEntity;
        if (iEntity2 != null) {
            object = iEntity2;
            world = world2;
            bl = false;
            Object object2 = object;
            if (object2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.EntityImpl<*>");
            }
            entity2 = ((EntityImpl)object2).getWrapped();
            world2 = world;
            entity = entity2;
        } else {
            entity = null;
        }
        object = iAxisAlignedBB;
        entity2 = entity;
        world = world2;
        bl = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)object).getWrapped();
        Function1 function12 = getEntitiesInAABBexcluding.3.INSTANCE;
        Function1 function13 = getEntitiesInAABBexcluding.2.INSTANCE;
        Collection collection = world.func_175674_a(entity2, axisAlignedBB, new Predicate(function1){
            final Function1 $predicate;

            static {
            }
            {
                this.$predicate = function1;
            }

            public boolean apply(Object object) {
                return this.apply((Entity)object);
            }

            public final boolean apply(@Nullable Entity entity) {
                IEntity iEntity;
                Function1 function1 = this.$predicate;
                Entity entity2 = entity;
                if (entity2 != null) {
                    Entity entity3 = entity2;
                    Function1 function12 = function1;
                    boolean bl = false;
                    IEntity iEntity2 = new EntityImpl(entity3);
                    function1 = function12;
                    iEntity = iEntity2;
                } else {
                    iEntity = null;
                }
                return (Boolean)function1.invoke(iEntity);
            }
        });
        return new WrappedCollection(collection, function13, function12);
    }

    @Override
    public IIBlockState getBlockState(WBlockPos wBlockPos) {
        WBlockPos wBlockPos2 = wBlockPos;
        World world = this.wrapped;
        boolean bl = false;
        BlockPos blockPos = new BlockPos(wBlockPos2.getX(), wBlockPos2.getY(), wBlockPos2.getZ());
        wBlockPos2 = world.func_180495_p(blockPos);
        bl = false;
        return new IBlockStateImpl((IBlockState)wBlockPos2);
    }

    @Override
    public IMovingObjectPosition rayTraceBlocks(WVec3 wVec3, WVec3 wVec32, boolean bl) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 wVec33 = wVec3;
        World world = this.wrapped;
        boolean bl2 = false;
        Vec3d vec3d = new Vec3d(wVec33.getXCoord(), wVec33.getYCoord(), wVec33.getZCoord());
        wVec33 = wVec32;
        bl2 = false;
        Vec3d vec3d2 = new Vec3d(wVec33.getXCoord(), wVec33.getYCoord(), wVec33.getZCoord());
        RayTraceResult rayTraceResult = world.func_72901_a(vec3d, vec3d2, bl);
        if (rayTraceResult != null) {
            wVec33 = rayTraceResult;
            bl2 = false;
            iMovingObjectPosition = new MovingObjectPositionImpl((RayTraceResult)wVec33);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    public Collection getCollidingBoundingBoxes(IEntity iEntity, IAxisAlignedBB iAxisAlignedBB) {
        Object object = iEntity;
        World world = this.wrapped;
        boolean bl = false;
        Entity entity = ((EntityImpl)object).getWrapped();
        object = iAxisAlignedBB;
        bl = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)object).getWrapped();
        Function1 function1 = getCollidingBoundingBoxes.2.INSTANCE;
        Function1 function12 = getCollidingBoundingBoxes.1.INSTANCE;
        Collection collection = world.func_184144_a(entity, axisAlignedBB);
        return new WrappedCollection(collection, function12, function1);
    }

    @Override
    public IMovingObjectPosition rayTraceBlocks(WVec3 wVec3, WVec3 wVec32, boolean bl, boolean bl2, boolean bl3) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 wVec33 = wVec3;
        World world = this.wrapped;
        boolean bl4 = false;
        Vec3d vec3d = new Vec3d(wVec33.getXCoord(), wVec33.getYCoord(), wVec33.getZCoord());
        wVec33 = wVec32;
        bl4 = false;
        Vec3d vec3d2 = new Vec3d(wVec33.getXCoord(), wVec33.getYCoord(), wVec33.getZCoord());
        RayTraceResult rayTraceResult = world.func_147447_a(vec3d, vec3d2, bl, bl2, bl3);
        if (rayTraceResult != null) {
            wVec33 = rayTraceResult;
            bl4 = false;
            iMovingObjectPosition = new MovingObjectPositionImpl((RayTraceResult)wVec33);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    public IMovingObjectPosition rayTraceBlocks(WVec3 wVec3, WVec3 wVec32) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 wVec33 = wVec3;
        World world = this.wrapped;
        boolean bl = false;
        Vec3d vec3d = new Vec3d(wVec33.getXCoord(), wVec33.getYCoord(), wVec33.getZCoord());
        wVec33 = wVec32;
        bl = false;
        Vec3d vec3d2 = new Vec3d(wVec33.getXCoord(), wVec33.getYCoord(), wVec33.getZCoord());
        RayTraceResult rayTraceResult = world.func_72933_a(vec3d, vec3d2);
        if (rayTraceResult != null) {
            wVec33 = rayTraceResult;
            bl = false;
            iMovingObjectPosition = new MovingObjectPositionImpl((RayTraceResult)wVec33);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    public Collection getEntitiesWithinAABBExcludingEntity(@Nullable IEntity iEntity, IAxisAlignedBB iAxisAlignedBB) {
        Entity entity;
        Entity entity2;
        boolean bl;
        World world;
        Object object;
        World world2 = this.wrapped;
        IEntity iEntity2 = iEntity;
        if (iEntity2 != null) {
            object = iEntity2;
            world = world2;
            bl = false;
            Object object2 = object;
            if (object2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.EntityImpl<*>");
            }
            entity2 = ((EntityImpl)object2).getWrapped();
            world2 = world;
            entity = entity2;
        } else {
            entity = null;
        }
        object = iAxisAlignedBB;
        entity2 = entity;
        world = world2;
        bl = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)object).getWrapped();
        Function1 function1 = getEntitiesWithinAABBExcludingEntity.2.INSTANCE;
        Function1 function12 = getEntitiesWithinAABBExcludingEntity.1.INSTANCE;
        Collection collection = world.func_72839_b(entity2, axisAlignedBB);
        return new WrappedCollection(collection, function12, function1);
    }

    public WorldImpl(World world) {
        this.wrapped = world;
    }

    public final World getWrapped() {
        return this.wrapped;
    }

    @Override
    public IWorldBorder getWorldBorder() {
        WorldBorder worldBorder = this.wrapped.func_175723_af();
        boolean bl = false;
        return new WorldBorderImpl(worldBorder);
    }

    @Override
    public boolean isRemote() {
        return this.wrapped.field_72995_K;
    }

    @Override
    public IChunk getChunkFromChunkCoords(int n, int n2) {
        Chunk chunk = this.wrapped.func_72964_e(n, n2);
        boolean bl = false;
        return new ChunkImpl(chunk);
    }

    @Override
    public IEntity getEntityByID(int n) {
        IEntity iEntity;
        Entity entity = this.wrapped.func_73045_a(n);
        if (entity != null) {
            Entity entity2 = entity;
            boolean bl = false;
            iEntity = new EntityImpl(entity2);
        } else {
            iEntity = null;
        }
        return iEntity;
    }

    @Override
    public boolean checkBlockCollision(IAxisAlignedBB iAxisAlignedBB) {
        IAxisAlignedBB iAxisAlignedBB2 = iAxisAlignedBB;
        World world = this.wrapped;
        boolean bl = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)iAxisAlignedBB2).getWrapped();
        return world.func_72829_c(axisAlignedBB);
    }

    @Override
    public Collection getCollisionBoxes(IAxisAlignedBB iAxisAlignedBB) {
        IAxisAlignedBB iAxisAlignedBB2 = iAxisAlignedBB;
        Entity entity = null;
        World world = this.wrapped;
        boolean bl = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)iAxisAlignedBB2).getWrapped();
        Function1 function1 = getCollisionBoxes.2.INSTANCE;
        Function1 function12 = getCollisionBoxes.1.INSTANCE;
        Collection collection = world.func_184144_a(entity, axisAlignedBB);
        return new WrappedCollection(collection, function12, function1);
    }
}


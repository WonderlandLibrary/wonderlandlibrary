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

public class WorldImpl<T extends World>
implements IWorld {
    private final T wrapped;

    @Override
    public boolean isRemote() {
        return ((World)this.wrapped).field_72995_K;
    }

    @Override
    public IScoreboard getScoreboard() {
        Scoreboard $this$wrap$iv = this.wrapped.func_96441_U();
        boolean $i$f$wrap = false;
        return new ScoreboardImpl($this$wrap$iv);
    }

    @Override
    public IWorldBorder getWorldBorder() {
        WorldBorder $this$wrap$iv = this.wrapped.func_175723_af();
        boolean $i$f$wrap = false;
        return new WorldBorderImpl($this$wrap$iv);
    }

    @Override
    public IEntity getEntityByID(int id) {
        IEntity iEntity;
        Entity entity = this.wrapped.func_73045_a(id);
        if (entity != null) {
            Entity $this$wrap$iv = entity;
            boolean $i$f$wrap = false;
            iEntity = new EntityImpl<Entity>($this$wrap$iv);
        } else {
            iEntity = null;
        }
        return iEntity;
    }

    @Override
    public IMovingObjectPosition rayTraceBlocks(WVec3 start, WVec3 end) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 $this$unwrap$iv;
        WVec3 wVec3 = start;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        $this$unwrap$iv = end;
        $i$f$unwrap = false;
        Vec3d vec3d2 = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        RayTraceResult rayTraceResult = t.func_72933_a(vec3d, vec3d2);
        if (rayTraceResult != null) {
            RayTraceResult $this$wrap$iv = rayTraceResult;
            boolean $i$f$wrap = false;
            iMovingObjectPosition = new MovingObjectPositionImpl($this$wrap$iv);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    public IMovingObjectPosition rayTraceBlocks(WVec3 start, WVec3 end, boolean stopOnLiquid) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 $this$unwrap$iv;
        WVec3 wVec3 = start;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        $this$unwrap$iv = end;
        $i$f$unwrap = false;
        Vec3d vec3d2 = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        RayTraceResult rayTraceResult = t.func_72901_a(vec3d, vec3d2, stopOnLiquid);
        if (rayTraceResult != null) {
            RayTraceResult $this$wrap$iv = rayTraceResult;
            boolean $i$f$wrap = false;
            iMovingObjectPosition = new MovingObjectPositionImpl($this$wrap$iv);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    public IMovingObjectPosition rayTraceBlocks(WVec3 start, WVec3 end, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 $this$unwrap$iv;
        WVec3 wVec3 = start;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        $this$unwrap$iv = end;
        $i$f$unwrap = false;
        Vec3d vec3d2 = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        RayTraceResult rayTraceResult = t.func_147447_a(vec3d, vec3d2, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        if (rayTraceResult != null) {
            RayTraceResult $this$wrap$iv = rayTraceResult;
            boolean $i$f$wrap = false;
            iMovingObjectPosition = new MovingObjectPositionImpl($this$wrap$iv);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    public Collection<IEntity> getEntitiesInAABBexcluding(@Nullable IEntity entityIn, IAxisAlignedBB boundingBox, Function1<? super IEntity, Boolean> predicate) {
        Entity entity;
        Entity entity2;
        IAxisAlignedBB $this$unwrap$iv;
        boolean $i$f$unwrap;
        T t;
        T t2 = this.wrapped;
        IEntity iEntity = entityIn;
        if (iEntity != null) {
            IEntity iEntity2 = iEntity;
            t = t2;
            $i$f$unwrap = false;
            IAxisAlignedBB iAxisAlignedBB = $this$unwrap$iv;
            if (iAxisAlignedBB == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.EntityImpl<*>");
            }
            entity2 = (Entity)((EntityImpl)((Object)iAxisAlignedBB)).getWrapped();
            t2 = t;
            entity = entity2;
        } else {
            entity = null;
        }
        $this$unwrap$iv = boundingBox;
        entity2 = entity;
        t = t2;
        $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        Function1 function1 = getEntitiesInAABBexcluding.3.INSTANCE;
        Function1 function12 = getEntitiesInAABBexcluding.2.INSTANCE;
        Collection collection = t.func_175674_a(entity2, axisAlignedBB, (Predicate)new Predicate<Entity>(predicate){
            final /* synthetic */ Function1 $predicate;

            /*
             * WARNING - void declaration
             */
            public final boolean apply(@Nullable Entity it) {
                IEntity iEntity;
                Function1 function1 = this.$predicate;
                Entity entity = it;
                if (entity != null) {
                    void $this$wrap$iv;
                    Entity entity2 = entity;
                    Function1 function12 = function1;
                    boolean $i$f$wrap = false;
                    IEntity iEntity2 = new EntityImpl<void>($this$wrap$iv);
                    function1 = function12;
                    iEntity = iEntity2;
                } else {
                    iEntity = null;
                }
                return (Boolean)function1.invoke(iEntity);
            }
            {
                this.$predicate = function1;
            }
        });
        return new WrappedCollection(collection, function12, function1);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public IIBlockState getBlockState(WBlockPos blockPos) {
        void $this$unwrap$iv;
        WBlockPos wBlockPos = blockPos;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        IBlockState $this$wrap$iv = t.func_180495_p(blockPos2);
        boolean $i$f$wrap = false;
        return new IBlockStateImpl($this$wrap$iv);
    }

    @Override
    public Collection<IEntity> getEntitiesWithinAABBExcludingEntity(@Nullable IEntity entity, IAxisAlignedBB bb) {
        Entity entity2;
        Entity entity3;
        IAxisAlignedBB $this$unwrap$iv;
        boolean $i$f$unwrap;
        T t;
        T t2 = this.wrapped;
        IEntity iEntity = entity;
        if (iEntity != null) {
            IEntity iEntity2 = iEntity;
            t = t2;
            $i$f$unwrap = false;
            IAxisAlignedBB iAxisAlignedBB = $this$unwrap$iv;
            if (iAxisAlignedBB == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.EntityImpl<*>");
            }
            entity3 = (Entity)((EntityImpl)((Object)iAxisAlignedBB)).getWrapped();
            t2 = t;
            entity2 = entity3;
        } else {
            entity2 = null;
        }
        $this$unwrap$iv = bb;
        entity3 = entity2;
        t = t2;
        $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        Function1 function1 = getEntitiesWithinAABBExcludingEntity.2.INSTANCE;
        Function1 function12 = getEntitiesWithinAABBExcludingEntity.1.INSTANCE;
        Collection collection = t.func_72839_b(entity3, axisAlignedBB);
        return new WrappedCollection(collection, function12, function1);
    }

    @Override
    public Collection<IAxisAlignedBB> getCollidingBoundingBoxes(IEntity entity, IAxisAlignedBB bb) {
        IAxisAlignedBB $this$unwrap$iv;
        IEntity iEntity = entity;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t2 = ((EntityImpl)((Object)$this$unwrap$iv)).getWrapped();
        $this$unwrap$iv = bb;
        $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        Function1 function1 = getCollidingBoundingBoxes.2.INSTANCE;
        Function1 function12 = getCollidingBoundingBoxes.1.INSTANCE;
        Collection collection = t.func_184144_a(t2, axisAlignedBB);
        return new WrappedCollection(collection, function12, function1);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean checkBlockCollision(IAxisAlignedBB aabb) {
        void $this$unwrap$iv;
        IAxisAlignedBB iAxisAlignedBB = aabb;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        return t.func_72829_c(axisAlignedBB);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public Collection<IAxisAlignedBB> getCollisionBoxes(IAxisAlignedBB bb) {
        void $this$unwrap$iv;
        IAxisAlignedBB iAxisAlignedBB = bb;
        Entity entity = null;
        T t = this.wrapped;
        boolean $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        Function1 function1 = getCollisionBoxes.2.INSTANCE;
        Function1 function12 = getCollisionBoxes.1.INSTANCE;
        Collection collection = t.func_184144_a(entity, axisAlignedBB);
        return new WrappedCollection(collection, function12, function1);
    }

    @Override
    public IChunk getChunkFromChunkCoords(int x, int z) {
        Chunk $this$wrap$iv = this.wrapped.func_72964_e(x, z);
        boolean $i$f$wrap = false;
        return new ChunkImpl($this$wrap$iv);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof WorldImpl && ((WorldImpl)other).wrapped.equals(this.wrapped);
    }

    public final T getWrapped() {
        return this.wrapped;
    }

    public WorldImpl(T wrapped) {
        this.wrapped = wrapped;
    }
}


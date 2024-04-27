/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
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
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.google.common.base.Predicate;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000v\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0013\u0010\u0017\u001a\u00020\u00072\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0096\u0002J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0018\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!H\u0016J\u001e\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00160$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u0016H\u0016J\u0016\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00160$2\u0006\u0010'\u001a\u00020\u0016H\u0016J6\u0010)\u001a\b\u0012\u0004\u0012\u00020&0$2\b\u0010*\u001a\u0004\u0018\u00010&2\u0006\u0010+\u001a\u00020\u00162\u0014\u0010,\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010&\u0012\u0004\u0012\u00020\u00070-H\u0016J \u0010.\u001a\b\u0012\u0004\u0012\u00020&0$2\b\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010'\u001a\u00020\u0016H\u0016J\u0012\u0010/\u001a\u0004\u0018\u00010&2\u0006\u00100\u001a\u00020!H\u0016J\u001a\u00101\u001a\u0004\u0018\u0001022\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u000204H\u0016J\"\u00101\u001a\u0004\u0018\u0001022\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u0002042\u0006\u00106\u001a\u00020\u0007H\u0016J2\u00101\u001a\u0004\u0018\u0001022\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u0002042\u0006\u00106\u001a\u00020\u00072\u0006\u00107\u001a\u00020\u00072\u0006\u00108\u001a\u00020\u0007H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u000e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0013\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\u0011\u0010\u0012\u00a8\u00069"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/WorldImpl;", "T", "Lnet/minecraft/world/World;", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorld;", "wrapped", "(Lnet/minecraft/world/World;)V", "isRemote", "", "()Z", "scoreboard", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreboard;", "getScoreboard", "()Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreboard;", "worldBorder", "Lnet/ccbluex/liquidbounce/api/minecraft/world/border/IWorldBorder;", "getWorldBorder", "()Lnet/ccbluex/liquidbounce/api/minecraft/world/border/IWorldBorder;", "getWrapped", "()Lnet/minecraft/world/World;", "Lnet/minecraft/world/World;", "checkBlockCollision", "aabb", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "equals", "other", "", "getBlockState", "Lnet/ccbluex/liquidbounce/api/minecraft/block/state/IIBlockState;", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getChunkFromChunkCoords", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IChunk;", "x", "", "z", "getCollidingBoundingBoxes", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "bb", "getCollisionBoxes", "getEntitiesInAABBexcluding", "entityIn", "boundingBox", "predicate", "Lkotlin/Function1;", "getEntitiesWithinAABBExcludingEntity", "getEntityByID", "id", "rayTraceBlocks", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "start", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "end", "stopOnLiquid", "ignoreBlockWithoutBoundingBox", "returnLastUncollidableBlock", "LiKingSense"})
public class WorldImpl<T extends World>
implements IWorld {
    @NotNull
    private final T wrapped;

    @Override
    public boolean isRemote() {
        return ((World)this.wrapped).field_72995_K;
    }

    @Override
    @NotNull
    public IScoreboard getScoreboard() {
        Scoreboard scoreboard = this.wrapped.func_96441_U();
        Intrinsics.checkExpressionValueIsNotNull((Object)scoreboard, (String)"wrapped.scoreboard");
        Scoreboard $this$wrap$iv = scoreboard;
        boolean $i$f$wrap = false;
        return new ScoreboardImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IWorldBorder getWorldBorder() {
        WorldBorder worldBorder = this.wrapped.func_175723_af();
        Intrinsics.checkExpressionValueIsNotNull((Object)worldBorder, (String)"wrapped.worldBorder");
        WorldBorder $this$wrap$iv = worldBorder;
        boolean $i$f$wrap = false;
        return new WorldBorderImpl($this$wrap$iv);
    }

    @Override
    @Nullable
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
    @Nullable
    public IMovingObjectPosition rayTraceBlocks(@NotNull WVec3 start, @NotNull WVec3 end) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)start, (String)"start");
        Intrinsics.checkParameterIsNotNull((Object)end, (String)"end");
        WVec3 wVec3 = start;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        $this$unwrap$iv = end;
        $i$f$unwrap = false;
        Vec3d vec3d2 = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        RayTraceResult rayTraceResult = t2.func_72933_a(vec3d, vec3d2);
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
    @Nullable
    public IMovingObjectPosition rayTraceBlocks(@NotNull WVec3 start, @NotNull WVec3 end, boolean stopOnLiquid) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)start, (String)"start");
        Intrinsics.checkParameterIsNotNull((Object)end, (String)"end");
        WVec3 wVec3 = start;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        $this$unwrap$iv = end;
        $i$f$unwrap = false;
        Vec3d vec3d2 = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        RayTraceResult rayTraceResult = t2.func_72901_a(vec3d, vec3d2, stopOnLiquid);
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
    @Nullable
    public IMovingObjectPosition rayTraceBlocks(@NotNull WVec3 start, @NotNull WVec3 end, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)start, (String)"start");
        Intrinsics.checkParameterIsNotNull((Object)end, (String)"end");
        WVec3 wVec3 = start;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        $this$unwrap$iv = end;
        $i$f$unwrap = false;
        Vec3d vec3d2 = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        RayTraceResult rayTraceResult = t2.func_147447_a(vec3d, vec3d2, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
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
    @NotNull
    public Collection<IEntity> getEntitiesInAABBexcluding(@Nullable IEntity entityIn, @NotNull IAxisAlignedBB boundingBox, @NotNull Function1<? super IEntity, Boolean> predicate) {
        Entity entity;
        Entity entity2;
        IAxisAlignedBB $this$unwrap$iv;
        boolean $i$f$unwrap;
        T t2;
        Intrinsics.checkParameterIsNotNull((Object)boundingBox, (String)"boundingBox");
        Intrinsics.checkParameterIsNotNull(predicate, (String)"predicate");
        T t3 = this.wrapped;
        IEntity iEntity = entityIn;
        if (iEntity != null) {
            IEntity iEntity2 = iEntity;
            t2 = t3;
            $i$f$unwrap = false;
            IAxisAlignedBB iAxisAlignedBB = $this$unwrap$iv;
            if (iAxisAlignedBB == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.EntityImpl<*>");
            }
            entity2 = (Entity)((EntityImpl)((Object)iAxisAlignedBB)).getWrapped();
            t3 = t2;
            entity = entity2;
        } else {
            entity = null;
        }
        $this$unwrap$iv = boundingBox;
        entity2 = entity;
        t2 = t3;
        $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        Function1 function1 = getEntitiesInAABBexcluding.3.INSTANCE;
        Function1 function12 = getEntitiesInAABBexcluding.2.INSTANCE;
        Collection collection = t2.func_175674_a(entity2, axisAlignedBB, (Predicate)new Predicate<Entity>(predicate){
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
    @NotNull
    public IIBlockState getBlockState(@NotNull WBlockPos blockPos) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
        WBlockPos wBlockPos = blockPos;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        IBlockState iBlockState = t2.func_180495_p(blockPos2);
        Intrinsics.checkExpressionValueIsNotNull((Object)iBlockState, (String)"wrapped.getBlockState(blockPos.unwrap())");
        IBlockState $this$wrap$iv = iBlockState;
        boolean $i$f$wrap = false;
        return new IBlockStateImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public Collection<IEntity> getEntitiesWithinAABBExcludingEntity(@Nullable IEntity entity, @NotNull IAxisAlignedBB bb) {
        Entity entity2;
        Entity entity3;
        IAxisAlignedBB $this$unwrap$iv;
        boolean $i$f$unwrap;
        T t2;
        Intrinsics.checkParameterIsNotNull((Object)bb, (String)"bb");
        T t3 = this.wrapped;
        IEntity iEntity = entity;
        if (iEntity != null) {
            IEntity iEntity2 = iEntity;
            t2 = t3;
            $i$f$unwrap = false;
            IAxisAlignedBB iAxisAlignedBB = $this$unwrap$iv;
            if (iAxisAlignedBB == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.EntityImpl<*>");
            }
            entity3 = (Entity)((EntityImpl)((Object)iAxisAlignedBB)).getWrapped();
            t3 = t2;
            entity2 = entity3;
        } else {
            entity2 = null;
        }
        $this$unwrap$iv = bb;
        entity3 = entity2;
        t2 = t3;
        $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        Function1 function1 = getEntitiesWithinAABBExcludingEntity.2.INSTANCE;
        Function1 function12 = getEntitiesWithinAABBExcludingEntity.1.INSTANCE;
        Collection collection = t2.func_72839_b(entity3, axisAlignedBB);
        return new WrappedCollection(collection, function12, function1);
    }

    @Override
    @NotNull
    public Collection<IAxisAlignedBB> getCollidingBoundingBoxes(@NotNull IEntity entity, @NotNull IAxisAlignedBB bb) {
        IAxisAlignedBB $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        Intrinsics.checkParameterIsNotNull((Object)bb, (String)"bb");
        IEntity iEntity = entity;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t3 = ((EntityImpl)((Object)$this$unwrap$iv)).getWrapped();
        $this$unwrap$iv = bb;
        $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        Function1 function1 = getCollidingBoundingBoxes.2.INSTANCE;
        Function1 function12 = getCollidingBoundingBoxes.1.INSTANCE;
        Collection collection = t2.func_184144_a(t3, axisAlignedBB);
        return new WrappedCollection(collection, function12, function1);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean checkBlockCollision(@NotNull IAxisAlignedBB aabb) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)aabb, (String)"aabb");
        IAxisAlignedBB iAxisAlignedBB = aabb;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        return t2.func_72829_c(axisAlignedBB);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Collection<IAxisAlignedBB> getCollisionBoxes(@NotNull IAxisAlignedBB bb) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)bb, (String)"bb");
        IAxisAlignedBB iAxisAlignedBB = bb;
        Entity entity = null;
        T t2 = this.wrapped;
        boolean $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        Function1 function1 = getCollisionBoxes.2.INSTANCE;
        Function1 function12 = getCollisionBoxes.1.INSTANCE;
        Collection collection = t2.func_184144_a(entity, axisAlignedBB);
        return new WrappedCollection(collection, function12, function1);
    }

    @Override
    @NotNull
    public IChunk getChunkFromChunkCoords(int x, int z) {
        Chunk chunk = this.wrapped.func_72964_e(x, z);
        Intrinsics.checkExpressionValueIsNotNull((Object)chunk, (String)"wrapped.getChunkFromChunkCoords(x, z)");
        Chunk $this$wrap$iv = chunk;
        boolean $i$f$wrap = false;
        return new ChunkImpl($this$wrap$iv);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof WorldImpl && Intrinsics.areEqual(((WorldImpl)other).wrapped, this.wrapped);
    }

    @NotNull
    public final T getWrapped() {
        return this.wrapped;
    }

    public WorldImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}


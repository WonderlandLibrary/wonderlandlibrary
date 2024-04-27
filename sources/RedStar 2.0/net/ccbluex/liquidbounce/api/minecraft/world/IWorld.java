package net.ccbluex.liquidbounce.api.minecraft.world;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreboard;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.world.IChunk;
import net.ccbluex.liquidbounce.api.minecraft.world.border.IWorldBorder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000f\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\b\n\b\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\b\bf\u000020J\r020H&J020H&J02020H&J\b002020H&J\b0020H&J6\b002\b 02!02\"000#H&J $\b002\b020H&J%02&0H&J'0(2)0*2+0*H&J\"'0(2)0*2+0*2,0H&J2'0(2)0*2+0*2,02-02.0H&R0X¦¢\bR0X¦¢\b\bR\t0\nX¦¢\b\f¨/"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorld;", "", "isRemote", "", "()Z", "scoreboard", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreboard;", "getScoreboard", "()Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreboard;", "worldBorder", "Lnet/ccbluex/liquidbounce/api/minecraft/world/border/IWorldBorder;", "getWorldBorder", "()Lnet/ccbluex/liquidbounce/api/minecraft/world/border/IWorldBorder;", "checkBlockCollision", "aabb", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "getBlockState", "Lnet/ccbluex/liquidbounce/api/minecraft/block/state/IIBlockState;", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getChunkFromChunkCoords", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IChunk;", "x", "", "z", "getCollidingBoundingBoxes", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "bb", "getCollisionBoxes", "getEntitiesInAABBexcluding", "entityIn", "boundingBox", "predicate", "Lkotlin/Function1;", "getEntitiesWithinAABBExcludingEntity", "getEntityByID", "id", "rayTraceBlocks", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "start", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "end", "stopOnLiquid", "ignoreBlockWithoutBoundingBox", "returnLastUncollidableBlock", "Pride"})
public interface IWorld {
    public boolean isRemote();

    @NotNull
    public IScoreboard getScoreboard();

    @NotNull
    public IWorldBorder getWorldBorder();

    @Nullable
    public IEntity getEntityByID(int var1);

    @Nullable
    public IMovingObjectPosition rayTraceBlocks(@NotNull WVec3 var1, @NotNull WVec3 var2);

    @Nullable
    public IMovingObjectPosition rayTraceBlocks(@NotNull WVec3 var1, @NotNull WVec3 var2, boolean var3);

    @Nullable
    public IMovingObjectPosition rayTraceBlocks(@NotNull WVec3 var1, @NotNull WVec3 var2, boolean var3, boolean var4, boolean var5);

    @NotNull
    public Collection<IEntity> getEntitiesInAABBexcluding(@Nullable IEntity var1, @NotNull IAxisAlignedBB var2, @NotNull Function1<? super IEntity, Boolean> var3);

    @NotNull
    public IIBlockState getBlockState(@NotNull WBlockPos var1);

    @NotNull
    public Collection<IEntity> getEntitiesWithinAABBExcludingEntity(@Nullable IEntity var1, @NotNull IAxisAlignedBB var2);

    @NotNull
    public Collection<IAxisAlignedBB> getCollidingBoundingBoxes(@NotNull IEntity var1, @NotNull IAxisAlignedBB var2);

    public boolean checkBlockCollision(@NotNull IAxisAlignedBB var1);

    @NotNull
    public Collection<IAxisAlignedBB> getCollisionBoxes(@NotNull IAxisAlignedBB var1);

    @NotNull
    public IChunk getChunkFromChunkCoords(int var1, int var2);
}

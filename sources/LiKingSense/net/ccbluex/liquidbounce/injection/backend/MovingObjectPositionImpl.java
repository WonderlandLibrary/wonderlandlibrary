/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.ccbluex.liquidbounce.injection.backend.EnumFacingImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0096\u0002R\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0016\u0010\t\u001a\u0004\u0018\u00010\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u000e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0016\u0010\u0011\u001a\u0004\u0018\u00010\u00128VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u00168VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a\u00a8\u0006\u001f"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/MovingObjectPositionImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "wrapped", "Lnet/minecraft/util/math/RayTraceResult;", "(Lnet/minecraft/util/math/RayTraceResult;)V", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getBlockPos", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "entityHit", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getEntityHit", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "hitVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "getHitVec", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "sideHit", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getSideHit", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "typeOfHit", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition$WMovingObjectType;", "getTypeOfHit", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition$WMovingObjectType;", "getWrapped", "()Lnet/minecraft/util/math/RayTraceResult;", "equals", "", "other", "", "LiKingSense"})
public final class MovingObjectPositionImpl
implements IMovingObjectPosition {
    @NotNull
    private final RayTraceResult wrapped;

    @Override
    @Nullable
    public IEntity getEntityHit() {
        IEntity iEntity;
        Entity entity = this.wrapped.field_72308_g;
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
    public WBlockPos getBlockPos() {
        WBlockPos wBlockPos;
        BlockPos blockPos = this.wrapped.func_178782_a();
        if (blockPos != null) {
            BlockPos $this$wrap$iv = blockPos;
            boolean $i$f$wrap = false;
            wBlockPos = new WBlockPos($this$wrap$iv.func_177958_n(), $this$wrap$iv.func_177956_o(), $this$wrap$iv.func_177952_p());
        } else {
            wBlockPos = null;
        }
        return wBlockPos;
    }

    @Override
    @Nullable
    public IEnumFacing getSideHit() {
        IEnumFacing iEnumFacing;
        EnumFacing enumFacing = this.wrapped.field_178784_b;
        if (enumFacing != null) {
            EnumFacing $this$wrap$iv = enumFacing;
            boolean $i$f$wrap = false;
            iEnumFacing = new EnumFacingImpl($this$wrap$iv);
        } else {
            iEnumFacing = null;
        }
        return iEnumFacing;
    }

    @Override
    @NotNull
    public WVec3 getHitVec() {
        Vec3d vec3d = this.wrapped.field_72307_f;
        Intrinsics.checkExpressionValueIsNotNull((Object)vec3d, (String)"wrapped.hitVec");
        Vec3d $this$wrap$iv = vec3d;
        boolean $i$f$wrap = false;
        return new WVec3($this$wrap$iv.field_72450_a, $this$wrap$iv.field_72448_b, $this$wrap$iv.field_72449_c);
    }

    @Override
    @NotNull
    public IMovingObjectPosition.WMovingObjectType getTypeOfHit() {
        IMovingObjectPosition.WMovingObjectType wMovingObjectType;
        RayTraceResult.Type type = this.wrapped.field_72313_a;
        Intrinsics.checkExpressionValueIsNotNull((Object)type, (String)"wrapped.typeOfHit");
        RayTraceResult.Type $this$wrap$iv = type;
        boolean $i$f$wrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$0[$this$wrap$iv.ordinal()]) {
            case 1: {
                wMovingObjectType = IMovingObjectPosition.WMovingObjectType.MISS;
                break;
            }
            case 2: {
                wMovingObjectType = IMovingObjectPosition.WMovingObjectType.BLOCK;
                break;
            }
            case 3: {
                wMovingObjectType = IMovingObjectPosition.WMovingObjectType.ENTITY;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wMovingObjectType;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof MovingObjectPositionImpl && Intrinsics.areEqual((Object)((MovingObjectPositionImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final RayTraceResult getWrapped() {
        return this.wrapped;
    }

    public MovingObjectPositionImpl(@NotNull RayTraceResult wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.NoWhenBranchMatchedException;
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
import org.jetbrains.annotations.Nullable;

public final class MovingObjectPositionImpl
implements IMovingObjectPosition {
    private final RayTraceResult wrapped;

    @Override
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
    public WVec3 getHitVec() {
        Vec3d $this$wrap$iv = this.wrapped.field_72307_f;
        boolean $i$f$wrap = false;
        return new WVec3($this$wrap$iv.field_72450_a, $this$wrap$iv.field_72448_b, $this$wrap$iv.field_72449_c);
    }

    @Override
    public IMovingObjectPosition.WMovingObjectType getTypeOfHit() {
        IMovingObjectPosition.WMovingObjectType wMovingObjectType;
        RayTraceResult.Type $this$wrap$iv = this.wrapped.field_72313_a;
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
        return other instanceof MovingObjectPositionImpl && ((MovingObjectPositionImpl)other).wrapped.equals(this.wrapped);
    }

    public final RayTraceResult getWrapped() {
        return this.wrapped;
    }

    public MovingObjectPositionImpl(RayTraceResult wrapped) {
        this.wrapped = wrapped;
    }
}


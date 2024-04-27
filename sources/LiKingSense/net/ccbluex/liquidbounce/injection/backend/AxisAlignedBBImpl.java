/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.injection.backend.MovingObjectPositionImpl;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\n\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u0006H\u0016J\u001a\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001cH\u0016J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010!H\u0096\u0002J \u0010\"\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u0006H\u0016J\u0010\u0010#\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\u0001H\u0016J\u0010\u0010%\u001a\u00020\u001f2\u0006\u0010&\u001a\u00020\u001cH\u0016J \u0010'\u001a\u00020\u00012\u0006\u0010(\u001a\u00020\u00062\u0006\u0010)\u001a\u00020\u00062\u0006\u0010*\u001a\u00020\u0006H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\bR\u0014\u0010\r\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\bR\u0014\u0010\u000f\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\bR\u0014\u0010\u0011\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006+"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/AxisAlignedBBImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "wrapped", "Lnet/minecraft/util/math/AxisAlignedBB;", "(Lnet/minecraft/util/math/AxisAlignedBB;)V", "maxX", "", "getMaxX", "()D", "maxY", "getMaxY", "maxZ", "getMaxZ", "minX", "getMinX", "minY", "getMinY", "minZ", "getMinZ", "getWrapped", "()Lnet/minecraft/util/math/AxisAlignedBB;", "addCoord", "x", "y", "z", "calculateIntercept", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "from", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "to", "equals", "", "other", "", "expand", "intersectsWith", "boundingBox", "isVecInside", "vec", "offset", "sx", "sy", "sz", "LiKingSense"})
public final class AxisAlignedBBImpl
implements IAxisAlignedBB {
    @NotNull
    private final AxisAlignedBB wrapped;

    @Override
    @NotNull
    public IAxisAlignedBB addCoord(double x, double y, double z) {
        AxisAlignedBB axisAlignedBB = this.wrapped.func_72321_a(x, y, z);
        Intrinsics.checkExpressionValueIsNotNull((Object)axisAlignedBB, (String)"wrapped.expand(x, y, z)");
        AxisAlignedBB $this$wrap$iv = axisAlignedBB;
        boolean $i$f$wrap = false;
        return new AxisAlignedBBImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IAxisAlignedBB expand(double x, double y, double z) {
        AxisAlignedBB axisAlignedBB = this.wrapped.func_72314_b(x, y, z);
        Intrinsics.checkExpressionValueIsNotNull((Object)axisAlignedBB, (String)"wrapped.grow(x, y, z)");
        AxisAlignedBB $this$wrap$iv = axisAlignedBB;
        boolean $i$f$wrap = false;
        return new AxisAlignedBBImpl($this$wrap$iv);
    }

    @Override
    @Nullable
    public IMovingObjectPosition calculateIntercept(@NotNull WVec3 from, @NotNull WVec3 to) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)from, (String)"from");
        Intrinsics.checkParameterIsNotNull((Object)to, (String)"to");
        WVec3 wVec3 = from;
        AxisAlignedBB axisAlignedBB = this.wrapped;
        boolean $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        $this$unwrap$iv = to;
        $i$f$unwrap = false;
        Vec3d vec3d2 = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        RayTraceResult rayTraceResult = axisAlignedBB.func_72327_a(vec3d, vec3d2);
        if (rayTraceResult != null) {
            RayTraceResult $this$wrap$iv = rayTraceResult;
            boolean $i$f$wrap = false;
            iMovingObjectPosition = new MovingObjectPositionImpl($this$wrap$iv);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isVecInside(@NotNull WVec3 vec) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)vec, (String)"vec");
        WVec3 wVec3 = vec;
        AxisAlignedBB axisAlignedBB = this.wrapped;
        boolean $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        return axisAlignedBB.func_72318_a(vec3d);
    }

    @Override
    @NotNull
    public IAxisAlignedBB offset(double sx, double sy, double sz) {
        AxisAlignedBB axisAlignedBB = this.wrapped.func_72317_d(sx, sy, sz);
        Intrinsics.checkExpressionValueIsNotNull((Object)axisAlignedBB, (String)"wrapped.offset(sx, sy, sz)");
        AxisAlignedBB $this$wrap$iv = axisAlignedBB;
        boolean $i$f$wrap = false;
        return new AxisAlignedBBImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean intersectsWith(@NotNull IAxisAlignedBB boundingBox) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)boundingBox, (String)"boundingBox");
        IAxisAlignedBB iAxisAlignedBB = boundingBox;
        AxisAlignedBB axisAlignedBB = this.wrapped;
        boolean $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB2 = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        return axisAlignedBB.func_72326_a(axisAlignedBB2);
    }

    @Override
    public double getMinX() {
        return this.wrapped.field_72340_a;
    }

    @Override
    public double getMinY() {
        return this.wrapped.field_72338_b;
    }

    @Override
    public double getMinZ() {
        return this.wrapped.field_72339_c;
    }

    @Override
    public double getMaxX() {
        return this.wrapped.field_72336_d;
    }

    @Override
    public double getMaxY() {
        return this.wrapped.field_72337_e;
    }

    @Override
    public double getMaxZ() {
        return this.wrapped.field_72334_f;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof AxisAlignedBBImpl && Intrinsics.areEqual((Object)((AxisAlignedBBImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final AxisAlignedBB getWrapped() {
        return this.wrapped;
    }

    public AxisAlignedBBImpl(@NotNull AxisAlignedBB wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}


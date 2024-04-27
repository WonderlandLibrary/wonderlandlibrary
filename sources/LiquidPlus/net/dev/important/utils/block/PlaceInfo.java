/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.utils.block;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.utils.block.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0012"}, d2={"Lnet/dev/important/utils/block/PlaceInfo;", "", "blockPos", "Lnet/minecraft/util/BlockPos;", "enumFacing", "Lnet/minecraft/util/EnumFacing;", "vec3", "Lnet/minecraft/util/Vec3;", "(Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/util/Vec3;)V", "getBlockPos", "()Lnet/minecraft/util/BlockPos;", "getEnumFacing", "()Lnet/minecraft/util/EnumFacing;", "getVec3", "()Lnet/minecraft/util/Vec3;", "setVec3", "(Lnet/minecraft/util/Vec3;)V", "Companion", "LiquidBounce"})
public final class PlaceInfo {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final BlockPos blockPos;
    @NotNull
    private final EnumFacing enumFacing;
    @NotNull
    private Vec3 vec3;

    public PlaceInfo(@NotNull BlockPos blockPos, @NotNull EnumFacing enumFacing, @NotNull Vec3 vec3) {
        Intrinsics.checkNotNullParameter(blockPos, "blockPos");
        Intrinsics.checkNotNullParameter(enumFacing, "enumFacing");
        Intrinsics.checkNotNullParameter(vec3, "vec3");
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
        this.vec3 = vec3;
    }

    public /* synthetic */ PlaceInfo(BlockPos blockPos, EnumFacing enumFacing, Vec3 vec3, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 4) != 0) {
            vec3 = new Vec3((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 0.5, (double)blockPos.func_177952_p() + 0.5);
        }
        this(blockPos, enumFacing, vec3);
    }

    @NotNull
    public final BlockPos getBlockPos() {
        return this.blockPos;
    }

    @NotNull
    public final EnumFacing getEnumFacing() {
        return this.enumFacing;
    }

    @NotNull
    public final Vec3 getVec3() {
        return this.vec3;
    }

    public final void setVec3(@NotNull Vec3 vec3) {
        Intrinsics.checkNotNullParameter(vec3, "<set-?>");
        this.vec3 = vec3;
    }

    @JvmStatic
    @Nullable
    public static final PlaceInfo get(@NotNull BlockPos blockPos) {
        return Companion.get(blockPos);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/utils/block/PlaceInfo$Companion;", "", "()V", "get", "Lnet/dev/important/utils/block/PlaceInfo;", "blockPos", "Lnet/minecraft/util/BlockPos;", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @Nullable
        public final PlaceInfo get(@NotNull BlockPos blockPos) {
            PlaceInfo placeInfo;
            Intrinsics.checkNotNullParameter(blockPos, "blockPos");
            if (BlockUtils.canBeClicked(blockPos.func_177982_a(0, -1, 0))) {
                BlockPos blockPos2 = blockPos.func_177982_a(0, -1, 0);
                Intrinsics.checkNotNullExpressionValue(blockPos2, "blockPos.add(0, -1, 0)");
                return new PlaceInfo(blockPos2, EnumFacing.UP, null, 4, null);
            }
            if (BlockUtils.canBeClicked(blockPos.func_177982_a(0, 0, 1))) {
                BlockPos blockPos3 = blockPos.func_177982_a(0, 0, 1);
                Intrinsics.checkNotNullExpressionValue(blockPos3, "blockPos.add(0, 0, 1)");
                return new PlaceInfo(blockPos3, EnumFacing.NORTH, null, 4, null);
            }
            if (BlockUtils.canBeClicked(blockPos.func_177982_a(-1, 0, 0))) {
                BlockPos blockPos4 = blockPos.func_177982_a(-1, 0, 0);
                Intrinsics.checkNotNullExpressionValue(blockPos4, "blockPos.add(-1, 0, 0)");
                return new PlaceInfo(blockPos4, EnumFacing.EAST, null, 4, null);
            }
            if (BlockUtils.canBeClicked(blockPos.func_177982_a(0, 0, -1))) {
                BlockPos blockPos5 = blockPos.func_177982_a(0, 0, -1);
                Intrinsics.checkNotNullExpressionValue(blockPos5, "blockPos.add(0, 0, -1)");
                return new PlaceInfo(blockPos5, EnumFacing.SOUTH, null, 4, null);
            }
            if (BlockUtils.canBeClicked(blockPos.func_177982_a(1, 0, 0))) {
                BlockPos blockPos6 = blockPos.func_177982_a(1, 0, 0);
                Intrinsics.checkNotNullExpressionValue(blockPos6, "blockPos.add(1, 0, 0)");
                PlaceInfo placeInfo2 = new PlaceInfo(blockPos6, EnumFacing.WEST, null, 4, null);
                placeInfo = placeInfo2;
            } else {
                placeInfo = null;
            }
            return placeInfo;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}


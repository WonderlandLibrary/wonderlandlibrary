/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.utils.Rotation;
import net.dev.important.utils.block.PlaceInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2={"Lnet/dev/important/utils/PlaceRotation;", "", "placeInfo", "Lnet/dev/important/utils/block/PlaceInfo;", "rotation", "Lnet/dev/important/utils/Rotation;", "(Lnet/dev/important/utils/block/PlaceInfo;Lnet/dev/important/utils/Rotation;)V", "getPlaceInfo", "()Lnet/dev/important/utils/block/PlaceInfo;", "getRotation", "()Lnet/dev/important/utils/Rotation;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "LiquidBounce"})
public final class PlaceRotation {
    @NotNull
    private final PlaceInfo placeInfo;
    @NotNull
    private final Rotation rotation;

    public PlaceRotation(@NotNull PlaceInfo placeInfo, @NotNull Rotation rotation) {
        Intrinsics.checkNotNullParameter(placeInfo, "placeInfo");
        Intrinsics.checkNotNullParameter(rotation, "rotation");
        this.placeInfo = placeInfo;
        this.rotation = rotation;
    }

    @NotNull
    public final PlaceInfo getPlaceInfo() {
        return this.placeInfo;
    }

    @NotNull
    public final Rotation getRotation() {
        return this.rotation;
    }

    @NotNull
    public final PlaceInfo component1() {
        return this.placeInfo;
    }

    @NotNull
    public final Rotation component2() {
        return this.rotation;
    }

    @NotNull
    public final PlaceRotation copy(@NotNull PlaceInfo placeInfo, @NotNull Rotation rotation) {
        Intrinsics.checkNotNullParameter(placeInfo, "placeInfo");
        Intrinsics.checkNotNullParameter(rotation, "rotation");
        return new PlaceRotation(placeInfo, rotation);
    }

    public static /* synthetic */ PlaceRotation copy$default(PlaceRotation placeRotation, PlaceInfo placeInfo, Rotation rotation, int n, Object object) {
        if ((n & 1) != 0) {
            placeInfo = placeRotation.placeInfo;
        }
        if ((n & 2) != 0) {
            rotation = placeRotation.rotation;
        }
        return placeRotation.copy(placeInfo, rotation);
    }

    @NotNull
    public String toString() {
        return "PlaceRotation(placeInfo=" + this.placeInfo + ", rotation=" + this.rotation + ')';
    }

    public int hashCode() {
        int result = this.placeInfo.hashCode();
        result = result * 31 + this.rotation.hashCode();
        return result;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PlaceRotation)) {
            return false;
        }
        PlaceRotation placeRotation = (PlaceRotation)other;
        if (!Intrinsics.areEqual(this.placeInfo, placeRotation.placeInfo)) {
            return false;
        }
        return Intrinsics.areEqual(this.rotation, placeRotation.rotation);
    }
}


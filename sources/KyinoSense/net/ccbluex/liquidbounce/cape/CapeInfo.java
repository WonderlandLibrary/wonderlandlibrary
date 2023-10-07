/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.cape;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00052\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0007\"\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/cape/CapeInfo;", "", "resourceLocation", "Lnet/minecraft/util/ResourceLocation;", "isCapeAvailable", "", "(Lnet/minecraft/util/ResourceLocation;Z)V", "()Z", "setCapeAvailable", "(Z)V", "getResourceLocation", "()Lnet/minecraft/util/ResourceLocation;", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "", "KyinoClient"})
public final class CapeInfo {
    @NotNull
    private final ResourceLocation resourceLocation;
    private boolean isCapeAvailable;

    @NotNull
    public final ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public final boolean isCapeAvailable() {
        return this.isCapeAvailable;
    }

    public final void setCapeAvailable(boolean bl) {
        this.isCapeAvailable = bl;
    }

    public CapeInfo(@NotNull ResourceLocation resourceLocation, boolean isCapeAvailable) {
        Intrinsics.checkParameterIsNotNull(resourceLocation, "resourceLocation");
        this.resourceLocation = resourceLocation;
        this.isCapeAvailable = isCapeAvailable;
    }

    public /* synthetic */ CapeInfo(ResourceLocation resourceLocation, boolean bl, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 2) != 0) {
            bl = false;
        }
        this(resourceLocation, bl);
    }

    @NotNull
    public final ResourceLocation component1() {
        return this.resourceLocation;
    }

    public final boolean component2() {
        return this.isCapeAvailable;
    }

    @NotNull
    public final CapeInfo copy(@NotNull ResourceLocation resourceLocation, boolean isCapeAvailable) {
        Intrinsics.checkParameterIsNotNull(resourceLocation, "resourceLocation");
        return new CapeInfo(resourceLocation, isCapeAvailable);
    }

    public static /* synthetic */ CapeInfo copy$default(CapeInfo capeInfo, ResourceLocation resourceLocation, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            resourceLocation = capeInfo.resourceLocation;
        }
        if ((n & 2) != 0) {
            bl = capeInfo.isCapeAvailable;
        }
        return capeInfo.copy(resourceLocation, bl);
    }

    @NotNull
    public String toString() {
        return "CapeInfo(resourceLocation=" + this.resourceLocation + ", isCapeAvailable=" + this.isCapeAvailable + ")";
    }

    public int hashCode() {
        ResourceLocation resourceLocation = this.resourceLocation;
        int n = (resourceLocation != null ? resourceLocation.hashCode() : 0) * 31;
        int n2 = this.isCapeAvailable ? 1 : 0;
        if (n2 != 0) {
            n2 = 1;
        }
        return n + n2;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof CapeInfo)) break block3;
                CapeInfo capeInfo = (CapeInfo)object;
                if (!Intrinsics.areEqual(this.resourceLocation, capeInfo.resourceLocation) || this.isCapeAvailable != capeInfo.isCapeAvailable) break block3;
            }
            return true;
        }
        return false;
    }
}


package net.ccbluex.liquidbounce.cape;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000$\n\n\u0000\n\u0000\n\n\u0000\n\n\b\f\n\b\n\u0000\n\n\u0000\b\b\u000020B0\b\b0¢J\t\f0HÆJ\t\r0HÆJ0\u00002\b\b02\b\b0HÆJ02\b0HÖJ\t0HÖJ\t0HÖR0X¢\n\u0000\b\"\b\b\tR0¢\b\n\u0000\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/cape/CapeInfo;", "", "resourceLocation", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "isCapeAvailable", "", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;Z)V", "()Z", "setCapeAvailable", "(Z)V", "getResourceLocation", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "", "Pride"})
public final class CapeInfo {
    @NotNull
    private final IResourceLocation resourceLocation;
    private boolean isCapeAvailable;

    @NotNull
    public final IResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public final boolean isCapeAvailable() {
        return this.isCapeAvailable;
    }

    public final void setCapeAvailable(boolean bl) {
        this.isCapeAvailable = bl;
    }

    public CapeInfo(@NotNull IResourceLocation resourceLocation, boolean isCapeAvailable) {
        Intrinsics.checkParameterIsNotNull(resourceLocation, "resourceLocation");
        this.resourceLocation = resourceLocation;
        this.isCapeAvailable = isCapeAvailable;
    }

    public CapeInfo(IResourceLocation iResourceLocation, boolean bl, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 2) != 0) {
            bl = false;
        }
        this(iResourceLocation, bl);
    }

    @NotNull
    public final IResourceLocation component1() {
        return this.resourceLocation;
    }

    public final boolean component2() {
        return this.isCapeAvailable;
    }

    @NotNull
    public final CapeInfo copy(@NotNull IResourceLocation resourceLocation, boolean isCapeAvailable) {
        Intrinsics.checkParameterIsNotNull(resourceLocation, "resourceLocation");
        return new CapeInfo(resourceLocation, isCapeAvailable);
    }

    public static CapeInfo copy$default(CapeInfo capeInfo, IResourceLocation iResourceLocation, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            iResourceLocation = capeInfo.resourceLocation;
        }
        if ((n & 2) != 0) {
            bl = capeInfo.isCapeAvailable;
        }
        return capeInfo.copy(iResourceLocation, bl);
    }

    @NotNull
    public String toString() {
        return "CapeInfo(resourceLocation=" + this.resourceLocation + ", isCapeAvailable=" + this.isCapeAvailable + ")";
    }

    public int hashCode() {
        IResourceLocation iResourceLocation = this.resourceLocation;
        int n = (iResourceLocation != null ? iResourceLocation.hashCode() : 0) * 31;
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

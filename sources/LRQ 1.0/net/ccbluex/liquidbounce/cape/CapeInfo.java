/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.cape;

import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class CapeInfo {
    private final IResourceLocation resourceLocation;
    private boolean isCapeAvailable;

    public final IResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public final boolean isCapeAvailable() {
        return this.isCapeAvailable;
    }

    public final void setCapeAvailable(boolean bl) {
        this.isCapeAvailable = bl;
    }

    public CapeInfo(IResourceLocation resourceLocation, boolean isCapeAvailable) {
        this.resourceLocation = resourceLocation;
        this.isCapeAvailable = isCapeAvailable;
    }

    public /* synthetic */ CapeInfo(IResourceLocation iResourceLocation, boolean bl, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 2) != 0) {
            bl = false;
        }
        this(iResourceLocation, bl);
    }

    public final IResourceLocation component1() {
        return this.resourceLocation;
    }

    public final boolean component2() {
        return this.isCapeAvailable;
    }

    public final CapeInfo copy(IResourceLocation resourceLocation, boolean isCapeAvailable) {
        return new CapeInfo(resourceLocation, isCapeAvailable);
    }

    public static /* synthetic */ CapeInfo copy$default(CapeInfo capeInfo, IResourceLocation iResourceLocation, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            iResourceLocation = capeInfo.resourceLocation;
        }
        if ((n & 2) != 0) {
            bl = capeInfo.isCapeAvailable;
        }
        return capeInfo.copy(iResourceLocation, bl);
    }

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
                if (!this.resourceLocation.equals(capeInfo.resourceLocation) || this.isCapeAvailable != capeInfo.isCapeAvailable) break block3;
            }
            return true;
        }
        return false;
    }
}


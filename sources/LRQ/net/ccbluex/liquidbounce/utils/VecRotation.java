/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.Rotation;
import org.jetbrains.annotations.Nullable;

public final class VecRotation {
    private final WVec3 vec;
    private final Rotation rotation;

    public final WVec3 getVec() {
        return this.vec;
    }

    public final Rotation getRotation() {
        return this.rotation;
    }

    public VecRotation(WVec3 vec, Rotation rotation) {
        this.vec = vec;
        this.rotation = rotation;
    }

    public final WVec3 component1() {
        return this.vec;
    }

    public final Rotation component2() {
        return this.rotation;
    }

    public final VecRotation copy(WVec3 vec, Rotation rotation) {
        return new VecRotation(vec, rotation);
    }

    public static /* synthetic */ VecRotation copy$default(VecRotation vecRotation, WVec3 wVec3, Rotation rotation, int n, Object object) {
        if ((n & 1) != 0) {
            wVec3 = vecRotation.vec;
        }
        if ((n & 2) != 0) {
            rotation = vecRotation.rotation;
        }
        return vecRotation.copy(wVec3, rotation);
    }

    public String toString() {
        return "VecRotation(vec=" + this.vec + ", rotation=" + this.rotation + ")";
    }

    public int hashCode() {
        WVec3 wVec3 = this.vec;
        Rotation rotation = this.rotation;
        return (wVec3 != null ? ((Object)wVec3).hashCode() : 0) * 31 + (rotation != null ? ((Object)rotation).hashCode() : 0);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof VecRotation)) break block3;
                VecRotation vecRotation = (VecRotation)object;
                if (!((Object)this.vec).equals(vecRotation.vec) || !((Object)this.rotation).equals(vecRotation.rotation)) break block3;
            }
            return true;
        }
        return false;
    }
}


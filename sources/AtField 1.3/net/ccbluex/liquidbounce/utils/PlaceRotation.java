/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import org.jetbrains.annotations.Nullable;

public final class PlaceRotation {
    private final Rotation rotation;
    private final PlaceInfo placeInfo;

    public final PlaceRotation copy(PlaceInfo placeInfo, Rotation rotation) {
        return new PlaceRotation(placeInfo, rotation);
    }

    public final PlaceInfo component1() {
        return this.placeInfo;
    }

    public final PlaceInfo getPlaceInfo() {
        return this.placeInfo;
    }

    public int hashCode() {
        PlaceInfo placeInfo = this.placeInfo;
        Rotation rotation = this.rotation;
        return (placeInfo != null ? placeInfo.hashCode() : 0) * 31 + (rotation != null ? ((Object)rotation).hashCode() : 0);
    }

    public final Rotation component2() {
        return this.rotation;
    }

    public PlaceRotation(PlaceInfo placeInfo, Rotation rotation) {
        this.placeInfo = placeInfo;
        this.rotation = rotation;
    }

    public final Rotation getRotation() {
        return this.rotation;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof PlaceRotation)) break block3;
                PlaceRotation placeRotation = (PlaceRotation)object;
                if (!this.placeInfo.equals(placeRotation.placeInfo) || !((Object)this.rotation).equals(placeRotation.rotation)) break block3;
            }
            return true;
        }
        return false;
    }

    public static PlaceRotation copy$default(PlaceRotation placeRotation, PlaceInfo placeInfo, Rotation rotation, int n, Object object) {
        if ((n & 1) != 0) {
            placeInfo = placeRotation.placeInfo;
        }
        if ((n & 2) != 0) {
            rotation = placeRotation.rotation;
        }
        return placeRotation.copy(placeInfo, rotation);
    }

    public String toString() {
        return "PlaceRotation(placeInfo=" + this.placeInfo + ", rotation=" + this.rotation + ")";
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.TypeCastException;
import org.jetbrains.annotations.Nullable;

public class WVec3i {
    private final int x;
    private final int y;
    private final int z;

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        Object object = other;
        if (this.getClass().equals(object != null ? object.getClass() : null) ^ true) {
            return false;
        }
        Object object2 = other;
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.util.WVec3i");
        }
        WVec3i cfr_ignored_0 = (WVec3i)object2;
        if (this.x != ((WVec3i)other).x) {
            return false;
        }
        if (this.y != ((WVec3i)other).y) {
            return false;
        }
        return this.z == ((WVec3i)other).z;
    }

    public int hashCode() {
        int result = this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }

    public final int getX() {
        return this.x;
    }

    public final int getY() {
        return this.y;
    }

    public final int getZ() {
        return this.z;
    }

    public WVec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public WVec3i(double x, double y, double z) {
        WVec3i wVec3i = this;
        boolean bl = false;
        double d = Math.floor(x);
        int n = (int)d;
        bl = false;
        double d2 = Math.floor(y);
        int n2 = (int)d2;
        bl = false;
        double d3 = Math.floor(z);
        wVec3i(n, n2, (int)d3);
    }
}


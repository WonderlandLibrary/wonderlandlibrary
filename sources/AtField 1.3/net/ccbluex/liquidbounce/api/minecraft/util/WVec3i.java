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
    private final int z;
    private final int y;

    public final int getX() {
        return this.x;
    }

    public final int getY() {
        return this.y;
    }

    public WVec3i(int n, int n2, int n3) {
        this.x = n;
        this.y = n2;
        this.z = n3;
    }

    public final int getZ() {
        return this.z;
    }

    public WVec3i(double d, double d2, double d3) {
        WVec3i wVec3i = this;
        boolean bl = false;
        double d4 = Math.floor(d);
        int n = (int)d4;
        bl = false;
        double d5 = Math.floor(d2);
        int n2 = (int)d5;
        bl = false;
        double d6 = Math.floor(d3);
        wVec3i(n, n2, (int)d6);
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        Object object2 = object;
        if (this.getClass().equals(object2 != null ? object2.getClass() : null) ^ true) {
            return false;
        }
        Object object3 = object;
        if (object3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.util.WVec3i");
        }
        WVec3i cfr_ignored_0 = (WVec3i)object3;
        if (this.x != ((WVec3i)object).x) {
            return false;
        }
        if (this.y != ((WVec3i)object).y) {
            return false;
        }
        return this.z == ((WVec3i)object).z;
    }

    public int hashCode() {
        int n = this.x;
        n = 31 * n + this.y;
        n = 31 * n + this.z;
        return n;
    }
}


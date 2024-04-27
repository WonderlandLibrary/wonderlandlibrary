/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.RayTraceResult
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.injection.backend.MovingObjectPositionImpl;
import net.minecraft.util.math.RayTraceResult;

public final class MovingObjectPositionImplKt {
    public static final RayTraceResult unwrap(IMovingObjectPosition iMovingObjectPosition) {
        boolean bl = false;
        return ((MovingObjectPositionImpl)iMovingObjectPosition).getWrapped();
    }

    public static final IMovingObjectPosition wrap(RayTraceResult rayTraceResult) {
        boolean bl = false;
        return new MovingObjectPositionImpl(rayTraceResult);
    }
}


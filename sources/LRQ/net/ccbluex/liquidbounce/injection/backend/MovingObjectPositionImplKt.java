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
    public static final RayTraceResult unwrap(IMovingObjectPosition $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((MovingObjectPositionImpl)$this$unwrap).getWrapped();
    }

    public static final IMovingObjectPosition wrap(RayTraceResult $this$wrap) {
        int $i$f$wrap = 0;
        return new MovingObjectPositionImpl($this$wrap);
    }
}


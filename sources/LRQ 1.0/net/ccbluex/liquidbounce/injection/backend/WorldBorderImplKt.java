/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.border.WorldBorder
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.world.border.IWorldBorder;
import net.ccbluex.liquidbounce.injection.backend.WorldBorderImpl;
import net.minecraft.world.border.WorldBorder;

public final class WorldBorderImplKt {
    public static final WorldBorder unwrap(IWorldBorder $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((WorldBorderImpl)$this$unwrap).getWrapped();
    }

    public static final IWorldBorder wrap(WorldBorder $this$wrap) {
        int $i$f$wrap = 0;
        return new WorldBorderImpl($this$wrap);
    }
}


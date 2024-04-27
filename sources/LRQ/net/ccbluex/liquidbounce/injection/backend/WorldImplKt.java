/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.World
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.injection.backend.WorldImpl;
import net.minecraft.world.World;

public final class WorldImplKt {
    public static final World unwrap(IWorld $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((WorldImpl)$this$unwrap).getWrapped();
    }

    public static final IWorld wrap(World $this$wrap) {
        int $i$f$wrap = 0;
        return new WorldImpl<World>($this$wrap);
    }
}


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
    public static final IWorldBorder wrap(WorldBorder worldBorder) {
        boolean bl = false;
        return new WorldBorderImpl(worldBorder);
    }

    public static final WorldBorder unwrap(IWorldBorder iWorldBorder) {
        boolean bl = false;
        return ((WorldBorderImpl)iWorldBorder).getWrapped();
    }
}


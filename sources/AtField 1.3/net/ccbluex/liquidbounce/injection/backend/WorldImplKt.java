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
    public static final IWorld wrap(World world) {
        boolean bl = false;
        return new WorldImpl(world);
    }

    public static final World unwrap(IWorld iWorld) {
        boolean bl = false;
        return ((WorldImpl)iWorld).getWrapped();
    }
}


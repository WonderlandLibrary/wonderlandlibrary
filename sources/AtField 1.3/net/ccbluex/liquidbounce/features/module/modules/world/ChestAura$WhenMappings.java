/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.event.EventState;

public final class ChestAura$WhenMappings {
    public static final int[] $EnumSwitchMapping$0 = new int[EventState.values().length];

    static {
        ChestAura$WhenMappings.$EnumSwitchMapping$0[EventState.PRE.ordinal()] = 1;
        ChestAura$WhenMappings.$EnumSwitchMapping$0[EventState.POST.ordinal()] = 2;
    }
}


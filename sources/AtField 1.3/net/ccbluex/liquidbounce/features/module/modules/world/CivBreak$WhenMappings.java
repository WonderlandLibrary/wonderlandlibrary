/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.event.EventState;

public final class CivBreak$WhenMappings {
    public static final int[] $EnumSwitchMapping$0 = new int[EventState.values().length];

    static {
        CivBreak$WhenMappings.$EnumSwitchMapping$0[EventState.PRE.ordinal()] = 1;
        CivBreak$WhenMappings.$EnumSwitchMapping$0[EventState.POST.ordinal()] = 2;
    }
}


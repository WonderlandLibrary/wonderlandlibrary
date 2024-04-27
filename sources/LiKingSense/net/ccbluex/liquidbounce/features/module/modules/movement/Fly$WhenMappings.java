/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.EventState;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3)
public final class Fly$WhenMappings {
    public static final /* synthetic */ int[] $EnumSwitchMapping$0;

    static {
        $EnumSwitchMapping$0 = new int[EventState.values().length];
        Fly$WhenMappings.$EnumSwitchMapping$0[EventState.PRE.ordinal()] = 1;
        Fly$WhenMappings.$EnumSwitchMapping$0[EventState.POST.ordinal()] = 2;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.utils.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.utils.render.Animation;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3)
public final class Animation$WhenMappings {
    public static final /* synthetic */ int[] $EnumSwitchMapping$0;

    static {
        $EnumSwitchMapping$0 = new int[Animation.EnumAnimationState.values().length];
        Animation$WhenMappings.$EnumSwitchMapping$0[Animation.EnumAnimationState.NOT_STARTED.ordinal()] = 1;
        Animation$WhenMappings.$EnumSwitchMapping$0[Animation.EnumAnimationState.DURING.ordinal()] = 2;
        Animation$WhenMappings.$EnumSwitchMapping$0[Animation.EnumAnimationState.STOPPED.ordinal()] = 3;
    }
}


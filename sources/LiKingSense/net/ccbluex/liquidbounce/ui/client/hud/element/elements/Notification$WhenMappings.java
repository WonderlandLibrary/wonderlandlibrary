/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.FadeState;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3)
public final class Notification$WhenMappings {
    public static final /* synthetic */ int[] $EnumSwitchMapping$0;
    public static final /* synthetic */ int[] $EnumSwitchMapping$1;

    static {
        $EnumSwitchMapping$0 = new int[FadeState.values().length];
        Notification$WhenMappings.$EnumSwitchMapping$0[FadeState.IN.ordinal()] = 1;
        Notification$WhenMappings.$EnumSwitchMapping$0[FadeState.STAY.ordinal()] = 2;
        Notification$WhenMappings.$EnumSwitchMapping$0[FadeState.OUT.ordinal()] = 3;
        Notification$WhenMappings.$EnumSwitchMapping$0[FadeState.END.ordinal()] = 4;
        $EnumSwitchMapping$1 = new int[NotifyType.values().length];
        Notification$WhenMappings.$EnumSwitchMapping$1[NotifyType.SUCCESS.ordinal()] = 1;
        Notification$WhenMappings.$EnumSwitchMapping$1[NotifyType.ERROR.ordinal()] = 2;
        Notification$WhenMappings.$EnumSwitchMapping$1[NotifyType.WARNING.ordinal()] = 3;
        Notification$WhenMappings.$EnumSwitchMapping$1[NotifyType.INFO.ordinal()] = 4;
    }
}


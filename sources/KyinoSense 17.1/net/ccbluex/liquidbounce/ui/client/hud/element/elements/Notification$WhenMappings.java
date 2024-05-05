/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, xi=2)
public final class Notification$WhenMappings {
    public static final /* synthetic */ int[] $EnumSwitchMapping$0;
    public static final /* synthetic */ int[] $EnumSwitchMapping$1;

    static {
        $EnumSwitchMapping$0 = new int[Notification.Type.values().length];
        Notification$WhenMappings.$EnumSwitchMapping$0[Notification.Type.SUCCESS.ordinal()] = 1;
        Notification$WhenMappings.$EnumSwitchMapping$0[Notification.Type.ERROR.ordinal()] = 2;
        Notification$WhenMappings.$EnumSwitchMapping$0[Notification.Type.WARNING.ordinal()] = 3;
        Notification$WhenMappings.$EnumSwitchMapping$0[Notification.Type.INFO.ordinal()] = 4;
        $EnumSwitchMapping$1 = new int[Notification.FadeState.values().length];
        Notification$WhenMappings.$EnumSwitchMapping$1[Notification.FadeState.IN.ordinal()] = 1;
        Notification$WhenMappings.$EnumSwitchMapping$1[Notification.FadeState.STAY.ordinal()] = 2;
        Notification$WhenMappings.$EnumSwitchMapping$1[Notification.FadeState.OUT.ordinal()] = 3;
        Notification$WhenMappings.$EnumSwitchMapping$1[Notification.FadeState.END.ordinal()] = 4;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.ui.client.hud.element.elements.FadeState;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;

public final class Notification$WhenMappings {
    public static final /* synthetic */ int[] $EnumSwitchMapping$0;
    public static final /* synthetic */ int[] $EnumSwitchMapping$1;
    public static final /* synthetic */ int[] $EnumSwitchMapping$2;

    static {
        $EnumSwitchMapping$0 = new int[FadeState.values().length];
        Notification$WhenMappings.$EnumSwitchMapping$0[FadeState.IN.ordinal()] = 1;
        Notification$WhenMappings.$EnumSwitchMapping$0[FadeState.STAY.ordinal()] = 2;
        Notification$WhenMappings.$EnumSwitchMapping$0[FadeState.OUT.ordinal()] = 3;
        Notification$WhenMappings.$EnumSwitchMapping$0[FadeState.END.ordinal()] = 4;
        $EnumSwitchMapping$1 = new int[NotifyType.values().length];
        Notification$WhenMappings.$EnumSwitchMapping$1[NotifyType.ERROR.ordinal()] = 1;
        Notification$WhenMappings.$EnumSwitchMapping$1[NotifyType.INFO.ordinal()] = 2;
        Notification$WhenMappings.$EnumSwitchMapping$1[NotifyType.SUCCESS.ordinal()] = 3;
        Notification$WhenMappings.$EnumSwitchMapping$1[NotifyType.WARNING.ordinal()] = 4;
        $EnumSwitchMapping$2 = new int[NotifyType.values().length];
        Notification$WhenMappings.$EnumSwitchMapping$2[NotifyType.ERROR.ordinal()] = 1;
        Notification$WhenMappings.$EnumSwitchMapping$2[NotifyType.INFO.ordinal()] = 2;
        Notification$WhenMappings.$EnumSwitchMapping$2[NotifyType.SUCCESS.ordinal()] = 3;
        Notification$WhenMappings.$EnumSwitchMapping$2[NotifyType.WARNING.ordinal()] = 4;
    }
}


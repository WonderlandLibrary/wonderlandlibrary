/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub;

import net.ccbluex.liquidbounce.utils.login.LoginUtils;

public final class GuiSessionLogin$WhenMappings {
    public static final /* synthetic */ int[] $EnumSwitchMapping$0;

    static {
        $EnumSwitchMapping$0 = new int[LoginUtils.LoginResult.values().length];
        GuiSessionLogin$WhenMappings.$EnumSwitchMapping$0[LoginUtils.LoginResult.LOGGED.ordinal()] = 1;
        GuiSessionLogin$WhenMappings.$EnumSwitchMapping$0[LoginUtils.LoginResult.FAILED_PARSE_TOKEN.ordinal()] = 2;
        GuiSessionLogin$WhenMappings.$EnumSwitchMapping$0[LoginUtils.LoginResult.INVALID_ACCOUNT_DATA.ordinal()] = 3;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.utils.login.LoginUtils;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, xi=2)
public final class GuiSessionLogin$WhenMappings {
    public static final /* synthetic */ int[] $EnumSwitchMapping$0;

    static {
        $EnumSwitchMapping$0 = new int[LoginUtils.LoginResult.values().length];
        GuiSessionLogin$WhenMappings.$EnumSwitchMapping$0[LoginUtils.LoginResult.LOGGED.ordinal()] = 1;
        GuiSessionLogin$WhenMappings.$EnumSwitchMapping$0[LoginUtils.LoginResult.FAILED_PARSE_TOKEN.ordinal()] = 2;
        GuiSessionLogin$WhenMappings.$EnumSwitchMapping$0[LoginUtils.LoginResult.INVALID_ACCOUNT_DATA.ordinal()] = 3;
    }
}


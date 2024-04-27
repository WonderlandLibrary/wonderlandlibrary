/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.utils.extensions;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;

public final class NetworkPlayerInfoKt {
    public static final String getFullName(INetworkPlayerInfo iNetworkPlayerInfo) {
        if (iNetworkPlayerInfo.getDisplayName() != null) {
            IIChatComponent iIChatComponent = iNetworkPlayerInfo.getDisplayName();
            if (iIChatComponent == null) {
                Intrinsics.throwNpe();
            }
            return iIChatComponent.getFormattedText();
        }
        ITeam iTeam = iNetworkPlayerInfo.getPlayerTeam();
        String string = iNetworkPlayerInfo.getGameProfile().getName();
        Object object = iTeam;
        if (object == null || (object = object.formatString(string)) == null) {
            object = string;
        }
        return object;
    }
}


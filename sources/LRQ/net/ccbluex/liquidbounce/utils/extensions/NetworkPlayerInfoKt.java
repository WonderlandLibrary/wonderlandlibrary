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
    public static final String getFullName(INetworkPlayerInfo $this$getFullName) {
        if ($this$getFullName.getDisplayName() != null) {
            IIChatComponent iIChatComponent = $this$getFullName.getDisplayName();
            if (iIChatComponent == null) {
                Intrinsics.throwNpe();
            }
            return iIChatComponent.getFormattedText();
        }
        ITeam team = $this$getFullName.getPlayerTeam();
        String name = $this$getFullName.getGameProfile().getName();
        Object object = team;
        if (object == null || (object = object.formatString(name)) == null) {
            object = name;
        }
        return object;
    }
}


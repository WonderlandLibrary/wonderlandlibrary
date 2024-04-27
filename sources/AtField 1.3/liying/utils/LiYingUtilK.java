/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  org.jetbrains.annotations.Nullable
 */
package liying.utils;

import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.Nullable;

public final class LiYingUtilK
extends MinecraftInstance {
    public static final LiYingUtilK INSTANCE;

    public final int getPing(@Nullable EntityPlayer entityPlayer) {
        INetworkPlayerInfo iNetworkPlayerInfo;
        if (entityPlayer == null) {
            return 0;
        }
        INetworkPlayerInfo iNetworkPlayerInfo2 = iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(entityPlayer.func_110124_au());
        return iNetworkPlayerInfo2 != null ? iNetworkPlayerInfo2.getResponseTime() : 0;
    }

    private LiYingUtilK() {
    }

    static {
        LiYingUtilK liYingUtilK;
        INSTANCE = liYingUtilK = new LiYingUtilK();
    }
}


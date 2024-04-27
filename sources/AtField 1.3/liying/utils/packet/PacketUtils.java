/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.client.CPacketKeepAlive
 */
package liying.utils.packet;

import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;

public final class PacketUtils
extends MinecraftInstance {
    public static List pList = new ArrayList();

    public static void send(CPacketKeepAlive cPacketKeepAlive) {
    }

    public static void send(CPacketConfirmTransaction cPacketConfirmTransaction) {
    }

    public static void send(IPacket iPacket) {
        pList.add(iPacket);
        mc.getNetHandler().getNetworkManager().sendPacket(iPacket);
    }
}


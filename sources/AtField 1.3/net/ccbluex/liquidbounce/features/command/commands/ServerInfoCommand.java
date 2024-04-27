/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.handshake.client.ICPacketHandshake;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class ServerInfoCommand
extends Command
implements Listenable {
    private int port;
    private String ip;

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isCPacketHandshake(iPacket)) {
            ICPacketHandshake iCPacketHandshake = iPacket.asCPacketHandshake();
            this.ip = iCPacketHandshake.getIp();
            this.port = iCPacketHandshake.getPort();
        }
    }

    @Override
    public void execute(String[] stringArray) {
        if (MinecraftInstance.mc.getCurrentServerData() == null) {
            this.chat("This command does not work in single player.");
            return;
        }
        IServerData iServerData = MinecraftInstance.mc.getCurrentServerData();
        if (iServerData == null) {
            return;
        }
        IServerData iServerData2 = iServerData;
        this.chat("Server infos:");
        this.chat("\u00a77Name: \u00a78" + iServerData2.getServerName());
        this.chat("\u00a77IP: \u00a78" + this.ip + ':' + this.port);
        this.chat("\u00a77Players: \u00a78" + iServerData2.getPopulationInfo());
        this.chat("\u00a77MOTD: \u00a78" + iServerData2.getServerMOTD());
        this.chat("\u00a77ServerVersion: \u00a78" + iServerData2.getGameVersion());
        this.chat("\u00a77ProtocolVersion: \u00a78" + iServerData2.getVersion());
        this.chat("\u00a77Ping: \u00a78" + iServerData2.getPingToServer());
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public ServerInfoCommand() {
        super("serverinfo", new String[0]);
        LiquidBounce.INSTANCE.getEventManager().registerListener(this);
        this.ip = "";
    }
}


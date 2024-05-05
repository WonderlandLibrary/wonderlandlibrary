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
    private String ip;
    private int port;

    @Override
    public void execute(String[] args) {
        if (MinecraftInstance.mc.getCurrentServerData() == null) {
            this.chat("This command does not work in single player.");
            return;
        }
        IServerData iServerData = MinecraftInstance.mc.getCurrentServerData();
        if (iServerData == null) {
            return;
        }
        IServerData data = iServerData;
        this.chat("Server infos:");
        this.chat("\u00a77Name: \u00a78" + data.getServerName());
        this.chat("\u00a77IP: \u00a78" + this.ip + ':' + this.port);
        this.chat("\u00a77Players: \u00a78" + data.getPopulationInfo());
        this.chat("\u00a77MOTD: \u00a78" + data.getServerMOTD());
        this.chat("\u00a77ServerVersion: \u00a78" + data.getGameVersion());
        this.chat("\u00a77ProtocolVersion: \u00a78" + data.getVersion());
        this.chat("\u00a77Ping: \u00a78" + data.getPingToServer());
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketHandshake(packet)) {
            ICPacketHandshake handshake = packet.asCPacketHandshake();
            this.ip = handshake.getIp();
            this.port = handshake.getPort();
        }
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


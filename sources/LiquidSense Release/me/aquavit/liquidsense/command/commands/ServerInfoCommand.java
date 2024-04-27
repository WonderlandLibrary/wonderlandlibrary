package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.Listenable;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.command.Command;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;

public class ServerInfoCommand extends Command implements Listenable {
    private String ip;
    private int port;

    public ServerInfoCommand() {
        super("serverinfo");
        LiquidSense.eventManager.registerListener(this);
    }

    @Override
    public void execute(final String[] args) {
        if (mc.getCurrentServerData() == null) {
            chat("This command only work on a server.");
            return;
        }
        final ServerData data = mc.getCurrentServerData();
        chat("Server infos:");
        chat("§7Name: §8" + data.serverName);
        chat("§7IP: §8" + this.ip + ':' + this.port);
        chat("§7Players: §8" + data.populationInfo);
        chat("§7MOTD: §8" + data.serverMOTD);
        chat("§7ServerVersion: §8" + data.gameVersion);
        chat("§7ProtocolVersion: §8" + data.version);
        chat("§7Ping: §8" + data.pingToServer);
    }

    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof C00Handshake) {
            final C00Handshake handshake = (C00Handshake)packet;
            ip = handshake.ip;
            port = handshake.port;
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.commands.defaultsubs;

import java.util.Iterator;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashSet;
import com.viaversion.viaversion.api.Via;
import java.util.Set;
import java.util.HashMap;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;

public class PPSSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "pps";
    }
    
    @Override
    public String description() {
        return "Shows the packets per second of online players";
    }
    
    @Override
    public String usage() {
        return "pps";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        final Map<Integer, Set<String>> playerVersions = new HashMap<Integer, Set<String>>();
        int totalPackets = 0;
        int clients = 0;
        long max = 0L;
        for (final ViaCommandSender p : Via.getPlatform().getOnlinePlayers()) {
            final int playerVersion = Via.getAPI().getPlayerVersion(p.getUUID());
            if (!playerVersions.containsKey(playerVersion)) {
                playerVersions.put(playerVersion, new HashSet<String>());
            }
            final UserConnection uc = Via.getManager().getConnectionManager().getConnectedClient(p.getUUID());
            if (uc != null && uc.getPacketTracker().getPacketsPerSecond() > -1L) {
                playerVersions.get(playerVersion).add(p.getName() + " (" + uc.getPacketTracker().getPacketsPerSecond() + " PPS)");
                totalPackets += (int)uc.getPacketTracker().getPacketsPerSecond();
                if (uc.getPacketTracker().getPacketsPerSecond() > max) {
                    max = uc.getPacketTracker().getPacketsPerSecond();
                }
                ++clients;
            }
        }
        final Map<Integer, Set<String>> sorted = new TreeMap<Integer, Set<String>>(playerVersions);
        ViaSubCommand.sendMessage(sender, "&4Live Packets Per Second", new Object[0]);
        if (clients > 1) {
            ViaSubCommand.sendMessage(sender, "&cAverage: &f" + totalPackets / clients, new Object[0]);
            ViaSubCommand.sendMessage(sender, "&cHighest: &f" + max, new Object[0]);
        }
        if (clients == 0) {
            ViaSubCommand.sendMessage(sender, "&cNo clients to display.", new Object[0]);
        }
        for (final Map.Entry<Integer, Set<String>> entry : sorted.entrySet()) {
            ViaSubCommand.sendMessage(sender, "&8[&6%s&8]: &b%s", ProtocolVersion.getProtocol(entry.getKey()).getName(), entry.getValue());
        }
        sorted.clear();
        return true;
    }
}

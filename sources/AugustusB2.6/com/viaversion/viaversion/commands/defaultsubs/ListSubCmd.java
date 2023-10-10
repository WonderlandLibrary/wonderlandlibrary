// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.commands.defaultsubs;

import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
import com.viaversion.viaversion.api.Via;
import java.util.Set;
import java.util.TreeMap;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;

public class ListSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "list";
    }
    
    @Override
    public String description() {
        return "Shows lists of the versions from logged in players";
    }
    
    @Override
    public String usage() {
        return "list";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        final Map<ProtocolVersion, Set<String>> playerVersions = new TreeMap<ProtocolVersion, Set<String>>((o1, o2) -> ProtocolVersion.getIndex(o2) - ProtocolVersion.getIndex(o1));
        for (final ViaCommandSender p : Via.getPlatform().getOnlinePlayers()) {
            final int playerVersion = Via.getAPI().getPlayerVersion(p.getUUID());
            final ProtocolVersion key = ProtocolVersion.getProtocol(playerVersion);
            playerVersions.computeIfAbsent(key, s -> new HashSet()).add(p.getName());
        }
        for (final Map.Entry<ProtocolVersion, Set<String>> entry : playerVersions.entrySet()) {
            ViaSubCommand.sendMessage(sender, "&8[&6%s&8] (&7%d&8): &b%s", entry.getKey().getName(), entry.getValue().size(), entry.getValue());
        }
        playerVersions.clear();
        return true;
    }
}

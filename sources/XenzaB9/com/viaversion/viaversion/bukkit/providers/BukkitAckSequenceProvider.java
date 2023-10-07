// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.providers;

import org.bukkit.plugin.Plugin;
import com.viaversion.viaversion.bukkit.tasks.protocol1_19to1_18_2.AckSequenceTask;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.storage.SequenceStorage;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.ViaVersionPlugin;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.provider.AckSequenceProvider;

public final class BukkitAckSequenceProvider extends AckSequenceProvider
{
    private final ViaVersionPlugin plugin;
    
    public BukkitAckSequenceProvider(final ViaVersionPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void handleSequence(final UserConnection connection, final int sequence) {
        final SequenceStorage sequenceStorage = connection.get(SequenceStorage.class);
        final int previousSequence = sequenceStorage.setSequenceId(sequence);
        if (previousSequence == -1) {
            final int serverProtocolVersion = connection.getProtocolInfo().getServerProtocolVersion();
            final long delay = (serverProtocolVersion > ProtocolVersion.v1_8.getVersion() && serverProtocolVersion < ProtocolVersion.v1_14.getVersion()) ? 2L : 1L;
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new AckSequenceTask(connection, sequenceStorage), delay);
        }
    }
}

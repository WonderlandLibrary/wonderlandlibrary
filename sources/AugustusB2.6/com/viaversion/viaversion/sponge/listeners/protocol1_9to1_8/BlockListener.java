// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.listeners.protocol1_9to1_8;

import org.spongepowered.api.event.Listener;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.SpongePlugin;
import com.viaversion.viaversion.sponge.listeners.ViaSpongeListener;

public class BlockListener extends ViaSpongeListener
{
    public BlockListener(final SpongePlugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }
    
    @Listener
    public void placeBlock(final ChangeBlockEvent.Place e, @Root final Player player) {
        if (this.isOnPipe(player.getUniqueId())) {
            final Location loc = ((BlockSnapshot)e.getTransactions().get(0).getFinal()).getLocation().get();
            final EntityTracker1_9 tracker = this.getUserConnection(player.getUniqueId()).getEntityTracker(Protocol1_9To1_8.class);
            tracker.addBlockInteraction(new Position(loc.getBlockX(), (short)loc.getBlockY(), loc.getBlockZ()));
        }
    }
}

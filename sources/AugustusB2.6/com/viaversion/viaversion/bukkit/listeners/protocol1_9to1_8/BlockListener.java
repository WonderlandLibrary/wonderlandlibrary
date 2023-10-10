// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import org.bukkit.event.block.BlockPlaceEvent;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import org.bukkit.plugin.Plugin;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;

public class BlockListener extends ViaBukkitListener
{
    public BlockListener(final Plugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void placeBlock(final BlockPlaceEvent e) {
        if (this.isOnPipe(e.getPlayer())) {
            final Block b = e.getBlockPlaced();
            final EntityTracker1_9 tracker = this.getUserConnection(e.getPlayer()).getEntityTracker(Protocol1_9To1_8.class);
            tracker.addBlockInteraction(new Position(b.getX(), (short)b.getY(), b.getZ()));
        }
    }
}

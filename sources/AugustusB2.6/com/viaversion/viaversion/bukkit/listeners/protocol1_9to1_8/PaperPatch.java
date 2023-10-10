// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPlaceEvent;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import org.bukkit.plugin.Plugin;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;

public class PaperPatch extends ViaBukkitListener
{
    public PaperPatch(final Plugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlace(final BlockPlaceEvent e) {
        if (this.isOnPipe(e.getPlayer())) {
            final Material block = e.getBlockPlaced().getType();
            if (this.isPlacable(block)) {
                return;
            }
            final Location location = e.getPlayer().getLocation();
            final Block locationBlock = location.getBlock();
            if (locationBlock.equals(e.getBlock())) {
                e.setCancelled(true);
            }
            else if (locationBlock.getRelative(BlockFace.UP).equals(e.getBlock())) {
                e.setCancelled(true);
            }
            else {
                final Location diff = location.clone().subtract(e.getBlock().getLocation().add(0.5, 0.0, 0.5));
                if (Math.abs(diff.getX()) <= 0.8 && Math.abs(diff.getZ()) <= 0.8) {
                    if (diff.getY() <= 0.1 && diff.getY() >= -0.1) {
                        e.setCancelled(true);
                        return;
                    }
                    final BlockFace relative = e.getBlockAgainst().getFace(e.getBlock());
                    if (relative == BlockFace.UP && diff.getY() < 1.0 && diff.getY() >= 0.0) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    
    private boolean isPlacable(final Material material) {
        if (!material.isSolid()) {
            return true;
        }
        switch (material.getId()) {
            case 63:
            case 68:
            case 176:
            case 177: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
}

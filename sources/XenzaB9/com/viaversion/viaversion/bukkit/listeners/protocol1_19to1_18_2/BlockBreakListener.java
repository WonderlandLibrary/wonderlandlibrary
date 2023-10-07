// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.listeners.protocol1_19to1_18_2;

import com.viaversion.viaversion.bukkit.util.NMSUtil;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.block.BlockState;
import org.bukkit.block.Block;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.Via;
import org.bukkit.event.block.BlockBreakEvent;
import com.viaversion.viaversion.api.protocol.Protocol;
import org.bukkit.plugin.Plugin;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viaversion.ViaVersionPlugin;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;

public final class BlockBreakListener extends ViaBukkitListener
{
    private static final Class<?> CRAFT_BLOCK_STATE_CLASS;
    
    public BlockBreakListener(final ViaVersionPlugin plugin) {
        super((Plugin)plugin, Protocol1_19To1_18_2.class);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockBreak(final BlockBreakEvent event) {
        final Block block = event.getBlock();
        if (!event.isCancelled() || !this.isBlockEntity(block.getState())) {
            return;
        }
        final int serverProtocolVersion = Via.getAPI().getServerVersion().highestSupportedVersion();
        final long delay = (serverProtocolVersion > ProtocolVersion.v1_8.getVersion() && serverProtocolVersion < ProtocolVersion.v1_14.getVersion()) ? 2L : 1L;
        this.getPlugin().getServer().getScheduler().runTaskLater(this.getPlugin(), () -> {
            final BlockState state = block.getState();
            if (this.isBlockEntity(state)) {
                state.update(true, false);
            }
        }, delay);
    }
    
    private boolean isBlockEntity(final BlockState state) {
        return state.getClass() != BlockBreakListener.CRAFT_BLOCK_STATE_CLASS;
    }
    
    static {
        try {
            CRAFT_BLOCK_STATE_CLASS = NMSUtil.obc("block.CraftBlockState");
        }
        catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

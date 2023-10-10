// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.providers;

import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.UUID;
import org.bukkit.Bukkit;
import com.viaversion.viaversion.api.connection.UserConnection;
import org.bukkit.Chunk;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;

public class BukkitBlockConnectionProvider extends BlockConnectionProvider
{
    private Chunk lastChunk;
    
    @Override
    public int getWorldBlockData(final UserConnection user, final int bx, final int by, final int bz) {
        final UUID uuid = user.getProtocolInfo().getUuid();
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            final World world = player.getWorld();
            final int x = bx >> 4;
            final int z = bz >> 4;
            if (world.isChunkLoaded(x, z)) {
                final Chunk c = this.getChunk(world, x, z);
                final Block b = c.getBlock(bx, by, bz);
                return b.getTypeId() << 4 | b.getData();
            }
        }
        return 0;
    }
    
    public Chunk getChunk(final World world, final int x, final int z) {
        if (this.lastChunk != null && this.lastChunk.getX() == x && this.lastChunk.getZ() == z) {
            return this.lastChunk;
        }
        return this.lastChunk = world.getChunkAt(x, z);
    }
}

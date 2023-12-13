/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 */
package com.viaversion.viaversion.bukkit.providers;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BukkitBlockConnectionProvider
extends BlockConnectionProvider {
    private Chunk lastChunk;

    @Override
    public int getWorldBlockData(UserConnection user, int bx, int by, int bz) {
        int z2;
        int x2;
        World world;
        UUID uuid = user.getProtocolInfo().getUuid();
        Player player = Bukkit.getPlayer((UUID)uuid);
        if (player != null && (world = player.getWorld()).isChunkLoaded(x2 = bx >> 4, z2 = bz >> 4)) {
            Chunk c2 = this.getChunk(world, x2, z2);
            Block b2 = c2.getBlock(bx, by, bz);
            return b2.getTypeId() << 4 | b2.getData();
        }
        return 0;
    }

    public Chunk getChunk(World world, int x2, int z2) {
        if (this.lastChunk != null && this.lastChunk.getX() == x2 && this.lastChunk.getZ() == z2) {
            return this.lastChunk;
        }
        this.lastChunk = world.getChunkAt(x2, z2);
        return this.lastChunk;
    }
}


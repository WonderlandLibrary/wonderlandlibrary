// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.block.Lectern;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import com.viaversion.viaversion.api.protocol.Protocol;
import org.bukkit.plugin.Plugin;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import com.viaversion.viabackwards.BukkitPlugin;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;

public class LecternInteractListener extends ViaBukkitListener
{
    public LecternInteractListener(final BukkitPlugin plugin) {
        super((Plugin)plugin, Protocol1_13_2To1_14.class);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLecternInteract(final PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.LECTERN) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.isOnPipe(player)) {
            return;
        }
        final Lectern lectern = (Lectern)block.getState();
        final ItemStack book = lectern.getInventory().getItem(0);
        if (book == null) {
            return;
        }
        final BookMeta meta = (BookMeta)book.getItemMeta();
        final ItemStack newBook = new ItemStack(Material.WRITTEN_BOOK);
        final BookMeta newBookMeta = (BookMeta)newBook.getItemMeta();
        newBookMeta.setPages(meta.getPages());
        newBookMeta.setAuthor("an upsidedown person");
        newBookMeta.setTitle("buk");
        newBook.setItemMeta((ItemMeta)newBookMeta);
        player.openBook(newBook);
        event.setCancelled(true);
    }
}

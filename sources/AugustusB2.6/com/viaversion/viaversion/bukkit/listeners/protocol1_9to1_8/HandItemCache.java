// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import org.bukkit.inventory.ItemStack;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.UUID;
import java.util.Map;
import org.bukkit.scheduler.BukkitRunnable;

public class HandItemCache extends BukkitRunnable
{
    private final Map<UUID, Item> handCache;
    
    public HandItemCache() {
        this.handCache = new ConcurrentHashMap<UUID, Item>();
    }
    
    public void run() {
        final List<UUID> players = new ArrayList<UUID>(this.handCache.keySet());
        for (final Player p : Bukkit.getOnlinePlayers()) {
            this.handCache.put(p.getUniqueId(), convert(p.getItemInHand()));
            players.remove(p.getUniqueId());
        }
        for (final UUID uuid : players) {
            this.handCache.remove(uuid);
        }
    }
    
    public Item getHandItem(final UUID player) {
        return this.handCache.get(player);
    }
    
    public static Item convert(final ItemStack itemInHand) {
        if (itemInHand == null) {
            return new DataItem(0, (byte)0, (short)0, null);
        }
        return new DataItem(itemInHand.getTypeId(), (byte)itemInHand.getAmount(), itemInHand.getDurability(), null);
    }
}

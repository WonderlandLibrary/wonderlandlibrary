// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.tasks.protocol1_12to1_11_1;

import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.util.Collections;
import java.util.ArrayList;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.storage.ItemTransaction;
import java.util.List;
import java.util.UUID;
import com.viaversion.viaversion.bukkit.providers.BukkitInventoryQuickMoveProvider;

public class BukkitInventoryUpdateTask implements Runnable
{
    private final BukkitInventoryQuickMoveProvider provider;
    private final UUID uuid;
    private final List<ItemTransaction> items;
    
    public BukkitInventoryUpdateTask(final BukkitInventoryQuickMoveProvider provider, final UUID uuid) {
        this.provider = provider;
        this.uuid = uuid;
        this.items = Collections.synchronizedList(new ArrayList<ItemTransaction>());
    }
    
    public void addItem(final short windowId, final short slotId, final short actionId) {
        final ItemTransaction storage = new ItemTransaction(windowId, slotId, actionId);
        this.items.add(storage);
    }
    
    @Override
    public void run() {
        final Player p = Bukkit.getServer().getPlayer(this.uuid);
        if (p == null) {
            this.provider.onTaskExecuted(this.uuid);
            return;
        }
        try {
            synchronized (this.items) {
                for (final ItemTransaction storage : this.items) {
                    final Object packet = this.provider.buildWindowClickPacket(p, storage);
                    final boolean result = this.provider.sendPacketToServer(p, packet);
                    if (!result) {
                        break;
                    }
                }
                this.items.clear();
            }
        }
        finally {
            this.provider.onTaskExecuted(this.uuid);
        }
    }
}

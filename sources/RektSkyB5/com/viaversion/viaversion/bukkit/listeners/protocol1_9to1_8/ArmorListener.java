/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemBreakEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.inventory.CraftingInventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ArmorType;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ArmorListener
extends ViaBukkitListener {
    private static final UUID ARMOR_ATTRIBUTE = UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150");

    public ArmorListener(Plugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }

    public void sendArmorUpdate(Player player) {
        if (!this.isOnPipe(player)) {
            return;
        }
        int armor = 0;
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            armor += ArmorType.findById(stack.getTypeId()).getArmorPoints();
        }
        PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.ENTITY_PROPERTIES, null, this.getUserConnection(player));
        try {
            wrapper.write(Type.VAR_INT, player.getEntityId());
            wrapper.write(Type.INT, 1);
            wrapper.write(Type.STRING, "generic.armor");
            wrapper.write(Type.DOUBLE, 0.0);
            wrapper.write(Type.VAR_INT, 1);
            wrapper.write(Type.UUID, ARMOR_ATTRIBUTE);
            wrapper.write(Type.DOUBLE, Double.valueOf(armor));
            wrapper.write(Type.BYTE, (byte)0);
            wrapper.scheduleSend(Protocol1_9To1_8.class);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onInventoryClick(InventoryClickEvent e2) {
        HumanEntity human = e2.getWhoClicked();
        if (human instanceof Player && e2.getInventory() instanceof CraftingInventory) {
            Player player = (Player)human;
            if (e2.getCurrentItem() != null && ArmorType.isArmor(e2.getCurrentItem().getTypeId())) {
                this.sendDelayedArmorUpdate(player);
                return;
            }
            if (e2.getRawSlot() >= 5 && e2.getRawSlot() <= 8) {
                this.sendDelayedArmorUpdate(player);
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onInteract(PlayerInteractEvent e2) {
        if (e2.getItem() != null && (e2.getAction() == Action.RIGHT_CLICK_AIR || e2.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            Player player = e2.getPlayer();
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> this.sendArmorUpdate(player), 3L);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onItemBreak(PlayerItemBreakEvent e2) {
        this.sendDelayedArmorUpdate(e2.getPlayer());
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onJoin(PlayerJoinEvent e2) {
        this.sendDelayedArmorUpdate(e2.getPlayer());
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onRespawn(PlayerRespawnEvent e2) {
        this.sendDelayedArmorUpdate(e2.getPlayer());
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onWorldChange(PlayerChangedWorldEvent e2) {
        this.sendArmorUpdate(e2.getPlayer());
    }

    public void sendDelayedArmorUpdate(Player player) {
        if (!this.isOnPipe(player)) {
            return;
        }
        Via.getPlatform().runSync(() -> this.sendArmorUpdate(player));
    }
}


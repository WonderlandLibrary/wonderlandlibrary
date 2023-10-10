// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.SoundCategory;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import com.viaversion.viaversion.api.protocol.Protocol;
import org.bukkit.plugin.Plugin;
import com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import com.viaversion.viabackwards.BukkitPlugin;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;

public class FireDamageListener extends ViaBukkitListener
{
    public FireDamageListener(final BukkitPlugin plugin) {
        super((Plugin)plugin, Protocol1_11_1To1_12.class);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFireDamage(final EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        final EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != EntityDamageEvent.DamageCause.FIRE && cause != EntityDamageEvent.DamageCause.FIRE_TICK && cause != EntityDamageEvent.DamageCause.LAVA && cause != EntityDamageEvent.DamageCause.DROWNING) {
            return;
        }
        final Player player = (Player)event.getEntity();
        if (this.isOnPipe(player)) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
    }
}

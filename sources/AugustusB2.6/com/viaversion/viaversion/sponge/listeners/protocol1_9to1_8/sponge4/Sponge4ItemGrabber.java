// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.listeners.protocol1_9to1_8.sponge4;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.entity.living.player.Player;
import com.viaversion.viaversion.sponge.listeners.protocol1_9to1_8.ItemGrabber;

public class Sponge4ItemGrabber implements ItemGrabber
{
    @Override
    public ItemStack getItem(final Player player) {
        return player.getItemInHand().orElse(null);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.listeners.protocol1_9to1_8.sponge5;

import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.entity.living.player.Player;
import com.viaversion.viaversion.sponge.listeners.protocol1_9to1_8.ItemGrabber;

public class Sponge5ItemGrabber implements ItemGrabber
{
    @Override
    public ItemStack getItem(final Player player) {
        return player.getItemInHand(HandTypes.MAIN_HAND).orElse(null);
    }
}

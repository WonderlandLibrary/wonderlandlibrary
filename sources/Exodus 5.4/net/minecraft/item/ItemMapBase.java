/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class ItemMapBase
extends Item {
    public Packet createMapDataPacket(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public boolean isMap() {
        return true;
    }
}


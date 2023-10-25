/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.util.CooldownTracker;

public class CooldownTrackerServer
extends CooldownTracker {
    private final EntityPlayerMP player;

    public CooldownTrackerServer(EntityPlayerMP playerIn) {
        this.player = playerIn;
    }

    @Override
    protected void notifyOnSet(Item itemIn, int ticksIn) {
        super.notifyOnSet(itemIn, ticksIn);
        this.player.connection.sendPacket(new SPacketCooldown(itemIn, ticksIn));
    }

    @Override
    protected void notifyOnRemove(Item itemIn) {
        super.notifyOnRemove(itemIn);
        this.player.connection.sendPacket(new SPacketCooldown(itemIn, 0));
    }
}


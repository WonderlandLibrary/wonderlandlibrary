// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayerMP;

public class CooldownTrackerServer extends CooldownTracker
{
    private final EntityPlayerMP player;
    
    public CooldownTrackerServer(final EntityPlayerMP playerIn) {
        this.player = playerIn;
    }
    
    @Override
    protected void notifyOnSet(final Item itemIn, final int ticksIn) {
        super.notifyOnSet(itemIn, ticksIn);
        this.player.connection.sendPacket(new SPacketCooldown(itemIn, ticksIn));
    }
    
    @Override
    protected void notifyOnRemove(final Item itemIn) {
        super.notifyOnRemove(itemIn);
        this.player.connection.sendPacket(new SPacketCooldown(itemIn, 0));
    }
}

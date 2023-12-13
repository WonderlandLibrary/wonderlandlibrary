// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.event.events.EventSendPacket;
import java.util.LinkedList;
import com.elementars.eclient.module.Category;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.Queue;
import com.elementars.eclient.module.Module;

public class Blink extends Module
{
    Queue<CPacketPlayer> packets;
    private EntityOtherPlayerMP clonedPlayer;
    
    public Blink() {
        super("Blink", "Hides movement for a short distance", 0, Category.PLAYER, true);
        this.packets = new LinkedList<CPacketPlayer>();
    }
    
    @EventTarget
    public void onPacket(final EventSendPacket event) {
        if (this.isToggled() && event.getPacket() instanceof CPacketPlayer) {
            event.setCancelled(true);
            this.packets.add((CPacketPlayer)event.getPacket());
        }
    }
    
    @Override
    public void onEnable() {
        Xulu.EVENT_MANAGER.register(this);
        if (Blink.mc.player != null) {
            (this.clonedPlayer = new EntityOtherPlayerMP((World)Blink.mc.world, Blink.mc.getSession().getProfile())).copyLocationAndAnglesFrom((Entity)Blink.mc.player);
            this.clonedPlayer.rotationYawHead = Blink.mc.player.rotationYawHead;
            Blink.mc.world.addEntityToWorld(-100, (Entity)this.clonedPlayer);
        }
    }
    
    @Override
    public void onDisable() {
        Xulu.EVENT_MANAGER.unregister(this);
        while (!this.packets.isEmpty()) {
            Blink.mc.player.connection.sendPacket((Packet)this.packets.poll());
        }
        final EntityPlayer localPlayer = (EntityPlayer)Blink.mc.player;
        if (localPlayer != null) {
            Blink.mc.world.removeEntityFromWorld(-100);
            this.clonedPlayer = null;
        }
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(this.packets.size());
    }
}

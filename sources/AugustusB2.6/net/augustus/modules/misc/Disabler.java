// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.misc;

import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.augustus.utils.RandomUtil;
import net.augustus.events.EventSendPacket;
import java.util.Iterator;
import java.util.function.Predicate;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.augustus.events.EventRender3D;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.augustus.events.EventUpdate;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.utils.custompackets.CustomC00PacketKeepAlive;
import java.util.ArrayList;
import net.augustus.modules.Module;

public class Disabler extends Module
{
    private final ArrayList<CustomC00PacketKeepAlive> keepAlivePackets;
    private final int counter = 0;
    public BooleanValue pingSpoof;
    public BooleanValue royalPixels;
    public BooleanValue minemenStrafe;
    public BooleanValue spectate;
    public BooleanValue keepAlive;
    public BooleanValue ping;
    public BooleanValue entityAction;
    public BooleanValue playerAbilities;
    public BooleanValue confirmTransaction;
    public DoubleValue delay;
    
    public Disabler() {
        super("Disabler", new Color(73, 127, 163), Categorys.MISC);
        this.keepAlivePackets = new ArrayList<CustomC00PacketKeepAlive>();
        this.pingSpoof = new BooleanValue(0, "PingSpoof", this, false);
        this.royalPixels = new BooleanValue(8, "RoyalPixels", this, false);
        this.minemenStrafe = new BooleanValue(9, "MinemenStrafe", this, false);
        this.spectate = new BooleanValue(0, "Spectate", this, false);
        this.keepAlive = new BooleanValue(2, "C00PacketKeepAlive", this, false);
        this.ping = new BooleanValue(3, "C01PacketPing", this, false);
        this.entityAction = new BooleanValue(4, "C0BPacketEntityAction", this, false);
        this.playerAbilities = new BooleanValue(5, "C13PacketPlayerAbilities", this, false);
        this.confirmTransaction = new BooleanValue(7, "C0FPacketConfirmTransaction", this, false);
        this.delay = new DoubleValue(1, "Delay", this, 1000.0, 1.0, 4000.0, 0);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.keepAlivePackets.clear();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (!this.keepAlivePackets.isEmpty()) {
            this.keepAlivePackets.clear();
        }
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        if (this.royalPixels.getBoolean()) {
            final C13PacketPlayerAbilities capabilities = new C13PacketPlayerAbilities();
            capabilities.setAllowFlying(true);
            capabilities.setFlying(true);
            Disabler.mc.thePlayer.sendQueue.addToSendQueue(capabilities);
        }
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        if (this.pingSpoof.getBoolean() && Disabler.mc.thePlayer != null && !this.keepAlivePackets.isEmpty()) {
            final ArrayList<CustomC00PacketKeepAlive> toRemove = new ArrayList<CustomC00PacketKeepAlive>();
            for (final CustomC00PacketKeepAlive packet : this.keepAlivePackets) {
                if (packet.getTime() < System.currentTimeMillis()) {
                    Disabler.mc.thePlayer.sendQueue.addToSendQueueDirect(new C00PacketKeepAlive(packet.getKey()));
                    toRemove.add(packet);
                }
            }
            this.keepAlivePackets.removeIf(toRemove::contains);
        }
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        if (this.pingSpoof.getBoolean() && packet instanceof C00PacketKeepAlive) {
            final C00PacketKeepAlive c00PacketKeepAlive = (C00PacketKeepAlive)packet;
            this.keepAlivePackets.add(new CustomC00PacketKeepAlive(c00PacketKeepAlive.getKey(), (long)(System.currentTimeMillis() + this.delay.getValue() + RandomUtil.nextLong(0L, 200L))));
            eventSendPacket.setCanceled(true);
        }
        if (this.minemenStrafe.getBoolean() && packet instanceof C0FPacketConfirmTransaction && Disabler.mc.thePlayer.ticksExisted % 3 == 0) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C00PacketKeepAlive && this.keepAlive.getBoolean()) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C01PacketPing && this.ping.getBoolean()) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C0BPacketEntityAction && this.entityAction.getBoolean()) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C13PacketPlayerAbilities && this.playerAbilities.getBoolean()) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C0FPacketConfirmTransaction && this.confirmTransaction.getBoolean()) {
            eventSendPacket.setCanceled(true);
        }
        if (packet instanceof C03PacketPlayer && this.spectate.getBoolean()) {
            Disabler.mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(Disabler.mc.thePlayer.getUniqueID()));
        }
    }
}

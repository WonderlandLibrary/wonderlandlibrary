// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.movement;

import java.util.function.Consumer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.modules.combat.KillAura;
import net.augustus.events.EventSendPacket;
import net.augustus.events.EventReadPacket;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventTick;
import java.util.Iterator;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.util.Map;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.minecraft.network.play.server.S14PacketEntity;
import java.util.HashMap;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class Blink extends Module
{
    public final DoubleValue autoDisable;
    public final DoubleValue pulse;
    public final BooleanValue noAura;
    private final ArrayList<Packet> packets;
    private final HashMap<S14PacketEntity, Long> spackets;
    public StringValue mode;
    private int counter;
    
    public Blink() {
        super("Blink", new Color(75, 5, 161), Categorys.MOVEMENT);
        this.autoDisable = new DoubleValue(2, "AutoDisable", this, 0.0, 0.0, 100.0, 0);
        this.pulse = new DoubleValue(2, "Pulse", this, 0.0, 0.0, 100.0, 0);
        this.noAura = new BooleanValue(137248, "NoAura", this, false);
        this.packets = new ArrayList<Packet>();
        this.spackets = new HashMap<S14PacketEntity, Long>();
        this.mode = new StringValue(1, "Mode", this, "OnlyMovement", new String[] { "OnlyMovement", "Players", "C0F", "All" });
        this.counter = 0;
    }
    
    @Override
    public void onEnable() {
        if (Blink.mc.theWorld != null && Blink.mc.thePlayer != null) {
            this.packets.clear();
            this.counter = 0;
        }
    }
    
    @Override
    public void onPreDisable() {
        for (final Map.Entry<S14PacketEntity, Long> set : this.spackets.entrySet()) {
            set.getKey().processPacket((INetHandlerPlayClient)Blink.mc.thePlayer.sendQueue);
        }
        this.spackets.clear();
        this.resetPackets();
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
    }
    
    @EventTarget
    public void onEventRecvPacket(final EventReadPacket eventReadPacket) {
        final Packet packet = eventReadPacket.getPacket();
        if (this.mode.getSelected().equals("Players")) {
            if (packet instanceof S14PacketEntity && ((S14PacketEntity)packet).getEntityId() != Blink.mc.thePlayer.getEntityId()) {
                this.spackets.put((S14PacketEntity)packet, System.currentTimeMillis());
                eventReadPacket.setCanceled(true);
            }
            this.checkS14Packets();
        }
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        if (!this.noAura.getBoolean() || KillAura.target == null) {
            if (this.autoDisable.getValue() > 0.0 && this.counter > this.autoDisable.getValue()) {
                this.resetPackets();
                this.toggle();
            }
            if (Blink.mc.thePlayer != null) {
                if (this.mode.getSelected().equals("OnlyMovement")) {
                    if (packet instanceof C03PacketPlayer) {
                        this.packets.add(packet);
                        eventSendPacket.setCanceled(true);
                    }
                }
                else if (this.mode.getSelected().equalsIgnoreCase("All")) {
                    this.packets.add(packet);
                    eventSendPacket.setCanceled(true);
                }
                else if (this.mode.getSelected().equalsIgnoreCase("C0F") && packet instanceof C0FPacketConfirmTransaction) {
                    this.packets.add(packet);
                    eventSendPacket.setCanceled(true);
                }
                if (packet instanceof C03PacketPlayer) {
                    ++this.counter;
                }
            }
            if (this.pulse.getValue() != 0.0 && this.pulse.getValue() < this.counter) {
                this.resetPackets();
            }
        }
        else {
            this.resetPackets();
        }
    }
    
    private void resetPackets() {
        this.counter = 0;
        try {
            this.packets.forEach(Blink.mc.thePlayer.sendQueue::addToSendQueueDirect);
        }
        catch (final Exception var2) {
            System.err.println("Error Blink");
        }
        this.packets.clear();
    }
    
    private void checkS14Packets() {
        try {
            S14PacketEntity toremove = null;
            for (final Map.Entry<S14PacketEntity, Long> set : this.spackets.entrySet()) {
                if (set.getValue() + 1000L < System.currentTimeMillis()) {
                    set.getKey().processPacket((INetHandlerPlayClient)Blink.mc.thePlayer.sendQueue);
                    toremove = set.getKey();
                }
            }
            if (toremove != null) {
                this.spackets.remove(toremove);
            }
        }
        catch (final Exception var2) {
            var2.printStackTrace();
        }
    }
}

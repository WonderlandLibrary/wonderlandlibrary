// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import java.util.function.Consumer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventSendPacket;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class Blink extends Module
{
    public final DoubleValue autoDisable;
    private final ArrayList<Packet> packets;
    public StringValue mode;
    private int counter;
    
    public Blink() {
        super("Blink", new Color(75, 5, 161), Categorys.MOVEMENT);
        this.autoDisable = new DoubleValue(2, "AutoDisable", this, 0.0, 0.0, 100.0, 0);
        this.packets = new ArrayList<Packet>();
        this.mode = new StringValue(1, "Mode", this, "OnlyMovement", new String[] { "OnlyMovement", "All" });
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
        this.resetPackets();
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        this.setDisplayName(this.getName() + " §8" + this.counter);
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
            else {
                this.packets.add(packet);
                eventSendPacket.setCanceled(true);
            }
            if (packet instanceof C03PacketPlayer) {
                ++this.counter;
            }
        }
    }
    
    private void resetPackets() {
        try {
            this.packets.forEach(Blink.mc.thePlayer.sendQueue::addToSendQueueDirect);
        }
        catch (Exception e) {
            System.err.println("Error Blink");
        }
        this.packets.clear();
    }
}

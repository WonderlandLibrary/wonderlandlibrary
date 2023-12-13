// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.misc;

import java.util.function.Consumer;
import java.util.Iterator;
import net.augustus.utils.PlayerUtil;
import java.util.Map;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventSendPacket;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.network.Packet;
import java.util.HashMap;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class TestModule extends Module
{
    public BooleanValue grimFlyTest;
    private final HashMap<Packet, Long> packets;
    
    public TestModule() {
        super("TestModule", Color.RED, Categorys.MISC);
        this.grimFlyTest = new BooleanValue(69420420, "GrimFly", this, false);
        this.packets = new HashMap<Packet, Long>();
    }
    
    @Override
    public void onPreDisable() {
        this.resetPackets();
    }
    
    @EventTarget
    public void onPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        if (this.grimFlyTest.getBoolean()) {
            this.packets.put(packet, System.currentTimeMillis());
            eventSendPacket.setCanceled(true);
            this.checkPackets();
        }
    }
    
    private void checkPackets() {
        try {
            Packet toremove = null;
            for (final Map.Entry<Packet, Long> set : this.packets.entrySet()) {
                if (set.getValue() + 100L < System.currentTimeMillis()) {
                    PlayerUtil.sendChat("Send packet " + set.getValue());
                    TestModule.mc.thePlayer.sendQueue.addToSendQueueDirect(set.getKey());
                    toremove = set.getKey();
                }
            }
            if (toremove != null) {
                this.packets.remove(toremove);
            }
        }
        catch (final Exception var2) {
            var2.printStackTrace();
        }
    }
    
    private void resetPackets() {
        try {
            this.packets.keySet().forEach(TestModule.mc.thePlayer.sendQueue::addToSendQueueDirect);
        }
        catch (final Exception var2) {
            System.err.println("Error Blink");
        }
        this.packets.clear();
    }
}

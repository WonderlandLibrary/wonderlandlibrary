/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.network.play.client.C01PacketChatMessage
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;

@ModuleInfo(name="AtAllProvider", description="Automatically mentions everyone on the server when using '@a' in your message.", category=ModuleCategory.FUN)
public class AtAllProvider
extends Module {
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 1000, 0, 20000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = (Integer)AtAllProvider.this.minDelayValue.get();
            if (i > newValue) {
                this.set(i);
            }
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 500, 0, 20000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = (Integer)AtAllProvider.this.maxDelayValue.get();
            if (i < newValue) {
                this.set(i);
            }
        }
    };
    private final BoolValue retryValue = new BoolValue("Retry", false);
    private final LinkedBlockingQueue<String> sendQueue = new LinkedBlockingQueue();
    private final List<String> retryQueue = new ArrayList<String>();
    private final MSTimer msTimer = new MSTimer();
    private long delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onDisable() {
        Collection<String> collection = this.sendQueue;
        synchronized (collection) {
            this.sendQueue.clear();
        }
        collection = this.retryQueue;
        synchronized (collection) {
            this.retryQueue.clear();
        }
        super.onDisable();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!this.msTimer.hasTimePassed(this.delay)) {
            return;
        }
        try {
            LinkedBlockingQueue<String> linkedBlockingQueue = this.sendQueue;
            synchronized (linkedBlockingQueue) {
                if (this.sendQueue.isEmpty()) {
                    if (!((Boolean)this.retryValue.get()).booleanValue() || this.retryQueue.isEmpty()) {
                        return;
                    }
                    this.sendQueue.addAll(this.retryQueue);
                }
                AtAllProvider.mc.field_71439_g.func_71165_d(this.sendQueue.take());
                this.msTimer.reset();
                this.delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onPacket(PacketEvent event) {
        C01PacketChatMessage packetChatMessage;
        String message;
        if (event.getPacket() instanceof C01PacketChatMessage && (message = (packetChatMessage = (C01PacketChatMessage)event.getPacket()).func_149439_c()).contains("@a")) {
            LinkedBlockingQueue<String> linkedBlockingQueue = this.sendQueue;
            synchronized (linkedBlockingQueue) {
                for (NetworkPlayerInfo playerInfo : mc.func_147114_u().func_175106_d()) {
                    String playerName = playerInfo.func_178845_a().getName();
                    if (playerName.equals(AtAllProvider.mc.field_71439_g.func_70005_c_())) continue;
                    this.sendQueue.add(message.replace("@a", playerName));
                }
                if (((Boolean)this.retryValue.get()).booleanValue()) {
                    List<String> list = this.retryQueue;
                    synchronized (list) {
                        this.retryQueue.clear();
                        this.retryQueue.addAll(Arrays.asList(this.sendQueue.toArray(new String[0])));
                    }
                }
            }
            event.cancelEvent();
        }
    }
}


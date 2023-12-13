// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.augustus.events.EventSendPacket;
import java.util.List;
import java.util.Locale;
import net.augustus.Augustus;
import net.augustus.savings.FileManager;
import net.lenni0451.eventapi.manager.EventManager;
import java.util.ArrayList;
import net.augustus.utils.interfaces.MC;

public class ClickRecordingUtil implements MC
{
    private final ArrayList<Integer> linkedList;
    private boolean isDigging;
    private long lastTime;
    
    public ClickRecordingUtil() {
        this.linkedList = new ArrayList<Integer>();
        this.isDigging = false;
        this.lastTime = System.currentTimeMillis();
    }
    
    public void startRecording() {
        this.linkedList.clear();
        this.isDigging = false;
        EventManager.register((Object)this);
        this.lastTime = System.currentTimeMillis();
    }
    
    public void stopRecording() {
        EventManager.unregister(this);
        final FileManager<Integer> fileManager = new FileManager<Integer>();
        fileManager.saveFile(Augustus.getInstance().getName().toLowerCase(Locale.ROOT) + "/clickpattern", "ClickingPattern.json", this.linkedList);
        this.linkedList.clear();
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        if (packet instanceof C07PacketPlayerDigging) {
            final C07PacketPlayerDigging digging = (C07PacketPlayerDigging)packet;
            if (digging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                this.isDigging = true;
            }
            if (digging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK || digging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                this.isDigging = false;
            }
        }
        if (packet instanceof C0APacketAnimation) {
            if (ClickRecordingUtil.mc.thePlayer.isSneaking() && !this.isDigging) {
                final int diff = (int)(System.currentTimeMillis() - this.lastTime);
                this.linkedList.add(diff);
            }
            this.lastTime = System.currentTimeMillis();
        }
    }
}

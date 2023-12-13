// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.combat;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.augustus.events.EventReadPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventSendPacket;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventRender3D;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.client.multiplayer.WorldClient;
import java.util.ArrayList;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class TimerRange extends Module
{
    private final TimeHelper timeHelper;
    private final TimeHelper timeHelper2;
    private final ArrayList<Integer> diffs;
    public long balanceCounter;
    private long lastTime;
    private WorldClient lastWorld;
    
    public TimerRange() {
        super("TimerRange", new Color(23, 233, 123), Categorys.COMBAT);
        this.timeHelper = new TimeHelper();
        this.timeHelper2 = new TimeHelper();
        this.diffs = new ArrayList<Integer>();
        this.balanceCounter = 0L;
        this.lastWorld = null;
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        if (packet instanceof C03PacketPlayer) {
            if (this.lastWorld != null && this.lastWorld != TimerRange.mc.theWorld) {
                this.balanceCounter = 0L;
                this.diffs.clear();
            }
            if (this.balanceCounter > 0L) {
                --this.balanceCounter;
            }
            final long diff = System.currentTimeMillis() - this.lastTime;
            this.diffs.add((int)diff);
            this.balanceCounter += (diff - 50L) * -3L;
            this.lastTime = System.currentTimeMillis();
            if (this.balanceCounter > 150L) {}
            this.lastWorld = TimerRange.mc.theWorld;
        }
    }
    
    @EventTarget
    public void onEventReadPacket(final EventReadPacket eventReadPacket) {
        final Packet packet = eventReadPacket.getPacket();
        if (packet instanceof S08PacketPlayerPosLook) {
            this.balanceCounter -= 100L;
        }
    }
}

package com.initial.modules.impl.visual;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.*;
import com.initial.events.impl.*;
import net.minecraft.network.play.server.*;

public class WorldTime extends Module
{
    public DoubleSet time;
    
    public WorldTime() {
        super("WorldTime", 0, Category.VISUAL);
        this.time = new DoubleSet("Time", 18.0, 0.0, 24.0, 0.5);
        this.addSettings(this.time);
    }
    
    @EventTarget
    public void onUpdate(final EventTick e) {
        this.setDisplayName("World Time §7" + Math.round(this.time.getValue()));
        this.mc.theWorld.setWorldTime((long)this.time.getValue() * 1000L);
    }
    
    @EventTarget
    public void onGet(final EventReceivePacket e) {
        if (e.getPacket() instanceof S03PacketTimeUpdate) {
            e.setCancelled(true);
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import com.elementars.eclient.event.events.EventReceivePacket;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Time extends Module
{
    long oldTime;
    private final Value<Long> time;
    
    public Time() {
        super("Time", "Clientside sets the time", 0, Category.MISC, true);
        this.time = this.register(new Value<Long>("Time", this, 0L, 0L, 24000L));
    }
    
    @EventTarget
    public void onTime(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            this.oldTime = ((SPacketTimeUpdate)event.getPacket()).getWorldTime();
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onUpdate() {
        if (Time.mc.world == null) {
            return;
        }
        Time.mc.world.setWorldTime((long)this.time.getValue());
    }
    
    @Override
    public void onEnable() {
        this.oldTime = Time.mc.world.getWorldTime();
    }
    
    @Override
    public void onDisable() {
        Time.mc.world.setWorldTime(this.oldTime);
    }
}

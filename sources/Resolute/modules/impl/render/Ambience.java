// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import vip.Resolute.events.impl.EventUpdate;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class Ambience extends Module
{
    public NumberSetting time;
    
    public Ambience() {
        super("Ambience", 0, "Changes world time", Category.RENDER);
        this.time = new NumberSetting("Time", 10.0, 1.0, 20.0, 1.0);
        this.addSettings(this.time);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventPacket && ((EventPacket)e).getPacket() instanceof S03PacketTimeUpdate) {
            ((EventPacket)e).getPacket().setWorldTime((long)(this.time.getValue() * 1000.0));
        }
        if (e instanceof EventUpdate) {
            if (Ambience.mc.thePlayer == null) {
                return;
            }
            Ambience.mc.theWorld.setWorldTime((long)this.time.getValue() * 1000L);
        }
    }
}

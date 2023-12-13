// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.augustus.events.EventReadPacket;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventRender3D;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class Ambiance extends Module
{
    public final DoubleValue time;
    public final DoubleValue timeSpeed;
    public final DoubleValue rainStrength;
    public final DoubleValue thunderStrength;
    private double counter;
    private float rainStrengthf;
    private float thunderStrengthf;
    
    public Ambiance() {
        super("Ambiance", new Color(224, 93, 40), Categorys.RENDER);
        this.time = new DoubleValue(1, "Time", this, 1900.0, 0.0, 24000.0, 0);
        this.timeSpeed = new DoubleValue(2, "TimeSpeed", this, 0.0, 0.0, 100.0, 1);
        this.rainStrength = new DoubleValue(3, "Rain", this, 0.0, 0.0, 1.0, 2);
        this.thunderStrength = new DoubleValue(4, "Thunder", this, 0.0, 0.0, 1.0, 2);
        this.rainStrengthf = 0.0f;
        this.thunderStrengthf = 0.0f;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.counter = 0.0;
        if (Ambiance.mc.theWorld != null) {
            this.rainStrengthf = Ambiance.mc.theWorld.getRainStrength(1.0f);
            this.thunderStrengthf = Ambiance.mc.theWorld.getThunderStrength(1.0f);
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.counter = 0.0;
        if (Ambiance.mc.theWorld != null) {
            Ambiance.mc.theWorld.setRainStrength(this.rainStrengthf);
            Ambiance.mc.theWorld.setThunderStrength(this.thunderStrengthf);
        }
    }
    
    @EventTarget
    public void onEventTick(final EventRender3D render3D) {
        this.counter = ((this.timeSpeed.getValue() > 0.0) ? (this.counter + this.timeSpeed.getValue()) : 0.0);
        Ambiance.mc.theWorld.setWorldTime((long)(this.time.getValue() + this.counter));
        if (this.counter > 24000.0) {
            this.counter = 0.0;
        }
        Ambiance.mc.theWorld.setRainStrength((float)this.rainStrength.getValue());
        Ambiance.mc.theWorld.setThunderStrength((float)this.thunderStrength.getValue());
    }
    
    @EventTarget
    public void onEventReadPacket(final EventReadPacket eventReadPacket) {
        final Packet packet = eventReadPacket.getPacket();
        if (packet instanceof S03PacketTimeUpdate) {
            eventReadPacket.setCanceled(true);
        }
    }
}

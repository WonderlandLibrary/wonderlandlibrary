// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventReceivePacket;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;

public class DamageFly extends Feature
{
    boolean velocity;
    int ticks;
    double motion;
    boolean damage;
    boolean isVelocity;
    
    public DamageFly() {
        super("DamageFly", "\u0424\u043b\u0430\u0439 \u043e\u0442 \u0434\u0430\u043c\u0430\u0433\u0430", Type.Movement);
    }
    
    @EventTarget
    private void onPacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof kf) {
            if (((kf)event.getPacket()).c() > 0) {
                this.isVelocity = true;
            }
            if (((kf)event.getPacket()).c() / 8000.0 > 0.2) {
                this.motion = ((kf)event.getPacket()).c() / 8000.0;
                this.velocity = true;
            }
        }
    }
    
    @EventTarget
    private void onUpdate(final EventPreMotion event) {
        if (DamageFly.mc.h.ay == 9) {
            this.damage = true;
        }
        if (this.damage && this.isVelocity) {
            if (this.velocity) {
                DamageFly.mc.h.t = this.motion;
                DamageFly.mc.h.aR = 0.415f;
                ++this.ticks;
            }
            if (this.ticks >= 27) {
                this.isVelocity = false;
                this.velocity = false;
                this.damage = false;
                this.ticks = 0;
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.damage = false;
        this.velocity = false;
        this.ticks = 0;
    }
}

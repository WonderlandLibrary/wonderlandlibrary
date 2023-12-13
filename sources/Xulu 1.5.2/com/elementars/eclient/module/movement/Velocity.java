// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import com.elementars.eclient.event.events.EntityEvent;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import com.elementars.eclient.event.events.EventReceivePacket;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Velocity extends Module
{
    private final Value<Float> horizontal;
    private final Value<Float> vertical;
    
    public Velocity() {
        super("Velocity", "Modifies knockback", 0, Category.MOVEMENT, true);
        this.horizontal = this.register(new Value<Float>("Horizontal", this, 0.0f, 0.0f, 100.0f));
        this.vertical = this.register(new Value<Float>("Vertical", this, 0.0f, 0.0f, 100.0f));
    }
    
    @EventTarget
    public void onPacket(final EventReceivePacket event) {
        if (Velocity.mc.player == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity velocity = (SPacketEntityVelocity)event.getPacket();
            if (velocity.getEntityID() == Velocity.mc.player.entityId) {
                if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                    event.setCancelled(true);
                }
                final SPacketEntityVelocity sPacketEntityVelocity = velocity;
                sPacketEntityVelocity.motionX *= (int)(Object)this.horizontal.getValue();
                final SPacketEntityVelocity sPacketEntityVelocity2 = velocity;
                sPacketEntityVelocity2.motionY *= (int)(Object)this.vertical.getValue();
                final SPacketEntityVelocity sPacketEntityVelocity3 = velocity;
                sPacketEntityVelocity3.motionZ *= (int)(Object)this.horizontal.getValue();
            }
        }
        else if (event.getPacket() instanceof SPacketExplosion) {
            if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                event.setCancelled(true);
            }
            final SPacketExplosion sPacketExplosion;
            final SPacketExplosion velocity2 = sPacketExplosion = (SPacketExplosion)event.getPacket();
            sPacketExplosion.motionX *= this.horizontal.getValue();
            final SPacketExplosion sPacketExplosion2 = velocity2;
            sPacketExplosion2.motionY *= this.vertical.getValue();
            final SPacketExplosion sPacketExplosion3 = velocity2;
            sPacketExplosion3.motionZ *= this.horizontal.getValue();
        }
    }
    
    @EventTarget
    public void onEntityCollision(final EntityEvent.EntityCollision event) {
        if (event.getEntity() == Velocity.mc.player) {
            if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                event.setCancelled(true);
                return;
            }
            event.setX(-event.getX() * this.horizontal.getValue());
            event.setY(0.0);
            event.setZ(-event.getZ() * this.horizontal.getValue());
        }
    }
}

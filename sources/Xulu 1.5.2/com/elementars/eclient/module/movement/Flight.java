// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.util.MathUtil;
import com.elementars.eclient.event.events.MotionEvent;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Flight extends Module
{
    private Double flyHeight;
    private Value<Float> speed;
    private Value<Float> vSpeed;
    private Value<Boolean> glide;
    private Value<Float> glideSpeed;
    
    public Flight() {
        super("Flight", "Get off the ground!", 0, Category.MOVEMENT, true);
        this.speed = this.register(new Value<Float>("Speed", this, 10.0f, 0.0f, 20.0f));
        this.vSpeed = this.register(new Value<Float>("V Speed", this, 3.0f, 0.0f, 20.0f));
        this.glide = this.register(new Value<Boolean>("Glide", this, true));
        this.glideSpeed = this.register(new Value<Float>("GlideSpeed", this, 0.25f, 0.0f, 5.0f));
    }
    
    @EventTarget
    public void onWalkingUpdate(final MotionEvent event) {
        final double[] dir = MathUtil.directionSpeed(this.speed.getValue());
        if (ElytraFly.mc.player.isElytraFlying()) {
            if (this.flyHeight == null) {
                this.flyHeight = ElytraFly.mc.player.posY;
            }
            if (this.glide.getValue()) {
                this.flyHeight -= (Double)this.glideSpeed.getValue();
            }
            double posX = 0.0;
            double posZ = 0.0;
            if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                posX = dir[0];
                posZ = dir[1];
            }
            if (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                this.flyHeight = ElytraFly.mc.player.posY + this.vSpeed.getValue();
            }
            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                this.flyHeight = ElytraFly.mc.player.posY - this.vSpeed.getValue();
            }
            ElytraFly.mc.player.setPosition(ElytraFly.mc.player.posX + posX, (double)this.flyHeight, ElytraFly.mc.player.posZ + posZ);
            ElytraFly.mc.player.setVelocity(0.0, 0.0, 0.0);
        }
        this.flyHeight = null;
    }
}

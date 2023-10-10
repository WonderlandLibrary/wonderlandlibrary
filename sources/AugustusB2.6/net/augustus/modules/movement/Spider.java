// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class Spider extends Module
{
    public StringValue mode;
    public final DoubleValue motionToJump;
    public final BooleanValue customJumpMotion;
    public final DoubleValue motion;
    public final BooleanValue onGroundPacket;
    
    public Spider() {
        super("Spider", new Color(168, 127, 50), Categorys.MOVEMENT);
        this.mode = new StringValue(4, "Mode", this, "Jump", new String[] { "Basic", "Jump" });
        this.motionToJump = new DoubleValue(3, "JumpMotion", this, -0.2, -0.42, 0.42, 2);
        this.customJumpMotion = new BooleanValue(5, "CustomMotion", this, false);
        this.motion = new DoubleValue(1, "Motion", this, 0.3, 0.0, 2.0, 2);
        this.onGroundPacket = new BooleanValue(2, "GroundPacket", this, false);
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        if (Spider.mc.thePlayer.isCollidedHorizontally && !Spider.mc.thePlayer.isOnLadder() && !Spider.mc.thePlayer.isInWater() && !Spider.mc.thePlayer.isInLava()) {
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "Basic": {
                    Spider.mc.thePlayer.motionY = this.motion.getValue();
                    break;
                }
                case "Jump": {
                    if (Spider.mc.thePlayer.onGround) {
                        Spider.mc.thePlayer.jump();
                        break;
                    }
                    if (Spider.mc.thePlayer.motionY >= this.motionToJump.getValue()) {
                        break;
                    }
                    if (this.customJumpMotion.getBoolean()) {
                        Spider.mc.thePlayer.motionY = this.motion.getValue();
                    }
                    else {
                        Spider.mc.thePlayer.jump();
                    }
                    if (this.onGroundPacket.getBoolean()) {
                        Spider.mc.thePlayer.sendQueue.addToSendQueueDirect(new C03PacketPlayer(true));
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Spider.mc.getTimer().timerSpeed = 1.0f;
    }
}

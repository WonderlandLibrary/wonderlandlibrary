// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public class FastLadder extends Module
{
    private final ModeSetting mode;
    private final NumberSetting speed;
    
    public FastLadder() {
        super("FastLadder", "Fast Ladder", Category.MOVEMENT, "Climbs up ladders faster than normal");
        this.mode = new ModeSetting("Mode", "Motion", new String[] { "Motion", "Timer", "Position" });
        this.speed = new NumberSetting("Speed", 1.5, 5.0, 0.1, 0.01);
        this.addSettings(this.mode, this.speed);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        this.setSuffix(this.mode.getMode());
        if (FastLadder.mc.thePlayer.isOnLadder()) {
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Timer": {
                    FastLadder.mc.timer.timerSpeed = this.speed.getValue().floatValue();
                    break;
                }
                case "Motion": {
                    FastLadder.mc.thePlayer.motionY = this.speed.getValue();
                    break;
                }
                case "Position": {
                    FastLadder.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(FastLadder.mc.thePlayer.posX, FastLadder.mc.thePlayer.posY + this.speed.getValue(), FastLadder.mc.thePlayer.posZ, FastLadder.mc.thePlayer.rotationYaw, FastLadder.mc.thePlayer.rotationPitch, false));
                    FastLadder.mc.thePlayer.setPosition(FastLadder.mc.thePlayer.posX, FastLadder.mc.thePlayer.posY + this.speed.getValue(), FastLadder.mc.thePlayer.posZ);
                    break;
                }
            }
        }
        else {
            FastLadder.mc.timer.timerSpeed = 1.0f;
        }
    }
    
    @Override
    public void onDisable() {
        FastLadder.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}

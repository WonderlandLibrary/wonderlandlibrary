// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.modules.Module;

public class VClip extends Module
{
    public VClip() {
        super("VClip", new Color(63, 255, 0, 255), Categorys.MOVEMENT);
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        if (VClip.mc.gameSettings.keyBindSneak.isPressed() && !VClip.mm.fly.isToggled()) {
            VClip.mc.thePlayer.setPosition(VClip.mc.thePlayer.posX, VClip.mc.thePlayer.posY - 3.0, VClip.mc.thePlayer.posZ);
            if ((VClip.mc.theWorld == null || VClip.mc.thePlayer == null) && VClip.mc.thePlayer.onGround && !VClip.mc.thePlayer.isOnLadder() && !VClip.mc.thePlayer.isInWater()) {
                final EntityPlayerSP thePlayer = VClip.mc.thePlayer;
                thePlayer.motionY -= 6.0;
            }
        }
    }
}

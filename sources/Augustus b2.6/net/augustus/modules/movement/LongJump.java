// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.augustus.utils.MoveUtil;
import net.augustus.events.EventTick;
import net.augustus.utils.PlayerUtil;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class LongJump extends Module
{
    public StringValue mode;
    private int counter;
    
    public LongJump() {
        super("LongJump", new Color(243, 45, 134), Categorys.MOVEMENT);
        this.mode = new StringValue(1, "Modes", this, "CubeCraft", new String[] { "CubeCraft" });
        this.counter = 0;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "CubeCraft": {
                if (LongJump.mc.thePlayer.onGround) {
                    PlayerUtil.cubedmg();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        LongJump.mc.getTimer().timerSpeed = 1.0f;
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "CubeCraft": {
                LongJump.mc.thePlayer.jumpMovementFactor = 0.028f;
                if (LongJump.mc.thePlayer.onGround) {
                    if (LongJump.mc.thePlayer.hurtTime != 0) {
                        this.counter = 0;
                        LongJump.mc.thePlayer.motionY = 0.6;
                        MoveUtil.addSpeed(0.3, false);
                        break;
                    }
                    break;
                }
                else {
                    if (this.counter == 1) {
                        MoveUtil.addSpeed(0.2, false);
                    }
                    LongJump.mc.getTimer().timerSpeed = 0.7f;
                    if (LongJump.mc.thePlayer.motionY > 0.0) {
                        final EntityPlayerSP thePlayer = LongJump.mc.thePlayer;
                        thePlayer.motionY += 0.05999999865889549;
                        break;
                    }
                    if (LongJump.mc.thePlayer.motionY > -0.4) {
                        final EntityPlayerSP thePlayer2 = LongJump.mc.thePlayer;
                        thePlayer2.motionY += 0.042;
                        break;
                    }
                    break;
                }
                break;
            }
        }
        ++this.counter;
    }
}

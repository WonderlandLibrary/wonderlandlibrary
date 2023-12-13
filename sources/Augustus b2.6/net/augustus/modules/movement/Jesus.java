// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.augustus.utils.MoveUtil;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class Jesus extends Module
{
    private final TimeHelper timeHelper;
    public StringValue mode;
    public DoubleValue speed;
    public DoubleValue motion;
    public BooleanValue onlyMove;
    
    public Jesus() {
        super("Jesus", new Color(39, 103, 146), Categorys.MOVEMENT);
        this.timeHelper = new TimeHelper();
        this.mode = new StringValue(2, "Mode", this, "Solid", new String[] { "Solid", "Speed", "Jump" });
        this.speed = new DoubleValue(1, "Speed", this, 1.0, 0.0, 20.0, 2);
        this.motion = new DoubleValue(3, "Motion", this, 0.6, 0.01, 2.0, 2);
        this.onlyMove = new BooleanValue(4, "OnlyMove", this, false);
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        this.setDisplayName(this.getName() + " §8" + this.mode.getSelected());
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Speed": {
                if (Jesus.mc.thePlayer.isInWater()) {
                    if (!Jesus.mc.gameSettings.keyBindJump.isKeyDown() && !Jesus.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        Jesus.mc.thePlayer.motionY = 0.0;
                    }
                    Jesus.mc.thePlayer.moveFlying(Jesus.mc.thePlayer.moveStrafing, Jesus.mc.thePlayer.moveForward, (float)(this.speed.getValue() / 100.0));
                    break;
                }
                break;
            }
            case "Jump": {
                if (Jesus.mc.thePlayer.isInWater()) {
                    if (this.onlyMove.getBoolean()) {
                        if (MoveUtil.isMoving()) {
                            Jesus.mc.thePlayer.motionY = this.motion.getValue();
                            if (this.timeHelper.reached(200L) && (Jesus.mc.thePlayer.isSprinting() || (Jesus.mm.sprint.allSprint && Jesus.mm.sprint.isToggled()))) {
                                final float f = Jesus.mc.thePlayer.rotationYaw * 0.017453292f;
                                final EntityPlayerSP thePlayer = Jesus.mc.thePlayer;
                                thePlayer.motionX -= MathHelper.sin(f) * 0.2f;
                                final EntityPlayerSP thePlayer2 = Jesus.mc.thePlayer;
                                thePlayer2.motionZ += MathHelper.cos(f) * 0.2f;
                                MoveUtil.strafe();
                            }
                        }
                    }
                    else {
                        Jesus.mc.thePlayer.motionY = this.motion.getValue();
                        if (this.timeHelper.reached(200L) && (Jesus.mc.thePlayer.isSprinting() || (Jesus.mm.sprint.allSprint && Jesus.mm.sprint.isToggled()))) {
                            final float f = Jesus.mc.thePlayer.rotationYaw * 0.017453292f;
                            final EntityPlayerSP thePlayer3 = Jesus.mc.thePlayer;
                            thePlayer3.motionX -= MathHelper.sin(f) * 0.2f;
                            final EntityPlayerSP thePlayer4 = Jesus.mc.thePlayer;
                            thePlayer4.motionZ += MathHelper.cos(f) * 0.2f;
                            MoveUtil.strafe();
                        }
                    }
                    this.timeHelper.reset();
                    break;
                }
                break;
            }
        }
    }
}

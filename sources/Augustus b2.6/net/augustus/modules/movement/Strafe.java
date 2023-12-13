// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.augustus.events.EventJump;
import net.augustus.events.EventMove;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.BlockUtil;
import net.augustus.events.EventPostMotion;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class Strafe extends Module
{
    public final StringValue mode;
    public final BooleanValue strafeWhileKB;
    public final BooleanValue onlyOnGround;
    
    public Strafe() {
        super("Strafe", new Color(50, 168, 82), Categorys.MOVEMENT);
        this.mode = new StringValue(0, "Mode", this, "Normal", new String[] { "Normal", "Matrix", "Test" });
        this.strafeWhileKB = new BooleanValue(1, "WhileKB", this, false);
        this.onlyOnGround = new BooleanValue(2, "OnlyOnGround", this, false);
    }
    
    @EventTarget
    public void onEventPostMotion(final EventPostMotion eventPostMotion) {
        if (!this.strafeWhileKB.getBoolean() && Strafe.mc.thePlayer.hurtTime != 0) {
            return;
        }
        if (this.onlyOnGround.getBoolean() && !Strafe.mc.thePlayer.onGround) {
            return;
        }
        if (!BlockUtil.isScaffoldToggled()) {
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "Normal": {
                    MoveUtil.strafe();
                    break;
                }
                case "Matrix": {
                    MoveUtil.strafeMatrix();
                    break;
                }
            }
        }
    }
    
    @EventTarget
    public void onEventMove(final EventMove eventMove) {
        if (!this.mode.getSelected().equals("Test")) {
            return;
        }
        if (!this.strafeWhileKB.getBoolean() && Strafe.mc.thePlayer.hurtTime != 0) {
            return;
        }
        if (this.onlyOnGround.getBoolean() && !Strafe.mc.thePlayer.onGround) {
            return;
        }
        eventMove.setYaw(MoveUtil.getYaw(true));
    }
    
    @EventTarget
    public void onEventJump(final EventJump eventJump) {
        if (!this.mode.getSelected().equals("Test")) {
            return;
        }
        if (!this.strafeWhileKB.getBoolean() && Strafe.mc.thePlayer.hurtTime != 0) {
            return;
        }
        if (this.onlyOnGround.getBoolean() && !Strafe.mc.thePlayer.onGround) {
            return;
        }
        eventJump.setYaw(MoveUtil.getYaw(true));
    }
}

// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.movement;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.utils.MoveUtil;
import net.augustus.events.EventUpdate;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class LongJump extends Module
{
    public StringValue mode;
    public DoubleValue speed;
    private boolean flag;
    
    public LongJump() {
        super("LongJump", Color.ORANGE, Categorys.MOVEMENT);
        this.mode = new StringValue(6932235, "Mode", this, "Vanilla", new String[] { "Vanilla" });
        this.speed = new DoubleValue(44, "Speed", this, 3.4, 0.0, 9.0, 2);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.flag = false;
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Vanilla": {
                LongJump.mc.thePlayer.jump();
                break;
            }
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (LongJump.mc.thePlayer.onGround) {
            if (this.flag) {
                this.toggle();
            }
            this.flag = true;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Vanilla": {
                if (LongJump.mc.thePlayer.onGround) {
                    MoveUtil.strafe(this.speed.getValue());
                    break;
                }
                MoveUtil.strafe();
                break;
            }
        }
    }
}

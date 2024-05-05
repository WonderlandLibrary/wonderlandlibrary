package com.initial.modules.impl.movement;

import com.initial.modules.*;
import com.initial.events.impl.*;
import com.initial.events.*;

public class Sprint extends Module
{
    public Sprint() {
        super("Sprint", 0, Category.MOVEMENT);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (this.mc.thePlayer.moveForward > 0.0f && !this.mc.thePlayer.isUsingItem() && !this.mc.thePlayer.isSneaking() && !this.mc.thePlayer.isCollidedHorizontally) {
            this.mc.thePlayer.setSprinting(true);
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.thePlayer.setSprinting(false);
    }
}

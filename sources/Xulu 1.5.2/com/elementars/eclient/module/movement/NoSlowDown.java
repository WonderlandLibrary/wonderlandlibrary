// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class NoSlowDown extends Module
{
    public NoSlowDown() {
        super("NoSlowDown", "Prevents slowdown when using items", 0, Category.MOVEMENT, true);
    }
    
    @SubscribeEvent
    public void onInput(final InputUpdateEvent event) {
        if (NoSlowDown.mc.player.isHandActive() && !NoSlowDown.mc.player.isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
        }
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
}

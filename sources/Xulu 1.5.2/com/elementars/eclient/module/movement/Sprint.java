// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.util.MovementUtils;
import com.elementars.eclient.event.Event;
import com.elementars.eclient.event.events.PlayerMoveEvent;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Sprint extends Module
{
    private final Value<String> mode;
    
    public Sprint() {
        super("Sprint", "Sprints", 0, Category.MOVEMENT, true);
        this.mode = this.register(new Value<String>("Mode", this, "Legit", new String[] { "Legit", "Rage" }));
    }
    
    @Override
    public void onUpdate() {
        try {
            if (this.mode.getValue().equalsIgnoreCase("Legit") && Sprint.mc.gameSettings.keyBindForward.isKeyDown() && !Sprint.mc.player.isSneaking() && !Sprint.mc.player.isHandActive() && !Sprint.mc.player.collidedHorizontally && Sprint.mc.currentScreen == null && Sprint.mc.player.getFoodStats().getFoodLevel() > 6.0f) {
                Sprint.mc.player.setSprinting(true);
            }
        }
        catch (Exception ex) {}
    }
    
    @EventTarget
    public void onMove(final PlayerMoveEvent event) {
        if (event.getEventState() != Event.State.PRE) {
            return;
        }
        if ((Sprint.mc.gameSettings.keyBindForward.isKeyDown() || Sprint.mc.gameSettings.keyBindBack.isKeyDown() || Sprint.mc.gameSettings.keyBindLeft.isKeyDown() || Sprint.mc.gameSettings.keyBindRight.isKeyDown()) && !Sprint.mc.player.isSneaking() && !Sprint.mc.player.collidedHorizontally && Sprint.mc.player.getFoodStats().getFoodLevel() > 6.0f) {
            Sprint.mc.player.setSprinting(true);
            final double[] dir = MovementUtils.forward2(0.01745329238474369);
            event.setX(dir[0] * 0.20000000298023224);
            event.setZ(dir[1] * 0.20000000298023224);
        }
    }
}

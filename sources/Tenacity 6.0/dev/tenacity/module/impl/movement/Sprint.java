// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import dev.tenacity.module.impl.player.Scaffold;
import dev.tenacity.Tenacity;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class Sprint extends Module
{
    private final BooleanSetting omniSprint;
    
    public Sprint() {
        super("Sprint", "Sprint", Category.MOVEMENT, "Sprints automatically");
        this.omniSprint = new BooleanSetting("Omni Sprint", false);
        this.addSettings(this.omniSprint);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (Tenacity.INSTANCE.getModuleCollection().get(Scaffold.class).isEnabled() && (Scaffold.sprintMode.is("Off") || Scaffold.isDownwards())) {
            Sprint.mc.gameSettings.keyBindSprint.pressed = false;
            Sprint.mc.thePlayer.setSprinting(false);
            return;
        }
        if (this.omniSprint.isEnabled()) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
        else if (Sprint.mc.thePlayer.isUsingItem()) {
            if (Sprint.mc.thePlayer.moveForward > 0.0f && (Tenacity.INSTANCE.isEnabled(NoSlow.class) || !Sprint.mc.thePlayer.isUsingItem()) && !Sprint.mc.thePlayer.isSneaking() && !Sprint.mc.thePlayer.isCollidedHorizontally && Sprint.mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                Sprint.mc.thePlayer.setSprinting(true);
            }
        }
        else {
            Sprint.mc.gameSettings.keyBindSprint.pressed = true;
        }
    }
    
    @Override
    public void onDisable() {
        Sprint.mc.thePlayer.setSprinting(false);
        Sprint.mc.gameSettings.keyBindSprint.pressed = false;
        super.onDisable();
    }
}

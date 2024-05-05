// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.movement;

import Lavish.Client;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class Sprint extends Module
{
    public Sprint() {
        super("Sprint", 0, true, Category.Movement, "Makes you sprint");
    }
    
    @Override
    public void onUpdate() {
        if (Client.instance.moduleManager.getModuleByName("Scaffold").isEnabled()) {
            return;
        }
        if (Sprint.mc.thePlayer.moveForward > 0.0f && !Sprint.mc.thePlayer.isUsingItem() && !Sprint.mc.thePlayer.isSneaking() && !Sprint.mc.thePlayer.isCollidedHorizontally && Sprint.mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
    }
    
    @Override
    public void onDisable() {
        Sprint.mc.thePlayer.setSneaking(false);
    }
}

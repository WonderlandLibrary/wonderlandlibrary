package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;

import static net.ccbluex.liquidbounce.utils.MovementUtils.isInLiquid;

@ModuleInfo(name = "Jesus", description = ":/", category = ModuleCategory.MOVEMENT)
public class Jesus extends Module {

    private boolean wasWater;
    private int ticks = 0;

    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mc.thePlayer.onGround || mc.thePlayer.isOnLadder()) {
            this.wasWater = false;
        }
        if (mc.thePlayer.motionY > 0.0D && this.wasWater) {
            if (mc.thePlayer.motionY <= 0.1027D) {
                mc.thePlayer.motionY *= 1.267D;
            }
            mc.thePlayer.motionY += 0.05172D;
        }
        if (isInLiquid() && !mc.thePlayer.isSneaking()) {
            if (this.ticks < 3) {
                mc.thePlayer.motionY = 0.09D;
                ++this.ticks;
                this.wasWater = false;
            } else {
                mc.thePlayer.motionY = 0.5D;
                this.ticks = 0;
                this.wasWater = true;
            }
        }
    }
}

package me.aquavit.liquidsense.module.modules.player;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import net.minecraft.item.ItemFishingRod;

@ModuleInfo(name = "AutoFish", description = "Automatically catches fish when using a rod.", category = ModuleCategory.PLAYER)
public class AutoFish extends Module {
    private MSTimer rodOutTimer = new MSTimer();

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod))
            return;

        if (rodOutTimer.hasTimePassed(500L) && mc.thePlayer.fishEntity == null || (mc.thePlayer.fishEntity != null && mc.thePlayer.fishEntity.motionX == 0.0 && mc.thePlayer.fishEntity.motionZ == 0.0 && mc.thePlayer.fishEntity.motionY != 0.0)) {
            mc.rightClickMouse();
            rodOutTimer.reset();
        }
    }
}

package me.aquavit.liquidsense.module.modules.movement;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.MoveEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;

@ModuleInfo(name = "SafeWalk", description = "Prevents you from falling down as if you were sneaking.", category = ModuleCategory.MOVEMENT)
public class SafeWalk extends Module {
    private BoolValue airSafeValue = new BoolValue("AirSafe", false);

    @EventTarget
    public void onMove(MoveEvent event){
        if (airSafeValue.get() || mc.thePlayer.onGround)
            event.setSafeWalk(true);
    }
}

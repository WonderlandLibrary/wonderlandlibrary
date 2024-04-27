package me.aquavit.liquidsense.module.modules.world;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;

@ModuleInfo(name = "GameSpeed", description = "Changes the speed of the entire game.", category = ModuleCategory.WORLD)
public class GameSpeed extends Module {

    private final IntegerValue tickValue = new IntegerValue("TicksExisted", 1, 1, 5);
    private final FloatValue speedValue = new FloatValue("Speed", 2F, 0.1F, 10F);
    private final BoolValue onMoveValue = new BoolValue("OnMove", true);

    @Override
    public void onEnable() {
        if (mc.thePlayer == null)
            return;

        mc.timer.timerSpeed = speedValue.get();
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null)
            return;

        mc.timer.timerSpeed = 1F;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (MovementUtils.isMoving() || !onMoveValue.get()) {
            if (mc.thePlayer.ticksExisted % tickValue.get() == 0) {
                mc.timer.timerSpeed = speedValue.get();
            }
        }
    }

    @Override
    public String getTag(){
        return String.valueOf(mc.timer.timerSpeed);
    }
}

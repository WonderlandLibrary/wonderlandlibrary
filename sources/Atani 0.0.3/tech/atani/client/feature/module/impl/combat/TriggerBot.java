package tech.atani.client.feature.module.impl.combat;

import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.feature.value.impl.SliderValue;

@ModuleData(name = "TriggerBot", description = "Automatically clicks at entities", category = Category.COMBAT)
public class TriggerBot extends Module {

    private final SliderValue<Integer> cps = new SliderValue<>("CPS", "How many cps will the client click?", this, 12, 1, 24, 1);

    private final TimeHelper timer = new TimeHelper();

    @Listen
    public final void onUpdateMotion(UpdateMotionEvent updateMotionEvent) {

        int randomizedCps = (int) ((cps.getValue() + Math.round(Math.random() / 6)) - Math.round(Math.random() / 8));
        boolean doubleClick;

        doubleClick = Math.random() * 100 < 33;

        if(mc.pointedEntity != null) {
            if(timer.hasReached(1000 / randomizedCps, true)) {
                mc.clickMouse();
                if(doubleClick) mc.clickMouse();
            }
        }

    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
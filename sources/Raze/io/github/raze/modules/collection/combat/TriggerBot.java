package io.github.raze.modules.collection.combat;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.math.TimeUtil;

public class TriggerBot extends AbstractModule {

    private final TimeUtil timer;
    private final NumberSetting cps;

    public TriggerBot() {
        super("TriggerBot", "Automatically clicks when holding down the attack button.", ModuleCategory.COMBAT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                cps = new NumberSetting(this, "CPS", 1,20 ,13)
        );
        timer = new TimeUtil();

    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        boolean doubleClick;
        int randomizedCps = (int) ((cps.get().intValue() + Math.round(Math.random() / 6)) - Math.round(Math.random() / 8));

        doubleClick = Math.random() * 100 < 33;

        if(mc.pointedEntity != null) {
            if(timer.elapsed(1000 / randomizedCps, true)) {
                mc.clickMouse();
                if(doubleClick) {
                    mc.clickMouse();
                }
            }
        }
    }
}

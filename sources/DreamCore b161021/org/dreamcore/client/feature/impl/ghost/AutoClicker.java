package org.dreamcore.client.feature.impl.ghost;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.settings.impl.NumberSetting;

public class AutoClicker extends Feature {

    public NumberSetting minCps = new NumberSetting("Min", 6, 1, 20, 1, () -> true, NumberSetting.NumberType.APS);
    public NumberSetting maxCps = new NumberSetting("Max", 10, 1, 20, 1, () -> true, NumberSetting.NumberType.APS);

    public TimerHelper timerHelper = new TimerHelper();

    public AutoClicker() {
        super("AutoClicker", "Кликает определенный cps", Type.Ghost);
        addSettings(minCps, maxCps);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        int cps;
        if (mc.gameSettings.keyBindAttack.isKeyDown() && !mc.player.isUsingItem()) {
            cps = (int) MathematicHelper.randomizeFloat(maxCps.getNumberValue(), minCps.getNumberValue());
            if (timerHelper.hasReached(1000 / cps)) {
                mc.clickMouse();
                timerHelper.reset();
            }
        }
    }

}

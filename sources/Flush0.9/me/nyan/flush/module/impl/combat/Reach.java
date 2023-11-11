package me.nyan.flush.module.impl.combat;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventReach;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.other.MathUtils;

public class Reach extends Module {
    private final NumberSetting maxReach = new NumberSetting("Max", this, 3.8, 3, 6, 0.05),
            minReach = new NumberSetting("Min", this, 3.5, 3, 6, 0.05);
    private final BooleanSetting onlySprinting = new BooleanSetting("Only Sprinting", this, true);

    public Reach() {
        super("Reach", Category.COMBAT);
    }

    @SubscribeEvent
    public void onReach(EventReach e) {
        if (onlySprinting.getValue() && !mc.thePlayer.isSprinting()) {
            return;
        }

        e.setReach(MathUtils.getRandomNumber(maxReach.getValue(), minReach.getValue()));
    }

    @Override
    public String getSuffix() {
        return minReach.toString() + " - " + maxReach.toString();
    }
}

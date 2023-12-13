/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.ghost;

import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;

/**
 * @author Hazsi
 * @since 03/29/2022
 */
@ModuleInfo(name = "Reach", description = "Allows you to reach further", category = Category.LEGIT)
public final class Reach extends Module {

    public static Reach instance;

    private final NumberSetting reachMin = new NumberSetting("Reach Min", this, 3.2, 3.0, 6.0, 0.05);
    private final NumberSetting reachMax = new NumberSetting("Reach Max", this, 3.3, 3.0, 6.0, 0.05);
    private final NumberSetting reachChance = new NumberSetting("Reach Chance", this, 90, 0, 100, 1);
    private final BooleanSetting waterCheck = new BooleanSetting("Water Check", this, true);

    // EntityRenderer.java
    public double getReach() {
        if (mc.playerController.extendedReach()) return 6.0;
        else if (!this.isEnabled()) return 3.0;

        if (shouldReach()) {
            double reachDelta = Math.abs(reachMax.getValue() - reachMin.getValue());
            double reachAdd = reachDelta * Math.random();

            return reachMin.getValue() + reachAdd;
        }

        return 3.0;
    }

    private boolean shouldReach() {
        return (Math.random() < (reachChance.getValue() / 100D)) && (!waterCheck.isEnabled() || !mc.thePlayer.isInLiquid());
    }

    public Reach() {
        instance = this;
    }
}

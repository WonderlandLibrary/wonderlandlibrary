package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.MathUtil;

public class VanillaSpeed
        extends Module {
    public Setting<Double> speed = this.register(new Setting<Double>("Speed", 1.0, 1.0, 10.0));

    public VanillaSpeed() {
        super("VanillaSpeed", "ec.me", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (VanillaSpeed.mc.player == null || VanillaSpeed.mc.world == null) {
            return;
        }
        double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
        VanillaSpeed.mc.player.motionX = calc[0];
        VanillaSpeed.mc.player.motionZ = calc[1];
    }
}


/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Animations", description = "Changes animations", category = Category.RENDER)
public final class Animations extends Module {

    private final ModeSetting mode = new ModeSetting("Animation", this, "1.7",
            "None", "1.7", "Skidding", "Exhibition", "Spin", "Forward", "Shove", "Chill", "Butter",
            "Smooth", "Slide", "Short", "Push", "Small", "Dortware", "Stab", "Dortware 2", "Rise", "Flush", "Whack", "Big Whack",
            "Wobble", "Chungus", "Bitch Slap", "Swong", "Spinny", "Reverse", "Down", "Rhys", "Throw", "Inwards", "Helicopter",
            "Cockless", "Swang", "LB", "Exhibition 2", "Old", "Leaked");

    private final NumberSetting slow = new NumberSetting("Slow", this, 6, 2, 66, 0.1);
    private final NumberSetting spinSpeed = new NumberSetting("Spin Speed", this, 0, 0, 20, 1);

    private final NumberSetting x = new NumberSetting("X", this, 0, -2, 2, 0.1);
    private final NumberSetting y = new NumberSetting("Y", this, 0, -2, 2, 0.1);
    private final NumberSetting z = new NumberSetting("Z", this, 0, -2, 2, 0.1);
    private final NumberSetting scale = new NumberSetting("Scale", this, 1, 0.1, 2, 0.1);

    private final BooleanSetting oldDamage = new BooleanSetting("Old Damage", this, true);
    private final BooleanSetting smoothSwing = new BooleanSetting("Smooth Swing", this, false);
    private final BooleanSetting resetPosition = new BooleanSetting("Reset Position", this, false);

    public static double xO, yO, zO, scale0 = 1;

    private final BooleanSetting alwaysShow = new BooleanSetting("Always Show", this, false);

    public static boolean enabled;

    @Override
    protected void onDisable() {
        xO = yO = zO = 0;
        scale0 = 1;
        enabled = false;
    }

    @Override
    public void onUpdateAlwaysInGui() {
        spinSpeed.hidden = !mode.is("Helicopter");

        if (resetPosition.isEnabled()) {
            xO = yO = zO = 0;
            scale0 = 1;

            x.setValue(0);
            y.setValue(0);
            z.setValue(0);

            slow.setValue(6);

            scale.setValue(1);

            resetPosition.toggle();
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        xO = x.getValue() / 3;
        yO = y.getValue() / 3;
        zO = z.getValue() / 3;

        scale0 = scale.getValue();
    }

    @Override
    protected void onEnable() {
        enabled = true;
    }
}

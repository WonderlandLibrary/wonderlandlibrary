package dev.tenacity.module.impl.render;

import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.NumberSetting;

public final class Animations extends Module {

    public static final ModeSetting mode = new ModeSetting("Mode", "Stella",
            "Stella", "Middle", "1.7", "Exhi", "Exhi 2", "Exhi 3", "Exhi 4", "Exhi 5", "Shred", "Smooth", "Sigma", "Custom");
    public static final NumberSetting slowdown = new NumberSetting("Swing Slowdown", 1, 15, 1, 1);
    public static final BooleanSetting oldDamage = new BooleanSetting("Old Damage", false);
    public static final BooleanSetting smallSwing = new BooleanSetting("Small Swing", false);
    public static final NumberSetting x = new NumberSetting("X", 0, 50, -50, 1);
    public static final NumberSetting y = new NumberSetting("Y", 0, 50, -50, 1);
    public static final NumberSetting size = new NumberSetting("Size", 0, 50, -50, 1);

    public Animations() {
        super("Animations", Category.RENDER, "changes animations");
        this.addSettings(x, y, size, smallSwing, mode, slowdown, oldDamage);
    }

    @Override
    public void onMotionEvent(MotionEvent event) {
        this.setSuffix(mode.getMode());
    }

}

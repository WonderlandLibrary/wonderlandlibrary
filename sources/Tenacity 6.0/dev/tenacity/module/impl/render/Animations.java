// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class Animations extends Module
{
    public static final ModeSetting mode;
    public static final NumberSetting slowdown;
    public static final BooleanSetting oldDamage;
    public static final BooleanSetting smallSwing;
    public static final NumberSetting x;
    public static final NumberSetting y;
    public static final NumberSetting size;
    
    public Animations() {
        super("Animations", "Animations", Category.RENDER, "changes animations");
        this.addSettings(Animations.x, Animations.y, Animations.size, Animations.smallSwing, Animations.mode, Animations.slowdown, Animations.oldDamage);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        this.setSuffix(Animations.mode.getMode());
    }
    
    static {
        mode = new ModeSetting("Mode", "Stella", new String[] { "Stella", "Middle", "1.7", "Exhi", "Exhi 2", "Exhi 3", "Exhi 4", "Exhi 5", "Shred", "Smooth", "Sigma", "Custom" });
        slowdown = new NumberSetting("Swing Slowdown", 1.0, 15.0, 1.0, 1.0);
        oldDamage = new BooleanSetting("Old Damage", false);
        smallSwing = new BooleanSetting("Small Swing", false);
        x = new NumberSetting("X", 0.0, 50.0, -50.0, 1.0);
        y = new NumberSetting("Y", 0.0, 50.0, -50.0, 1.0);
        size = new NumberSetting("Size", 0.0, 50.0, -50.0, 1.0);
    }
}

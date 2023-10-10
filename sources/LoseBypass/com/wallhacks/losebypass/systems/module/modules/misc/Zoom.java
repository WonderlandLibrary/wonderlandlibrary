/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.misc;

import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import com.wallhacks.losebypass.utils.Animation;

@Module.Registration(name="Zoom", hold=true, description="Zooms in your camera", allowHold=false)
public class Zoom
extends Module {
    Animation animation = new Animation(0.0f, 0.0075f);
    IntSetting fov = this.intSetting("Fov", 20, 5, 40);
    DoubleSetting sensitivity = this.doubleSetting("Sensitivity", 0.4, 0.1, 1.0).description("Lower sensitivity for when zooming");
    static Zoom instance;
    long lastFrame;

    public Zoom() {
        instance = this;
    }

    public static float getSensitivity() {
        return instance.calcSensitivity();
    }

    private float calcSensitivity() {
        float difference = 1.0f - this.sensitivity.getFloatValue();
        return 1.0f - difference * (1.0f - this.animation.value());
    }

    private float calcFov() {
        float difference = Zoom.mc.gameSettings.fovSetting - (float)((Integer)this.fov.getValue()).intValue();
        this.animation.update(this.isEnabled() ? 0.0f : 1.0f, System.currentTimeMillis() - this.lastFrame);
        this.lastFrame = System.currentTimeMillis();
        return (float)((Integer)this.fov.getValue()).intValue() + difference * this.animation.value();
    }

    public static float getFov() {
        return instance.calcFov();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.combat;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.SettingChangeEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import com.wallhacks.losebypass.systems.setting.settings.ModeSetting;
import java.util.Arrays;
import java.util.Random;

@Module.Registration(name="Reach", description="Lets you reach enemies from further", alwaysListening=true, category=Module.Category.COMBAT)
public class Reach
extends Module {
    private final ModeSetting mode = this.modeSetting("Mode", "Gaussian", Arrays.asList("Gaussian", "Random")).description("Gaussian: will make it more likely for your reach to be lower, Random: Will make it as likely to get high reach as to get low reach, if you have no idea what this means you should probably use Gaussian");
    private final BooleanSetting sprintOnly = this.booleanSetting("SprintOnly", true).description("Makes reach only work if your are sprinting, helps to bypass the anticheat and look more legit");
    private final IntSetting chance = this.intSetting("Chance", 60, 1, 100).description("The chance for reach to actually work every tick");
    private final DoubleSetting minReach = this.doubleSetting("MinReach", 3.0, 3.0, 4.5).description("The minimum amount of reach to deal");
    private final DoubleSetting maxReach = this.doubleSetting("MaxReach", 3.25, 3.0, 4.5).description("The maximum amount of reach to take");
    private static Reach instance;

    public Reach() {
        instance = this;
    }

    @SubscribeEvent
    public void onSettingChange(SettingChangeEvent event) {
        if (event.getSetting() == this.minReach) {
            if (!((Double)this.minReach.getValue() > (Double)this.maxReach.getValue())) return;
            this.minReach.setValueSilent(this.maxReach.getValue());
            return;
        }
        if (event.getSetting() != this.maxReach) return;
        if (!((Double)this.minReach.getValue() > (Double)this.maxReach.getValue())) return;
        this.maxReach.setValueSilent(this.minReach.getValue());
    }

    public static float getReach() {
        Random random = new Random();
        if (random.nextInt(100) >= (Integer)Reach.instance.chance.getValue()) return 3.0f;
        if (!instance.isEnabled()) return 3.0f;
        if (Reach.mc.thePlayer == null) return 3.0f;
        if (!Reach.mc.thePlayer.isSprinting()) {
            if ((Boolean)Reach.instance.sprintOnly.getValue() != false) return 3.0f;
        }
        float add = Reach.instance.maxReach.getFloatValue() - Reach.instance.minReach.getFloatValue();
        if (Reach.instance.mode.is("Random")) {
            add = (float)((double)add * random.nextDouble());
            return add + Reach.instance.minReach.getFloatValue();
        }
        add = (float)((double)add * Math.abs(random.nextGaussian()));
        return add + Reach.instance.minReach.getFloatValue();
    }
}


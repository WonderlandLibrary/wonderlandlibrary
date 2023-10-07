package sudo.module.world;


import com.google.common.eventbus.Subscribe;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import sudo.events.EventBlockBreakingCooldown;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;

public class FastBreak extends Mod{

    BooleanSetting creative = new BooleanSetting("creative", true);
    NumberSetting delay = new NumberSetting("Delay", 0, 1, 0.2, 0.1);
    ModeSetting mode = new ModeSetting("Mode", "Haste", "Haste");

    public FastBreak() {
        super("FastBreak", "Breaks blocks faster", Category.WORLD, 0);
        addSettings(creative,delay,mode);
    }

    private StatusEffectInstance haste;

    @Override
    public void onEnable() {
        haste = new StatusEffectInstance(StatusEffects.HASTE, Integer.MAX_VALUE, (int) 3 - 1);
    }

    @Override
    public void onDisable() {
        if (mode.is("Haste")) {
            mc.player.removeStatusEffect(StatusEffects.HASTE);
        }
    }

    @Override
    public void onTick() {
        if (mode.is("Haste")) mc.player.addStatusEffect(haste);
        else mc.player.removeStatusEffect(StatusEffects.HASTE);
    }

    @Subscribe
    public void onBlockBreakingCooldown(EventBlockBreakingCooldown event) {
        if (mc.player.isCreative() && !creative.isEnabled()) return;
        if (delay.getValue() != 1) event.setCooldown((int) (delay.getValue() * 5));
    }
}
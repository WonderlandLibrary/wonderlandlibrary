package info.sigmaclient.sigma.modules.movement.speeds.impl;

import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Speed;
import info.sigmaclient.sigma.modules.movement.speeds.SpeedModule;
import info.sigmaclient.sigma.premium.PremiumManager;
import info.sigmaclient.sigma.utils.Variable;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.function.Predicate;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class GrimSpeed extends SpeedModule {
    public static Runnable premium = null;
    public GrimSpeed(Speed speed) {
        super("Grim", "Grim ac speed", speed);
    }

    @Override
    public void onDisable() {
//        mc.gameSettings.keyBindJump.pressed = false;
        super.onDisable();
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
//        mc.gameSettings.keyBindJump.pressed = true;
        if(premium != null && PremiumManager.isPremium)
            premium.run();
        super.onUpdateEvent(event);
    }
}

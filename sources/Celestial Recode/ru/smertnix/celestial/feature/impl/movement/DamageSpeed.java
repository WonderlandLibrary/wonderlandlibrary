package ru.smertnix.celestial.feature.impl.movement;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.utils.math.TimerHelper;
import ru.smertnix.celestial.utils.movement.MovementUtils;

public class DamageSpeed extends Feature {
    public static float ticks = 0;
    public TimerHelper timerHelper = new TimerHelper();
    public static BooleanSetting damageOnly = new BooleanSetting("Damage only", "", true, () -> true);
    public static BooleanSetting autoDisable = new BooleanSetting("Auto disable", "", true, () -> true);
    public DamageSpeed() {
        super("DamageSpeed", "Автоматически бустит после получения урона", FeatureCategory.Movement);
        addSettings(damageOnly, autoDisable);

    }
    
    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        ticks = 0;
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
    	if (ticks > 10) {
    		if (autoDisable.getBoolValue())
    		this.toggle();
    	}
    	if (damageOnly.getBoolValue() && mc.player.hurtTime == 0) {
    		return;
    	}
    	if (MovementUtils.isMoving()) {
    		if (mc.player.ticksExisted % 2 == 0) {
    			ticks++;
    		}
            if (mc.player.onGround) {
                mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 9.5D / 24.5D, 0, Math.cos(MovementUtils.getAllDirection()) * 9.5D / 24.5D);
                MovementUtils.strafe();
            } else if (mc.player.isInWater()) {
                mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 9.5D / 24.5D, 0, Math.cos(MovementUtils.getAllDirection()) * 9.5D / 24.5D);
                MovementUtils.strafe();
            } else if (!mc.player.onGround) {
                mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.11D / 24.5D, 0, Math.cos(MovementUtils.getAllDirection()) * 0.11D / 24.5D);
                MovementUtils.strafe();
            } else {
                mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.005D * MovementUtils.getSpeed(), 0, Math.cos(MovementUtils.getAllDirection()) * 0.005 * MovementUtils.getSpeed());
                MovementUtils.strafe();
            }
        }
    }

}

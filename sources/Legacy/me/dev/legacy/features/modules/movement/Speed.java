package me.dev.legacy.features.modules.movement;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.EntityUtil;
import me.dev.legacy.util.PlayerUtil;
import me.dev.legacy.util.Timer;

public class Speed extends Module {
    public Speed() {
        super("Speed", "YPort Speed.", Category.MOVEMENT, false, false, false);
    }

    Setting<Double> yPortSpeed = this.register(new Setting<Double>("YPort Speed", 0.6, 0.5, 1.5));

    private double playerSpeed;
    private final Timer timer = new Timer();

    @Override
    public void onEnable() {
        playerSpeed = PlayerUtil.getBaseMoveSpeed();
    }

    @Override
    public void onDisable() {
        EntityUtil.resetTimer();
        this.timer.reset();
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) {
            this.disable();
            return;
        }

        if (!PlayerUtil.isMoving(mc.player) || mc.player.isInWater() && mc.player.isInLava() || mc.player.collidedHorizontally) {
            return;
        }
        if (mc.player.onGround) {
            EntityUtil.setTimer(1.15f);
            mc.player.jump();
            PlayerUtil.setSpeed(mc.player, PlayerUtil.getBaseMoveSpeed() + yPortSpeed.getValue() / 10);
        } else {
            mc.player.motionY = -1;
            EntityUtil.resetTimer();
        }
    }
}
package me.nyan.flush.module.impl.world;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.movement.Fly;
import me.nyan.flush.module.impl.movement.Speed;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.movement.MovementUtils;

public class Timer extends Module {
    private final BooleanSetting onMovement = new BooleanSetting("On Movement", this, false);
    private final NumberSetting speed = new NumberSetting("Speed", this, 1, 0.1, 10, 0.05);

    public Timer() {
        super("Timer", Category.WORLD);
    }

    public void onDisable() {
        super.onDisable();
        if (!isEnabled(Fly.class) && !isEnabled(Speed.class))
            mc.timer.timerSpeed = 1;

        //mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (!onMovement.getValue() || MovementUtils.isMoving())
            mc.timer.timerSpeed = speed.getValueFloat();
        else
            mc.timer.timerSpeed = 1;
/*
        if (mc.thePlayer.onGround)
            mc.thePlayer.jump();

        mc.gameSettings.keyBindSneak.pressed = true;

 */
    }

    @Override
    public String getSuffix() {
        return speed.toString();
    }
}

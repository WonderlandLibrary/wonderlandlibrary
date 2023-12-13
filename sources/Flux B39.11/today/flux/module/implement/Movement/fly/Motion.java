package today.flux.module.implement.Movement.fly;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.event.UpdateEvent;
import today.flux.module.ModuleManager;
import today.flux.module.SubModule;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.implement.Combat.TargetStrafe;
import today.flux.utility.PlayerUtils;
import today.flux.module.value.FloatValue;

public class Motion extends SubModule {
    public static FloatValue speed = new FloatValue("Fly", "Motion Speed", 0.5f, 0.1f, 3.0f, 0.1f);

    public Motion() {
        super("Motion", "Fly");
    }

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        mc.thePlayer.motionX = 0;
            mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.motionY = speed.getValueState();
        }

        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.thePlayer.motionY = -speed.getValueState();
        }

        if (PlayerUtils.isMoving()) {
            if (KillAura.target != null) {
                mc.timer.timerSpeed = 1;
                TargetStrafe.move(null, speed.getValueState() * 3, KillAura.target);
            } else {
                PlayerUtils.setSpeed(speed.getValueState() * 3);
            }
            PlayerUtils.setSpeed(speed.getValueState() * 3);
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
        super.onDisable();
    }
}
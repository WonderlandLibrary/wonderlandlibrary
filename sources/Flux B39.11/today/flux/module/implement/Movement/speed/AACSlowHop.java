package today.flux.module.implement.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.module.implement.Movement.Speed;
import today.flux.event.MoveEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.module.SubModule;
import today.flux.utility.BlockUtils;
import today.flux.utility.PlayerUtils;

public class AACSlowHop extends SubModule {
    public AACSlowHop() {
        super("AAC4", "Speed");
    }

    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        mc.thePlayer.speedInAir = 0.02f;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(PostUpdateEvent event) {
        if(mc.thePlayer.isInWater())
            return;

        if(PlayerUtils.isMoving()) {
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.speedInAir = 0.02f;
            }
            if(mc.thePlayer.fallDistance > 0.7 && mc.thePlayer.fallDistance < 1.3) {
                mc.thePlayer.speedInAir = 0.02f;
                mc.timer.timerSpeed = 1.04f;
            }
        }else{
            mc.thePlayer.motionX = 0D;
            mc.thePlayer.motionZ = 0D;
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {

    }
}

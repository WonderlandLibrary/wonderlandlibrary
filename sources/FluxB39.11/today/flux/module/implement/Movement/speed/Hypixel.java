package today.flux.module.implement.Movement.speed;


import com.darkmagician6.eventapi.EventTarget;
import today.flux.event.MoveEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.ModuleManager;
import today.flux.module.SubModule;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.implement.Combat.TargetStrafe;
import today.flux.module.implement.Movement.Speed;
import today.flux.utility.MoveUtils;
import today.flux.utility.PlayerUtils;

public class Hypixel extends SubModule {
    public Hypixel() {
        super("Hypixel", "Speed");
    }

    @EventTarget
    public void onPre(PreUpdateEvent e) {
        if (PlayerUtils.isMoving()) {
            if (MoveUtils.isOnGround(0.01) && mc.thePlayer.isCollidedVertically) {
                Speed.setMotion(null, Math.max(0.275, MoveUtils.defaultSpeed() * 0.9));
                mc.thePlayer.jump();
            } else if(!Speed.onGround.getValue()){
                if ((ModuleManager.targetStrafeModule.isEnabled() && (!TargetStrafe.jumpKey.getValueState() || mc.gameSettings.keyBindJump.isKeyDown()) && KillAura.target != null && KillAura.target.getDistanceToEntity(mc.thePlayer) < TargetStrafe.range.getValue()) || PlayerUtils.getSpeed() < PlayerUtils.getBaseMoveSpeed() * 0.7) {
                    Speed.setMotion(null, MoveUtils.defaultSpeed() * 0.9);
                } else if(!Speed.onGround.getValue()){
                    Speed.setMotion(null, PlayerUtils.getSpeed());
                }
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        if (PlayerUtils.isMoving()) {

        }
    }
}

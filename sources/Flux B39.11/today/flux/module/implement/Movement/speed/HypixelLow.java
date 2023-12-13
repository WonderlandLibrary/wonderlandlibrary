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

import java.util.function.Supplier;

public class HypixelLow extends SubModule {

    public HypixelLow() {
        super("Hypixel Low", "Speed");
    }

    // A Wrapping supplier to deduct whether to reduce Y motions.
    private final Supplier<Boolean> reductionSupplier = () -> {
        return mc.thePlayer.fallDistance < 3
                && !mc.thePlayer.isCollidedHorizontally
                && !mc.gameSettings.keyBindJump.isKeyDown()
                && !MoveUtils.isOverVoid()
                && PlayerUtils.isMoving();
    };

    @EventTarget
    @SuppressWarnings("unused")
    public void onUpdateEvent(PreUpdateEvent event) {
        final boolean flag = mc.thePlayer.moveStrafing != 0 || mc.thePlayer.moveForward < 0;
        if (PlayerUtils.isMoving()) {
            if (MoveUtils.isOnGround(0.01) && mc.thePlayer.isCollidedVertically) {
                Speed.setMotion(null, Math.max(0.275, MoveUtils.defaultSpeed() * 0.9));
            } else if(!Speed.onGround.getValue()){
                if ((ModuleManager.targetStrafeModule.isEnabled() && (!TargetStrafe.jumpKey.getValueState() || mc.gameSettings.keyBindJump.isKeyDown()) && KillAura.target != null && KillAura.target.getDistanceToEntity(mc.thePlayer) < TargetStrafe.range.getValue()) || PlayerUtils.getSpeed() < PlayerUtils.getBaseMoveSpeed() * 0.7) {
                    Speed.setMotion(null, MoveUtils.defaultSpeed() * 1.2);
                } else if(!Speed.onGround.getValue()){
                    Speed.setMotion(null, PlayerUtils.getSpeed());
                }
            }
        }
    }

    // Method to calculate and apply low motion
    private void reduceMotion(MoveEvent moveEvent, Supplier<Boolean> reductionSupplier) {
        if (reductionSupplier.get()) {
            if (MoveUtils.isOnGround(0.01) && mc.thePlayer.isCollidedVertically) {
                moveEvent.setY(0.36);
                mc.thePlayer.motionY = 0.36;
            }
            if (moveEvent.getY() < 0)
                moveEvent.setY(moveEvent.getY() - 0.15);
            if (moveEvent.getY() > 0)
                moveEvent.setY(mc.thePlayer.motionY -= 0.005);
        }
    }

    @EventTarget
    @SuppressWarnings("unused")
    public void onMoveEvent(MoveEvent event) {
        reduceMotion(event, reductionSupplier);
    }
}


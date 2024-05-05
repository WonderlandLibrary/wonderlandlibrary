package dev.tenacity.module.impl.movement.speeds.impl;

import dev.tenacity.Tenacity;
import dev.tenacity.event.impl.player.input.MoveInputEvent;
import dev.tenacity.event.impl.player.movement.MotionEvent;
import dev.tenacity.event.impl.player.movement.correction.JumpEvent;
import dev.tenacity.event.impl.player.movement.correction.StrafeEvent;
import dev.tenacity.module.impl.combat.KillAura;
import dev.tenacity.module.impl.exploit.Breaker;
import dev.tenacity.module.impl.movement.Scaffold;
import dev.tenacity.module.impl.movement.speeds.SpeedMode;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.utils.player.RotationUtils;

public class PolarSpeed extends SpeedMode {

    private float rotation;

    public PolarSpeed() {
        super("Polar");
    }

    @Override
    public void onMotionEvent(MotionEvent event) {

        if (!canPerformRotation()) {
            return;
        }

        this.rotation = mc.thePlayer.rotationYaw + 45;

        event.setYaw(this.rotation);
        RotationUtils.setVisualRotations(event);
    }

    @Override
    public void onMoveInputEvent(MoveInputEvent event) {

        if (!canPerformRotation()) {
            return;
        }

        MovementUtils.fixMovement(event, rotation);

        event.setStrafe(event.getForward());
    }

    @Override
    public void onStrafeEvent(StrafeEvent event) {

        if (!canPerformRotation()) {
            return;
        }

        event.setYaw(rotation);
    }

    @Override
    public void onJumpFixEvent(JumpEvent event) {

        if (!canPerformRotation()) {
            return;
        }

        event.setYaw(rotation);
    }

    @Override
    public void onJumpEvent(JumpEvent event) {
        event.cancel();
        super.onJumpEvent(event);
    }

    public boolean canPerformRotation() {
        return mc.theWorld != null && mc.thePlayer != null && !Tenacity.INSTANCE.getModuleCollection().get(KillAura.class).isEnabled() && !Tenacity.INSTANCE.getModuleCollection().get(Scaffold.class).isEnabled() && !Tenacity.INSTANCE.getModuleCollection().get(Breaker.class).isEnabled();
    }
}
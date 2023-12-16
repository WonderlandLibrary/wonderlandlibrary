package wtf.expensive.modules.impl.movement;

import net.minecraft.network.play.client.CConfirmTransactionPacket;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventInput;
import wtf.expensive.events.impl.player.EventMove;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.misc.TimerUtil;
import wtf.expensive.util.movement.MoveUtil;

/**
 * @author dedinside
 * @since 11.06.2023
 */
@FunctionAnnotation(name = "Speed", type = Type.Movement)
public class SpeedFunction extends Function {

    private final ModeSetting spdMode = new ModeSetting("Режим", "Matrix", "Matrix", "Timer", "Sunrise DMG", "Really World");


    public SpeedFunction() {
        addSettings(spdMode);
    }


    public boolean boosting;

    @Override
    protected void onEnable() {
        super.onEnable();
        timerUtil.reset();
        boosting = false;
    }

    public TimerUtil timerUtil = new TimerUtil();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket e) {
            handlePacketEvent(e);
        } else if (event instanceof EventMove) {
            handleEventMove((EventMove) event);
        } else if (event instanceof EventUpdate) {
            handleEventUpdate((EventUpdate) event);
        }
    }

    private void handleEventMove(EventMove eventMove) {
        if (spdMode.is("Matrix")) {
            if (!mc.player.isOnGround() && mc.player.fallDistance >= 0.5f && eventMove.toGround()) {
                applyMatrixSpeed();
            }
        }
    }

    private void handleEventUpdate(EventUpdate eventUpdate) {
        switch (spdMode.get()) {
            case "Matrix" -> {
                if (mc.player.isOnGround() && MoveUtil.isMoving())
                    mc.player.jump();
            }
            case "Really World" -> handleRWMode();
            case "Timer" -> handleTimerMode();
            case "Sunrise DMG" -> handleSunriseDamageMode();
        }
    }

    private void handlePacketEvent(EventPacket e) {
        if (spdMode.is("Really World")) {
            if (e.getPacket() instanceof CConfirmTransactionPacket p) {
                e.setCancel(true);
            }
            if (e.getPacket() instanceof SPlayerPositionLookPacket p) {
                mc.player.func_242277_a(new Vector3d(p.getX(), p.getY(), p.getZ()));
                mc.player.setRawPosition(p.getX(), p.getY(), p.getZ());
                toggle();
            }
        }
    }

    private void handleRWMode() {
        if (timerUtil.hasTimeElapsed(1150)) {
            boosting = true;
        }
        if (timerUtil.hasTimeElapsed(7000)) {
            boosting = false;
            timerUtil.reset();
        }
        if (boosting) {
            if (mc.player.isOnGround() && !mc.gameSettings.keyBindJump.pressed) {
                mc.player.jump();
            }
            mc.timer.timerSpeed = mc.player.ticksExisted % 2 == 0 ? 1.5f : 1.2f;
        } else {
            mc.timer.timerSpeed = 0.05f;
        }
    }

    private void applyMatrixSpeed() {
        double speed = 2;
        mc.player.motion.x *= speed;
        mc.player.motion.z *= speed;
        MoveUtil.StrafeMovement.oldSpeed *= speed;
    }

    private void handleTimerMode() {
        if (mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder()) {
            return;
        }

        float timerValue = 1;
        if (mc.player.fallDistance <= 0.1f) {
            timerValue = 1.34f;
        }
        if (mc.player.fallDistance > 1.0f) {
            timerValue = 0.6f;
        }

        if (MoveUtil.isMoving()) {
            mc.timer.timerSpeed = 1;
            if (mc.player.isOnGround()) {
                if (!mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.jump();
                }
            } else {
                mc.timer.timerSpeed = timerValue;
            }
        } else {
            mc.timer.timerSpeed = 1.0f;
        }
    }

    private void handleSunriseDamageMode() {
        double radians = MoveUtil.getDirection();

        if (MoveUtil.isMoving()) {
            if (mc.player.isOnGround()) {
                applySunriseGroundMotion(radians);
            } else if (mc.player.isInWater()) {
                applySunriseWaterMotion(radians);
            } else if (!mc.player.isOnGround()) {
                applySunriseAirMotion(radians);
            } else {
                applySunriseDefaultMotion(radians);
            }
        }
    }

    private void applySunriseGroundMotion(double radians) {
        mc.player.addVelocity(-MathHelper.sin(radians) * 9.5 / 24.5, 0, MathHelper.cos(radians) * 9.5 / 24.5);
        MoveUtil.setMotion(MoveUtil.getMotion());
    }

    private void applySunriseWaterMotion(double radians) {
        mc.player.addVelocity(-MathHelper.sin(radians) * 9.5 / 24.5, 0, MathHelper.cos(radians) * 9.5 / 24.5);
        MoveUtil.setMotion(MoveUtil.getMotion());
    }

    private void applySunriseAirMotion(double radians) {
        mc.player.addVelocity(-MathHelper.sin(radians) * 0.11 / 24.5, 0, MathHelper.cos(radians) * 0.11 / 24.5);
        MoveUtil.setMotion(MoveUtil.getMotion());
    }

    private void applySunriseDefaultMotion(double radians) {
        mc.player.addVelocity(-MathHelper.sin(radians) * 0.005 * MoveUtil.getMotion(), 0,
                MathHelper.cos(radians) * 0.005 * MoveUtil.getMotion());
        MoveUtil.setMotion(MoveUtil.getMotion());
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        super.onDisable();
    }
}

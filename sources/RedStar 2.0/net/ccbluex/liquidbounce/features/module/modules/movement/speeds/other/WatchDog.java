package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import java.util.Random;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000:\n\n\n\b\n\n\b\b\n\b\n\b\n\n\u0000\n\n\b\n\n\b\n\n\b\u000020B¢J0J\b0HJ0\rJ\b0HJ\b0HJ\b0HJ020HJ\b 0HJ!02\"02\t0R0X¢\n\u0000\b\"\b\bR\t0X¢\n\u0000\b\n\"\b\bR\f0\rX¢\n\u0000\b\"\bR0X¢\n\u0000R0X¢\n\u0000¨#"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/other/WatchDog;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "lastDist", "", "getLastDist", "()D", "setLastDist", "(D)V", "speed", "getSpeed", "setSpeed", "stage", "", "getStage", "()I", "setStage", "(I)V", "stage1", "", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "getBaseMoveSpeed", "getHypixelBest", "", "getJumpEffect", "onDisable", "onEnable", "onMotion", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onUpdate", "setMotion", "e", "Pride"})
public final class WatchDog
extends SpeedMode {
    private double lastDist;
    private double speed;
    private int stage = 1;
    private boolean stage1;
    private final MSTimer timer = new MSTimer();

    public final double getLastDist() {
        return this.lastDist;
    }

    public final void setLastDist(double d) {
        this.lastDist = d;
    }

    public final double getSpeed() {
        return this.speed;
    }

    public final void setSpeed(double d) {
        this.speed = d;
    }

    public final int getStage() {
        return this.stage;
    }

    public final void setStage(int n) {
        this.stage = n;
    }

    public final double getBaseMoveSpeed() {
        double baseSpeed = 0.3;
        EntityPlayerSP entityPlayerSP = SpeedMode.mc2.player;
        if (entityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (entityPlayerSP.func_70644_a(MobEffects.SPEED)) {
            MovementUtils.strafe(0.48f);
        }
        return baseSpeed;
    }

    public final int getJumpEffect() {
        int n;
        if (SpeedMode.mc2.player.func_70644_a(MobEffects.JUMP_BOOST)) {
            EntityPlayerSP entityPlayerSP = SpeedMode.mc2.player;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            PotionEffect potionEffect = entityPlayerSP.func_70660_b(MobEffects.JUMP_BOOST);
            if (potionEffect == null) {
                Intrinsics.throwNpe();
            }
            Intrinsics.checkExpressionValueIsNotNull(potionEffect, "mc2.player!!.getActivePo…(MobEffects.JUMP_BOOST)!!");
            n = potionEffect.getAmplifier() + 1;
        } else {
            n = 0;
        }
        return n;
    }

    public final void setMotion(@NotNull MoveEvent e, double speed) {
        Intrinsics.checkParameterIsNotNull(e, "e");
        IEntityPlayerSP iEntityPlayerSP = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double forward = iEntityPlayerSP.getMovementInput().getMoveForward();
        IEntityPlayerSP iEntityPlayerSP2 = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double strafe = iEntityPlayerSP2.getMovementInput().getMoveStrafe();
        IEntityPlayerSP iEntityPlayerSP3 = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        float rotationYaw = iEntityPlayerSP3.getRotationYaw();
        IEntityPlayerSP iEntityPlayerSP4 = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP4.getMoveForward() < 0.0f) {
            rotationYaw += 180.0f;
        }
        IEntityPlayerSP iEntityPlayerSP5 = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP5 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP5.getMoveStrafing() > 0.0f) {
            rotationYaw -= (float)((double)90.0f * forward);
        }
        IEntityPlayerSP iEntityPlayerSP6 = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP6 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP6.getMoveStrafing() < 0.0f) {
            rotationYaw += (float)((double)90.0f * forward);
        }
        IEntityPlayerSP iEntityPlayerSP7 = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP7 == null) {
            Intrinsics.throwNpe();
        }
        double yaw = iEntityPlayerSP7.getRotationYaw();
        if (forward == 0.0 && strafe == 0.0) {
            IEntityPlayerSP iEntityPlayerSP8 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP8.setMotionX(0.0);
            IEntityPlayerSP iEntityPlayerSP9 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP9 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP9.setMotionZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (double)(forward > 0.0 ? -44 : 44);
                } else if (strafe < 0.0) {
                    yaw += (double)(forward > 0.0 ? 44 : -44);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            e.setX(forward * speed * Math.cos(Math.toRadians(yaw + (double)90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + (double)90.0f)));
            e.setZ(forward * speed * Math.sin(Math.toRadians(yaw + (double)90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + (double)90.0f)));
        }
    }

    @Override
    public void onEnable() {
        this.stage = 2;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.speed = this.getBaseMoveSpeed();
        this.stage = 2;
    }

    @Override
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        double xDist = thePlayer.getPosX() - thePlayer.getPrevPosX();
        double zDist = thePlayer.getPosZ() - thePlayer.getPrevPosZ();
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.movement.Speed");
        }
        Speed speed1 = (Speed)module;
        if (this.stage1) {
            IEntityPlayerSP iEntityPlayerSP2 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if ((double)iEntityPlayerSP2.getFallDistance() <= 0.1) {
                SpeedMode.mc.getTimer().setTimerSpeed(((Number)speed1.HypixelTimerValue.get()).floatValue());
            }
            if (this.timer.hasTimePassed(600L)) {
                this.timer.reset();
                this.stage1 = !this.stage1;
            }
        } else {
            IEntityPlayerSP iEntityPlayerSP3 = SpeedMode.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if ((double)iEntityPlayerSP3.getFallDistance() > 0.1) {
                IEntityPlayerSP iEntityPlayerSP4 = SpeedMode.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if ((double)iEntityPlayerSP4.getFallDistance() < 1.3) {
                    SpeedMode.mc.getTimer().setTimerSpeed(((Number)speed1.HypixelDealyTimerValue.get()).floatValue());
                }
            }
            if (this.timer.hasTimePassed(400L)) {
                this.timer.reset();
                this.stage1 = !this.stage1;
            }
        }
        IEntityPlayerSP iEntityPlayerSP5 = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP5 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP5.getFallDistance() >= 1.3f) {
            SpeedMode.mc.getTimer().setTimerSpeed(1.0f);
        }
        if (this.stage > 0 && !thePlayer.isInWater()) {
            boolean stoptick = false;
            if (this.stage == 1 && thePlayer.getOnGround() && MovementUtils.isMoving()) {
                ++this.stage;
            }
            if (this.stage == 2 && thePlayer.getOnGround() && MovementUtils.isMoving()) {
                double d = 0.399999986886975 + (double)this.getJumpEffect() * 0.1;
                MoveEvent moveEvent = event;
                boolean bl = false;
                boolean bl2 = false;
                double it = d;
                boolean bl3 = false;
                IEntityPlayerSP iEntityPlayerSP6 = SpeedMode.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP6.setMotionY(it);
                double d2 = d;
                moveEvent.setY(d2);
            } else if (this.stage >= 3) {
                IEntityPlayerSP iEntityPlayerSP7 = SpeedMode.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP7.isCollidedVertically()) {
                    this.speed = this.getBaseMoveSpeed();
                    this.lastDist = 0.0;
                    this.stage = 0;
                }
            }
            this.getHypixelBest();
        } else {
            this.stage = 0;
        }
        if (MovementUtils.isMoving()) {
            this.setMotion(event, this.speed);
        } else {
            this.setMotion(event, 0.0);
            this.stage = 0;
        }
        WatchDog watchDog = this;
        ++watchDog.stage;
        int cfr_ignored_0 = watchDog.stage;
    }

    private final void getHypixelBest() {
        IEntityPlayerSP iEntityPlayerSP = SpeedMode.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        boolean slowdown = false;
        switch (this.stage) {
            case 1: {
                this.stage = 2;
                break;
            }
            case 2: {
                if (!thePlayer.getOnGround() || !MovementUtils.isMoving()) break;
                this.speed *= 1.7;
                break;
            }
            case 3: {
                this.speed += new Random().nextDouble() / (double)4799;
                double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                this.speed = this.lastDist - difference;
                this.speed -= new Random().nextDouble() / (double)4799;
                break;
            }
            default: {
                slowdown = (double)thePlayer.getFallDistance() > 0.0;
                IWorldClient iWorldClient = SpeedMode.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                if (!iWorldClient.getCollidingBoundingBoxes(thePlayer, thePlayer.getEntityBoundingBox().offset(0.0, thePlayer.getMotionY(), 0.0)).isEmpty() || thePlayer.isCollidedVertically() && thePlayer.getOnGround()) {
                    this.stage = 1;
                }
                this.speed = this.lastDist - this.lastDist / (double)159;
            }
        }
        this.speed = Math.max(this.speed - (slowdown ? Math.sqrt(this.lastDist * this.lastDist + this.speed * this.speed) * 0.012 : 0.02 * this.lastDist), this.getBaseMoveSpeed());
        if (slowdown) {
            this.speed *= 1.0 - this.lastDist / (double)50;
        }
    }

    public WatchDog() {
        super("WatchDog");
    }
}

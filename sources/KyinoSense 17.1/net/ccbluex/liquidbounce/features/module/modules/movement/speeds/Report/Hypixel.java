/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.potion.Potion
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;

public class Hypixel
extends SpeedMode {
    public static int stage;
    public double movementSpeed;
    boolean collided = false;
    boolean lessSlow;
    double less;
    private static final double distance = 0.0;

    public Hypixel() {
        super("Hypixel");
    }

    @Override
    public void onDisable() {
        Hypixel.mc.field_71428_T.field_74278_d = 1.0f;
    }

    @Override
    public void setMotion(MoveEvent event, double baseMoveSpeed, double d, boolean b) {
    }

    private double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Hypixel.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            int amplifier = Hypixel.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    @Override
    public void onEnable() {
        boolean player = Hypixel.mc.field_71439_g == null;
        this.collided = player ? false : Hypixel.mc.field_71439_g.field_70123_F;
        this.lessSlow = false;
        this.less = 0.0;
        stage = 2;
        Hypixel.mc.field_71428_T.field_74278_d = 1.0f;
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        MovementUtils.strafe();
    }

    public static double getBaseMovementSpeed() {
        double baseSpeed = 0.2873;
        if (Hypixel.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            int amplifier = Hypixel.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    private boolean canZoom() {
        return MovementUtils.isMoving() && Hypixel.mc.field_71439_g.field_70122_E;
    }

    @Override
    public void onMove(MoveEvent e) {
        Speed speed = (Speed)LiquidBounce.moduleManager.getModule(Speed.class);
        if (this.canZoom() && stage == 1) {
            this.movementSpeed = 1.66 * Hypixel.getBaseMovementSpeed() - 0.01;
            Hypixel.mc.field_71428_T.field_74278_d = ((Float)speed.speedtimerValue.get()).floatValue();
        } else if (this.canZoom() && stage == 2) {
            Hypixel.mc.field_71439_g.field_70181_x = ((Float)speed.speedYValue.get()).floatValue();
            e.setY(((Float)speed.speedYValue.get()).floatValue());
            this.movementSpeed *= 1.63;
            Hypixel.mc.field_71428_T.field_74278_d = ((Float)speed.speedtimerValue.get()).floatValue();
        } else if (stage == 3) {
            double difference = 0.66 * (0.0 - Hypixel.getBaseMovementSpeed());
            this.movementSpeed = 0.0 - difference;
            Hypixel.mc.field_71428_T.field_74278_d = ((Float)speed.speedtimerValue.get()).floatValue();
        } else {
            List collidingList = Hypixel.mc.field_71441_e.func_72945_a((Entity)Hypixel.mc.field_71439_g, Hypixel.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, Hypixel.mc.field_71439_g.field_70181_x, 0.0));
            if (collidingList.size() > 0 || Hypixel.mc.field_71439_g.field_70124_G && stage > 0) {
                stage = MovementUtils.isMoving() ? 1 : 0;
            }
            this.movementSpeed = 0.0;
        }
        this.movementSpeed = Math.max(this.movementSpeed, Hypixel.getBaseMovementSpeed());
        this.setMoveSpeed(e, this.movementSpeed);
        if (MovementUtils.isMoving()) {
            ++stage;
        }
        MovementUtils.strafe();
    }

    public void setMoveSpeed(MoveEvent event, double speed) {
        double forward = Hypixel.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = Hypixel.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = Hypixel.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
}


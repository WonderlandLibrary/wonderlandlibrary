/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class CNHypixel
extends SpeedMode {
    public static int stage;
    public static MSTimer lastCheck;
    private final MSTimer timer = new MSTimer();
    public boolean shouldslow = false;
    boolean collided = false;
    boolean lessSlow;
    double less;
    double stair;
    private double speed;

    public CNHypixel() {
        super("CNHypixel");
    }

    public static int getSpeedEffect() {
        if (CNHypixel.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            return CNHypixel.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c() + 1;
        }
        return 0;
    }

    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.func_71410_x().field_71439_g.func_70644_a(Potion.field_76424_c)) {
            int amplifier = Minecraft.func_71410_x().field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static int getJumpEffect() {
        if (CNHypixel.mc.field_71439_g.func_70644_a(Potion.field_76430_j)) {
            return CNHypixel.mc.field_71439_g.func_70660_b(Potion.field_76430_j).func_76458_c() + 1;
        }
        return 0;
    }

    public static boolean isMoving2() {
        return CNHypixel.mc.field_71439_g.field_70701_bs != 0.0f || CNHypixel.mc.field_71439_g.field_70702_br != 0.0f;
    }

    public static boolean isOnGround(double height) {
        return !CNHypixel.mc.field_71441_e.func_72945_a((Entity)CNHypixel.mc.field_71439_g, CNHypixel.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -height, 0.0)).isEmpty();
    }

    public static boolean isInLiquid() {
        if (CNHypixel.mc.field_71439_g.func_70090_H()) {
            return true;
        }
        boolean inLiquid = false;
        int y = (int)CNHypixel.mc.field_71439_g.func_174813_aQ().field_72338_b;
        for (int x = MathHelper.func_76128_c((double)CNHypixel.mc.field_71439_g.func_174813_aQ().field_72340_a); x < MathHelper.func_76128_c((double)CNHypixel.mc.field_71439_g.func_174813_aQ().field_72336_d) + 1; ++x) {
            for (int z = MathHelper.func_76128_c((double)CNHypixel.mc.field_71439_g.func_174813_aQ().field_72339_c); z < MathHelper.func_76128_c((double)CNHypixel.mc.field_71439_g.func_174813_aQ().field_72334_f) + 1; ++z) {
                Block block = CNHypixel.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
                if (block == null || block.func_149688_o() == Material.field_151579_a) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                inLiquid = true;
            }
        }
        return inLiquid;
    }

    @Override
    public void onEnable() {
        boolean player = CNHypixel.mc.field_71439_g == null;
        this.collided = !player && CNHypixel.mc.field_71439_g.field_70123_F;
        this.lessSlow = false;
        this.less = 0.0;
        stage = 2;
        CNHypixel.mc.field_71428_T.field_74278_d = 1.0f;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        CNHypixel.mc.field_71428_T.field_74278_d = 1.0f;
        super.onDisable();
    }

    @Override
    public void setMotion(MoveEvent event, double baseMoveSpeed, double d, boolean b) {
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
        if (CNHypixel.mc.field_71439_g.field_70123_F) {
            this.collided = true;
        }
        if (this.collided) {
            CNHypixel.mc.field_71428_T.field_74278_d = 1.0f;
            stage = -1;
        }
        if (this.stair > 0.0) {
            this.stair -= 0.25;
        }
        this.less -= this.less > 1.0 ? 0.12 : 0.11;
        if (this.less < 0.0) {
            this.less = 0.0;
        }
        if (!CNHypixel.isInLiquid() && CNHypixel.isOnGround(0.01) && CNHypixel.isMoving2()) {
            this.collided = CNHypixel.mc.field_71439_g.field_70123_F;
            if (stage >= 0 || this.collided) {
                stage = 0;
                double a = LiquidBounce.moduleManager.getModule(Scaffold.class).getState() ? 0.407 : 0.41999742;
                double motY = a + (double)CNHypixel.getJumpEffect() * 0.1;
                if (this.stair == 0.0) {
                    CNHypixel.mc.field_71439_g.func_70664_aZ();
                    CNHypixel.mc.field_71439_g.field_70181_x = motY;
                    event.setY(CNHypixel.mc.field_71439_g.field_70181_x);
                }
                this.less += 1.0;
                boolean bl = this.lessSlow = this.less > 1.0 && !this.lessSlow;
                if (this.less > 1.12) {
                    this.less = 1.12;
                }
            }
        }
        this.speed = this.getHypixelSpeed(stage) + 0.01 + Math.random() / 500.0;
        this.speed *= 0.87;
        if (this.stair > 0.0) {
            this.speed *= 0.7 - (double)CNHypixel.getSpeedEffect() * 0.1;
        }
        if (stage < 0) {
            this.speed = CNHypixel.defaultSpeed();
        }
        if (this.lessSlow) {
            this.speed *= 0.95;
        }
        if (CNHypixel.isInLiquid()) {
            this.speed = 0.12;
        }
        if (CNHypixel.mc.field_71439_g.field_70701_bs != 0.0f || CNHypixel.mc.field_71439_g.field_70702_br != 0.0f) {
            this.setMotion(event, this.speed);
            ++stage;
        }
    }

    private double getHypixelSpeed(int stage) {
        double value = CNHypixel.defaultSpeed() + 0.028 * (double)CNHypixel.getSpeedEffect() + (double)CNHypixel.getSpeedEffect() / 15.0;
        double firstvalue = 0.4145 + (double)CNHypixel.getSpeedEffect() / 12.5;
        double decr = (double)stage / 500.0 * 2.0;
        if (stage == 0) {
            if (this.timer.delay(300.0f)) {
                this.timer.reset();
            }
            if (!lastCheck.delay(500.0f)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            } else if (this.shouldslow) {
                this.shouldslow = false;
            }
            value = 0.64 + ((double)CNHypixel.getSpeedEffect() + 0.028 * (double)CNHypixel.getSpeedEffect()) * 0.134;
        } else if (stage == 1) {
            value = firstvalue;
        } else if (stage >= 2) {
            value = firstvalue - decr;
        }
        if (this.shouldslow || !lastCheck.delay(500.0f) || this.collided) {
            value = 0.2;
            if (stage == 0) {
                value = 0.0;
            }
        }
        return Math.max(value, this.shouldslow ? value : CNHypixel.defaultSpeed() + 0.028 * (double)CNHypixel.getSpeedEffect());
    }

    private void setMotion(MoveEvent em, double speed) {
        double forward = CNHypixel.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = CNHypixel.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = CNHypixel.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            em.setX(0.0);
            em.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -41 : 41);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 41 : -41);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    static {
        lastCheck = new MSTimer();
    }
}


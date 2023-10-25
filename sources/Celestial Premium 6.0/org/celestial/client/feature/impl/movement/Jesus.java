/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventLiquidSolid;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Jesus
extends Feature {
    public static ListSetting mode;
    public static NumberSetting speed;
    public static NumberSetting NCPSpeed;
    public static NumberSetting motionUp;
    public static NumberSetting boostSpeed;
    public static NumberSetting boostTicks;
    public static BooleanSetting boost;
    public static BooleanSetting useTimer;
    private final NumberSetting timerSpeed;
    private final BooleanSetting speedCheck;
    private final BooleanSetting autoMotionStop;
    private final BooleanSetting autoWaterDown;
    private int waterTicks = 0;

    public Jesus() {
        super("Jesus", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0445\u043e\u0434\u0438\u0442\u044c \u043f\u043e \u0432\u043e\u0434\u0435", Type.Movement);
        mode = new ListSetting("Jesus Mode", "Matrix", () -> true, "Matrix", "Sunrise", "Sunrise New", "Matrix Zoom", "Matrix Solid", "NCP");
        speed = new NumberSetting("Speed", 0.65f, 0.1f, 10.0f, 0.01f, () -> !Jesus.mode.currentMode.equals("Sunrise New") && !Jesus.mode.currentMode.equals("NCP"));
        NCPSpeed = new NumberSetting("NCP Speed", 0.25f, 0.01f, 0.5f, 0.01f, () -> Jesus.mode.currentMode.equals("NCP"));
        boost = new BooleanSetting("Boost", true, () -> Jesus.mode.currentMode.equals("Matrix Solid"));
        boostSpeed = new NumberSetting("Boost Speed", 1.35f, 0.1f, 4.0f, 0.01f, () -> Jesus.mode.currentMode.equals("Matrix Solid") && boost.getCurrentValue());
        motionUp = new NumberSetting("Motion Up", 0.42f, 0.1f, 2.0f, 0.01f, () -> Jesus.mode.currentMode.equals("Matrix"));
        boostTicks = new NumberSetting("Boost Ticks", 2.0f, 0.0f, 30.0f, 1.0f, () -> Jesus.mode.currentMode.equals("Matrix Solid") && boost.getCurrentValue());
        this.speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        useTimer = new BooleanSetting("Use Timer", false, () -> true);
        this.timerSpeed = new NumberSetting("Timer Speed", 1.05f, 1.01f, 1.5f, 0.01f, () -> useTimer.getCurrentValue());
        this.autoMotionStop = new BooleanSetting("Auto Motion Stop", true, () -> Jesus.mode.currentMode.equals("Sunrise") || Jesus.mode.currentMode.equals("Matrix Solid"));
        this.autoWaterDown = new BooleanSetting("Auto Water Down", false, () -> Jesus.mode.currentMode.equals("Sunrise") || Jesus.mode.currentMode.equals("Matrix Solid"));
        this.addSettings(mode, speed, NCPSpeed, useTimer, this.timerSpeed, boost, boostSpeed, boostTicks, motionUp, this.speedCheck, this.autoWaterDown, this.autoMotionStop);
    }

    @Override
    public void onDisable() {
        Jesus.mc.timer.timerSpeed = 1.0f;
        if ((Jesus.mode.currentMode.equals("Sunrise") || Jesus.mode.currentMode.equals("Matrix Solid")) && this.autoWaterDown.getCurrentValue()) {
            Jesus.mc.player.motionY -= 500.0;
        }
        this.waterTicks = 0;
        super.onDisable();
    }

    @EventTarget
    public void onLiquidBB(EventLiquidSolid event) {
        if (Jesus.mode.currentMode.equals("Matrix Solid") || Jesus.mode.currentMode.equals("Sunrise") || Jesus.mode.currentMode.equals("NCP")) {
            event.setCancelled(true);
        }
    }

    private boolean isWater() {
        BlockPos bp1 = new BlockPos(Jesus.mc.player.posX - 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ - 0.5);
        BlockPos bp2 = new BlockPos(Jesus.mc.player.posX - 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ + 0.5);
        BlockPos bp3 = new BlockPos(Jesus.mc.player.posX + 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ + 0.5);
        BlockPos bp4 = new BlockPos(Jesus.mc.player.posX + 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ - 0.5);
        return Jesus.mc.player.world.getBlockState(bp1).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp2).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp3).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp4).getBlock() == Blocks.WATER || Jesus.mc.player.world.getBlockState(bp1).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp2).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp3).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp4).getBlock() == Blocks.LAVA;
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        this.setSuffix(mode.getCurrentMode());
        if (!Jesus.mc.player.isPotionActive(MobEffects.SPEED) && this.speedCheck.getCurrentValue()) {
            return;
        }
        BlockPos blockPos = new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY - 0.1, Jesus.mc.player.posZ);
        Block block = Jesus.mc.world.getBlockState(blockPos).getBlock();
        if (useTimer.getCurrentValue()) {
            Jesus.mc.timer.timerSpeed = this.timerSpeed.getCurrentValue();
        }
        switch (Jesus.mode.currentMode) {
            case "Matrix": {
                if (!Jesus.mc.player.isInLiquid() || !(Jesus.mc.player.motionY < 0.0)) break;
                Jesus.mc.player.motionY = motionUp.getCurrentValue();
                MovementHelper.setSpeed(speed.getCurrentValue());
                break;
            }
            case "Matrix Zoom": {
                if (Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY - (double)0.2f, Jesus.mc.player.posZ)).getBlock() instanceof BlockLiquid && !Jesus.mc.player.onGround) {
                    MovementHelper.setSpeed(speed.getCurrentValue());
                }
                if (!(Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + (double)1.0E-4f, Jesus.mc.player.posZ)).getBlock() instanceof BlockLiquid)) break;
                Jesus.mc.player.motionX = 0.0;
                Jesus.mc.player.motionZ = 0.0;
                Jesus.mc.player.motionY = 0.05f;
                break;
            }
            case "Sunrise New": {
                if (Jesus.mc.player.isInLiquid()) {
                    double d = Jesus.mc.player.motionY = Jesus.mc.player.ticksExisted % 2 != 0 ? 0.01 : -0.01;
                }
                if (Jesus.mc.player.motionY != -0.01 || !Jesus.mc.player.isInLiquid()) break;
                Jesus.mc.player.onGround = false;
                MovementHelper.setSpeed(0.18f);
                break;
            }
            case "Matrix Solid": {
                if (this.isWater() && block instanceof BlockLiquid) {
                    ++this.waterTicks;
                    Jesus.mc.player.motionY = 0.0;
                    Jesus.mc.player.onGround = false;
                    Jesus.mc.player.isAirBorne = true;
                    MovementHelper.setSpeed(boost.getCurrentValue() ? ((float)Jesus.mc.player.ticksExisted % boostTicks.getCurrentValue() == 0.0f ? speed.getCurrentValue() : boostSpeed.getCurrentValue()) : speed.getCurrentValue());
                    event.setPosY(Jesus.mc.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.02 : event.getPosY() - 0.02);
                    event.setOnGround(false);
                    break;
                }
                this.waterTicks = MathHelper.clamp(this.waterTicks, 0, this.waterTicks);
                this.waterTicks -= 10;
                if (!this.autoMotionStop.getCurrentValue() || this.waterTicks < 5 || Jesus.mc.player.onGround) break;
                MovementHelper.setSpeed(0.0f);
                break;
            }
            case "NCP": {
                if (!this.isWater() || !(block instanceof BlockLiquid)) break;
                Jesus.mc.player.motionY = 0.0;
                Jesus.mc.player.onGround = false;
                Jesus.mc.player.isAirBorne = true;
                MovementHelper.setSpeed(NCPSpeed.getCurrentValue());
                event.setPosY(Jesus.mc.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.02 : event.getPosY() - 0.02);
                event.setOnGround(false);
                break;
            }
            case "Sunrise": {
                if (this.isWater() && block instanceof BlockLiquid) {
                    ++this.waterTicks;
                    Jesus.mc.player.motionY = 0.0;
                    event.setOnGround(false);
                    Jesus.mc.player.onGround = false;
                    event.setPosY(Jesus.mc.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.02 : event.getPosY() - 0.02);
                    if (Jesus.mc.player.ticksExisted % 2 == 0) {
                        MovementHelper.setSpeed(speed.getCurrentValue() * 5.2f);
                        break;
                    }
                    MovementHelper.setSpeed(0.07f);
                    break;
                }
                this.waterTicks = MathHelper.clamp(this.waterTicks, 0, this.waterTicks);
                this.waterTicks -= 10;
                if (!this.autoMotionStop.getCurrentValue() || this.waterTicks < 5 || Jesus.mc.player.onGround) break;
                MovementHelper.setSpeed(0.0f);
            }
        }
    }
}


package org.dreamcore.client.feature.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventLiquidSolid;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class LiquidWalk extends Feature {

    public static ListSetting mode;
    public static NumberSetting speed;
    public static NumberSetting motionUp;
    public static NumberSetting boostSpeed;
    public static NumberSetting boostTicks;
    public static BooleanSetting boost;
    private final BooleanSetting speedCheck;
    public boolean start = false;

    public BooleanSetting groundSpoof = new BooleanSetting("GroundSpoof", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting solid = new BooleanSetting("Solid", false, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting groundSpoofExploit = new BooleanSetting("GroundSpoof Exploit", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting useMotion = new BooleanSetting("Use Motion", false, () -> mode.currentMode.equals("Custom"));
    public NumberSetting motionY = new NumberSetting("Y-Motion", 0.42F, 0, 2, 0.01F, () -> mode.currentMode.equals("Custom") && useMotion.getBoolValue());
    public BooleanSetting useSpeed = new BooleanSetting("Use Speed", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting useJumpMoveFactor = new BooleanSetting("Use Jump Move Factor", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting useTimer = new BooleanSetting("Use Timer", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting useTimerExploit = new BooleanSetting("Timer Exploit", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting fallDistExploit = new BooleanSetting("Fall Distance Exploit", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting useFallDist = new BooleanSetting("Use Fall Distance", true, () -> mode.currentMode.equals("Custom"));
    public NumberSetting groundSpoofMotion = new NumberSetting("GroundSpoof Y-Motion", 0.08F, 0, 1, 0.001F, () -> mode.currentMode.equals("Custom") && groundSpoofExploit.getBoolValue());
    public NumberSetting inLiquid = new NumberSetting("In Liquid", 0.1F, 0, 1, 0.001F, () -> mode.currentMode.equals("Custom"));
    public NumberSetting fallDist = new NumberSetting("Fall Distance", 0, 0, 10000, 0.1F, () -> mode.currentMode.equals("Custom") && useFallDist.getBoolValue());
    public NumberSetting jumpMoveFactor = new NumberSetting("Jump Move Factor", 0.1F, 0, 10, 0.001F, () -> mode.currentMode.equals("Custom") && useJumpMoveFactor.getBoolValue());
    public NumberSetting timer = new NumberSetting("Timer", 1, 0.001F, 15, 0.001F, () -> mode.currentMode.equals("Custom") && useTimer.getBoolValue());

    public LiquidWalk() {
        super("Liquid Walk", "Позволяет ходить по воде", Type.Movement);
        mode = new ListSetting("LiquidWalk Mode", "Matrix Jump", () -> true, "Matrix Jump", "Dolphin", "Matrix Ground", "Matrix Zoom", "Matrix Solid", "Custom");
        speed = new NumberSetting("Speed", 1, 0.1F, 15, 0.1F, () -> !mode.currentMode.equals("Dolphin") && (mode.currentMode.equals("Custom") && useSpeed.getBoolValue()) || mode.currentMode.equals("Matrix Ground") || mode.currentMode.equals("Matrix Solid") || mode.currentMode.equals("Matrix Jump"));
        boost = new BooleanSetting("Boost", false, () -> mode.currentMode.equals("Matrix Ground") || mode.currentMode.equals("Custom"));
        boostSpeed = new NumberSetting("Boost Speed", 1.35F, 0.1F, 4, 0.01F, () -> boost.getBoolValue() && mode.currentMode.equals("Custom") || mode.currentMode.equals("Matrix Ground"));
        motionUp = new NumberSetting("Motion Up", 0.42F, 0.1F, 2, 0.01F, () -> mode.currentMode.equals("Matrix Jump"));
        boostTicks = new NumberSetting("Boost Ticks", 2, 0, 30, 1, () -> boost.getBoolValue() && mode.currentMode.equals("Custom") || mode.currentMode.equals("Matrix Ground"));
        speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        addSettings(mode, useMotion, useSpeed, useFallDist, useJumpMoveFactor, useTimer, speed, motionY, timer, useTimerExploit, groundSpoof, groundSpoofExploit, groundSpoofMotion, solid, inLiquid, fallDist, fallDistExploit, jumpMoveFactor, boost, boostSpeed, boostTicks, motionUp, speedCheck);
    }

    @EventTarget
    public void onLiquidBB(EventLiquidSolid event) {
        if (mode.currentMode.equals("Matrix Ground") || mode.currentMode.equals("Matrix Solid") || (solid.getBoolValue() && mode.currentMode.equals("Custom"))) {
            event.setCancelled(!mc.gameSettings.keyBindJump.isKeyDown());
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        this.setSuffix(mode.getCurrentMode());
        BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ);
        Block block = mc.world.getBlockState(blockPos).getBlock();
        if (!mc.player.isPotionActive(MobEffects.SPEED) && speedCheck.getBoolValue())
            return;
        switch (mode.currentMode) {
            case "Matrix Jump":
                if (mc.player.isInLiquid() && mc.player.motionY < 0) {
                    mc.player.motionY = motionUp.getNumberValue();
                    MovementHelper.setSpeed(speed.getNumberValue());
                }
                break;
            case "Dolphin":
                if (mc.player.isInLiquid()) {
                    mc.player.motionX *= 1.17;
                    mc.player.motionZ *= 1.17;

                    if (mc.player.isCollidedHorizontally) {
                        mc.player.motionY = 0.24;
                    } else if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() != Blocks.AIR)
                        mc.player.motionY += 0.04;
                }
                break;
            case "Matrix Ground":
                if (block instanceof BlockLiquid) {
                    mc.player.onGround = false;
                    mc.player.isAirBorne = true;
                    MovementHelper.setSpeed(boost.getBoolValue() ? mc.player.ticksExisted % boostTicks.getNumberValue() == 0 ? speed.getNumberValue() : boostSpeed.getNumberValue() : speed.getNumberValue());
                    event.setPosY(mc.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.02 : event.getPosY() - 0.02);
                    event.setOnGround(false);
                }
                break;
            case "Matrix Zoom":
                if (block instanceof BlockLiquid) {
                    MovementHelper.setSpeed(speed.getNumberValue());
                }

                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.0000001, mc.player.posZ)).getBlock() == Blocks.WATER) {
                    mc.player.fallDistance = 0;
                    mc.player.motionX = 0;
                    mc.player.motionY = 0.08;
                    mc.player.jumpMovementFactor = 0.2f;
                    mc.player.motionZ = 0;
                }
                break;
            case "Matrix Solid":
                if (block instanceof BlockLiquid) {
                    mc.player.onGround = false;
                    mc.player.isAirBorne = true;
                    MovementHelper.setSpeed(mc.player.ticksExisted % 2 == 0 ? speed.getNumberValue() : 0.1F);
                    event.setPosY(mc.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.02 : event.getPosY() - 0.02);
                    event.setOnGround(false);
                }
                break;


            case "Custom":

                BlockPos blockPosCustom = new BlockPos(mc.player.posX, mc.player.posY - inLiquid.getNumberValue(), mc.player.posZ);
                Block blockCustom = mc.world.getBlockState(blockPosCustom).getBlock();

                if (blockCustom instanceof BlockLiquid) {

                    if (useFallDist.getBoolValue()) {
                        mc.player.fallDistance = fallDist.getNumberValue();
                    }
                    if (useJumpMoveFactor.getBoolValue()) {
                        mc.player.jumpMovementFactor = jumpMoveFactor.getNumberValue();
                    }

                    if (useMotion.getBoolValue()) {
                        mc.player.motionY = motionY.getNumberValue();
                    }

                    if (useTimer.getBoolValue()) {
                        mc.timer.timerSpeed = timer.getNumberValue();
                    }

                    if (useTimerExploit.getBoolValue()) {
                        mc.timer.timerSpeed = mc.player.ticksExisted % 60 > 39 ? 1000 : 1;
                    }

                    if (groundSpoof.getBoolValue()) {
                        mc.player.onGround = false;
                    }

                    if (fallDistExploit.getBoolValue()) {
                        mc.player.fallDistance = (float) (Math.random() * 1.0E-12);
                    }

                    if (useSpeed.getBoolValue()) {
                        MovementHelper.setSpeed(boost.getBoolValue() ? mc.player.ticksExisted % boostTicks.getNumberValue() == 0 ? speed.getNumberValue() : boostSpeed.getNumberValue() : speed.getNumberValue());
                    }

                    if (groundSpoofExploit.getBoolValue()) {
                        event.setPosY(mc.player.ticksExisted % 2 == 0 ? event.getPosY() + groundSpoofMotion.getNumberValue() : event.getPosY() - groundSpoofMotion.getNumberValue());
                        event.setOnGround(false);
                    }
                }
                break;

        }
    }
}

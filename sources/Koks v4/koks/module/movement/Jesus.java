package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.api.utils.RandomUtil;
import koks.event.LiquidBoundingBoxEvent;
import koks.event.UpdateEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Jesus", description = "You can walk over water", category = Module.Category.MOVEMENT)
public class Jesus extends Module {

    @Value(name = "Mode", modes = {"Solid", "Intave13", "Matrix6.0.3", "NCP", "Karhu 2.2 169"})
    String mode = "Solid";

    @Value(name = "Intave13-SpeedBoost", displayName = "Boost")
    boolean intave13SpeedBoost = false;

    @Value(name = "Matrix6.0.3-Speed", minimum = 0.1, maximum = 1)
    double matrix603Speed = 0.6;

    public boolean isLiquid(Block block) {
        return (block == Blocks.water || block == Blocks.flowing_water || block == Blocks.lava || block == Blocks.flowing_lava);
    }

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "Matrix6.0.3-Speed":
                return mode.equalsIgnoreCase("Matrix6.0.3");
            case "Intave13-SpeedBoost":
                return mode.equalsIgnoreCase("Intave13");
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        final MovementUtil movementUtil = MovementUtil.getInstance();
        final RandomUtil randomUtil = RandomUtil.getInstance();
        if (event instanceof LiquidBoundingBoxEvent) {
            switch (mode) {
                case "Solid":
                    final LiquidBoundingBoxEvent liquidBoundingBoxEvent = (LiquidBoundingBoxEvent) event;
                    final BlockPos pos = liquidBoundingBoxEvent.getBlockPos();
                    final BlockLiquid block = liquidBoundingBoxEvent.getBlock();
                    liquidBoundingBoxEvent.setAxisAlignedBB(new AxisAlignedBB((double) pos.getX() + block.minX, (double) pos.getY() + block.minY, (double) pos.getZ() + block.minZ, (double) pos.getX() + block.maxX, (double) pos.getY() + block.maxY, (double) pos.getZ() + block.maxZ));
                    break;
            }
        }
        if (event instanceof UpdateEvent) {
            switch (mode) {
                case "Karhu 2.2 169":
                    if(getPlayer().isInWater() && !getPlayer().isCollidedHorizontally) {
                        getPlayer().motionY = 0.005;
                        if(isMoving())
                            movementUtil.setSpeed(0.45);
                    }
                    break;
                case "Matrix6.0.3":
                    if (isLiquid(getBlockUnderPlayer(0)) && getWorld().getCollidingBoundingBoxes(getPlayer(), getPlayer().getEntityBoundingBox().expand(0.2, 0, 0.2)).size() == 0) {
                        if (getPlayer().isWet()) {
                            getPlayer().motionY = 0.05;
                            if (isMoving())
                                movementUtil.setSpeed(randomUtil.getRandomFloat((float) matrix603Speed - 0.01F, (float) matrix603Speed));
                        }
                    }
                    break;
                case "Intave13":
                    if (isLiquid(getBlockUnderPlayer(0))) {
                        getGameSettings().keyBindJump.pressed = false;
                        getPlayer().motionY = 0.005;
                        getPlayer().speedInAir = 0.0225F;
                        getPlayer().onGround = intave13SpeedBoost;
                        getPlayer().motionZ *= 0.9F;
                        getPlayer().motionX *= 0.9F;
                    }
                    break;
                case "NCP":
                    if (getPlayer().isInWater()) {
                        getPlayer().motionY = 0;
                        getPlayer().onGround = true;
                    }
                    break;
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}

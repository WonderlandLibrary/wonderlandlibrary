package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.api.utils.MovementUtil;
import koks.event.UpdateEvent;
import net.minecraft.block.*;
import net.minecraft.util.MathHelper;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "StairSpeed", description = "You are fast on stairs", category = Module.Category.MOVEMENT)
public class StairSpeed extends Module implements Module.Unsafe {

    @Value(name = "Boost", minimum = 0.1, maximum = 10)
    double boost = 0.7;

    @Value(name = "MaxBoost", minimum = 0.1, maximum = 10)
    double maxBoost = 9;

    @Value(name = "Decrement", minimum = 0, maximum = 1)
    double decrement = 0.05;

    @Value(name = "Air-Decrement", minimum = 0, maximum = 10)
    double airDecrement = 3;

    @Value(name = "KeepBoost")
    boolean keepBoost = false;

    double speed;

    final MovementUtil movementUtil = MovementUtil.getInstance();

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof UpdateEvent) {
            if (isMoving() && getPlayer().getFoodStats().getFoodLevel() > 6.0F) {
                final boolean flag = getBlockUnderPlayer(0.1F) instanceof BlockStairs || getBlockUnderPlayer(0.1F) instanceof BlockSlab && !(getBlockUnderPlayer(0.1F) instanceof BlockDoubleWoodSlab) && !(getBlockUnderPlayer(0.1F) instanceof BlockDoubleStoneSlab && !(getBlockUnderPlayer(0.1F) instanceof BlockDoubleStoneSlabNew));
                if (flag) {
                    speed += boost / 10;
                    speed = MathHelper.clamp_double(speed, 0, maxBoost / 10);
                } else if (speed > 0.2) {
                    if (getPlayer().onGround) {
                        speed -= decrement / 10;
                    } else {
                        speed -= airDecrement / 10;
                    }
                    speed = Math.max(speed, 0);
                }
                if (speed > 0.2 && (flag || keepBoost))
                    movementUtil.setSpeed(speed, getYaw());
            } else {
                speed = 0;
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        speed = 0;
    }
}

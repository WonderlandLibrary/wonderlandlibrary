package me.nyan.flush.module.impl.movement;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventStep;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Step extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "NCP");
    private final NumberSetting height = new NumberSetting("Height", this, 2, 1, 10, 0.25,
            () -> mode.is("vanilla"));

    public Step() {
        super("Step", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onStep(EventStep e) {
        if (mode.is("vanilla")) {
            e.setStepHeight(height.getValueFloat());
        }
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (!(MovementUtils.isMoving() && mode.is("NCP") && mc.thePlayer.isCollidedHorizontally)) {
            return;
        }
        BlockPos blockPos = new BlockPos(
                mc.thePlayer.posX - (MovementUtils.isMoving() ? MathHelper.sin(MovementUtils.getDirection()) : 0),
                mc.thePlayer.posY,
                mc.thePlayer.posZ + (MovementUtils.isMoving() ? MathHelper.cos(MovementUtils.getDirection()) : 0)
        );
        BlockPos blockPosH = new BlockPos(
                mc.thePlayer.posX - (MovementUtils.isMoving() ? MathHelper.sin(MovementUtils.getDirection()) : 0),
                mc.thePlayer.posY + 1,
                mc.thePlayer.posZ + (MovementUtils.isMoving() ? MathHelper.cos(MovementUtils.getDirection()) : 0)
        );
        if (!blockPos.getBlock().canCollideCheck(mc.theWorld.getBlockState(blockPos), false) ||
                blockPos.getBlock().getBlockBoundsMaxY() <= 0.6) {
            return;
        }

        if (blockPosH.getBlock().canCollideCheck(mc.theWorld.getBlockState(blockPosH), false)) {
            return;
        }

        if (MovementUtils.isOnGround(0.05) && !isEnabled(Speed.class)) {
            mc.thePlayer.jump();
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}

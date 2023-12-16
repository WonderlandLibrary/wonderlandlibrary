package wtf.expensive.modules.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.movement.MoveUtil;

/**
 * @author dedinside
 * @since 09.06.2023
 */
@FunctionAnnotation(name = "Jesus", type = Type.Movement)
public class JesusFunction extends Function {

    private ModeSetting jesusMode = new ModeSetting("Режим", "Matrix Solid", "Matrix Solid", "Matrix Zoom");

    private SliderSetting zoomSpeed = new SliderSetting("Скорость", 7.0F, 2.0F, 10.0F, 0.1F).setVisible(() -> jesusMode.is("Matrix Zoom"));
    private BooleanOption noJump = new BooleanOption("Не приземляться", false).setVisible(() -> jesusMode.is("Matrix Solid"));

    private int ticks;

    public JesusFunction() {
        addSettings(jesusMode, zoomSpeed, noJump);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMotion e) {
            handleEventMove(e);
        }
    }

    /**
     * Переделать
     */
    private void handleEventMove(EventMotion e) {
        if (jesusMode.is("Matrix Solid")) {
            handleWaterAndAirMovement(e);
        }
    }


    private void handleWaterAndAirMovement(EventMotion motion) {
        handleWaterMovement();
        handleAirMovement(motion);
    }

    private void handleWaterMovement() {
        BlockPos playerPos = new BlockPos(mc.player.getPosX(), mc.player.getPosY() + 0.008D, mc.player.getPosZ());
        Block playerBlock = mc.world.getBlockState(playerPos).getBlock();
        if (playerBlock == Blocks.WATER && !mc.player.isOnGround()) {
            boolean isUp = mc.world.getBlockState(new BlockPos(mc.player.getPosX(), mc.player.getPosY() + 0.03D, mc.player.getPosZ())).getBlock() == Blocks.WATER;
            mc.player.jumpMovementFactor = 0.0F;
            float yPort = MoveUtil.getMotion() > 0.1D ? 0.02F : 0.032F;
            mc.player.setVelocity(mc.player.motion.x, (double) mc.player.fallDistance < 3.5D ? (double) (isUp ? yPort : -yPort) : -0.1D, mc.player.motion.z);
        }
    }

    private void handleAirMovement(EventMotion motion) {
        double posY = mc.player.getPosY();
        if (posY > (double) ((int) posY) + 0.89D && posY <= (double) ((int) posY + 1) || (double) mc.player.fallDistance > 3.5D) {
            mc.player.getPositionVec().y = ((double) ((int) posY + 1) + 1.0E-45D);
            if (!mc.player.isInWater()) {
                BlockPos waterBlockPos = new BlockPos(mc.player.getPosX(), mc.player.getPosY() - 0.1D, mc.player.getPosZ());
                Block waterBlock = mc.world.getBlockState(waterBlockPos).getBlock();
                if (waterBlock == Blocks.WATER) {
                    movementInWater(motion);
                }
            }
        }
    }

    private void movementInWater(EventMotion motion) {
        motion.setOnGround(false);
        handleCollisionJump();
        if (ticks == 1) {
            MoveUtil.setMotion(1.1f);
            ticks = 0;
        } else {
            ticks = 1;
        }
    }

    private void handleCollisionJump() {
        if (this.mc.player.collidedHorizontally && !noJump.get()) {
            this.mc.player.motion.y = 0.2D;
            mc.player.motion.x *= 0.0D;
            mc.player.motion.z *= 0.0D;
        }
    }
}

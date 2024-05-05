package club.cloverhook.cheat.impl.movement;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.utils.Stopwatch;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;

public class Jesus extends Cheat {

    private Stopwatch timer = new Stopwatch();
    int stage;

    public Jesus() {
        super("Jesus", "The holy lord him fucking self.", CheatCategory.MOVEMENT);
    }

    @Collect
    public void onUpdate(PlayerUpdateEvent e) {
        if(e.isPre()) {
            if (mc.thePlayer.isInWater() && !mc.thePlayer.isSneaking() && shouldJesus()) {
                mc.thePlayer.motionY = 0.09;
            }
            if (mc.thePlayer.onGround && !mc.thePlayer.isInWater() && shouldJesus()) {
                stage = 1;
                timer.reset();
            }
            if (stage > 0 && !timer.hasPassed(2500)) {

                if ((mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround || mc.thePlayer.isSneaking())) {
                    stage = -1;
                }
                mc.thePlayer.motionX *= 0;
                mc.thePlayer.motionZ *= 0;
                if (!mc.thePlayer.isInWater()) {
                    mc.thePlayer.setMotion(0.25 + mc.thePlayer.getSpeedEffect() * 0.05);
                }
                double motionY = getMotionY(stage);
                if (motionY != -999) {
                    mc.thePlayer.motionY = motionY;

                }

                stage += 1;
            }
        }
    }
    private boolean shouldJesus() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        ArrayList<BlockPos> pos = new ArrayList<>(Arrays.asList(new BlockPos(x + 0.3, y, z + 0.3),
                new BlockPos(x - 0.3, y, z + 0.3), new BlockPos(x + 0.3, y, z - 0.3), new BlockPos(x - 0.3, y, z - 0.3)));
        for (BlockPos poss : pos) {
            if (!(mc.theWorld.getBlockState(poss).getBlock() instanceof BlockLiquid))
                continue;
            if (mc.theWorld.getBlockState(poss).getProperties().get(BlockLiquid.LEVEL) instanceof Integer) {
                if ((int) mc.theWorld.getBlockState(poss).getProperties().get(BlockLiquid.LEVEL) <= 4) {
                    return true;
                }
            }
        }


        return false;
    }

    private double getMotionY(int stage) {
        //modified from longjump
        stage--;
        double[] motion = new double[]{0.500, 0.484, 0.468, 0.436, 0.404, 0.372, 0.340, 0.308, 0.276, 0.244, 0.212, 0.180, 0.166, 0.166, 0.156, 0.123, 0.135, 0.111, 0.086, 0.098, 0.073, 0.048, 0.06, 0.036, 0.0106, 0.015, 0.004, 0.004, 0.004, 0.004, -0.013, -0.045, -0.077, -0.109};
        if (stage < motion.length && stage >= 0)
            return motion[stage];
        else
            return -999;
    }

}

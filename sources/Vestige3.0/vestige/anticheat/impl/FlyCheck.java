package vestige.anticheat.impl;

import net.minecraft.entity.player.EntityPlayer;
import vestige.anticheat.ACPlayer;
import vestige.anticheat.AnticheatCheck;
import vestige.anticheat.PlayerData;

public class FlyCheck extends AnticheatCheck {

    public FlyCheck(ACPlayer player) {
        super("Fly", player);
    }

    @Override
    public void check() {
        PlayerData data = player.getData();
        EntityPlayer entity = player.getEntity();

        boolean closeToGround = data.isCloseToGround() || data.getCloseToGroundTicks() <= 2;

        boolean exempt = data.getDist() < 0.2 || entity.ticksExisted < 50 || entity.hurtTime >= 4 || mc.thePlayer.getDistanceToEntity(entity) > 20 || player.isBot() || entity.isInvisible() || entity.isInvisibleToPlayer(mc.thePlayer);

        if(!closeToGround && !exempt) {
            double motionY = data.getMotionY();
            double lastMotionY = data.getLastMotionY();

            double predictedMotionY = (lastMotionY - 0.08) * 0.98F;

            if(Math.abs(predictedMotionY) < 0.005) {
                predictedMotionY = 0;
            }

            double exceed = motionY - predictedMotionY;

            if(exceed > 0.01) {
                if(this.increaseBufferBy(exceed) > 0.2) {
                    this.alert("Exceed : " + exceed);
                }
            }
        }
    }
}
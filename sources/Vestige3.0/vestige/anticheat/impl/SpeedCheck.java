package vestige.anticheat.impl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockSlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import vestige.anticheat.ACPlayer;
import vestige.anticheat.AnticheatCheck;
import vestige.anticheat.PlayerData;

public class SpeedCheck extends AnticheatCheck {

    private float lastFriction;

    private boolean wasSneaking;

    public SpeedCheck(ACPlayer player) {
        super("Speed", player);
    }

    @Override
    public void check() {
        PlayerData data = player.getData();
        EntityPlayer entity = player.getEntity();

        boolean sprinting = data.isSprinting();
        boolean sneaking = data.isSneaking();

        boolean onGround = data.isLastGround();

        boolean exempt = data.getDist() < 0.22 || entity.ticksExisted < 50 || entity.hurtTime >= 4 || mc.thePlayer.getDistanceToEntity(entity) > 20 || player.isBot();

        double predictedDist = data.getLastDist();

        if(wasSneaking || sneaking) {
            predictedDist = Math.max(predictedDist, 0.055);
        }

        float friction = 0.91F;

        if(onGround) {
            friction = (float) (getFriction() * 0.91);
        }

        float f = 0.16277136F / (friction * friction * friction);
        float attributeSpeed;

        if(onGround) {
            float speed = sprinting ? getWalkSpeed(entity) * 1.3F : getWalkSpeed(entity);

            attributeSpeed = speed * f;
        } else {
            attributeSpeed = sprinting ? 0.026F : 0.02F;
        }

        predictedDist *= lastFriction;

        if(!data.isOnGround() && data.isLastGround() && data.getMotionY() >= 0) {
            if (sprinting) {
                predictedDist += 0.2;
            }
        }

        if(sneaking) {
            attributeSpeed *= 0.3;
        }

        if(entity.isUsingItem()) {
            attributeSpeed *= 0.2;
        }

        predictedDist += attributeSpeed;

        double dist = data.getDist();

        double exceed = dist - predictedDist;

        double maxExceed = 0.02;

        if(sneaking) {
            maxExceed += 0.005;
        }

        if(!exempt) {
            if(exceed > maxExceed && dist > getWalkSpeed(entity)) {
                if(this.increaseBuffer() >= 4.5) {
                    this.alert("Predicted dist : " + round(predictedDist) + " | Dist : " + round(dist));
                }
            } else {
                this.decreaseBufferBy(0.1);
            }
        }

        lastFriction = friction;
        wasSneaking = sneaking;
    }

    private float getWalkSpeed(EntityPlayer entity) {
        return 0.1F + getSpeedBoost(entity) * 0.02F;
    }

    private int getSpeedBoost(EntityPlayer entity) {
        if(entity.isPotionActive(Potion.moveSpeed)) {
            return entity.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }

        return 0;
    }

    private float getFriction() {
        PlayerData data = player.getData();

        Block block = mc.theWorld.getBlockState(new BlockPos(data.getX(), Math.floor(data.getY()) - 1, data.getZ())).getBlock();

        if(block != null) {
            if(block instanceof BlockIce || block instanceof BlockPackedIce) {
                return 0.98F;
            } else if(block instanceof BlockSlime) {
                return 0.8F;
            }
        }

        return 0.6F;
    }

}
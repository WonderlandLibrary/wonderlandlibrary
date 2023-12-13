package me.earth.phobos.manager;

import me.earth.phobos.features.Feature;
import me.earth.phobos.features.modules.client.Managers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;

public class SpeedManager
        extends Feature {
    public static final double LAST_JUMP_INFO_DURATION_DEFAULT = 3.0;
    public static boolean didJumpThisTick = false;
    public static boolean isJumping = false;
    public double firstJumpSpeed = 0.0;
    public double lastJumpSpeed = 0.0;
    public double percentJumpSpeedChanged = 0.0;
    public double jumpSpeedChanged = 0.0;
    public boolean didJumpLastTick = false;
    public long jumpInfoStartTime = 0L;
    public boolean wasFirstJump = true;
    public double speedometerCurrentSpeed = 0.0;
    public HashMap<EntityPlayer, Double> playerSpeeds = new HashMap();
    private final int distancer = 20;

    public static void setDidJumpThisTick(boolean val) {
        didJumpThisTick = val;
    }

    public static void setIsJumping(boolean val) {
        isJumping = val;
    }

    public float lastJumpInfoTimeRemaining() {
        return (float) (Minecraft.getSystemTime() - this.jumpInfoStartTime) / 1000.0f;
    }

    public void updateValues() {
        double distTraveledLastTickX = SpeedManager.mc.player.posX - SpeedManager.mc.player.prevPosX;
        double distTraveledLastTickZ = SpeedManager.mc.player.posZ - SpeedManager.mc.player.prevPosZ;
        this.speedometerCurrentSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
        if (didJumpThisTick && (!SpeedManager.mc.player.onGround || isJumping)) {
            if (didJumpThisTick && !this.didJumpLastTick) {
                this.wasFirstJump = this.lastJumpSpeed == 0.0;
                this.percentJumpSpeedChanged = this.speedometerCurrentSpeed != 0.0 ? this.speedometerCurrentSpeed / this.lastJumpSpeed - 1.0 : -1.0;
                this.jumpSpeedChanged = this.speedometerCurrentSpeed - this.lastJumpSpeed;
                this.jumpInfoStartTime = Minecraft.getSystemTime();
                this.lastJumpSpeed = this.speedometerCurrentSpeed;
                this.firstJumpSpeed = this.wasFirstJump ? this.lastJumpSpeed : 0.0;
            }
            this.didJumpLastTick = didJumpThisTick;
        } else {
            this.didJumpLastTick = false;
            this.lastJumpSpeed = 0.0;
        }
        if (Managers.getInstance().speed.getValue().booleanValue()) {
            this.updatePlayers();
        }
    }

    public void updatePlayers() {
        for (EntityPlayer player : SpeedManager.mc.world.playerEntities) {
            if (!(SpeedManager.mc.player.getDistanceSq(player) < (double) (this.distancer * this.distancer)))
                continue;
            double distTraveledLastTickX = player.posX - player.prevPosX;
            double distTraveledLastTickZ = player.posZ - player.prevPosZ;
            double playerSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
            this.playerSpeeds.put(player, playerSpeed);
        }
    }

    public double getPlayerSpeed(EntityPlayer player) {
        if (this.playerSpeeds.get(player) == null) {
            return 0.0;
        }
        return this.turnIntoKpH(this.playerSpeeds.get(player));
    }

    public double turnIntoKpH(double input) {
        return (double) MathHelper.sqrt(input) * 71.2729367892;
    }

    public double getSpeedKpH() {
        double speedometerkphdouble = this.turnIntoKpH(this.speedometerCurrentSpeed);
        speedometerkphdouble = (double) Math.round(10.0 * speedometerkphdouble) / 10.0;
        return speedometerkphdouble;
    }

    public double getSpeedMpS() {
        double speedometerMpsdouble = this.turnIntoKpH(this.speedometerCurrentSpeed) / 3.6;
        speedometerMpsdouble = (double) Math.round(10.0 * speedometerMpsdouble) / 10.0;
        return speedometerMpsdouble;
    }
}

